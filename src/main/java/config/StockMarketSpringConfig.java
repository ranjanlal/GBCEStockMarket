package config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.gbce.stockmarket.beans.Stock;
import com.gbce.stockmarket.constants.StockType;

/**
 * Spring Java context configuration for the GBCE Stock Market application.
 * 
 * @author Ranjan Lal
 *
 */
@Configuration
@ComponentScan
public class StockMarketSpringConfig {

	/**
	 * Creates Spring Bean to represent an in-memory database table to hold
	 * Stock Data. Initialized with sample data to be used in the application.
	 * 
	 * @return Map representing Stock data
	 */
	@Bean(name = "stocksDatabase")
	public Map<String, Stock> getStockData() {

		Map<String, Stock> stocks = new ConcurrentHashMap<>();

		stocks.put("TEA", new Stock("TEA", StockType.COMMON, 0, 0, 100));
		stocks.put("POP", new Stock("POP", StockType.COMMON, 8, 0, 100));
		stocks.put("ALE", new Stock("ALE", StockType.COMMON, 23, 0, 60));
		stocks.put("GIN", new Stock("GIN", StockType.PREFERRED, 8, 0.02, 100));
		stocks.put("JOE", new Stock("JOE", StockType.COMMON, 13, 0, 250));

		return stocks;
	}

}
