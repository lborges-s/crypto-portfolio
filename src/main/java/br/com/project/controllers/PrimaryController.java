package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.crypto_portfolio.App;
import br.com.project.models.MyClientEndpoint;
import br.com.project.models.TickerStreamModel;
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

	private ObjectMapper _objectMapper = new ObjectMapper();

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

		// Create a new Locale
		Locale brl = new Locale("pt", "BR");
		// Create a Currency instance for the Locale
		Currency brls = Currency.getInstance(brl);
		// Create a formatter given the Locale
		NumberFormat brlFormat = NumberFormat.getCurrencyInstance(brl);

		client.addMessageHandler(new MyClientEndpoint.MessageHandler() {
			public void handleMessage(String message) {

				try {
					TickerStreamModel ticker = _objectMapper.readValue(message, TickerStreamModel.class);

					Double.parseDouble(ticker.getLastPrice());
					
					System.out.println(message);

					System.out.println("SYMBOL     > " + ticker.getSymbol());
					System.out.println("LAST PRICE > " + brlFormat.format(Double.parseDouble(ticker.getLastPrice())));
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			}
		});
	}
}
