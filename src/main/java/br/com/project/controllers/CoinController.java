package br.com.project.controllers;

import br.com.project.models.CoinChangeModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CoinController {

	@FXML
	private Label nomeCard;

	@FXML
	private Label nomeMoedaCard;

	@FXML
	private ImageView moedaImagem;

	public CoinChangeModel coin = new CoinChangeModel();

	public void setData(CoinChangeModel coin) {
		this.coin = coin;
		nomeMoedaCard.setText(coin.getNomeMoeda());
		nomeCard.setText(coin.getNomeMoeda());
		Image image = new Image(getClass().getResourceAsStream(coin.getImgMoeda()));
		moedaImagem.setImage(image);
	}

}
