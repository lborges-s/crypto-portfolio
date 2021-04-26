package br.com.project.components;

import org.bson.types.ObjectId;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class ListPortifolios extends Pane {

	@EqualsAndHashCode.Exclude
	private Label lbName = new Label();
	
	private String id;

	public ListPortifolios(ObjectId id, String nomePortifolio) {
		
		this.id = id.toString();
		this.setId(id.toString());
		
		lbName.setText(nomePortifolio);
		lbName.setFont(Font.font(20));
		lbName.setTextFill(Color.WHITE);
		lbName.setLayoutX(14.0);
		lbName.setLayoutY(10.0);
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	
            }
        });

		this.getChildren().addAll(lbName);
	}
}