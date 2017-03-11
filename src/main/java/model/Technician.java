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
    private List<LogMessage> log;

    public Technician(String name, Environment environment, Location location) {
        super(name, environment, location);
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

        }
    }

    private void ageUp() {
        age++;
        if(age > AGE_MAX) {
            die();
        }
    }

}
