package Classes;

public class Worker {
    protected String login;
    protected String password;
    protected String agencyName;
    protected String telNumber;

    public Worker() {}

    public Worker(String login,String password, String agencyName, String telNumber) {
        this.login = login;
        this.password = password;
        this.agencyName = agencyName;
        this.telNumber = telNumber;
    }
}
