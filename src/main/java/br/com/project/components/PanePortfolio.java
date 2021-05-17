package br.com.project.components;

import javax.swing.JOptionPane;

import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.portfolio.PortfolioModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class PanePortfolio extends Pane {

	@EqualsAndHashCode.Exclude
	private Label lbName = new Label();
	@EqualsAndHashCode.Exclude
	private Button btnEdit = new Button();
	@EqualsAndHashCode.Exclude
	private Button btnDelete = new Button();

	@FXML
	AnchorPane mainPane;

	private String id;

	@EqualsAndHashCode.Exclude
	private ClickCallback clickCallback;
	private ClickDeleteCallback clickDeleteCallback;

	public PanePortfolio(PortfolioModel portfolio) {

		this.id = portfolio.getId().toString();
		this.setId(id);
		this.setWidth(500);

		lbName.setCursor(Cursor.HAND);
		lbName.setText(portfolio.getNome());
		lbName.setFont(Font.font(20));
		lbName.setLayoutX(15.0);
		lbName.setLayoutY(10.0);
		lbName.getStyleClass().add("nome-portifolio");

		lbName.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (clickCallback != null) {
					clickCallback.handle(false);
				}
			}
		});

		btnDelete.setText("🗑");
		btnDelete.setFont(Font.font(38));
		btnDelete.setTextFill(Color.WHITE);
		btnDelete.setPadding(new Insets(-12, 0, -9, 0));
		btnDelete.setStyle("-fx-text-fill:  white;");
		btnDelete.getStyleClass().add("transparente");
		btnDelete.getStylesheets().add("@css/fullpackstyling.css");
		btnDelete.setLayoutX(this.getWidth() - 30);
		btnDelete.setLayoutY(0.0);
		btnDelete.setCursor(Cursor.HAND);
		btnDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				int selectedOption = JOptionPane.showConfirmDialog(null,
						String.format("Deseja excluir o portfólio \"%s\"?", portfolio.getNome()), "Excluir Portfólio",
						JOptionPane.YES_NO_OPTION);
				if (selectedOption == JOptionPane.YES_OPTION) {
					final MongoConcretePortfolio mongo = new MongoConcretePortfolio();
					final boolean isDeleted = mongo.remove(portfolio.getId());
					if(isDeleted) {
						if (clickDeleteCallback != null) {
							clickDeleteCallback.handle();
						}
						JOptionPane.showMessageDialog(null, "Seu portfólio foi removido com sucesso!", "Operação concluída",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

		btnEdit.setText("✎");
		btnEdit.setFont(Font.font(38));
		btnEdit.setTextFill(Color.WHITE);
		btnEdit.setPadding(new Insets(-12, 0, -9, 0));
		btnEdit.setStyle("-fx-text-fill:  white;");
		btnEdit.getStyleClass().add("transparente");
		btnEdit.getStylesheets().add("@css/fullpackstyling.css");
		btnEdit.setLayoutX(btnDelete.getLayoutX() - 45);
		btnEdit.setLayoutY(0.0);
		btnEdit.setCursor(Cursor.HAND);

		btnEdit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (clickCallback != null) {
					clickCallback.handle(true);
				}
			}
		});

		this.getChildren().addAll(lbName);
		this.getChildren().addAll(btnDelete);
		this.getChildren().addAll(btnEdit);
	}

	public void addClickCallback(ClickCallback clickCallback) {
		this.clickCallback = clickCallback;
	}

	public void addClickDeleteCallback(ClickDeleteCallback clickDeleteCallback) {
		this.clickDeleteCallback = clickDeleteCallback;
	}

	public static interface ClickCallback {
		public void handle(boolean isEdit);
	}

	public static interface ClickDeleteCallback {
		public void handle();
	}

}