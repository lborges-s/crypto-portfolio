package br.com.project.crypto_portfolio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.models.BinanceModel;
import br.com.project.utils.Functions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Scene scene;
	private static String baseUrl = "https://api.binance.com/api/v3/ticker/24hr?symbol=BNBUSDT";
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void start(Stage stage) throws IOException {
		

		scene = new Scene(_loadFXML("testeTelaInicial"));
		stage.setScene(scene);
		stage.show();

		stage.setMinHeight(650);
		stage.setMinWidth(800);

		try {
			getApiTest();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	public static void setRoot(String fxml) throws IOException {
		scene.setRoot(_loadFXML(fxml));
	}

	private static Parent _loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	private void getApiTest()
			throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException {
		final HttpClient client = HttpClient.newBuilder().build();
		URI uri = URI.create(baseUrl);

		HttpRequest req = HttpRequest.newBuilder(uri).GET().build();

		CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
		response.thenAcceptAsync(res -> System.out.println(res));

		if (response.get().statusCode() == 200) {
			String body = response.get().body();
			BinanceModel model = objectMapper.readValue(body, BinanceModel.class);
			System.out.println("---------- API --------- ");
			System.out.println("SYMBOL > " + model.getSymbol());
			System.out.println("LAST PRICE > " + Functions.formatMoney(model.getLastPrice()));
			System.out.println("------------------------ ");
		}

	}

	public static void main(String[] args) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.getLogger("org.mongodb.driver").setLevel(Level.ERROR);
		launch();
	}

}