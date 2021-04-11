package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.crypto_portfolio.App;
import br.com.project.models.MyClientEndpoint;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class PrimaryController implements Initializable {
	static String uriWss = "wss://stream.binance.com:9443/ws/bnbusdt@ticker";

	@FXML
	private WebView browser;
	@FXML
	private ListView<?> listCriptos;
//	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String html = "<html><script type=\"text/javascript\" src=\"https://files.coinmarketcap.com/static/widget/coinMarquee.js\"></script><div id=\"coinmarketcap-widget-marquee\" coins=\"1,1027,825,2010,52,6636,3718,1839,4687\" currency=\"BRL\" theme=\"dark\" transparent=\"false\" show-symbol-logo=\"true\"></div></html>";
		
		final WebEngine webEngine = browser.getEngine();

		webEngine.loadContent(html);
		

        _initWebsocket();

	}

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    
	private void _initWebsocket() {
		MyClientEndpoint client = new MyClientEndpoint(URI.create(uriWss));

		client.addMessageHandler(new MyClientEndpoint.MessageHandler() {
			public void handleMessage(String message) {
				System.out.println("Mensagem: " + message);
				
//				Gson t = new Gson();
//				TickerStreamModel ticker = t.fromJson(message, TickerStreamModel.class);
//				var price = ticker.getLastPrice().toString();
				
			}
		});
	}
}
