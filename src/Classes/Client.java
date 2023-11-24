package Classes;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String username;
    private String password;
    private String name;
    private String telNumber;
    private String address;
    private String agencyName;

    public Client() {}

    public Client(String username, String password, String name, String telNumber, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.telNumber = telNumber;
        this.address = address;
    }
    public Client(String username, String password, String name, String telNumber, String address, String agencyName) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.telNumber = telNumber;
        this.address = address;
        this.agencyName = agencyName;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }

    public String getTelNumber() {
        return telNumber;
    }
    public String getAgencyName() {
        return agencyName;
    }
    public boolean checkClientExists(String clientName) {
        // Реализация метода checkClientExists
        return false;
    }

    public void authorize() {
        // Реализация метода authorize
    }

    public void showMyVouchers(String clientName) {
        // Реализация метода showMyVouchers
    }
}
