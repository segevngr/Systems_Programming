package bgu.spl.mics.application.passiveObjects;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private ConcurrentHashMap<String, Agent> agents;
	private static Squad instance = new Squad();

	// Constructor
	private Squad() {
		this.agents = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for(Agent a : agents) {
			a.release();
			this.agents.put(a.getSerialNumber(), a);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for(String s : serials) {
			if(agents.containsKey(s))
				agents.get(s).release();
		}
	}

	/**if
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {
		Thread.sleep(time*100);
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) throws InterruptedException {
		Collections.sort(serials);

		for(String s : serials)
			if (!agents.containsKey(s))
				return false;

		for(String s : serials)
			agents.get(s).acquire();

		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		List<String> agentsNames = new LinkedList<>();
		for(String s : serials) {
			agentsNames.add(agents.get(s).getName());
		}

		return agentsNames;
    }

}
