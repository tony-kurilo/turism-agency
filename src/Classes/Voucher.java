package Classes;

import Interface.UserData;
import javafx.scene.control.DatePicker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class Voucher {
    private static long idCounter = 0;
    private String username;
    private String country;
    private String city;
    private String hotel;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String state;
    private double price;
    private int id;
    private String url;

    public Voucher(int id, String username, String country, String city, String hotel, LocalDate beginDate, LocalDate endDate, String state, double price ) {
        this.id = id;
        this.username = username;
        this.country = country;
        this.city = city;
        this.hotel = hotel;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.state = state;
        this.price = price;  // Assign price as double
    }
    public Voucher(String username, String country, String city, String hotel, LocalDate beginDate, LocalDate endDate, double price) {
        this.username = username;
        this.country = country;
        this.city = city;
        this.hotel = hotel;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.state = "default_state";  // Default value
        this.price = price;  // Set price as double
        this.id = (int) (Math.random() * 10000); // Generate random id
    }

    public Voucher(String username, String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {
        this.username = username;
        this.country = country;
        this.city = city;
        this.hotel = hotel;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.state = "default_state";  // Default value
        this.price = price;  // Set price as double
        this.id = (int) (Math.random() * 10000); // Generate random id
    }

    @Override
    public String toString() {
        return username + "," + country + "," + city + "," + hotel + "," + beginDate + "," + endDate + "," + state + "," + price + "," + id;
    }
    public String toString1() {
        return country + "," + city + "," + hotel + "," + beginDate + "," + endDate;
    }

    public String getUsername(){
        return username;
    }
    public String getCountry(){
        return country;
    }
    public String getCity(){
        return city;
    }
    public String getHotel(){
        return hotel;
    }
    public String getUrl(){
        return url;
    }
    public LocalDate getBeginDate(){
        return beginDate;
    }
    public LocalDate getEndDate(){
        return endDate;
    }
    public String getState(){
        return state;
    }
    public double getPrice(){
        return price;
    }

    public int getId() {
        return id;  // Возвращаем id как int
    }

    public void setId(int id) {
        this.id = id;  // Принимаем id как int и сохраняем в переменную
    }
    public String setState(String state){
        this.state = state;
        return state;
    }
    public double setPrice(Double price){
        this.price = price;
        return price;
    }
    private synchronized long generateUniqueId() {

        long maxId = getMaxIdFromFile("C:\\Users\\kuril\\IdeaProjects\\kursova\\src\\Interface\\clientVouchers.txt");
        return ++maxId;
    }
    private long getMaxIdFromFile(String filePath) {
        long maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 8) {
                    long currentId = Long.parseLong(parts[8]);
                    maxId = Math.max(maxId, currentId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId;
    }
}
/*protected String clientName;
    protected String country;
    protected String city;
    protected DatePicker beginTime;
    protected DatePicker endTime;
    protected String hotel;
    protected String state;
    protected String price;
    protected String id;
    //private List<String> data;

    public Voucher(String client, String country, String city, LocalDate beginTime, LocalDate endTime, String hotel, String state, String price, String id) {
        this.clientName = client.getName();
        this.country = country;
        this.city = city;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.hotel = hotel;
        this.state = state;
        this.price = price;
        this.id = id;
    }
    public String getClientName() {
        return clientName;
    }
    public String getCountry() {
        return country;
    }
    public String getCity(){return city;}
    public String getBeginTime(){return beginTime;}
    public String getEndTime(){return endTime;}
    public String getHotel(){return hotel;}
    public String getState(){return state;}
    public String getPrice(){return price;}
    public String getId(){return id;}

    public Voucher() {
        data = new ArrayList<>();
    }

    public void setName(String name) {
        this.type = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public void fillDataToVoucher(String clientName) {
        // Реализация метода fillDataToVoucher
    }

    public static void calculateAveragePrice() {
        // Реализация метода calculateAveragePrice
    }

    public static int getDayDifference(String beginTime, String endTime) {
        // Реализация метода getDayDifference
        return 0;
    }

    public static void calculateAverageTravelTime() {
        // Реализация метода calculateAverageTravelTime
    }

    public static void showCountryWithHighestDemand() {
        // Реализация метода showCountryWithHighestDemand
    }*/