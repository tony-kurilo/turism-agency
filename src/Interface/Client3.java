package Interface;

import Classes.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Client3 {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Voucher foundVoucher;


    @FXML
    TextField countryTextField;
    @FXML
    TextField cityTextField;
    @FXML
    TextField hotelTextField;
    @FXML
    DatePicker beginDatePicker;
    @FXML
    DatePicker endDatePicker;
    @FXML
    Label yesLabel;
    @FXML
    Label noLabel;
    @FXML
    Label countryCityLabel;
    @FXML
    Label hotelLabel;
    @FXML
    Label datesLabel;
    @FXML
    Label alertLabel;
    @FXML
    ImageView hotelImageView;
    @FXML
    Button searchButton;
    @FXML
    AnchorPane Anchor;

    public void switchToMyVouchers(javafx.event.ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("client1.fxml"));
        Parent root = loader.load();

        Client1 client1Controller = loader.getController();
        client1Controller.searchMyVouchers();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToMyData(javafx.event.ActionEvent actionEvent) throws IOException {
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

    public void searchVoucher(ActionEvent actionEvent) throws IOException {
        hotelImageView.setImage(null);

        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String hotel = hotelTextField.getText();
        LocalDate beginDate = null;
        LocalDate endDate = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            if (beginDatePicker.getEditor().getText() != null && !beginDatePicker.getEditor().getText().isEmpty()) {
                beginDate = LocalDate.parse(beginDatePicker.getEditor().getText(), formatter);
            }

            if (endDatePicker.getEditor().getText() != null && !endDatePicker.getEditor().getText().isEmpty()) {
                endDate = LocalDate.parse(endDatePicker.getEditor().getText(), formatter);
            }
        } catch (DateTimeParseException e) {
            // Выводим сообщение об ошибке в случае неверного формата
            alertLabel.setText("Неправильно введена дата");
            return;
        }


        boolean found = searchVoucherInFile(country, city, hotel, beginDate, endDate);


        if (found) {
            alertLabel.setText("");
            foundVoucher = new Voucher(UserData.getUsername(), country, city, hotel, beginDate, endDate);
            yesLabel.setText("Так, у наявності є така путівка");
            noLabel.setText("");
            displayVoucherDetails(country, city, hotel, beginDate, endDate);
            searchButton.setVisible(true);
            Anchor.setVisible(true);

        } else {
            foundVoucher = null;
            yesLabel.setText("");
            noLabel.setText("На жаль, такої путівки немає у наявності");
            clearVoucherDetails();
        }
    }
    private boolean searchVoucherInFile(String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");


                String countryFromFile = parts[0];
                String cityFromFile = parts[1];
                String hotelFromFile = parts[2];
                LocalDate beginDateFromFile = LocalDate.parse(parts[3], formatter);
                LocalDate endDateFromFile = LocalDate.parse(parts[4], formatter);



                if (country.equals(countryFromFile) && city.equals(cityFromFile) && hotel.equals(hotelFromFile) &&
                        (beginDate.isEqual(beginDateFromFile) || beginDate.isAfter(beginDateFromFile)) &&
                        (endDate.isEqual(endDateFromFile) || endDate.isBefore(endDateFromFile))) {
                    return true;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return false;
    }

    private void displayVoucherDetails(String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {

        String hotelImageURL = searchHotelInFile(hotel);

        countryCityLabel.setText(country + ", " + city);
        hotelLabel.setText(hotel);
        datesLabel.setText("З " + beginDate + " по " + endDate);

        if (hotelImageURL != null) {
            hotelImageView.setImage(new Image(hotelImageURL));
        } else {

            hotelImageView.setImage(null);
        }
    }

    private void clearVoucherDetails() {
        countryCityLabel.setText("");
        hotelLabel.setText("");
        datesLabel.setText("");
    }
    private String searchHotelInFile(String hotel) {

        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String hotelFromFile = parts[0];
                String imageLink = parts[1];

                if (hotel.equals(hotelFromFile)) {
                    return imageLink;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }
    public void createVoucherRequest(ActionEvent actionEvent) throws IOException{
        writeVoucherRequestInFile(foundVoucher);
    }


    private void writeVoucherRequestInFile(Voucher voucher) {
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(voucher.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    

}
