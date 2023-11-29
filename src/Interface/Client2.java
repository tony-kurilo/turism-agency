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

public class Client2 {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public Label nameLabel;

    @FXML
    public Label numberLabel;

    @FXML
    public Label addressLabel;

    public void displayUserData() {
        String username = UserData.getUsername();

        String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clients.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String storedUsername = parts[0];
                    String name = parts[2];
                    String tel_number = parts[3];
                    String address = parts[4];

                    if (storedUsername.equals(username)) {
                        nameLabel.setText(name);
                        numberLabel.setText(tel_number);
                        addressLabel.setText(address);
                        return;
                    }
                }
            }
            nameLabel.setText("Ім'я не знайдено");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client3.fxml"));
        Parent root = loader.load();
        Client3 client3Controller = loader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
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
    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
}
