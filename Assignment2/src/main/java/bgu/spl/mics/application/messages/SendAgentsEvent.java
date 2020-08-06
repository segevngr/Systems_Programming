package bgu.spl.mics.application.messages;
import java.util.List;

import bgu.spl.mics.Event;

public class SendAgentsEvent implements Event {
    private List<String> serials;
    private int duration;

    public SendAgentsEvent(List <String> serials, int duration) {
        this.serials = serials;
        this.duration = duration;
    }

    public List<String> getSentAgentsSerials() {
        return serials;
    }

    public int getDuration() {
        return duration;
    }


}
