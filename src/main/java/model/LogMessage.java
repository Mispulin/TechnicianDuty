package model;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class LogMessage {

    private Technician technician;
    private String message;

    public LogMessage(Technician technician, String message) {
        this.technician = technician;
        this.message = message;
    }

    public String toString() {
        return String.format("%s\\t%s\\t%s", technician.getName(), technician.getAssignment().getName(), message);
    }

}
