package br.com.project.controllers;

import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import br.com.project.components.CurrencyField;
import br.com.project.models.PortfolioModel;
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

	private PortfolioModel portfolioSave = new PortfolioModel();

	public boolean isRefresh = false;
	private boolean isEdit = false;

	private Stage _stage;

	@FXML
	private Label titleText;

	@FXML
	private TextField txtFieldNomePortfolio;

	@FXML
	private CurrencyField txtFieldVlrAporte;

	@FXML
	private Button btnSave;

	FormPortfolioController() {
	}

	public FormPortfolioController(PortfolioModel portfolio) {
		isEdit = true;
		portfolioSave = portfolio;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_stage.setOnShowing(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				System.out.println("tamo com a tela mostrando carai");

				if (isEdit) {

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
		portfolioSave.setNome(txtFieldNomePortfolio.getText());
		portfolioSave.setVlrTotalAporte(txtFieldVlrAporte.getAmount());
		// AQUI CARMELA, LANÇA O CODIGO DO BANCO MEU REI
		
		
		
		_stage.close();
	}

}
