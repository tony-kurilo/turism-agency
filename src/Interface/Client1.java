package Interface;

import Classes.Client;
import Classes.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

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


    public void switchToData(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("client2.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
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
    public void onSaveButtonClicked(ActionEvent event) {
        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String hotel = hotelTextField.getText();
        LocalDate beginDate = beginDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (country.isEmpty() || city.isEmpty() || hotel.isEmpty() || beginDate == null || endDate == null) {
            // Обработка ошибки: не все данные введены
            // Вывести сообщение или предпринять другие действия
            return;
        }

        String username = userModel.getUsername(); // Получить имя пользователя

        // Создать объект Voucher и передать данные
        Voucher voucher = new Voucher(username, country, city, hotel, beginDate, endDate);

        // Сохранить объект в файл
        VoucherManager.saveVoucherToFile(voucher);
    }

}
