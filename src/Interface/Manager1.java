package Interface;

import Classes.Client;
import Classes.Manager;
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
import javafx.util.StringConverter;

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
    private Label countLabel;
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

    TextFormatter<Integer> telNumberFormatter = new TextFormatter<>(new StringConverter<>() {
        @Override
        public String toString(Integer object) {
            if (object == null) {
                return "";
            }
            return object.toString();
        }

        @Override
        public Integer fromString(String string) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    });

    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager2.fxml"));
        Parent root = loader.load();
        Manager2 manager2controller = loader.getController();
        manager2controller.scanClientDataFile();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {

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
        numberTextField.setTextFormatter(telNumberFormatter);
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumberText = numberTextField.getText();
        String address = addressTextField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || telNumberText.isEmpty() || address.isEmpty()) {
            alertLabel.setText("Заповніть всі поля для створення профілю");
            return;
        }
        alertLabel.setText("");

        Integer telNumber = parseTelNumber(telNumberText);

        if (telNumber == null) {
            alertLabel.setText("Номер телефону повинен бути числом");
            return;
        }

        int duplicateScenario = checkForDuplicates(username, telNumber.toString());

        switch (duplicateScenario) {
            case 1:
                alertLabel.setText("Акаунт з таким логіном і номером телефону вже існує");
                return;
            case 2:
                alertLabel.setText("Акаунт з таким логіном вже існує");
                return;
            case 3:
                alertLabel.setText("Акаунт з таким номером телефону вже існує");
                return;
            default:
                Client client = new Client(username, password, name, telNumber.toString(), address, "Touristique");

                clients.add(client);

                saveDataToFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", clients);

                alertLabel.setText("");
                break;
        }

    }
    private Integer parseTelNumber(String telNumberText) {
        try {
            return Integer.parseInt(telNumberText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private  int checkForDuplicates(String username, String telNumber) {
        boolean usernameExists = false;
        boolean telNumberExists = false;

        List<Client> clients = loadDataFromClientFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt");
        List<Manager> managers = loadDataFromManagerFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt");

        for (Client client : clients) {
            if (client.getUsername().equals(username)) {
                usernameExists = true;
            }

            if (client.getTelNumber().equals(telNumber)) {
                telNumberExists = true;
            }

            // Если оба условия выполняются, значит, оба дубликата существуют
            if (usernameExists && telNumberExists) {
                return 1;
            }

            // Если только логин существует
            if (usernameExists) {
                return 2;
            }

            // Если только номер телефона существует
            if (telNumberExists) {
                return 3;
            }
        }

        // Проверка на дубликаты в менеджерах
        for (Manager manager : managers) {
            if (manager.getLogin().equals(username)) {
                usernameExists = true;
            }

            if (manager.getTelNumber().equals(telNumber)) {
                telNumberExists = true;
            }

            // Если оба условия выполняются, значит, оба дубликата существуют
            if (usernameExists && telNumberExists) {
                return 1;
            }

            // Если только логин существует
            if (usernameExists) {
                return 2;
            }

            // Если только номер телефона существует
            if (telNumberExists) {
                return 3;
            }
        }

        // Нет дубликатов
        return 0;
    }
    private List<Client> loadDataFromClientFile(String filePath) {
        List<Client> clients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String username = data[0].trim();
                    String password = data[1].trim();
                    String name = data[2].trim();
                    String telNumber = data[3].trim();
                    String address = data[4].trim();
                    String agencyName = data[5].trim();

                    clients.add(new Client(username, password, name, telNumber, address, agencyName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок загрузки данных из файла
        }

        return clients;
    }
    private List<Manager> loadDataFromManagerFile(String filePath) {
        List<Manager> managers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String username = data[0].trim();
                    String password = data[1].trim();
                    String name = data[2].trim();
                    String telNumber = data[3].trim();
                    String agencyName = data[4].trim();

                    managers.add(new Manager(username, password, name, telNumber,agencyName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок загрузки данных из файла
        }

        return managers;
    }
    private void saveDataToFile(String filename, ArrayList<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (Client client : clients) {

                String line = String.format("%s,%s,%s,%s,%s,%s",
                        client.getUsername(), client.getPassword(), client.getName(),
                        client.getTelNumber(), client.getAddress(), client.getAgencyName());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void scanClientDataFile() throws IOException{

        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";

        List<String> clientUserNames = readClientName(new File(filePath));
        clientEditChoiceBox.getItems().setAll(clientUserNames);
        clientDeleteChoiceBox.getItems().setAll(clientUserNames);

        countClients();
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
                String[] parts = line.split(",");

                String username = parts[0];
                String password = parts[1];
                String name = parts[2];
                String telNumber = parts[3];
                String address = parts[4];
                String agencyName = parts[5];

                clientData.add(username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientData;
    }
    private List<Client> readClientData(File file) {
        List<Client> clientData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String username = parts[0];
                String password = parts[1];
                String name = parts[2];
                String telNumber = parts[3];
                String address = parts[4];
                String agencyName = parts[5];

                Client client = new Client(username, password, name, telNumber, address, agencyName);
                clientData.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clientData;
    }

    public void deleteClient(ActionEvent actionEvent) throws IOException {
        String selectedUserName = clientDeleteChoiceBox.getValue();

        List<Client> updatedClients = new ArrayList<>();

        List<Client> existingClients = readClientData(new File("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt"));

        for (Client client : existingClients) {
            if (!client.getUsername().equals(selectedUserName)) {
                updatedClients.add(client);
            }
        }

        updateDataFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt", updatedClients);

        scanClientDataFile();

    }
    private void updateDataFile(String filename, List<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Client client : clients) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        client.getUsername(), client.getPassword(), client.getName(),
                        client.getTelNumber(), client.getAddress(), client.getAgencyName());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void countClients() {
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";
        List<Client> clients = loadDataFromClientFile(filePath);
        int clientCount = clients.size();
        countLabel.setText("Кількість - " + clientCount);
    }
}
