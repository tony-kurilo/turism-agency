package Classes;

import java.util.ArrayList;
import java.util.List;

public class Client {
    protected String username;
    protected String password;
    protected String name;
    protected String telNumber;
    protected String address;
    protected String agencyName;

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

}
