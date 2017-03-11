package model;

/**
 * Created by Mish.k.a on 11. 3. 2017.
 */
public class Location {

    private int row;
    private int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        }
        else {
            return false;
        }
    }

    public String toString()  {
        return "[" + row + "," + col + "]";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
