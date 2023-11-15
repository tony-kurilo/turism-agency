package Interface;

import Classes.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Manager1 {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField numberTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private ChoiceBox clientEditChoiceBox;


    private ArrayList<Client> clients = new ArrayList<>();
    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager2.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager3.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToData(javafx.event.ActionEvent actionEvent) throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("manager4.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager4.fxml"));
        Parent root = loader.load();

        Manager4 manager4controller = loader.getController();
        manager4controller.displayUserData();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToDeleteClientScene(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager11.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Название вашего нового окна"); // Установите заголовок нового окна
        newStage.setScene(new Scene(root));
        // Показываем новое окно
        newStage.show();
    }
    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }

    public void createClient(ActionEvent actionEvent) throws IOException {
        // Считывание данных из текстовых полей
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = numberTextField.getText();
        String address = addressTextField.getText();

        // Создание объекта Client и установка значений атрибутов
        Client client = new Client(username, password, name, telNumber, address);

        // Добавление клиента в коллекцию
        clients.add(client);

        // Добавление коллекции в файл с данными
        saveDataToFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", clients);
    }

    private void saveDataToFile(String filename, ArrayList<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (Client client : clients) {
                // Форматирование строки перед записью в файл
                String line = String.format("%s,%s,%s,%s,%s",
                        client.getUsername(), client.getPassword(), client.getName(),
                        client.getTelNumber(), client.getAddress());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибок записи в файл
        }
    }


}
