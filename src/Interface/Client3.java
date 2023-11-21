package Interface;

import Classes.Voucher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Client3 {

    private Stage stage;
    private Scene scene;
    private Parent root;

    protected String ImagePath1;

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
    ImageView hotelImageView;

    public void switchToMyVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client1.fxml"));
        Parent root = loader.load();
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
        String country = countryTextField.getText();
        String city = cityTextField.getText();
        String hotel = hotelTextField.getText();
        LocalDate beginDate = beginDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Проводим поиск в файле с данными
        boolean found = searchVoucherInFile(country, city, hotel, beginDate, endDate);

        // Выводим результат в соответствующие метки и изображение
        if (found) {
            yesLabel.setText("Так, у наявності є така путівка");
            noLabel.setText(""); // Clear the noLabel
            displayVoucherDetails(country, city, hotel, beginDate, endDate);
        } else {
            yesLabel.setText(""); // Clear the yesLabel
            noLabel.setText("На жаль, такої путівки немає у наявності");
            clearVoucherDetails(); // Clear details if no voucher is found
        }
    }
    private boolean searchVoucherInFile(String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {
        // Пропишите путь к файлу с данными
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String countryFromFile = parts[0];
                String cityFromFile = parts[1];
                String hotelFromFile = parts[2];
                LocalDate beginDateFromFile = LocalDate.parse(parts[3], formatter);
                LocalDate endDateFromFile = LocalDate.parse(parts[4], formatter);


                // Проверяем совпадение
                if (country.equals(countryFromFile) && city.equals(cityFromFile) &&
                        hotel.equals(hotelFromFile) && beginDate.equals(beginDateFromFile) && endDate.equals(endDateFromFile)) {
                    return true; // Найдено совпадение
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return false; // Совпадение не найдено
    }

    private void displayVoucherDetails(String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {

        String hotelImageURL = searchHotelInFile(hotel);

        //String imagePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\images" + ImagePath1; // specify your image folder path
        //String hotelImageURL = new File(imagePath).toURI().toString();
        // Выводим значения в соответствующие метки и изображение
        countryCityLabel.setText(country + ", " + city);
        hotelLabel.setText(hotel);
        datesLabel.setText("З " + beginDate + " по " + endDate);
        // Вставьте код для отображения изображения в hotelImageView
        //hotelImageView.setImage(new Image(hotelImageURL));
        if (hotelImageURL != null) {
            hotelImageView.setImage(new Image(hotelImageURL));
        } else {
            // Вставьте код для очистки изображения в hotelImageView
            hotelImageView.setImage(null);
        }
    }

    private void clearVoucherDetails() {
        // Очищаем значения в метках и изображении
        countryCityLabel.setText("");
        hotelLabel.setText("");
        datesLabel.setText("");
        // Вставьте код для очистки изображения в hotelImageView
        //hotelImageView.setImage(null);
    }
    private String searchHotelInFile(String hotel) {
        // Пропишите путь к файлу с данными
        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделяем строку на части, используя запятую как разделитель
                String[] parts = line.split(",");

                // Предполагаем, что у нас есть все необходимые части данных
                String hotelFromFile = parts[0];
                String imageLink = parts[1];

                // Проверяем совпадение
                if (hotel.equals(hotelFromFile)) {
                    return imageLink; // Найдено совпадение
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработайте исключение, если что-то пошло не так при чтении файла
        }

        return null; // Совпадение не найдено
    }


}
