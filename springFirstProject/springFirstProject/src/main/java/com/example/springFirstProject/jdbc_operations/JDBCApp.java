package com.example.springFirstProject.jdbc_operations;

import com.example.springFirstProject.jdbc_operations.utility.DBConnection;

public class JDBCApp {
    public static void main(String[] args) {
//        DBConnection con = new DBConnection();
        System.out.print("connection : " + DBConnection.getConnection());
    }
}
