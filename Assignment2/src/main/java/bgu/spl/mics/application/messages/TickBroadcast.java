package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private int time;

    public TickBroadcast(int time) {
        this.time = time;
    }

    public int getCurrTime() {
        return time;
    }
}
