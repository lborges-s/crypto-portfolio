package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.components.PaneCrypto;
import br.com.project.models.MultiTickerModel;
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
	static String uriWssStreams = "wss://stream.binance.com:9443/stream?streams=";
	static String uriWssAll = "wss://stream.binance.com:9443/ws/!ticker@arr";
	ObjectMapper objectMapper = new ObjectMapper();

	@FXML
	AnchorPane mainPane;

	@FXML
	VBox vBoxListCriptos;
	
	List<String> streams = new ArrayList<String>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		

		_generateStringWssUrl();
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
		MyClientEndpoint client = new MyClientEndpoint(URI.create(uriWssStreams));

		client.addMessageHandler(new MyClientEndpoint.MessageHandler() {
			public void handleMessage(String message) {

				TickerStreamModel ticker;
				MultiTickerModel ticker2;
				try {
//					System.out.println("Message " + message);

					// Convert a single object
					ticker2 = objectMapper.readValue(message, MultiTickerModel.class);

					// Convert a list object
//					List<TickerStreamModel> tickers = objectMapper.readValue(message,
//							new TypeReference<List<TickerStreamModel>>() {
//							});

//					tickers.sort(Comparator.comparing(TickerStreamModel::getLastPrice));

//					Collections.sort(tickers, new Comparator<TickerStreamModel>() {
//						  @Override
//						  public int compare(TickerStreamModel u1, TickerStreamModel u2) {
//						    return u2.getLastPrice().compareTo(u1.getLastPrice());
//						  }
//						});
//										
//					for (int i = 0; i < 10; i++) {
//						_addPane(tickers.get(i));
//					}
 
					System.out.println("SYMBOL > " + ticker2.getTicker().getSymbol() + " | "
							+ Functions.formatMoney(ticker2.getTicker().getLastPrice()));
//
					_addPane(ticker2.getTicker());

				}
//				catch (JsonMappingException e) {
//					e.printStackTrace();
//				} 
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void _addPane(TickerStreamModel ticker) {

		Platform.runLater(() -> {
			try {
				PaneCrypto pane = new PaneCrypto(ticker);

				int index = vBoxListCriptos.getChildren().indexOf(pane);
				if (index != -1) {
					PaneCrypto customPane = (PaneCrypto) vBoxListCriptos.getChildren().get(index);
					customPane.setLbNameText(ticker.getSymbol());
					customPane.setLbPrice(ticker.getLastPrice());
					customPane.setLbPercent(ticker.getPriceChangePercent());
				} else {
					vBoxListCriptos.getChildren().add(pane);
				}
			} catch (Exception e) {
				System.out.println("Deu ERRO ESSA PORRA " + e);
			}
		});

	}
	
	public String _generateStringWssUrl() {

		streams.add("bnbusdt@ticker");
		streams.add("btcusdt@ticker");
		streams.add("dogeusdt@ticker");
		streams.add("adausdt@ticker");
		streams.add("xrpusdt@ticker");
		
		for (int i = 0; i < streams.size(); i++) {
			uriWssStreams += streams.get(i);
			if(i != streams.size() - 1) {
				uriWssStreams += "/";	
			}
		}
		
		System.out.println("URL > " + uriWssStreams);
		
		return uriWssStreams;
	}

}
