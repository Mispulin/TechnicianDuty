package model.entity;

import model.Counter;
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
    private Server server;

    public Computer(Environment environment, Location location, Server server, boolean reportSelf) {
        super("Computer " + Counter.computer, environment, location, reportSelf);
        Counter.addComputer();
        working = true;
        priority = 0;
        assigned = false;
        age = rand.nextInt(AGE_MAX - 1) + 1;
        logs = new ArrayList<>();
        addListener(server);
    }

    public void addListener(Server toAdd) {
        server = toAdd;
    }

    public void act(List<Entity> entities) {
        ageUp();
        if (isAlive()) {
            compute();
        }
    }

    private void ageUp() {
        if (age == AGE_MAX + 1 && working) {
            // Why only working? Cause there's no need for now working computer to send another crash notification.
            // When technician get to the computer, he will find out that the computer is too old and he will replace it.
            // Also... it's not working so it doesn't do anything anymore.
            working = false;
            report("I'm too old, need to be replaced!");
            server.crashNotification(this);
            age++;
        } else {
            age++;
        }
    }

    private void compute() {
        if (working) {
            if (rand.nextDouble() <= MALFUNCTION_PROBABILITY) {
                breakIt();
            } else {
                report("Working as usual.");
            }
        } else {
            if (isRepairable()) {
                if (!assigned) {
                    if (priority <= PRIORITY_MAX && rand.nextDouble() > 0.5) {
                        priority++;
                        report("Please, repair me! I really need it!");
                    } else {
                        report("Please, repair me!");
                    }
                } else {
                    report("Please, repair me!");
                }
            } else {
                setPriority(0);
                report("OUT OF SERVICE.");
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

    public void replace(List<Entity> entities) {
        Location place = getLocation();
        Computer replacement = new Computer(getEnvironment(), place, server, getReportSelf());
        replacement.setAge(0);
        report(String.format("Computer %d is now my replacement.", Counter.computer - 1));
        entities.add(replacement);
        die();
    }

    public boolean isAssigned() {
        return assigned;
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
        if (!(priority > 0)) {
            priority = rand.nextInt(PRIORITY_MAX - 1) + 1;
        }
        report("Oops! Something went wrong.");
        server.crashNotification(this);
    }

    public boolean getWorking() {
        return working;
    }

    public void print() {
        System.out.println(String.format("%-15s age: %-5d %s, %s", getName(), getAge(), getLocation().toString(), server.getName()));
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Computer) {
            Computer comp = (Computer) obj;
            return Integer.compare(comp.getPriority(), getPriority());
        } else {
            return -1;
        }
    }
}
