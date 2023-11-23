package Interface;

import Classes.Voucher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    protected List<Voucher> voucherList;
    public void switchToClients(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager1.fxml"));
        Parent root = loader.load();

        Manager1 manager1Controller = loader.getController();
        manager1Controller.scanClientDataFile();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        /*Parent root = FXMLLoader.load(getClass().getResource("manager1.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/
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
        /*Parent root = FXMLLoader.load(getClass().getResource("manager2.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/
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

    public void searchCountries() {
        // Assuming your data file has the 'country' attribute at a specific index (adjust the index accordingly)
        int countryIndex = 1;

        // Provide the path to your data file
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        Set<String> uniqueCountries = readUniqueCountries(filePath, countryIndex);

        // Update the ChoiceBox items with the unique countries
        selectCountryChoiceBox.getItems().setAll(uniqueCountries);
    }
    private Set<String> readUniqueCountries(String filePath, int countryIndex) {
        Set<String> uniqueCountries = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > countryIndex) {
                    String country = parts[countryIndex].trim();
                    uniqueCountries.add(country);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if something goes wrong while reading the file
        }

        return uniqueCountries;
    }

    public void searchClientWhoChosedCountry(){
        String selectedCountry = selectCountryChoiceBox.getValue();
        List<String> matchingUsernames = searchUsernamesByCountry(selectedCountry, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt");
        List<String> matchingNames = searchNamesByUsername(matchingUsernames, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt");
        displayNamesInTableView(matchingNames);
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
        // Очистите существующие данные в таблице
        clientWhoChoseCountryTableView.getItems().clear();

        // Создайте ObservableList для хранения данных имен
        ObservableList<String> namesList = FXCollections.observableArrayList(names);

        // Очистите существующие колонки в таблице
        clientWhoChoseCountryTableView.getColumns().clear();

        // Создайте новую колонку
        //TableColumn<String, String> nameColumn = new TableColumn<>("ПІБ");

        // Установите данные в колонку
        nameColumn.setCellValueFactory(cellData -> {
            String name = cellData.getValue();
            return new SimpleStringProperty(name);
        });

        // Добавьте колонку в таблицу
        clientWhoChoseCountryTableView.getColumns().add(nameColumn);

        // Установите данные в TableView
        clientWhoChoseCountryTableView.setItems(namesList);
    }

}
