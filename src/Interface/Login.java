package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.*;

public class Login {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private UserData userModel;
    @FXML
    public TextField loginTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public Label errorLabel;

    // Метод для подключения к базе данных
    private Connection connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Touristique%20DB%20(Java)";
        String user = "postgres";
        String password = "3113";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void login(ActionEvent actionEvent) throws IOException {
        String username = loginTextField.getText();
        String password = passwordTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Заповніть оба поля.");
            return;
        }
        // Проверка учетных данных в базе данных
        boolean isValidUser = checkCredentialsInDatabase(username, password);
        if (isValidUser) {
            String role = getUserRole(username);
            int id = getUserId(username);
            if ("client".equals(role)) {
                UserData.setUsername(username);
                UserData.setPassword(password);
                UserData.setId(id);
                switchToClient(actionEvent);
            } else if ("manager".equals(role)) {
                UserData.setUsername(username);
                UserData.setPassword(password);
                UserData.setId(id);
                switchToManager(actionEvent);
            } else {
                errorLabel.setText("Неизвестная роль.");
            }
        } else {
            errorLabel.setText("Неверный логин или пароль.");
        }
    }
    // Метод для проверки логина и пароля в базе данных
    private boolean checkCredentialsInDatabase(String username, String password) {
        String query = "SELECT username, password FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Если пользователь найден, возвращаем true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Метод для получения роли пользователя (например, client или manager)
    private String getUserRole(String username) {
        String query = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для получения id пользователя из базы данных
    private int getUserId(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Возвращаем id пользователя как int
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public void switchToClient(javafx.event.ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Client3.fxml"));
        Parent root = loader.load();
        Client3 client3Controller = loader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToManager(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager1.fxml"));
        Parent root = loader.load();

        Manager1 manager1Controller = loader.getController();
        manager1Controller.scanClientDataFile();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

