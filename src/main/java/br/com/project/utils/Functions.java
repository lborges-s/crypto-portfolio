package br.com.project.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.project.crypto_portfolio.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Functions {
	static Locale locale = new Locale("en", "US");

	public static String formatMoney(String value) {

		NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		return format.format(Double.parseDouble(value));

	}
	
	public static String formatMoney(String value, int fractionDigits) {

		NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		format.setMaximumFractionDigits(fractionDigits);
		
		return format.format(Double.parseDouble(value));

	}

	public static Stage handleNewWindow(String nomeTela, String title, IController controller, Window owner)
			throws IOException {
		FXMLLoader loader = new FXMLLoader(App.class.getResource(nomeTela + ".fxml"));
		loader.setController(controller);
		Stage stage = new Stage();
		controller.setStage(stage);

		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.setTitle(title);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.centerOnScreen();
		stage.initOwner(owner);
		stage.setScene(scene);
		stage.setMinHeight(650);
		stage.setMinWidth(800);
		stage.show();
		return stage;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String generateStreamWssUrl(List<String> symbols) {
		String uriWssStreams = "wss://stream.binance.com:9443/stream?streams=";
		List<String> streams = new ArrayList<String>();
		for (String symbol : symbols) {
			streams.add(symbol.toLowerCase() + "@ticker");
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
}
