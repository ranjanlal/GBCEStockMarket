package com.gbce.stockmarket.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gbce.stockmarket.beans.Stock;
import com.gbce.stockmarket.beans.Trade;
import com.gbce.stockmarket.constants.StockType;
import com.gbce.stockmarket.constants.TradeType;
import com.gbce.stockmarket.util.StockServiceTestUtil;

import config.StockMarketTestSpringConfig;

/**
 * TestNG Unit Test class for Trade DAO
 * 
 * @author Ranjan Lal
 *
 */
@Test
@ContextConfiguration(classes = { StockMarketTestSpringConfig.class })
public class TradeDaoTest extends AbstractTestNGSpringContextTests {

	/**
	 * Trade DAO object. Injected via Spring Auto Wiring.
	 */
	@Autowired
	private TradeDao tradeDao;

	private static final Date DATE_1 = (new GregorianCalendar(2013, 1, 28, 13, 24, 56)).getTime();
	private static final Date DATE_2 = (new GregorianCalendar(2016, 10, 16, 18, 17, 46)).getTime();

	/**
	 * Cleans up Trade database table before tests begin.
	 * 
	 */
	@BeforeClass
	public void setUp() {
		StockServiceTestUtil.cleanUpTradeDatabase(tradeDao);
	}

	/**
	 * Cleans up Trade database table after all tests complete.
	 * 
	 */
	@AfterClass
	public void tearDown() {
		StockServiceTestUtil.cleanUpTradeDatabase(tradeDao);
	}

	/**
	 * Test to check if a Trade can be successfully added to database through
	 * Trade DAO.
	 * 
	 */
	@Test
	public void testAddTrade() {

		Stock stock;
		Trade trade;

		// Add 1st trade
		stock = new Stock("TEA", StockType.COMMON, 0, 0, 100);
		trade = new Trade(stock, DATE_1, TradeType.BUY, 20, 280);

		boolean tradeBooked = tradeDao.addTrade(trade);

		assertTrue(tradeBooked);

		// Add 2nd trade
		stock = new Stock("GIN", StockType.PREFERRED, 8, 0.02, 100);
		trade = new Trade(stock, DATE_2, TradeType.SELL, 100, 570);

		tradeBooked = tradeDao.addTrade(trade);

		assertTrue(tradeBooked);
	}

	/**
	 * Test to check a Trade can be read from database through Trade DAO.
	 * 
	 */
	@Test(dependsOnMethods = "testAddTrade")
	public void testGetTrade() {

		Trade trade = tradeDao.getTrade(DATE_1);

		assertNotNull(trade);

		assertEquals(trade.getType(), TradeType.BUY);
		assertEquals(trade.getStocksQuantity(), 20);
		assertEquals(trade.getPrice(), 280.0);
		assertEquals(trade.getStock().getSymbol(), "TEA");
		assertEquals(trade.getStock().getParValue(), 100.0);

	}

	/**
	 * Test to check if all trades in database is fetched through Trade DAO.
	 * 
	 */
	@Test(dependsOnMethods = "testAddTrade")
	public void testGetAllTrades() {

		Map<Date, Trade> allTrades = tradeDao.getAllTrades();

		assertEquals(allTrades.values().size(), 2);

		Trade trade1 = allTrades.get(DATE_1);

		assertNotNull(trade1);

		Trade trade2 = allTrades.get(DATE_2);

		assertNotNull(trade2);

	}

	/**
	 * Test to check if all trades done on a given stock can be fetched through
	 * Trade DAO.
	 * 
	 */
	@Test(dependsOnMethods = "testAddTrade")
	public void testGetAllTradesForStock() {
		Map<Date, Trade> allTrades = tradeDao.getAllTradesForStock("TEA");
		assertEquals(allTrades.size(), 1);
	}

	/**
	 * Test to check if a given trade can be deleted from database through Trade
	 * DAO.
	 * 
	 */
	@Test(dependsOnMethods = { "testGetAllTrades", "testGetAllTradesForStock" })
	public void testDeleteTrade() {

		Map<Date, Trade> allTrades = tradeDao.getAllTrades();

		assertEquals(allTrades.size(), 2);

		Trade trade1 = allTrades.get(DATE_1);

		tradeDao.deleteTrade(trade1);

		allTrades = tradeDao.getAllTrades();

		assertEquals(allTrades.size(), 1);

	}

	/**
	 * Test if an exception is thrown in case trade for a given time stamp is
	 * not available in database.
	 * 
	 */
	@Test(expectedExceptions = { RuntimeException.class })
	public void testTradeNotFoundInDatabase() {
		Date now = new Date();
		tradeDao.getTrade(now);
	}

}
