package bgu.spl.net.srv;

public class User {
    private int connectionId;
    private String username;
    private String password;
    private boolean online;

    public User(int connectionId, String username, String password) {
        this.connectionId = connectionId;
        this.username = username;
        this.password = password;
        this.online = true;
    }

    public boolean isOnline() {
        return online;
    }

    public void setStatus(boolean b) {
        online = b;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getConnectionId() {
        return connectionId;
    }
}
