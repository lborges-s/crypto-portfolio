package br.com.project.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.project.components.CurrencyField;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.portfolio.AporteModel;
import br.com.project.utils.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
	
	@FXML
	private DatePicker datePickerField;
	
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
		LocalDate localDate = LocalDate.now();
		datePickerField.setValue(localDate);
	}

	@FXML
	void onSave(ActionEvent event) {
		if(!_validateFields())return;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
		aporteSave.setValor(txtFieldVlrAporte.getAmount());
		aporteSave.setData(datePickerField.getValue().format(formatter));

		mongo.addAporte(idPortfolio, aporteSave);

		voidCallback.handleCallback();
		stage.close();
	}
	
	private boolean _validateFields() {
		if (txtFieldVlrAporte.getText().isEmpty() || !(txtFieldVlrAporte.getAmount() != 0)) {
			JOptionPane.showMessageDialog(null, "Informe o valor de aporte", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		if (datePickerField.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Informe a data de aporte", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public interface IVoidCallback {
		public void handleCallback();
	}

}