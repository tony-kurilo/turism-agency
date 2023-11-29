package Classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Worker {
    protected String name;

    public Manager() {}

    public Manager(String login, String password,String name, String telNumber, String agencyName ) {
        super(login, password, agencyName, telNumber);
        this.name = name;
    }
    public Manager(String login, String name, String telNumber){
        this.login = login;
        this.name = name;
        this.telNumber = telNumber;
    }
    public StringProperty loginProperty() {
        return new SimpleStringProperty(this.login);
    }

    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public String getTelNumber(){
        return telNumber;
    }
    public String getAgencyName(){
        return agencyName;
    }




}
