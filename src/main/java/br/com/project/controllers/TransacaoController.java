package br.com.project.controllers;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.project.components.CurrencyField;
import br.com.project.dao.MongoConcretePortfolio;
import br.com.project.models.CoinPriceModel;
import br.com.project.models.portfolio.PortfolioModel;
import br.com.project.models.portfolio.Transacao;
import br.com.project.utils.Functions;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.DoubleStringConverter;

public class TransacaoController implements Initializable, IController {
	private static String baseUrl = "https://api.binance.com/api/v3/ticker/price";

	public TransacaoController(PortfolioModel portfolio, IVoidCallback voidCallback) {
		this.portfolio = portfolio;
		this.voidCallback = voidCallback;
	}

	private IVoidCallback voidCallback;

	private PortfolioModel portfolio;

	final MongoConcretePortfolio mongo = new MongoConcretePortfolio();

	private Stage stage;

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
	private Button btnRemoveMoeda;

	@FXML
	private DatePicker fieldDtCompra;

	@FXML
	private Text txtMoeda;

	@FXML
	private TextField txtFieldQtd;

	@FXML
	private CurrencyField txtFieldPrecoMoeda;

	private String tpTransacao = "C";

	private CoinPriceModel moedaAtual;

	private ObjectMapper objectMapper = new ObjectMapper();
	NumberFormat nf = NumberFormat.getInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nf.setMaximumFractionDigits(8);
		LocalDate localDate = LocalDate.now();
		fieldDtCompra.setValue(localDate);
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
		final ToggleGroup group = new ToggleGroup();

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				String data = (String) group.getSelectedToggle().getUserData();
				if (data != null) {
					tpTransacao = data;

					if (tpTransacao.equals("C")) {
						btnFinalizar.setText("COMPRAR");
					} else {
						btnFinalizar.setText("VENDER");
					}
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
				DoubleStringConverter converter = new DoubleStringConverter();

				txtFieldQtd.setTextFormatter(new TextFormatter<>(converter));
			}
		});
	}

	@FXML
	private void removerMoedaPesquisada(ActionEvent event) {
		moedaAtual = null;
		btnRemoveMoeda.setVisible(false);
		btnPesquisaMoeda.setVisible(true);
		txtFieldMoeda.setEditable(true);
		txtFieldMoeda.clear();
		txtFieldMoeda.requestFocus();
		txtMoeda.setText("");
		txtFieldPrecoMoeda.setText("");
	}

	@FXML
	private void pesquisarMoeda(ActionEvent event) {
		if (txtFieldMoeda.getText().isBlank()) {
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
				moedaAtual = objectMapper.readValue(body, CoinPriceModel.class);
				btnRemoveMoeda.setVisible(true);
				btnRemoveMoeda.setLayoutX(0);
				btnPesquisaMoeda.setVisible(false);
				txtFieldMoeda.setEditable(false);
				txtMoeda.setText("MOEDA: " + moedaAtual.getSymbol());
				txtFieldPrecoMoeda.setText(Functions.formatMoney(Double.parseDouble(moedaAtual.getPrice())));

			} else {
				JOptionPane.showMessageDialog(null, "Não foi possível encontrar a moeda, tente novamente",
						"Erro ao pesquisar", JOptionPane.ERROR_MESSAGE);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void salvarTransacao(ActionEvent event) {
		if (!_validatarCampos())
			return;

		final double precoMoeda = txtFieldPrecoMoeda.getAmount();
		final char tpTr = tpTransacao.charAt(0);
		final double qtd = Double.parseDouble(txtFieldQtd.getText());
		final double total = qtd * precoMoeda;
		final String symbol = moedaAtual.getSymbol();

		System.out.println("Total > " + String.valueOf(total));

		if (tpTr == 'C' && !portfolio.isValidComprar(total)) {
			JOptionPane.showMessageDialog(null,
					"Parece que você não possui dinheiro o suficiente em sua carteira, faça um novo aporte!",
					"Aporte necessário", JOptionPane.WARNING_MESSAGE);
			return;
		} else if (tpTr == 'V' && !portfolio.isValidVender(symbol, qtd)) {
			JOptionPane.showMessageDialog(null,
					"Parece que você não possui moeda o suficiente da qual está querendo vender!", "Venda bloqueada",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		Transacao transacao = new Transacao();
		transacao.setTpTransacao(tpTr);
		transacao.setSimboloMoeda(symbol);
		transacao.setDtTransacao(fieldDtCompra.getValue().format(formatter));
		transacao.setPrecoTransacao(precoMoeda);
		transacao.setQtde(qtd);
		transacao.setCounter(portfolio.lastCounterTransaction() + 1);
		transacao.setAno(fieldDtCompra.getValue().getYear());

		mongo.addTransacao(portfolio.getId(), transacao);

		voidCallback.handleCallback();
		stage.close();
	}

	private boolean _validatarCampos() {
		if (moedaAtual == null) {
			JOptionPane.showMessageDialog(null, "Pesquise a moeda desejada para continuar!", "Pesquisa Obrigatória",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (txtFieldQtd.getText().isBlank()) {
			JOptionPane.showMessageDialog(null, "Informe a quantidade de moedas", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			try {
				double qtd = Double.parseDouble(txtFieldQtd.getText());
				if (qtd == 0) {

					JOptionPane.showMessageDialog(null, "Informe a quantidade de moedas", "Campo Obrigatório",
							JOptionPane.WARNING_MESSAGE);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Campo de quantidade com formato inválido", "Campo Obrigatório",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}

		if (txtFieldPrecoMoeda.getText().isBlank() || txtFieldPrecoMoeda.getAmount() == 0) {
			JOptionPane.showMessageDialog(null, "Informe o valor por moeda", "Campo Obrigatório",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}

	public interface IVoidCallback {
		public void handleCallback();
	}
}
