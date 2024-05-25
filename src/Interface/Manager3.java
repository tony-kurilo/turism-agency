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
        int countryIndex = 1;

        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        Set<String> uniqueCountries = readUniqueCountries(filePath, countryIndex);

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
        }

        return uniqueCountries;
    }

    public void searchClientWhoChosedCountry() {
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
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";
        double totalCost = 0;
        int voucherCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 9) {
                    String priceString = parts[7].trim();

                    if (!priceString.isEmpty()) {
                        try {
                            double price = Double.parseDouble(priceString);
                            totalCost += price;
                            voucherCount++;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (voucherCount > 0) {
            averageCost = totalCost / voucherCount;
        } else {
            averageCost = 0;
        }

        averageCostLabel.setText(""+averageCost);
    }

    public void averageDuration(ActionEvent actionEvent){
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt"; // Укажите правильный путь к файлу

        int totalDuration = 0;
        int tripCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 9) {
                    String beginDateString = parts[4].trim();
                    String endDateString = parts[5].trim();

                    try {
                        LocalDate beginDate = LocalDate.parse(beginDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        int duration = (int) beginDate.until(endDate).getDays();

                        totalDuration += duration;
                        tripCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tripCount > 0) {
            int averageDuration = totalDuration / tripCount;

            averageDurationLabel.setText(averageDuration + " діб");
        } else {
            averageDurationLabel.setText("Немає даних");
        }
    }
    public void countryWithHighestDemand(ActionEvent actionEvent){
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        Map<String, Integer> countryCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String country = parts[1].trim();

                countryCountMap.put(country, countryCountMap.getOrDefault(country, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mostPopularCountry = findMostPopularCountry(countryCountMap);

        if (mostPopularCountry != null) {
            countryWithHighestDemandLabel.setText(mostPopularCountry);
        } else {
            countryWithHighestDemandLabel.setText("Нема даних");
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
