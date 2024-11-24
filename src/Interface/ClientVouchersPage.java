package Interface;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.sql.*;
public class ClientVouchersPage {
    private Stage stage;
    private Scene scene;
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
    @FXML
    private ChoiceBox<String> deleteVoucherChoiceBox;

    // Подключение к базе данных PostgreSQL
    private Connection connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Touristique%20DB%20(Java)";  // Используем вашу базу данных
        String user = "postgres";
        String password = "3113";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void switchToCreationVoucher(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientSearchVoucherPage.fxml"));
        Parent root = loader.load();
        ClientSearchVoucherPage clientSearchVoucherPageController = loader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToData(javafx.event.ActionEvent actionEvent) throws IOException {
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

    public void searchMyVouchers() {
        String username = UserData.getUsername();
        ObservableList<Voucher> voucherList = FXCollections.observableArrayList();

        String query = "SELECT * FROM voucher_requests WHERE username = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Voucher voucher = new Voucher(
                        rs.getInt("id"),  // Используем rs.getInt для получения id как int
                        rs.getString("username"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("hotel"),
                        rs.getDate("begin_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getString("state"),
                        rs.getDouble("price")
                );
                voucherList.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        Set<String> uniqueIds = new HashSet<>();
        for (Voucher voucher : voucherList) {
            uniqueIds.add(String.valueOf(voucher.getId()));  // Преобразуем int id в String для отображения в ChoiceBox
        }
        deleteVoucherChoiceBox.getItems().setAll(uniqueIds);
    }

    public void deleteVoucher(ActionEvent actionEvent) {
        String selectedId = deleteVoucherChoiceBox.getValue();

        if (selectedId != null && !selectedId.isEmpty()) {
            String deleteQuery = "DELETE FROM voucher_requests WHERE id = ?";
            try (Connection conn = connectToDatabase();
                 PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                stmt.setString(1, selectedId);
                stmt.executeUpdate();
                searchMyVouchers(); // Обновляем таблицу после удаления
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
