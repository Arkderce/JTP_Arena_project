package Arena;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Targets {
    public int targetId = 0;
    HashMap<Integer, List<Double>> targetsPos = new HashMap<Integer, List<Double>>();


    public void addTarget(double x, double y){
        targetsPos.put(targetId, Arrays.asList((x/2), (y/2)));
        targetId++;
    }

    public void deleteTargets(){
        int size = targetsPos.size();
        for (int i = 0; i < size; i++) {
            targetsPos.remove(i);
        }
    }

    public HashMap<Integer, List<Double>> returnTargetsPos(){
        return targetsPos;
    }
}
