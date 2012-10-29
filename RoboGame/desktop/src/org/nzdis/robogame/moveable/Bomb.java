package org.nzdis.robogame.moveable;

import org.nzdis.robogame.*;

import java.awt.*;
import org.nzdis.fragme.factory.*;
import org.nzdis.fragme.objects.*;
import org.nzdis.fragme.ControlCenter;
import org.nzdis.robogame.nonmoveable.FalseBrick;

/**
* @author GAME DEVELOPMENT TEAM ONE INFO401 2004
* GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
*                                Mengqiu Wang
*                                Lincoln Johnston
*                                Hamed Hawwari
*
* Class of the Bomb.
*/
public class Bomb extends Moveable{

  private static Image bombImage = null;
  private final static Image[] explosionImages = new Image[4];
  private volatile boolean positionSet = false;

  /**
   * animation settings
   */
  private volatile boolean threadSuspended;
  private volatile boolean animating = false;
  private int timestamp = 0;
  private int timeLeft;

  /* the longer the initial timestamp,the greater the power */
  private int power = 0;

  /* the color of the bomb's timer --- same as the Robo's health bar */
  private Color bombColor = null;

  /**
   * blocked describes whether the bomb's explosion has hit an obstacle
   * 0 means unblocked, 1 means blocked
   */
  private int[] blocked = new int[4];

  /**
   * direction and travelled way arrays
   */
  private int[][] dir = {{1,0},{0,1},{-1,0},{0,-1}};
  private double[][] travelled;

  /**
   * the bomb's animation thread
   */
  private AnimationThread t;


