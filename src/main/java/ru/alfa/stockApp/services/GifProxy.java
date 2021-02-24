package ru.alfa.stockApp.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="gifSource", url="${gif.url}")
public interface GifProxy {

    @GetMapping(value=  "/v1/gifs/random" +
                        "?api_key=${gif.api_key}" +
                        "&tag={tag}" +
                        "&rating=g")

    public String getJsonWithRandomGifByTag(@PathVariable("tag") String tag);

}
