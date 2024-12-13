package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ClientDataPage {
    private Stage stage;
    private Scene scene;
    @FXML
    public Label nameLabel;

    @FXML
    public Label numberLabel;

    @FXML
    public Label addressLabel;

    // Метод для подключения к базе данных PostgreSQL
    private Connection connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Touristique%20DB%20(Java)";  // Используем вашу базу данных
        String user = "postgres";
        String password = "3113";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void displayUserData() {
        int id = UserData.getId();

        String query = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String tel_number = rs.getString("tel_number");
                String address = rs.getString("address");

                nameLabel.setText(name);
                numberLabel.setText(tel_number);
                addressLabel.setText(address);
            } else {
                nameLabel.setText("Ім'я не знайдено");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientSearchVoucherPage.fxml"));
        Parent root = loader.load();
        ClientSearchVoucherPage clientSearchVoucherPageController = loader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToMyVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientVouchersPage.fxml"));
        Parent root = loader.load();
        ClientVouchersPage clientVouchersPageController = loader.getController();
        clientVouchersPageController.searchMyVouchers();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        UserData.setId(0);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
}
