package com.java101.jdbc.step4;

import java.sql.*;

public class MemberFindMain {
    public static void main(String[] args) throws SQLException {
        Member member = findMember(5L);
        if (member == null) {
            System.out.println("멤버를 찾을 수 없습니다.");
            return ;
        }
        System.out.println(member.getName());
        System.out.println(member.getEmail());
    }

    private static Member findMember(long id) throws SQLException {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";
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
}
