package com.gbce.stockmarket.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gbce.stockmarket.beans.Stock;

/**
 * Implementation of Stock DAO Interface. <br>
 * 
 * Provides access to Stock data in the database. Implements methods for CRUD
 * operations.
 * 
 * @see com.gbce.stockmarket.dao.StockDao
 * 
 * @author Ranjan Lal
 *
 */
@Component
public class StockDaoImpl implements StockDao {

	/**
	 * Map containing Stock data (In-memory data store). Injected via Spring
	 * Auto Wiring.
	 */
	@Resource(name = "stocksDatabase")
	private Map<String, Stock> stocksDatabase;

	private static final Logger logger = LoggerFactory.getLogger(StockDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.dao.StockDao#addStock(com.gbce.stockmarket.beans.
	 * Stock)
	 */
	@Override
	public boolean addStock(Stock stock) {

		logger.info("Adding stock to database : " + stock);

		stocksDatabase.put(stock.getSymbol(), stock);

		boolean stockAdded = false;

		try {
			logger.info("Look up added stock in database : " + stock);
			stockAdded = findStock(stock.getSymbol()) != null;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return stockAdded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gbce.stockmarket.dao.StockDao#findStock(java.lang.String)
	 */
	@Override
	public Stock findStock(String stockSymbol) {

		Stock stock = stocksDatabase.get(stockSymbol);

		if (stock == null) {
			throw new RuntimeException("Couldnot find stock with symbol : " + stockSymbol + " in database.");
		}

		return stock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gbce.stockmarket.dao.StockDao#getAllStocks()
	 */
	@Override
	public List<Stock> getAllStocks() {
		List<Stock> allStocks = new ArrayList<>(stocksDatabase.values());
		return Collections.unmodifiableList(allStocks);
	}

}
