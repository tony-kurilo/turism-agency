package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Manager11 {

    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label telNumberLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Button close;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField telNumberTextField;
    @FXML
    private TextField addressTextField;


    public void displayUserData() {
        String storedUserName = UserData.getUsername();

        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String username = parts[0];
                    String password = parts[1];
                    String name = parts[2];
                    String telNumber = parts[3];
                    String address = parts[4];
                    String agencyName = parts[5];
                    // Если найдено совпадение, отображаем имя в Label
                    if (storedUserName.equals(username)) {
                        usernameLabel.setText(username);
                        passwordLabel.setText(password);
                        nameLabel.setText(name);
                        telNumberLabel.setText(telNumber);
                        addressLabel.setText(address);
                        return;
                    }
                }
            }
            nameLabel.setText("Имя не найдено");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateClientData() {
        String filename = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = telNumberTextField.getText();
        String address = addressTextField.getText();
        String userName = UserData.getUsername();

        if (username.isEmpty() && password.isEmpty() && name.isEmpty() && telNumber.isEmpty() && address.isEmpty()) {
            System.out.println("Нет данных для изменения.");
            return;
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
                String[] tokens = line.split(","); // Предполагаем, что атрибуты разделены запятой
                String currentUserName = tokens[0].trim();

                if (currentUserName.equals(userName)) {
                    String oldUsername = tokens[0].trim();
                    String oldPassword = tokens[1].trim();
                    String oldName = tokens[2].trim();
                    String oldTelNumber = tokens[3].trim();
                    String oldAddress = tokens[4].trim();
                    String oldAgencyName = tokens[5].trim();

                    if (!username.isEmpty()) tokens[0] = username.trim();
                    if (!password.isEmpty()) tokens[1] = password.trim();
                    if (!name.isEmpty()) tokens[2] = name.trim();
                    if (!telNumber.isEmpty()) tokens[3] = telNumber.trim();
                    if (!address.isEmpty()) tokens[4] = address.trim();

                    lines.set(lines.size() - 1, String.join(",", tokens));

                    // Обновляем Label, используя старые значения при необходимости
                    usernameLabel.setText(!username.isEmpty() ? username : oldUsername);
                    passwordLabel.setText(!password.isEmpty() ? password : oldPassword);
                    nameLabel.setText(!name.isEmpty() ? name : oldName);
                    telNumberLabel.setText(!telNumber.isEmpty() ? telNumber : oldTelNumber);
                    addressLabel.setText(!address.isEmpty() ? address : oldAddress);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeWindow(javafx.event.ActionEvent actionEvent)throws IOException {


        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();


    }
}
