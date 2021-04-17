package br.com.project.crypto_portfolio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bson.Document;

import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import br.com.project.models.TickerStreamModel;
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
		scene = new Scene(_loadFXML("testeTelaInicial"));
		stage.setScene(scene);
		stage.show();

		try {
			getApiTest();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// cria o Mongo client
		MongoClient mongo = MongoClients.create("mongodb://localhost:27017");

		// cria as Credenciais
		MongoCredential credential;
		credential = MongoCredential.createCredential("userTeste", "dbTeste", "password".toCharArray());
		System.out.println("conectado com sucesso " + credential);

		// acessa o banco de dados
		MongoDatabase database = mongo.getDatabase("dbTeste");

		// recupera uma coleção
		MongoCollection<Document> collection = database.getCollection("exColl");
		System.out.println("coleção selecionada com sucesso " + collection.countDocuments());
//		Document document1 = new Document("title", "MongoDB").append("description", "database").append("likes", 100)
//				.append("url", "http://www.tutorialspoint.com/mongodb/").append("by", "tutorials point");
//		Document document2 = new Document("title", "RethinkDB").append("description", "database").append("likes", 200)
//				.append("url", "http://www.tutorialspoint.com/rethinkdb/").append("by", "tutorials point");
//
//		ArrayList<Document> list = new ArrayList<Document>();
//		list.add(document1);
//		list.add(document2);
//		collection.insertMany(list);

		// pegar o objeto iteravel
		FindIterable<Document> iterDoc = collection.find();
		int i = 1;

		// pega o iterador
		MongoCursor<Document> it = iterDoc.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
			i++;
		}

		TickerStreamModel tsm = new TickerStreamModel();
		System.out.println(tsm.getLowPrice() + "aqui o viado");
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