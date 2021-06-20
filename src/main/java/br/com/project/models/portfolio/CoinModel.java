package br.com.project.models.portfolio;

import java.util.List;

import br.com.project.utils.Functions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class CoinModel {
	@Getter
	private String symbol;
	@EqualsAndHashCode.Exclude
	@Getter
	private double totalQtd = 0;
	@EqualsAndHashCode.Exclude
	@Getter
	private double totalPayed = 0;
	@EqualsAndHashCode.Exclude
	@Getter
	private double totalPayedDisplay = 0;
	@EqualsAndHashCode.Exclude
	@Getter
	private double currentPercentProfit = 0;
	@EqualsAndHashCode.Exclude
	private List<Transacao> transacoes;
	@Getter
	private double avgPrice = 0;

	CoinModel(String symbol, List<Transacao> transacoes) {
		this.symbol = symbol;
		this.transacoes = transacoes;

		for (Transacao tr : transacoes) {
			if (tr.getTpTransacao() == 'C') {
				totalQtd += tr.getQtde();
				totalPayed += tr.vlrTotal();
			} else {
				totalQtd -= tr.getQtde();
				totalPayed -= tr.vlrTotal();
			}
		}
		this.totalPayedDisplay = totalPayed;
		this.avgPrice = _calcAvgPrice();

		System.out.println("Avg Price > " + avgPrice);
	}

	public double calcPercentProfit(double actualPrice) {
		this.currentPercentProfit = Functions.round((actualPrice - avgPrice) / avgPrice, 2);
		System.out.println("currentPercentProfit > " + currentPercentProfit);
		totalPayedDisplay = totalPayedDisplay * (currentPercentProfit + 1);

		return currentPercentProfit;
	}

	public double calcTotalPrice() {
		return totalQtd * avgPrice;
	}

	private double _calcAvgPrice() {
		double totalPayed = 0;
		for (Transacao tr : transacoes) {
			totalPayed += tr.getPrecoTransacao();
		}
		return totalPayed / totalQtd;
	}

}
