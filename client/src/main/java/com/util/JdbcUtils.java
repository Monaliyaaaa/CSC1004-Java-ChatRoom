package com.util;


import com.messages.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcUtils {

    private PreparedStatement pstmt;
    private Connection connection;

    public JdbcUtils() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatroom?serverTimezone=UTC","root","900420");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public String search(String name, String pass, Connection con) throws SQLException { //A method to search accounts.
        String result= "";
        Statement sql = con.createStatement();
        ResultSet rs = sql.executeQuery("select * from userinfo;");
        while(rs.next()){
            if (rs.getString(1).equals(name)){     //Check the username first. Because username is unique.
                if (rs.getString(2).equals(pass)){  //Then verify the accuracy of password.
                    result= rs.getString(4);
                    break;
                }

            }
        }
        rs.close();
        sql.close();
        con.close();
        return result;


    }

    //While registering, we need to check whether the username is duplicated.
    public boolean duplicationName(String name, Connection con) throws SQLException{
        boolean r = true;
        Statement sql = con.createStatement();
        ResultSet rs = sql.executeQuery("select * from userinfo;");
        while(rs.next()){
            if (rs.getString(1).equals(name)){
                r = false;
                break;
            }
        }
        rs.close();
        sql.close();
        return r;

    }

    public boolean update(String sql, List<Object> params) throws SQLException {
        int resultLineNumber = -1;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i+1, params.get(i));
            }
        }
        resultLineNumber = pstmt.executeUpdate();
        return resultLineNumber > 0;
    }

    public ArrayList<User> getUserList(String name, Connection con) throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        Statement sql = con.createStatement();
        ResultSet rs = sql.executeQuery("select * from userinfo;");

        while(rs.next()){
            if (rs.getString(1).equals(name)){
            }else {
                User user = new User();
                user.setName(rs.getString(1));
                user.setPicture(rs.getString(4));
                list.add(user);
            }
        }
        rs.close();
        sql.close();

        return list;
    }

}
