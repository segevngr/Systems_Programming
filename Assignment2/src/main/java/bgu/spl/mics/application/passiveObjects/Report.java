package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {

    private String missionName;
    private int M;
    private int moneypenny;
    private List<String> agentsSerials;
    private List<String> agentNames;
    private String gadget;
    private int timeIssued;
    private int qTime;
    private int timeCreated;

    /**
     * Retrieves the mission name.
     */
    public String getMissionName() {
        return this.missionName;
    }

    /**
     * Sets the mission name.
     */
    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    /**
     * Retrieves the M's id.
     */
    public int getM() {
        return this.M;
    }

    /**
     * Sets the M's id.
     */
    public void setM(int m) {
        this.M = m;
    }

    /**
     * Retrieves the Moneypenny's id.
     */
    public int getMoneypenny() {

        return this.moneypenny;
    }

    /**
     * Sets the Moneypenny's id.
     */
    public void setMoneypenny(int moneypenny) {
        this.moneypenny = moneypenny;
    }

    /**
     * Retrieves the serial numbers of the agents.
     * <p>
     *
     * @return The serial numbers of the agents.
     */
    public List<String> getAgentsSerials() {
        return this.agentsSerials;
    }

    /**
     * Sets the serial numbers of the agents.
     */
    public void setAgentsSerials(List<String> agentsSerials) {
        this.agentsSerials = agentsSerials;
    }

    /**
     * Retrieves the agents names.
     * <p>
     *
     * @return The agents names.
     */
    public List<String> getAgentNames() {
        return this.agentNames;
    }

    /**
     * Sets the agents names.
     */
    public void setAgentNames(List<String> agentNames) {
        this.agentNames = agentNames;
    }

    /**
     * Retrieves the name of the gadget.
     * <p>
     *
     * @return the name of the gadget.
     */
    public String getGadget() {
        return this.gadget;
    }

    /**
     * Sets the name of the gadget.
     */
    public void setGadget(String gadget) {
        this.gadget = gadget;
    }

    /**
     * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public int getqTime() {
        return this.qTime;
    }

    /**
     * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public void setqTime(int qTime) {
        this.qTime = qTime;
    }

    /**
     * Retrieves the time when the mission was sent by an Intelligence Publisher.
     */
    public int getTimeIssued() {
        return this.timeIssued;
    }

    /**
     * Sets the time when the mission was sent by an Intelligence Publisher.
     */
    public void setTimeIssued(int timeIssued) {
        this.timeIssued = timeIssued;
    }

    /**
     * Retrieves the time-tick when the report has been created.
     */
    public int getTimeCreated() {
        return this.timeCreated;
    }

    /**
     * Sets the time-tick when the report has been created.
     */
    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }
}