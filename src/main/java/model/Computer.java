package model;

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
    private static final double MALFUNCTION_PROBABILITY = 0.2;

    private boolean working;
    private int priority;
    private boolean assigned;
    private int age;

    public Computer(String name, Environment environment, Location location) {
        super(name, environment, location);
        working = rand.nextDouble() > MALFUNCTION_PROBABILITY;
        priority = 0;
        assigned = false;
        age = rand.nextInt(AGE_MAX - 1) + 1;
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
            report("Please, repair me!");
            priority++;
        }
    }

    private void report(String message) {
        System.out.println(String.format("%-13s %-2s %s", getName(), getPriority(), message));
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
        report("Yay, I'm okay now!");
    }

    public void replace() {
        working = true;
        assigned = false;
        age = 0;
        report("Yay, someone younger is taking my place!");
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
        priority = rand.nextInt(PRIORITY_MAX - 1) + 1;
        report("Oops! Something went wrong.");
    }

    public boolean getWorking() {
        return working;
    }

    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Computer) {
            Computer comp = (Computer) obj;
            return Integer.compare(getPriority(), comp.getPriority());
        }
        else {
            return -1;
        }
    }
}
