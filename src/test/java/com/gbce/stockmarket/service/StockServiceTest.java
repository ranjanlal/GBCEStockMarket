package com.gbce.stockmarket.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gbce.stockmarket.beans.Stock;
import com.gbce.stockmarket.beans.Trade;
import com.gbce.stockmarket.constants.TradeType;
import com.gbce.stockmarket.dao.TradeDao;
import com.gbce.stockmarket.util.StockServiceTestUtil;

import config.StockMarketTestSpringConfig;

/**
 * TestNG Unit Test class for Stock Service
 * 
 * @author Ranjan Lal
 *
 */
@Test
@ContextConfiguration(classes = { StockMarketTestSpringConfig.class })
public class StockServiceTest extends AbstractTestNGSpringContextTests {

	/**
	 * Stock Service object. Injected via Spring Auto Wiring.
	 */
	@Autowired
	private StockService stockService;

	/**
	 * Trade DAO object. Injected via Spring Auto Wiring.
	 */
	@Autowired
	private TradeDao tradeDao;

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
	 * Tests application requirement - For a given stock, Record a trade, with
	 * timestamp, quantity, buy or sell indicator and price.
	 * 
	 */
	@Test
	public void testRecordTrade() {

		Stock stock = stockService.findStock("TEA");

		assertNotNull(stock);
		assertEquals(stock.getParValue(), 100.0);

		Trade trade = new Trade(stock, new Date(), TradeType.BUY, 20, 280);

		boolean tradeBooked = stockService.recordTrade(trade);

		assertTrue(tradeBooked);
	}

	/**
	 * Tests application requirement - For a given stock, Given any price as
	 * input, calculate the dividend yield.
	 * 
	 */
	@Test
	public void testCalculateDividendYield() {

		double dividendYield = stockService.calculateDividendYield("POP", 2.0);

		assertEquals(dividendYield, 4.0);

		dividendYield = stockService.calculateDividendYield("GIN", 8.0);

		assertEquals(dividendYield, 0.25);
	}

	/**
	 * Tests application requirement - For a given stock, Given any price as
	 * input, calculate the P/E Ratio.
	 * 
	 */
	@Test
	public void testCalculatePERatio() {

		double peRatio = stockService.calculatePERatio("ALE", 69.0);

		assertEquals(peRatio, 3.0);

	}

	/**
	 * Tests application requirement - For a given stock, Calculate Volume
	 * Weighted Stock Price based on trades in past 5 minutes.
	 * 
	 */
	@Test(dependsOnMethods = "testRecordTrade")
	public void testCalculateVolumeStockPrice() {

		double volumeStockPrice = stockService.calculateVolumeStockPrice("TEA", 5);

		assertEquals(volumeStockPrice, 280.0);

	}

	/**
	 * Tests application requirement - Calculate the GBCE All Share Index using
	 * the geometric mean of the Volume Weighted Stock Price for all stocks
	 * 
	 */
	@Test(dependsOnMethods = "testRecordTrade")
	public void testCalculateAllShareIndex() {

		double allShareIndex = stockService.calculateAllShareIndex();

		assertEquals(allShareIndex, 280.00);

	}

	/**
	 * Tests an exception is thrown in case the stock with symbol given as input
	 * is not available in database.
	 * 
	 */
	@Test(expectedExceptions = { RuntimeException.class })
	public void testStockNotFoundInDatabase() {
		stockService.findStock("&*PP$");
	}

	/**
	 * Tests an exception is thrown in case price input is invalid for a given
	 * stock for Dividend Yield calculation.
	 * 
	 */
	@Test(expectedExceptions = { RuntimeException.class })
	public void testInvalidPriceInputForDividendYieldCalculation() {
		stockService.calculateDividendYield("TEA", -1.0);
	}

	/**
	 * Tests an exception is thrown in case price input is invalid for a given
	 * stock for P/E Ratio calculation.
	 * 
	 */
	@Test(expectedExceptions = { RuntimeException.class })
	public void testInvalidPriceInputForPERatioCalculation() {
		stockService.calculatePERatio("TEA", -1.0);
	}

	/**
	 * Tests an exception is thrown in case Last Dividend property of stock is 0
	 * which indicates a divide by zero condition, invalid for P/E Ratio
	 * calculation formula.
	 * 
	 */
	@Test(expectedExceptions = { RuntimeException.class })
	public void testInvalidDividendInputForPERatioCalculation() {
		stockService.calculatePERatio("TEA", 20.0);
	}

}
