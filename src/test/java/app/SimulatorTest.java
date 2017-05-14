package app;

import model.AssignmentItem;
import model.Randomizer;
import model.entity.Computer;
import model.entity.Entity;
import model.entity.Technician;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class SimulatorTest {

    private static boolean LOG;

    @Before
    public void init() {
        LOG = false;
    }

    @Test
    public void isPopulated() {
        int countServers = 1;
        int countTechnicians = 2;
        int countComputers = 4;
        Simulator simulator = new Simulator(countServers, countTechnicians, countComputers);
        simulator.simulateOneStep();
        if (LOG) simulator.print(simulator.getEntities());
        assertTrue(simulator.getEntities().size() == (countServers + countTechnicians + countComputers));
    }

    @Test
    public void twoSteps() {
        Simulator simulator = new Simulator(false);
        simulator.simulate(2);
    }

    @Test
    public void fiveSteps() {
        Simulator simulator = new Simulator(false);
        simulator.simulate(5);
    }

    @Test
    public void tenSteps() {
        Simulator simulator = new Simulator(false);
        simulator.simulate(10);
    }

    @Test
    public void longSimulation() {
        Simulator simulator = new Simulator(1, 2, 6);
        simulator.runLongSimulation();
    }

    @Test
    public void longerSimulation() {
        Simulator simulator = new Simulator(1, 2, 3);
        simulator.simulate(1000);
    }

    @Test
    public void techniciansHistory() {
        Simulator simulator = new Simulator(false);
        simulator.simulate(20);
        List<Technician> technicians = simulator.getTechnicians();
        Random rand = Randomizer.getRandom();
        Technician technician = technicians.get(rand.nextInt(technicians.size()));
        if (LOG) technician.readLog();
    }

    @Test
    public void retiredNotEmpty() {
        Simulator simulator = new Simulator(false);
        simulator.runLongSimulation();
        assertFalse(simulator.getRetired().isEmpty());
        if (LOG) simulator.print(simulator.getRetired());
    }

    @Test
    public void sortEntities() {
        Simulator simulator = new Simulator(false);
        simulator.simulate(50);
        List<Entity> current = simulator.getEntities();
        if (LOG) current.forEach(entity -> entity.print());
        if (LOG) System.out.println();
        List<Entity> sorted = simulator.sortEntitiesByType();
        if (LOG) sorted.forEach(entity -> entity.print());
    }

    @Test
    public void correctCounts() {
        Simulator simulator = new Simulator(2, 10, 20);
        simulator.simulate(200);
/*
        System.out.println(simulator.getServers().size());
        System.out.println(simulator.getTechnicians().size());
        System.out.println(simulator.getComputers().size());
*/
        assertTrue(simulator.getEntities().size() == (simulator.getCountServers() + simulator.getCountTechnicians() + simulator.getCountComputers()));
        assertTrue(simulator.getServers().size() == simulator.getCountServers());
        assertTrue(simulator.getTechnicians().size() == simulator.getCountTechnicians());
        assertTrue(simulator.getComputers().size() == simulator.getCountComputers());
    }

    @Test
    public void printCurrentTasks() {
        Simulator simulator = new Simulator(1, 3, 7);
        simulator.simulate(500);
        System.out.println();
        if (LOG) simulator.getServers().get(0).printCurrentTasks();
    }

    @Test
    public void correctCurrentTasks() {
        Simulator simulator = new Simulator(1, 2, 6);
        simulator.simulate(50);
        List<AssignmentItem<Computer, Technician>> tasks = simulator.getServers().get(0).getCurrentTasks();
        simulator.getTechnicians().forEach(technician -> {
            boolean listed = false;
            if (!technician.isAvailable()) {
                for (AssignmentItem<Computer, Technician> task : tasks) {
                    if (task.getTechnician() == technician) {
                        listed = true;
                        break;
                    }
                }
                assertTrue(listed);
            } else {
                assertFalse(listed);
            }
        });
    }

    @Test
    public void printField() {
        Simulator simulator = new Simulator(1, 2, 3);
        simulator.simulate(25);
        simulator.getEnvironment().print();
    }

    @Test
    public void printEntities() {
        Simulator simulator = new Simulator(1, 2, 3);
        simulator.simulate(25);
        simulator.print(simulator.getEntities());
    }

}