package Arena;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bullets {
    public int bulletNumber = 1;
    public double x = 1200;
    public double y = 800;
    Arena arena;
    HashMap<Integer, List<Double>> bullets = new HashMap<Integer, List<Double>>();

    Bullets(Arena _arena){
        arena = _arena;
    }

    public void createBullet(int ClientId, double posX, double posY, double angle){
        List<Double> temp = new ArrayList<Double>();
        temp.add(posX); temp.add(posY); temp.add(angle); temp.add((double)ClientId);
        bullets.put(bulletNumber, temp);
        bulletNumber ++;
    }

    public void moveBullets( Targets oTargets, Walls oWalls, double posX, double posY) {
        HashMap<Integer, List<Double>> targetsPos = oTargets.returnTargetsPos();
        HashMap<Integer, List<Double>> wallsPos = oWalls.returnWalls();
        List<Integer> toDelete = new ArrayList<Integer>();
        for (Integer bullet : bullets.keySet()) {
            List<Double> bulletSpecs = bullets.get(bullet);
            Double angle = bulletSpecs.get(2);
            double vectorY = Math.sin((angle + 90) * (Math.PI / 180.0));
            double vectorX = Math.cos((angle + 90) * (Math.PI / 180.0));
            Double newX = (bulletSpecs.get(0) + vectorX * 5);
            Double newY = (bulletSpecs.get(1) + vectorY * 5);
            List<Double> temp = new ArrayList<Double>();
            temp.add(newX);
            temp.add(newY);
            temp.add(angle);
            temp.add(bulletSpecs.get(3));

            if (!interactionOfBullet(temp, bullet, oTargets, targetsPos, oWalls, wallsPos, posX, posY, arena)) {
                bullets.put(bullet, temp);
            } else {
                toDelete.add(bullet);
            }
        }

        for (int i = 0; i < toDelete.size(); i++) {
            bullets.remove(toDelete.get(i));
        }
    }

    public boolean interactionOfBullet(List<Double> bulletSpecs, Integer bullet, Targets oTargets,
                                       HashMap<Integer, List<Double>> targetsPos, Walls oWalls,
                                       HashMap<Integer, List<Double>> wallsPos, double posX, double posY, Arena arena){
        boolean didInteract = false;
        Double bX = bulletSpecs.get(0);
        Double bY = bulletSpecs.get(1);
        Double clientB = bulletSpecs.get(3);
        if(bX >= x || bX <= 0 || bY >= y || bY <= 0){
            didInteract = true;
        }

        if((bX >= posX && bX <= posX + 40) && (bY >= posY && bY <= posY + 40) && clientB != 1){
            if(arena.getArmour() > 0){
                arena.setHealth(arena.getHealth()-5);
                arena.setArmour(arena.getArmour()-25);
            }
            else arena.setHealth(arena.getHealth()-10);
            didInteract = true;
        }

        List<Integer> toInteract = new ArrayList<Integer>();
        for (Integer targetId : targetsPos.keySet()) {
            if(clientB != 2) {
                List<Double> targetSpecs = targetsPos.get(targetId);
                Double pX = targetSpecs.get(0);
                Double pY = targetSpecs.get(1);
                if ((bX >= pX && bX <= pX + 40) && (bY >= pY && bY <= pY + 40)) {
                    toInteract.add(targetId);
                    didInteract = true;
                }
            }
        }

        for(int i = 0; i < toInteract.size(); i++){
            oTargets.targetHit(toInteract.get(i));
        }

        for (Integer wallId : wallsPos.keySet()) {
            List<Double> wallSpecs = wallsPos.get(wallId);
            Double pX = wallSpecs.get(0);
            Double pY = wallSpecs.get(1);
            if((bX >= pX && bX <= pX + 40) && (bY >= pY && bY <= pY + 40)){
                didInteract = true;
            }
        }


        return didInteract;
    }

    public HashMap<Integer, List<Double>> returnBullets(){
        return bullets;
    }
}
