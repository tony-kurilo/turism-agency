package Interface;

import Classes.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Manager1 {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label alertLabel;
    @FXML
    private TableColumn<Client, String> usernameColumn;
    @FXML
    private TableColumn<Client, String> nameColumn;
    @FXML
    private TableColumn<Client, String> telNumberColumn;
    @FXML
    private TableColumn<Client, String> addressColumn;
    @FXML
    private Button updateClientsButton;
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
    private ChoiceBox <String> clientEditChoiceBox;
    @FXML
    private ChoiceBox <String> clientDeleteChoiceBox;
    @FXML
    private TableView<Client> clientsTableView;
    private ArrayList<Client> clients = new ArrayList<>();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();


    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager2.fxml"));
        Parent root = loader.load();
        Manager2 manager2controller = loader.getController();
        manager2controller.scanClientDataFile();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        /*Parent root = FXMLLoader.load(getClass().getResource("manager2.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/
    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("manager3.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager3.fxml"));
        Parent root = loader.load();

        Manager3 manager3controller = loader.getController();
        manager3controller.searchCountries();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    public void switchToEditClientScene(javafx.event.ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager11.fxml"));
        Parent root = loader.load();

        Manager11  manager11Controller = loader.getController();
        String selectedUserName = clientEditChoiceBox.getValue();
        UserData.setUsername(selectedUserName);
        manager11Controller.displayUserData();

        Stage newStage = new Stage();
        newStage.setTitle("Редагування даних Клієнта");
        newStage.setScene(new Scene(root));
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
        // Считывание данных из текстовых полей
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = numberTextField.getText();
        String address = addressTextField.getText();

        // Check for duplicates in existing clients
        if (checkForDuplicates(username, telNumber)) {
            // Display an alert if a duplicate is found
            alertLabel.setText("Такий акаунт або номер телефону вже існує");
            return; // Cancel the creation process
        }

        // Создание объекта Client и установка значений атрибутов
        Client client = new Client(username, password, name, telNumber, address, "Touristique");

        // Добавление клиента в коллекцию
        clients.add(client);

        // Добавление коллекции в файл с данными
        saveDataToFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", clients);

        // Clear the alertLabel if the creation process is successful
        alertLabel.setText("");
        /*String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = numberTextField.getText();
        String address = addressTextField.getText();

        // Создание объекта Client и установка значений атрибутов
        Client client = new Client(username, password, name, telNumber, address);

        // Добавление клиента в коллекцию
        clients.add(client);
        clientsList.add(client);

        // Добавление коллекции в файл с данными
        saveDataToFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", clients);*/
    }

    private boolean checkForDuplicates(String username, String telNumber) {
        for (Client client : clients) {
            if (client.getUsername().equals(username) || client.getTelNumber().equals(telNumber)) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicates found
    }

    private void saveDataToFile(String filename, ArrayList<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (Client client : clients) {
                // Форматирование строки перед записью в файл
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        client.getUsername(), client.getPassword(), client.getName(),
                        client.getTelNumber(), client.getAddress(), client.getAgencyName());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибок записи в файл
        }
    }

    public void scanClientDataFile() throws IOException{
        // Пропишите путь к файлу с данными
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";

        List<String> clientUserNames = readClientName(new File(filePath));
        clientEditChoiceBox.getItems().setAll(clientUserNames);
        clientDeleteChoiceBox.getItems().setAll(clientUserNames);


        clientsTableView.setItems(clientsList);
        List<Client> clientData = readClientData(new File(filePath));
        clientsList.clear();
        clientsList.addAll(clientData);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        telNumberColumn.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private List<String> readClientName(File file) {
        List<String> clientData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String username = parts[0];
                String password = parts[1];
                String name = parts[2];
                String telNumber = parts[3];
                String address = parts[4];
                String agencyName = parts[5];

                // Вы можете использовать только 'name' или любую другую часть данных, в зависимости от вашего выбора
                clientData.add(username);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return clientData;
    }
    private List<Client> readClientData(File file) {
        List<Client> clientData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String username = parts[0];
                String password = parts[1];
                String name = parts[2];
                String telNumber = parts[3];
                String address = parts[4];
                String agencyName = parts[5];

                // Вы можете использовать только 'name' или любую другую часть данных, в зависимости от вашего выбора
                Client client = new Client(username, password, name, telNumber, address, agencyName);
                clientData.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return clientData;
    }

    public void deleteClient(ActionEvent actionEvent) throws IOException {
        //Get the selected client name from the choice box
        String selectedUserName = clientDeleteChoiceBox.getValue();

        // Create a temporary list to store the clients excluding the one to be deleted
        List<Client> updatedClients = new ArrayList<>();

        // Read the existing clients from the file
        List<Client> existingClients = readClientData(new File("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt"));

        // Iterate through the existing clients and add to the updated list except for the one to be deleted
        for (Client client : existingClients) {
            if (!client.getUsername().equals(selectedUserName)) {
                updatedClients.add(client);
            }
        }

        // Update the file with the modified list of clients
        updateDataFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", updatedClients);

        // Refresh the TableView
        scanClientDataFile();

    }
    private void updateDataFile(String filename, List<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Client client : clients) {
                // Format the line before writing to the file
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        client.getUsername(), client.getPassword(), client.getName(),
                        client.getTelNumber(), client.getAddress(), client.getAgencyName());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions that occur during the file update
        }
    }

}
