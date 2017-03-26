package model;

import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Randomizer {
    private static final int SEED = 13;
    private static final Random rand = new Random(SEED);
    // If true, numbers will always be the same, depending on SEED.
    private static final boolean useShared = true;

    public static Random getRandom() {
        if(useShared) {
            return rand;
        }
        else {
            return new Random();
        }
    }

    public static void reset() {
        if(useShared) {
            rand.setSeed(SEED);
        }
    }

}
