package br.com.project.models.portfolio;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
		return calcVlrDisponivelPortfolio() + calcVlrEmMoedasDisplay() + _calcTotalVendido();
	}

	public double calcVlrDisponivelPortfolio() {
		return calcVlrTotalAportes() - calcVlrEmMoedas();
	}

	private double _calcTotalVendido() {
		double totalSold = 0;
		for (var c : coins) {
			totalSold += c.getTotalSold();
		}
		return totalSold;
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
		var coinsFiltered = coins.stream().filter(c -> symbol.equals(c.getSymbol())).collect(Collectors.toList());

		if (coinsFiltered.isEmpty())
			return false;
		for (var c : coinsFiltered) {
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

	public List<Integer> anosDiferentes() {
		List<Integer> anos = new ArrayList<Integer>();
		for (var t : transacoes) {
			var ano = t.getAno();
			if (!anos.contains(ano))
				anos.add(ano);
		}

		return anos;
	}

	public double calcTotalProfitLoss() {
		double profit = 0;

		for (var c : coins) {
			profit += c.getTotalProfit();
		}

		return profit;
	}

	public int lastCounterTransaction() {
		int counter = 1;
		for (var t : transacoes) {
			if (t.getCounter() > counter)
				counter = t.getCounter();
		}
		return counter;
	}

	public double getVlrTotalTransactionsByDate(int ano, int mes) {

		var trs = transacoes.stream().filter(tr -> {
			try {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(tr.getDate());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);

				return year == ano && mes == month;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList());

		double vlrTotal = 0;
		
		for (var t : trs) {
			vlrTotal += t.vlrTotal();
		}

		return vlrTotal;

	}

}