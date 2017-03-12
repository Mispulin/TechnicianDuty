package model;

import model.entity.Computer;
import model.entity.Server;
import model.entity.Technician;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 12. 3. 2017.
 */
public class ServerTest {

    private Environment environment;
    private static boolean LOG;

    @Before
    public void init() {
        environment = new Environment(50, 50);
        LOG = false;
    }


    @Test
    public void getsTechnician() {
        Server server = new Server("Crash server", environment, new Location(0, 0), LOG);
        Technician technician = new Technician("Technician", environment, new Location(0, 2), server, LOG);
        assertTrue(server.getTechnicians().size() == 1);
    }


    @Test
    public void getsNotification() {
        Server server = new Server("Crash server", environment, new Location(0, 0), LOG);
        Computer computer = new Computer("Computer", environment, new Location(0, 0), server, LOG);
        computer.addListener(server);
        computer.breakIt();
        assertTrue(server.getAssignments().size() == 1);
    }

    @Test
    public void correctTechnicianAssignment() {
        Server server = new Server("Crash server", environment, new Location(0, 0), LOG);

        Technician technician1 = new Technician("Technician 1", environment, new Location(0, 2), server, LOG);
        technician1.setAge(35);

        Technician technician2 = new Technician("Technician 2", environment, new Location(0, 2), server, LOG);
        technician2.setAge(25);

        Technician technician3 = new Technician("Technician 3", environment, new Location(0, 2), server, LOG);
        technician3.setAge(28);

        Computer computer = new Computer("Computer 1", environment, new Location(0, 2), server, LOG);
        computer.breakIt();
        server.assignWork();

        assertEquals(computer, technician1.getAssignment());
    }

    @Test
    public void correctComputerAssignment() {
        Server server = new Server("Crash server", environment, new Location(0, 0), LOG);

        Technician technician = new Technician("Technician 1", environment, new Location(0, 2), server, LOG);

        Computer computer1 = new Computer("Computer 1", environment, new Location(0, 2), server, LOG);
        computer1.setPriority(9);
        computer1.breakIt();

        for (Computer computer : server.getAssignments()) {
            System.out.println(computer.getName() + ' ' + computer.getPriority());
        }
        System.out.println();

        Computer computer2 = new Computer("Computer 2", environment, new Location(0, 2), server, LOG);
        computer2.setPriority(10);
        computer2.breakIt();

        for (Computer computer : server.getAssignments()) {
            System.out.println(computer.getName() + ' ' + computer.getPriority());
        }
        System.out.println();

        Computer computer3 = new Computer("Computer 3", environment, new Location(0, 2), server, LOG);
        computer3.setPriority(8);
        computer3.breakIt();

        for (Computer computer : server.getAssignments()) {
            System.out.println(computer.getName() + ' ' + computer.getPriority());
        }
        System.out.println();

        server.assignWork();

        assertEquals(computer2, technician.getAssignment());
    }

}