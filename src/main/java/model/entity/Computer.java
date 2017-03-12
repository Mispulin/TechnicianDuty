package model.entity;

import model.Environment;
import model.Location;
import model.Randomizer;
import model.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Computer extends Entity implements Comparable {

    private static final Random rand = Randomizer.getRandom();
    public static final int PRIORITY_MAX = 10;
    public static final int AGE_MAX = 40;
    public static final int RETIREMENT = 2;
    private static final double MALFUNCTION_PROBABILITY = 0.1;

    private boolean working;
    private int priority;
    private boolean assigned;
    private int age;
    private List<Log> logs;
    private ServerListener server;

    public Computer(String name, Environment environment, Location location, ServerListener server, boolean reportSelf) {
        super(name, environment, location, reportSelf);
        working = true;
        priority = 0;
        assigned = false;
        age = rand.nextInt(AGE_MAX - 1) + 1;
        logs = new ArrayList<>();
        addListener(server);
    }

    public void addListener(ServerListener toAdd) {
        server = toAdd;
    }

    public void act(List<Entity> entities) {
        ageUp();
        if (isAlive()) {
            compute();
        }
    }

    private void ageUp() {
        age++;
        if(age > AGE_MAX) {
            report("I'm too old, need to be replaced!");
            setDead();
            working = false;
        }
    }

    private void compute() {
        if (working) {
            if(rand.nextDouble() <= MALFUNCTION_PROBABILITY) {
                breakIt();
            } else {
                report("Working as usual.");
            }
        } else {
            if (rand.nextDouble() > 0.5) {
                priority++;
                report("Please, repair me! I really need it!");
            } else {
                report("Please, repair me!");
            }
        }
    }

    public void report(String message) {
        Log log;
        if (priority > 0) {
            log = new Log(this, String.format("%s (priority: %d)", message, getPriority()));
        } else {
            log = new Log(this, String.format("%s", message));
        }
        logs.add(log);
    }

    public void assign() {
        assigned = true;
        server.assignedNotification(this);
    }

    public boolean isRepairable() {
        return age <= (AGE_MAX - RETIREMENT);
    }

    public void repair() {
        working = true;
        assigned = false;
        priority = 0;
        report("Yay, I'm okay now!");
    }

    public void replace() {
        working = true;
        assigned = false;
        age = 0;
        priority = 0;
        report("Yay, someone younger is taking my place!");
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void breakIt() {
        working = false;
        if (! (priority > 0)) {
            priority = rand.nextInt(PRIORITY_MAX - 1) + 1;
        }
        report("Oops! Something went wrong.");
        server.crashNotification(this);
    }

    public boolean getWorking() {
        return working;
    }

    public void print() {
        System.out.println(String.format("%s, stáří: %-5d %s", getName(), getAge(), getLocation().toString()));
    }

    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Computer) {
            Computer comp = (Computer) obj;
            return Integer.compare(comp.getPriority(), getPriority());
        }
        else {
            return -1;
        }
    }
}
