package br.com.project.models;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickerStreamModel {
//	@SerializedName("e") 
	private String EventTtype;
//	@SerializedName("E") 
	private long EventTime;
//	@SerializedName("s")
	private String Symbol;
//	@SerializedName("p") 
	private String PriceChange;
//	@SerializedName("P") 
	private String PriceChangePercent;
//	@SerializedName("w") 
	private String WeightedAveragePrice;
//	@SerializedName("x")
	private String FirstTradePrice;
//	@SerializedName("c")
	private String LastPrice;
//	@SerializedName("Q") 
	private String lastQuantity;
//	@SerializedName("b") 
	private String BestBidPrice;
//	@SerializedName("B") 
	private String BestBidQuantity;
//	@SerializedName("a") 
	private String BestAskPrice;
//	@SerializedName("A") 
	private String BestAskQuantity;
//	@SerializedName("o") 
	private String OpenPrice;
//	@SerializedName("h") 
	private String HighPrice;
//	@SerializedName("l") 
	private String LowPrice;
//	@SerializedName("v") 
	private String TotalTradedBase;
//	@SerializedName("q")
	private String TotalTradedQuote;
//	@SerializedName("O") 
	private long StatisticsOpenTime;
//	@SerializedName("C") 
	private long StatisticsCloseTime;
//	@SerializedName("F") 
	private long FirstTradeID;
//	@SerializedName("l") 
	private long LastTradeID;
//	@SerializedName("n") 
	private long TotalNumberTrades;
}




