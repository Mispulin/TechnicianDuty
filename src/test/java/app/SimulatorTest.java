package app;

import model.Randomizer;
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
        Simulator simulator = new Simulator();
        simulator.simulateOneStep();
        assertTrue(simulator.getEntities().size() == (Simulator.countComputers + Simulator.countTechnicians + Simulator.countServers));
    }

    @Test
    public void twoSteps() {
        Simulator simulator = new Simulator();
        simulator.simulate(2);
    }

    @Test
    public void fiveSteps() {
        Simulator simulator = new Simulator(true);
        simulator.simulate(5);
    }

    @Test
    public void tenSteps() {
        Simulator simulator = new Simulator(true);
        simulator.simulate(10);
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
}