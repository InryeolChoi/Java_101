package com.java101.jdbc.step4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberFindAllMain{
    public static void main(String[] args) throws SQLException {
        List<Member> members = findAllMembers();
        for (Member member : members) {
            System.out.println(member);
        }
    }

    private static List<Member> findAllMembers() throws SQLException {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";
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
