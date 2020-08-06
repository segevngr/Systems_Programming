package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.messages.MissionRecievedEvent;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

    private int currTick = 0;
    private List<MissionInfo> missionList;

    public Intelligence(LinkedList<MissionInfo> missionList, String name) {
        super(name);
        this.missionList = missionList;
        this.missionList.sort(Comparator.comparingInt(MissionInfo::getTimeIssued));    // Sort missions by TimeIssued field
        this.setSerialNumber(Integer.parseInt(name.substring(name.indexOf(' ')+1)));
    }


    @Override
    protected void initialize() {

        subscribeBroadcast(TickBroadcast.class, (tickB) -> {
            this.currTick = tickB.getCurrTime();
            // Goes over missions and creates MissionRecievedEvent
            while (missionList.size() > 0 && currTick >= missionList.get(0).getTimeIssued()) {
                MissionInfo currMission = missionList.remove(0);
                MissionRecievedEvent mission = new MissionRecievedEvent(currMission, currTick);
                getSimplePublisher().sendEvent(mission);
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, (terminateB) -> {
            terminate();
        });

    }

}
