package br.com.project.components;

import java.util.Locale;

import br.com.project.models.TickerStreamModel;
import br.com.project.utils.Functions;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class PaneCrypto extends Pane {

	@EqualsAndHashCode.Exclude
	private Label lbName = new Label();

	@EqualsAndHashCode.Exclude
	private Label lbPrice = new Label();

	@EqualsAndHashCode.Exclude
	private Label lbPercent = new Label();
	@EqualsAndHashCode.Exclude
	private TickerStreamModel ticker;
	private String symbol;

	public PaneCrypto(TickerStreamModel ticker) {
		this.ticker = ticker;
		symbol = ticker.getSymbol();
		this.setPrefSize(360, 75);
		

		setLbNameText(ticker.getSymbol());
		lbName.setFont(Font.font(17));
		lbName.relocate(79, 5);
		lbName.setStyle("-fx-text-fill:  linear-gradient(to bottom , #ff9500, #fc466b);");
		lbName.getStyleClass().add("texto-degrade");
		lbName.getStylesheets().add("@css/fullpackstyling.css");

		setLbPrice(ticker.getLastPrice());
		lbPrice.setFont(Font.font(17));
		lbPrice.relocate(224, 5);
		lbPrice.setStyle("-fx-text-fill:  linear-gradient(to bottom , #ff9500, #fc466b);");
		lbPrice.getStyleClass().add("texto-degrade");
		lbPrice.getStylesheets().add("@css/fullpackstyling.css");

		setLbPercent(ticker.getPriceChangePercent());
		lbPercent.setFont(Font.font(11));

		double changePercent = Double.parseDouble(ticker.getPriceChangePercent());
		if (changePercent < 0)
			lbPercent.setTextFill(Color.RED);
		else
			lbPercent.setTextFill(Color.GREEN);
		lbPercent.relocate(224, 42);
		lbPercent.getStyleClass().add("texto-degrade");
		lbPercent.getStylesheets().add("@css/fullpackstyling.css");

		this.getChildren().addAll(lbName, lbPrice, lbPercent);
	}

	public void setLbNameText(String name) {
		lbName.setText(ticker.getSymbol());
	}

	public void setLbPrice(String lastPrice) {
		lbPrice.setText(Functions.formatMoney(lastPrice, new Locale("en", "US")));
	}

	public void setLbPercent(String pricePercent) {
		lbPercent.setText(pricePercent + "%");
	}

}
