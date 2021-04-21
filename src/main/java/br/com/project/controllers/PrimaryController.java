package br.com.project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.project.crypto_portfolio.App;
import br.com.project.utils.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Window;

public class PrimaryController implements Initializable {


	@FXML
	private WebView browser;


//	@FXML
//	private WebView browser2;
//	
//
//	@FXML
//	private WebView browser3;
	@FXML
	private ListView<?> listCriptos;
	
	@FXML
	private AnchorPane mainPane;

//	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
//		String html = "<html><script type=\"text/javascript\" src=\"https://files.coinmarketcap.com/static/widget/coinMarquee.js\"></script><div id=\"coinmarketcap-widget-marquee\" coins=\"1,1027,825,2010,52,6636,3718,1839,4687\" currency=\"BRL\" theme=\"dark\" transparent=\"false\" show-symbol-logo=\"true\"></div></html>";

//		String html2 = "<html><iframe src=\"https://br.widgets.investing.com/crypto-currency-rates?theme=darkTheme&pairs=945629,997650,1001803,1010773,940810,1010776,1031068,1014071,1010883,1010785\" width=\"100%\" height=\"600\" frameborder=\"0\" allowtransparency=\"true\" marginwidth=\"0\" marginheight=\"0\"></iframe><div class=\"poweredBy\" style=\"font-family: Arial, Helvetica, sans-serif;\"></div></html>";
//
//		String html3 = "<html><iframe src=\"https://br.widgets.investing.com/crypto-currency-rates?theme=darkTheme&pairs=945629,997650,1001803,1010773,940810,1010776\" width=\"100%\" height=\"600\" frameborder=\"0\" allowtransparency=\"true\" marginwidth=\"0\" marginheight=\"0\"></iframe><div class=\"poweredBy\" style=\"font-family: Arial, Helvetica, sans-serif;\"></div></html>";

//		final WebEngine webEngine = 
//		browser.getEngine().loadContent(html);
//		final WebEngine webEngine2 = browser2.getEngine();
//		final WebEngine webEngine3 = browser3.getEngine();

//		webEngine.loadContent(html);
//		webEngine2.loadContent(html2);
//		webEngine3.loadContent(html3);

		_initMongo();
	}

	@FXML
	private void muda(ActionEvent event) throws IOException {
		
		FormPortfolioController controller = new FormPortfolioController("Inicializado");
        Window owner = mainPane.getScene().getWindow();

        Functions.handleNewWindow("telaCriaPortifolio", "Criar portifólio", controller, owner);
	}
	
	@FXML
	private void switchToSecondary() throws IOException {
		App.setRoot("secondary");
	}

	

	public void _initMongo() {
		MongoClient mongo = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = mongo.getDatabase("dbTeste");

		try {
			MongoCollection<Document> collection = database.getCollection("exColl");
			System.out.println("coleção selecionada com sucesso " + collection.countDocuments());
		} catch (Exception e) {
			System.out.println("DEU PAU");
		}

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
//		FindIterable<Document> iterDoc = collection.find();
//		int i = 1;
//
//		// pega o iterador
//		MongoCursor<Document> it = iterDoc.iterator();
//		while (it.hasNext()) {
//			System.out.println(it.next());
//			i++;
//		}
	}

}
