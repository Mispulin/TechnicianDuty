package model;

import model.entity.Technician;

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
        String assignment = technician.getAssignment() != null ? technician.getAssignment().getName() : "";
        return String.format("%-20s %-20s %s", technician.getName(), assignment, message);
    }

}
