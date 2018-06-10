package Arena;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Walls {
    HashMap<Integer, List<Double>> wallsPos = new HashMap<Integer, List<Double>>();
    private int wallId = 0;

    public void createWalls(double x, double y){
        wallsPos.put(wallId, Arrays.asList(x/2 - 100, y/2 - 100));
        wallId++;
    }

    public HashMap<Integer, List<Double>> returnWalls(){
        return wallsPos;
    }
}
