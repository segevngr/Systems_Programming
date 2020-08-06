package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    private Squad squad;
    private Agent[] agents;
    private List<String> Serials;

    @BeforeEach
    public void setUp(){
        try {
            squad = Squad.getInstance();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        agents = new Agent[4];
        for (int i = 0; i < 4; i++)
            agents[i] = new Agent("agent"+i, "00"+i);
        squad.load(agents);
        Serials = new ArrayList<>();
    }

    @Test
    public void LoadTest(){
        Serials.add("000");
        Serials.add("001");
        Serials.add("002");
        Serials.add("003");
        List<String> AgentsNames = squad.getAgentsNames(Serials);
        assertTrue(AgentsNames.contains("agent1"));
        assertTrue(AgentsNames.contains("agent2"));
        assertTrue(AgentsNames.contains("agent3"));
        assertTrue(AgentsNames.contains("agent4"));
    }

    @Test
    public void ReleaseAgentsTest(){
        Serials.add("001");
        Serials.add("003");
        squad.releaseAgents(Serials);
        List<String> AgentsName = squad.getAgentsNames(Serials);
        assertFalse(AgentsName.contains("agent2"));
        assertFalse(AgentsName.contains("agent4"));
    }

    @Test
    public void GetAgentsTest() throws InterruptedException {
        Serials.add("0005");
        assertFalse(squad.getAgents(Serials));
        Serials.remove("0005");
        Serials.add("0006");
        Serials.add("0007");
        assertTrue(squad.getAgents(Serials));
    }

    @Test
    public void GetAgentsNamesTest(){
        Serials.add("000");
        Serials.add("003");
        List<String> names = squad.getAgentsNames(Serials);
        assertTrue(names.contains("agent1"));
        assertTrue(names.contains("agent4"));
        assertEquals(2,names.size());
    }
}