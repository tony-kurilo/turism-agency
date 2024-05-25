package Interface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Manager4 {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public Label nameLabel;

    @FXML
    public Label numberLabel;

    @FXML
    public Label companyLabel;

    public void displayUserData() {
        String username = UserData.getUsername();

        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String storedUsername = parts[0];
                    String name = parts[2];
                    String tel_number = parts[3];
                    String company = parts[4];

                    if (storedUsername.equals(username)) {
                        nameLabel.setText(name);
                        numberLabel.setText(tel_number);
                        companyLabel.setText(company);
                        return;
                    }
                }
            }
            nameLabel.setText("Имя не найдено");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void switchToClients(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager1.fxml"));
        Parent root = loader.load();

        Manager1 manager1Controller = loader.getController();
        manager1Controller.scanClientDataFile();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager2.fxml"));
        Parent root = loader.load();

        Manager2 manager2Controller = loader.getController();
        manager2Controller.scanClientDataFile();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager3.fxml"));
        Parent root = loader.load();

        Manager3 manager3controller = loader.getController();
        manager3controller.searchCountries();

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
