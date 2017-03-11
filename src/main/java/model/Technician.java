package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Technician extends Entity {

    private static final Random rand = Randomizer.getRandom();
    private static final int AGE_MAX = 40;
    private static final int SKILL_MAX = 10;

    private int age;
    private int skill;
    private boolean available;
    private List<String> log;

    public Technician(Environment environment, Location location) {
        super(environment, location);
        age = rand.nextInt(AGE_MAX - 1) + 1;
        skill = rand.nextInt(SKILL_MAX - 1) + 1;
        available = true;
        log = new ArrayList<String>();
    }

    public void act() {

    }

}
