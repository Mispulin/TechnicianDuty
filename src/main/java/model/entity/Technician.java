package model.entity;

import model.Counter;
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
    public static final int EXP_MAX = 45;

    private int experience;
    private boolean available = true;
    private Location place;
    private Computer assignment = null;
    private List<WorkLog> workLogs = new ArrayList<>();
    private Server boss;

    public Technician(Environment environment, Location place, Server boss, boolean reportSelf) {
        super("Technician " + Counter.technician, environment, place, reportSelf);
        Counter.addTechnician();
        experience = rand.nextInt(EXP_MAX - 1) + 1;
        this.place = place;
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
        workLogs.forEach(log -> log.print());
    }

    public void act(List<Entity> entities) {
        ageUp(entities);
        if (isAlive()) {
            if (isAvailable()) {
                goHome();
            } else {
                goWork(entities);
            }
        }
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }

    private void ageUp(List<Entity> entities) {
        experience++;
        if (experience >= EXP_MAX) {
            retire(entities);
        }
    }

    private void findReplacement(List<Entity> entities) {
        Technician replacement = new Technician(getEnvironment(), getLocation(), boss, getReportSelf());
        entities.add(replacement);
        report(String.format("Technician %d is now my replacement.", Counter.technician - 1));
    }

    public void assign(Computer computer) {
        boss.assignedNotification(this, computer);
        computer.assign();
        assignment = computer;
        available = false;
        report(String.format("Got an assignment, bummer... Now at %s and gotta go to %s.", getLocation(), assignment.getLocation()));
    }

    private void retire(List<Entity> entities) {
        if (!available) {
            giveUp();
        }
        findReplacement(entities);
        report("I'm too old for this - bye!");
        boss.retireTechnician(this);
        die();
    }

    public void giveUp() {
        report("Gotta give up my assignment...");
        available = true;
        boss.giveUpNotification(assignment);
        assignment = null;
    }

    public void report(String message) {
        WorkLog workLog = new WorkLog(this, message);
        workLogs.add(workLog);
    }

    private void go(Location location) {
        if (!isAt(location)) {
            /*
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
            */
            takeShortcut(location);
        } else {
            report("I'm here.");
        }
    }

    private void free() {
        available = true;
        assignment = null;
    }

    private void doWork(List<Entity> entities) {
        if (assignment.isRepairable()) {
            report("Oh, I can repair this.");
            if (rand.nextDouble() <= ( (double) 1 / EXP_MAX * getExperience() )) {
                assignment.repair();
                report("Succesfully repaired.");
                free();
            } else {
                report("Well, I misjudged the situation, I will need more time.");
            }
        } else if(!assignment.isRepairable()) {
            report("Terrible condition - I have to replace it.");
            report("Good as new.. well, it is new!");
            assignment.replace(entities);
            free();
            report("I'm free.");
        }
    }

    private void goHome() {
        if (!isAt(place)) {
            report("Nothing to do - going to my place.");
            go(place);
        } else {
            report("Nothing to do - staying at my place.");
        }
    }

    private void goWork(List<Entity> entities) {
        if (!isAt(assignment.getLocation())) {
            report("Work to do! Gotta get there quickly!");
            go(assignment.getLocation());
        } else {
            report("At my assignment, so I better get to it.");
            doWork(entities);
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

    private void takeShortcut(Location location) {
        Location getTo = place;
        if (!location.equals(getTo)) {
            getTo = new Location(location.getRow() - 1, location.getCol());
        }
        setLocation(getTo);
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
        System.out.println(String.format("%-15s exp: %-5d %s, %s", getName(), getExperience(), getLocation().toString(), boss.getName()));
    }

    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Technician) {
            Technician tech = (Technician) obj;
            return Integer.compare(tech.getExperience(), getExperience());
        }
        else {
            return -1;
        }
    }
}
