package Classes;

import java.util.ArrayList;
import java.util.List;
public class HRManager extends Worker {
    private List<String> data;
    private List<Manager> managers;

    public HRManager() {
        data = new ArrayList<>();
        managers = new ArrayList<>();
    }

    public HRManager(String name, String agencyName, String telNumber) {
        super(name, agencyName, telNumber);
        data = new ArrayList<>();
        managers = new ArrayList<>();
    }

    public void createManager() {
        // Реализация метода createManager
    }

    public void editManager() {
        // Реализация метода editManager
    }

    public void showManagerList() {
        // Реализация метода showManagerList
    }

    public void authorize() {
        // Реализация метода authorize
    }

    public boolean checkHRManagerExists(String hrManagerName) {
        // Реализация метода checkHRManagerExists
        return false;
    }

    public void deleteManager() {
        // Реализация метода deleteManager
    }
}
