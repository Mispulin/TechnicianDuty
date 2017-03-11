package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Technician extends Entity {

    private static final Random rand = Randomizer.getRandom();
    private static final int AGE_MAX = 45;
    private static final double REPAIR_PROBABILITY = 0.4;

    private int age;
    private boolean available;
    private Location home;
    private Computer assignment;
    private List<LogMessage> log;

    public Technician(String name, Environment environment, Location home) {
        super(name, environment, home);
        age = rand.nextInt(AGE_MAX - 1) + 1;
        available = true;
        log = new ArrayList<LogMessage>();
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

    public void log(LogMessage log) {
        this.log.add(log);
        System.out.println(log.toString());
    }

    public void readLog() {
        log.forEach(message -> System.out.println(message.toString()));
    }

    public void act() {
        ageUp();
        if (isAlive()) {
            if (isAvailable()) {
                goHome();
            } else {
                goWork();
                doWork();
            }
        }
    }

    private void ageUp() {
        age++;
        if (age > AGE_MAX) {
            report("I'm too old for this - bye!");
            die();
        }
    }

    public void assign(Computer computer) {
        assignment = computer;
        report("Got an assignment, bummer...");
    }

    private void report(String message) {
        log.add(new LogMessage(this, message));
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
            report("Arrived finally.");
        }
    }

    private void doWork() {
        if (assignment.isRepairable()) {
            report("Oh, I can repair this.");
            assignment.repair();
            report("Succesfully repaired.");
        } else {
            report("Terrible condition - I have to replace it.");
            assignment.replace();
            report("Good as new.. well, it is new!");
        }
        report("Now I'm free.");
        available = true;
        assignment = null;
    }

    private void goHome() {
        report("Nothing to do - going home.");
        go(home);
    }

    private void goWork() {
        report("Work to do! Gotta get there quickly!");
        go(assignment.getLocation());
    }

    private boolean isAt(Location location) {
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
}
