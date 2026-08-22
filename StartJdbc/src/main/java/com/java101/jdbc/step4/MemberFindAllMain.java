package com.java101.jdbc.step4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberFindAllMain{
    public static void main(String[] args) throws SQLException {
        MemberDao memberDao = new MemberDao();
        List<Member> members = memberDao.findAll();
        for (Member member : members) {
            System.out.println(member);
        }
    }

}
