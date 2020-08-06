package bgu.spl.net.srv;

import bgu.spl.net.api.StompFrame;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImpl implements Connections<String> {

    private ConcurrentHashMap<String, User> usersByName;          // Username | User
    private ConcurrentHashMap<Integer, User> usersById;             // Connection ID | User
    private ConcurrentHashMap<String, Map<User, Integer>> topics;   // Topic | list of subscribed users
    private Map<Integer, ConnectionHandler> connectionHandlers;     // Connection ID | ConnectionHandler
    private AtomicInteger messageId;

    private static ConnectionsImpl instance = new ConnectionsImpl();

    private ConnectionsImpl() {
        usersByName = new ConcurrentHashMap<>();                         // Username | User
        usersById = new ConcurrentHashMap<>();                           // Connection ID | User
        topics = new ConcurrentHashMap<>();    // Topic | list of subscribed users
        connectionHandlers = new ConcurrentHashMap<>();
        messageId = new AtomicInteger(0);
    }

    public static ConnectionsImpl getInstance() {
        return instance;
    }

    // Send to a specific clients
    public boolean send(int connectionId, String msg) {

        //TODO Remove this:
        System.out.println("Private Send: ");
        System.out.println(msg);
        System.out.println("");

        ConnectionHandler connectionHandler = connectionHandlers.get(connectionId);
        if (connectionHandler != null) {
            connectionHandler.send(msg);
            return true;
        } else {
            disconnect(connectionId);
            return false;
        }
    }

    // Send to all topic-subscribed clients
    public void send(String channel, String msg) {
         //TODO Remove this:
        System.out.println("Channel Send: ");
        System.out.println(msg);
        System.out.println("");

        Map<User, Integer> users = topics.get(channel);
        for (Map.Entry<User, Integer> entry : users.entrySet()) {
            if (connectionHandlers.containsKey(entry.getKey().getConnectionId()))  // If subscribed user is in
                connectionHandlers.get(entry.getKey().getConnectionId()).send(msg);   // send him the message
        }
    }

    // TODO: Verify
    public void disconnect(int connectionId) {
        if (usersById.containsKey(connectionId)) {
            User user = usersById.get(connectionId);
            user.setStatus(false);
            for (Map.Entry<String, Map<User,Integer>> entry : topics.entrySet()) // Remove from topics
            {
                if (entry.getKey() != null && entry.getValue().containsKey(user)) {
                    synchronized (entry.getValue()) {
                        entry.getValue().remove(user);
                    }
                }
            }
        }
    }


    public void connect(int connectionId, ConnectionHandler<String> handler) {
        connectionHandlers.putIfAbsent(connectionId, handler);
    }


    public ConcurrentHashMap<String, User> getUsersByName() {
        return usersByName;
    }

    public ConcurrentHashMap<Integer, User> getUsersById() {
        return usersById;
    }

    public ConcurrentHashMap<String, Map<User, Integer>> getTopics() {
        return topics;
    }

    public int getAndIncrementMessageId() {
        return messageId.getAndIncrement();
    }

    synchronized public void subscribeUser(String topic, int connectionId, int subscriptionId) {
        User user = usersById.get(connectionId);
        topics.putIfAbsent(topic, new ConcurrentHashMap<>());
        topics.get(topic).put(user, subscriptionId);
    }

    synchronized public void unsubscribeUser(int connectionId, int subscriptionId) {
        User user = usersById.get(connectionId);
        for (Map.Entry<String, Map<User, Integer>> entry : topics.entrySet()) {
            if (entry.getValue().containsKey(user) && entry.getValue().containsValue(subscriptionId))
                entry.getValue().remove(user);
        }
    }

    synchronized public void addUser(User user) {
        usersById.put(user.getConnectionId(), user);
        usersByName.put(user.getUsername(), user);
    }

    synchronized public int getSubscriptionId(String topic, int connectionId){
        User user = usersById.get(connectionId);
        Map<User, Integer> m = topics.get(topic);
        return m.get(user);
    }

}


