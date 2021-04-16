package br.com.project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import br.com.project.models.CoinChangeModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CoinChosee implements Initializable {

	@FXML
	private VBox moedaEscolhaCard;

	@FXML
	private Label nomeMoedaCard;

	@FXML
	private ImageView moedaImagem;

	@FXML
	private Label minimaCard;

	@FXML
	private Label maximaCard;

	@FXML
	private Label nomeCard;

	@FXML
	private Label infoCard;

	@FXML
	private ScrollPane scroll;

	@FXML
	private GridPane grid;

	private ArrayList<CoinChangeModel> moedas = new ArrayList<>();

	private ArrayList<CoinChangeModel> getData() {
		ArrayList<CoinChangeModel> moedas = new ArrayList<>();

		CoinChangeModel moeda;

		for (int i = 0; i < 20; i++) {
			moeda = new CoinChangeModel();
			moeda.setNomeMoeda("teste");
			moeda.setMinima(10);
			moeda.setMaxima(2031);
			moeda.setImgMoeda("/img/9ab8ddc00d534ec22d05df7f140ccd2b.png");
			moedas.add(moeda);
		}

		return moedas;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		moedas.addAll(getData());
		int coluna = 0;
		int linha = 1;
		
		try {
			for (int i = 0; i < moedas.size(); i++) {
				FXMLLoader fxmloader = new FXMLLoader();
				fxmloader.setLocation(getClass().getResource("itemMoeda.fxml"));
				AnchorPane anchorPane = fxmloader.load();

				CoinController controle = fxmloader.getController();
				controle.setData(moedas.get(i));
				
				if (coluna == 3) {
					coluna = 0;
					linha++;
				}
				
				grid.add(anchorPane, coluna++, linha);
				GridPane.setMargin(anchorPane, new Insets(10));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
