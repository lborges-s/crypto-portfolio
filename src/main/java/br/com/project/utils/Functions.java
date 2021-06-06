package br.com.project.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.project.crypto_portfolio.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Functions {
	static Locale locale = new Locale("pt", "BR");

	public static String formatMoney(String value, Locale loc) {
		locale = loc;

		return formatMoney(value);
	}

	public static String formatMoney(String value) {

		NumberFormat brlFormat = NumberFormat.getCurrencyInstance(locale);

		return brlFormat.format(Double.parseDouble(value));

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
}
