package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.components.PaneCrypto;
import br.com.project.models.MyClientEndpoint;
import br.com.project.models.TickerStreamModel;
import br.com.project.utils.Functions;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class TelaInicialController implements Initializable {
	static String uriWss = "wss://stream.binance.com:9443/ws/bnbusdt@ticker";
	ObjectMapper objectMapper = new ObjectMapper();

	@FXML
	AnchorPane mainPane;

	@FXML
	VBox vBoxListCriptos;

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

					_addPane(ticker);

				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void _addPane(TickerStreamModel ticker) {

		Platform.runLater(() -> {
			try {
				PaneCrypto pane = new PaneCrypto(ticker);
				
				boolean contains = vBoxListCriptos.getChildren().contains(pane);
				if(contains) {
					vBoxListCriptos.getChildren().remove(pane);
				}
				vBoxListCriptos.getChildren().add(pane);
				System.out.println("childrens > " + vBoxListCriptos.getChildren().size());
			} catch (Exception e) {
				System.out.println("Deu ERRO ESSA PORRA " + e);
			}
		});

	}

}
