package bgu.spl.mics.application.passiveObjects;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private static Diary instance = new Diary();
	private List<Report> reportList = new LinkedList<>();
	public static AtomicInteger totalReports = new AtomicInteger();

	// Constructor
	private Diary() {
		reportList = new LinkedList();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return instance;
	}

	public List<Report> getReports(){
		return this.reportList;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
		if (reportToAdd != null)
			this.reportList.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) throws IOException {
		JsonArray jsonDiaryArray = new JsonArray();
		for (Report report : reportList) {
			JsonArray jsonReportArray = new JsonArray();
				jsonReportArray.add("mission name: " + report.getMissionName());
				jsonReportArray.add("m: " + report.getM());
				jsonReportArray.add("moneypenny: " + report.getMoneypenny());
				JsonArray jsonSerialsArray = new JsonArray();
				for (String serial : report.getAgentsSerials())
					jsonSerialsArray.add(serial);
				jsonReportArray.add("agentsSerialNumbers: " + jsonSerialsArray);
				JsonArray jsonNamessArray = new JsonArray();
				for (String name : report.getAgentNames())
					jsonNamessArray.add(name);
				jsonReportArray.add("agentsNames: " + jsonNamessArray);
				jsonReportArray.add("gadgetName: " + report.getGadget());
				jsonReportArray.add("timeCreated: " + report.getTimeCreated());
				jsonReportArray.add("timeIssued: " + report.getTimeIssued());
				jsonReportArray.add("qTime: " + report.getqTime());
				jsonDiaryArray.add(jsonReportArray);
			}
			jsonDiaryArray.add("total: " + Diary.getInstance().getTotal());
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(String.valueOf(jsonDiaryArray));
			String prettyJsonString = gson.toJson(je);
			Files.write(Paths.get(filename), prettyJsonString.getBytes());
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){ // how should we check if m is (executed / aborted)? create a status variable in M??
		return totalReports.get();
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		this.totalReports.incrementAndGet();
	}
}