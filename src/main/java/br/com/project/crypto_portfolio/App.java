package br.com.project.crypto_portfolio;

import java.io.IOException;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.utils.IController;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	private static Scene scene;
//	private static String baseUrl = "https://api.binance.com/api/v3/ticker/24hr?symbol=BNBUSDT";
	ObjectMapper objectMapper = new ObjectMapper();
	private static Stage stage;

	@Override
	public void start(Stage stage) throws IOException {
		App.stage = stage;

		scene = new Scene(_loadFXML("telaInicial").load());
		stage.setScene(scene);
		stage.show();

		stage.setMaximized(true);
		stage.setMinHeight(650);
		stage.setMinWidth(800);
		stage.setTitle("Ez Crypto");

//		try {
//			getApiTest();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}

	}

	public static void setRoot(String fxmlName) throws IOException {
		FXMLLoader fxml = _loadFXML(fxmlName);

		scene.setRoot(fxml.load());
	}

	public static void setRoot(String fxmlName, IController controller) throws IOException {
		FXMLLoader fxml = _loadFXML(fxmlName);
		controller.setStage(stage);
		fxml.setController(controller);
		scene.setRoot(fxml.load());
	}

	private static FXMLLoader _loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader;
	}

//	private void getApiTest()
//			throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {
//		final HttpClient client = HttpClient.newBuilder().build();
//		URI uri = URI.create(baseUrl);
//
//		HttpRequest req = HttpRequest.newBuilder(uri).GET().build();
//
//		CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
//		response.thenAcceptAsync(res -> System.out.println(res));
//
//		if (response.get().statusCode() == 200) {
//			String body = response.get().body();
//			BinanceModel model = objectMapper.readValue(body, BinanceModel.class);
//			System.out.println("---------- API --------- ");
//			System.out.println("SYMBOL > " + model.getSymbol());
//			System.out.println("LAST PRICE > " + Functions.formatMoney(model.getLastPrice()));
//			System.out.println("------------------------ ");
//		}
//
//	}

	public static void main(String[] args) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.getLogger("org.mongodb.driver").setLevel(Level.ERROR);
		launch();
	}

}