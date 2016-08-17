package com.gbce.stockmarket.service;

import com.gbce.stockmarket.beans.Stock;
import com.gbce.stockmarket.beans.Trade;

/**
 * Interface for Stock Service. Provides operations for GBCE Super Simple Stock
 * Market application:<br>
 * <br>
 * 
 * 1. Search for a Stock with given stock symbol <br>
 * 2. Record a Trade done with a stock <br>
 * 3. Calculate Dividend Yield for a given stock at a given price <br>
 * 4. Calculate P/E Ratio for a given stock at a given price <br>
 * 5. Calculate Volume Weighted Stock Price based on trades in past given time
 * frame (e.g. past 5 minutes) for a given stock <br>
 * 6. Calculate the GBCE All Share Index using the geometric mean of the Volume
 * Weighted Stock Price for all stocks <br>
 * <br>
 * 
 * For GBCE Super Simple Stock Market requirements all data is stored and
 * maintained in-memory.
 * 
 * @author Ranjan Lal
 * 
 */
public interface StockService {

	/**
	 * Fetches a stock for the given stock symbol.
	 * 
	 * @param stockSymbol
	 *            symbol representing a stock in GBCE Stock Market
	 * @return Stock object representing a stock in GBCE Stock Market
	 */
	Stock findStock(String stockSymbol);

	/**
	 * Records a trade in the trading system of GBCE Stock Market
	 * 
	 * @param trade
	 *            Trade object representing a Trade made on a stock in GBCE
	 *            Stock Market
	 * @return
	 */
	boolean recordTrade(Trade trade);

	/**
	 * Calculates Dividend Yield for a given stock at a given price
	 * 
	 * @param stockSymbol
	 *            symbol representing a stock in GBCE Stock Market
	 * @param price
	 *            price of the given stock
	 * @return Dividend Yield calculated value
	 */
	double calculateDividendYield(String stockSymbol, double price);

	/**
	 * Calculates P/E Ratio for a given stock at a given price
	 * 
	 * @param stockSymbol
	 *            symbol representing a stock in GBCE Stock Market
	 * @param price
	 *            price of the given stock
	 * @return P/E Ratio calculated value
	 */
	double calculatePERatio(String stockSymbol, double price);

	/**
	 * Calculates Volume Weighted Stock Price based on trades in past given time
	 * frame (e.g. past 5 minutes) for a given stock
	 * 
	 * @param stockSymbol
	 *            symbol representing a stock in GBCE Stock Market
	 * @param timeInMinutes
	 *            past amount of time in mintutes to look up trades for
	 *            calculation
	 * @return Volume Weighted Stock Price calculated value
	 */
	double calculateVolumeStockPrice(String stockSymbol, int timeInMinutes);

	/**
	 * Calculates the GBCE All Share Index using the geometric mean of the
	 * Volume Weighted Stock Price for all stocks
	 * 
	 * @return All Share Index calculated value
	 */
	double calculateAllShareIndex();

}
