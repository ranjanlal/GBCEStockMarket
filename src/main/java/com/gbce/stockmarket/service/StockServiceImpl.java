package com.gbce.stockmarket.service;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbce.stockmarket.beans.Stock;
import com.gbce.stockmarket.beans.Trade;
import com.gbce.stockmarket.constants.StockType;
import com.gbce.stockmarket.dao.StockDao;
import com.gbce.stockmarket.dao.TradeDao;

/**
 * Implementation of Stock Service. <br>
 * 
 * Provides operations for GBCE Super Simple Stock Market application.
 * 
 * @see com.gbce.stockmarket.service.StockService
 * 
 * @author Ranjan Lal
 */
@Service
public class StockServiceImpl implements StockService {

	/**
	 * Stock DAO object injected via Spring Auto Wiring
	 */
	@Autowired
	private StockDao stockDao;

	/**
	 * Trade DAO object injected via Spring Auto Wiring
	 */
	@Autowired
	private TradeDao tradeDao;

	private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.service.StockService#findStock(java.lang.String)
	 */
	@Override
	public Stock findStock(String stockSymbol) {

		logger.info("Looking up Stock with symbol : " + stockSymbol);

		Stock stock = null;

		try {
			stock = stockDao.findStock(stockSymbol);
			logger.info("Found Stock : " + stock);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return stock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gbce.stockmarket.service.StockService#recordTrade(com.gbce.
	 * stockmarket.beans.Trade)
	 */
	@Override
	public boolean recordTrade(Trade trade) {

		logger.info("Adding trade to database : " + trade);

		boolean tradeAdded = false;

		try {
			tradeAdded = tradeDao.addTrade(trade);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return tradeAdded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.service.StockService#calculateDividendYield(java.
	 * lang.String, double)
	 */
	@Override
	public double calculateDividendYield(String stockSymbol, double price) {

		logger.info("Calculating divident yield for stock with symbol : " + stockSymbol + ", and price = " + price);

		double dividendYield = 0.0;

		try {

			if (price <= 0) {
				throw new RuntimeException("Price should be a positive number. Cannot process with price = " + price);
			}

			Stock stock = stockDao.findStock(stockSymbol);

			if (stock.getType() == StockType.COMMON) {
				dividendYield = stock.getLastDividend() / price;
			} else if (stock.getType() == StockType.PREFERRED) {
				dividendYield = (stock.getFixedDividend() * stock.getParValue()) / price;
			}

			logger.info("Dividend Yield = " + dividendYield);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return dividendYield;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.service.StockService#calculatePERatio(java.lang.
	 * String, double)
	 */
	@Override
	public double calculatePERatio(String stockSymbol, double price) {

		logger.info("Calculating P/E ratio for stock with symbol : " + stockSymbol + ", and price = " + price);

		double peRatio = 0.0;

		try {

			if (price <= 0) {
				throw new RuntimeException("Price should be a positive number. Cannot process with price = " + price);
			}

			Stock stock = stockDao.findStock(stockSymbol);

			if (stock.getLastDividend() == 0.0) {
				throw new RuntimeException("Dividend = 0, Cannot calculate P/E ratio for stock : " + stock);
			}

			peRatio = price / stock.getLastDividend();

			logger.info("P/E Ratio = " + peRatio);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return peRatio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gbce.stockmarket.service.StockService#calculateVolumeStockPrice(java.
	 * lang.String, int)
	 */
	@Override
	public double calculateVolumeStockPrice(String stockSymbol, int timeInMinutes) {

		String logMessage = "Calculating Volume Weighted Stock Price for stock with symbol : " + stockSymbol;
		if (timeInMinutes > 0) {
			logMessage += " for trades in past " + timeInMinutes + " minutes.";
		} else {
			logMessage += " for all trades.";
		}
		logger.info(logMessage);

		double volumeWeigthedStockPrice = 0;

		SortedMap<Date, Trade> trades = tradeDao.getAllTradesForStock(stockSymbol);

		if (timeInMinutes > 0) {
			Date startTime = new Date(new Date().getTime() - (timeInMinutes * 60 * 1000));
			trades = trades.tailMap(startTime);
		}

		double totalPrice = 0.0;
		int totalQuantity = 0;

		for (Trade trade : trades.values()) {
			totalQuantity += trade.getStocksQuantity();
			totalPrice += trade.getPrice() * trade.getStocksQuantity();
		}

		if (totalQuantity > 0) {
			volumeWeigthedStockPrice = totalPrice / totalQuantity;
		}

		logger.info("Volume Weighted Stock Price for stock with symbol : " + stockSymbol + " = "
				+ volumeWeigthedStockPrice);

		return volumeWeigthedStockPrice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gbce.stockmarket.service.StockService#calculateAllShareIndex()
	 */
	@Override
	public double calculateAllShareIndex() {

		logger.info("Calculating All Share Index");

		double allShareIndex = 0.0;
		double shareIndexProduct = 1.0;

		List<Stock> allStocks = stockDao.getAllStocks();

		int volumeStockPriceCount = 0;
		for (Stock stock : allStocks) {
			double volumeStockPrice = calculateVolumeStockPrice(stock.getSymbol(), 0);
			if (volumeStockPrice > 0) {
				shareIndexProduct *= volumeStockPrice;
				volumeStockPriceCount++;
			}
		}

		if (allStocks.size() > 0) {
			allShareIndex = Math.pow(shareIndexProduct, 1.0 / volumeStockPriceCount);
			allShareIndex = Math.round(allShareIndex * 100) / 100;
		}

		logger.info("All Share Index = " + allShareIndex);

		return allShareIndex;
	}

}
