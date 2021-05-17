package br.com.project.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import br.com.project.components.CurrencyField;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.portfolio.AporteModel;
import br.com.project.utils.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AporteController implements Initializable, IController {

	Stage stage;

	private AporteModel aporteSave = new AporteModel();

	private final MongoConcretePortfolio mongo = new MongoConcretePortfolio();

	public String idPortfolio;

	@FXML
	private Button btnSave;

	@FXML
	private CurrencyField txtFieldVlrAporte;
	
	private IVoidCallback voidCallback;

	public AporteController(String idPortfolio, IVoidCallback voidCallback) {
		this.idPortfolio = idPortfolio;
		this.voidCallback = voidCallback;
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	void onSave(ActionEvent event) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		aporteSave.setValor(txtFieldVlrAporte.getAmount());
		aporteSave.setData(dateFormat.format(date));

		mongo.addAporte(idPortfolio, aporteSave);

		voidCallback.handleCallback();
		stage.close();
	}

	public interface IVoidCallback {
		public void handleCallback();
	}

}