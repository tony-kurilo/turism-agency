package Interface;

import Classes.Client;
import Classes.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Hrmanager1 {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label alertCreateLabel;
    @FXML
    private Label alertCreate2Label;
    @FXML
    private TableView<Manager> managersTableView;
    @FXML
    private TableColumn<Manager, String> loginColumn;
    @FXML
    private TableColumn<Manager, String> nameColumn;
    @FXML
    private TableColumn<Manager, String> telNumberColumn;
    @FXML
    private ChoiceBox<String> managerEditChoceBox;
    @FXML
    private ChoiceBox<String> managerDeleteChoiceBox;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField telNumberTextField;
    public void scanManagerFile() {
        ObservableList<Manager> managers = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String login = data[0].trim();
                    String name = data[2].trim();
                    String telNumber = data[3].trim();

                    Manager manager = new Manager(login, name, telNumber);
                    managers.add(manager);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        telNumberColumn.setCellValueFactory(new PropertyValueFactory<>("telNumber"));

        managersTableView.setItems(managers);

        Set<String> uniqueLogins = managers.stream().map(Manager::getLogin).collect(Collectors.toSet());

        managerEditChoceBox.setItems(FXCollections.observableArrayList(uniqueLogins));
        managerDeleteChoiceBox.setItems(FXCollections.observableArrayList(uniqueLogins));

    }
    public void createManagerProfile(ActionEvent actionEvent){
        String login = loginTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        String name = nameTextField.getText().trim();
        String telNumber = telNumberTextField.getText().trim();

        if (login.isEmpty() || password.isEmpty() || name.isEmpty() || telNumber.isEmpty()) {
            alertCreateLabel.setText("Заповніть усі поля");
            return;
        }
        alertCreateLabel.setText("");
        if (checkForDuplicates(login, telNumber, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt")) {
            alertCreate2Label.setText("Профіль з таким логіном\n чи телефоном вже існує");
            return;
        }
        alertCreate2Label.setText("");
        Manager manager = new Manager(login, password, name, telNumber, "Touristique");

        addManagerToFile(manager, "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt");
        scanManagerFile();
    }
    private void addManagerToFile(Manager manager, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(manager.getLogin() + "," + manager.getPassword() + "," +
                    manager.getName() + "," + manager.getTelNumber() + "," + manager.getAgencyName());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean checkForDuplicates(String username, String telNumber, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String existingUsername = parts[0].trim();
                    String existingTelNumber = parts[3].trim();

                    if (username.equals(existingUsername) || telNumber.equals(existingTelNumber)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public void editManagerProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrmanager11.fxml"));
        Parent root = loader.load();

        Hrmanager11  hrmanager11Controller = loader.getController();
        String selectedUserName = managerEditChoceBox.getValue();
        UserData.setUsername(selectedUserName);
        hrmanager11Controller.displayUserData();
        Stage newStage = new Stage();
        newStage.setTitle("Редагування даних Менеджера");
        newStage.setScene(new Scene(root));
        newStage.show();

    }

    private List<Manager> readManagerData(File file) {
        List<Manager> managerData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String username = parts[0];
                String password = parts[1];
                String name = parts[2];
                String telNumber = parts[3];
                String agencyName = parts[4];

                Manager manager = new Manager(username, password, name, telNumber, agencyName);
                managerData.add(manager);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return managerData;
    }

    public void deleteManagerProfile(ActionEvent actionEvent) throws IOException{
        String selectedUserName = managerDeleteChoiceBox.getValue();

        java.util.List<Manager> updatedManagers = new ArrayList<>();

        List<Manager> existingManagers = readManagerData(new File("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt"));

        for (Manager manager : existingManagers) {
            if (!manager.getLogin().equals(selectedUserName)) {
                updatedManagers.add(manager);
            }
        }

        updateDataFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\managers.txt", updatedManagers);

        scanManagerFile();
    }
    private void updateDataFile(String filename, List<Manager> managers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Manager manager : managers) {
                String line = String.format("%s,%s,%s,%s,%s",
                        manager.getLogin(), manager.getPassword(), manager.getName(),
                        manager.getTelNumber(),  manager.getAgencyName());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToCreationVoucher(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hrmanager2.fxml"));
        Parent root = loader.load();

        Hrmanager2 hrmanager2Controller = loader.getController();
        hrmanager2Controller.scanVoucherFile();

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
