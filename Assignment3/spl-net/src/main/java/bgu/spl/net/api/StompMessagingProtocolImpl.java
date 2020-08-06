package bgu.spl.net.api;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.User;
import java.util.Map;

public class StompMessagingProtocolImpl implements StompMessagingProtocol {
    private int connectionId;
    private ConnectionsImpl connections;
    private boolean terminate;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = ConnectionsImpl.getInstance();
        terminate = false;
    }

    @Override
    public void process(String message) {

        StompFrame clientFrame = new StompFrame(message);
        String command = clientFrame.getCommand();

        switch(command) {
            case "SEND":
                StompFrame send_message = new StompFrame();
                send_message.setCommand("SEND");
                send_message.addHeader("subscription",
                        String.valueOf(connections.getSubscriptionId(clientFrame.getHeaders().get("destination"), connectionId)));
                send_message.addHeader("Message-id", String.valueOf(connections.getAndIncrementMessageId()));
                send_message.addHeader("destination", clientFrame.getHeaders().get("destination"));
                send_message.setFrameBody(clientFrame.getFrameBody());
                // Send to Channel:
                connections.send(clientFrame.getHeaders().get("destination"), StompFrameToString(send_message));
                break;

            case "SUBSCRIBE":
                String topic = clientFrame.getHeaders().get("destination");
                String subscriptionId = clientFrame.getHeaders().get("id");
                connections.subscribeUser(topic, connectionId, Integer.parseInt(subscriptionId));
                StompFrame subscribe_receipt = new StompFrame();
                subscribe_receipt.setCommand("RECEIPT");
                subscribe_receipt.addHeader("receipt-id", clientFrame.getHeaders().get("receipt"));
                connections.send(connectionId, StompFrameToString(subscribe_receipt));
                break;

            case "UNSUBSCRIBE":
                connections.unsubscribeUser(connectionId, Integer.parseInt(clientFrame.getHeaders().get("id")));
                StompFrame unsubscribe_receipt = new StompFrame();
                unsubscribe_receipt.setCommand("RECEIPT");
                unsubscribe_receipt.addHeader("receipt-id", clientFrame.getHeaders().get("receipt"));
                connections.send(connectionId, StompFrameToString(unsubscribe_receipt));
                break;

            case "CONNECT":
                String username = clientFrame.getHeaders().get("login");
                String password = clientFrame.getHeaders().get("passcode");

                Map<String, User> usersByName = connections.getUsersByName();
                if(!usersByName.containsKey(username))  //  New User
                {
                    User user = new User(connectionId, username, password);
                    connections.addUser(user);
                    StompFrame connect_connected = new StompFrame();
                    connect_connected.setCommand("CONNECTED");
                    connect_connected.addHeader("version", clientFrame.getHeaders().get("accept-version"));
                    connections.send(connectionId, StompFrameToString(connect_connected));
                }
                else if (!usersByName.get(username).getPassword().equals(password)){
                    StompFrame connect_err1 = new StompFrame();
                    connect_err1.setCommand("ERROR");
                    connect_err1.addHeader("message", "Wrong password");
                    connect_err1.setFrameBody("Wrong password");
                    connections.send(connectionId, StompFrameToString(connect_err1));
                }
                else if (usersByName.get(username).isOnline()) {
                    StompFrame connect_err2 = new StompFrame();
                    connect_err2.setCommand("ERROR");
                    connect_err2.addHeader("message", "User already logged in");
                    connect_err2.setFrameBody("User already logged in");
                    connections.send(connectionId, StompFrameToString(connect_err2));
                }
                else {  // User exists
                StompFrame connect_connected = new StompFrame();
                connect_connected.setCommand("CONNECTED");
                connect_connected.addHeader("version", clientFrame.getHeaders().get("accept-version"));
                connections.send(connectionId, StompFrameToString(connect_connected));
                }
                break;

            case "DISCONNECT":

                StompFrame disconnect_frame = new StompFrame();
                disconnect_frame.setCommand("RECEIPT");
                disconnect_frame.addHeader("receipt-id",clientFrame.getHeaders().get("receipt"));
                connections.send(connectionId, StompFrameToString(disconnect_frame));
                connections.disconnect(connectionId);
                terminate = true;
                break;

                /*
                connections.removeUser(connectionId);
                StompFrame disconnect_receipt = new StompFrame();
                disconnect_receipt.setCommand("RECEIPT");
                disconnect_receipt.addHeader("receipt-id", clientFrame.getHeaders().get("receipt"));
                connections.send(connectionId, StompFrameToString(disconnect_receipt));
                terminate = true;
                break;

                 */
        }
    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }

    public String StompFrameToString (StompFrame frame) {
        String output = "";
        output += frame.getCommand() + "\n";
        for (Map.Entry<String, String> entry : frame.getHeaders().entrySet())
            output += entry.getKey() + ":" + entry.getValue() + "\n";
        output += "\n" + frame.getFrameBody();
        return output;
    }

}
