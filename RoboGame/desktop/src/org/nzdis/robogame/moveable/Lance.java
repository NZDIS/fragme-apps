package org.nzdis.robogame.moveable;

import org.nzdis.robogame.*;

import java.awt.*;
import org.nzdis.fragme.factory.*;
import org.nzdis.fragme.objects.*;
import org.nzdis.fragme.ControlCenter;
import org.nzdis.robogame.nonmoveable.Pool;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Class of the Lance. Animation similar to bomb.
 */
public class Lance
    extends Moveable {

  private final static Image[] lanceImages = new Image[4];
  private volatile boolean positionSet = false;
  private volatile boolean threadSuspended;

  private int power = 15;

  //for blocked,0 means unblocked,1 means blocked
  private int blocked = 0;
  private int[][] dir = {
      {
      0, 1}
      , {
      0, -1}
      , {
       -1, 0}
      , {
      1, 0}
  };
  private double[] travelled;

  private int orientation;

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
    int xx = 0;
    int yy = 0;

    public AnimationThread() {
      start();
    }

    public void wakeUp() {
      synchronized (t) {
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

        xlen = map.length;
        ylen = map[0].length;
        nextStepX = 0;
        nextStepY = 0;

        //draw explosion
        //canTravel 0 means can,1 means can't
        while (true) {
          if (blocked == 1)
            break;

          xx = x + (int) travelled[0] + dir[orientation][0];
          yy = y + (int) travelled[1] + dir[orientation][1];

          if (! (xx > 0 && xx < xlen - 1 && yy > 0 && yy < ylen - 1)) {

            break;
          }

          //draw 1/4 of a image each time,nicer animation
          for (i = 1; i <= 4; i++) {
            travelled[0] += dir[orientation][0] / 4.0;
            travelled[1] += dir[orientation][1] / 4.0;

            game.repaint();
            try {
              sleep(20);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          } //finish drawing

          //need to check if it bumps into anything next
          nextStepX = x + (int) travelled[0];
          nextStepY = y + (int) travelled[1];
          Object obj = map[nextStepX][nextStepY];

          //test for object on map
          if (obj != null) {
            if (obj instanceof Pool) {
              blocked = 0;
            }
            else {
              if (obj instanceof Robo) {
                if (getOwnerAddr().equals(ControlCenter.getMyAddress())) {
                  ( (Robo) obj).getLanced(power);
                }
              }
              blocked = 1;
            }
          }
          try {
            sleep(20);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        } //end while true

        //only call delete if it's my own lance
        if (getOwnerAddr().equals(ControlCenter.getMyAddress())) {
          game.totalLances--;
          delete();
        }
        threadSuspended = true;
        RoboGame.runGC();
      } //end while(true)
    } //end run
  }

  /**
   * Static image loader to save memory.
   */
  static {
    for (int i = 0; i < 4; i++) {
      lanceImages[i] = Toolkit.getDefaultToolkit().getImage(
          "res/images/lance" + i + ".png");
      loadImage(lanceImages[i]);
    }
  }

  /**
   * the constructor.
   */
  public Lance() {
    threadSuspended = true;
    positionSet = false;
    t = new AnimationThread();
  }

  /**
   * paint method for lance.
   */
  public void paint(Graphics g) {
    if (image != null) {

      int bX = 0;
      int bY = 0;

      if (blocked == 0 && positionSet) { //should still draw
        bX = (int) ( (x + travelled[0]) * tileSize);
        bY = (int) ( (map[0].length - (y + travelled[1] + 1)) * tileSize);
        g.drawImage(image, bX, bY, this);
      }
    }
    else {
      System.out.println(this +"'s image is null for " + this);
    }
  }

  /**
   * setup lance at initial position. it flys from here.
   */
  public void setup(int x, int y, int orientation) {
    this.orientation = orientation;
    this.blocked = 0;
    this.travelled = new double[2];
    this.image = lanceImages[orientation];
    positionSet = true;
    super.setup(x, y);

    threadSuspended = false;

    /* wake up lance animation thread to make it fly */
    t.wakeUp();
  }

  public void deletedObject() {
    positionSet = false;
    game.repaint();
  }

  public Class getSerializedObjectClassName() {
    return Lance_ser.class;
  }

  // Classes that must be implemented
  public FMeSerialised serialize(FMeSerialised lanceSer) {
    try {
      ( (Lance_ser) lanceSer).setPositionSet(this.positionSet);
      ( (Lance_ser) lanceSer).setX(this.x);
      ( (Lance_ser) lanceSer).setY(this.y);
      ( (Lance_ser) lanceSer).setOrientation(this.orientation);

      return lanceSer;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public void deserialize(FMeSerialised serObject) {
    if ( ( (Lance_ser) serObject).getPositionSet() == true) {
      this.setup( ( (Lance_ser) serObject).getX(),
                 ( (Lance_ser) serObject).getY(),
                 ( (Lance_ser) serObject).getOrientation());
    }
    else {
      //do nothing
    }
  }

// the factory
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Lance();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), Lance.class);
  }

}
