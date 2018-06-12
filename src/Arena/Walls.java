package Arena;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Walls {
    HashMap<Integer, List<Double>> wallsPos = new HashMap<Integer, List<Double>>();
    private int wallId = 0;

    /**
     * Initialization of static walls on map
     * @param x
     * Width of arena
     * @param y
     * Height of arena
     */
    public void createWalls(double x, double y){
        addWall(x/2 - 100, y/2 - 100);
        addWall(x/2 - 60, y/2 - 100);
        addWall(x/2 - 100, y/2 - 60);

        addWall(x/2 - 100, y/2 + 100);
        addWall(x/2 - 60, y/2 + 100);
        addWall(x/2 - 100, y/2 + 60);

        addWall(x/2 + 100, y/2 - 100);
        addWall(x/2 + 60, y/2 - 100);
        addWall(x/2 + 100, y/2 - 60);

        addWall(x/2 + 100, y/2 + 100);
        addWall(x/2 + 60, y/2 + 100);
        addWall(x/2 + 100, y/2 + 60);

        addWall((x-700)/2 - 100, y-200 - 100);
        addWall((x-700)/2 - 60, y-200 - 100);
        addWall((x-700)/2 - 100, y-200 - 60);

        addWall((x-700)/2 - 100, y-200 + 100);
        addWall((x-700)/2 - 60, y-200 + 100);
        addWall((x-700)/2 - 100, y-200 + 60);

        addWall((x-700)/2 + 100, y-200 - 100);
        addWall((x-700)/2 + 60, y-200 - 100);
        addWall((x-700)/2 + 100, y-200 - 60);

        addWall((x-700)/2 + 100, y-200 + 100);
        addWall((x-700)/2 + 60, y-200 + 100);
        addWall((x-700)/2 + 100, y-200 + 60);

        addWall(x-200, y-240);
        addWall(x-240, y-240);
        addWall(x-240, y-280);
        addWall(x-280, y-280);


        addWall(x-700, y-700);
        addWall(x-800, y-700);
        addWall(x-900, y-700);
        addWall(x-1000, y-700);
        addWall(x-1100, y-700);

        addWall(x - 100, y-100);
        addWall(x - 100, y-140);
        addWall(x - 100, y-180);

        addWall(x - 100, y-740);
        addWall(x - 140, y-740);
        addWall(x - 140, y-700);
        addWall(x - 140, y-660);
        addWall(x - 140, y-620);
        addWall(x - 140, y-580);

        addWall(x - 100, y-700);
        addWall(x - 180, y-700);
        addWall(x - 220, y-700);

        addWall(x-200 + 100, y-300 + 100);
        addWall(x-200 + 60, y-300 + 100);
        addWall(x-200 + 100, y-300 + 60);
    }

    /**
     * Adds wall on given coordinates.
     *
     * @param x wall x-position
     * @param y wall y-position
     */
    public void addWall(double x, double y){
        wallsPos.put(wallId, Arrays.asList(x, y));
        wallId++;
    }

    /**
     * @return hashmap of walls position
     */
    public HashMap<Integer, List<Double>> returnWalls(){
        return wallsPos;
    }
}
