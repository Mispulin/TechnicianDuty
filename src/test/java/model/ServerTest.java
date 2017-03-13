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
        Server server = new Server(environment, new Location(0, 0), LOG);
        Technician technician = new Technician(environment, new Location(0, 2), server, LOG);
        assertTrue(server.getTechnicians().size() == 1);
    }


    @Test
    public void getsNotification() {
        Server server = new Server(environment, new Location(0, 0), LOG);
        Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
        computer.addListener(server);
        computer.breakIt();
        assertTrue(server.getAssignments().size() == 1);
    }

    @Test
    public void experiencedTechnicianAssignment() {
        Server server = new Server(environment, new Location(0, 0), LOG);

        Technician technician1 = new Technician(environment, new Location(0, 2), server, LOG);
        technician1.setExperience(35);

        Technician technician2 = new Technician(environment, new Location(0, 2), server, LOG);
        technician2.setExperience(25);

        Technician technician3 = new Technician(environment, new Location(0, 2), server, LOG);
        technician3.setExperience(28);

        Computer computer = new Computer(environment, new Location(0, 2), server, LOG);
        computer.breakIt();
        server.assignWork();

        assertEquals(computer, technician1.getAssignment());
    }

    @Test
    public void priorityComputerAssignment() {
        Server server = new Server(environment, new Location(0, 0), LOG);

        Technician technician = new Technician(environment, new Location(0, 2), server, LOG);

        Computer computer1 = new Computer(environment, new Location(0, 2), server, LOG);
        computer1.setPriority(9);
        computer1.breakIt();
        if (LOG) {
            for (Computer computer : server.getAssignments()) {
                System.out.println(computer.getName() + ' ' + computer.getPriority());
            }
            System.out.println();
        }
        Computer computer2 = new Computer(environment, new Location(0, 2), server, LOG);
        computer2.setPriority(10);
        computer2.breakIt();
        if (LOG) {
            for (Computer computer : server.getAssignments()) {
                System.out.println(computer.getName() + ' ' + computer.getPriority());
            }
            System.out.println();
        }
        Computer computer3 = new Computer(environment, new Location(0, 2), server, LOG);
        computer3.setPriority(8);
        computer3.breakIt();
        if (LOG) {
            for (Computer computer : server.getAssignments()) {
                System.out.println(computer.getName() + ' ' + computer.getPriority());
            }
            System.out.println();
        }
        server.assignWork();

        assertEquals(computer2, technician.getAssignment());
    }

    @Test
    public void assignToFreeTechnician() {
        Server server = new Server(environment, new Location(0, 0), LOG);

        Technician technician1 = new Technician(environment, new Location(0, 2), server, LOG);
        technician1.setExperience(35);
        technician1.setAvailable(false);

        Technician technician2 = new Technician(environment, new Location(0, 4), server, LOG);
        technician2.setExperience(31);
        technician2.setAvailable(true);

        Technician technician3 = new Technician(environment, new Location(0, 5), server, LOG);
        technician3.setExperience(13);
        technician3.setAvailable(true);

        Technician technician4 = new Technician(environment, new Location(0, 8), server, LOG);
        technician4.setExperience(5);
        technician4.setAvailable(true);

        Computer computer1 = new Computer(environment, new Location(0, 2), server, LOG);
        computer1.setPriority(9);
        computer1.breakIt();

        server.assignWork();

        assertEquals(false, technician2.isAvailable());
    }

}