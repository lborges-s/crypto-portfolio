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
	@Getter
	private double actualPrice = 0;
	@Getter
	private double totalProfit = 0;

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

	public double calcPercentProfit(double _actualPrice) {
		this.actualPrice = Functions.round(_actualPrice,4);
		
		
		this.currentPercentProfit = Functions.round((actualPrice / avgPrice - 1) * 100, 4);
//		this.currentPercentProfit = Functions.round((actualPrice - avgPrice) / avgPrice, 3);

		System.out.println("currentPercentProfit > " + currentPercentProfit);

//		System.out.println("totalPayedDisplay > " + totalPayedDisplay);

		this.totalProfit = (currentPercentProfit / 100) * totalPayed;

		totalPayedDisplay = totalPayed + totalProfit;

		return currentPercentProfit;
	}

//	public double calcTotalPrice() {
//		return totalQtd * avgPrice;
//	}

	private double _calcAvgPrice() {
		double totalPayed = 0;
		for (Transacao tr : transacoes) {
			if(tr.getTpTransacao() == 'C')
			totalPayed += tr.vlrTotal();
		}
		return totalPayed / totalQtd;
	}

}
