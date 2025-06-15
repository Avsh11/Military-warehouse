package org.warehouse.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.warehouse.util.DBConnection;
import org.warehouse.util.UserInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class UserController {
    @FXML
    public Label balanceLabel;
    @FXML
    public Label welcomeLabel;

    // Inicjalizacje pól z FXMLa, buttony itd.

    @FXML
    private Button franceButton;
    @FXML
    private Button germanyButton;
    @FXML
    private Button usaButton;
    @FXML
    private Button unitedkingdomButton;
    @FXML
    private Button logoutButton;
    @FXML
    public Button orderHistoryButton;

    // Metoda initialize jest tutaj potrzebna żeby załadować nam obrazki na buttony.

    @FXML
    public void initialize() {
        handleFranceImage();
        handleGermanyImage();
        handleUsaImage();
        handleUnitedkingdomImage();

        String sql = "SELECT balance, loginHash FROM funds JOIN users ON funds.userID = users.userID WHERE users.userID = ?;";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,UserInfo.userID);
            ResultSet result = stmt.executeQuery();
            double balance = 0;
            String username = "";

            while (result.next()) {
                balance = result.getDouble("balance");
                username = result.getString("loginHash");
            }

            // Formatka wyświetlania środków.

            DecimalFormat formatter = new DecimalFormat("#,###.##");
            String formattedBalance = formatter.format(balance) + " $";
            balanceLabel.setText(formattedBalance);
            welcomeLabel.setText("Welcome " + username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Chcemy załadować panel (nowy widok) - ProductCategoryPanel i przekazać mu informacje o wybranym kraju.
    // controller.handleSetCountry(nationalityID) - przekazujemy id kraju.
    // Zmodyfikowanie metody do przenoszenia na następny widok. (AdminPanel).

    private void handleSetProductCategoryPanel(int nationalityID, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/ProductCategoryPanel.fxml"));
            Parent primaryFXML = fxmlLoader.load();
            ProductCategoryController controller = fxmlLoader.getController();
            controller.handleSetCountry(nationalityID);
            Scene scene = new Scene(primaryFXML);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Chcemy przenieść się do jednego panelu niezależnie którą narodowość wybierzemy.
    // Potem już chcemy mieć spółki i produkty danego kraju.
    // Lecimy po IDkach kraju.
    // Mam połączenia tabel więc później nie będzie problemu połączyć wszystko w całość (produkty, spółki na kraj).

    // Wywołanie metody handleSetProductCategoryPanel i dajemy jej ID kraju.

    @FXML
    private void handleFranceClick() {
        Stage stage = (Stage) franceButton.getScene().getWindow();
        handleSetProductCategoryPanel(12, stage);
    }

    @FXML
    private void handleGermanyClick() {
        Stage stage = (Stage) germanyButton.getScene().getWindow();
        handleSetProductCategoryPanel(2, stage);
    }

    @FXML
    private void handleUsaClick() {
        Stage stage = (Stage) usaButton.getScene().getWindow();
        handleSetProductCategoryPanel(33, stage);
    }

    @FXML
    private void handleUnitedkingdomClick() {
        Stage stage = (Stage) unitedkingdomButton.getScene().getWindow();
        handleSetProductCategoryPanel(32, stage);
    }

    // Obrazki na buttony, nie zapomnieć dodać odpowiedniej linijki w panelu poprzednim w który ładujemy ten - załadowanie css.
    // Chcemy mieć obrazki na buttonach dla przejrzystości i klarowności w GUI.

    public void handleFranceImage() {
        Image image = new Image(getClass().getResourceAsStream("/image/fr/fr.png"));
        ImageView imageView = new ImageView(image);
        imageView.getStyleClass().add("flagSize");
        franceButton.setGraphic(imageView);
    }

    public void handleGermanyImage() {
        Image image = new Image(getClass().getResourceAsStream("/image/de/de.png"));
        ImageView imageView = new ImageView(image);
        imageView.getStyleClass().add("flagSize");
        germanyButton.setGraphic(imageView);
    }

    public void handleUsaImage() {
        Image image = new Image(getClass().getResourceAsStream("/image/us/us.png"));
        ImageView imageView = new ImageView(image);
        imageView.getStyleClass().add("flagSize");
        usaButton.setGraphic(imageView);
    }

    public void handleUnitedkingdomImage() {
        Image image = new Image(getClass().getResourceAsStream("/image/uk/uk.png"));
        ImageView imageView = new ImageView(image);
        imageView.getStyleClass().add("flagSize");
        unitedkingdomButton.setGraphic(imageView);
    }

    // Te same metody obsługujące button i ładujące nowy widok.

    @FXML
    private void handleOrderHistoryClick() throws IOException {
        Stage stage = (Stage) orderHistoryButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/OrderHistoryUserPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogoutClick() throws IOException {
        UserInfo.userID = 0;
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/LoginPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

}
