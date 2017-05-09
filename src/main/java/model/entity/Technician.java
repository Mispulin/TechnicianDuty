package model.entity;

import app.Controller;
import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import model.*;
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
        // I freeze it when on assignment.
        if (available) {
            experience++;
            if (experience >= EXP_MAX) {
                retire(entities);
            }
        }
    }

    private void findReplacement(List<Entity> entities) {
        die();
        Technician replacement = new Technician(getEnvironment(), place, boss, getReportSelf());
        entities.add(replacement);
        report(String.format("Technician %d will be my replacement.", Counter.technician - 1));
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
        report("I'm too old for this - bye!");
        boss.retireTechnician(this);
        findReplacement(entities);
    }

    public void giveUp() {
        report("Gotta give up my assignment...");
        available = true;
        boss.giveUpNotification(this, assignment);
        assignment = null;
    }

    public void report(String message) {
        WorkLog workLog = new WorkLog(this, message);
        workLogs.add(workLog);
    }

    private void go(Location location) {
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

        //takeShortcut(location);

        ;
        List<MyPoint> pointsToTarget = computePointsToTarget(
                this.getLocation().getCol(),
                this.getLocation().getRow(),
                location.getCol(),
                location.getRow()
        );
        setPath(new Path(pointsToTarget));


        if (path.getPath().size() > 0) {
            setLocation(path.getPath().get(1).getY(), path.getPath().get(1).getX());
        } else {
        }


//        List<MyPoint> pointsToTarget = computePointsToTarget(
//                this.getLocation().getCol(),
//                this.getLocation().getRow(),
//                location.getCol(),
//                location.getRow());
//
//        setPath(new Path(pointsToTarget));
//
//        System.out.println("11111111111111111111111111111111111111111111111111> " + pathToComputer.size());
//
//        Location getTo = place;
//        if (!location.equals(getTo)) {
//            int row = pathToComputer.get(1).getY();
//            int col =  pathToComputer.get(1).getX();
//            getTo = new Location(row, col);
//        }
//        setLocation(getTo);
//
////        setLocation(
////                new Double(pathToComputer.get(1).getX()).intValue(),
////                new Double(pathToComputer.get(1).getY()).intValue());


    }

    @Setter
    @Getter
    private Path path = new Path(new ArrayList<MyPoint>());

    List<MyPoint> computePointsToTarget(int x, int y, int x2, int y2) {
        List<MyPoint> points = new ArrayList<>();

        int w = x2 - x;
        int h = y2 - y;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w < 0) dx1 = -1;
        else if (w > 0) dx1 = 1;
        if (h < 0) dy1 = -1;
        else if (h > 0) dy1 = 1;
        if (w < 0) dx2 = -1;
        else if (w > 0) dx2 = 1;
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) dy2 = -1;
            else if (h > 0) dy2 = 1;
            dx2 = 0;
        }
        int numerator = longest >> 1;
        for (int i = 0; i <= longest; i++) {

            points.add(new MyPoint(x, y));
            numerator += shortest;
            if (!(numerator < longest)) {
                numerator -= longest;
                x += dx1;
                y += dy1;
            } else {
                x += dx2;
                y += dy2;
            }
        }
        return points;
    }

    private void free() {
        available = true;
        assignment = null;
        report("I'm free.");
    }

    private void doWork(List<Entity> entities) {
        if (assignment.isRepairable()) {
            report("Oh, I can repair this.");
            if (rand.nextDouble() <= ((double) 1 / EXP_MAX * getExperience())) {
                assignment.repair();
                boss.completedNotification(this);
                free();
            } else {
                report("Well, I misjudged the situation, I will need more time.");
            }
        } else if (!assignment.isRepairable()) {
            report("Terrible condition - I have to replace it.");
            assignment.replace(entities);
            boss.completedNotification(this);
            free();
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

    private void takeShortcut(Location location) {
        Location getTo = place;
        if (!location.equals(getTo)) {
            int row = location.getRow() - 1;
            row = (row >= 0 && row < getEnvironment().getWidth()) ? row : location.getRow() + 1;
            int col = location.getCol();
            getTo = new Location(row, col);
        }
        setLocation(getTo);
    }

    public void print() {
        System.out.println(String.format("%-15s exp: %-5d %s, %s", getName(), getExperience(), getLocation().toString(), boss.getName()));
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Technician) {
            Technician tech = (Technician) obj;
            return Integer.compare(tech.getExperience(), getExperience());
        } else {
            return -1;
        }
    }
}
