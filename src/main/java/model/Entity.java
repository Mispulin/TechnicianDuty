package model;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public abstract class Entity {

    private boolean alive;
    private Environment environment;
    private Location location;

    public Entity(Environment environment, Location location) {
        alive = true;
        this.environment = environment;
        setLocation(location);
    }

    abstract public void act();

    public boolean isAlive() {
        return alive;
    }

    public void setDead() {
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

    public Location getLocation() {
        return location;
    }

    public Environment getField() {
        return environment;
    }

}
