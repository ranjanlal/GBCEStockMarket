package com.gbce.stockmarket.dao;

import java.util.List;

import com.gbce.stockmarket.beans.Stock;

/**
 * Interface for Stock DAO. <br>
 * 
 * Provides methods for CRUD (Create, Read, Update, Delete) operations to work
 * on Stock database table. <br>
 * For GBCE Super Simple Stock Market requirements the data is maintained
 * in-memory.
 * 
 * @author Ranjan Lal
 *
 */
public interface StockDao {

	/**
	 * Adds a Stock to the database.
	 * 
	 * @param stock
	 *            Stock object to be stored
	 * @return true if Stock was stored successfully, false otherwise
	 */
	boolean addStock(Stock stock);

	/**
	 * Fetches a Stock from database for a given stock symbol.
	 * 
	 * @param stockSymbol
	 *            symbol representing a stock
	 * @return Stock object retrieved from database
	 */
	Stock findStock(String stockSymbol);

	/**
	 * Fetches all stocks stored in the database.
	 * 
	 * @return List of Stock objects retrieved from database.
	 */
	List<Stock> getAllStocks();

}
