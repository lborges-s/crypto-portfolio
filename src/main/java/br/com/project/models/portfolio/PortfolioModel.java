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

	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}

	public double calcVlrTotalPortfolio() {
		return calcVlrTotalAportes() + calcVlrEmMoedas();
	}

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

	public double calcVlrEmMoedas() {
		double total = 0;
		for (var t : transacoes) {
			if (t.getTpTransacao() == 'C') {
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

	public boolean isValidComprar(double amount) {
		return amount <= (calcVlrTotalAportes() - calcVlrEmMoedas());
	}

	public List<CoinModel> listMoedas() {
		var coins = new ArrayList<CoinModel>();

		for(Transacao t: transacoes) {
			var coin = new CoinModel(t.getSimboloMoeda(), t.getQtde(), t.getPrecoTransacao());
			if(coins.contains(coin)) {
				System.out.println("Contains coin > " + coin.getSymbol());
				CoinModel c = coins.get(coins.indexOf(coin));
				c.addQtd(t.getQtde());
			}else {
				coins.add(coin);
			}		
		}
		return coins;
	}

	public List<String> unifiedSymbols() {
		var symbols = new ArrayList<String>();
		for (Transacao t : transacoes) {
			boolean contains = symbols.contains(t.getSimboloMoeda());
			if (!contains)
				symbols.add(t.getSimboloMoeda());
		}
		return symbols;
	}
}