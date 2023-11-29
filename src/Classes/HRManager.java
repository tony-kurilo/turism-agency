package Classes;

public class HRManager extends Worker {
    protected String name;

    public HRManager() {
    }

    public HRManager(String login, String password, String name, String telNumber, String agencyName) {
        super(login, password, agencyName, telNumber);
        this.name = name;
    }

}
