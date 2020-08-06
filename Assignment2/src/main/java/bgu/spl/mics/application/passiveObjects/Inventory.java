package bgu.spl.mics.application.passiveObjects;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	private static Inventory instance = new Inventory();

	// Constructor
	private Inventory() {
		gadgets = new LinkedList<>();
	}

		/**
         * Retrieves the single instance of this class.
         */
	public static Inventory getInstance() {
		return instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		for(String s : inventory)
			gadgets.add(s);
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public synchronized boolean getItem(String gadget){
		boolean gadgetExists = gadgets.contains(gadget);
		if(gadgetExists)
			gadgets.remove(gadget);
		return gadgetExists;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the gadgets.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) throws IOException {
		JsonArray jsonInvArray = new JsonArray();
		for (String gadget: gadgets)
			jsonInvArray.add(gadget);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(String.valueOf(jsonInvArray));
		String prettyJsonString = gson.toJson(je);

		Files.write(Paths.get(filename), prettyJsonString.getBytes());	}
}
