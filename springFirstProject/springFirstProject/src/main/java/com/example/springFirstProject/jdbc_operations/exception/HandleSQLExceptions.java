package com.example.springFirstProject.jdbc_operations.exception;

import java.util.logging.Logger;

public class HandleSQLExceptions extends Exception{
    public static void handleSqlException (String s, Exception e, Logger logger) {
        logger.warning(String.format(s, e.getMessage(), logger));
        throw new RuntimeException(e);
    }
}
