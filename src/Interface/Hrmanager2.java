package Interface;

import Classes.Manager;
import Classes.Voucher;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Hrmanager2 {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField hotelTextField;
    @FXML
    private DatePicker beginDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField urlTextField;

    @FXML
    TableView<Voucher> vouchersTableView;
    @FXML
    TableColumn countryColumn;
    @FXML
    TableColumn cityColumn;
    @FXML
    TableColumn beginDateColumn;
    @FXML
    TableColumn endDateColumn;
    @FXML
    TableColumn hotelColumn;
    @FXML
    ChoiceBox editChoiceBox;
    @FXML
    ChoiceBox deleteChoiceBox;
    @FXML
    Label alertCreateLabel;
    @FXML
    Label alertCreate2Label;


    public void scanVoucherFile() {
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();
        ObservableList<Voucher> choiceBoxList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String country = data[0].trim();
                    String city = data[1].trim();
                    String hotel = data[2].trim();
                    LocalDate beginDate = LocalDate.parse(data[3].trim(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate endDate = LocalDate.parse(data[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    Voucher voucher = new Voucher(country, city, hotel, beginDate, endDate);
                    vouchers.add(voucher);
                    choiceBoxList.add(voucher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        vouchersTableView.setItems(vouchers);
        populateChoiceBoxes();
    }
    public void populateChoiceBoxes() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt"))) {
            ObservableList<String> choiceBoxList = FXCollections.observableArrayList();

            String line;
            while ((line = reader.readLine()) != null) {
                choiceBoxList.add(line);
            }

            editChoiceBox.setItems(choiceBoxList);
            deleteChoiceBox.setItems(choiceBoxList);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public void createVoucher(ActionEvent actionEvent){
        // Получаем введенные данные из текстовых полей
        String country = countryTextField.getText().trim();
        String city = cityTextField.getText().trim();
        String hotel = hotelTextField.getText().trim();
        LocalDate beginDate = beginDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String url = urlTextField.getText().trim();


        if (country.isEmpty() || city.isEmpty() || hotel.isEmpty() || beginDate == null || endDate == null || url.isEmpty()) {
            alertCreateLabel.setText("Заповніть усі поля");
            return;
        }
        alertCreateLabel.setText("");
        if (checkForDuplicates(hotel,beginDate, endDate, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt")) {
            alertCreate2Label.setText("Профіль з таким логіном\n чи телефоном вже існує");
            return;
        }
        alertCreate2Label.setText("");
        Voucher voucher = new Voucher(country, city, hotel, beginDate, endDate);

        Voucher voucher1 = new Voucher(hotel, url);


        addVoucherToFile1(voucher, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt");
        addVoucherToFile2(voucher1,"C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels.txt");
        scanVoucherFile();
    }
    private void addVoucherToFile1(Voucher voucher, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            writer.write(voucher.getCountry() + "," + voucher.getCity() + "," +
                    voucher.getHotel() + "," + voucher.getBeginDate() + "," + voucher.getEndDate());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    private void addVoucherToFile2(Voucher voucher, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            writer.write(voucher.getHotel() + "," + voucher.getUrl());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    private boolean checkForDuplicates(String hotel, LocalDate beginDate, LocalDate endDate, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String existingHotel = parts[2].trim();
                    String existingBeginDate = parts[3].trim();
                    String existingEndDate = parts[4].trim();


                    if (hotel.equals(existingHotel) || beginDate.equals(existingBeginDate) || endDate.equals(existingEndDate)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return false;
    }
    public void editVoucher(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrmanager21.fxml"));
        Parent root = loader.load();

        Hrmanager21 hrmanager21Controller = loader.getController();
        Object selectedObject = editChoiceBox.getValue();

        if (selectedObject != null && selectedObject instanceof String) {
            String selectedValue = (String) selectedObject;

            String[] parts = selectedValue.split(",");
            if (parts.length == 5) {
                String country = parts[0];
                String city = parts[1];
                String hotel = parts[2];
                LocalDate beginDate = LocalDate.parse(parts[3]);
                LocalDate endDate = LocalDate.parse(parts[4]);

                UserData.setCountry(country);
                UserData.setCity(city);
                UserData.setHotel(hotel);
                UserData.setBeginDate(beginDate);
                UserData.setEndDate(endDate);

                hrmanager21Controller.displayUserData();

                Stage newStage = new Stage();
                newStage.setTitle("Редагування даних Путівки");
                newStage.setScene(new Scene(root));
                newStage.show();
            } else {
                System.out.println("Invalid selected value format");
            }
        } else {
            System.out.println("Invalid selection");
        }
    }

    private List<Manager> readManagerData(File file) {
        List<Manager> managerData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String username = parts[0];
                String password = parts[1];
                String name = parts[2];
                String telNumber = parts[3];
                String agencyName = parts[4];

                Manager manager = new Manager(username, password, name, telNumber, agencyName);
                managerData.add(manager);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return managerData;
    }
    public void deleteVoucher(ActionEvent actionEvent) throws IOException{

        Object selectedObject = deleteChoiceBox.getValue();
        String filePath1 = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt";
        String filePath2 = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels.txt";

        String selectedObjectString = selectedObject.toString();
        String[] selectedObjectParts = selectedObjectString.split(",");

        String selectedCountry = selectedObjectParts[0].trim();
        String selectedCity = selectedObjectParts[1].trim();
        String selectedHotel = selectedObjectParts[2].trim();
        String selectedBeginDate = selectedObjectParts[3].trim();
        String selectedEndDate = selectedObjectParts[4].trim();

        // Проводим поиск в первом файле
        boolean matchFile1 = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 &&
                        parts[0].trim().equals(selectedCountry) &&
                        parts[1].trim().equals(selectedCity) &&
                        parts[2].trim().equals(selectedHotel) &&
                        parts[3].trim().equals(selectedBeginDate) &&
                        parts[4].trim().equals(selectedEndDate)) {
                    matchFile1 = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Проводим поиск во втором файле
        boolean matchFile2 = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath2))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].trim().equals(selectedHotel)) {
                    matchFile2 = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Если найдены совпадения в обоих файлах, производим удаление данных
        if (matchFile1 && matchFile2) {
            try {
                // Удаление данных в первом файле
                String tempFilePath1 = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers_temp.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath1));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath1))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (!(parts.length == 5 &&
                                parts[0].trim().equals(selectedCountry) &&
                                parts[1].trim().equals(selectedCity) &&
                                parts[2].trim().equals(selectedHotel) &&
                                parts[3].trim().equals(selectedBeginDate) &&
                                parts[4].trim().equals(selectedEndDate))) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                }

                // Переименование временного файла в основной
                java.nio.file.Files.move(
                        java.nio.file.Path.of(tempFilePath1),
                        java.nio.file.Path.of(filePath1),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                // Удаление данных во втором файле
                String tempFilePath2 = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels_temp.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath2));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath2))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (!(parts.length > 0 && parts[0].trim().equals(selectedHotel))) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                }

                // Переименование временного файла в основной
                java.nio.file.Files.move(
                        java.nio.file.Path.of(tempFilePath2),
                        java.nio.file.Path.of(filePath2),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchToCreationManager(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrmanager1.fxml"));
        Parent root = loader.load();

        Hrmanager1 hrmanager1Controller = loader.getController();

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
}
