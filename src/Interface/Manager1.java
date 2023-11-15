package Interface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Manager1 {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToVouchers(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager2.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToDataAnalys(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager3.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToData(javafx.event.ActionEvent actionEvent) throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("manager4.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("manager4.fxml"));
        Parent root = loader.load();

        Manager4 manager4controller = loader.getController();
        manager4controller.displayUserData();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToDeleteClientScene(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("manager11.fxml"));
        Stage newStage = new Stage();
        newStage.setTitle("Название вашего нового окна"); // Установите заголовок нового окна
        newStage.setScene(new Scene(root));
        // Показываем новое окно
        newStage.show();
    }
    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
}
