package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import com.google.gson.Gson;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.application.subscribers.Moneypenny;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static TimeService time;
    private static int m;
    private static int moneypenny;
    private static ArrayList<M> MList;
    private static ArrayList<Moneypenny> moneypennyList;
    private static ArrayList<Intelligence> intelList;
    private static ArrayList<Thread> threadList;


    public static void main(String[] args) throws IOException, InterruptedException {

        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();
        MList = new ArrayList<>();
        moneypennyList = new ArrayList<>();
        intelList = new ArrayList<>();
        threadList = new ArrayList<>();


        // Start of Json Parser:

        String s = new String(Files.readAllBytes(Paths.get(args[0])));
        JParser parser = new Gson().fromJson(s, JParser.class);

        String[] gadgets = new String[parser.inventory.length];
        Agent[] agents = new Agent[parser.squad.length];

        for (int i = 0; i < parser.inventory.length; i++)
            gadgets[i] = parser.inventory[i];
        inventory.load(gadgets);

        for (int i = 0; i < parser.squad.length; i++)
            agents[i] = new Agent(parser.squad[i].name, parser.squad[i].serialNumber);//
        squad.load(agents);

        for (int i = 0; i < parser.services.intelligence.length; i++) {
            LinkedList<MissionInfo> missions = new LinkedList<>();
            for (int j = 0; j < parser.services.intelligence[i].missions.length; j++)
                missions.add(parser.services.intelligence[i].missions[j]);
            Intelligence intel = new Intelligence(missions, "Intelligence " +Integer.toString(i + 1));//
            intelList.add(intel);
        }

        for (int i = 0; i < parser.services.M; i++) {
            M m = new M("M " +Integer.toString(i + 1));
            MList.add(m);
        }

        for (int i = 0; i < parser.services.Moneypenny; i++) {
            Moneypenny moneypenny = new Moneypenny("Moneypenny " +Integer.toString(i + 1));
            moneypennyList.add(moneypenny);
        }

        time = new TimeService(parser.services.time);
        // End of Json parser


        // Threads:
        for (M m : MList) {
            Thread thread = new Thread(m);
            threadList.add(thread);
        }
        for (Moneypenny money : moneypennyList) {
            Thread thread = new Thread(money);
            threadList.add(thread);
        }
        for (Intelligence i : intelList) {
            Thread thread = new Thread(i);
            threadList.add(thread);
        }
        threadList.add(new Thread(new Q()));
        for (Thread t : threadList) {
            t.start();
        }

        Thread.sleep(200); // Wait for subscribers\publishers to initialize

        threadList.add(new Thread(time));
        threadList.get(threadList.size() - 1).start();
        for (Thread t : threadList)
            t.join();

        Inventory.getInstance().printToFile(args[1]);
        Diary.getInstance().printToFile(args[2]);

    }

}
