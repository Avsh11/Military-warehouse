package org.warehouse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.warehouse.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel {
    @FXML
    private TextField hashlogin;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;

    // Metody

    // 1. Metoda do przechwytywania logowania - przycisk
    // Musimy sprawdzić czy poprawne są dane z pola hasła i loginu
    @FXML
    private void handleLogin(){
        String loginHash = hashlogin.getText();
        String PasswordField = password.getText();

        if (loginHash.isEmpty() || PasswordField.isEmpty()) {
            System.out.println("Login hash or password is empty. Try again.");
            return;
        }

        try(Connection conn = DBConnection.getConnection()){
            // Prepared statement
            String sql = "SELECT * FROM users WHERE loginHash = ? AND password = ?";
            PreparedStatement loginst = conn.prepareStatement(sql);

            loginst.setString(1, loginHash);
            loginst.setString(2, PasswordField);
            ResultSet rs = loginst.executeQuery();
            // Czy zwróciło nam jakiś rekord:
            if(rs.next()){
                boolean isAdmin = rs.getBoolean("isAdmin");
                // Informacje na konsoli gdyby pojawił się jakiś problem.
                System.out.println("Login successful for user Admin: " + isAdmin);
            }else{
                System.out.println("Login failed. Try again.");
            }
        }catch(SQLException e){
            System.out.println("Error, login failed " + e.getMessage());
        }
    }
}
