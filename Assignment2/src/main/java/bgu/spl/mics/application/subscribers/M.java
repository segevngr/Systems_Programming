package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import javafx.util.Pair;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	private int currTick = 0;

	public M(String name) {
		super(name);
		this.setSerialNumber(Integer.parseInt(name.substring(name.indexOf(' ')+1)));
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TickBroadcast.class, (tickB) -> {
			currTick = tickB.getCurrTime();
		});

		subscribeBroadcast(TerminateBroadcast.class, (terminateB) -> {
			terminate();
		});

		subscribeEvent(MissionRecievedEvent.class, (event) -> {
			MissionInfo missionInfo = event.getMissionInfo();
			Diary.getInstance().incrementTotal();
			Report report = null;

			// First Moneypenny event: are agents available?
			Future<Pair<Integer, List<String>>> agentsAvailableF = getSimplePublisher().sendEvent(new AgentsAvailableEvent(missionInfo.getSerialAgentsNumbers()));

			if (agentsAvailableF != null && agentsAvailableF.get() != null) { 	// If Moneypenny verified agents are available
				// Q event: is gadget available?
				Future<Integer> gadgetAvailableF = getSimplePublisher().sendEvent(new GadgetAvailableEvent(missionInfo.getGadget()));
				if (gadgetAvailableF != null && gadgetAvailableF.get() != null && gadgetAvailableF.get() != -1) // If Q verified gadget available
				{
					if (missionInfo.getTimeExpired() > currTick)     // Time expired Ok - we tell moneypenny to send the agents
					{
						// Second Moneypenny event: send the agents
						getSimplePublisher().sendEvent(new SendAgentsEvent(missionInfo.getSerialAgentsNumbers(), missionInfo.getDuration()));

						// Report:
						report = new Report();
						report.setMissionName(missionInfo.getMissionName());
						report.setTimeCreated(currTick);
						report.setTimeIssued(missionInfo.getTimeIssued());
						report.setM(getSerialNumber());
						report.setMoneypenny(agentsAvailableF.get().getKey());
						report.setqTime(gadgetAvailableF.get());
						report.setGadget(missionInfo.getGadget());
						report.setAgentsSerials(missionInfo.getSerialAgentsNumbers());
						report.setAgentNames(agentsAvailableF.get().getValue());

					} else { // If time expired - we release agents
						System.out.println("Mission aborted cause time expired. ");
						getSimplePublisher().sendEvent(new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers()));
					}
				} else { // If gadget is missing - we release agents
					System.out.println("Mission aborted cause Gadget is not available. ");
					getSimplePublisher().sendEvent(new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers()));
				}
				getSimplePublisher().sendEvent(new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers()));
			}
			Diary.getInstance().addReport(report);
		});
	}
}
