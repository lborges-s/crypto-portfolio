package br.com.project.utils;

import java.io.IOException;
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

	public static String formatMoney(String value) {

		Locale brl = new Locale("pt", "BR");

//		Currency brls = Currency.getInstance(brl);

		NumberFormat brlFormat = NumberFormat.getCurrencyInstance(brl);

		return brlFormat.format(Double.parseDouble(value));

	}
	
	public static void handleNewWindow(String nomeTela, String title, Object controller, Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(nomeTela + ".fxml"));
        loader.setController(controller);
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);

        stage.setMinHeight(650);
        stage.setMinWidth(800);

        stage.show();
    }
	
}
