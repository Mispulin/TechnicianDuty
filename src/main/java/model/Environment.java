package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Environment {

    // For random locations.
    private static final Random rand = Randomizer.getRandom();

    private Entity[][] field;
    private int height, width;

    public Environment(int height, int width) {
        this.height = height;
        this.width = width;
        field = new Entity[height][width];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void clear() {
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    public void clear(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    public void place(Entity entity, int row, int col) {
        place(entity, new Location(row, col));
    }

    public void place(Entity entity, Location location) {
        field[location.getRow()][location.getCol()] = entity;
    }

    public Entity getEntityAt(int row, int col) {
        return field[row][col];
    }

    public Entity getEntityAt(Location location) {
        return getEntityAt(location.getRow(), location.getCol());
    }

    public Location randomAdjacentLocation(Location location) {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }

    public List<Location> getFreeAdjacentLocations(Location location)  {
        List<Location> free = new LinkedList<Location>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            if(getEntityAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }

    public Location freeAdjacentLocation(Location location)  {
        List<Location> free = getFreeAdjacentLocations(location);
        if(free.size() > 0) {
            return free.get(0);
        }
        else {
            return null;
        }
    }

    public List<Location> adjacentLocations(Location location)  {
        assert location != null : "Null location passed to adjacentLocations";
        List<Location> locations = new LinkedList<Location>();
        if(location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for(int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                int nextRow = row + rowOffset;
                if(nextRow >= 0 && nextRow < height) {
                    for(int colOffset = -1; colOffset <= 1; colOffset++) {
                        int nextCol = col + colOffset;
                        if(nextCol >= 0 && nextCol < width && (rowOffset != 0 || colOffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            Collections.shuffle(locations, rand);
        }
        return locations;
    }

}
