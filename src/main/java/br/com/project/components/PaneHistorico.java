package br.com.project.components;

import br.com.project.crypto_portfolio.App;
import br.com.project.models.HistoricoModel;
import br.com.project.utils.Functions;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PaneHistorico extends Pane {
	private Label lbStatus = new Label();

	private Label lbPrice = new Label();

	private Label lbDate = new Label();

	private Label lbQtdCoin = new Label();

	private ImageView image = new ImageView();

	public PaneHistorico(HistoricoModel historico) {
		this.setPrefSize(400, 70);
		final boolean isTransacao = historico.isTransacao();

		String icon = "icon/plus.png";

		if (isTransacao && historico.getTransacao().isVenda()) {
			icon = "icon/negative.png";
		}

		var img = new Image(App.class.getResourceAsStream(icon));
		image.setImage(img);
		image.setFitWidth(50);
		image.setFitHeight(50);
		image.relocate(10, 10);

		String date;
		if (isTransacao)
			date = historico.getTransacao().getDtTransacao();
		else
			date = historico.getAporte().getData();

		var color = Color.LAWNGREEN;
		if (isTransacao && historico.getTransacao().isVenda()) {
			color = Color.RED;
		}
		String text = "";
		if (isTransacao) {
			if (historico.getTransacao().isVenda()) {
				text = "VENDA";
			} else {
				text = "COMPRA";
			}
		} else {
			text = "ADIÇÃO APORTE";
		}
		lbStatus.setText(text);
		lbStatus.setFont(Font.font("System", FontWeight.BOLD, 18));
		lbStatus.setTextFill(color);
		lbStatus.relocate(70, 5);

		lbDate.setText(date);
		lbDate.setFont(Font.font(14));
		lbDate.relocate(70, 35);
		lbDate.setTextFill(Color.WHITE);

		String textPrice = "";
		if (isTransacao) {
			textPrice += historico.getTransacao().getSimboloMoeda().toUpperCase() + " - "
					+ Functions.formatMoney(historico.getTransacao().custoTransacao());
		} else {
			textPrice += Functions.formatMoney(historico.getAporte().getValor());
		}

		lbPrice.setText(textPrice);
		lbPrice.setFont(Font.font("System", FontWeight.BOLD, 18));
		lbPrice.setTextFill(Color.WHITE);
		lbPrice.relocate(225, 5);

		this.getChildren().addAll(image, lbStatus, lbDate, lbPrice);
		if (isTransacao) {
			var qtdMoeda = String.valueOf(historico.getTransacao().getQtde());
			var precoTransacao = historico.getTransacao().getPrecoTransacao();

			lbQtdCoin.setText(qtdMoeda + " x " + Functions.formatMoney(precoTransacao) + " (por moeda)");
			lbQtdCoin.setFont(Font.font(14));
			lbQtdCoin.setTextFill(Color.WHITE);
			lbQtdCoin.relocate(225, 35);

			this.getChildren().add(lbQtdCoin);
		}

	}

}
