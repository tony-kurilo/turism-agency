package Classes;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Worker {


    public Manager() {
    }

    public Manager(String name, String agencyName, String telNumber) {
        super(name, agencyName, telNumber);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getName() {
        return name;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public static void readDataFromFile() {
        // Реализация метода readDataFromFile
    }

    public void registerClient() {
        // Реализация метода registerClient
    }

    public boolean checkClientExists(String clientName) {
        // Реализация метода checkClientExists
        return false;
    }

    public void authorizeClient() {
        // Реализация метода authorizeClient
    }

    public void displayClientInformation(String clientName) {
        // Реализация метода displayClientInformation
    }

    public void showClientVouchers(String clientName) {
        // Реализация метода showClientVouchers
    }

    public void showClientList() {
        // Реализация метода showClientList
    }

    public void editClient() {
        // Реализация метода editClient
    }

    public void deleteClient() {
        // Реализация метода deleteClient
    }

    public boolean checkManagerExists(String managerName) {
        // Реализация метода checkManagerExists
        return false;
    }

    public void authorizeManager() {
        // Реализация метода authorizeManager
    }

    public void createVoucher(String clientName) {
        // Реализация метода createVoucher
    }

    public void editVoucher(String clientName) {
        // Реализация метода editVoucher
    }

    public void deleteVoucher(String clientName) {
        // Реализация метода deleteVoucher
    }

    public void showClientWhoChoseCountry() {
        // Реализация метода showClientWhoChoseCountry
    }

    public void showAverageCostVoucher() {
        // Реализация метода showAverageCostVoucher
    }

    public void showAverageDurationTrip() {
        // Реализация метода showAverageDurationTrip
    }

    public void showCountryWithHighestDemand() {
        // Реализация метода showCountryWithHighestDemand
    }
}
