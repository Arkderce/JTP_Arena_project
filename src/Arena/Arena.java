
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
    double posX = 600;
    double posY = 400;
    double mposX = 0;
    double mposY = 0;
    double angle = 0;

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
        arenaController.changeHealth((int) health);
    }

    double health = 100;
    boolean running, goNorth, goSouth, goEast, goWest;

    HashMap<Integer, List<Double>> bullets = new HashMap<Integer, List<Double>>();
    HashMap<Integer, List<Double>> targetsPos = new HashMap<Integer, List<Double>>();
    HashMap<Integer, List<Double>> wallsPos = new HashMap<Integer, List<Double>>();
    HashMap<Integer, Double> targetsHealth = new HashMap<Integer, Double>();

    Bullets oBullets = new Bullets();
    Targets oTargets = new Targets();
    Walls oWalls = new Walls();
    ArenaController arenaController;


    public void start(Canvas theStage, AnchorPane anchorPane){

        GraphicsContext gc = theStage.getGraphicsContext2D();

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


        theStage.setOnMouseMoved(event -> {
            if(health > 0) {
                mposX = (int) event.getX();
                mposY = (int) event.getY();
            }
        });

        theStage.setOnMousePressed(event -> {
            if(health > 0) {
                //connectorClient.changeStr("PLAYER:SHOOT:" + (posX + 17) + ":" + (posY + 17) + ":" + angle);
                oBullets.createBullet(myId, posX+17, posY+17, angle);
            }
        });

        theStage.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
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
                System.out.println(event.getCode());
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
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                double x = 232 + 128 * Math.cos(t);
                double y = 232 + 128 * Math.sin(t);

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
                    gc.drawImage( gameover, 350, 450);

                }

                if(targetsPos.size() == 0){
                    currentNumOfTargets = 0;
                    Random rand = new Random();
                    numberOfMaxTargets = rand.nextInt(20) + 5;
                }

                if(currentNumOfTargets < numberOfMaxTargets) {
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
                //drawEnemyGuns(gc);

                targetsPos = oTargets.returnTargetsPos();
                drawTargets(gc);




                oBullets.moveBullets(oTargets);
                bullets = oBullets.returnBullets();
                drawBullets(gc);

                if(dx != 0 || dy != 0) movePlayer(dx, dy);

            }
        }.start();
    }

    private double countAngle(double spX, double spY, double mpX, double mpY){
        double xDistance = mpX - spX;
        double yDistance = mpY - spY;
        return Math.toDegrees(Math.atan2(yDistance, xDistance));
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save();
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore();
    }

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

    private void drawTargets(GraphicsContext gc){
        for (Integer key : targetsPos.keySet()) {
            gc.drawImage( target , targetsPos.get(key).get(0), targetsPos.get(key).get(1) );
        }
    }

    private void movePlayer(double x, double y){
        double newposX = x+posX;
        double newposY = y+posY;
        if(newposX <= Wi-40 && newposX >= 0 && newposY <= He-40 && newposY >= 0){
            posY = newposY;
            posX = newposX;
        }
    }

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
        return isWithinA;
    }

    public void setController(ArenaController arenaController) {
        this.arenaController = arenaController;
    }

}