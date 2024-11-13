package org.example;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        String sql = "select * from employees where officeCode=3";
        String url = "jdbc:mysql://localhost:3306/classicmodels";
        String user = "root";
        String password = "szPYa&bqH5lFShl";

        try(Connection connection = DriverManager.getConnection(url, user, password)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                System.out.println(resultSet.getString("lastname"));
                System.out.println(resultSet.getString("firstname"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}