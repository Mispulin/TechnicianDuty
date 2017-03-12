package model.entity;

import model.Environment;
import model.Location;
import model.log.Log;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mish.k.a on 12. 3. 2017.
 */
public class Server extends Entity implements ServerListener {

    private List<Technician> technicians;
    private List<Computer> assignments;
    private List<Log> logs;

    public Server(String name, Environment environment, Location location, boolean reportSelf) {
        super(name, environment, location, reportSelf);
        technicians = new ArrayList<>();
        assignments = new ArrayList<>();
        logs = new ArrayList<>();
    }

    @Override
    public void act(List<Entity> entities) {
        if (hasAssignments()) {
            if (hasFreeTechnicians()) {
                assignWork();
            } else {
                report("There's work to do but no free technician... Gotta wait.");
            }
        } else {
            report("There are no free assignments.");
        }
    }

    @Override
    public void report(String message) {
        Log log = new Log(this, message);
        logs.add(log);
    }

    @Override
    public void crashNotification(Computer computer) {
        assignments.add(computer);
        report("Got a crash log! Better assign it to someone!");
    }

    @Override
    public void assignedNotification(Technician technician, Computer computer) {
        assignments.remove(computer);
        report(String.format("%s assigned to %s.", computer.getName(), technician.getName()));
    }

    @Override
    public void giveUpNotification(Computer computer) {
        assignments.add(computer);
        report(String.format("%s assignment cancelled.", computer.getName()));
    }

    @Override
    public void retireTechnician(Technician technician) {
        technicians.remove(technician);
        report(String.format("%s has left.", technician.getName()));
    }

    @Override
    public void addTechnician(Technician technician) {
        technicians.add(technician);
    }

    public List<Technician> getTechnicians() {
        return technicians;
    }

    private boolean hasFreeTechnicians() {
        for (Technician technician : technicians) {
            if (technician.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private List<Technician> getFreeTechnicians() {
        List<Technician> freeTechnicians = technicians.stream().filter(Technician::isAvailable).collect(Collectors.toList());
        return freeTechnicians;
    }

    public List<Computer> getAssignments() {
        return assignments;
    }

    private boolean hasAssignments() {
        return getAssignments().size() > 0;
    }

    public void assignWork() {
        Collections.sort(assignments);
        List<Technician> freeTechnicians = getFreeTechnicians();
        Collections.sort(freeTechnicians);
        freeTechnicians.stream().filter(technician -> !assignments.isEmpty()).forEach(technician -> {
            technician.assign(assignments.get(0));
            report(String.format("There's work to do! %s is on his way!", technician.getName()));
        });
    }

    public void print() {
        System.out.println(String.format("%-13s %s", getName(), getLocation().toString()));
    }
}
