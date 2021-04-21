package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.models.MyClientEndpoint;
import br.com.project.models.TickerStreamModel;
import br.com.project.utils.Functions;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class TelaInicialController implements Initializable {
	static String uriWss = "wss://stream.binance.com:9443/ws/bnbusdt@ticker";
	ObjectMapper objectMapper = new ObjectMapper();

	@FXML
	AnchorPane mainPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		_initWebsocket();

	}

	@FXML
	private void handleNewWindow(ActionEvent event) throws IOException {
		FormPortfolioController controller = new FormPortfolioController("Carmela é lindo");
		Window owner = mainPane.getScene().getWindow();

		Stage stage = Functions.handleNewWindow("telaCriaPortifolio", "Criar portifólio", controller, owner);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				System.out.println("Fechando a tela");
				System.out.println("isRefresh > " + controller.isRefresh);

			}
		});
	}

	private void _initWebsocket() {
		MyClientEndpoint client = new MyClientEndpoint(URI.create(uriWss));

		client.addMessageHandler(new MyClientEndpoint.MessageHandler() {
			public void handleMessage(String message) {

				TickerStreamModel ticker;
				try {
					ticker = objectMapper.readValue(message, TickerStreamModel.class);
					System.out.println(
							"SYMBOL > " + ticker.getSymbol() + " | " + Functions.formatMoney(ticker.getLastPrice()));

				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			}
		});
	}

}
