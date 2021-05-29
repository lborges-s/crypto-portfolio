package br.com.project.controllers;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import br.com.project.utils.IController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TransacaoController implements Initializable, IController {
	private static String baseUrl = "https://api.binance.com/api/v3/ticker/price";

	Stage stage;

	@FXML
	private ToggleButton btnToggleComprar;

	@FXML
	private ToggleButton btnToggleVender;

	@FXML
	private TextField txtFieldMoeda;

	@FXML
	private Button btnPesquisaMoeda;
	
	@FXML
	private Button btnFinalizar;
	
    @FXML
    private DatePicker fieldDtCompra;

	private String tpTransacao;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LocalDate localDate = LocalDate.now();
		fieldDtCompra.setValue(localDate);
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
		final ToggleGroup group = new ToggleGroup();

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				tpTransacao = (String) group.getSelectedToggle().getUserData();
				if(tpTransacao == null)return;
				if(tpTransacao.equals("C")) {
					btnFinalizar.setText("COMPRAR");
				}else {
					btnFinalizar.setText("VENDER");
				}
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

	@FXML
	private void salvarTransacao(ActionEvent event) {
		System.out.println("salvarTransacao");
	}
	
	@FXML
	private void pesquisarMoeda(ActionEvent event) {
		if(txtFieldMoeda.getText().isBlank()) {
			JOptionPane.showMessageDialog(null, "Informe a moeda para pesquisar", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String url = baseUrl + "?symbol=" + txtFieldMoeda.getText().toUpperCase();
		final HttpClient client = HttpClient.newBuilder().build();
		URI uri = URI.create(url);

		HttpRequest req = HttpRequest.newBuilder(uri).GET().build();

		CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
		response.thenAcceptAsync(res -> System.out.println(res));

		try {
			if (response.get().statusCode() == 200) {
				String body = response.get().body();
//			BinanceModel model = objectMapper.readValue(body, BinanceModel.class);
				System.out.println("---------- API --------- ");
				System.out.println("CHAMOU > " + body);
				System.out.println("------------------------ ");
			}else {
				JOptionPane.showMessageDialog(null, "Não foi possível encontrar a moeda, tente novamente", "Erro ao pesquisar",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}
