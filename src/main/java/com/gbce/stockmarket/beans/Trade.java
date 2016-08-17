package com.gbce.stockmarket.beans;

import java.util.Date;

import com.gbce.stockmarket.constants.TradeType;

/**
 * @author Ranjan Lal
 *
 */
public class Trade {

	private Stock stock;
	private Date timeStamp;
	private TradeType type;
	private int stocksQuantity;
	private double price;

	public Trade() {
		super();
	}

	public Trade(Stock stock, Date timeStamp, TradeType type, int stocksQuantity, double price) {
		super();
		this.stock = stock;
		this.timeStamp = timeStamp;
		this.type = type;
		this.stocksQuantity = stocksQuantity;
		this.price = price;
	}

	@Override
	public String toString() {
		return "Trade [stock=" + stock + ", timeStamp=" + timeStamp + ", type=" + type + ", stocksQuantity="
				+ stocksQuantity + ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trade other = (Trade) obj;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public TradeType getType() {
		return type;
	}

	public void setType(TradeType type) {
		this.type = type;
	}

	public int getStocksQuantity() {
		return stocksQuantity;
	}

	public void setStocksQuantity(int stocksQuantity) {
		this.stocksQuantity = stocksQuantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
