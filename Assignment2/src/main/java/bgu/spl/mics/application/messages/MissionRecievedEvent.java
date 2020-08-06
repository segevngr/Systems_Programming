package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class  MissionRecievedEvent implements Event  {

    private MissionInfo missionInfo;

    public  MissionRecievedEvent (MissionInfo missionInfo, int timeIssued) {
        this.missionInfo = missionInfo;
    }

    public MissionInfo getMissionInfo() {return missionInfo; }

}
