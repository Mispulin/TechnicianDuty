package model;

/**
 * Created by Mish.k.a on 19. 3. 2017.
 */
public class AssignmentItem<Computer, Technician> {

    private Computer computer;
    private Technician technician;

    public AssignmentItem(Computer computer, Technician technician) {
        this.computer = computer;
        this.technician = technician;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Computer getComputer() {
        return computer;
    }

    public Technician getTechnician() {
        return technician;
    }
}
