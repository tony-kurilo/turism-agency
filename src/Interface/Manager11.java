package Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        // Получение username из UserSession
        String storedClientName = ClientData.getClientName();

        // Путь к файлу с данными пользователя
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";

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
                    String address = parts[4];
                    // Если найдено совпадение, отображаем имя в Label
                    if (storedClientName.equals(name)) {
                        usernameLabel.setText(username);
                        passwordLabel.setText(password);
                        nameLabel.setText(name);
                        telNumberLabel.setText(telNumber);
                        addressLabel.setText(address);
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
    }

    public void updateClientData() {
        String filename = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = telNumberTextField.getText();
        String address = addressTextField.getText();
        String clientName = ClientData.getClientName();

        if (username.isEmpty() && password.isEmpty() && name.isEmpty() && telNumber.isEmpty() && address.isEmpty()) {
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
                String currentClientName = tokens[2].trim();

                if (currentClientName.equals(clientName)) {
                    // Найден нужный клиент, обновляем данные
                    if (!username.isEmpty()) tokens[0] = username.trim();
                    if (!password.isEmpty()) tokens[1] = password.trim();
                    if (!name.isEmpty()) tokens[2] = name.trim();
                    if (!telNumber.isEmpty()) tokens[3] = telNumber.trim();
                    if (!address.isEmpty()) tokens[4] = address.trim();

                    // Обновляем строку в списке
                    lines.set(lines.size() - 1, String.join(",", tokens));
                    displayUserData();
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
