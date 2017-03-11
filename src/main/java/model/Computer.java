package model;

import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Computer extends Entity {

    private static final Random rand = Randomizer.getRandom();
    private static final int PRIORITY_MAX = 10;
    private static final int AGE_MAX = 20;
    private static final double MALFUNCTION_PROBABILITY = 0.4;

    private boolean working;
    private int priority;
    private boolean assigned;
    private int age;

    public Computer(String name, Environment environment, Location location) {
        super(name, environment, location);
        working = rand.nextDouble() <= MALFUNCTION_PROBABILITY;
        priority = rand.nextInt(PRIORITY_MAX - 1) + 1;
        assigned = false;
        age = rand.nextInt(AGE_MAX - 1) + 1;
    }

    public void act() {
        ageUp();
        if (isAlive()) {
            compute();
        }
    }

    private void ageUp() {
        age++;
        if(age > AGE_MAX) {
            working = false;
        }
    }

    private void compute() {
        if (working) {
            if(rand.nextDouble() <= MALFUNCTION_PROBABILITY) {
                working = false;
                report("Oops! Something went wrong.");
            } else {
                report("Working as usual.");
            }
        } else {
            report("Please, repair me!");
        }
    }

    private void report(String message) {
        System.out.println(String.format("%s\\t%s", getName(), message));
    }

    public void assign() {
        assigned = true;
    }

    public boolean isRepairable() {
        return age <= AGE_MAX;
    }

    public void repair() {
        working = true;
        assigned = false;
    }

    public void replace() {
        working = true;
        assigned = false;
        age = 0;
    }

    public int getPriority() {
        return priority;
    }

}
