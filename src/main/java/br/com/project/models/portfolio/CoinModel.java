package br.com.project.models.portfolio;

import java.util.List;

import br.com.project.utils.Functions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class CoinModel {
	@Getter
	private String symbol;

	@Getter
	@EqualsAndHashCode.Exclude
	private double totalQtd = 0;

	@Getter
	@EqualsAndHashCode.Exclude
	private double vlrTotal = 0;

	@Getter
	@EqualsAndHashCode.Exclude
	private double vlrTotalInvestido = 0;

//	@Getter
//	@EqualsAndHashCode.Exclude
//	private double totalPayedDisplay = 0;

	@Getter
	@EqualsAndHashCode.Exclude
	private double rentabilidade = 0;

	@Getter
	@EqualsAndHashCode.Exclude
	private List<Transacao> transacoes;

	@Getter
	@EqualsAndHashCode.Exclude
	private double actualPrice = 0;

	@Getter
	@EqualsAndHashCode.Exclude
	private double totalProfit = 0;
	
	@Getter
	@EqualsAndHashCode.Exclude
	private double totalGanho= 0;

	CoinModel(String symbol, List<Transacao> transacoes) {
		this.symbol = symbol;
		this.transacoes = transacoes;

		double _totalQtdCompra = 0;
		double _totalCustoCompra = 0;

		double _totalQtdVenda = 0;
		double _totalGanho = 0;

		System.out.println("Qtd Transacoes > " + transacoes.size());
		for (Transacao tr : transacoes) {
			if (tr.isCompra()) {
				_totalQtdCompra += tr.getQtde();
				_totalCustoCompra += tr.custoTransacao();
			} else {
				_totalQtdVenda += tr.getQtde();
				_totalGanho += tr.custoTransacao();
			}
		}
		
		System.out.println("Total Custo Compra > " + _totalCustoCompra);
		System.out.println("Total Ganho Vendas > " + _totalGanho);
		
		this.totalGanho = _totalGanho;
		this.totalQtd = _totalQtdCompra - _totalQtdVenda;
		this.vlrTotalInvestido = _totalCustoCompra - _totalGanho;

//		this.totalPayedDisplay = vlrTotalInvestido;
//		this.avgPrice = _calcAvgPrice();

//		System.out.println("Avg Price > " + avgPrice);
	}

	public void calcProfit(double _actualPrice) {
		this.actualPrice = Functions.round(_actualPrice, 4);

		final double vlrTotal = actualPrice * totalQtd;
		final double vlrTotalProfit = (totalQtd * actualPrice) - vlrTotalInvestido;
		final double rentabilidade = ((vlrTotal - vlrTotalInvestido) / vlrTotalInvestido) * 100;

		System.out.println("Preço atual   > " + actualPrice);
		System.out.println("Valor Total   > " + vlrTotal);
		System.out.println("Lucro/Preju   > " + vlrTotalProfit);
		System.out.println("Rentabilidade > " + rentabilidade);
		System.out.println("--------------------------------");

		this.totalProfit = vlrTotalProfit;
		this.vlrTotal = vlrTotal;
		this.rentabilidade = rentabilidade;

//		this.currentPercentProfit = Functions.round((actualPrice / avgPrice - 1) * 100, 4);

//		this.totalProfit = (currentPercentProfit / 100) * totalInvestido;

//		totalPayedDisplay = vlrTotalInvestido + totalProfit;
	}

//	// Preco Médio Compra
//	private double _calcAvgPrice() {
//		double _totalPayed = 0;
//		double _totalQtd = 0;
//		for (Transacao tr : transacoes) {
//			if (tr.isCompra()) {
//				_totalPayed += tr.custoTransacao();
//				_totalQtd += tr.getQtde();
//			}
////			else {
////				_totalQtd -= tr.getQtde();
////			}
//		}
//		return _totalPayed / _totalQtd;
//	}

}
