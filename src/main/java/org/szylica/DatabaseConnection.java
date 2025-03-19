package org.szylica;

import java.sql.*;

/**
 * Class helps with connection and operations on database, here you can declare url, user and password to database
 *
 * @author szylica
 */
public class DatabaseConnection {
    private static final String url = "connection";
    private static final String user = "root";
    private static final String password = "pass";


    /**
     * Uses given in class url, user and password to create Connection to database
     * @return Connection to database
     */
    public Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection != null && !connection.isClosed()) {
                System.out.println("Połączenie z bazą danych powiodło się. (in connect method) ");
                return connection;
            }
        } catch (SQLException e) {
            System.err.println("Błąd połączenia z bazą danych: " + e.getMessage());
        }
        return null;
    }

    //TODO: obsługa wyjątków takich jak powtarzanie się primary key, program musi sprawdzać czy nie istenieje już dany primary key

    /**
     * Opens connection with database using and execute statement from param
     * @param stmt String local variable, which contain SQL statement which is sending to database
     */
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

    //WORK IN PROGRESS
    /*public void selectRecord(String stmt){

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

    }*/

    // Helper method, which helps me to get name of primary key column

    /**
     * Method gets the name of the primary key column in table
     * @param tableName name of the table we are searching in
     * @return String value - name of the primary key column
     */
    public String getPrimaryKeyColumnName(String tableName) {
        String primaryKeyColumn = null;

        try (Connection connection = connect()) {


            DatabaseMetaData metaData = connection.getMetaData();

            // Pobranie kluczy głównych dla danej tabeli
            ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName);

            // Jeśli klucz główny istnieje, pobierz nazwę kolumny
            if (resultSet.next()) {
                primaryKeyColumn = resultSet.getString("COLUMN_NAME");
            }

            resultSet.close(); // Zamknięcie resultSet

        } catch (SQLException e) {
            System.err.println("Błąd połączenia z bazą danych: " + e.getMessage());
        }

        return primaryKeyColumn;
    }

    //TODO: sprawdzanie czy przekazana w argumencie tabela istnieje

    /**
     * Method searches highest value from the primary key column
     * @param table name of the table where you are searching
     * @return int value which is max Id value in table
     */
    public int returnMaxId(String table){


        String idColumnName = getPrimaryKeyColumnName(table);
        String stmt = "SELECT MAX("+idColumnName+") AS max_id FROM "+table+";";
        int maxId = -1;

        try(Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(stmt)){

            if (resultSet.next()) {
                maxId = resultSet.getInt("max_id");
            }

        } catch (SQLException e){
            System.err.println("Błąd połączenia z bazą danych: " + e.getMessage());
        }

        return maxId;
    }

    public void updateRecord(String stmt){}

    /**
     * Deletes all given values in given column from given table
     * @param tableName String value - table name
     * @param columnName String value - column name
     * @param value String value which is gonna be deleted
     */
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
