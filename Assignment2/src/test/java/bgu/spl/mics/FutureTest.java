package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<Integer> f1;
    Future<String> f2;

    @BeforeEach
    public void setUp(){
        f1 = new Future<>();
        f2 = new Future<>();

    }

    @Test
    public void get() throws InterruptedException {
        assertNull(f1.get());
        assertNull(f2.get());

        f1.resolve(100);
        assertEquals(new Integer(100), f1.get());


        f2.resolve("SPLFLIX");
        assertEquals("SPLFLIX", f2.get());
    }

    @Test
    public void resolve() throws InterruptedException {
        f1.resolve(10);
        assertEquals(new Integer(10), f1.get());
    }

    @Test
    public void isDone() {
        assertFalse(f1.isDone());
        f1.resolve(1);
        assertTrue(f1.isDone());
    }

    @Test
    public void time() throws InterruptedException {
        TimeUnit time = TimeUnit.MILLISECONDS;
        long TO = 100;
        get(TO, time);
    }


    @Test
    public void get(long TO, TimeUnit time) throws InterruptedException {

        Object o;
        o = f1.get(23,time);
        assertNull(o);

        f1.resolve(new Integer(7));
        assertEquals(new Integer(7), f1.get(120, time));
    }

}