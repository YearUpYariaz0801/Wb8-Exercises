package com.pluralsight;

public class Main {

   final static Logger logger = Logger.getLogger(App.class);


    public static void main(String[] args) {
        System.out.println("Hello world!");
        logMeLikeYouDo("☕ ");

    }

    private static void logMeLikeYouDo(String input) {
        if (logger.isDebugEnabled()) {
            logger.debug("This is debug : " + input);
        }
        if (logger.isInfoEnabled()) {
            logger.info("This is info : " + input);
        }
        logger.warn("This is warn : " + input);
        logger.error("This is error : " + input);
        logger.fatal("This is fatal : " + input);


    }
}