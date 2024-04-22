package com.wama;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogClass {
    // See getLogger() method below for this variable usage
    private static final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();

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
        /*
            * This method is used to get the logger for the class that called the logging method.
            * It uses the stack trace to determine the calling class name to place in log lines.
            * We use a ThreadLocal to store the logger for each thread, so we only need to create
            * a logger once per class per thread, and then we don't have blocking log calls.
            * Like a thread-safe Singleton.
         */
        Logger logger  = loggerThreadLocal.get();
        if (logger == null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String callingClassName = stackTrace[3].getClassName();
            logger = LoggerFactory.getLogger(callingClassName);
            loggerThreadLocal.set(logger);
        }
        return logger;
    }
}