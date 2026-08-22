package com.java101.jdbc.step4;

import java.sql.*;

public class MemberFindMain {
    public static void main(String[] args) throws SQLException {
        MemberDao memberDao = new MemberDao();
        Member member = memberDao.findById(5L);
        if (member == null) {
            System.out.println("멤버를 찾을 수 없습니다.");
            return ;
        }
        System.out.println(member.getName());
        System.out.println(member.getEmail());
    }
}
