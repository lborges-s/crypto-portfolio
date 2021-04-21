package br.com.project.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.utils.IController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FormPortfolioController implements Initializable, IController {
	private String _teste;

	public boolean isRefresh = false;

	private Stage _stage;

	@FXML
	private Label titleText;
	
	@FXML
	private Button btnSave;

	FormPortfolioController() {

	}

	FormPortfolioController(String teste) {

		_teste = teste;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println("Teste value > " + _teste);

		_stage.setOnShowing(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				System.out.println("tamo com a tela mostrando carai");

				if (_teste != null) {

					titleText.setText("Editando portfólio");
				} else {
					titleText.setText("Criando portfólio");
				}

			}
		});
	}

	@Override
	public void setStage(Stage stage) {
		_stage = stage;
	}

	@FXML
	void onSave(ActionEvent event) {
		isRefresh = true;
		_stage.close();
	}

}
