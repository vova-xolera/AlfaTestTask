package ru.alfa.stockApp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.alfa.stockApp.exeptions.stockServiceExeptions.GIFReceivingException;
import ru.alfa.stockApp.exeptions.stockServiceExeptions.StockReceivingException;

import java.time.LocalDate;

@Service
public class StockService {

    @Autowired
    private Environment environment;

    @Autowired
    private StockProxy stockProxy;

    @Autowired
    private GifProxy gifProxy;

    /**
     * Here I used protected for encapsulation
     * end test access at the same time.
    **/

    protected Boolean isHigherThanYesterday(LocalDate date) {
        double currentRate = getRateAtDate(date);
        double yesterdayRate = getRateAtDate(date.minusDays(1));
        return currentRate > yesterdayRate;
    }

    protected Double getRateAtDate(LocalDate date) {
        String Json = stockProxy.getRateAndDate("stock.input_currency", date.toString());
        return getRateFromNode(getRatesNode(Json));
    }

    private Double getRateFromNode(JsonNode ratesNode) {
        return ratesNode.path(environment.getProperty("stock.base_currency")).asDouble();
    }

    protected JsonNode getRatesNode(String ratesJson) {
        try {
            return new ObjectMapper().readTree(ratesJson).path("rates");
        }
        catch (Exception e) {
             throw new StockReceivingException();
        }
    }

    public String getGifUrl() {
        if (isHigherThanYesterday(LocalDate.now())) {
            return extractGIFFromJSON(gifProxy.getJsonWithRandomGifByTag("rich"));
        }
        return extractGIFFromJSON(gifProxy.getJsonWithRandomGifByTag("broke"));
    }

    /**
     * Here i use "original" to identify
     * gif. There a lot of types of one.
     * You can fund example of JSON in
     * file in test/resources.
     **/

    protected String extractGIFFromJSON(String jsonWithGif) {
        try {
           return new ObjectMapper().readTree(jsonWithGif)
                    .path("data").path("images").path("original").path("url").asText();
        }
        catch (Exception e) {
            throw new GIFReceivingException();
        }
    }
}
