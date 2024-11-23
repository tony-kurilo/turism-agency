package Interface;

import Classes.Voucher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Manager3 {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ChoiceBox<String> selectCountryChoiceBox;
    @FXML
    private TableColumn<String, String> nameColumn;
    @FXML
    private TableView<String> clientWhoChoseCountryTableView;
    @FXML
    private Label averageCostLabel;
    @FXML
    private Label averageDurationLabel;
    @FXML
    private Label countryWithHighestDemandLabel;
    protected List<Voucher> voucherList;
    private double averageCost;

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

    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager2.fxml"));
        Parent root = loader.load();

        Manager2 manager2Controller = loader.getController();
        manager2Controller.scanClientDataFile();

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
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void searchCountries() {
        String query = "SELECT DISTINCT country FROM vouchers";

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            Set<String> uniqueCountries = new HashSet<>();
            while (resultSet.next()) {
                uniqueCountries.add(resultSet.getString("country"));
            }

            selectCountryChoiceBox.getItems().setAll(uniqueCountries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchClientWhoChosedCountry() {
        String selectedCountry = selectCountryChoiceBox.getValue();
        if (selectedCountry == null) return;

        String query = """
        SELECT u.username
        FROM users u
        JOIN voucher_requests v ON u.username = v.username
        WHERE v.country = ?
    """;

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, selectedCountry);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<String> matchingNames = new ArrayList<>();
                while (resultSet.next()) {
                    matchingNames.add(resultSet.getString("username"));
                }

                displayNamesInTableView(matchingNames);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> searchUsernamesByCountry(String country, String filePath) {
        List<String> matchingUsernames = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String username = parts[0];
                String countryFromFile = parts[1];

                if (country.equals(countryFromFile)) {
                    matchingUsernames.add(username);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchingUsernames;
    }

    private List<String> searchNamesByUsername(List<String> usernames, String filePath) {
        List<String> matchingNames = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String usernameFromFile = parts[0];
                String name = parts[2];

                if (usernames.contains(usernameFromFile)) {
                    matchingNames.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchingNames;
    }

    private void displayNamesInTableView(List<String> names) {
        clientWhoChoseCountryTableView.getItems().clear();

        ObservableList<String> namesList = FXCollections.observableArrayList(names);

        clientWhoChoseCountryTableView.getColumns().clear();


        nameColumn.setCellValueFactory(cellData -> {
            String name = cellData.getValue();
            return new SimpleStringProperty(name);
        });

        clientWhoChoseCountryTableView.getColumns().add(nameColumn);

        clientWhoChoseCountryTableView.setItems(namesList);
    }


    public void averageVoucherCost(ActionEvent actionEvent) {
        String query = "SELECT AVG(price) AS average_price FROM voucher_requests WHERE price IS NOT NULL";

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                averageCost = resultSet.getDouble("average_price"); // Исправлено: "average_price"
                if (resultSet.wasNull()) {
                    averageCostLabel.setText("Немає даних");
                } else {
                    averageCostLabel.setText(String.format("%.2f", averageCost));
                }
            } else {
                averageCostLabel.setText("Немає даних");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void averageDuration(ActionEvent actionEvent) {
        String query = """
        SELECT begin_date, end_date, (end_date - begin_date) AS duration
        FROM voucher_requests
        WHERE end_date IS NOT NULL AND begin_date IS NOT NULL;
    """;

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                double averageDuration = resultSet.getDouble("duration");
                if (resultSet.wasNull()) {
                    averageDurationLabel.setText("Немає даних");
                } else {
                    averageDurationLabel.setText(String.format("%.1f діб", averageDuration));
                }
            } else {
                averageDurationLabel.setText("Немає даних");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void countryWithHighestDemand(ActionEvent actionEvent) {
        String query = """
        SELECT country, COUNT(*) AS count
        FROM vouchers
        GROUP BY country
        ORDER BY count DESC
        LIMIT 1
    """;

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                String mostPopularCountry = resultSet.getString("country");
                countryWithHighestDemandLabel.setText(mostPopularCountry);
            } else {
                countryWithHighestDemandLabel.setText("Немає даних");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String findMostPopularCountry(Map<String, Integer> countryCountMap) {
        int maxCount = 0;
        String mostPopularCountry = null;

        for (Map.Entry<String, Integer> entry : countryCountMap.entrySet()) {
            String country = entry.getKey();
            int count = entry.getValue();

            if (count > maxCount) {
                maxCount = count;
                mostPopularCountry = country;
            }
        }

        return mostPopularCountry;
    }
}
