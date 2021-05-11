package br.com.project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.models.portfolio.PortfolioModel;
import br.com.project.utils.Functions;
import br.com.project.utils.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CarteiraController implements Initializable, IController {

	Stage stage;
	@FXML
	AnchorPane mainPane;
	@FXML
	private WebView widgetCoinMarket;
	@FXML
	private Label txtNomeCarteira;
	@FXML
	private Label txtVlrTotalCarteira;

	private PortfolioModel portfolio;

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
		widgetCoinMarket.getEngine().loadContent(html);
		
		txtNomeCarteira.setText(portfolio.getNome());
		txtVlrTotalCarteira.setText(Functions.formatMoney(String.valueOf(portfolio.calcVlrTotalAportes())));
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
		TransacaoController controller = new TransacaoController();
		Window owner = mainPane.getScene().getWindow();

		try {
			Functions.handleNewWindow("telaCompraVenda", "Comprar e vender", controller, owner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void telaAddAporte() {
		AporteController controller = new AporteController("");
		Window owner = mainPane.getScene().getWindow();

		try {
			Functions.handleNewWindow("telaAporte", "Adicionar aporte", controller, owner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
