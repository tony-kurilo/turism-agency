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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
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
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager3.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
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
    public void scanClientDataFile() throws IOException{
        // Пропишите путь к файлу с данными
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        List<String> voucherIDs = readvoucherID(new File(filePath));
        voucherIDSelectChoiceBox.getItems().setAll(voucherIDs);
        List<String> voucherStates = Arrays.asList("До сплати", "Відмова", "Скасовано");
        voucherStatusSelectChoiceBox.getItems().setAll(voucherStates);


        vouchersTableView.setItems(voucherList);
        List<Voucher> voucherData = readVoucherData(new File(filePath));
        voucherList.clear();
        voucherList.addAll(voucherData);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    private List<String> readvoucherID(File file) {
        List<String> voucherData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String username = parts[0];
                String country = parts[1];
                String city = parts[2];
                String hotel = parts[3];
                String begin_date = parts[4];
                String end_date = parts[5];
                String state = parts[6];
                String price = parts[7];
                String id = parts[8];

                // Вы можете использовать только 'name' или любую другую часть данных, в зависимости от вашего выбора
                voucherData.add(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return voucherData;
    }
    private List<Voucher> readVoucherData(File file) {
        List<Voucher> voucherData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String username = parts[0];
                String country = parts[1];
                String city = parts[2];
                String hotel = parts[3];
                LocalDate begin_date = LocalDate.parse(parts[4]);
                LocalDate end_date = LocalDate.parse(parts[5]);
                String state = parts[6];
                String price = parts[7];
                String id = parts[8];

                // Вы можете использовать только 'name' или любую другую часть данных, в зависимости от вашего выбора
                Voucher voucher = new Voucher(username, country, city, hotel, begin_date, end_date , state, price, id);
                voucherData.add(voucher);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return voucherData;
    }

    public void processVoucher() throws IOException {
        // Get selected values
        String selectedID = voucherIDSelectChoiceBox.getValue();
        String selectedStatus = voucherStatusSelectChoiceBox.getValue();
        String newPrice = priceTextField.getText(); // Assuming you have a TextField named priceTextField

        // Find the corresponding Voucher object
        Voucher selectedVoucher = null;
        for (Voucher voucher : voucherList) {
            if (voucher.getId().equals(selectedID)) {
                selectedVoucher = voucher;
                break;
            }
        }

        // Update the status and price if the Voucher is found
        if (selectedVoucher != null) {
            selectedVoucher.setState(selectedStatus);
            selectedVoucher.setPrice(newPrice);

            // Update the file with the modified voucher list
            updateVoucherFile();
            scanClientDataFile();
        } else {
            // Handle the case where the voucher with the selected ID is not found
            System.out.println("Voucher with ID " + selectedID + " not found.");
        }
    }
    private void updateVoucherFile() throws IOException {
        // Create a StringBuilder to store the updated voucher data
        StringBuilder updatedData = new StringBuilder();

        // Append the updated voucher data to the StringBuilder
        for (Voucher voucher : voucherList) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    voucher.getUsername(), voucher.getCountry(), voucher.getCity(),
                    voucher.getHotel(), voucher.getBeginDate(), voucher.getEndDate(),
                    voucher.getState(), voucher.getPrice(), voucher.getId());
            updatedData.append(line);
        }

        // Specify the file path
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        // Write the updated data back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(updatedData.toString());
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log an error) based on your application's requirements
        }
    }
}
