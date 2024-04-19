package com.wama;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogClass {
    public static void info(String message) {
        Logger logger = getLogger();
        logger.info(message);
    }

    public static void error(String message, Exception e) {
        Logger logger = getLogger();
        logger.error(message, e);
    }

    public static void error(String message) {
        Logger logger = getLogger();
        logger.error(message);
    }

    public static void debug(String message) {
        Logger logger = getLogger();
        logger.debug(message);
    }

    public static void warn(String message) {
        Logger logger = getLogger();
        logger.warn(message);
    }

    private static Logger getLogger() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callingClassName = stackTrace[3].getClassName();
        return LoggerFactory.getLogger(callingClassName);
    }
}