package model;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class LogMessage {

    private int time;
    private Technician technician;
    private Computer computer;
    private String message;

    public LogMessage(int time, Technician technician, Computer computer, String message) {
        this.time = time;
        this.technician = technician;
        this.computer = computer;
        this.message = message;
    }

    public String toString() {
        return String.format("%d\\t%s\\t%s\\t%s", time, technician.getName(), computer.getName(), message);
    }

}
