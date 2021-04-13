package br.com.project.models;


public class BinanceModel{

	String symbol;
	String priceChange;
	String priceChangePercent;
	String weightedAvgPrice;
    String prevClosePrice;	
    String lastPrice;
    String lastQty;
    String bidPrice;
    
    public String getSymbol() { 
		 return this.symbol; } 
    public void setSymbol(String symbol) { 
		 this.symbol = symbol; } 
     
    public String getPriceChange() { 
		 return this.priceChange; } 
    public void setPriceChange(String priceChange) { 
		 this.priceChange = priceChange; } 
    
    public String getPriceChangePercent() { 
		 return this.priceChangePercent; } 
    public void setPriceChangePercent(String priceChangePercent) { 
		 this.priceChangePercent = priceChangePercent; } 
    
    public String getWeightedAvgPrice() { 
		 return this.weightedAvgPrice; } 
    public void setWeightedAvgPrice(String weightedAvgPrice) { 
		 this.weightedAvgPrice = weightedAvgPrice; } 
    
    public String getPrevClosePrice() { 
		 return this.prevClosePrice; } 
    public void setPrevClosePrice(String prevClosePrice) { 
		 this.prevClosePrice = prevClosePrice; } 

    
    public String getLastPrice() { 
		 return this.lastPrice; } 
    public void setLastPrice(String lastPrice) { 
		 this.lastPrice = lastPrice; } 
    
    
    public String getLastQty() { 
		 return this.lastQty; } 
    public void setLastQty(String lastQty) { 
		 this.lastQty = lastQty; } 
    
    public String getBidPrice() { 
		 return this.bidPrice; } 
    public void setBidPrice(String bidPrice) { 
		 this.bidPrice = bidPrice; } 
    
    public String getBidQty() { 
		 return this.bidQty; } 
    public void setBidQty(String bidQty) { 
		 this.bidQty = bidQty; } 
    String bidQty;
     
    public String getAskPrice() { 
		 return this.askPrice; } 
    public void setAskPrice(String askPrice) { 
		 this.askPrice = askPrice; } 
    String askPrice;

    public String getAskQty() { 
		 return this.askQty; } 
    public void setAskQty(String askQty) { 
		 this.askQty = askQty; } 
    String askQty;

    public String getOpenPrice() { 
		 return this.openPrice; } 
    public void setOpenPrice(String openPrice) { 
		 this.openPrice = openPrice; } 
    String openPrice;

    public String getHighPrice() { 
		 return this.highPrice; } 
    public void setHighPrice(String highPrice) { 
		 this.highPrice = highPrice; } 
    String highPrice;

    public String getLowPrice() { 
		 return this.lowPrice; } 
    public void setLowPrice(String lowPrice) { 
		 this.lowPrice = lowPrice; } 
    String lowPrice;

    public String getVolume() { 
		 return this.volume; } 
    public void setVolume(String volume) { 
		 this.volume = volume; } 
    String volume;

    public String getQuoteVolume() { 
		 return this.quoteVolume; } 
    public void setQuoteVolume(String quoteVolume) { 
		 this.quoteVolume = quoteVolume; } 
    String quoteVolume;

    public long getOpenTime() { 
		 return this.openTime; } 
    public void setOpenTime(long openTime) { 
		 this.openTime = openTime; } 
    long openTime;

    public long getCloseTime() { 
		 return this.closeTime; } 
    public void setCloseTime(long closeTime) { 
		 this.closeTime = closeTime; } 
    long closeTime;

    public int getFirstId() { 
		 return this.firstId; } 
    public void setFirstId(int firstId) { 
		 this.firstId = firstId; } 
    int firstId;

    public int getLastId() { 
		 return this.lastId; } 
    public void setLastId(int lastId) { 
		 this.lastId = lastId; } 
    int lastId;

    public int getCount() { 
		 return this.count; } 
    public void setCount(int count) { 
		 this.count = count; } 
    int count;
}

