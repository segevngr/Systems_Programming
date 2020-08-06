package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private int currTick = 0;

	public Q() {
		super("Q");
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TickBroadcast.class, (tickB) -> {
			this.currTick = tickB.getCurrTime();
		});

		subscribeBroadcast(TerminateBroadcast.class, (terminateB) -> {
			terminate();
		});

		subscribeEvent(GadgetAvailableEvent.class, (event)->{
			String gadget = event.getGadget();
			if(Inventory.getInstance().getItem(gadget)) {
				MessageBrokerImpl.getInstance().complete(event, currTick);
			}
			else
				MessageBrokerImpl.getInstance().complete(event, -1);
		});

	}

}
