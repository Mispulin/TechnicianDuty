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

    private int age;
    private boolean available;
    private Location workPlace;
    private List<LogMessage> log;

    public Technician(String name, Environment environment, Location workPlace) {
        super(name, environment, workPlace);
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
                go();
            }
        }
    }

    private void ageUp() {
        age++;
        if(age > AGE_MAX) {
            die();
        }
    }

    public void assign(Computer computer) {
        workPlace = computer.getLocation();
    }

    private void go() {
        if(!isAt()) {
            if((getLocation().getRow() == workPlace.getRow()) && (getLocation().getCol() < workPlace.getCol())){
                goRight();
            } else if((getLocation().getRow() == workPlace.getRow()) && (getLocation().getCol() > workPlace.getCol())){
                goLeft();
            } else if((getLocation().getCol() == workPlace.getCol()) && (getLocation().getRow() < workPlace.getRow())) {
                goUp();
            } else if((getLocation().getCol() == workPlace.getCol()) && (getLocation().getRow() > workPlace.getRow())) {
                goDown();
            }
        }
    }

    private boolean isAt() {
        boolean above = workPlace.getRow() - 1 == getLocation().getRow();
        boolean under = workPlace.getRow() + 1 == getLocation().getRow();
        boolean left = workPlace.getCol() - 1 == getLocation().getCol();
        boolean right = workPlace.getCol() + 1 == getLocation().getCol();
        boolean horizontal = (left || right) && (workPlace.getRow() == getLocation().getRow());
        boolean vertical = (above || under) && (workPlace.getCol() == getLocation().getCol());
        return (horizontal || vertical);
    }

    private boolean hasObstacle(Location location) {
        return getEnvironment().getEntityAt(location) != null;
    }

    private void goUp() {
        Location toGo = new Location(getLocation().getRow() - 1, getLocation().getCol());
        if(hasObstacle(toGo)) {
            if(toGo.getCol() + 1 <= getEnvironment().getWidth()) {
                goRight();
            } else {
                goLeft();
            }
        } else setLocation(toGo);
    }

    private void goRight() {
        Location toGo = new Location(getLocation().getRow(), getLocation().getCol() + 1);
        if(hasObstacle(toGo)) {
            if(toGo.getRow() + 1 <= getEnvironment().getHeight()) {
                goDown();
            } else {
                goUp();
            }
        } else setLocation(toGo);
    }

    private void goDown() {
        Location toGo = new Location(getLocation().getRow() + 1, getLocation().getCol());
        if(hasObstacle(toGo)) {
            if(toGo.getCol() - 1 >= 0) {
                goLeft();
            } else {
                goRight();
            }
        } else setLocation(toGo);
    }

    private void goLeft() {
        Location toGo = new Location(getLocation().getRow(), getLocation().getCol() - 1);
        if(hasObstacle(toGo)) {
            if(toGo.getRow() - 1 >= 0) {
                goUp();
            } else {
                goDown();
            }
        } else setLocation(toGo);

    }
}
