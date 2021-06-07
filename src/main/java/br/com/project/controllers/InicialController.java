package br.com.project.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.components.PaneCrypto;
import br.com.project.components.PanePortfolio;
import br.com.project.controllers.CriarPortfolioController.IVoidCallback;
import br.com.project.crypto_portfolio.App;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.MultiTickerModel;
import br.com.project.models.MyClientEndpoint;
import br.com.project.models.TickerStreamModel;
import br.com.project.models.portfolio.PortfolioModel;
import br.com.project.utils.Functions;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class InicialController implements Initializable {
	static String uriWss = "wss://stream.binance.com:9443/ws/bnbusdt@ticker";
	static String uriWssAll = "wss://stream.binance.com:9443/ws/!ticker@arr";
	ObjectMapper objectMapper = new ObjectMapper();
	private final MongoConcretePortfolio mongo = new MongoConcretePortfolio();

	@FXML
	AnchorPane mainPane;

	@FXML
	VBox vBoxListCriptos;

	@FXML
	VBox vBoxListPortifolios;

	List<PortfolioModel> listPortfolios;

	MyClientEndpoint websocketClient;

	final IVoidCallback callbackUpdatePortfolios = new IVoidCallback() {
		@Override
		public void handleCallback() {
			_loadListPortfolios(true);
			_initWebsocket();
		}
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_loadListPortfolios();
		_initWebsocket();
	}

	@FXML
	private void addPortfolio(ActionEvent event) throws IOException {
		CriarPortfolioController controller = new CriarPortfolioController(callbackUpdatePortfolios);
		Window owner = mainPane.getScene().getWindow();
		Functions.handleNewWindow("telaCriaPortifolio", "Criar portfólio", controller, owner);
	}

	private void editPortfolio(PortfolioModel portfolio) throws IOException {
		CriarPortfolioController controller = new CriarPortfolioController(portfolio, callbackUpdatePortfolios);

		Window owner = mainPane.getScene().getWindow();
		Functions.handleNewWindow("telaCriaPortifolio", "Editar portfólio", controller, owner);
	}

	private void _initWebsocket() {
		if (websocketClient != null) {
			websocketClient.closeConnection();
			vBoxListCriptos.getChildren().clear();
		}
			
		List<String> symbols = _getSymbolsAllPortfolios();
		String urlStreams = _generateStringWssUrl(symbols);
		websocketClient = new MyClientEndpoint(URI.create(urlStreams));

		websocketClient.addMessageHandler(new MyClientEndpoint.MessageHandler() {
			public void handleMessage(String message) {
				MultiTickerModel ticker;
				try {
					ticker = objectMapper.readValue(message, MultiTickerModel.class);

					System.out.println("SYMBOL > " + ticker.getTicker().getSymbol() + " | "
							+ Functions.formatMoney(ticker.getTicker().getLastPrice()));

					_addPane(ticker.getTicker());

				} catch (Exception e) {
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
					customPane.editLbNameText(ticker.getSymbol());
					customPane.editLbPrice(ticker.getLastPrice());
					customPane.editLbPercent(ticker.getPriceChangePercent());
				} else {
					vBoxListCriptos.getChildren().add(pane);
				}
			} catch (Exception e) {
				System.out.println("Deu ERRO ESSA PORRA " + e);
			}
		});
	}

	public String _generateStringWssUrl(List<String> symbols) {
		String uriWssStreams = "wss://stream.binance.com:9443/stream?streams=";
		List<String> streams = new ArrayList<String>();
		for (String symbol : symbols) {
			streams.add(symbol + "@ticker");
		}

		for (int i = 0; i < streams.size(); i++) {
			uriWssStreams += streams.get(i);
			if (i != streams.size() - 1) {
				uriWssStreams += "/";
			}
		}

		System.out.println("URL > " + uriWssStreams);

		return uriWssStreams;
	}

	public void _loadListPortfolios(boolean refresh) {
		if (refresh)
			vBoxListPortifolios.getChildren().clear();
		_loadListPortfolios();
	}

	public void _loadListPortfolios() {

		try {
			listPortfolios = mongo.getAll();

			for (PortfolioModel portfolio : listPortfolios) {
				PanePortfolio pane = new PanePortfolio(portfolio);

				pane.addClickCallback(new PanePortfolio.ClickCallback() {
					public void handle(boolean isEdit) {
						try {
							if (isEdit) {
								editPortfolio(portfolio);
							} else {
								CarteiraController controller = new CarteiraController(portfolio);
								websocketClient.closeConnection();
								App.setRoot("telaCarteira", controller);
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

				pane.addClickDeleteCallback(new PanePortfolio.ClickDeleteCallback() {
					@Override
					public void handle() {
						callbackUpdatePortfolios.handleCallback();
					}
				});

				vBoxListPortifolios.getChildren().add(pane);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private List<String> _getSymbolsAllPortfolios() {
		List<String> symbols = new ArrayList<String>();

		for (PortfolioModel port : listPortfolios) {
			var list = port.unifiedSymbols();

			for (String symbol : list) {
				if (!symbols.contains(symbol)) {
					symbols.add(symbol.toLowerCase());
				}
			}
		}
		return symbols;
	}
}
