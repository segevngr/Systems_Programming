package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link //Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private int finalTime;
	public static int currTick = 0;

	public TimeService(int finalTime) {
		super("TimeService");
		this.finalTime = finalTime;
	}

	public int getFinalTime()
	{
		return finalTime;
	}

	@Override
	protected void initialize() throws InterruptedException {
		while (currTick < finalTime) {
			TickBroadcast tickBroadcast = new TickBroadcast(currTick);
			MessageBrokerImpl.getInstance().sendBroadcast(tickBroadcast);
			Thread.sleep(100);
			currTick++;
		}
			TerminateBroadcast terminateB = new TerminateBroadcast();
			MessageBrokerImpl.getInstance().sendBroadcast(terminateB);
		}

	@Override
	public void run() {
		try {
			initialize();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}