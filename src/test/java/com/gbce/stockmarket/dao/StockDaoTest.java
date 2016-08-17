package com.gbce.stockmarket.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.gbce.stockmarket.beans.Stock;
import com.gbce.stockmarket.constants.StockType;

import config.StockMarketTestSpringConfig;

/**
 * 
 * TestNG Unit Test class for Stock DAO
 * 
 * @author Ranjan Lal
 *
 */
@Test
@ContextConfiguration(classes = { StockMarketTestSpringConfig.class })
public class StockDaoTest extends AbstractTestNGSpringContextTests {

	/**
	 * Stock DAO object. Injected via Spring Auto Wiring.
	 */
	@Autowired
	private StockDao stockDao;

	/**
	 * Test to check if a Stock can be added to the database through Stock DAO.
	 * 
	 */
	@Test
	public void testAddStock() {

		Stock stock;
		boolean stockAdded;

		stock = new Stock("ABC", StockType.COMMON, 4, 0, 50);
		stockAdded = stockDao.addStock(stock);

		assertTrue(stockAdded);

		stock = new Stock("GIR", StockType.PREFERRED, 10, 0.04, 200);
		stockAdded = stockDao.addStock(stock);

		assertTrue(stockAdded);
	}

	/**
	 * Test to check if a stock with given symbol can be fetched from the
	 * database through Stock DAO.
	 * 
	 */
	@Test(dependsOnMethods = "testAddStock")
	public void testFindStock() {

		Stock stock = stockDao.findStock("GIR");

		assertNotNull(stock);

		assertEquals(stock.getType(), StockType.PREFERRED);
		assertEquals(stock.getParValue(), 200.0);

	}

	/**
	 * Test to check if all stocks stored in database can be fetched through
	 * Stock DAO.
	 * 
	 */
	@Test(dependsOnMethods = "testAddStock")
	public void testGetAllStocks() {

		List<Stock> allStocks = stockDao.getAllStocks();

		assertEquals(allStocks.size(), 7);

	}

	/**
	 * Test to check if a stock with given symbol does not exist in database.
	 * 
	 */
	@Test(expectedExceptions = { RuntimeException.class })
	public void testStockNotFoundInDatabase() {
		stockDao.findStock("ZZZP");
	}

}
