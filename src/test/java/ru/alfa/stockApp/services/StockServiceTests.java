package ru.alfa.stockApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class StockServiceTests {

    @Autowired
    StockService stockService;

    @BeforeEach
    public void setUp() {

    }

    private final LocalDate extremumDate = LocalDate.of(2021,2,24);

    @Test
    void ifTodayRateBigger(){
    }
}
