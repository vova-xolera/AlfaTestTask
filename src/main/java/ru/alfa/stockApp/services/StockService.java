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

    public Boolean isHigherThanYesterday() {
        double currentRate = getRateAt(LocalDate.now());
        double yesterdayRate = getRateAt(LocalDate.now().minusDays(1));
        System.out.println(currentRate + " " + yesterdayRate);
        return currentRate > yesterdayRate;
    }

    public Double getRateAt(LocalDate date) {
            String Json = stockProxy.getRateAndDate("stock.input_currency", date.toString());
            return getRateFromNode(getRatesNode(Json));
    }

    private Double getRateFromNode(JsonNode ratesNode) {
        return ratesNode.path(environment.getProperty("stock.base_currency")).asDouble();
    }

    private JsonNode getRatesNode(String ratesJson) {
        try {
            return new ObjectMapper().readTree(ratesJson).path("rates");
        }
        catch (Exception e) {
             throw new StockReceivingException();
    }
    }

    public String getGifUrl() {
        if (isHigherThanYesterday()) {
            String GifURL = getFixedHeightGifUrl(gifProxy.getJsonWithRandomGifByTag("rich"));
            System.out.println(GifURL);
            return GifURL;
        }
        String GifURL = getFixedHeightGifUrl(gifProxy.getJsonWithRandomGifByTag("broke"));
        System.out.println(GifURL);
        return GifURL;
    }

    private String getFixedHeightGifUrl(String jsonWithGif) {
        try {
           return new ObjectMapper().readTree(jsonWithGif)
                    .path("data").path("images").path("fixed_height").path("url")
                    .asText();
        }
        catch (Exception e) {
            throw new GIFReceivingException();
        }
    }
}
