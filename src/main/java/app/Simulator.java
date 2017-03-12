package app;

import model.*;
import model.entity.Computer;
import model.entity.Entity;
import model.entity.Server;
import model.entity.Technician;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Simulator {

    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 20;

    public static int countTechnicians = 3;
    public static int countComputers = 6;
    public static int countServers = 1;

    public static boolean LOG;

    private List<Entity> entities;
    private Environment environment;
    private int step = 0;


    public Simulator() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH, false);
    }

    public Simulator(boolean report) {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH, report);
    }

    public Simulator(int height, int width, boolean report) {
        if(width <= 0 || height <= 0) {
            height = DEFAULT_HEIGHT;
            width = DEFAULT_WIDTH;
        }
        LOG = report;

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
        if (LOG) System.out.println("\nStep " + step + "\n");
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
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < countServers; i++) {
            Server server = new Server("Crash server", environment, new Location(0, i), LOG);
            entities.add(server);
            servers.add(server);
            // server.print();
        }

        for (int i = 0; i < countTechnicians; i++) {
            Server boss = servers.get(rand.nextInt(countServers ));
            Technician technician = new Technician("Technician " + (i + 1), environment, new Location(2, i * 2), boss, LOG);
            entities.add(technician);
            // technician.print();
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
            Server boss = servers.get(rand.nextInt(countServers ));
            Computer computer = new Computer("Computer " + i, environment, new Location(row, col), boss, LOG);
            entities.add(computer);
            used.add(new Location(row, col));
            // computer.print();
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Technician> getTechnicians() {
        List<Technician> technicians = entities.stream().filter(entity -> entity instanceof Technician).map(entity -> (Technician) entity).collect(Collectors.toList());
        return technicians;
    }

    public List<Computer> getComputers() {
        List<Computer> computers = entities.stream().filter(entity -> entity instanceof Computer).map(entity -> (Computer) entity).collect(Collectors.toList());
        return computers;
    }

    public List<Server> getServers() {
        List<Server> servers = entities.stream().filter(entity -> entity instanceof Server).map(entity -> (Server) entity).collect(Collectors.toList());
        return servers;
    }
}
