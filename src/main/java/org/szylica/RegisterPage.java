package org.szylica;

import javax.swing.*;

public class RegisterPage extends JFrame {
    DatabaseConnection conn = new DatabaseConnection();

    public RegisterPage() {

    }

    public void add(){
        conn.insertRecord("insert into customers(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit) values (10311,'Atelier graphique','Schmitt','Carine ','40.32.2555','54, rue Royale',NULL,'Nantes',NULL,'44000','France',1370,'21000.00');");
    }

    public void delete(){
        conn.deleteRecord("customers", "customerNumber", 10311);
    }
}