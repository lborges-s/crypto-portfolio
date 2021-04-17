package br.com.project.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinanceModel{
	private String symbol;
	private String priceChange;
	private String priceChangePercent;
	private String weightedAvgPrice;
	private String prevClosePrice;	
	private String lastPrice;
	private String lastQty;
    private String bidPrice;
    private String bidQty;    
    private String askPrice;
    private String askQty;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String volume;
    private String quoteVolume;
    private long openTime;
    private long closeTime;
    private int firstId;
    private int lastId;
    private int count;
}

