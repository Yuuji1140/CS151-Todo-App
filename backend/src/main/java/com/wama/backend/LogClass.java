package com.wama.backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogClass {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(LogClass.class);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message, Exception e) {
        logger.error(message, e);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }
}