package org.nzdis.robogame;

import java.awt.*;
import java.awt.image.*;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Base class for all background objects. Does not implement FMeObject, because
 * background objects should be created on every peer and not send (to save
 * memory and bandwidth)
 */
public class BackgroundObject
    implements ImageObserver {

  /* current position of an object in the 2D array */
  /* note: the 2D symbolizes tiles on map */
  protected int x;
  protected int y;
  protected int aY;


  /**
   * variables to track loading of images
   */
  private static int imageID = 0;
  public static MediaTracker mediaTracker;

  /**
   * references to the game and the game map
   */
  public static RoboGame game;
  public static Object[][] map;

  protected static int tileSize = 40;
  public Image image;

  /**
   * Constructor
   */
  public BackgroundObject() {

  }

  public static void init(Object[][] gameMap, RoboGame roboGame) {
    map = gameMap;
    game = roboGame;
  }

  /**
   * Controlled loading of images. Function uses MediaTracker to wait until an
   * image is completely loaded. Should be called by any object inherited from
   * GameObject after calling getImage() to avoid half-loaded images.
   */
  protected static void loadImage(Image image) {
    mediaTracker.addImage(image, imageID);
    try {
      mediaTracker.waitForID(imageID);
    }
    catch (InterruptedException ie) {
      System.err.println(ie);
      System.exit(1);
    }
    imageID++;
  }

  /**
   * Set up an object on the map at the tile position x, y,
   * then repaint the game map.
   *
   * @param x int
   * @param y int
   */
  public void setup(int x, int y) {
    this.x = x;
    this.y = y;
    map[x][y] = (Object) this;

    game.reconstructBG(true);
    game.repaint();

  }

  /**
   * main paint method for GameObject. Is called by RoboGame.paint() and paints
   * an object at its current position.
   *
   * @param g Graphics
   */
  public void paint(Graphics g) {

    if (image != null) {
      aY = map[0].length - (y + 1);
      g.drawImage(image,
                  x * tileSize,
                  aY * tileSize, this);
    }
    else {
      System.out.println(this +"'s image is null");
    }
  }

  /**
   * imageUpdate -- what is this used for???
   *
   * @param img Image
   * @param infoflags int
   * @param x int
   * @param y int
   * @param width int
   * @param height int
   * @return boolean
   */
  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width,
                             int height) {
    return true;
  }
}
