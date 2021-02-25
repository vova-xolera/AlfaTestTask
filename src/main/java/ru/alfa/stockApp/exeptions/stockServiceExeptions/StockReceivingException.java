package ru.alfa.stockApp.exeptions.stockServiceExeptions;

public class StockReceivingException extends RuntimeException {

        public StockReceivingException() {
            super("Error with stock service");
        }

}
