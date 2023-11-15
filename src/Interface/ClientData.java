package Interface;

public class ClientData {
    private static String name;

    public static String getClientName() {
        return name;
    }

    public static void setClientName(String name) {
        ClientData.name = name;
    }
}
