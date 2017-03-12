package model.entity;

import model.Environment;
import model.Location;
import model.Randomizer;
import model.log.WorkLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Technician extends Entity implements Comparable {

    private static final Random rand = Randomizer.getRandom();
    public static final int AGE_MAX = 45;
    private static final double REPAIR_PROBABILITY = 0.3;

    private int age;
    private boolean available = true;
    private Location home;
    private Computer assignment = null;
    private List<WorkLog> workLogs = new ArrayList<>();
    private ServerListener boss;

    public Technician(String name, Environment environment, Location home, ServerListener boss, boolean reportSelf) {
        super(name, environment, home, reportSelf);
        age = rand.nextInt(AGE_MAX - 1) + 1;
        this.home = home;
        boss.addTechnician(this);
        this.boss = boss;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public Computer getAssignment() {
        return assignment;
    }

    public void readLog() {
        System.out.println("Reading logs.");
        workLogs.forEach(log -> log.print());
    }

    public void act(List<Entity> entities) {
        ageUp(entities);
        if (isAlive()) {
            if (isAvailable()) {
                goHome();
            } else {
                goWork();
            }
        }
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    private void ageUp(List<Entity> entities) {
        age++;
        if (age > AGE_MAX) {
            report("I'm too old for this - bye!");
            findReplacement(entities);
            die();
        }
    }

    private void findReplacement(List<Entity> entities) {
        Environment environment = getEnvironment();
        Location place = getLocation();
        int number = Integer.valueOf(getName().replaceAll("\\D+","")) + 1;
        number = Integer.valueOf(getName().substring(getName().lastIndexOf(" ") + 1)) + 1;
        Technician replacement = new Technician("Technician " + number, getEnvironment(), place, boss, getReportSelf());
        entities.add(replacement);
    }

    public void assign(Computer computer) {
        computer.assign();
        assignment = computer;
        available = false;
        report("Got an assignment, bummer...");
    }

    public void report(String message) {
        WorkLog workLog = new WorkLog(this, message);
        workLogs.add(workLog);
    }

    private void go(Location location) {
        if (!isAt(location)) {
            if ((getLocation().getRow() == location.getRow())) {
                if (getLocation().getCol() < location.getCol()) {
                    goRight();
                } else {
                    goLeft();
                }
            } else if ((getLocation().getCol() == location.getCol())) {
                if (getLocation().getRow() < location.getRow()) {
                    goUp();
                } else {
                    goDown();
                }
            } else {
                // not the same row or column so pick the lower distance and then the higher
                Location diff = getLocation().diff(location);

            }
        } else {
            report("I'm here.");
        }
    }

    private void doWork() {
        if (assignment.isRepairable()) {
            report("Oh, I can repair this.");
            if (rand.nextDouble() <= REPAIR_PROBABILITY) {
                assignment.repair();
                report("Succesfully repaired.");
                available = true;
                assignment = null;
            } else {
                report("Well, I misjudged the situation, I will need more time.");
            }
        } else if(!assignment.isRepairable()) {
            report("Terrible condition - I have to replace it.");
            assignment.replace();
            report("Good as new.. well, it is new!");
        } else {
            report("I'm free.");
        }
    }

    private void goHome() {
        if (!isAt(home)) {
            report("Nothing to do - going to my place.");
            go(home);
        } else {
            report("Nothing to do - staying at my place.");
        }
    }

    private void goWork() {
        if (!isAt(assignment.getLocation())) {
            report("Work to do! Gotta get there quickly!");
            go(assignment.getLocation());
        } else {
            report("Finally at my assignment, so I better get to it.");
            doWork();
        }
    }

    private boolean isAt(Location location) {
        if (location.equals(getLocation())) {
            return true;
        }
        boolean above = location.getRow() - 1 == getLocation().getRow();
        boolean under = location.getRow() + 1 == getLocation().getRow();
        boolean left = location.getCol() - 1 == getLocation().getCol();
        boolean right = location.getCol() + 1 == getLocation().getCol();
        boolean horizontal = (left || right) && (location.getRow() == getLocation().getRow());
        boolean vertical = (above || under) && (location.getCol() == getLocation().getCol());
        return (horizontal || vertical);
    }

    private boolean hasObstacle(Location location) {
        return getEnvironment().getEntityAt(location) != null;
    }

    private void goUp() {
        Location toGo = new Location(getLocation().getRow() - 1, getLocation().getCol());
        setLocation(toGo);
    }

    private void goRight() {
        Location toGo = new Location(getLocation().getRow(), getLocation().getCol() + 1);
        setLocation(toGo);
    }

    private void goDown() {
        Location toGo = new Location(getLocation().getRow() + 1, getLocation().getCol());
        setLocation(toGo);
    }

    private void goLeft() {
        Location toGo = new Location(getLocation().getRow(), getLocation().getCol() - 1);
        setLocation(toGo);
    }

    public void print() {
        System.out.println(String.format("%s, stáří: %-5d %s", getName(), getAge(), getLocation().toString()));
    }

    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Technician) {
            Technician tech = (Technician) obj;
            return Integer.compare(tech.getAge(), getAge());
        }
        else {
            return -1;
        }
    }
}
