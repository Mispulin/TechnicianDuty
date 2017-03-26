package model;

/**
 * Created by Mish.k.a on 13. 3. 2017.
 */
public class Counter {

    public static int server = 1;
    public static int technician = 1;
    public static int computer = 1;

    public static void addServer() {
        server++;
    }

    public static void addTechnician() {
        technician++;
    }

    public static void addComputer() {
        computer++;
    }

    public static void reset() {
        server = 1;
        technician = 1;
        computer = 1;
    }

}
