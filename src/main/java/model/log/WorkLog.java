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
        String location = String.format("[%d, %d]", technician.getLocation().getRow() + 1, technician.getLocation().getCol() + 1);
        if (assignment.length() > 0) {
            // return String.format("%s, %-6s %s (%s)", technician.getName(), technician.getExperience(), getMessage(), assignment);
            // return String.format("%s %-20s %s (%s)", location, technician.getName(), getMessage(), assignment);
            return String.format("%-20s %s (%s)", technician.getName(), getMessage(), assignment);
        }
        // return String.format("%s, %-6s %s", technician.getName(), technician.getExperience(), getMessage());
        // return String.format("%s %-20s %s", location, technician.getName(), getMessage());
        return String.format("%-20s %s", technician.getName(), getMessage());
    }

}
