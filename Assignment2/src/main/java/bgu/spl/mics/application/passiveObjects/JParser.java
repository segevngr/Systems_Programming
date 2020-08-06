package bgu.spl.mics.application.passiveObjects;

public class JParser {
    public String[] inventory;
    public GsonAgents[] squad;
    public GsonServices services;

    public class GsonGadget{
        public String gadget;
    }

    public class GsonSquad{
        GsonAgents[] agents;
    }
    public class GsonAgents {
        public String name;
        public String serialNumber;
    }

    public class GsonServices{
        public int M;
        public int Moneypenny;
        public GsonMissions[] intelligence;
        public int time;
    }
    public class GsonMissions{
        public MissionInfo[] missions;
    }


}