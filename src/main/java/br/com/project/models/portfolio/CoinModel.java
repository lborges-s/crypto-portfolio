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
	@Getter
	private double totalSold = 0;
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

		double _totalQtdCompra = 0;
		double _totalPagoCompra = 0;

		double _totalQtdVenda= 0;
		double _totalPagoVenda = 0;

		for (Transacao tr : transacoes) {
			if (tr.isCompra()) {
				_totalQtdCompra += tr.getQtde();
				_totalPagoCompra += tr.vlrTotal();
			} else {
				_totalQtdVenda = tr.getQtde();
				_totalPagoVenda -= tr.vlrTotal();
			}
		}
		
		System.out.println("_totalPagoCompra > " + _totalPagoCompra);
		System.out.println("_totalPagoVenda > " + _totalPagoVenda);

		this.totalQtd = _totalQtdCompra - _totalQtdVenda;
		this.totalPayed = _totalPagoCompra - _totalPagoVenda;
		
//		(qtdMoeda * vlrVendido) - (qtdMoeda * vlrCompra);
//		var gainLoss = (QtdMoedaVendida  * ValorMoedaAtual) - (QtdMoedaVendida * ValorVendaPorMoeda)
		this.totalPayedDisplay = totalPayed;
		this.avgPrice = _calcAvgPrice();

		System.out.println("Avg Price > " + avgPrice);
	}

	public double calcPercentProfit(double _actualPrice) {
		this.actualPrice = Functions.round(_actualPrice, 4);

		this.currentPercentProfit = Functions.round((actualPrice / avgPrice - 1) * 100, 4);
//		this.currentPercentProfit = Functions.round((actualPrice - avgPrice) / avgPrice, 3);

		System.out.println("currentPercentProfit > " + currentPercentProfit);

//		System.out.println("totalPayedDisplay > " + totalPayedDisplay);

		this.totalProfit = (currentPercentProfit / 100) * totalPayed;

		totalPayedDisplay = totalPayed + totalProfit;

		return currentPercentProfit;
	}

	// Preco Médio Compra
	private double _calcAvgPrice() {
		double _totalPayed = 0;
		double _totalQtd = 0;
		for (Transacao tr : transacoes) {
			if (tr.isCompra()) {
				_totalPayed += tr.vlrTotal();
				_totalQtd += tr.getQtde();
			}
//			else {
//				_totalQtd -= tr.getQtde();
//			}
		}
		return _totalPayed / _totalQtd;
	}

}
