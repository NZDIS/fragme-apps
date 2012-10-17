package org.nzdis.robogame;

import org.nzdis.fragme.objects.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Base class for all RoboJoust objects. Offers main functions setup(), remove(),
 * loadImage(), paint() as well as the x,y tile position of objects.
 * GameObject inherits FMeObject, so the serialize() and deserialize() methods have
 * to be implemented.
 */
public class GameObject
    extends FMeObject
    implements ImageObserver {

  /* current position of an object in the 2D array */
  /* note: the 2D symbolizes tiles on map */
  protected int x;
  protected int y;
  protected int aY;

  /**
   * two integers used for iterating through for loops
   * avoid local variable creation,maximize memory usage
   */
  protected int i;
  protected int j;

  /**
   * variables to track loading of images
   */
  private static int imageID = 0;

  /**
   * a media tracker used to load images for all objects
   */
  public static MediaTracker mediaTracker;

  /**
   * references to the game and the game map
   */
  public static RoboGame game;
  public static Object[][] map;

  /**
   * the side size of a "tile" or "grid" in robo game
   */
  protected static int tileSize;

  /**
   * the image of the game object
   */
  public Image image;

  /**
   * Constructor
   */
  public GameObject() {
  }

  /**
   * Controlled loading of images. Function uses MediaTracker to wait until an
   * image is completely loaded. Should be called by any object inherited from
   * GameObject after calling getImage() to avoid half-loaded images.
   */
  protected static void loadImage(Image image){
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
    game.repaint();
  }

  /**
   * remove an object from the game map. This does NOT remove the object itself,
   * still delete() has to be called.
   */
  public void remove() {
    map[x][y] = null;
  }

  /**
   * set size of game tiles
   */
  public static void setTileSize(int tileSize) {
    GameObject.tileSize = tileSize;
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
      g.drawImage(image, x * tileSize ,
                  aY * tileSize, this);
    }else{
      System.out.println(this+"'s image is null");
    }
  }

  /**
   * creates the game map container that holds the position of all objects
   *
   * @param xMax int
   * @param yMax int
   * @return GameObject[][]
   */
  public static Object[][] createMap(int xMax, int yMax) {
    if (map == null) {
      map = new Object[xMax][yMax];
    }
    return map;
  }

  /**
   * deserialize -- has to be implemented due to FMeObject. Left empty here,
   * should be implemented by the actual object (e.g., Robo)
   *
   * @param serObject FMeSerialised
   */
  public void deserialize(FMeSerialised serObject) {
  }

  /**
   * getSerializedObjectClassName -- has to be implemented due to FMeObject.
   * Left empty here, should be implemented by the actual object (e.g., Robo)
   *
   * @return Class
   */
  public Class getSerializedObjectClassName() {
    return this.getClass();
  }

  /**
   * serialize -- has to be implemented due to FMeObject. Left empty here,
   * should be implemented by the actual object (e.g., Robo)
   *
   * @param ser FMeSerialised
   * @return FMeSerialised
   */
  public FMeSerialised serialize(FMeSerialised ser) {
    return null;
  }

  /**
   * changedObject --   has to be implemented due to FMeObject. Left empty here
   * should be implemented by the actual object (e.g., Robo)
   * @see FMeObject.changedObject()
   */
  public void changedObject() {
  }

  /**
   * as declared in the FMeObject interface
   * @see FMeObject.deleteObject()
   */
  public void deletedObject() {

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
