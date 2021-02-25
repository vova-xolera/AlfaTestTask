package ru.alfa.stockApp.exeptions.stockServiceExeptions;

public class GIFReceivingException extends RuntimeException {

        public GIFReceivingException() {
            super("Error with GIF service");
        }

}
