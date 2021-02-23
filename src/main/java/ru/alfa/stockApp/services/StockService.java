package ru.alfa.stockApp.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StockService {

   @Autowired
    private Environment env;

    @Autowired
    private StockProxy stockProxy;

    public Double getRateAt(String currencyToCheck, LocalDate date) throws JsonProcessingException {
        String Json = stockProxy.getRateAndDate(currencyToCheck, date.toString());
        return getRateFromNode(getRatesNode(Json));
    }

    private Double getRateFromNode(JsonNode ratesNode) {
        double inputToCheckNode = ratesNode.path(env.getProperty("stock.input_currency")).asDouble();
        double baseCurrencyNode = ratesNode.path(env.getProperty("stock.base_currency")).asDouble();
        return inputToCheckNode / baseCurrencyNode;
    }

    private JsonNode getRatesNode(String ratesJson) throws JsonProcessingException {
        return new ObjectMapper().readTree(ratesJson).path("rates");
    }

}
