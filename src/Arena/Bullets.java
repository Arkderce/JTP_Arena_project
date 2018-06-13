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

    /**
     * Constructor
     * @param _arena
     * Arena object that's pointing at place where bullets are going to exist
     */
    Bullets(Arena _arena){
        arena = _arena;
    }

    /**
     * Creates a bullet with given set of information
     * @param ClientId
     * Decides which object bullet belongs to
     * @param posX
     * Starting position X of bullet
     * @param posY
     * Starting position Y of bullet
     * @param angle
     * Angle refering to starting position at which bullet flies
     */
    public void createBullet(int ClientId, double posX, double posY, double angle){
        List<Double> temp = new ArrayList<Double>();
        temp.add(posX); temp.add(posY); temp.add(angle); temp.add((double)ClientId);
        bullets.put(bulletNumber, temp);
        bulletNumber ++;
    }

    /**
     * Moves each of bullets currently existing on arena by magnitude of 5
     *
     * Use  interactionOfBullet(List, Targets, HashMap, HashMap, double, double)
     * to check if moved bulled ceased to exist and what consequences it had
     *
     * @param oTargets
     * Object that has specifications of every target currently on arena
     * @param oWalls
     * Object that has specifications of every wall currently on arena
     * @param posX
     * Current position X of player on arena
     * @param posY
     * Current position Y of player on arena
     */

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

            if (!interactionOfBullet(temp, oTargets, targetsPos, wallsPos, posX, posY)) {
                bullets.put(bullet, temp);
            } else {
                toDelete.add(bullet);
            }
        }

        for (int i = 0; i < toDelete.size(); i++) {
            bullets.remove(toDelete.get(i));
        }
    }

    /**
     * Checks whenever bullet does interact with objects on arena such as walls, player or boundaries
     *
     * @param bulletSpecs
     * Specification of currently checked bullet
     * @param oTargets
     * Object that has specifications of every target currently on arena
     * @param targetsPos
     * List of positions for every target on arena
     * @param wallsPos
     * List of positions for every wall on arena
     * @param posX
     * Current position X of player
     * @param posY
     * Current position Y of player
     * @return {boolean}
     * True if interaction happened, otherwise false
     */
    public boolean interactionOfBullet(List<Double> bulletSpecs, Targets oTargets,
                                       HashMap<Integer, List<Double>> targetsPos,
                                       HashMap<Integer, List<Double>> wallsPos, double posX, double posY){
        boolean didInteract = false;
        Double bX = bulletSpecs.get(0);
        Double bY = bulletSpecs.get(1);
        Double clientB = bulletSpecs.get(3);
        if(bX >= x || bX <= 0 || bY >= y || bY <= 0){
            didInteract = true;
        }

        if((bX >= posX && bX <= posX + 40) && (bY >= posY && bY <= posY + 40) && clientB != 1){
            if(arena.getArmor() > 0){
                arena.setHealth(arena.getHealth()-5);
                arena.setArmor(arena.getArmor()-25);
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

    /**
     * Encapsulation of private data
     * @return map of current bullets on arena
     */
    public HashMap<Integer, List<Double>> returnBullets(){
        return bullets;
    }
}
