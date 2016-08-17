package com.gbce.stockmarket.dao;

import java.util.Date;
import java.util.TreeMap;

import com.gbce.stockmarket.beans.Trade;

/**
 * Interface for Trade DAO. <br>
 * 
 * Provides methods for CRUD (Create, Read, Update, Delete) operations to work
 * on Trade database table. <br>
 * For GBCE Super Simple Stock Market requirements the data is maintained
 * in-memory.
 * 
 * @author Ranjan Lal
 *
 */
public interface TradeDao {

	/**
	 * Adds a trade to the database.
	 * 
	 * @param trade
	 *            Trade object to be stored
	 * @return true if Trade was stored successfully, false otherwise
	 */
	boolean addTrade(Trade trade);

	/**
	 * Fetches a Stock from database for a given time stamp
	 * 
	 * @param timestamp
	 *            time stamp when the trade was made and stored in database
	 * @return Trade object retrieved from database
	 */
	Trade getTrade(Date timestamp);

	/**
	 * Removes the given trade from database.
	 * 
	 * @param trade
	 *            Trade object to be deleted
	 */
	void deleteTrade(Trade trade);

	/**
	 * Fetches all trades stored in the database.
	 * 
	 * @return tree map of Trade objects with time stamp of trade as retrieved
	 *         from database
	 */
	TreeMap<Date, Trade> getAllTrades();

	/**
	 * Fetches all trades for a given stock.
	 * 
	 * @param stockSymbol
	 *            symbol of stock to look up for a trade stored in the database.
	 * @return
	 */
	TreeMap<Date, Trade> getAllTradesForStock(String stockSymbol);

}
