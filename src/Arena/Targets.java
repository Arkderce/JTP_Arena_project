package Arena;
import Controllers.ArenaController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Targets {
    ArenaController arenaController;
    public int targetId = 0;
    HashMap<Integer, List<Double>> targetsPos = new HashMap<Integer, List<Double>>();
    HashMap<Integer, Double> targetsHealth = new HashMap<Integer, Double>();



    public void addTarget(double x, double y, double h){
        targetsPos.put(targetId, Arrays.asList(x, y));
        targetsHealth.put(targetId, h);
        targetId++;
    }

    public void targetHit(int id){
        targetsHealth.put(id, targetsHealth.get(id) - 10);
        if(targetsHealth.get(id) <= 0){
            deleteTarget(id);
            arenaController.writeOut("You kill target nr " + id);
        }else{
            arenaController.writeOut("You hit target nr " + id);
        }
    }

    public void deleteTarget(int id){
        targetsPos.remove(id);
        targetsHealth.remove(id);
    }

    public void deleteTargets(){
        int size = targetsPos.size();
        for (int i = 0; i < size; i++) {
            targetsPos.remove(i);
        }
        size = targetsHealth.size();
        for(int i = 0; i < size; i++){
            targetsHealth.remove(i);
        }
    }

    public HashMap<Integer, List<Double>> returnTargetsPos(){
        return targetsPos;
    }
    public HashMap<Integer, Double> returnTargetsHealth(){ return targetsHealth; }
    public void setController(ArenaController arenaController) {
        this.arenaController = arenaController;
    }

}
