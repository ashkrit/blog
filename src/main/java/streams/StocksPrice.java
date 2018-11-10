package streams;

import java.util.Date;

public class StocksPrice {

    private String stockSymbol;
    private Date tradeDate;
    private double openPrice;
    private double highPrice;

    public Date getTradeDate() {
        return tradeDate;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    @Override
    public String toString() {
        return String.format("Symbol=%s;Date=%s;open price=%s ; HighPrice=%s", stockSymbol, tradeDate, openPrice, highPrice);
    }

}
