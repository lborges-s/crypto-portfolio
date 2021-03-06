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
	private Label txtVlrEmMoeda;
	@FXML
	private Label txtVlrLucroPerda;

	@FXML
	private BorderPane paneChart;
	@FXML
	private VBox vBoxHistorico;

	private PortfolioModel portfolio;

	MyClientEndpoint websocketClient;

	@FXML
	VBox vBoxListCriptos;

	@FXML
	ComboBox<Integer> comboBoxAno;

	LineChart<String, Number> lineChart;
	

	ObjectMapper objectMapper = new ObjectMapper();

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
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("M??s");

		lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.getStylesheets().add("@css/fullpackstyling.css");
		lineChart.setTitle("Movimenta????o da carteira");
		paneChart.setCenter(lineChart);

		loadInfos(false);
//		loadChart();
	}

	public void loadInfos(boolean isUpdate) {
		try {
			if (isUpdate)
				portfolio = mongo.getById(portfolio.getId());

			portfolio.listMoedas();
			_initWebsocketMoedas(isUpdate);
			_loadFields();
			initComboBox();
			loadChart();
			loadHistorico();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void _loadFields() {
		txtNomeCarteira.setText(portfolio.getNome());

//		txtVlrTotalCarteira.setText(Functions.formatMoney(portfolio.calcVlrTotalPortfolio()));

		txtVlrDisponivelCarteira.setText(Functions.formatMoney(portfolio.calcVlrDisponivelPortfolio()));

		double qtdMoeda = portfolio.calcQtdMoedas();
		txtQtdMoeda.setText(String.valueOf(qtdMoeda));
		
		double vlrMoedas = portfolio.calcVlrEmMoedas();
		
		if(vlrMoedas < 0 || qtdMoeda == 0) {
			txtVlrEmMoeda.setText(Functions.formatMoney(0));
		}else {
			txtVlrEmMoeda.setText(Functions.formatMoney(vlrMoedas));	
		}

		txtVlrLucroPerda.setText(Functions.formatMoney(portfolio.calcTotalProfit()));
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

	public void loadChart() {
		if (comboBoxAno.getItems().isEmpty())
			return;

		final int ano = comboBoxAno.getValue();

		var series = new XYChart.Series<String, Number>();
		series.getData().add(new XYChart.Data<String, Number>("Jan", portfolio.getVlrTotalTransactionsByDate(ano, 0)));
		series.getData().add(new XYChart.Data<String, Number>("Fev", portfolio.getVlrTotalTransactionsByDate(ano, 1)));
		series.getData().add(new XYChart.Data<String, Number>("Mar", portfolio.getVlrTotalTransactionsByDate(ano, 2)));
		series.getData().add(new XYChart.Data<String, Number>("Abr", portfolio.getVlrTotalTransactionsByDate(ano, 3)));
		series.getData().add(new XYChart.Data<String, Number>("Mai", portfolio.getVlrTotalTransactionsByDate(ano, 4)));
		series.getData().add(new XYChart.Data<String, Number>("Jun", portfolio.getVlrTotalTransactionsByDate(ano, 5)));
		series.getData().add(new XYChart.Data<String, Number>("Jul", portfolio.getVlrTotalTransactionsByDate(ano, 6)));
		series.getData().add(new XYChart.Data<String, Number>("Ago", portfolio.getVlrTotalTransactionsByDate(ano, 7)));
		series.getData().add(new XYChart.Data<String, Number>("Set", portfolio.getVlrTotalTransactionsByDate(ano, 8)));
		series.getData().add(new XYChart.Data<String, Number>("Out", portfolio.getVlrTotalTransactionsByDate(ano, 9)));
		series.getData().add(new XYChart.Data<String, Number>("Nov", portfolio.getVlrTotalTransactionsByDate(ano, 10)));
		series.getData().add(new XYChart.Data<String, Number>("Dez", portfolio.getVlrTotalTransactionsByDate(ano, 11)));

		lineChart.getData().clear();
		lineChart.getData().add(series);
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
			if (isUpdate) {
				websocketClient.closeConnection();				
			}
			vBoxListCriptos.getChildren().clear();
		}

		List<String> symbols = portfolio.unifiedSymbols();
		List<CoinModel> coins = portfolio.listMoedas();

		if (!coins.isEmpty()) {

			String urlStreams = Functions.generateStreamWssUrl(symbols);
			websocketClient = new MyClientEndpoint(URI.create(urlStreams));

			websocketClient.addMessageHandler(new MyClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					MultiTickerModel multiTicker;
					try {
						multiTicker = objectMapper.readValue(message, MultiTickerModel.class);
						TickerStreamModel ticker = multiTicker.getTicker();
						var symbol = ticker.getSymbol();

//						System.out.println("SYMBOL > " + symbol + " | " + Functions.formatMoney(ticker.getLastPrice()));

						CoinModel cc = coins.stream().filter(coin -> symbol.equals(coin.getSymbol())).findAny()
								.orElse(null);

						if (cc.getTotalQtd() != 0) {

							var indexCoin = coins.indexOf(cc);
							if (indexCoin != -1) {
								var c = coins.get(indexCoin);

								var actualPrice = Double.parseDouble(ticker.getLastPrice());
								Platform.runLater(() -> {
									c.calcProfit(actualPrice);
									_loadFields();
									PaneMoeda pane = new PaneMoeda(c);

									int indexVBox = vBoxListCriptos.getChildren().indexOf(pane);
									if (indexVBox != -1) {
										PaneMoeda customPane = (PaneMoeda) vBoxListCriptos.getChildren().get(indexVBox);

										customPane.editLbNome(c.getSymbol());
										customPane.editPercent(c.getRentabilidade());
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
		if (!anos.isEmpty()) {
			comboBoxAno.getItems().clear();
			comboBoxAno.getItems().addAll(anos);
			comboBoxAno.setValue(anos.get(0));
		}
	}

	@FXML
	void onChangeComboBox(ActionEvent event) {
		loadChart();
	}

}
