package ru.alfa.stockApp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StockProxyTests {

    @Autowired
    StockProxy stockProxy;

    static final LocalDate DAY = LocalDate.of(2021, 2, 24);

    static final String CURRENCY = "USD";

    @Autowired
    Environment env;

    @Test
    void containsWrightValuesInASpecialDay() throws Exception {
            String ratesJson = stockProxy.getRateAndDate(CURRENCY, DAY.toString());
            JsonNode rates = new ObjectMapper().readTree(ratesJson).path("rates");

            assertEquals(1.0, rates.path(CURRENCY).asDouble(), "Error with access to stock");
            assertEquals(73.524, rates.path(env.getProperty("stock.base_currency")).asDouble(), "Error with access to stock");
    }

    @Test
    void containsNotZeroValuesToday() throws Exception {
            String ratesJson = stockProxy.getRateAndDate(CURRENCY, LocalDate.now().toString());
            JsonNode rates = new ObjectMapper().readTree(ratesJson).path("rates");
            if (env.getProperty("stock.base_currency") != null && env.getProperty("stock.input_currency") != null) {
                assertEquals(3.0, env.getProperty("stock.base_currency").length(), "Every currency must contains 3 symbols, check stock.base_currency");
                assertEquals(3.0, env.getProperty("stock.input_currency").length(), "Every currency must contains 3 symbols, check stock.input_currency");
            }
            else throw new Exception("One of currency fields is empty, check application.properties!");
            assertEquals(1.0, rates.path(CURRENCY).asDouble(), "Check input currency name!");
            assertNotEquals(0.0, rates.path(env.getProperty("stock.base_currency")).asDouble(), "Check base currency name!");
    }
}
