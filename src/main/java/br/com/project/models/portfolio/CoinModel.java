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
	@EqualsAndHashCode.Exclude
	private double currentPercentProfit;

	CoinModel(String symbol, double totalQtd, double payedPrice) {
		this.symbol = symbol;
		this.totalQtd = totalQtd;
		this.payedPrice = payedPrice;
	}

	public void addQtd(double qtd) {
		this.totalQtd += qtd;
	}
	
	public double calcAvgPrice() {
		return totalQtd / payedPrice;
	}
	

	public double calcPercentProfit(double actualPrice) {
		this.currentPercentProfit =  actualPrice - calcAvgPrice() / calcAvgPrice();
		return currentPercentProfit;

	}

	public double calcTotalPrice() {
		return totalQtd * payedPrice;
	}

}
