package app;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class SimulatorTest {

    @Test
    public void isPopulated() {
        Simulator simulator = new Simulator();
        simulator.simulateOneStep();
        assertTrue(simulator.getEntities().size() == (Simulator.countComputers + Simulator.countTechnicians));
    }

}