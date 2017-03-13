package app;

import model.Randomizer;
import model.entity.Entity;
import model.entity.Technician;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class SimulatorTest {

    @Test
    public void isPopulated() {
        int countServers = 1;
        int countTechnicians = 2;
        int countComputers = 4;
        Simulator simulator = new Simulator(20, countServers, countTechnicians, countComputers, false);
        simulator.simulateOneStep();
        simulator.print(simulator.getEntities());
        assertTrue(simulator.getEntities().size() == (countServers + countTechnicians + countComputers));
    }

    @Test
    public void twoSteps() {
        Simulator simulator = new Simulator();
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
        Simulator simulator = new Simulator(1, 2, 6, true);
        simulator.runLongSimulation();
    }

    @Test
    public void longerSimulation() {
        Simulator simulator = new Simulator(1, 2, 3, false);
        simulator.simulate(1000);
    }

    @Test
    public void techniciansHistory() {
        Simulator simulator = new Simulator();
        simulator.simulate(5);
        List<Technician> technicians = simulator.getTechnicians();
        Random rand = Randomizer.getRandom();
        Technician technician = technicians.get(rand.nextInt(technicians.size()));
        technician.readLog();
    }

    @Test
    public void retiredNotEmpty() {
        Simulator simulator = new Simulator(false);
        simulator.runLongSimulation();
        assertFalse(simulator.getRetired().isEmpty());
        simulator.print(simulator.getRetired());
    }

    @Test
    public void sortEntities() {
        Simulator simulator = new Simulator(false);
        simulator.simulate(50);
        List<Entity> current = simulator.getEntities();
        current.forEach(entity -> entity.print());
        System.out.println();
        List<Entity> sorted = simulator.sortEntitiesByType();
        sorted.forEach(entity -> entity.print());
    }
}