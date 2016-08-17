package com.gbce.stockmarket.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gbce.stockmarket.beans.Trade;

/**
 * Implementation of Trade DAO Interface. <br>
 * 
 * Provides access to Trade data in the database. Implements methods for CRUD
 * operations.
 * 
 * @see com.gbce.stockmarket.dao.TradeDao
 * 
 * @author Ranjan Lal
 *
 */
@Component
public class TradeDaoImpl implements TradeDao {

	/**
	 * Map containing Trade data (In-memory data store).
	 */
	private Map<Date, Trade> trades = new ConcurrentHashMap<>();

	private static final Logger logger = LoggerFactory.getLogger(StockDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.dao.TradeDao#addTrade(com.gbce.stockmarket.beans.
	 * Trade)
	 */
	@Override
	public boolean addTrade(Trade trade) {

		logger.info("Saving trade to database : " + trade);

		trades.put(trade.getTimeStamp(), trade);

		boolean stockAdded = false;

		try {
			logger.info("Look up added trade in database : " + trade);
			stockAdded = getTrade(trade.getTimeStamp()) != null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return stockAdded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gbce.stockmarket.dao.TradeDao#getTrade(java.util.Date)
	 */
	@Override
	public Trade getTrade(Date timestamp) {

		Trade trade = trades.get(timestamp);

		if (trade == null) {
			throw new RuntimeException("Could not find trade in database : " + trade);
		}

		return trade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gbce.stockmarket.dao.TradeDao#getAllTrades()
	 */
	@Override
	public TreeMap<Date, Trade> getAllTrades() {
		TreeMap<Date, Trade> allTrades = new TreeMap<>();
		allTrades.putAll(trades);
		return allTrades;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.dao.TradeDao#deleteTrade(com.gbce.stockmarket.beans.
	 * Trade)
	 */
	@Override
	public void deleteTrade(Trade trade) {
		trades.remove(trade.getTimeStamp());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.dao.TradeDao#getAllTradesForStock(java.lang.String)
	 */
	@Override
	public TreeMap<Date, Trade> getAllTradesForStock(String stockSymbol) {
		TreeMap<Date, Trade> allTradesForStock = new TreeMap<>();
		Iterator<Entry<Date, Trade>> iterator = trades.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Date, Trade> entry = iterator.next();
			if (entry.getValue().getStock().getSymbol().equalsIgnoreCase(stockSymbol)) {
				allTradesForStock.put(entry.getKey(), entry.getValue());
			}
		}

		return allTradesForStock;
	}

}
