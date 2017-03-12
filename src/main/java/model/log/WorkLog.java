package model.log;

import model.entity.Technician;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class WorkLog extends Log {

    public WorkLog(Technician technician, String message) {
        super(technician, message);
    }

    @Override
    public String toString() {
        Technician technician = (Technician) getEntity();
        String assignment = technician.getAssignment() != null ? technician.getAssignment().getName() : "";
        if (assignment.length() > 0) {
            // return String.format("%s, %-6s %s (%s)", technician.getName(), technician.getExperience(), getMessage(), assignment);
            return String.format("%-20s %s (%s)", technician.getName(), getMessage(), assignment);
        }
        // return String.format("%s, %-6s %s", technician.getName(), technician.getExperience(), getMessage());
        return String.format("%-20s %s", technician.getName(), getMessage());
    }

}
