package org.szylica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegisterPage extends JFrame {
    DatabaseConnection conn = new DatabaseConnection();

    public RegisterPage() {
        JFrame registerFrame = new JFrame();

        registerFrame.setTitle("Register");
        registerFrame.setSize(400, 200);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Etykieta i pole tekstowe dla loginu
        JLabel userLabel = new JLabel("Login:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userText, gbc);

        // Etykieta i pole tekstowe dla hasła
        JLabel passwordLabel = new JLabel("Hasło:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordText, gbc);

        // Przycisk logowania
        JButton registerButton = new JButton("Zarejestruj");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = userText.getText();
                String password = new String(passwordText.getPassword()); // Pobieranie hasła

                // Sprawdzenie, czy pola nie są puste
                if (login.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Login i hasło nie mogą być puste.");
                } else {
                    // Dodanie użytkownika do bazy danych
                    int newId = conn.returnMaxId("customers")+1;
                    conn.insertRecord("insert into customers(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit, password) values ("+newId+",'"+login+"',NULL,NULL,NULL, NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'"+password+"');");
                    JOptionPane.showMessageDialog(null, "Użytkownik został zarejestrowany.");
                }
            }
        });

        // Dodanie panelu do okna
        registerFrame.add(panel);




        registerFrame.setVisible(true);

    }

}


