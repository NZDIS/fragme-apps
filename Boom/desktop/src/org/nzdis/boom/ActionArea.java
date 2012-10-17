package org.nzdis.boom;

import java.awt.*;
import java.util.*;
import org.nzdis.fragme.ControlCenter;
import java.util.Iterator;
import org.nzdis.fragme.objects.FMeObject;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActionArea
    extends Panel {

  public static Label lblTurn;
  private Label lblScore;
  private Label lblRound;
  private Label lblWind;
  private boolean landscapePainted = false;
  private static Landscape landscape;
  public static GameSettings gameSettings;
  public static Tank myTank;
  private static Tank tempTank;
  private static int myTankID;
  private static int tankTurn = 1;
  private static Missile missile;
  public static ArrayList tankArray = new ArrayList();
  private static ArrayList missileArray = new ArrayList();
  private static int wind;
  private static Timer timer;
  public static Timer turnTimer;
  private static boolean fired = false;
  private boolean t = false;
  public boolean tanksPainted = true;
  private Vector objs;
  private static boolean firstPlayer = false;
  private static boolean turn = false;
  public static EndOfRound roundScreen;
  public boolean timeStarted = false;

  // private static int count = 0;
  private static Splash splash;
  private Iterator it;
  public static int time = 10;
  public ActionArea(int xSize, int ySize) {
    this.setSize(xSize, ySize);
    this.setupLandscape();
    wind = landscape.getWind();
    System.out.println("Wind = " + wind);
    timer = new Timer();
    timer.schedule(new PaintingTask(), 4000, 50);
  }

  public static void setActionAreaVar() {
    fired = false;

  }

  /**
   * The method sets up the landscape for the game.  If there is already a
   * landscape created then it will use it.  If the player is the first player
   * then it will generate a new landscape object and then create the landscape
   */
  private void setupLandscape() {

    // If the landscape has already been created then player must not
    // be the first player.  Therefore we need to get the current landscape
    // object.
    objs = ControlCenter.getAllObjects(Landscape.class);
    if (objs.size() == 1) {
      FMeObject obj = (FMeObject) objs.get(0);
      if (obj instanceof Landscape) {
        System.out.println("Getting Landscape...");
        landscape = (Landscape) obj;
      }
      else {
        System.out.println("There was a problem with the landscape");
      }
      objs = ControlCenter.getAllObjects(GameSettings.class);
      obj = (FMeObject) objs.get(0);
      if (obj instanceof GameSettings) {
        System.out.println("Getting GameSettings...");
        gameSettings = (GameSettings) obj;
        gameSettings.setActionArea(this);
      }
      else {
        System.out.println("There was a problem with the game settings");
      }
    }
    else {
      // First player must create the landsacpe
      firstPlayer = true;
      try {
        System.out.println("Generating the landscape...");
        landscape = (Landscape) ControlCenter.createNewObject(Landscape.class);
        landscape.createLandscape();
        System.out.println("Creating new game settings object...");
        gameSettings = (GameSettings) ControlCenter.createNewObject(
            GameSettings.class);
        gameSettings.setActionArea(this);
      }
      catch (Exception e) {
        System.out.println("Problem creating lanscape object");
        System.out.println(e.getMessage());
      }
    }
  }

// -------------------------- PAINTING METHODS -----------------------------//

  /**
   * Paints the whole action area
   */
  public void paint(Graphics g) {
    Iterator it = tankArray.iterator();

    if (gameSettings.isGameStarted()) {

      if (landscapePainted == false && t != false) {
        landscape.paint(g);
        landscapePainted = true;
        System.out.println("Landscape Repainted");

        if (!tanksPainted) {
          while (it.hasNext()) {
            Tank t = (Tank) it.next();
            if (t.isVisible()) {
              landscape.paint(t.getX(), t.getY(), g);
              t.paint(getGraphics());
            }
          }
          tanksPainted = true;
        }
      }
//------------------------------ Display messages ------------------------//
      if (gameSettings.tankTurn == myTankID && !gameSettings.roundOver  && gameSettings.isGameStarted()) {
        GameFrame.lblInfo.setText("YOUR TURN!");
      }
      else if (!myTank.isVisible() && !gameSettings.roundOver  && gameSettings.isGameStarted()) {
        GameFrame.lblInfo.setText("DESTROYED!");
      }
      else {
        GameFrame.lblInfo.setText("TURN OVER!");
      }
//------------------------------ Missile paint ------------------------//
      int tempID = 0;
      if (missile != null) {
        if (landscape.isPointValid(missile.getX(), missile.getY())) {
          missile.paint(g);
        }
        else {
//--------------------- Check if missile hits a tank ------------------------//
          it = tankArray.iterator();
          while (it.hasNext()) {
            Tank t = (Tank) it.next();
            if (t.getX() < (missile.getX() + 20) &&
                t.getX() > (missile.getX() - 40) && t.isVisible()) {

              tempID = t.getTankID();
              GameFrame.lblInfo.setText("Destroyed!");

              if (turn) {
                System.out.println("t.getTankID()" + t.getTankID());
                System.out.println("myTankID" + myTankID);
                if (t.getTankID() == myTankID) {
                  myTank.subScore(500);
                }else {
                  System.out.println("I just hit something!");
                  myTank.addToScore(500);
                }
              }

              if (t.getTankID() == myTankID) {
                myTank.setVisible(false);
              }

              myTank.change();
              this.lblScore.setText(String.valueOf(myTank.getScore()));
              TankExplosion te = new TankExplosion(t.getX(), t.getY(), 40, 40,g);
              System.out.println("Tank: " + t.getTankID() + " destroyed ");
              break;

            }
            missile.paint(g, true);
          }

          landscape.modifyLandscape(missile.getX(), 20);
          landscape.paint(missile.getX() - 15, missile.getY(), 50, g);
          repaint();
          this.missile = null;

//------------------------------ Set next turn -----------------------------//
          if (turn) {
            gameSettings.nextTurn();
            gameSettings.change();
            turn = false;
          }

        }

      }
      this.setTT();
    }
  }



  /**
   * Repaints a tank
   */
  public void repaint(Tank t) {
    t.paint(getGraphics());
  }

  /**
   * Repaints a missile
   */
  public void repaint(Missile miss) {
    miss.paint(getGraphics());
  }

  /**
   * Updates the action area
   */
  public void update(Graphics g) {
    paint(g);
  }

  /**
   * This method paints the new possion of a tank and repaints all the tanks
   */
  public void moveTank() {
    //myTank.paint(getGraphics());
    Iterator it = tankArray.iterator();

    while (it.hasNext()) {
      Tank t = (Tank) it.next();
      if (t.isVisible()) {
        landscape.paint(t.getX(), t.getY(), getGraphics());
        t.paint(getGraphics());
      }
    }

  }

  // ----------------------------- TANK MEHTODS -----------------------------//

  /**
   * Adds a tank to the tankArray.  This is called from the gameframe class
   */
  public void addTank(Tank tank) {
    tankArray.add(tank);
    tank.setActionArea(this);
  }

  /**
   * This method fires a missle for this players tank
   */
  public void tankFire() {
    fired = true;
    myTank.setFire(true);
    this.missile = new Missile();
    missile.setup(myTank.getX(), myTank.getY(), myTank.getAngle(),
                  myTank.getPower(), landscape.getWind());

  }

  /**
   * This method fires a missle for another player
   */
  public void tankFire(Tank anotherTank) {
    missile = new Missile();
    System.out.println("Tank X: " + anotherTank.getX());
    System.out.println("Tank Y: " + anotherTank.getY());
    System.out.println("Tank angle: " + anotherTank.getAngle());
    System.out.println("Tank power: " + anotherTank.getPower());
    missile.setup(anotherTank.getX(), anotherTank.getY(), anotherTank.getAngle(),
                  anotherTank.getPower(), landscape.getWind());
  }

  public void otherTanks() {
    Iterator it = tankArray.iterator();
    while (it.hasNext()) {
      //  Tank temp = (Tank)it.next();
      System.out.println("one tank ere!");
    }
  }

// ---------------------------- TURN METHODS ----------------------------//

  public void trunTimer() {
   if(!timeStarted){
     turnTimer = new Timer();
     turnTimer.schedule(new TurnTask(this), 4000, 50);
   }
  }

  public void preRoundOver() {
    gameSettings.setRoundOver(true);
    gameSettings.change();
    roundOver();
  }

  public void roundOver() {
    turnTimer.cancel();
    System.out.println("in round over");
    roundScreen.setTankArray(this.tankArray);
    setTT();
    roundScreen.setVisible(true);
    roundScreen.repaint();

    resetTankStartingPoint();
    myTank.setVisible(true);
    myTank.change();
    paintLandscape();
  }

  public void newRound() {
    lblWind.setText(String.valueOf(landscape.getWind()));
    this.lblRound.setText(String.valueOf(gameSettings.getRound()));
    gameSettings.setRoundOver(false);
    gameSettings.setNewRound(false);
    gameSettings.change();
    setT();
    roundScreen.setVisible(false);
    paintLandscape();
    myTank.move(1);
    myTank.change();
  }

  private void paintLandscape() {
    setLandscapePainted(false);
    setT();
    this.repaint();
  }

  private void resetLandscape() {

    if (this.isFirstPlayer()) {
      landscape.createLandscape();
      landscape.change();
    }

  }

  private void resetTankStartingPoint() {

    while (landscape.getLandscapeNumber() != gameSettings.round) {
      try {
        Thread.sleep(1000);
      }
      catch (Exception e) {
        System.out.println(e);
      }
    }
    int startingPoint = -1;
    while (startingPoint < 5 || startingPoint > 610) {
      startingPoint = (int) (Math.random() * landscape.landscape.length);
    }
    myTank.setLocation(startingPoint, landscape.landscape[startingPoint] - 17);

  }



  // ------------------------------ SETTERS --------------------------------//

  public void setTank(Tank tank) {
    this.myTank = tank;
    int startingPoint = startingPoint = (int) (Math.random() *
                                               landscape.landscape.length);
    while (startingPoint < 5 || startingPoint > 610) {
      startingPoint = (int) (Math.random() * landscape.landscape.length);
    }

    this.myTank.setLocation(startingPoint,
                            landscape.landscape[startingPoint] - 17);
    this.myTank.setActionArea(this);
  }

  public void setLandscapePainted(boolean landscapePainted) {
    this.landscapePainted = landscapePainted;
  }

  public void setTurn(boolean turn) {
    this.turn = turn;

  }

  public void setMyTankID(int id) {
    this.myTankID = id;
  }

  public void setTankTurn(int id) {
    this.tankTurn = id;
  }

  public void setLblTurnActual(Label lblTurn) {
    this.lblTurn = lblTurn;
  }

  public void setLblScoreActual(Label lblScore) {
    this.lblScore = lblScore;
  }

  public void setLblRoundActual(Label lblRound) {
    this.lblRound = lblRound;
  }

  public void setLblWind(Label lblWind) {
    this.lblWind = lblWind;
  }

  public void setT() {
    t = true;
  }

  public void setTT() {
    t = false;
  }

  public void setSplash(Splash splash) {
    this.splash = splash;
  }

  public void setRoundScreen(EndOfRound roundScreen) {
    this.roundScreen = roundScreen;
  }

  public void setTime(int time) {
    this.time = time;
  }

  // ------------------------------ GETTERS ---------------------------------//

  public Landscape getLandscape() {
    return landscape;
  }

  public int getMyTankID() {
    return myTankID;
  }

  public int getTankTurn() {
    return tankTurn;
  }

  public boolean isFirstPlayer() {
    return firstPlayer;
  }

  public boolean isTurn() {

    return turn;
  }

  public boolean isFired() {
    return fired;
  }


  class PaintingTask
     extends TimerTask {
   int numWarningBeeps = 300;
   public void run() {
     repaint();
   }
 }

 class TurnTask
     extends TimerTask {
   int numWarningBeeps = 100;

   ActionArea aa;
   public TurnTask(ActionArea aa) {
     this.aa = aa;
     aa.setTime(10);
   }

   public void run() {
     timeStarted = true;
     aa.time--;
     GameFrame.lblTimer.setText(String.valueOf(time));
     System.out.println("Time: " + time);
     try {
       Thread.sleep(1000);
     }
     catch (Exception e) {
       System.out.println(e);
     }
     if (aa.time <= 0) {
       GameFrame.lblTimer.setText("");

       System.out.println("This should be only on one tank!!!!!");
       timeStarted = false;
       gameSettings.nextTurn();
       gameSettings.change();
       turn = false;

       this.cancel();
     }
   }
 }


} // end class
