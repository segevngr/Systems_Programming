package bgu.spl.mics;

import bgu.spl.mics.application.MI6Runner;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InventoryTest {

    private Inventory Invi;
    private String[] Gadgets = {"gadg1","gadg2","gadg3","gadg4","gadg5"};

    @BeforeEach
    public void setUp(){
        try {
            Invi = Inventory.getInstance();
            Invi.load(Gadgets);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void GetItemsTest(){
        assertTrue(Invi.getItem("gadg1"));
        assertFalse(Invi.getItem("gadg1"));
        assertFalse(Invi.getItem("gadg2"));
    }

    @Test
    public void PrintToFileTest() {
            File tempFile = new File("inventoryOutputFile.json");
            boolean exists = tempFile.exists();
            assertTrue(exists);
    }
}