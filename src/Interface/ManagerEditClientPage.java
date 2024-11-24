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
import javafx.util.StringConverter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerEditClientPage {
    private Stage stage;
    private Scene scene;
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

    // Метод для подключения к базе данных
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerProcessVouchersPage.fxml"));
        Parent root = loader.load();
        ManagerProcessVouchersPage managerProcessVouchersPageController = loader.getController();
        managerProcessVouchersPageController.scanClientDataFile();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerDataAnalysisPage.fxml"));
        Parent root = loader.load();

        ManagerDataAnalysisPage managerDataAnalysisPageController = loader.getController();
        managerDataAnalysisPageController.searchCountries();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToData(javafx.event.ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerDataPage.fxml"));
        Parent root = loader.load();

        ManagerDataPage managerDataPageController = loader.getController();
        managerDataPageController.displayUserData();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToEditClientScene(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerEditClientModalPage.fxml"));
        Parent root = loader.load();

        ManagerEditClientModalPage managerEditClientModalPageController = loader.getController();

        // Получение выбранного имени пользователя
        String selectedUserName = clientEditChoiceBox.getValue();

        // Устанавливаем username
        UserData.setUsername(selectedUserName);

        // Получение id пользователя из базы данных
        int userId = fetchUserIdByUsername(selectedUserName);

        if (userId != -1) {
            // Устанавливаем id пользователя
            UserData.setId(userId);

            // Отображение данных пользователя
            managerEditClientModalPageController.displayUserData();

            // Открываем новое окно
            Stage newStage = new Stage();
            newStage.setTitle("Редагування даних Клієнта");
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            System.out.println("Ошибка: пользователь не найден в базе данных.");
        }
    }

    private int fetchUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                System.out.println("Пользователь с username " + username + " не найден.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        UserData.setId(0);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }

    // Создание нового клиента
    public void createClient(ActionEvent actionEvent) {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Ошибка подключения к базе данных");
            return;
        }

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String telNumber = numberTextField.getText();
        String address = addressTextField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || telNumber.isEmpty() || address.isEmpty()) {
            alertLabel.setText("Заполните все поля для создания клиента");
            return;
        }

        try {
            // Определение нового уникального id только по таблице users
            String findMaxIdQuery = "SELECT COALESCE(MAX(id), 0) + 1 AS new_id FROM users";
            int newId = 1;

            try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(findMaxIdQuery)) {
                if (rs.next()) {
                    newId = rs.getInt("new_id");
                }
            }

            // Логика добавления нового пользователя и клиента
            String insertUserQuery = "INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, 'client')";
            try (var pstmt = connection.prepareStatement(insertUserQuery)) {
                pstmt.setInt(1, newId);
                pstmt.setString(2, username);
                pstmt.setString(3, password);
                pstmt.executeUpdate();
            }

            String insertClientQuery = "INSERT INTO clients (id, name, tel_number, address, agency_name) VALUES (?, ?, ?, ?, 'Touristique')";
            try (var pstmt = connection.prepareStatement(insertClientQuery)) {
                pstmt.setInt(1, newId);
                pstmt.setString(2, name);
                pstmt.setString(3, telNumber);
                pstmt.setString(4, address);
                pstmt.executeUpdate();
            }

            alertLabel.setText("Клиент успешно создан!");
            loadClients(); // Обновляем таблицу клиентов

        } catch (SQLException e) {
            e.printStackTrace();
            alertLabel.setText("Ошибка при создании клиента");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Загрузка данных клиентов в таблицу
    public void loadClients() {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Ошибка подключения к базе данных");
            return;
        }

        clientsList.clear();

        String query = "SELECT u.username, c.name, c.tel_number, c.address FROM users u INNER JOIN clients c ON u.id = c.id";
        try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String name = rs.getString("name");
                String telNumber = rs.getString("tel_number");
                String address = rs.getString("address");

                clientsList.add(new Client(username, "", name, telNumber, address, "Touristique"));
            }

            clientsTableView.setItems(clientsList);
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            telNumberColumn.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Удаление клиента
    public void deleteClient(ActionEvent actionEvent) {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Ошибка подключения к базе данных");
            return;
        }

        String selectedUserName = clientDeleteChoiceBox.getValue();

        if (selectedUserName == null) {
            alertLabel.setText("Выберите клиента для удаления");
            return;
        }

        try {
            // Удаляем данные клиента и связанные данные из таблицы users
            String deleteClientQuery = """
            DELETE FROM clients 
            WHERE id = (SELECT id FROM users WHERE username = ?)
        """;
            try (var pstmt = connection.prepareStatement(deleteClientQuery)) {
                pstmt.setString(1, selectedUserName);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    // Удаляем пользователя только если клиент был удалён
                    String deleteUserQuery = "DELETE FROM users WHERE username = ?";
                    try (var pstmtUser = connection.prepareStatement(deleteUserQuery)) {
                        pstmtUser.setString(1, selectedUserName);
                        pstmtUser.executeUpdate();
                    }
                    alertLabel.setText("Клиент успешно удалён!");
                } else {
                    alertLabel.setText("Ошибка: клиент не найден.");
                }
            }

            loadClients();

        } catch (SQLException e) {
            e.printStackTrace();
            alertLabel.setText("Ошибка при удалении клиента");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Подсчет клиентов
    public void countClients() {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Ошибка подключения к базе данных");
            return;
        }

        String countQuery = "SELECT COUNT(*) AS client_count FROM clients";
        try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(countQuery)) {
            if (rs.next()) {
                int count = rs.getInt("client_count");
                countLabel.setText("Клиентов: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void scanClientDataFile() {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Ошибка подключения к базе данных");
            return;
        }

        try {
            // Загружаем данные только клиентов
            List<String> clientUserNames = readClientName(connection);
            clientEditChoiceBox.getItems().setAll(clientUserNames);
            clientDeleteChoiceBox.getItems().setAll(clientUserNames);

            List<Client> clientData = readClientData(connection);
            clientsList.clear();
            clientsList.addAll(clientData);

            clientsTableView.setItems(clientsList);
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            telNumberColumn.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

            countClients();
        } catch (SQLException e) {
            e.printStackTrace();
            alertLabel.setText("Ошибка при загрузке данных");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> readClientName(Connection connection) throws SQLException {
        List<String> clientNames = new ArrayList<>();
        String query = "SELECT u.username FROM users u INNER JOIN clients c ON u.id = c.id";

        try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                clientNames.add(rs.getString("username"));
            }
        }

        return clientNames;
    }


    private List<Client> readClientData(Connection connection) throws SQLException {
        List<Client> clientData = new ArrayList<>();
        String query = """
        SELECT u.username, u.password, c.name, c.tel_number, c.address, c.agency_name
        FROM users u
        INNER JOIN clients c ON u.id = c.id
        """;

        try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String telNumber = rs.getString("tel_number");
                String address = rs.getString("address");
                String agencyName = rs.getString("agency_name");

                Client client = new Client(username, password, name, telNumber, address, agencyName);
                clientData.add(client);
            }
        }

        return clientData;
    }

}
