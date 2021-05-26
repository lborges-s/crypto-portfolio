package br.com.project.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.utils.IController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TransacaoController implements Initializable, IController {

	Stage stage;

	@FXML
	private ToggleButton btnToggleComprar;

	@FXML
	private ToggleButton btnToggleVender;
	
	private String tpTransacao;

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
		final ToggleGroup group = new ToggleGroup();
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				System.out.println((String) group.getSelectedToggle().getUserData());
				tpTransacao = (String) group.getSelectedToggle().getUserData();
			}
		});

		stage.setOnShowing(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {

				btnToggleComprar.setToggleGroup(group);
				btnToggleComprar.setUserData("C");
				btnToggleComprar.setSelected(true);
				btnToggleComprar.setCursor(Cursor.HAND);
				
				btnToggleVender.setToggleGroup(group);
				btnToggleVender.setUserData("V");
				btnToggleVender.setCursor(Cursor.HAND);
				
				System.out.println(btnToggleComprar.getToggleGroup());
			}
		});
	}
	
	private void _save() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
