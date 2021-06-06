package br.com.project.models;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


public class TickerStreamModel {
	@JsonProperty("e")
	@Getter
	@Setter
	private String EventType;
	@JsonProperty("E")
	@Getter
	@Setter
	private long EventTime;
	@JsonProperty("s")
	@Getter
	@Setter
	private String Symbol;
	@JsonProperty("p")
	@Getter
	@Setter
	private String PriceChange;
	@JsonProperty("P")
	@Getter
	@Setter
	private String PriceChangePercent;
	@JsonProperty("w")
	@Getter
	@Setter
	private String WeightedAveragePrice;
	@JsonProperty("x")
	@Getter
	@Setter
	private String FirstTradePrice;
	@JsonProperty("c")
	@Getter
	@Setter
	private String LastPrice;
	@JsonProperty("Q")
	@Getter
	@Setter
	private String lastQuantity;
	@JsonProperty("b")
	@Getter
	@Setter
	private String BestBidPrice;
	@JsonProperty("B")
	@Getter
	@Setter
	private String BestBidQuantity;
	@JsonProperty("a")
	@Getter
	@Setter
	private String BestAskPrice;
	@JsonProperty("A")
	@Getter
	@Setter
	private String BestAskQuantity;
	@JsonProperty("o")
	@Getter
	@Setter
	private String OpenPrice;
	@JsonProperty("h")
	@Getter
	@Setter
	private String HighPrice;
	@JsonProperty("l")
	@Getter
	@Setter
	private String LowPrice;
	@JsonProperty("v")
	@Getter
	@Setter
	private String TotalTradedBase;
	@JsonProperty("q")
	@Getter
	@Setter
	private String TotalTradedQuote;

	@JsonProperty("O")
	@Getter
	@Setter
	private long StatisticsOpenTime;
	@JsonProperty("C")
	@Getter
	@Setter
	private long StatisticsCloseTime;
	@JsonProperty("F")
	@Getter
	@Setter
	private long FirstTradeID;
	@JsonProperty("L")
	@Getter
	@Setter
	private long LastTradeID;
	@JsonProperty("n")
	@Getter
	@Setter
	private long TotalNumberTrades;
}