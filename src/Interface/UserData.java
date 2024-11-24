package Interface;

import java.time.LocalDate;

public class UserData {

    private static int id;
    private static String username;
    private static String password;

    private static String country;
    private static String city;
    private static String hotel;
    private static LocalDate beginDate;
    private static LocalDate endDate;
    private static String url;

    public static int getId() {
        return id;  // Возвращаем id как int
    }

    public static void setId(int id) {
        UserData.id = id;  // Принимаем id как int и сохраняем в переменную
    }


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserData.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserData.password = password;
    }

    public static String getCountry() {
        return country;
    }
    public static void setCountry(String country) {
        UserData.country = country;
    }
    public static String getCity() {
        return city;
    }
    public static void setCity(String city) {
        UserData.city = city;
    }
    public static String getHotel() {
        return hotel;
    }
    public static void setHotel(String hotel) {
        UserData.hotel = hotel;
    }
    public static LocalDate getBeginDate() {
        return beginDate;
    }
    public static void setBeginDate(LocalDate beginDate) {
        UserData.beginDate = beginDate;
    }
    public static LocalDate getEndDate() {
        return endDate;
    }
    public static void setEndDate(LocalDate endDate) {
        UserData.endDate = endDate;
    }
    public static String getUrl() {
        return url;
    }
    public static void setUrl(String url) {
        UserData.url = url;
    }
}
