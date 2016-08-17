package com.gbce.stockmarket.util;

import java.util.Date;
import java.util.Map;

import com.gbce.stockmarket.beans.Trade;
import com.gbce.stockmarket.dao.TradeDao;

/**
 * Utility class for unit tests
 * 
 * @author Ranjan Lal
 *
 */
public class StockServiceTestUtil {

	/**
	 * Cleans up the Trade database table to be used before each test class in
	 * the TestNG test suite is executed.
	 * 
	 * @param tradeDao
	 *            Trade DAO object to access Trade database table.
	 */
	public static void cleanUpTradeDatabase(TradeDao tradeDao) {
		Map<Date, Trade> allTrades = tradeDao.getAllTrades();
		for (Date key : allTrades.keySet()) {
			tradeDao.deleteTrade(allTrades.get(key));
		}
	}

}
