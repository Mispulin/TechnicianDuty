package model.entity;

import model.entity.Computer;
import model.entity.Technician;

/**
 * Created by Mish.k.a on 12. 3. 2017.
 */
public interface ServerListener {

    void addTechnician(Technician technician);

    void crashNotification(Computer computer);

    void assignedNotification(Computer computer);
}
