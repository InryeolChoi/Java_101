package com.java101.jdbc.step2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberTableMain {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();

            String createTableSql = "CREATE TABLE IF NOT EXISTS members (\n" +
                    "    id BIGINT PRIMARY KEY,\n" +
                    "    name VARCHAR(100) NOT NULL,\n" +
                    "    email VARCHAR(255) NOT NULL UNIQUE\n" +
                    ")";

            System.out.println("성공여부 : " + stmt.executeUpdate(createTableSql));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null & conn != null) {
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
