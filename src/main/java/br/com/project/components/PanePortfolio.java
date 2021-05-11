package br.com.project.components;

import br.com.project.models.portfolio.PortfolioModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class PanePortfolio extends Pane {

	@EqualsAndHashCode.Exclude
	private Label lbName = new Label();
	
	@FXML
	AnchorPane mainPane;
	
	private String id;

	private MessageHandler messageHandler;

	public PanePortfolio(PortfolioModel portfolio) {
		
		this.id = portfolio.getId().toString();
		this.setId(id);
		
		lbName.setText(portfolio.getNome());
		lbName.setFont(Font.font(20));
		lbName.setLayoutX(14.0);
		lbName.setLayoutY(10.0);
		lbName.getStyleClass().add("nome-portifolio");
		this.setCursor(Cursor.HAND);
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	if(messageHandler != null) {
            		messageHandler.handleMessage(id.toString());
            	}
            }
        });

		this.getChildren().addAll(lbName);
	}
	
	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	public static interface MessageHandler {
		public void handleMessage(String _id);
	}

}