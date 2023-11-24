package Classes;

import java.util.ArrayList;
import java.util.List;
public class HRManager extends Worker {
    protected String name;

    public HRManager() {
    }

    public HRManager(String login, String password, String name, String telNumber, String agencyName) {
        super(login, password, agencyName, telNumber);
        this.name = name;
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
