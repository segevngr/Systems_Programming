package bgu.spl.net.srv;

import java.util.Queue;

public class Client  {
    private int Id;
    private int name; // unique variable
    private Queue<String> topicsQueuePerClient;
    private boolean isConnected;


    public boolean getConnectionStatus()
    {
        return this.isConnected;
    }

    public int getId() {
        return Id;
    }

    public int getName() {
        return name;
    }

    public Queue<String> getTopicsQueuePerClient() {
        return topicsQueuePerClient;
    }

    public void setConnectionStatus(boolean isConnected)
    {
        this.isConnected= isConnected;
    }
}
