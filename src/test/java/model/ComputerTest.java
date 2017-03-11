package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class ComputerTest {

    private Environment environment = new Environment(50, 50);

    @Test
    public void correctCreation() {
        int step = 0;
        while (step < 100) {
            Computer computer = new Computer("Computer", environment, new Location(0, 0));
            assertTrue("Error, priority is too high.", Computer.PRIORITY_MAX >= computer.getPriority());
            assertTrue("Error, priority is too low.",  0 <= computer.getPriority());
            assertTrue("Error, age is too high.", Computer.AGE_MAX >= computer.getAge());
            assertTrue("Error, age is too low.",  1 <= computer.getAge());
            step++;
        }
    }

    @Test
    public void isRepairable() {
        Computer computer = new Computer("Computer", environment, new Location(0, 0));
        computer.setAge(2);
        assertTrue(computer.isRepairable());
    }

    @Test
    public void isNotRepairable() {
        Computer computer = new Computer("Computer", environment, new Location(0, 0));
        computer.setAge(Computer.AGE_MAX - Computer.RETIREMENT + 1);
        assertFalse(computer.isRepairable());
    }

    @Test
    public void isRepaired() {
        Computer computer = new Computer("Computer", environment, new Location(0, 0));
        computer.breakIt();
        computer.repair();
        assertTrue(computer.getWorking());
    }

    @Test
    public void isReplaced() {
        Computer computer = new Computer("Computer", environment, new Location(0, 0));
        computer.breakIt();
        computer.replace();
        assertTrue(computer.getWorking());
    }

    @Test
    public void isNotWorking() {
        Computer computer = new Computer("Computer", environment, new Location(0, 0));
        computer.breakIt();
        assertFalse(computer.getWorking());
    }
}