package br.com.project.crypto_portfolio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
	private static String baseUrl = "https://api.binance.com/api/v3/ticker/24hr?symbol=BNBUSDT";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(_loadFXML("telaInicial"), 640, 480);
        stage.setScene(scene);
        stage.show();
        
        
        
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
    
    private void getApiTest() throws InterruptedException, ExecutionException {
		final HttpClient client = HttpClient.newBuilder().build();
		URI uri = URI.create(baseUrl);

		HttpRequest req = HttpRequest.newBuilder(uri).GET().build();

		CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
		response.thenAcceptAsync(res -> System.out.println(res));

		if (response.get().statusCode() == 200) {
			String body = response.get().body();
			System.out.println(body);
//			Gson t = new Gson();
//			BinanceModel model = t.fromJson(body, BinanceModel.class);
//
//			System.out.println("Response > " + model.getHighPrice());

		}

	}

    public static void main(String[] args) {
        launch();
    }

}