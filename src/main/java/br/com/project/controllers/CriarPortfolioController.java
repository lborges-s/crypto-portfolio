package br.com.project.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import br.com.project.components.CurrencyField;
import br.com.project.dao.MongoConcrete;
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

	private final MongoConcrete<PortfolioModel> mongo = new MongoConcrete<PortfolioModel>(PortfolioModel.class);
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

	CriarPortfolioController() {
	}

	public CriarPortfolioController(PortfolioModel portfolio) {
		isEdit = true;
		portfolioSave = portfolio;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_stage.setOnShowing(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
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
		Date date = new Date();
		portfolioSave.setNome(txtFieldNomePortfolio.getText());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		  
		AporteModel aporte = new AporteModel(txtFieldVlrAporte.getAmount(),dateFormat.format(date));
		portfolioSave.addAporte(aporte);
		
		mongo.add(portfolioSave);

		_stage.close();
	}
}