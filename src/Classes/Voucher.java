package Classes;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class Voucher {
    private static long idCounter = 0;
    private String clientName;
    private String country;
    private String city;
    private String hotel;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String state;
    private String price;
    private String id;

    public Voucher(String clientName, String country, String city, String hotel, LocalDate beginDate, LocalDate endDate) {
        this.clientName = clientName;
        this.country = country;
        this.city = city;
        this.hotel = hotel;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.state = "В обробці";
        this.price = " ";
        this.id = String.valueOf(generateUniqueId()); // Генерация уникального ID
    }
    public String getClientName(){
        return clientName;
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
    public LocalDate getBeginDate(){
        return beginDate;
    }
    public LocalDate getEndDate(){
        return endDate;
    }
    public String getState(){
        return state;
    }
    public String getPrice(){
        return price;
    }
    public String getId(){
        return id;
    }
    private synchronized long generateUniqueId(){
        return ++idCounter;
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