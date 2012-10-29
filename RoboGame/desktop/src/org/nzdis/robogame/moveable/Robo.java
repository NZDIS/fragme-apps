package org.nzdis.robogame.moveable;

import org.nzdis.robogame.*;
import org.nzdis.robogame.nonmoveable.*;

import java.awt.Toolkit;
import java.awt.Image;
import java.lang.Math;

import org.nzdis.fragme.factory.*;
import org.nzdis.fragme.objects.FMeSerialised;
import java.util.Random;
import org.nzdis.robogame.consumeable.HealthP;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import org.nzdis.fragme.ControlCenter;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Class of the Robo, the player's character in RoboJoust. Implements all the
 * moving and action logic for the Robos as well as the FMeObject methods.
 */
public class Robo
    extends Moveable {

  /**
   * Font is used to draw the robo's name and life on top of the robo.
   */
  private static Font font = new Font(null, 0, 10);

  /**
   * AnimationThread for running the robos moving animation
   */
  private AnimationThread t;
  private LanceTimerThread lanceT;

  /**
   * Maximum health of a Robo -- fixed for the moment
   */
  private int maximumHealth = 100;

  /**
   * total lives for a Robo
   */
  private int totalLives = 3;

  /**
   * number of lives set at 3 for the moment
   */
  private int noOfLives;

  /**
   * The health level, initialized at 100 (for the moment)
   */
  private int health;

  //this direction holds the direction of where the hitting is coming from
  private int direction = -1;

  /**
   * defines static variables for different orientations to load images
   * accordingly
   */
  private static final int IMG_OR_UP = 0;
  private static final int IMG_OR_DOWN = 1;
  private static final int IMG_OR_LEFT = 2;
  private static final int IMG_OR_RIGHT = 3;

  private static final int IMG_HIT_UP = 4;
  private static final int IMG_HIT_DOWN = 5;
  private static final int IMG_HIT_LEFT = 6;
  private static final int IMG_HIT_RIGHT = 7;

  private static final int IMG_WALK_UP_1 = 8;
  private static final int IMG_WALK_DOWN_1 = 9;
  private static final int IMG_WALK_LEFT_1 = 10;
  private static final int IMG_WALK_RIGHT_1 = 11;

  private static final int IMG_WALK_UP_2 = 12;
  private static final int IMG_WALK_DOWN_2 = 13;
  private static final int IMG_WALK_LEFT_2 = 14;
  private static final int IMG_WALK_RIGHT_2 = 15;

  private int imgOrientation = IMG_OR_UP;

  private volatile boolean move = false;
  public volatile boolean moveDone = true;

  public volatile boolean isThrown = false;

  /** control for animation thread */
  private volatile boolean threadSuspended;

  /* control for lance timing thread */
  private volatile boolean lanceThreadSuspended;

  /* health bar color to distinguish colors */
  private Color healthBarColor;

  /*
   * once loaded, save images in this array to access them there
   */
  private final static Image localImg[] = new Image[16];
  private String peerName;

  /** static block that loads all the images */
  static {

    Image image = null;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Not_Hitting_Left_ws.png");
    loadImage(image);
    localImg[IMG_OR_LEFT] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Not_Hitting_Right_ws.png");
    loadImage(image);
    localImg[IMG_OR_RIGHT] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Not_Hitting_Down_ws.png");
    loadImage(image);
    localImg[IMG_OR_DOWN] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Not_Hitting_ws.png");
    loadImage(image);
    localImg[IMG_OR_UP] = image;

    // Robo hitting
    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Hitting_Left_ws.png");
    loadImage(image);
    localImg[IMG_HIT_LEFT] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Hitting_Right_ws.png");
    loadImage(image);
    localImg[IMG_HIT_RIGHT] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Hitting_Down_ws.png");
    loadImage(image);
    localImg[IMG_HIT_DOWN] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Hitting_ws.png");
    loadImage(image);
    localImg[IMG_HIT_UP] = image;

    // walking images
    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking1_Left.png");
    loadImage(image);
    localImg[IMG_WALK_LEFT_1] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking2_Left.png");
    loadImage(image);
    localImg[IMG_WALK_LEFT_2] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking1_Right.png");
    loadImage(image);
    localImg[IMG_WALK_RIGHT_1] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking2_Right.png");
    loadImage(image);
    localImg[IMG_WALK_RIGHT_2] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking1_Down.png");
    loadImage(image);
    localImg[IMG_WALK_DOWN_1] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking2_Down.png");
    loadImage(image);
    localImg[IMG_WALK_DOWN_2] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking1.png");
    loadImage(image);
    localImg[IMG_WALK_UP_1] = image;

    image = Toolkit.getDefaultToolkit().getImage(
        "res/images/Robo_Walking2.png");
    loadImage(image);
    localImg[IMG_WALK_UP_2] = image;
  }

  /**
   * Constructor
   */
  public Robo() {
    image = localImg[IMG_OR_UP];
    this.peerName = game.peername;

    // start animation thread
    threadSuspended = true;
    t = new AnimationThread();

    // start lance timer
    lanceThreadSuspended = true;
    lanceT = new LanceTimerThread();

    // set lives and health
    noOfLives = totalLives;
    health = maximumHealth;
  }

  /**
   * getters / setters for the health bar color
   */
  public Color getHealthBarColor() {
    return healthBarColor;
  }

  public void setHealthBarColor(Color c) {
    healthBarColor = c;
  }

  /**
   * set lives back to max in case of dying / restart
   */
  public void setLivesBackToMax() {
    noOfLives = totalLives;
    health = maximumHealth;
  }

  /**
   * Method for changing health, taking into account whether we have died or
   * are at maximum health already
   *
   * nb. use negative to lower hp
   */
  public void changeHealth(int amount) {
    int newHealth = health + amount;
    if (newHealth <= 0) {
      this.loseLife();
    }

    // this is if you pick up a normal health bottle, later there could be
    // other health bottles that extend your maximum health
    else if (newHealth > maximumHealth) {
      health = maximumHealth;
      change();
    }
    else {
      health = newHealth;
      change();
    }
  }

  /**
   * If a robo gets hit, he gets pushed one tile in the direction from where he
   * is hit (i.e., if he gets hit from the right, he is pushed away from his
   * opponent to the left).
   */
  public void getHit(int orientation, int damage) {
    if (orientation == IMG_OR_RIGHT || orientation == IMG_WALK_RIGHT_1 ||
        orientation == IMG_WALK_RIGHT_2 || orientation == IMG_HIT_RIGHT) {
      direction = IMG_OR_RIGHT;
    }
    else if (orientation == IMG_OR_LEFT || orientation == IMG_WALK_LEFT_1 ||
             orientation == IMG_WALK_LEFT_2 || orientation == IMG_HIT_LEFT) {
      direction = IMG_OR_LEFT;
    }
    else if (orientation == IMG_OR_UP || orientation == IMG_WALK_UP_1 ||
             orientation == IMG_WALK_UP_2 || orientation == IMG_HIT_UP) {
      direction = IMG_OR_UP;
    }
    else if (orientation == IMG_OR_DOWN || orientation == IMG_WALK_DOWN_1 ||
             orientation == IMG_WALK_DOWN_2 || orientation == IMG_HIT_DOWN) {
      direction = IMG_OR_DOWN;
    }
    changeHealth( -damage);
    game.runGC();
  }

  /**
   * get hit by a bomb - just decrease health.
   * @param damage int
   */
  public void getBombed(int damage) {

    //we can add splash or something here
    changeHealth( -damage);
  }

  /**
   * get hit by a lance - just decrease health.
   * @param damage int
   */
  public void getLanced(int damage) {

    //we can add splash or something here
    changeHealth( -damage);
  }

  /**
   * remove a life from the robo
   */
  public void loseLife() {
    noOfLives--;
    health = maximumHealth;

    /**
     * if all lives are lost, robo dies.
     */
    if (noOfLives < 0) {
      this.die();
    }
    change();
  }

  /**
   * out of lives so robo has lost the game
   */
  public void die() {
    // tell the other peer that i've lost
    if (this.getOwnerAddr().equals(ControlCenter.getMyAddress())) {
      Message m = (Message) ControlCenter.createNewObject(Message.class);
      m.setPerformative(game.WIN_COND);
      m.setContent(game.LOOSE);
      m.change();

      game.endGame(false);
      this.delete();
    }
  }

  /**
   * got a message that we won the game
   */
  public void win() {
    game.endGame(true);
  }

  /**
   * Robo timer class to measure lance timing
   * Inner class of Robo
   */
  class LanceTimerThread
      extends Thread {

    public LanceTimerThread() {
      start();
    }

    public void changeState() {
      synchronized (lanceT) {
        lanceT.notify();
      }
    }

    public void run() {
      while (true) {
        try {
          if (lanceThreadSuspended) {
            synchronized (lanceT) {
              while (lanceThreadSuspended)
                lanceT.wait();
            }
          }
        }
        catch (InterruptedException e) {
        }

        isThrown = true;
        try {
          lanceT.sleep(1000);
        }
        catch (InterruptedException ex) {
          ex.printStackTrace();
        }
        lanceThreadSuspended = true;
        isThrown = false;
      }
    }
  }

  /**
   * Robo animation thread class
   * Inner class of Robo
   */
  class AnimationThread
      extends Thread {

    boolean setX = false;
    boolean setY = false;

    public AnimationThread() {
      start();
    }

    public void changeState(boolean setX, boolean setY) {
      this.setX = setX;
      this.setY = setY;
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

        /**
         * make a move in x direction (left / right)
         */
        if (setX) {
          moveDone = false;
          stepY = Math.abs(totalStepY);
          for (i = 1; i < Math.abs(totalStepX); i++) {
            stepX = i;
            /**
             * load images for a right move
             */
            if (imgOrientation == IMG_OR_RIGHT ||
                imgOrientation == IMG_WALK_RIGHT_1 ||
                imgOrientation == IMG_WALK_RIGHT_2) {
              if ( (i % 2) == 0) {
                imgOrientation = IMG_WALK_RIGHT_1;
              }
              if ( (i % 2) == 1) {
                imgOrientation = IMG_WALK_RIGHT_2;
              }
              image = localImg[imgOrientation];
            }
            /**
             * load images for a left move
             */
            if (imgOrientation == IMG_OR_LEFT ||
                imgOrientation == IMG_WALK_LEFT_1 ||
                imgOrientation == IMG_WALK_LEFT_2) {
              if ( (i % 2) == 0) {
                imgOrientation = IMG_WALK_LEFT_1;
              }
              if ( (i % 2) == 1) {
                imgOrientation = IMG_WALK_LEFT_2;
              }
              image = localImg[imgOrientation];
            }
            setup(x, y);
            try {
              sleep(100);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
          // load image for standing robo facing right
          if (imgOrientation == IMG_WALK_RIGHT_2) {
            image = localImg[IMG_OR_RIGHT];
            imgOrientation = IMG_OR_RIGHT;
          }
          // load image for standing robo facing left
          if (imgOrientation == IMG_WALK_LEFT_2) {
            image = localImg[IMG_OR_LEFT];
            imgOrientation = IMG_OR_LEFT;
          }
          stepX = Math.abs(totalStepX);
          setup(x, y);
          threadSuspended = true;
          moveDone = true;
        }

        /**
         * make a move in y direction (up / down)
         */
        else if (setY) {
          moveDone = false;
          stepX = Math.abs(totalStepX);
          for (i = 1; i < Math.abs(totalStepY); i++) {
            stepY = i;

            /**
             * load images for a up move
             */
            if (imgOrientation == IMG_OR_UP || imgOrientation == IMG_WALK_UP_1 ||
                imgOrientation == IMG_WALK_UP_2) {
              if ( (i % 2) == 0) {
                imgOrientation = IMG_WALK_UP_1;
              }
              else if ( (i % 2) == 1) {
                imgOrientation = IMG_WALK_UP_2;
              }
              image = localImg[imgOrientation];
            }
            /**
             * load images for a down move
             */
            if (imgOrientation == IMG_OR_DOWN ||
                imgOrientation == IMG_WALK_DOWN_1 ||
                imgOrientation == IMG_WALK_DOWN_2) {
              if ( (i % 2) == 0) {
                imgOrientation = IMG_WALK_DOWN_1;
              }
              else if ( (i % 2) == 1) {
                imgOrientation = IMG_WALK_DOWN_2;
              }
              image = localImg[imgOrientation];
            }

            setup(x, y);
            try {
              sleep(100);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
          stepY = Math.abs(totalStepY);

          // load image for standing robo facing up
          if (imgOrientation == IMG_WALK_UP_2) {
            image = localImg[IMG_OR_UP];
            imgOrientation = IMG_OR_UP;
          }
          // load image for standing robo facing down
          if (imgOrientation == IMG_WALK_DOWN_2) {
            image = localImg[IMG_OR_DOWN];
            imgOrientation = IMG_OR_DOWN;
          }
          setup(x, y);
          threadSuspended = true;
          moveDone = true;
        }
      }
    }
  }

// end of animator class

  /**
   * This method checkes the image orientation and loads the according standing
   * or hitting image.
   */
  public void checkOrientation() {
    switch (imgOrientation) {
      case IMG_OR_LEFT:
        image = localImg[IMG_OR_LEFT];
        break;

      case IMG_OR_RIGHT:
        image = localImg[IMG_OR_RIGHT];
        break;

      case IMG_OR_UP:
        image = localImg[IMG_OR_UP];
        break;

      case IMG_OR_DOWN:
        image = localImg[IMG_OR_DOWN];
        break;

      case IMG_HIT_LEFT:
        image = localImg[IMG_HIT_LEFT];
        break;

      case IMG_HIT_RIGHT:
        image = localImg[IMG_HIT_RIGHT];
        break;

      case IMG_HIT_UP:
        image = localImg[IMG_HIT_UP];
        break;

      case IMG_HIT_DOWN:
        image = localImg[IMG_HIT_DOWN];
        break;
    }
  }

  /**
   * Robo moves left
   */
  public void moveLeft() {

    // load image for left move
    if (moveDone) {
      imgOrientation = IMG_OR_LEFT;
      checkOrientation();
    }

    // move
    if (x > 0 && moveDone) {
      Object currentObj = map[x - 1][y];
      if (! (currentObj instanceof WhirlePool)) {

        // if next field is a consumeable, consume it
        if (currentObj instanceof Consumable) {
          Consumable currCon = (Consumable) currentObj;
          if (currCon instanceof HealthP) {
            this.healthPAction();
          }
          currCon.deleteConsumeable();
        }

        stepX = 0;

        if (map[x - 1][y] == null) {
          move = true;
        }
        else {
          move = false;
        }
        super.moveLeft();

        // play an animation
        if (move) {
          threadSuspended = false;
          super.totalStepX = -Math.abs(super.totalStepX);
          super.totalStepY = Math.abs(super.totalStepY);
          super.stepIndicatorX = -1;
          super.stepIndicatorY = 1;
          t.changeState(true, false);
        }
      }

      // if next field is a whirlpool, player falls and is reset somewhere else
      // idea for whirlpool: players could be reseted randomly
      else if (currentObj instanceof WhirlePool) {
        whirlpoolAction();
      }
    }
  }

  /**
   * Robo moves right, functions according to left move
   */
  public void moveRight() {
    if (moveDone) {
      imgOrientation = IMG_OR_RIGHT;
      checkOrientation();
    }

    if (x < map.length - 1 && moveDone) {
      Object currentObj = map[x + 1][y];
      if (! (currentObj instanceof WhirlePool)) {
        if (currentObj instanceof Consumable) {
          Consumable currCon = (Consumable) currentObj;
          if (currCon instanceof HealthP) {
            this.healthPAction();
          }
          currCon.deleteConsumeable();
        }
        stepX = 0;

        if (map[x + 1][y] == null) {
          move = true;
        }
        else {
          move = false;
        }
        super.moveRight();

        // play an animation
        if (move) {
          threadSuspended = false;
          super.totalStepX = Math.abs(super.totalStepX);
          super.totalStepY = Math.abs(super.totalStepY);
          super.stepIndicatorX = 1;
          super.stepIndicatorY = 1;
          t.changeState(true, false);
        }
      }
      else if (currentObj instanceof WhirlePool) {
        whirlpoolAction();
      }
    }
  }

  /**
   * Robo moves up, functions according to left move
   */
  public void moveUp() {
    if (moveDone) {
      imgOrientation = IMG_OR_UP;
      checkOrientation();
    }

    if (y < map[0].length - 1 && moveDone) {
      Object currentObj = map[x][y + 1];
      if (! (currentObj instanceof WhirlePool)) {
        if (currentObj instanceof Consumable) {
          Consumable currCon = (Consumable) currentObj;
          if (currentObj instanceof HealthP) {
            this.healthPAction();
          }
          currCon.deleteConsumeable();
        }
        stepY = 0;

        if (map[x][y + 1] == null) {
          move = true;
        }
        else {
          move = false;
        }
        super.moveUp();

        // play an animation
        if (move) {
          threadSuspended = false;
          super.totalStepX = Math.abs(super.totalStepX);
          super.totalStepY = -Math.abs(super.totalStepY);
          super.stepIndicatorX = 1;
          super.stepIndicatorY = -1;
          t.changeState(false, true);
        }

      }
      else if (currentObj instanceof WhirlePool) {
        whirlpoolAction();
      }
    }
  }

  /**
   * Robo moves down, functions according to left move
   */
  public void moveDown() {
    if (moveDone) {
      imgOrientation = IMG_OR_DOWN;
      checkOrientation();
    }

    if (y > 0 && moveDone) {
      Object currentObj = map[x][y - 1];
      if (! (currentObj instanceof WhirlePool)) {
        if (currentObj instanceof Consumable) {
          Consumable currCon = (Consumable) currentObj;
          if (currentObj instanceof HealthP) {
            this.healthPAction();
          }
          currCon.deleteConsumeable();
        }
        stepY = 0;

        if (map[x][y - 1] == null) {
          move = true;
        }
        else {
          move = false;
        }
        super.moveDown();

        // play an animation
        if (move) {
          threadSuspended = false;
          super.totalStepX = Math.abs(super.totalStepX);
          super.totalStepY = Math.abs(super.totalStepY);
          super.stepIndicatorX = 1;
          super.stepIndicatorY = 1;
          t.changeState(false, true);
        }

      }
      else if (currentObj instanceof WhirlePool) {
        whirlpoolAction();
      }
    }
  }

  /**
   * Robo puts a bomb at where he's currently is
   */
  public void bomb(int timestamp) {
    Bomb bomb = (Bomb) ControlCenter.createNewObject(Bomb.class);
    bomb.setup(this.x, this.y, timestamp, healthBarColor);
    bomb.change();
  }

  /**
   * Robo throws a lance from where he's currently at
   */
  public void throwLance() {
    if (!isThrown) {
      Lance lance = (Lance) ControlCenter.createNewObject(Lance.class);
      lance.setup(this.x, this.y, imgOrientation % 4);
      lance.change();
      lanceThreadSuspended = false;
      lanceT.changeState();
    }
  }

  /**
   * Robo triggers an action (action key pressed)
   * action yet to be implemented
   */
  public void action() {
    // check the orientation, load accoring image
    if (moveDone) {
      switch (imgOrientation) {
        case IMG_OR_UP:
          imgOrientation = IMG_HIT_UP;
          checkOrientation();
          this.hitSomeone();
          break;
        case IMG_OR_DOWN:
          imgOrientation = IMG_HIT_DOWN;
          checkOrientation();
          this.hitSomeone();
          break;
        case IMG_OR_RIGHT:
          imgOrientation = IMG_HIT_RIGHT;
          checkOrientation();
          this.hitSomeone();
          break;
        case IMG_OR_LEFT:
          imgOrientation = IMG_HIT_LEFT;
          checkOrientation();
          this.hitSomeone();
          break;
        default:
          break;
      }

      super.stepX = Math.abs(super.totalStepX);
      super.stepY = Math.abs(super.totalStepY);
      move = false;
      super.action();
    }
  }

  /**
   * Robo action ends (action key released) -- takes back his sword
   */
  public void actionR() {
    switch (imgOrientation) {
      case IMG_HIT_UP:
        imgOrientation = IMG_OR_UP;
        checkOrientation();
        break;
      case IMG_HIT_DOWN:
        imgOrientation = IMG_OR_DOWN;
        checkOrientation();
        break;
      case IMG_HIT_RIGHT:
        imgOrientation = IMG_OR_RIGHT;
        checkOrientation();
        break;
      case IMG_HIT_LEFT:
        imgOrientation = IMG_OR_LEFT;
        checkOrientation();
        break;
    }
    super.action();
  }

  /**
   * Method for telling another peer to take damage
   */
  public void hitSomeone() {

    //firstly need to get a damage amount (partially randon)
    Random r = new Random();
    int d = r.nextInt(5);

    //this means that damage will be between 10 and 15;
    int damage = 15 - d;

    if (imgOrientation == IMG_OR_LEFT || imgOrientation == IMG_HIT_LEFT) {
      if (map[x - 1][y] instanceof Robo) {
        Robo ro = (Robo) map[x - 1][y];
        ro.getHit(imgOrientation, damage);
      }
      else {
        //we are just hitting nothing so don't do anything
      }
    }
    if (imgOrientation == IMG_OR_RIGHT || imgOrientation == IMG_HIT_RIGHT) {
      if (map[x + 1][y] instanceof Robo) {
        Robo ro = (Robo) map[x + 1][y];
        ro.getHit(imgOrientation, damage);
      }
      else {
        //we are just hitting nothing so don't do anything
      }
    }
    if (imgOrientation == IMG_OR_UP || imgOrientation == IMG_HIT_UP) {
      if (map[x][y + 1] instanceof Robo) {
        Robo ro = (Robo) map[x][y + 1];
        ro.getHit(imgOrientation, damage);
      }
      else {
        //we are just hitting nothing so don't do anything
      }
    }
    if (imgOrientation == IMG_OR_DOWN || imgOrientation == IMG_HIT_DOWN) {
      if (map[x][y - 1] instanceof Robo) {
        Robo ro = (Robo) map[x][y - 1];
        ro.getHit(imgOrientation, damage);
      }
      else {
        //we are just hitting nothing so don't do anything
      }
    }
    game.runGC();
  }

  /**
   * Robo's paint method
   * @param g Graphics
   */
  public void paint(Graphics g) {

    if (image != null) {
      aY = map[0].length - (y + 1);

      int bX = ( (x - stepIndicatorX) * tileSize) +
          ( (tileSize / totalStepX) * stepX);
      int bY = ( (aY - stepIndicatorY) * tileSize) +
          ( (tileSize / totalStepY) * stepY);

      g.drawImage(image, bX, bY, this);
      g.setColor(healthBarColor);
      g.drawRect(tileSize / 4 + bX, bY - tileSize / 4, tileSize * 5 / 8,
                 tileSize / 8);
      g.fillRect(tileSize / 4 + bX, bY - tileSize / 4,
                 (int) (tileSize * 5 / 8 * (double) health / maximumHealth),
                 tileSize / 8);
      g.setFont(font);
      g.drawString(String.valueOf(noOfLives), bX, bY - tileSize / 8);
      g.drawString(peerName, bX, bY - tileSize * 3 / 8);
    }
    else {
      System.out.println(this +"'s image is null");
    }
  }

  //-----------------------ITEM/OBJECT ACTIONS---------------------------------

  /**
   * action if a robo falls in the whirlpool. loses life and gets set up at
   * position (1,1)
   */
  public void whirlpoolAction() {
    remove();
    move = false;
    this.localSetup(1, 1);
    if (this.x == 1 && this.y == 1) {
      loseLife();
    }
  }

  /**
   * hp value of each health bottle is currently set,
   * currently set at 30, possibly create, say, healthp2 object for
   * different health value? hmmmm...
   */
  public void healthPAction() {
    changeHealth(maximumHealth);
  }

  //--------------------------------------------------------------------


  //-----------------IMAGE ORIENTATION GETTERS/SETTERS------------------
  public int getImageOrientation() {
    return imgOrientation;
  }

  public void setImageOrientation(int orientation) {
    imgOrientation = orientation;
  }

  //--------------------------------------------------------------------


  // FMeObject Classes that must be implemented
  public FMeSerialised serialize(FMeSerialised roboSer) {
    try {
      ( (Robo_ser) roboSer).setDirection(this.direction);
      ( (Robo_ser) roboSer).setPeerName(this.peerName);

      ( (Robo_ser) roboSer).setX(this.x);
      ( (Robo_ser) roboSer).setY(this.y);

      ( (Robo_ser) roboSer).setOldX(this.oldX);
      ( (Robo_ser) roboSer).setOldY(this.oldY);

      ( (Robo_ser) roboSer).setMove(this.move);
      ( (Robo_ser) roboSer).setOrientation(this.imgOrientation);

      ( (Robo_ser) roboSer).setHealth(this.health);

      ( (Robo_ser) roboSer).setLives(this.noOfLives);

      ( (Robo_ser) roboSer).setMaxHealth(this.maximumHealth);

      ( (Robo_ser) roboSer).setTotalLives(this.totalLives);

      ( (Robo_ser) roboSer).setHealthBarColor(this.healthBarColor);

      return roboSer;
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void deserialize(FMeSerialised serObject) {
    map[x][y] = null;
    this.imgOrientation = ( (Robo_ser) serObject).getOrientation();
    this.move = ( (Robo_ser) serObject).getMove();
    checkOrientation();

    this.peerName = ( (Robo_ser) serObject).getPeerName();
    this.direction = ( (Robo_ser) serObject).getDirection();
    this.x = ( (Robo_ser) serObject).getX();
    this.y = ( (Robo_ser) serObject).getY();
    this.oldX = ( (Robo_ser) serObject).getOldX();
    this.oldY = ( (Robo_ser) serObject).getOldY();

    this.health = ( (Robo_ser) serObject).getHealth();

    this.noOfLives = ( (Robo_ser) serObject).getLives();

    this.maximumHealth = ( (Robo_ser) serObject).getMaxHealth();

    this.totalLives = ( (Robo_ser) serObject).getTotalLives();

    this.healthBarColor = ( (Robo_ser) serObject).getHealthBarColor();
  }

  public Class getSerializedObjectClassName() {
    return Robo_ser.class;
  }

  public void changedObject() {

    // if he does not have any more lives, GAME OVER
    if (this.noOfLives < 0) {
      this.die();
    }

    else {
      // play the according animation for a move down
      if (this.move && this.imgOrientation == IMG_OR_DOWN) {
        super.stepY = 0;

        // set up the object
        this.remoteSetup();

        threadSuspended = false;
        super.totalStepX = Math.abs(super.totalStepX);
        super.totalStepY = Math.abs(super.totalStepY);
        super.stepIndicatorX = 1;
        super.stepIndicatorY = 1;
        t.changeState(false, true);
        move = false;
      }

      // play the according animation for a move up
      else if (this.move && this.imgOrientation == IMG_OR_UP) {
        super.stepY = 0;

        // set up the object
        this.remoteSetup();

        threadSuspended = false;
        super.totalStepX = Math.abs(super.totalStepX);
        super.totalStepY = -Math.abs(super.totalStepY);
        super.stepIndicatorX = 1;
        super.stepIndicatorY = -1;
        t.changeState(false, true);
        move = false;
      }

      // play the according animation for a move left
      else if (this.move && this.imgOrientation == IMG_OR_LEFT) {
        super.stepX = 0;

        // set up the object
        this.remoteSetup();

        threadSuspended = false;
        super.totalStepX = -Math.abs(super.totalStepX);
        super.totalStepY = Math.abs(super.totalStepY);
        super.stepIndicatorX = -1;
        super.stepIndicatorY = 1;
        t.changeState(true, false);
        move = false;
      }

      // play the according animation for move right
      else if (this.move && this.imgOrientation == IMG_OR_RIGHT) {
        super.stepX = 0;

        // set up the object
        this.remoteSetup();

        threadSuspended = false;
        super.totalStepX = Math.abs(super.totalStepX);
        super.totalStepY = Math.abs(super.totalStepY);
        super.stepIndicatorX = 1;
        super.stepIndicatorY = 1;
        t.changeState(true, false);
        move = false;
      }
      else {
        // set up the object
        this.remoteSetup();
        move = false;
      }

      if (direction != -1) {
        switch (direction) {
          case IMG_OR_DOWN:
            moveDown();
            break;
          case IMG_OR_UP:
            moveUp();
            break;
          case IMG_OR_LEFT:
            moveLeft();
            break;
          case IMG_OR_RIGHT:
            moveRight();
            break;
          default:
            break;
        }
        direction = -1;
        change();
      }
    }
  }

// the factory
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Robo();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), Robo.class);
  }
}
