package br.com.project.components;

import br.com.project.crypto_portfolio.App;
import br.com.project.models.portfolio.CoinModel;
import br.com.project.utils.Functions;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.EqualsAndHashCode;

public class PaneMoeda extends Pane {
	@EqualsAndHashCode.Exclude
	private Label lbNome = new Label();
	
	@EqualsAndHashCode.Exclude
	private Label lbPrecoAtual = new Label();
	
	@EqualsAndHashCode.Exclude
	private Label lbPorcentagem = new Label();
	
	@EqualsAndHashCode.Exclude
	private Label lbPriceAtualMoeda = new Label();
	
	@EqualsAndHashCode.Exclude
	private ImageView imageView = new ImageView();
	
	@FXML
	AnchorPane mainPane;
	
	public PaneMoeda (CoinModel coin) {
		
		String icon = "icon/plus.png";
		var img = new Image(App.class.getResourceAsStream(icon));
		
		imageView.setLayoutX(13.0);
		imageView.setLayoutY(13.0);
		imageView.setImage(img);

		lbNome.setText(coin.getSymbol());
		lbNome.setFont(Font.font(17));
		lbNome.setLayoutX(79.0);
		lbNome.setLayoutY(5.0);
		lbNome.setStyle("-fx-text-fill:   linear-gradient(to bottom , #ff9500, #fc466b);");
		lbNome.getStylesheets().add("@css/fullpackstyling.css");
		lbNome.getStyleClass().add("texto-degrade");
		
		lbPrecoAtual.setText(Functions.formatMoney(String.valueOf(coin.calcTotalPrice())));
		lbPrecoAtual.setFont(Font.font(17));
		lbPrecoAtual.setLayoutX(224.0);
		lbPrecoAtual.setLayoutY(5.0);
		lbPrecoAtual.setStyle("-fx-text-fill:   linear-gradient(to bottom , #ff9500, #fc466b);");
		lbPrecoAtual.getStylesheets().add("@css/fullpackstyling.css");
		lbPrecoAtual.getStyleClass().add("texto-degrade");
		
		var percent = coin.calcPercentProfit(coin.getPayedPrice());
		lbPorcentagem.setText(coin.calcPercentProfit(coin.getPayedPrice()) + "%");
		lbPorcentagem.setFont(Font.font(11));
		lbPorcentagem.setLayoutX(79.0);
		lbPorcentagem.setLayoutY(42.0);
		if(percent >= 0) {
			lbPorcentagem.setTextFill(Color.GREEN);
		}else  {
			lbPorcentagem.setTextFill(Color.RED);
		}
		
		lbPriceAtualMoeda.setText(Functions.round(coin.getTotalQtd(), 5) + " " + coin.getSymbol());
		lbPriceAtualMoeda.setFont(Font.font(11));
		lbPriceAtualMoeda.setLayoutX(224.0);	
		lbPriceAtualMoeda.setLayoutY(42.0);
		lbPriceAtualMoeda.setTextFill(Color.WHITE);
				
		this.getChildren().addAll(lbNome);
		this.getChildren().addAll(lbPrecoAtual);
		this.getChildren().addAll(lbPorcentagem);
		this.getChildren().addAll(lbPriceAtualMoeda);
		
		this.setPadding(new Insets(15, 15, 15, 15));
	}
}
