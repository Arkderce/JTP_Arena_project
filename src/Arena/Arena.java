
package Arena;
import Controllers.ArenaController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Arena {
    private static final double Wi = 1200, He = 800;

    int myId = 1;

    private Image player;
    private Image target;
    private Image gun;
    private Image bulletEnemy;
    private Image bulletOwn;
    private Image crosshair;
    private Image gameover;
    private Image background;
    private Image wall;

    int numberOfMaxTargets = 10;
    int currentNumOfTargets = 0;
    int armor = 100;
    int levelDisplay = 1;
    double health = 100;
    double posX = 600;
    double posY = 400;
    double mposX = 0;
    double mposY = 0;
    double angle = 0;
    double oldT = 0;
    double level = 0;

    /**
     * Returns current armor value.
     *
     * @return current armor value.
     */
    public int getArmor() {
        return armor;
    }

    /**
     * Sets current armor value.
     *
     * @param armor current armor value
     */
    public void setArmor(int armor) {
        if(health>0) {
            if (armor != 100)
                arenaController.writeOut("You have been hit for : " + (int) (getArmor() - armor) + " armor");
            this.armor = armor;
            arenaController.changeArmor(armor);
        }
    }

    /**
     * Returns current health value.
     *
     * @return current health value
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets current health value.
     *
     * @param health current health value
     */
    public void setHealth(double health) {
        if(health>0) {
            arenaController.writeOut("You have been hit for : " + (int) (getHealth() - health) + " health");
        }
            this.health = health;
            arenaController.changeHealth(health);
    }

    boolean running, goNorth, goSouth, goEast, goWest;

    HashMap<Integer, List<Double>> bullets = new HashMap<Integer, List<Double>>();
    HashMap<Integer, List<Double>> targetsPos = new HashMap<Integer, List<Double>>();
    HashMap<Integer, List<Double>> wallsPos = new HashMap<Integer, List<Double>>();
    HashMap<Integer, Double> targetsHealth = new HashMap<Integer, Double>();

    Bullets oBullets = new Bullets(this);
    Targets oTargets = new Targets();
    Walls oWalls = new Walls();
    ArenaController arenaController;

    /**
     * The main entry point for all JavaFX applications. The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * @param theStage
     * Primary stage of arena application
     * @param anchorPane
     * Primary Pane for arena
     */
    public void start(Canvas theStage, AnchorPane anchorPane){

        GraphicsContext gc = theStage.getGraphicsContext2D();
        oTargets.setController(arenaController);

        try {
            background = new Image(getClass().getResource("/resources/background.png").toURI().toString());
            player = new Image(getClass().getResource("/resources/player.png").toURI().toString());
            target = new Image(getClass().getResource("/resources/target.png").toURI().toString());
            gun = new Image(getClass().getResource("/resources/gunv2.png").toURI().toString());
            bulletEnemy = new Image(getClass().getResource("/resources/bullet.png").toURI().toString());
            bulletOwn = new Image(getClass().getResource("/resources/bulletown.png").toURI().toString());
            crosshair = new Image(getClass().getResource("/resources/crosshair.png").toURI().toString());
            gameover = new Image(getClass().getResource("/resources/gameover.png").toURI().toString());
            wall = new Image(getClass().getResource("/resources/wall.png").toURI().toString());
        } catch(Exception e){
            e.printStackTrace();
        }

        oWalls.createWalls(Wi, He);
        wallsPos = oWalls.returnWalls();


        theStage.setOnMouseMoved(event -> {
            if(health > 0) {
                mposX = (int) event.getX();
                mposY = (int) event.getY();
            }
        });

        theStage.setOnMousePressed(event -> {
            if(health > 0) {
                oBullets.createBullet(myId, posX+17, posY+17, angle);
            }
        });

        theStage.setOnKeyPressed(event -> {
            if(health > 0) {
                switch (event.getCode()) {
                    case W:
                        goNorth = true;
                        break;
                    case S:
                        goSouth = true;
                        break;
                    case A:
                        goWest = true;
                        break;
                    case D:
                        goEast = true;
                        break;
                    case SHIFT:
                        running = true;
                        break;
                }
            }
        });
        theStage.setOnKeyReleased(event -> {
            if (health > 0) {
                switch (event.getCode()) {
                    case W:
                        goNorth = false;
                        break;
                    case S:
                        goSouth = false;
                        break;
                    case A:
                        goWest = false;
                        break;
                    case D:
                        goEast = false;
                        break;
                    case SHIFT:
                        running = false;
                        break;
                }
            }
        });


        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            /**
             * GameLoop
             * @param currentNanoTime
             * Current Time
             */
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                int dx = 0, dy = 0;

                if (goNorth) dy -= 1;
                if (goSouth) dy += 1;
                if (goEast)  dx += 1;
                if (goWest)  dx -= 1;
                if (running) { dx *= 3; dy *= 3; }

                gc.drawImage( background, 0, 0 );

                double recentAngle = (countAngle(posX+20, posY+20, mposX, mposY)-90);
                if(recentAngle < 0.0) recentAngle += 360;
                if(angle != recentAngle){
                    angle = recentAngle;
                }

                double vectorY = Math.sin((angle+90)*(Math.PI/180.0)) ;
                double vectorX = Math.cos((angle+90)*(Math.PI/180.0)) ;
                if(health > 0){
                    drawRotatedImage(gc, gun, angle, posX-10,   posY-10);
                    gc.drawImage( player , posX, posY );
                    gc.drawImage( crosshair , (posX + 17) + vectorX * 100, (posY + 17) + vectorY * 100);
                }
                else {
                    gc.drawImage( gameover, 370, 350);
                    arenaController.button.setDisable(false);
                    arenaController.button.setManaged(true);
                }
                drawWalls(gc);


                if(targetsPos.size() == 0){
                    currentNumOfTargets = 0;
                    arenaController.setLevel(levelDisplay++);
                    Random rand = new Random();
                    numberOfMaxTargets = rand.nextInt(20) + 5;
                    setArmor(100);
                    if(level < 2.5) level += 0.5;
                }

                while(currentNumOfTargets < numberOfMaxTargets) {
                    Random rand = new Random();
                    int rX = rand.nextInt(1100) + 1;
                    int rY = rand.nextInt(700) + 1;
                    while(!isWithinAcceptableBounds(rX, rY)){
                        rX = rand.nextInt(1150) + 1;
                        rY = rand.nextInt(750) + 1;
                    }
                    oTargets.addTarget(rX, rY, 20);
                    currentNumOfTargets++;
                }

                targetsPos = oTargets.returnTargetsPos();
                drawTargets(gc);

                if(t + level >= oldT + 3.00){
                    oldT = t;
                    createEnemyBullets();
                }
                oBullets.moveBullets(oTargets, oWalls, posX, posY);
                bullets = oBullets.returnBullets();
                drawBullets(gc);

                if(dx != 0 || dy != 0) movePlayer(dx, dy);

            }
        }.start();
    }

    /**
     * Count angle between two points
     * @param spX
     * Current position X of center for player
     * @param spY
     * Current position Y of center for player
     * @param mpX
     * Current position X for mouse
     * @param mpY
     * Current position Y for mouse
     * @return
     * Return angle of sp between cathetus and hypotenuse of rectangular triangle created from sp and mp
     */
    private double countAngle(double spX, double spY, double mpX, double mpY){
        double xDistance = mpX - spX;
        double yDistance = mpY - spY;
        return Math.toDegrees(Math.atan2(yDistance, xDistance));
    }

    /**
     * Rotation of image
     * @param gc
     * Current graphicscontext
     * @param angle
     * Angle at which rotation will occur
     * @param px
     * Position X of rotation point
     * @param py
     * Position Y of rotation point
     */
    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    /**
     * Draws image that's supposed to rotate
     * @param gc
     * Current graphicscontext
     * @param image
     * Image to be rotated
     * @param angle
     * Angle at which rotation will occur
     * @param tlpx
     * Position X of rotation point
     * @param tlpy
     * Position Y of rotation point
     */
    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save();
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore();
    }

    /**
     * Spawn bullets for every target on arena
     */
    private void createEnemyBullets(){
        for(Integer targetId : targetsPos.keySet()){
            List<Double> targetSpecs = targetsPos.get(targetId);
            Double pX = targetSpecs.get(0);
            Double pY = targetSpecs.get(1);
            Random rand = new Random();
            int ang = rand.nextInt(360) + 1;
            oBullets.createBullet(2, pX+17, pY+17, ang);
        }
    }

    /**
     * Draw currently existing bullets
     * @param gc
     * Current graphicscontext
     */
    private void drawBullets(GraphicsContext gc){
        for(Integer bullet : bullets.keySet()){
            List<Double> bulletSpecs = bullets.get(bullet);
            Double bX = bulletSpecs.get(0);
            Double bY = bulletSpecs.get(1);
            Double id = bulletSpecs.get(3);

            if(id == myId){
                gc.drawImage( bulletOwn , bX, bY);
            } else {
                gc.drawImage( bulletEnemy , bX, bY);
            }

        }
    }

    /**
     * Draw currently existing walls
     * @param gc
     * Current graphicscontext
     */
    private void drawWalls(GraphicsContext gc){
        for (Integer key : wallsPos.keySet()) {
            gc.drawImage( wall , wallsPos.get(key).get(0), wallsPos.get(key).get(1) );
        }
    }

    /**
     * Draw currently existing targets
     * @param gc
     * Current graphicscontext
     */
    private void drawTargets(GraphicsContext gc){
        for (Integer key : targetsPos.keySet()) {
            gc.drawImage( target , targetsPos.get(key).get(0), targetsPos.get(key).get(1) );
        }
    }

    /**
     * Moves player by given coordinates.
     *
     * @param x value added to x position
     * @param y value added to y position
     */
    private void movePlayer(double x, double y){
        double newposX = x+posX;
        double newposY = y+posY;
        boolean obstacleOnPath = false;
        if(newposX <= Wi-40 && newposX >= 0 && newposY <= He-40 && newposY >= 0){
            for(Integer wallId : wallsPos.keySet()){
                List<Double> wallSpecs = wallsPos.get(wallId);
                Double pX = wallSpecs.get(0);
                Double pY = wallSpecs.get(1);
                if((newposX >= pX - 40 && newposX <= pX + 40 ) && (newposY >= pY - 40 && newposY <= pY + 40 )){
                    obstacleOnPath = true;
                }
            }

            if(!obstacleOnPath){
                posY = newposY;
                posX = newposX;
            }

        }
    }

    /**
     * Checks if target that's supposed to spawn wont appear at wall or player
     * @param x
     * Position X at which target want's to spawn
     * @param y
     * Position Y at which target want's to spawn
     * @return
     * True if target can spawn, false if otherwise
     */
    private boolean isWithinAcceptableBounds(double x, double y){
        boolean isWithinA = true;
        if(x >= Wi || x <= 0 || y >= He || y <= 0){
            isWithinA = false;
        }

        if((x >= posX - 100 && x <= posX + 200) && (y >= posY - 100 && y <= posY + 200)){
            isWithinA = false;
        }

        targetsPos = oTargets.returnTargetsPos();
        for (Integer targetId : targetsPos.keySet()) {
            List<Double> targetSpecs = targetsPos.get(targetId);
            Double pX = targetSpecs.get(0);
            Double pY = targetSpecs.get(1);
            if((x >= pX - 100 && x <= pX + 200) && (y >= pY - 100 && y <= pY + 200)){
                isWithinA = false;
            }
        }

        wallsPos = oWalls.returnWalls();
        for (Integer wallId : wallsPos.keySet()) {
            List<Double> wallSpecs = wallsPos.get(wallId);
            Double pX = wallSpecs.get(0);
            Double pY = wallSpecs.get(1);
            if((x >= pX - 40 && x <= pX + 80) && (y >= pY - 40 && y <= pY + 80)){
                isWithinA = false;
            }
        }

        return isWithinA;
    }

    /**
     * Sets controller for GUI manipulation.
     *
     * @param arenaController given controller
     */
    public void setController(ArenaController arenaController) {
        this.arenaController = arenaController;
    }

}