  /**
   * Bomb animation thread class
   * Inner class of Bomb
   */
  class AnimationThread
      extends Thread {
    int xlen = 0;
    int ylen = 0;
    int nextStepX = 0;
    int nextStepY = 0;

    /**
     * draw explosion
     * canTravel 0 means can,1 means can't
     */
    int[] canTravel = new int[4];
    boolean done = true;
    int xx = 0;
    int yy = 0;

    public AnimationThread() {
      start();
    }

    public void wakeUp() {
      synchronized(t) {
        t.notify();
      }
    }

    public void run() {
      while (true) {
        try {
          if (threadSuspended) {
            synchronized (t) {
              while (threadSuspended)
                t.wait();
            }
          }
        }
        catch (InterruptedException e) {
        }

        /**
         * make a move in x direction (left / right)
         */
        for (;timeLeft >= 0; timeLeft--) {
          game.repaint();
          try {
            sleep(1000);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        animating = true;
        xlen = map.length;
        ylen = map[0].length;
        nextStepX = 0;
        nextStepY = 0;

        //draw explosion
        //canTravel 0 means can,1 means can't
        canTravel = new int[4];
        done = true;

        while(true){
          done = true;
          for(int i=0;i<4;i++){
            xx = x+(int)travelled[i][0]+dir[i][0];
            yy = y+(int)travelled[i][1]+dir[i][1];
            if(blocked[i] == 0 && xx > 0 && xx < xlen-1 && yy > 0 && yy < ylen-1){
              done = false;
              canTravel[i] = 0;
            }else{
              canTravel[i] = 1;
            }
          }
          if(done)
            break;
          else{
            System.arraycopy(canTravel,0,blocked,0,blocked.length);
            //draw 1/4 of a image each time,nicer animation
            for(i=1;i<=4;i++){
              for(j=0;j<4;j++){
                if(blocked[j] == 0){//can travel
                  travelled[j][0] += dir[j][0]/4.0*1;
                  travelled[j][1] += dir[j][1]/4.0*1;
                }
              }
              game.repaint();
              try {
                sleep(20);
              }
              catch (Exception e) {
                e.printStackTrace();
              }
            }//finish drawing
          }

          //need to check if it bumps into anything next
          for(i=0;i<4;i++){
            if(blocked[i] == 0){
              nextStepX = x+(int)travelled[i][0];
              nextStepY = y+(int)travelled[i][1];
              Object obj = map[nextStepX][nextStepY];
              if(obj != null){
                if(obj instanceof Robo){
                  if (getOwnerAddr().equals(ControlCenter.getMyAddress())) {
                    ( (Robo) obj).getBombed(power);
                  }
                }else if(obj instanceof FalseBrick){
                  ((FalseBrick) obj).delete();
                }
                blocked[i] = 1;
              }
            }
          }
          try {
            sleep(20);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }//end while true

        //only call delete if it's my own bomb
        if(getOwnerAddr().equals(ControlCenter.getMyAddress())){
          game.totalBombs--;
          delete();
        }
        threadSuspended = true;
        RoboGame.runGC();
      }//end while(true)
    }//end run
  }

  /**
   * static image loader to save memory
   */
  static{
      bombImage = Toolkit.getDefaultToolkit().getImage(
      "res/images/Bomb.png");
      loadImage(bombImage);
      for(int i=0;i<4;i++){
          explosionImages[i] = Toolkit.getDefaultToolkit().getImage(
      "res/images/Explosion"+i+".png");
        loadImage(explosionImages[i]);
      }
    }

    /**
     * constructor of bomb
     */
    public Bomb() {
    threadSuspended = true;
    t = new AnimationThread();
    image = bombImage;
  }

  public void paint(Graphics g) {

    if (image != null) {
      aY = map[0].length - 1 - y;

      int bX = ( (x - stepIndicatorX) * tileSize) +
          ( (tileSize / totalStepX) * stepX);
      int bY = ( (aY - stepIndicatorY) * tileSize) +
          ( (tileSize / totalStepY) * stepY);
      if (animating) {
        for (i = 0; i < 4; i++) {
          if (blocked[i] == 0) { //should still draw
            bX = (int) ( (x + travelled[i][0]) * tileSize);
            bY = (int) ( (map[0].length - 1 - (y + travelled[i][1])) * tileSize);
            g.drawImage(explosionImages[i], bX, bY, this);
          }
        }
      }
      else if (positionSet) {
        g.drawImage(image, bX, bY, this);
        g.setColor(bombColor);
        g.drawString(String.valueOf(timeLeft), bX, bY - tileSize / 8);
      }
    }
    else {
    }
  }


  public void setup(int x,int y,int timestamp,Color playerColor) {

    this.blocked = new int[4];
    this.travelled = new double[4][2];
    this.image = bombImage;
    this.power = timestamp*3;
    this.timestamp = timestamp;
    this.timeLeft = timestamp;
    this.animating = false;
    positionSet = true;
    this.bombColor = playerColor;
    super.setup(x,y);

    threadSuspended = false;
    t.wakeUp();
  }

  public void deletedObject() {
    positionSet = false;
    game.repaint();
  }

  public Class getSerializedObjectClassName() {
    return Bomb_ser.class;
  }

  // Classes that must be implemented
  public FMeSerialised serialize(FMeSerialised bombSer) {
    try {
      ( (Bomb_ser) bombSer).setPositionSet(this.positionSet);
      ( (Bomb_ser) bombSer).setX(this.x);
      ( (Bomb_ser) bombSer).setY(this.y);
      ( (Bomb_ser) bombSer).setTimestamp(this.timestamp);
      ( (Bomb_ser) bombSer).setColor(this.bombColor);

      return bombSer;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


  public void deserialize(FMeSerialised serObject) {
    if(( (Bomb_ser) serObject).getPositionSet() == true){
      this.setup( ((Bomb_ser) serObject).getX(),( (Bomb_ser) serObject).getY(),
                 ((Bomb_ser) serObject).getTimestamp(),((Bomb_ser) serObject).getColor());
    }else{
      //do nothing
    }
  }


// the factory
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Bomb();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), Bomb.class);
  }

}
