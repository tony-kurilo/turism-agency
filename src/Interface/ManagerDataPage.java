package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ManagerDataPage {
    private Stage stage;
    private Scene scene;

    @FXML
    public Label nameLabel;

    @FXML
    public Label numberLabel;

    @FXML
    public Label companyLabel;

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

    public void displayUserData() {
        int id = UserData.getId();
        String query = "SELECT name, tel_number, agency_name FROM managers WHERE id = ?";

        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nameLabel.setText(resultSet.getString("name"));
                numberLabel.setText(resultSet.getString("tel_number"));
                companyLabel.setText(resultSet.getString("agency_name"));
            } else {
                nameLabel.setText("Имя не найдено");
                numberLabel.setText("-");
                companyLabel.setText("-");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void switchToClients(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerEditClientPage.fxml"));
        Parent root = loader.load();

        ManagerEditClientPage managerEditClientPageController = loader.getController();
        managerEditClientPageController.scanClientDataFile();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerProcessVouchersPage.fxml"));
        Parent root = loader.load();

        ManagerProcessVouchersPage managerProcessVouchersPageController = loader.getController();
        managerProcessVouchersPageController.scanClientDataFile();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerDataAnalysisPage.fxml"));
        Parent root = loader.load();

        ManagerDataAnalysisPage managerDataAnalysisPageController = loader.getController();
        managerDataAnalysisPageController.searchCountries();

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
}
