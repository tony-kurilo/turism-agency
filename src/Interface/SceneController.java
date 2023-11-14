package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    public void initialize() {
        // Добавьте обработчик событий для кнопки
        loginButton.setOnAction(actionEvent -> {
            try {
                login(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void login(javafx.event.ActionEvent actionEvent) throws IOException {
        String username = loginTextField.getText();
        String password = passwordTextField.getText();

        if (checkCredentials("Interface/clients.txt", username, password)) {
            switchToClient(actionEvent);
        } else if (checkCredentials("Interface/managers.txt", username, password)) {
            switchToManager(actionEvent);
        } else {
            errorLabel.setText("Невірний логін або пароль");
        }
    }

    private boolean checkCredentials(String filename, String username, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }



    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClient(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("client1.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToManager(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager1.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }

}
