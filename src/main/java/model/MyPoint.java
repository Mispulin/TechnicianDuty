package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Filip Zedek
 *         Date: 06.05.2017
 */
@Getter
@Setter
@ToString
public class MyPoint {
    private int x;
    private int y;

    public MyPoint(int x,int y){
        this.x=x;
        this.y=y;
    }
}
