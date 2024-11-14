package org.szylica;

import java.sql.*;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/classicmodels";
    private static final String user = "root";
    private static final String password = "szPYa&bqH5lFShl";


    public Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection != null && !connection.isClosed()) {
                System.out.println("Połączenie z bazą danych powiodło się.");
                return connection;
            }
        } catch (SQLException e) {
            System.err.println("Błąd połączenia z bazą danych: " + e.getMessage());
        }
        return null;
    }

    //TODO: obsługa wyjątków takich jak powtarzanie się primary key, program musi sprawdzać czy nie istenieje już dany primary key

    public void insertRecord(String stmt){
        try(Connection connection = connect()){
            if (connection != null) {
                connection.createStatement().execute(stmt);
                System.out.println("Zapytanie wykonane pomyślnie.");
            } else {
                System.err.println("Brak połączenia, zapytanie nie zostało wykonane.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void selectRecord(String stmt){
        /*
        try(Connection connection = DriverManager.getConnection(url, user, password)){
            if (connection != null) {

                connection.createStatement().execute(stmt);

                System.out.println("Zapytanie wykonane pomyślnie.");
            } else {
            System.err.println("Brak połączenia, zapytanie nie zostało wykonane.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
         */
    }

    public void updateRecord(String stmt){}
    public void deleteRecord(String tableName, String columnName, Object value){
        String stmt = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(stmt)) {

            // Ustawienie wartości parametru zapytania w zależności od typu wartości
            if (value instanceof Integer) {
                preparedStatement.setInt(1, (Integer) value);
            } else if (value instanceof String) {
                preparedStatement.setString(1, (String) value);
            } else if (value instanceof Double) {
                preparedStatement.setDouble(1, (Double) value);
            } else {
                throw new IllegalArgumentException("Nieobsługiwany typ wartości: " + value.getClass().getName());
            }

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Usunięto " + rowsAffected + " rekord(ów) z tabeli " + tableName + ".");
            } else {
                System.out.println("Nie znaleziono rekordu spełniającego podany warunek.");
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas usuwania rekordu: " + e.getMessage());
        }



    }
}
