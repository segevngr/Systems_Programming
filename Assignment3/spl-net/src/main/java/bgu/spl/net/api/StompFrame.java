package bgu.spl.net.api;


import java.util.HashMap;

public class StompFrame {

    protected String command;
    protected HashMap<String, String> headers;
    protected String FrameBody;

    public StompFrame() {
        headers = new HashMap<>();
        FrameBody = "";
    }

    public StompFrame(String msg) {
        headers = new HashMap<>();
        String[] splitedArray = msg.split("\n");
        command = splitedArray[0];
        String[] headerRow;
        for( int i = 1; i < splitedArray.length; i++) {
            if(splitedArray[i].contains(":")) {
                headerRow = splitedArray[i].split(":");
                headers.put(headerRow[0], headerRow[1]);
            }
        }
        if(!splitedArray[splitedArray.length - 1].contains(":"))
            FrameBody = splitedArray[splitedArray.length - 1];
    }

    public String getCommand() {
        return command;
    }

    public String getFrameBody() {
        return FrameBody;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setFrameBody(String frameBody) {
        this.FrameBody = frameBody;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
