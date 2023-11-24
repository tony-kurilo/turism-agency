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
            // Handle the exception appropriately
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
            // Обработка ошибок чтения файла
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

        // Проверяем, что все поля заполнены
        if (country.isEmpty() || city.isEmpty() || hotel.isEmpty() || beginDate == null || endDate == null || url.isEmpty()) {
            alertCreateLabel.setText("Заповніть усі поля");
            return;
        }
        alertCreateLabel.setText("");
        if (checkForDuplicates(hotel,beginDate, endDate, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt")) {
            // Ваш код обработки ошибки (например, вы можете вывести сообщение об ошибке)
            alertCreate2Label.setText("Профіль з таким логіном\n чи телефоном вже існує");
            return;
        }
        alertCreate2Label.setText("");
        // Создаем объект Manager с фиксированным значением agencyName
        Voucher voucher = new Voucher(country, city, hotel, beginDate, endDate);

        Voucher voucher1 = new Voucher(hotel, url);

        // Добавляем объект Manager в файл с данными
        addVoucherToFile1(voucher, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt");
        addVoucherToFile2(voucher1,"C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels.txt");
        scanVoucherFile();
    }
    private void addVoucherToFile1(Voucher voucher, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Добавляем данные в файл
            writer.write(voucher.getCountry() + "," + voucher.getCity() + "," +
                    voucher.getHotel() + "," + voucher.getBeginDate() + "," + voucher.getEndDate());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибок при записи в файл
        }
    }
    private void addVoucherToFile2(Voucher voucher, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Добавляем данные в файл
            writer.write(voucher.getHotel() + "," + voucher.getUrl());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибок при записи в файл
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

                    // Проверяем, существует ли логин или номер телефона
                    if (hotel.equals(existingHotel) || beginDate.equals(existingBeginDate) || endDate.equals(existingEndDate)) {
                        return true; // Дубликат найден
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибок чтения файла
        }

        return false; // Дубликаты не найдены
    }
    public void editVoucher(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrmanager11.fxml"));
        Parent root = loader.load();

        Hrmanager11  hrmanager11Controller = loader.getController();
        Object selectedObject = editChoiceBox.getValue();

        // Check if the selectedObject is not null and is of type String
        if (selectedObject != null && selectedObject instanceof String) {
            String selectedUserName = (String) selectedObject;
            UserData.setUsername(selectedUserName);
            hrmanager11Controller.displayUserData();
            Stage newStage = new Stage();
            newStage.setTitle("Редагування даних Менеджера");
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            // Handle the case where the selected value is not a String
            System.out.println("Invalid selection");
        }
    }
    /*String selectedUserName = editChoiceBox.getValue();
            UserData.setUsername(selectedUserName);
            hrmanager11Controller.displayUserData();
            Stage newStage = new Stage();
            newStage.setTitle("Редагування даних Менеджера");
            newStage.setScene(new Scene(root));
            newStage.show();*/
    private List<Manager> readManagerData(File file) {
        List<Manager> managerData = new ArrayList<>();

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
                String agencyName = parts[4];

                // Вы можете использовать только 'name' или любую другую часть данных, в зависимости от вашего выбора
                Manager manager = new Manager(username, password, name, telNumber, agencyName);
                managerData.add(manager);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return managerData;
    }

    /*public void deleteManagerProfile(ActionEvent actionEvent) throws IOException{
        //Get the selected client name from the choice box
        String selectedUserName = managerDeleteChoiceBox.getValue();

        // Create a temporary list to store the clients excluding the one to be deleted
        java.util.List<Manager> updatedManagers = new ArrayList<>();

        // Read the existing clients from the file
        List<Manager> existingManagers = readManagerData(new File("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt"));

        // Iterate through the existing clients and add to the updated list except for the one to be deleted
        for (Manager manager : existingManagers) {
            if (!manager.getLogin().equals(selectedUserName)) {
                updatedManagers.add(manager);
            }
        }

        // Update the file with the modified list of clients
        updateDataFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt", updatedManagers);

        // Refresh the TableView
        scanManagerFile();
    }
    private void updateDataFile(String filename, List<Manager> managers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Manager manager : managers) {
                // Format the line before writing to the file
                String line = String.format("%s,%s,%s,%s,%s",
                        manager.getLogin(), manager.getPassword(), manager.getName(),
                        manager.getTelNumber(),  manager.getAgencyName());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions that occur during the file update
        }
    }*/

    public void switchToCreationManager(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrmanager1.fxml"));
        Parent root = loader.load();

        Hrmanager1 hrmanager1Controller = loader.getController();
        //client3Controller.displayUserData();

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
