package app;

import model.Environment;
import model.Location;
import model.Randomizer;
import model.entity.Computer;
import model.entity.Entity;
import model.entity.Server;
import model.entity.Technician;

import java.util.*;
import java.util.stream.Collectors;

public class Simulator {

    private static final int DEFAULT_SIZE = 15;
    private static int size = DEFAULT_SIZE;
    private static boolean LOG = true;

    private int countServers = 1;
    private int countTechnicians = 2;
    private int countComputers = 3;
    private int steps = 10;
    private int step = 0;

    private Environment environment = new Environment(size, size);
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> retired = new ArrayList<>();

    public Simulator() {
        this(DEFAULT_SIZE, LOG);
    }

    public Simulator(boolean report) {
        this(DEFAULT_SIZE, report);
    }

    public Simulator(int size, boolean report) {
        if (size > 0) {
            this.size = size;
        }
        LOG = report;
        environment = new Environment(size, size);
        setup();
    }

    public Simulator(int serv, int tech, int comp, boolean report) {
        countTechnicians = tech;
        countComputers = comp;
        countServers = serv;
        LOG = report;
        setup();
    }

    public Simulator(int size, int serv, int tech, int comp, boolean report) {
        countTechnicians = tech;
        countComputers = comp;
        countServers = serv;
        LOG = report;
        environment = new Environment(size, size);
        setup();
    }

    public void runLongSimulation() {
        simulate(100);
    }

    public void simulate(int numSteps) {
        for(int step = 1; step <= numSteps; step++) {
            simulateOneStep();
            if (LOG) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Entity> sortEntitiesByType() {
        List<Server> servers = new ArrayList<>();
        List<Technician> technicians = new ArrayList<>();
        List<Computer> computers = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof Server) servers.add((Server) entity);
            if (entity instanceof Technician) technicians.add((Technician) entity);
            if (entity instanceof Computer) computers.add((Computer) entity);
        }

        List<Entity> combine = new ArrayList<>();
        combine.addAll(servers);
        combine.addAll(technicians);
        combine.addAll(computers);

        return combine;
    }

    public void simulateOneStep() {
        addStep();
        if (LOG) System.out.println("\nStep " + step + "\n");
        List<Entity> newEntities = new ArrayList<>();
        entities = sortEntitiesByType();
        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            entity.act(newEntities);
            if(! entity.isAlive()) {
                retired.add(entity);
                it.remove();
            }
        }
        entities.addAll(newEntities);
    }

    public static boolean isLOG() {
        return LOG;
    }

    public int getCountServers() {
        return countServers;
    }

    public int getCountTechnicians() {
        return countTechnicians;
    }

    public int getCountComputers() {
        return countComputers;
    }

    public int getSteps() {
        return steps;
    }

    public int getStep() {
        return step;
    }

    private void addStep() {
        step++;
    }

    public void setup() {
        step = 0;
        entities.clear();
        populate();
    }

    private void populate() {
        Random rand = Randomizer.getRandom();
        environment.clear();
        List<Server> servers = new ArrayList<>();
        List<Location> usedLocations = new ArrayList<>();
        int row, col;

        for (int i = 0; i < countServers; i++) {
            do {
                row = rand.nextInt(size - 5) + 5;
                col = rand.nextInt(size - 1) + 1;

            } while (usedLocations.contains(new Location(row, col)));
            Server server = new Server(environment, new Location(row, col), LOG);
            entities.add(server);
            servers.add(server);
            usedLocations.add(new Location(row, col));
            if (LOG) server.print();
        }

        for (int i = 0; i < countTechnicians; i++) {
            Server boss = servers.get(rand.nextInt(countServers ));
            do {
                row = rand.nextInt(size - 5) + 5;
                col = rand.nextInt(size - 1) + 1;

            } while (usedLocations.contains(new Location(row, col)));
            Technician technician = new Technician(environment, new Location(row, col), boss, LOG);
            entities.add(technician);
            usedLocations.add(new Location(row, col));
            if (LOG) technician.print();
        }

        int i = 0;
        while (i < countComputers) {
            i++;
            do {
                row = rand.nextInt(size - 5) + 5;
                col = rand.nextInt(size - 1) + 1;

            } while (usedLocations.contains(new Location(row, col)));
            Server boss = servers.get(rand.nextInt(countServers ));
            Computer computer = new Computer(environment, new Location(row, col), boss, LOG);
            entities.add(computer);
            usedLocations.add(new Location(row, col));
            if (LOG) computer.print();
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getRetired() {
        return retired;
    }

    public void print(List<Entity> entities) {
        entities.forEach(Entity::print);
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
