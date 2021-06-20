package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.components.PaneHistorico;
import br.com.project.components.PaneMoeda;
import br.com.project.crypto_portfolio.App;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.HistoricoModel;
import br.com.project.models.MultiTickerModel;
import br.com.project.models.MyClientEndpoint;
import br.com.project.models.TickerStreamModel;
import br.com.project.models.portfolio.CoinModel;
import br.com.project.models.portfolio.PortfolioModel;
import br.com.project.utils.Functions;
import br.com.project.utils.IController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CarteiraController implements Initializable, IController {
	private final MongoConcretePortfolio mongo = new MongoConcretePortfolio();
	Stage stage;
	@FXML
	AnchorPane mainPane;
	@FXML
	private WebView widgetCoinMarket;
	@FXML
	private Label txtNomeCarteira;
	@FXML
	private Label txtVlrTotalCarteira;
	@FXML
	private Label txtVlrDisponivelCarteira;
	@FXML
	private Label txtQtdMoeda;
	@FXML
	private Label txtVlrPorMoeda;

	@FXML
	private BorderPane paneChart;
	@FXML
	private VBox vBoxHistorico;

	private PortfolioModel portfolio;

	MyClientEndpoint websocketClient;

	@FXML
	VBox vBoxListCriptos;

	@FXML
	ComboBox<String> comboBoxAno;

	public CarteiraController(PortfolioModel portfolio) {
		this.portfolio = portfolio;
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String html = "<html style=\"background-color:#3e3e3e;\"><script type=\"text/javascript\" src=\"https://files.coinmarketcap.com/static/widget/coinMarquee.js\"></script><div id=\"coinmarketcap-widget-marquee\" coins=\"1,1027,825,2010,52,6636,3718,1839,4687\" currency=\"BRL\" theme=\"dark\" transparent=\"true\" show-symbol-logo=\"true\"></div></html>";
		var engine = widgetCoinMarket.getEngine();
		engine.loadContent(html);

		vBoxListCriptos.getChildren().clear();
		loadInfos(false);

		initComboBox();
		initChart();
	}

	public void loadInfos(boolean isUpdate) {
		try {
			if (isUpdate)
				portfolio = mongo.getById(portfolio.getId());

			portfolio.listMoedas();
			_loadFields();
			
			loadHistorico();
			_initWebsocketMoedas(isUpdate);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void _loadFields() {
		txtNomeCarteira.setText(portfolio.getNome());
		txtVlrTotalCarteira.setText(Functions.formatMoney(String.valueOf(portfolio.calcVlrTotalPortfolio())));

		txtVlrDisponivelCarteira
				.setText(Functions.formatMoney(String.valueOf(portfolio.calcVlrDisponivelPortfolio())));
		txtQtdMoeda.setText(String.valueOf(portfolio.calcQtdMoedas()));
		txtVlrPorMoeda.setText(Functions.formatMoney(String.valueOf(portfolio.calcVlrEmMoedasDisplay())));

	}

	private void loadHistorico() {
		vBoxHistorico.getChildren().clear();
		List<PaneHistorico> listPanes = new ArrayList<PaneHistorico>();
		for (HistoricoModel h : portfolio.historico()) {
			final PaneHistorico pane = new PaneHistorico(h);
			listPanes.add(pane);
		}
		Collections.reverse(listPanes);

		vBoxHistorico.getChildren().addAll(listPanes);

	}

	public void initChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Mês");

		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.getStylesheets().add("@css/fullpackstyling.css");
		lineChart.setTitle("Movimentação da carteira");

		var series = new XYChart.Series<String, Number>();
		series.getData().add(new XYChart.Data<String, Number>("Jan", 10));
		series.getData().add(new XYChart.Data<String, Number>("Fev", 0));
		series.getData().add(new XYChart.Data<String, Number>("Mar", 0));
		series.getData().add(new XYChart.Data<String, Number>("Abr", 0));
		series.getData().add(new XYChart.Data<String, Number>("Mai", 0));
		series.getData().add(new XYChart.Data<String, Number>("Jun", 0));
		series.getData().add(new XYChart.Data<String, Number>("Jul", 0));
		series.getData().add(new XYChart.Data<String, Number>("Ago", 0));
		series.getData().add(new XYChart.Data<String, Number>("Set", 0));
		series.getData().add(new XYChart.Data<String, Number>("Out", 0));
		series.getData().add(new XYChart.Data<String, Number>("Nov", 0));
		series.getData().add(new XYChart.Data<String, Number>("Dez", 0));

		lineChart.getData().add(series);
		paneChart.setCenter(lineChart);
	}

	public void _loadListMoedas() {
		vBoxListCriptos.getChildren().clear();
		try {
			for (CoinModel coin : portfolio.listMoedas()) {
				PaneMoeda pane = new PaneMoeda(coin);

				vBoxListCriptos.getChildren().add(pane);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void _initWebsocketMoedas(boolean isUpdate) {
		if (websocketClient != null) {
			if (isUpdate)
				websocketClient.closeConnection();
			vBoxListCriptos.getChildren().clear();
		}
		ObjectMapper objectMapper = new ObjectMapper();

		List<String> symbols = portfolio.unifiedSymbols();
		List<CoinModel> coins = portfolio.listMoedas();

		String urlStreams = Functions.generateStreamWssUrl(symbols);
		websocketClient = new MyClientEndpoint(URI.create(urlStreams));

		websocketClient.addMessageHandler(new MyClientEndpoint.MessageHandler() {
			public void handleMessage(String message) {
				MultiTickerModel multiTicker;
				try {
					multiTicker = objectMapper.readValue(message, MultiTickerModel.class);
					TickerStreamModel ticker = multiTicker.getTicker();
					var symbol = ticker.getSymbol();

					System.out.println("SYMBOL > " + symbol + " | " + Functions.formatMoney(ticker.getLastPrice()));

					CoinModel cc = coins.stream().filter(coin -> symbol.equals(coin.getSymbol())).findAny()
							.orElse(null);

					var indexCoin = coins.indexOf(cc);
					if (indexCoin != -1) {
						var c = coins.get(indexCoin);						

						if (c.getTotalQtd() > 0) {
							var actualPrice = Double.parseDouble(ticker.getLastPrice());
							Platform.runLater(() -> {
								c.calcPercentProfit(actualPrice);
								_loadFields();
								PaneMoeda pane = new PaneMoeda(c);

								int indexVBox = vBoxListCriptos.getChildren().indexOf(pane);
								if (indexVBox != -1) {
									PaneMoeda customPane = (PaneMoeda) vBoxListCriptos.getChildren().get(indexVBox);
									
									customPane.editLbNome(c.getSymbol());
									customPane.editPercent(c.getCurrentPercentProfit());
									customPane.editActualPrice(actualPrice);
									customPane.editTotalPrice(c.getTotalProfit());
								} else {
									vBoxListCriptos.getChildren().add(pane);
								}
							});
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	@FXML
	public void abrirTelaMoedas() {
		ProcuraMoedaController controller = new ProcuraMoedaController();
		Window owner = mainPane.getScene().getWindow();

		try {
			Functions.handleNewWindow("telaProcuraMoeda", "Pesquisar moedas", controller, owner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void abrirTelaCompraVenda() {
		TransacaoController.IVoidCallback callbackIfOk = new TransacaoController.IVoidCallback() {
			@Override
			public void handleCallback() {
				loadInfos(true);
			}
		};

		TransacaoController controller = new TransacaoController(portfolio, callbackIfOk);
		Window owner = mainPane.getScene().getWindow();

		try {
			Functions.handleNewWindow("telaCompraVenda", "Comprar e vender", controller, owner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void abrirTelaAddAporte() {
		AporteController.IVoidCallback callbackIfOk = new AporteController.IVoidCallback() {
			@Override
			public void handleCallback() {
				loadInfos(true);
			}
		};

		AporteController controller = new AporteController(portfolio.getId(), callbackIfOk);
		Window owner = mainPane.getScene().getWindow();

		try {
			Functions.handleNewWindow("telaAporte", "Adicionar aporte", controller, owner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void backScreen(ActionEvent event) {
		try {
			App.setRoot("telaInicial");
			websocketClient.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initComboBox() {
		var anos = portfolio.anosDiferentes();
		comboBoxAno.getItems().addAll(anos);
	}
}
