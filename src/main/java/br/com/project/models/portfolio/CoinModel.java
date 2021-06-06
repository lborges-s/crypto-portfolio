package br.com.project.models.portfolio;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CoinModel {
	private String symbol;
	@EqualsAndHashCode.Exclude
	private double totalQtd;
	@EqualsAndHashCode.Exclude
	private double payedPrice;

	CoinModel(String symbol, double totalQtd, double payedPrice) {
		this.symbol = symbol;
		this.totalQtd = totalQtd;
		this.payedPrice = payedPrice;
	}

	public void addQtd(double qtd) {
		this.totalQtd += qtd;
	}


	public double calcPercentProfit(double actualPrice) {
		return actualPrice - payedPrice / payedPrice;

	}

	public double calcTotalPrice() {
		return totalQtd * payedPrice;
	}

}
