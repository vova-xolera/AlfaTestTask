package ru.alfa.stockApp.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="stockService", url="${stock.url}")
public interface StockProxy {

    @GetMapping(value=  "/historical/{date}.json" +
                        "?app_id=${stock.app_id}" +
                        "&symbols=${stock.base_currency}" +
                        "%2C{inputCurrency}")

    String getRateAndDate(
            @PathVariable("inputCurrency") String inputCurrency,
            @PathVariable("date") String date
    );
}
