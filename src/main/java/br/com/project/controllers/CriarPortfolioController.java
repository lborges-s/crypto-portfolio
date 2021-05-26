package br.com.project.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.project.components.CurrencyField;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.portfolio.AporteModel;
import br.com.project.models.portfolio.PortfolioModel;
import br.com.project.utils.IController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CriarPortfolioController implements Initializable, IController {

	private final MongoConcretePortfolio mongo = new MongoConcretePortfolio();
	private PortfolioModel portfolioSave = new PortfolioModel();

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

	private IVoidCallback callbackSaveOk;

	CriarPortfolioController(IVoidCallback callbackSaveOk) {
		this.callbackSaveOk = callbackSaveOk;
	}

	public CriarPortfolioController(PortfolioModel portfolio, IVoidCallback callbackSaveOk) {
		isEdit = true;
		portfolioSave = portfolio;
		this.callbackSaveOk = callbackSaveOk;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_stage.setOnShowing(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				if (isEdit) {
					titleText.setText("Editando portfólio");
					txtFieldNomePortfolio.setText(portfolioSave.getNome());
					txtFieldVlrAporte.setEditable(false);
					txtFieldVlrAporte.setAmount(portfolioSave.getAportes().get(0).getValor());
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

		try {
			if (!_validateFields())
				return;
			portfolioSave.setNome(txtFieldNomePortfolio.getText());
			if (isEdit) {
				mongo.updatePortfolioName(portfolioSave.getId(), portfolioSave.getNome());

			} else {
				Date date = new Date();
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				AporteModel aporte = new AporteModel(txtFieldVlrAporte.getAmount(), dateFormat.format(date));
				portfolioSave.addAporte(aporte);

				mongo.add(portfolioSave);
			}

			JOptionPane.showMessageDialog(null, "Seu portfólio foi salvo com sucesso!", "Operação concluída",
					JOptionPane.INFORMATION_MESSAGE);
			callbackSaveOk.handleCallback();
			_stage.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Não foi possível salvar o portfólio > " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean _validateFields() {

		if (txtFieldNomePortfolio.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o nome do portfólio", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (txtFieldVlrAporte.getText().isEmpty() || !(txtFieldVlrAporte.getAmount() != 0)) {
			JOptionPane.showMessageDialog(null, "Informe o aporte inicial", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public interface IVoidCallback {
		public void handleCallback();
	}

}