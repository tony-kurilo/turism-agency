package Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hrmanager11 {
    @FXML
    private Label loginLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label telNumberLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField telNumberTextField;

    public void displayUserData() {
        String storedUserName = UserData.getUsername();

        if (storedUserName != null) {
            String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String username = parts[0];
                        String password = parts[1];
                        String name = parts[2];
                        String telNumber = parts[3];
                        String agencyName = parts[4];
                        if (storedUserName.equals(username)) {
                            loginLabel.setText(username);
                            passwordLabel.setText(password);
                            nameLabel.setText(name);
                            telNumberLabel.setText(telNumber);
                            return;
                        }
                    }
                }
                nameLabel.setText("Имя не найдено");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            nameLabel.setText("Имя не найдено");
        }
    }
    public void updateManagerData() {
        String filename = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt";
        String username = loginTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = telNumberTextField.getText();
        String userName = UserData.getUsername();

        if (username.isEmpty() && password.isEmpty() && name.isEmpty() && telNumber.isEmpty()) {
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
                    String oldAgencyName = tokens[4].trim();

                    if (!username.isEmpty()) tokens[0] = username.trim();
                    if (!password.isEmpty()) tokens[1] = password.trim();
                    if (!name.isEmpty()) tokens[2] = name.trim();
                    if (!telNumber.isEmpty()) tokens[3] = telNumber.trim();


                    lines.set(lines.size() - 1, String.join(",", tokens));

                    loginTextField.setText(!username.isEmpty() ? username : oldUsername);
                    passwordLabel.setText(!password.isEmpty() ? password : oldPassword);
                    nameLabel.setText(!name.isEmpty() ? name : oldName);
                    telNumberLabel.setText(!telNumber.isEmpty() ? telNumber : oldTelNumber);

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
}
