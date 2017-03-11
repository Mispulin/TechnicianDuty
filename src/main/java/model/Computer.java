package model;

import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Computer extends Entity {

    private static final Random rand = Randomizer.getRandom();
    private static final int PRIORITY_MAX = 10;
    private static final int DIFFICULTY_MAX = 10;
    private static final int AGE_MAX = 20;

    private boolean working;
    private int priority;
    private int difficulty;
    private boolean assigned;
    private int age;

    public Computer(String name, Environment environment, Location location) {
        super(name, environment, location);
        working = rand.nextDouble() < 0.5 ? false : true;
        priority = rand.nextInt(PRIORITY_MAX - 1) + 1;
        difficulty = rand.nextInt(DIFFICULTY_MAX - 1) + 1;
        assigned = false;
        age = rand.nextInt(AGE_MAX - 1) + 1;
    }

    public void act() {
        
    }

}
