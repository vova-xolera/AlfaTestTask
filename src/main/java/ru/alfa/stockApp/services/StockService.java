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
    private Environment environment;

    @Autowired
    private StockProxy stockProxy;

    @Autowired
    private GifProxy gifProxy;

    public Double getRateAt(String currencyToCheck, LocalDate date) throws JsonProcessingException {
        String Json = stockProxy.getRateAndDate(currencyToCheck, date.toString());
        return getRateFromNode(getRatesNode(Json));
    }

    private Double getRateFromNode(JsonNode ratesNode) {
        double inputToCheckNode = ratesNode.path(environment.getProperty("stock.input_currency")).asDouble();
        double baseCurrencyNode = ratesNode.path(environment.getProperty("stock.base_currency")).asDouble();
        return inputToCheckNode / baseCurrencyNode;
    }

    private JsonNode getRatesNode(String ratesJson) throws JsonProcessingException {
        return new ObjectMapper().readTree(ratesJson).path("rates");
    }

    public Boolean isHigherThanYesterday() throws JsonProcessingException {
        double currentRate = getRateAt("stock.input_currency", LocalDate.now());
        double yesterdayRate = getRateAt("stock.input_currency", LocalDate.now().minusDays(1));
        return currentRate > yesterdayRate;
    }

    public String getGifUrl() throws JsonProcessingException {
        if (isHigherThanYesterday()) {
            return getFixedHeightGifUrl(gifProxy.getJsonWithRandomGifByTag("rich"));
        }
        return getFixedHeightGifUrl(gifProxy.getJsonWithRandomGifByTag("broke"));
    }

    private String getFixedHeightGifUrl(String jsonWithGif) throws JsonProcessingException {
        return new ObjectMapper().readTree(jsonWithGif)
                .path("data").path("images").path("fixed_height").path("url")
                .asText();
    }
}
