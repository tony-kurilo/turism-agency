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

import java.sql.*;

public class ClientSearchVoucherPage {
    private Stage stage;
    private Scene scene;
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

    // Метод для подключения к базе данных PostgreSQL
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

    public void switchToMyVouchers(javafx.event.ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientVouchersPage.fxml"));
        Parent root = loader.load();

        ClientVouchersPage clientVouchersPageController = loader.getController();
        clientVouchersPageController.searchMyVouchers();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToMyData(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientDataPage.fxml"));
        Parent root = loader.load();

        ClientDataPage clientDataPageController = loader.getController();
        clientDataPageController.displayUserData();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        UserData.setId(0);
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
            alertLabel.setText("Неправильно введена дата");
            return;
        }

        boolean found = searchVoucherInDatabase(country, city, hotel, beginDate, endDate);

        if (found) {
            alertLabel.setText("");
            foundVoucher = new Voucher(UserData.getUsername(), country, city, hotel, beginDate, endDate);
            yesLabel.setText("Так, у наявності є така путівка");
            noLabel.setText("");
            displayVoucherDetails(country, city, hotel, beginDate, endDate);
            searchButton.setVisible(true);
            searchButton.setDisable(false);
            Anchor.setVisible(true);

        } else {
            foundVoucher = null;
            yesLabel.setText("");
            noLabel.setText("На жаль, такої путівки немає у наявності");
            clearVoucherDetails();
        }
    }

    private void clearVoucherDetails() {
        countryCityLabel.setText("");
        hotelLabel.setText("");
        datesLabel.setText("");
    }
    // Метод поиска путевки в базе данных
    private boolean searchVoucherInDatabase(String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {
        String query = "SELECT * FROM vouchers WHERE country = ? AND city = ? AND hotel = ? AND begin_date <= ? AND end_date >= ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, country);
            stmt.setString(2, city);
            stmt.setString(3, hotel);
            stmt.setDate(4, Date.valueOf(beginDate));
            stmt.setDate(5, Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Если путевка найдена, возвращаем true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String convertGoogleDriveURL(String url) {
        if (url != null && url.contains("drive.google.com")) {
            String fileId = url.split("id=")[1].split("\"")[0];// Извлекаем ID файла из URL
            return "https://drive.google.com/uc?id=" + fileId;
        }
        return url; // Возвращаем оригинальный URL, если это не ссылка на Google Drive
    }


    private void displayVoucherDetails(String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {
        String hotelImageURL = searchHotelImageInDatabase(hotel);

        countryCityLabel.setText(country + ", " + city);
        hotelLabel.setText(hotel);
        datesLabel.setText("З " + beginDate + " по " + endDate);

        if (hotelImageURL != null && !hotelImageURL.isEmpty()) {
            hotelImageURL = convertGoogleDriveURL(hotelImageURL); // Преобразование URL для Google Drive
            System.out.println("Loading image from URL: " + hotelImageURL); // Отладочный вывод
            hotelImageView.setImage(new Image(hotelImageURL));
        } else {
            System.out.println("No image found for hotel: " + hotel); // Отладочный вывод
            hotelImageView.setImage(null); // Очищаем изображение, если URL некорректен
        }
    }

    public void createVoucherRequest(ActionEvent actionEvent) throws IOException {
        writeVoucherRequestInDatabase(foundVoucher);
    }

    // Метод поиска изображения отеля в базе данных
    private String searchHotelImageInDatabase(String hotel) {
        String query = "SELECT image_url FROM hotel_images WHERE hotel = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hotel);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("image_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для записи заявки на путевку в базу данных
    private void writeVoucherRequestInDatabase(Voucher voucher) {
        String maxIdQuery = "SELECT MAX(id) FROM voucher_requests"; // Запрос на получение максимального id
        String insertQuery = "INSERT INTO voucher_requests (id, username, country, city, hotel, begin_date, end_date, state, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ' ', NULL)"; // Запрос на вставку нового тура с id

        try (Connection conn = connectToDatabase()) {
            // Получаем максимальный id
            PreparedStatement maxIdStmt = conn.prepareStatement(maxIdQuery);
            ResultSet rs = maxIdStmt.executeQuery();
            int newId = 1; // Если нет записей, начнем с 1
            if (rs.next()) {
                newId = rs.getInt(1) + 1; // Прибавляем 1 к максимальному id
            }

            // Вставляем новый тур с полученным id
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, newId); // Устанавливаем новый id
                stmt.setString(2, voucher.getUsername());
                stmt.setString(3, voucher.getCountry());
                stmt.setString(4, voucher.getCity());
                stmt.setString(5, voucher.getHotel());
                stmt.setDate(6, Date.valueOf(voucher.getBeginDate()));
                stmt.setDate(7, Date.valueOf(voucher.getEndDate()));
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
