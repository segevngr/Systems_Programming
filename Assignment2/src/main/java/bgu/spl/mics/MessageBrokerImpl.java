package bgu.spl.mics;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	// Key\Value: Message Type\Queue of subscribers - updated by subscribed (subscribers)
	ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> messagesMap;
	// Key\Value: Subscribers\Queue of messages - updated by sends (publishers)
	ConcurrentHashMap <Subscriber, LinkedBlockingQueue<Message>> subsMap;
	ConcurrentHashMap <Event, Future> futureMap;


	private static MessageBrokerImpl instance = new MessageBrokerImpl();

	// Constructor
	private MessageBrokerImpl() {
		messagesMap = new ConcurrentHashMap();
		subsMap = new ConcurrentHashMap();
		futureMap = new ConcurrentHashMap();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return instance;
	}

	// Subscribes: Subscribes to a message type
	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		subsMap.putIfAbsent(m, new LinkedBlockingQueue<>());
		messagesMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		messagesMap.get(type).add(m);

	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		subsMap.putIfAbsent(m, new LinkedBlockingQueue<>());
		messagesMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		messagesMap.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve(result);
	}

	// Send: Add message to a Queue of subscriber\All subscribers

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		messagesMap.putIfAbsent(b.getClass(), new ConcurrentLinkedQueue<>());
		ConcurrentLinkedQueue<Subscriber> broadCastQ = messagesMap.get(b.getClass());
			for (Subscriber s : broadCastQ) {
				subsMap.get(s).add(b);
			}
		notifyAll();
	}

	@Override
	public synchronized <T> Future<T> sendEvent(Event <T> e) {
		futureMap.putIfAbsent(e, new Future());    // Store event in future map

		if (!messagesMap.get(e.getClass()).isEmpty() && messagesMap.containsKey(e.getClass())) {
			ConcurrentLinkedQueue<Subscriber> eventQ = messagesMap.get(e.getClass());
			Subscriber firstSub = eventQ.remove();    // Take first sub from event type Q
			subsMap.get(firstSub).add(e);         // add the event to the subscriber Q
			eventQ.add(firstSub);                    // Return sub to the end of the event type Q
		}
		else return null;

		notifyAll();
		return futureMap.get(e);

	}

	@Override
	public synchronized void register(Subscriber m) {
		subsMap.putIfAbsent(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {
		// Remove sub from messagesMap (msg type | subs)
		for(Map.Entry<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> entry : messagesMap.entrySet()) {
			synchronized (entry.getKey()) {
				entry.getValue().remove(m);
			}
		}

		// Remove sub from subsMap (sub | messages)
		BlockingQueue<Message> subMsgsToRemove = subsMap.remove(m);;

		// Update removed events Future to null
		if(subMsgsToRemove != null) {
			for (Message msg : subMsgsToRemove){
				if(msg instanceof Event){
					futureMap.get(msg).resolve(null);
				}
			}
		}
	}

	@Override
	public synchronized Message awaitMessage(Subscriber m) throws InterruptedException {
		while(subsMap.get(m).isEmpty()) {
			wait();
		}
		return subsMap.get(m).remove();

	}

	

}
