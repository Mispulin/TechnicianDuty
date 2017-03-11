package model;

import java.util.List;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public abstract class Entity {

    private boolean alive;
    private String name;
    private Environment environment;
    private Location location;

    public Entity(String name, Environment environment, Location location) {
        alive = true;
        this.name = name;
        this.environment = environment;
        setLocation(location);
    }

    abstract public void act(List<Entity> entities);

    public void setDead() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
        if(location != null) {
            environment.clear(location);
            location = null;
            environment = null;
        }
    }

    public void setLocation(Location newLocation)  {
        if(location != null) {
            environment.clear(location);
        }
        location = newLocation;
        environment.place(this, newLocation);
    }

    public void setLocation(int row, int col)  {
        if(location != null) {
            environment.clear(location);
        }
        location = new Location(row, col);
        environment.place(this, location);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Environment getEnvironment() {
        return environment;
    }

}
