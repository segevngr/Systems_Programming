package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;
import javafx.util.Pair;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private int currTick = 0;

	public Moneypenny(String name) {
		super(name);
		this.setSerialNumber(Integer.parseInt(name.substring(name.indexOf(' ')+1)));
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (tickB) -> {
			this.currTick = tickB.getCurrTime();
		});

		subscribeBroadcast(TerminateBroadcast.class, (terminateB) -> {
			terminate();
		});


		if (this.getSerialNumber() % 2 == 0) {

			subscribeEvent(AgentsAvailableEvent.class, (event) -> {
				List<String> serials = event.getAgentsAvailabeSerials();

				if(Squad.getInstance().getAgents(serials)){		// Trying to acquire Agents
					// Report: Moneypenny serial number | List of available agents
					Pair<Integer,List<String>> report = new Pair(getSerialNumber(), Squad.getInstance().getAgentsNames(serials));
					MessageBrokerImpl.getInstance().complete(event, report);
				}
				else {
					System.out.println("Mission aborted cause Agent do not exist.");
					MessageBrokerImpl.getInstance().complete(event, null);
				}
			});

		} else {
			subscribeEvent(SendAgentsEvent.class, (event) -> {
				List<String> serials = event.getSentAgentsSerials();
				Squad.getInstance().sendAgents(serials, event.getDuration());
				MessageBrokerImpl.getInstance().complete(event, true);
			});

			subscribeEvent(ReleaseAgentsEvent.class, (event) -> {
				Squad.getInstance().releaseAgents(event.getReleaseAgentsSerials());
				MessageBrokerImpl.getInstance().complete(event, true);
			});
		}


	}
}
