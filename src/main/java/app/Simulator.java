package app;

import model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Simulator {

    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;

    public static int countTechnicians = 1;
    public static int countComputers = 6;

    private List<Entity> entities;
    private Environment environment;
    private int step;


    public Simulator() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    public Simulator(int height, int width) {
        if(width <= 0 || height <= 0) {
            height = DEFAULT_HEIGHT;
            width = DEFAULT_WIDTH;
        }

        entities = new ArrayList<>();
        environment = new Environment(height, width);

        reset();
    }

    public void runLongSimulation() {
        simulate(5000);
    }

    public void simulate(int numSteps) {
        for(int step = 1; step <= numSteps; step++) {
            simulateOneStep();
        }
    }

    public void simulateOneStep() {
        step++;

        List<Entity> newEntities = new ArrayList<>();
        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            entity.act(newEntities);
            if(! entity.isAlive()) {
                it.remove();
            }
        }
        entities.addAll(newEntities);
    }

    public void reset() {
        step = 0;
        entities.clear();
        populate();
    }

    private void populate() {
        Random rand = Randomizer.getRandom();
        environment.clear();
        for (int i = 1; i <= countTechnicians; i++) {
            Technician technician = new Technician("Technician " + i, environment, new Location(0, i - 1));
            entities.add(technician);
            technician.print();
        }

        int i = 0;
        List<Location> used = new ArrayList<>();
        while (i < countComputers) {
            i++;
            int row = rand.nextInt(DEFAULT_WIDTH - 5) + 5;
            int col = rand.nextInt(DEFAULT_HEIGHT - 1) + 1;
            if (used.contains(new Location(row, col))) {
                continue;
            }
            Computer computer = new Computer("Computer " + i, environment, new Location(row, col));
            entities.add(computer);
            used.add(new Location(row, col));
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
