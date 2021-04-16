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

public class SecondaryController implements Initializable {
	static String uriWss = "wss://stream.binance.com:9443/ws/bnbusdt@ticker";

	@FXML
	private WebView browser;
	@FXML
	private ListView<?> listCriptos;
//	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String html = "<html><iframe src=\"https://br.widgets.investing.com/top-cryptocurrencies?theme=darkTheme&roundedCorners=true\" width=\"100%\" height=\"100%\" frameborder=\"0\" allowtransparency=\"true\" marginwidth=\"0\" marginheight=\"0\"></iframe><div class=\"poweredBy\" style=\"font-family: Arial, Helvetica, sans-serif;\">Desenvolvido por <a href=\"https://br.investing.com?utm_source=WMT&amp;utm_medium=referral&amp;utm_campaign=TOP_CRYPTOCURRENCIES&amp;utm_content=Footer%20Link\" target=\"_blank\" rel=\"nofollow\">Investing.com</a></div></html>";
		
		final WebEngine webEngine = browser.getEngine();

		webEngine.loadContent(html);
		

        _initWebsocket();

	}

    @FXML
    private void switchTopPrimary() throws IOException {
        App.setRoot("testeTelaInicial");
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
