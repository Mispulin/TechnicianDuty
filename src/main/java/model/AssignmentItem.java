package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Mish.k.a on 19. 3. 2017.
 */
@Getter
@Setter
@AllArgsConstructor
public class AssignmentItem<Computer, Technician> {

    private Computer computer;
    private Technician technician;
}
