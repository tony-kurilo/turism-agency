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
        // Получение username из UserSession
        String storedUserName = UserData.getUsername();

        if (storedUserName != null) {
            // Путь к файлу с данными пользователя
            String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt";

            // Поиск данных пользователя в файле
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
                        // Если найдено совпадение, отображаем имя в Label
                        if (storedUserName.equals(username)) {
                            loginLabel.setText(username);
                            passwordLabel.setText(password);
                            nameLabel.setText(name);
                            telNumberLabel.setText(telNumber);
                            return;
                        }
                    }
                }
                // Если не найдено совпадение
                nameLabel.setText("Имя не найдено");
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибок чтения файла
            }
        } else {
            // Если storedUserName == null
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
            // Если все поля пусты, нет необходимости в изменениях
            System.out.println("Нет данных для изменения.");
            return;
        }

        // Чтение данных из файла и поиск клиента по атрибуту ClientName
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

                    // Найден нужный клиент, обновляем данные
                    if (!username.isEmpty()) tokens[0] = username.trim();
                    if (!password.isEmpty()) tokens[1] = password.trim();
                    if (!name.isEmpty()) tokens[2] = name.trim();
                    if (!telNumber.isEmpty()) tokens[3] = telNumber.trim();


                    // Обновляем строку в списке
                    lines.set(lines.size() - 1, String.join(",", tokens));

                    // Обновляем Label, используя старые значения при необходимости
                    loginTextField.setText(!username.isEmpty() ? username : oldUsername);
                    passwordLabel.setText(!password.isEmpty() ? password : oldPassword);
                    nameLabel.setText(!name.isEmpty() ? name : oldName);
                    telNumberLabel.setText(!telNumber.isEmpty() ? telNumber : oldTelNumber);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Перезаписываем файл с обновленными данными
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
