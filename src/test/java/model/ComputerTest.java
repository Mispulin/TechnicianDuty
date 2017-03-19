package model;

import model.entity.Computer;
import model.entity.Entity;
import model.entity.Server;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class ComputerTest {

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
            Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
            assertTrue("Error, priority is too high.", Computer.PRIORITY_MAX >= computer.getPriority());
            assertTrue("Error, priority is too low.",  0 <= computer.getPriority());
            assertTrue("Error, age is too high.", Computer.AGE_MAX >= computer.getAge());
            assertTrue("Error, age is too low.",  1 <= computer.getAge());
            step++;
        }
    }

    @Test
    public void isRepairable() {
        Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
        computer.setAge(2);
        assertTrue(computer.isRepairable());
    }

    @Test
    public void isNotRepairable() {
        Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
        computer.setAge(Computer.AGE_MAX - Computer.RETIREMENT + 1);
        assertFalse(computer.isRepairable());
    }

    @Test
    public void isRepaired() {
        Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
        computer.breakIt();
        computer.repair();
        assertTrue(computer.getWorking());
    }

    @Test
    public void isReplaced() {
        Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
        List<Entity> entities = new ArrayList<>();
        entities.add(computer);

        List<Entity> newEntities = new ArrayList<>();
        computer.replace(newEntities);
        entities.addAll(newEntities);
        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            if(! entity.isAlive()) {
                it.remove();
            }
        }
        if (LOG) entities.forEach(entity -> entity.print());
        assertTrue(!entities.contains(computer));
    }

    @Test
    public void areReplaced() {
        Computer computer1 = new Computer(environment, new Location(0, 0), server, LOG);
        Computer computer2 = new Computer(environment, new Location(0, 1), server, LOG);
        Computer computer3 = new Computer(environment, new Location(0, 2), server, LOG);
        List<Entity> entities = new ArrayList<>();
        entities.add(computer1);
        entities.add(computer2);
        entities.add(computer3);

        List<Entity> newEntities = new ArrayList<>();
        computer1.replace(newEntities);
        computer2.replace(newEntities);
        computer3.replace(newEntities);
        entities.addAll(newEntities);
        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            if(! entity.isAlive()) {
                it.remove();
            }
        }
        if (LOG) entities.forEach(entity -> entity.print());
        assertTrue(!entities.contains(computer1));
        assertTrue(!entities.contains(computer2));
        assertTrue(!entities.contains(computer3));

    }
    @Test
    public void isNotWorking() {
        Computer computer = new Computer(environment, new Location(0, 0), server, LOG);
        computer.breakIt();
        assertFalse(computer.getWorking());
    }

    @Test
    public void correctOrder() {
        List<Computer> computers = new ArrayList<>();

        Computer computer1 = new Computer(environment, new Location(0, 2), server, LOG);
        computer1.setPriority(7);
        computers.add(computer1);

        Computer computer2 = new Computer(environment, new Location(0, 2), server, LOG);
        computer2.setPriority(4);
        computers.add(computer2);

        Computer computer3 = new Computer(environment, new Location(0, 2), server, LOG);
        computer3.setPriority(2);
        computers.add(computer3);

        Collections.sort(computers);

        assertTrue(computers.get(0).equals(computer1));
        assertTrue(computers.get(1).equals(computer2));
        assertTrue(computers.get(2).equals(computer3));
    }
}