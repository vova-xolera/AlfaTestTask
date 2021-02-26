package ru.alfa.stockApp.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import ru.alfa.stockApp.exeptions.stockServiceExeptions.GIFReceivingException;
import ru.alfa.stockApp.exeptions.stockServiceExeptions.StockReceivingException;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StockServiceTests {

    @Autowired
    StockService stockService;

    @Autowired
    StockProxy stockProxy;

    @Autowired
    GifProxy gifProxy;

    private final LocalDate extremumDate = LocalDate.of(2021,2,24);
    static String JSON_FROM_GIF_FILE;
    static String GARBAGE_JSON = "just not JSON";
    static String JSON_FROM_STOCK_FILE;
    static final String TEST_GIF_FILE_NAME = "gifResponse.json";
    static final String TEST_STOCK_FILE_NAME = "stockResponseUSD2021-02-24.json";

    static final String GIF_URL_FROM_FILE = "https://media1.giphy.com/media/WoRz0xf3fUBWTWXUJ0/giphy.gif?cid=090a7f179f56365171be9f7b1c8a25c26e8de152ec537d80&rid=giphy.gif";

    @BeforeAll
    static void setUp(@Autowired Environment env) throws Exception {
        try {
            JSON_FROM_GIF_FILE = new String(Files.readAllBytes(Paths.get(env.getProperty("test.resources"), TEST_GIF_FILE_NAME)));
            JSON_FROM_STOCK_FILE = new String(Files.readAllBytes(Paths.get(env.getProperty("test.resources"), TEST_STOCK_FILE_NAME)));
        }
        catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Error with test files. Check existing of files and settings in application.properies");
        }

    }

    void setGIFProxyRespond(String tag, String result) {
        try {
            Mockito.when(gifProxy.getJsonWithRandomGifByTag(tag))
                    .thenReturn(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkExtractingOfJSONFromGIF() {
        setGIFProxyRespond("rich", JSON_FROM_GIF_FILE);
        assertEquals(stockService.extractGIFFromJSON(JSON_FROM_GIF_FILE),GIF_URL_FROM_FILE);
    }

    @Test
    void checkExceptionThenExtractingOfJSONFromGIF() {
        setGIFProxyRespond("rich", GARBAGE_JSON);
        assertThrows(GIFReceivingException.class, () -> stockService.extractGIFFromJSON(GARBAGE_JSON));
    }

    // Here start stock tests


    void setStockProxyRespond(String currency, LocalDate date, String response) {
        try {
            Mockito.when(stockProxy.getRateAndDate(currency, date.toString()))
                    .thenReturn(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkBooleanMethodWithExtremumDate(){
        assertFalse(stockService.isHigherThanYesterday(extremumDate));
        assertTrue(stockService.isHigherThanYesterday(extremumDate.plusDays(1)));
    }

    @Test
    void checkExceptionThenExtractingOfJSONFromStock() {
        setStockProxyRespond("USD", extremumDate, GARBAGE_JSON);
        assertThrows(StockReceivingException.class, () -> stockService.getRatesNode(GARBAGE_JSON));
    }

    @Test
    void checkCorrectnessOfCurrentValueInJSON() {
        setStockProxyRespond("USD", extremumDate, JSON_FROM_STOCK_FILE);
        assertEquals(73.524, stockService.getRateAtDate(extremumDate), "Error with getting rate in a special date!");
    }
}
