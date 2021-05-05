package br.com.project.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.utils.IController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TelaCarteiraController implements Initializable, IController {
	
	Stage stage;
	
	@FXML
	private WebView widgetCoinMarket;
	
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
}
