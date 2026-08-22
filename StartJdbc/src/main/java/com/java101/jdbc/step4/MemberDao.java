package com.java101.jdbc.step4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {
    String url = "jdbc:h2:file:./StartJdbc/data/java101";
    String username = "sa";
    String password = "";

    public Member findById(long id) throws SQLException {
        String sql = "SELECT id, name, email FROM members WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                long memberId = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                return new Member(memberId, name, email);
            }
        }
    }

    public List<Member> findAll() throws SQLException {
        String sql = "SELECT * FROM members ORDER BY id ASC";
        List<Member> members = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next()) {
                    members.add(new Member(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
                }
            }
        }
        return members;
    }
}
