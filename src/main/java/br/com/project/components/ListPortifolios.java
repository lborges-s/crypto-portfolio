package br.com.project.components;

import org.bson.types.ObjectId;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class ListPortifolios extends Pane {

	@EqualsAndHashCode.Exclude
	private Label lbName = new Label();
	
	@FXML
	AnchorPane mainPane;
	
	private String id;

	private MessageHandler messageHandler;

	public ListPortifolios(ObjectId id, String nomePortifolio) {
		
		this.id = id.toString();
		this.setId(id.toString());
		
		lbName.setText(nomePortifolio);
		lbName.setFont(Font.font(20));
//		lbName.setTextFill(Color.WHITE);
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