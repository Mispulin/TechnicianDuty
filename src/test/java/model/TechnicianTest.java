package model;

import model.entity.Computer;
import model.entity.Server;
import model.entity.Technician;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class TechnicianTest {


    private Environment environment;
    private Server server;
    private static boolean LOG;

    @Before
    public void init() {
        environment = new Environment(50, 50);
        server = new Server(environment, new Location(0, 0), LOG);
        LOG = false;
    }

    @Test
    public void correctCreation() {
        int step = 0;
        while (step < 100) {
            Technician technician = new Technician(environment, new Location(0, 2), server, LOG);
            assertTrue("Error, age is too high.", Technician.AGE_MAX >= technician.getExperience());
            assertTrue("Error, age is too low.", 1 <= technician.getExperience());
            assertTrue(technician.isAvailable());
            step++;
        }
    }

    @Test
    public void isAvailable() {
        Technician technician = new Technician(environment, new Location(0, 2), server, LOG);
        assertTrue(technician.isAvailable());
    }

    @Test
    public void isNotAvailable() {
        Technician technician = new Technician(environment, new Location(0, 2), server, LOG);
        technician.assign(new Computer(environment, new Location(1, 1), server, LOG));
        assertFalse(technician.isAvailable());
    }

    @Test
    public void correctOrder() {
        List<Technician> technicians = new ArrayList<>();

        Technician technician1 = new Technician(environment, new Location(0, 2), server, LOG);
        technician1.setExperience(17);
        technicians.add(technician1);

        Technician technician2 = new Technician(environment, new Location(0, 2), server, LOG);
        technician2.setExperience(5);
        technicians.add(technician2);

        Technician technician3 = new Technician(environment, new Location(0, 2), server, LOG);
        technician3.setExperience(15);
        technicians.add(technician3);

        Collections.sort(technicians);

        assertTrue(technicians.get(0).equals(technician1));
        assertTrue(technicians.get(1).equals(technician3));
        assertTrue(technicians.get(2).equals(technician2));
    }

}