package br.com.project.models.portfolio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.project.models.HistoricoModel;
import br.com.project.models.MongoID;
import lombok.Getter;
import lombok.Setter;

public class PortfolioModel {

	@JsonIgnore
	private MongoID id;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private List<AporteModel> aportes = new ArrayList<>();

	@Getter
	@Setter
	private List<Transacao> transacoes = new ArrayList<>();

	public double calcVlrTotalAportes() {
		if (aportes.isEmpty())
			return 0;
		double vlrTotal = 0;
		for (AporteModel aporte : aportes) {
			vlrTotal += aporte.getValor();
		}
		return vlrTotal;
	}

	public int calcQtdMoedas() {
		if (transacoes.isEmpty())
			return 0;
		List<String> symbols = new ArrayList<String>();
		for (Transacao transacao : transacoes) {
			var symbol = transacao.getSimboloMoeda();
			if (!symbols.contains(symbol))
				symbols.add(symbol);
		}
		return symbols.size();
	}
	/// Valor total em moedas compradas
	public double calcVlrEmMoedas() {
		double total = 0;
		for (var t : transacoes) {
			if(t.getTpTransacao() == 'C') {
				total += t.vlrTotal();
			}
		}
		return total;
	}

	public List<HistoricoModel> historico() {
		var historico = new ArrayList<HistoricoModel>();

		for (AporteModel a : aportes) {
			historico.add(new HistoricoModel(a));
		}
		for (Transacao t : transacoes) {
			historico.add(new HistoricoModel(t));
		}

		return historico;
	}

	public void addAporte(AporteModel aporte) {
		aportes.add(aporte);
	}

	public void addTransacao(Transacao transacao) {
		transacoes.add(transacao);
	}

	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}

	public boolean isValidComprar(double amount) {
		return amount <= (calcVlrTotalAportes() - calcVlrEmMoedas());
	}

//	public boolean isValidVender(String symbol, double qtdMoeda) {
//		double qtdTotal = 0;
//		for (var t : transacoes) {
//			if (t.getSimboloMoeda().equals(symbol) && t.getTpTransacao() == 'C') {
//				qtdTotal += t.getQtde();
//			}
//		}
//		return qtdMoeda <= qtdTotal;
//	}

}