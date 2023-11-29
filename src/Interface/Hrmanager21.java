package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Path;


import javafx.scene.shape.Path;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Hrmanager21 {
    @FXML
    private Label countryLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label hotelLabel;
    @FXML
    private Label beginDateLabel;
    @FXML
    private Label endDateLabel;
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
    @FXML
    private TextField urlTextField;

    public void displayUserData() {
        String storedCountry = UserData.getCountry();
        String storedCity = UserData.getCity();
        String storedHotel = UserData.getHotel();
        LocalDate storedBeginDate = UserData.getBeginDate();
        LocalDate storedEndDate = UserData.getEndDate();


        if (storedCountry != null) {
            String filePath = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt";



            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String country = parts[0];
                        String city = parts[1];
                        String hotel = parts[2];
                        LocalDate beginDate = LocalDate.parse(parts[3]);
                        LocalDate endDate = LocalDate.parse(parts[4]);
                        if (storedCountry.equals(country) || storedCity.equals(city) || storedHotel.equals(hotel) || storedBeginDate.equals(beginDate) || storedEndDate.equals(endDate)) {
                            countryLabel.setText(country);
                            cityLabel.setText(city);
                            hotelLabel.setText(hotel);
                            beginDateLabel.setText(beginDate.toString());
                            endDateLabel.setText(endDate.toString());
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }

    }
    public void updateVoucherData() {
        String storedCountry = UserData.getCountry();
        String storedCity = UserData.getCity();
        String storedHotel = UserData.getHotel();
        LocalDate storedBeginDate = UserData.getBeginDate();
        LocalDate storedEndDate = UserData.getEndDate();

        // Получение новых данных из TextField и DatePicker
        String newCountry = countryTextField.getText();
        String newCity = cityTextField.getText();
        String newHotel = hotelTextField.getText();
        LocalDate newBeginDate = beginDatePicker.getValue();
        LocalDate newEndDate = endDatePicker.getValue();

        String url = urlTextField.getText();

        // Пути к файлам с данными
        String filePath1 = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\vouchers.txt";
        String filePath2 = "C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\hotels.txt";

        List<String> matchingLines1 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 &&
                        parts[0].equals(storedCountry) &&
                        parts[1].equals(storedCity) &&
                        parts[2].equals(storedHotel) &&
                        parts[3].equals(storedBeginDate.toString()) &&
                        parts[4].equals(storedEndDate.toString())) {
                    matchingLines1.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Проводим поиск во втором файле
        List<String> matchingLines2 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath2))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(storedHotel)) {
                    matchingLines2.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Если найдены совпадения в обоих файлах, производим замену данных
        if (!matchingLines1.isEmpty() && !matchingLines2.isEmpty()) {
            try {
                // Замена данных в первом файле
                List<String> fileLines1 = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath1))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (matchingLines1.contains(line)) {
                            // Замена строки на новые данные
                            line = newCountry + "," + newCity + "," + newHotel + "," + newBeginDate + "," + newEndDate;
                        }
                        fileLines1.add(line);
                    }
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath1))) {
                    for (String line : fileLines1) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                /// Замена данных во втором файле
                List<String> fileLines2 = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath2))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (matchingLines2.contains(line)) {
                            // Замена строки на новые данные
                            line = newHotel + "," + url;
                        }
                        fileLines2.add(line);
                    }
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2))) {
                    for (String line : fileLines2) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}



