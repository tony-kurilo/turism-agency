package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

    public void login(ActionEvent actionEvent) throws IOException {
        String username = loginTextField.getText();
        String password = passwordTextField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Заполните оба поля.");
            return;
        }
        boolean clientMatch = checkCredentials("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", username, password);
        boolean managerMatch = checkCredentials("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt", username, password);
        boolean hrManagerMatch = checkCredentials("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hrmanagers.txt", username, password);


        if (clientMatch && !managerMatch && !hrManagerMatch) {
            UserData.setUsername(username);
            UserData.setPassword(password);
            switchToClient(actionEvent);
        } else if (managerMatch && !clientMatch && !hrManagerMatch) {
            UserData.setUsername(username);
            UserData.setPassword(password);
            switchToManager(actionEvent);
        } else if (hrManagerMatch && !clientMatch && !managerMatch) {
            UserData.setUsername(username);
            UserData.setPassword(password);
            switchToHRManager(actionEvent);
        } else {
            errorLabel.setText("Неверный логин или пароль.");
        }
    }

    private boolean checkCredentials(String filename, String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void switchToClient(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Client3.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
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
        /*Parent root = FXMLLoader.load(getClass().getResource("manager1.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/
    }
    public void switchToHRManager(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hrmanager1.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
}

