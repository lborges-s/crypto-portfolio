package br.com.project.models.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private List<AporteModel> aportes = new ArrayList<AporteModel>();

	@Getter
	private List<Transacao> transacoes = new ArrayList<Transacao>();

	@Getter
	private List<CoinModel> coins = new ArrayList<CoinModel>();

	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}

	public void setTransacoes(List<Transacao> transacoes) {
		this.transacoes = transacoes;
		System.out.println("setTransacoes");
		listMoedas();
	}

	public double calcVlrTotalPortfolio() {
		return calcVlrDisponivelPortfolio() + calcVlrEmMoedasDisplay();
	}

	public double calcVlrDisponivelPortfolio() {
		return calcVlrTotalAportes() - calcVlrEmMoedas();
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

		if (coins.isEmpty())
			return 0;
		List<String> symbols = new ArrayList<String>();
		for (CoinModel coin : coins) {
			var symbol = coin.getSymbol();
			symbols.add(symbol);
		}
		return symbols.size();
	}
	
	public double calcVlrEmMoedasDisplay() {
		double total = 0;

		for (var c : coins) {
			total += c.getTotalPayedDisplay();
		}
		return total;
	}

	public double calcVlrEmMoedas() {
		double total = 0;
		for (var c : coins) {
			total += c.getTotalPayed();
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
		listMoedas();
	}

	public boolean isValidComprar(double amount) {
		return amount <= calcVlrDisponivelPortfolio();
	}

	public boolean isValidVender(String symbol, double qtdMoeda) {
		double qtdTotal = 0;
		for (var c : coins) {
			qtdTotal += c.getTotalQtd();
		}
		return qtdMoeda <= qtdTotal;
	}

	public List<CoinModel> listMoedas() {
		var coins = new ArrayList<CoinModel>();
		var symbols = new ArrayList<String>();
		for (Transacao t : transacoes) {
			if (!symbols.contains(t.getSimboloMoeda())) {
				symbols.add(t.getSimboloMoeda());
			}
		}

		for (String symbol : symbols) {
			var trs = transacoes.stream().filter(tr -> symbol.equals(tr.getSimboloMoeda()))
					.collect(Collectors.toList());

			coins.add(new CoinModel(symbol, trs));
		}

		this.coins = coins;
		return coins;
	}

	public List<String> unifiedSymbols() {
		var symbols = new ArrayList<String>();
		for (Transacao t : transacoes) {
			boolean contains = symbols.contains(t.getSimboloMoeda());
			if (!contains)
				symbols.add(t.getSimboloMoeda());
		}
		System.out.println("unifiedSymbols " + symbols);
		return symbols;
	}
	
	public List<String> anosDiferentes() {
		List<String> ano = new ArrayList<String>();
		for (Transacao transacao : transacoes) {
			var data = transacao.getDtTransacao();
			
			data = data.substring(data.length()-4, data.length());
			
			if (!ano.contains(data))
				ano.add(data);
		}
		
		return ano;
	}
}