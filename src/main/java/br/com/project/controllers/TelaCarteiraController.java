package br.com.project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.models.PortifolioModel;
import br.com.project.utils.Functions;
import br.com.project.utils.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TelaCarteiraController implements Initializable, IController {
	
	Stage stage;
	
	@FXML
	AnchorPane mainPane;
	
	@FXML
	private WebView widgetCoinMarket;

	private PortifolioModel Portifolio;
	
	public TelaCarteiraController (PortifolioModel Portifolio) {
		this.Portifolio = Portifolio;
	}
	
	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		String html = "<html style=\"background-color:#3e3e3e;\"><script type=\"text/javascript\" src=\"https://files.coinmarketcap.com/static/widget/coinMarquee.js\"></script><div id=\"coinmarketcap-widget-marquee\" coins=\"1,1027,825,2010,52,6636,3718,1839,4687\" currency=\"BRL\" theme=\"dark\" transparent=\"true\" show-symbol-logo=\"true\"></div></html>";
		widgetCoinMarket.getEngine().loadContent(html);
	}
	
	@FXML
	public void telaMoedas() {
		TelaProcuraMoedaController controller = new TelaProcuraMoedaController();
		Window owner = mainPane.getScene().getWindow();
		
		try {
			Stage stage = Functions.handleNewWindow("telaProcuraMoeda", "Pesquisar moedas", controller, owner);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void telaCompraVenda() {
		TelaCompraVendaController controller = new TelaCompraVendaController();
		Window owner = mainPane.getScene().getWindow();

		try {
			Stage stage = Functions.handleNewWindow("telaCompraVenda", "Comprar e vender", controller, owner);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void telaAddAporte() {
		TelaAporteController controller = new TelaAporteController("");
		Window owner = mainPane.getScene().getWindow();

		try {
			Stage stage = Functions.handleNewWindow("telaAporte", "Adicionar aporte", controller, owner);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
