package com.java101.jdbc.step1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionMain {
    public static void main(String[] args){
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";

        System.out.println("작업 디렉토리 : " + System.getProperty("user.dir"));
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection 구현체 : " + conn.getClass().getName());
            System.out.println("close 전 isClosed : " + conn.isClosed());
            System.out.println("autocommit : " + conn.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            if (conn != null) {
                try {
                    conn.close();}
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (conn != null) {
                System.out.println("close 후 isClosed : " + conn.isClosed());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
