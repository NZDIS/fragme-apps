package org.nzdis.robogame;

import java.awt.Graphics;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Base class for all moveable objects in RoboJoust. In addition to the
 * GameObject variables and methods, it includes the previous position of an
 * object oldX and oldY as well as setup() methods to set these values. Setup
 * here also checkes whether the new position of an object is still null.
 */

public class Moveable
    extends GameObject {

  /** previous position of objects, initially -1, set to the start position when
   * an object is created and setup for the first time
   */
  protected int oldX = -1;
  protected int oldY = -1;

  /* current step position */
  protected int stepX = 4;
  protected int stepY = 4;
  protected int totalStepX = 4;
  protected int totalStepY = 4;
  protected int stepIndicatorX = 1;
  protected int stepIndicatorY = 1;


  /**
   * main paint method for GameObject. Is called by RoboGame.paint() and paints
   * an object at its current position.
   *
   * @param g Graphics
   */
  public void paint(Graphics g) {

    if (image != null) {
      int aY = map[0].length - (y + 1);
      g.drawImage(image, ((x - stepIndicatorX) * tileSize ) + ((tileSize / totalStepX) * stepX),
                  ((aY - stepIndicatorY)* tileSize) + ((tileSize / totalStepY) * stepY), this);
    }else{
      System.out.println(this+"'s image is null");
    }
  }


  /**
   * This setup checks whether the new position of a moveable object is still
   * free. If yes, it calls super.setup() with the new x and y, otherwise with
   * the old values (the object does not move then). It also keeps track of the\
   * old position when an object moves.
   * This setup is only used in case of an actual move (keyPressed), to move an
   * object on another peer use remoteSetup().
   *
   * @param x int
   * @param y int
   */
  public void localSetup(int x, int y) {
    if (oldX == -1)
      oldX = x;
    if (oldY == -1)
      oldY = y;
    oldX = super.x;
    oldY = super.y;

    if (x != oldX || y != oldY) {
      if (map[x][y] != null) {
        x = super.x;
        y = super.y;
        oldX = x;
        oldY = y;
      }
      else {
        oldX = super.x;
        oldY = super.y;
      }
    }
    super.setup(x, y);
  }

  /**
   * remoteSetup() is called by the changedObject() method of an moveable
   * object and is used to setup an object at its new position and to clear the
   * old position.
   */
  /* this may be duplicated with what deserialized method does */
  public void remoteSetup() {
    if (oldX == -1)
      oldX = x;
    if (oldY == -1)
      oldY = y;

    map[oldX][oldY] = null;

    super.setup(x, y);
  }

  /**
   * move an object left
   */
  public void moveLeft() {
    if (x > 0) { //moving for bombs should be different
      if (map[x - 1][y] == null) {
        remove();
        localSetup(x - 1, y);
      }
      else {
        stepX = Math.abs(totalStepX);
        stepY = Math.abs(totalStepY);
        setup(x, y);
      }
    }
  }

  /**
   * move an object right
   */
  public void moveRight() {
    if (x < map.length - 1) { //moving for bombs should be different
      if (map[x + 1][y] == null) {
        remove();
        localSetup(x + 1, y);
      }
      else {
        stepX = Math.abs(totalStepX);
        stepY = Math.abs(totalStepY);
        setup(x, y);
      }
    }
  }

  /**
   * move an object up
   */
  public void moveUp() {
    if (y < map[0].length - 1) { //moving for bombs should be different
      if (map[x][y + 1] == null) {
        remove();
        localSetup(x, y + 1);
      }
      else {
        stepX = Math.abs(totalStepX);
        stepY = Math.abs(totalStepY);
        setup(x, y);
      }
    }
  }

  /**
   * move an object down
   */
  public void moveDown() {
    if (y > 0) { //moving for bombs should be different
      if (map[x][y - 1] == null) {
        remove();
        localSetup(x, y - 1);
      }
      else {
        stepX = Math.abs(totalStepX);
        stepY = Math.abs(totalStepY);
        setup(x, y);
      }
    }
  }

  /**
   * trigger an object action
   */
  public void action() {
    setup(x,y);
  }
}
