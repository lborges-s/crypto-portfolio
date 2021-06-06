package br.com.project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.project.components.PaneHistorico;
import br.com.project.components.PaneMoeda;
import br.com.project.crypto_portfolio.App;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.HistoricoModel;
import br.com.project.models.portfolio.CoinModel;
import br.com.project.models.portfolio.PortfolioModel;
import br.com.project.utils.Functions;
import br.com.project.utils.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
	private Label txtQtdMoeda;
	@FXML
	private Label txtVlrPorMoeda;

	@FXML
	private BorderPane paneChart;
	@FXML
	private VBox vBoxHistorico;

	private PortfolioModel portfolio;

	@FXML
	VBox vBoxListCriptos;

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

//		Timer timer = new Timer();
//		int begin = 0;
//		int timeInterval = 1000;
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				Platform.runLater(() -> {
//					System.out.println("Call timer");
//					engine.loadContent(html);
//					engine.reload();
//				});
//				
//			}
//		}, begin, timeInterval);
		loadInfos(false);

		initChart();

	}

	@FXML
	public void telaMoedas() {
		ProcuraMoedaController controller = new ProcuraMoedaController();
		Window owner = mainPane.getScene().getWindow();

		try {
			Functions.handleNewWindow("telaProcuraMoeda", "Pesquisar moedas", controller, owner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void telaCompraVenda() {
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
	public void telaAddAporte() {
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

	public void loadInfos(boolean isUpdate) {
		try {
			if (isUpdate)
				portfolio = mongo.getById(portfolio.getId());

			txtNomeCarteira.setText(portfolio.getNome());
			txtVlrTotalCarteira.setText(Functions.formatMoney(String.valueOf(portfolio.calcVlrTotalPortfolio())));
			txtQtdMoeda.setText(String.valueOf(portfolio.calcQtdMoedas()));
			txtVlrPorMoeda.setText(Functions.formatMoney(String.valueOf(portfolio.calcVlrEmMoedas())));

			loadHistorico();
			_loadListMoedas();

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
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

		lineChart.setTitle("Movimentação da carteira");

		var series = new XYChart.Series<String, Number>();

		series.getData().add(new XYChart.Data<String, Number>("Jan", 23));
		series.getData().add(new XYChart.Data<String, Number>("Feb", 14));
		series.getData().add(new XYChart.Data<String, Number>("Mar", 15));
		series.getData().add(new XYChart.Data<String, Number>("Apr", 24));
		series.getData().add(new XYChart.Data<String, Number>("May", 34));
		series.getData().add(new XYChart.Data<String, Number>("Jun", 36));
		series.getData().add(new XYChart.Data<String, Number>("Jul", 22));
		series.getData().add(new XYChart.Data<String, Number>("Aug", 45));
		series.getData().add(new XYChart.Data<String, Number>("Sep", 43));
		series.getData().add(new XYChart.Data<String, Number>("Oct", 17));
		series.getData().add(new XYChart.Data<String, Number>("Nov", 29));
		series.getData().add(new XYChart.Data<String, Number>("Dec", 25));

		lineChart.getData().add(series);

		paneChart.setCenter(lineChart);

	}

	public void _loadListMoedas() {
		vBoxListCriptos.getChildren().clear();
		try {
			for (CoinModel transacao : portfolio.listMoedas()) {
				PaneMoeda pane = new PaneMoeda(transacao);

				vBoxListCriptos.getChildren().add(pane);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@FXML
	void backScreen(ActionEvent event) {
		try {
			App.setRoot("telaInicial");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
