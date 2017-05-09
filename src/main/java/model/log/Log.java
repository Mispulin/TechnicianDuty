package model.log;

import app.Controller;
import model.entity.Entity;

/**
 * Created by Mish.k.a on 12. 3. 2017.
 */
public class Log {

    private Entity entity;
    private String message;

    public Log(Entity entity, String message) {
        this.entity = entity;
        this.message = message;
        if (entity.getReportSelf()) {
            this.print();
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.format("%-20s %s", getEntity().getName(), getMessage());
    }

    public void print() {
        Controller.instance.getLogArea().appendText(this.toString()+"\n");
        System.out.println(this.toString());
    }

}
