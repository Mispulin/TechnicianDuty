package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class TechnicianTest {

    private Environment environment = new Environment(50, 50);

    @Test
    public void correctCreation() {
        int step = 0;
        while (step < 100) {
            Technician technician = new Technician("Technician", environment, new Location(0, 0));
            assertTrue("Error, age is too high.", Technician.AGE_MAX >= technician.getAge());
            assertTrue("Error, age is too low.", 1 <= technician.getAge());
            assertTrue(technician.isAvailable());
            step++;
        }
    }

    @Test
    public void isAvailable() {
        Technician technician = new Technician("Technician", environment, new Location(0, 0));
        assertTrue(technician.isAvailable());
    }

    @Test
    public void isNotAvailable() {
        Technician technician = new Technician("Technician", environment, new Location(0, 0));
        technician.assign(new Computer("Computer", environment, new Location(1, 1)));
        assertFalse(technician.isAvailable());
    }

}