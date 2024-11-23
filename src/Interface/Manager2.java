package Interface;

import Classes.Client;
import Classes.Voucher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manager2 {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField priceTextField;
    @FXML
    Label alertLabel;
    @FXML
    private ChoiceBox<String> voucherIDSelectChoiceBox;
    @FXML
    private ChoiceBox<String> voucherStatusSelectChoiceBox;
    @FXML
    private TableView<Voucher> vouchersTableView;
    @FXML
    private TableColumn<Voucher, String> usernameColumn;
    @FXML
    private TableColumn<Voucher, String> countryColumn;
    @FXML
    private TableColumn<Voucher, String> cityColumn;
    @FXML
    private TableColumn<Voucher, LocalDate> beginDateColumn;
    @FXML
    private TableColumn<Voucher, LocalDate> endDateColumn;
    @FXML
    private TableColumn<Voucher, String> hotelColumn;
    @FXML
    private TableColumn<Voucher, String> idColumn;
    @FXML
    private TableColumn<Voucher, String> stateColumn;
    @FXML
    private TableColumn<Voucher, String> priceColumn;
    private ObservableList<Voucher> voucherList = FXCollections.observableArrayList();

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

    TextFormatter<Double> priceFormatter = new TextFormatter<>(new StringConverter<>() {
        @Override
        public String toString(Double object) {
            if (object == null) {
                return "";
            }
            return object.toString();
        }

        @Override
        public Double fromString(String string) {
            try {
                // Пытаемся преобразовать введенную строку в число с плавающей точкой
                return Double.parseDouble(string);
            } catch (NumberFormatException e) {
                // Если не удалось преобразовать, возвращаем null
                return null;
            }
        }
    });
    public void switchToClients(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager1.fxml"));
        Parent root = loader.load();

        Manager1 manager1Controller = loader.getController();
        manager1Controller.scanClientDataFile();

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
    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    public void scanClientDataFile() {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Помилка підключення до бази даних");
            return;
        }

        try {
            // Получение данных из базы
            String query = "SELECT id,username, country, city, hotel, begin_date, end_date, state, price FROM voucher_requests";
            var stmt = connection.createStatement();
            var rs = stmt.executeQuery(query);

            // Обновляем список ChoiceBox и TableView
            voucherIDSelectChoiceBox.getItems().clear();
            voucherList.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String country = rs.getString("country");
                String city = rs.getString("city");
                String hotel = rs.getString("hotel");
                LocalDate beginDate = rs.getDate("begin_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                String state = rs.getString("state");
                Double price = rs.getDouble("price");

                voucherIDSelectChoiceBox.getItems().add(String.valueOf(id));
                Voucher voucher = new Voucher(id, username, country, city, hotel, beginDate, endDate, state, price);
                voucherList.add(voucher);
            }

            vouchersTableView.setItems(voucherList);
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
            beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotel"));
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            List<String> voucherStates = Arrays.asList("До сплати", "Відмова", "Скасовано");
            voucherStatusSelectChoiceBox.getItems().setAll(voucherStates);

        } catch (SQLException e) {
            e.printStackTrace();
            alertLabel.setText("Помилка отримання даних");
        }
    }

    public void processVoucher() {
        Connection connection = connectToDatabase();
        if (connection == null) {
            alertLabel.setText("Помилка підключення до бази даних");
            return;
        }

        String selectedID = voucherIDSelectChoiceBox.getValue();
        String selectedStatus = voucherStatusSelectChoiceBox.getValue();
        String newPriceText = priceTextField.getText();

        Double newPrice = parsePrice(newPriceText);
        if (newPrice == null) {
            alertLabel.setText("Невірний формат ціни");
            return;
        }

        try {
            // Обновляем запись в базе данных
            String updateQuery = "UPDATE voucher_requests SET state = ?, price = ? WHERE id = ?";
            var pstmt = connection.prepareStatement(updateQuery);
            pstmt.setString(1, selectedStatus);
            pstmt.setDouble(2, newPrice);
            pstmt.setInt(3, Integer.parseInt(selectedID));
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                alertLabel.setText("Дані успішно оновлені");
                scanClientDataFile(); // Обновляем отображение данных
            } else {
                alertLabel.setText("Не вдалося знайти вказаний ваучер");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alertLabel.setText("Помилка оновлення даних");
        }
    }
    private Double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
