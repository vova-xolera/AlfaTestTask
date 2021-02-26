package ru.alfa.stockApp.services;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class GifProxyTests {

    @Autowired
    GifProxy gifProxy;

    @Test
    void checkExceptionsThanCreateNewObject() {
        assertDoesNotThrow(() -> new JSONObject(gifProxy.getJsonWithRandomGifByTag("rich")));
        assertDoesNotThrow(() -> new JSONObject(gifProxy.getJsonWithRandomGifByTag("broke")));
    }
}
