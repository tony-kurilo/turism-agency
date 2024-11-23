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
import java.sql.*;
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

    public void displayUserData() {
        int userId = UserData.getId(); // Получаем ID пользователя из UserData
        String query = """
        SELECT 
            u.username, 
            u.password, 
            c.name, 
            c.tel_number, 
            c.address
        FROM 
            users u
        INNER JOIN 
            clients c 
        ON 
            u.id = c.id
        WHERE 
            u.id = ?
    """;

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId); // Устанавливаем ID пользователя в запрос
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                usernameLabel.setText(resultSet.getString("username"));
                passwordLabel.setText(resultSet.getString("password"));
                nameLabel.setText(resultSet.getString("name"));
                telNumberLabel.setText(resultSet.getString("tel_number"));
                addressLabel.setText(resultSet.getString("address"));
            } else {
                nameLabel.setText("Данные не найдены");
            }
        } catch (SQLException e) {
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
