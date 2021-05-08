package br.com.project.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.utils.IController;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class TelaAporteController implements Initializable, IController {
	
	Stage stage;
	
	public TelaAporteController (String idPortifolio) {
		
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}