package Interface;

import Classes.Client;
import Classes.Voucher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Client1 {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    protected UserData userModel;
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
    TableView <Voucher> voucherTableView;
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
    TableColumn stateColumn;
    @FXML
    TableColumn priceColumn;
    @FXML
    TableColumn idColumn;

    public void switchToCreationVoucher(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Client3.fxml"));
        Parent root = loader.load();

        //Client3 client3Controller = loader.getController();
        //client3Controller.displayUserData();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToData(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client2.fxml"));
        Parent root = loader.load();

        Client2 client2Controller = loader.getController();
        client2Controller.displayUserData();

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

    public void searchMyVouchers(){
        String username = UserData.getUsername();
        // Создаем список для хранения данных
        ObservableList<Voucher> voucherList = FXCollections.observableArrayList();

        // Пропишите путь к файлу с данными
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String voucherUsername = parts[0].trim();

                // Проверяем совпадение с именем пользователя
                if (username.equals(voucherUsername)) {
                    Voucher voucher = parseVoucherFromLine(parts);
                    voucherList.add(voucher);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        // Очищаем TableView перед добавлением новых данных
        voucherTableView.getItems().clear();

        // Добавляем данные в TableView
        voucherTableView.setItems(voucherList);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotel"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    }
    private Voucher parseVoucherFromLine(String[] parts) {
        // Реализуйте этот метод в соответствии с вашей структурой данных (Voucher)
        // В зависимости от порядка и структуры данных в файле
        // Пример: Voucher voucher = new Voucher(parts[0], parts[1], ..., parts[n]);
        // return voucher;

        // Пример (если данные в файле имеют определенный порядок):
        String country = parts[1].trim();
        String city = parts[2].trim();
        String hotel = parts[3].trim();
        LocalDate beginDate = LocalDate.parse(parts[4].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(parts[5].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String state = parts[6].trim();
        String price = parts[7].trim();
        String id = parts[8].trim();

        return new Voucher(country, city, hotel, beginDate, endDate, state , price, id);
    }
}
