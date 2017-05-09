package model;

import lombok.*;

import java.util.List;

/**
 * @author Filip Zedek
 *         Date: 06.05.2017
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Path {
    List<MyPoint> path;

}
