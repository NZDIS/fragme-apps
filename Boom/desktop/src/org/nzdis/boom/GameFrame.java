package org.nzdis.boom;

import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import org.nzdis.fragme.ControlCenter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;
import org.nzdis.fragme.objects.FMeObject;

/**
 * <p>Title: GameFrame Class for Boom game </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class GameFrame
    extends Frame
    implements KeyListener, WindowListener, MouseListener{

  // This is the key table.
  private static int POWERUP = 0;
  private static int POWERDOWN = 1;
  private static int LEFT1 = 2;
  private static int RIGHT1 = 3;
  private static int SHOOT1 = 4;
  private static int SPLASH1 = 5;
  private static int ANGLEDOWN = 6;
  private static int ANGLEUP = 7;
  private static int TEST = 8;
  private static int TANK1 = 9;
  private static int TANK2 = 10;
  private static int TANK3 = 11;

  public static Color bgc = new Color(255, 123, 60);

  public static Tank tank;
  private static Tank tempTank;
  private static int tankID = 1;

  private static ActionArea aa;
  private int keyTable[];
  public Image background = null;
  private Vector objs;
  private BufferedReader br;
  private static Label lblPowerActual;
  private static Label lblAngleActual;
  private static Label lblWindActual;
  private static Label lblPlayerActual;
  private static Label lblTurnActual;
  private static Label lblScoreActual;
  private static Label lblRoundActual;
  public static Label lblTimer;
  public static Label lblInfo;
  private static Splash splash;
  private static EndOfRound roundScreen;
  private static int count = 0;
  private static boolean pickedTank = false;
  private String playerName;

  public static boolean tankMoving = false;

  public GameFrame() {

    FlowLayout fl = new FlowLayout();


    //Create the stats bar
    Panel statsBar = new Panel();
    statsBar.setBackground(Color.black);
    this.setBackground(Color.black);
    statsBar.setSize(640, 50);
    Label lblPower = new Label("POWER:");
    lblPowerActual = new Label("20");
    lblPower.setForeground(Color.red);
    lblPowerActual.setForeground(Color.yellow);
    Label lblWind = new Label("WIND:");
    lblWindActual = new Label("0");
    lblWind.setForeground(Color.red);
    lblWindActual.setForeground(Color.yellow);
    Label lblAngle = new Label("ANGLE:");
    lblAngleActual = new Label("45");
    lblAngle.setForeground(Color.red);
    lblAngleActual.setForeground(Color.yellow);
    Label lblPlayer = new Label("Player:");
    lblPlayerActual = new Label("           ");
    lblPlayer.setForeground(Color.red);
    lblPlayerActual.setForeground(Color.yellow);
    Label lblTurn = new Label("Turn:");
    lblTurnActual = new Label("1");
    lblTurn.setForeground(Color.red);
    lblTurnActual.setForeground(Color.yellow);
    Label lblScore = new Label("Score:");
    lblScoreActual = new Label("    0", 1);
    Label lblRound = new Label("Round:");
    lblRoundActual = new Label("1", 1);
    lblRound.setForeground(Color.red);
    lblRoundActual.setForeground(Color.yellow);
    lblScore.setForeground(Color.red);
    lblScoreActual.setForeground(Color.yellow);
    lblTimer = new Label("0");
    lblTimer.setBackground(GameFrame.bgc);
    lblTimer.setForeground(Color.black);
    lblInfo = new Label("YOU HAVE BEEN DESTROYED!");
    lblInfo.setBackground(GameFrame.bgc);
    lblInfo.setForeground(Color.black);


    statsBar.add(lblScore, fl);
    statsBar.add(lblScoreActual, fl);
    statsBar.add(lblPower, fl);
    statsBar.add(lblPowerActual, fl);
    statsBar.add(lblAngle, fl);
    statsBar.add(lblAngleActual, fl);
    statsBar.add(lblWind, fl);
    statsBar.add(lblWindActual, fl);
    statsBar.add(lblPlayer, fl);
    statsBar.add(lblPlayerActual, fl);
    statsBar.add(lblTurn, fl);
    statsBar.add(lblTurnActual, fl);
    statsBar.add(lblRound, fl);
    statsBar.add(lblRoundActual, fl);
    this.add(statsBar, BorderLayout.NORTH);

    // Splash
    background = Toolkit.getDefaultToolkit().getImage("res/images/Nuke.jpg");
    splash = new Splash();
    roundScreen = new EndOfRound();
    this.add(roundScreen);
    this.add(splash);
    splash.setVisible(true);
    roundScreen.setVisible(false);


    splash.setMessage("Connecting to FRAGME");
    ControlCenter.setUpConnections("Boom" + GameSettings.verNum);
//    playerName = ControlCenter.getPeerManager().getPeerName(ControlCenter.getMyAddress());
//    lblPlayerActual.setText(playerName);

    // Setup the frame
    this.setSize(640, 480);
    this.setTitle("Boom");
    this.setVisible(true);

    //Create the ActionArea and add to frame
    aa = new ActionArea(640, 440);
    this.add(aa, BorderLayout.CENTER);
    aa.setSplash(this.splash);
    aa.setRoundScreen(this.roundScreen);
    aa.add(lblInfo, fl);
    aa.add(lblTimer, fl);

    // Get wind from landscape class
    // Note: moved to landscape class so that it was the same for all players
    int windy = (int) aa.getLandscape().getWind();
    this.lblWindActual.setText(String.valueOf(windy));

    // Add all the window and key listener in
    this.addKeyListener(this);
    this.addWindowListener(this);
    this.addMouseListener(this);
    //Set the keys
    setKeys();
    try {
      //Wait until the player picks their tank
      splash.setDrawTanks(true);
      splash.setMessage("Please pick a tank");

      if( (aa.gameSettings.isGameStarted()) == false) {
        while (pickedTank == false) {
          System.out.println(ControlCenter.getPeerManager().getMyPeerName());
          Thread.sleep(1000);

        }
      } else{
      objs = ControlCenter.getAllObjects(Tank.class);
      Iterator it = objs.iterator();
      while (it.hasNext()) {
        FMeObject obj = (FMeObject) it.next();
        if (obj instanceof Tank) {
        System.out.println(tank.getMyPeerName());
        System.out.println(ControlCenter.getMyPeerName());
        //  if(tank.getMyPeerName().equals(ControlCenter.getMyPeerName())){
            System.out.println("here");
            tank = (Tank) obj;
            aa.setTank(tank);

        //  }

        }
      }


      }

      // Wait for other players to join the game and then the first player can
      // start it by press the enter key.
      splash.setMessage("Waitting for other players");
      splash.setMessage1("Press a ENTER to start");

      if (aa.isFirstPlayer()) {
        aa.setTurn(true);
     }

      splash.repaint();
      while ( (aa.gameSettings.isGameStarted()) == false) {
        Thread.sleep(1000);
        splash.repaint();

      }
      this.setVisible(true);
      aa.repaint();
    }
    catch (Exception ex) {
      System.out.println(ex);
    }

    //Get all the Fragme tank and add them to an array in the ActionArea
    getAllTanks();

    splash.setVisible(false);
    aa.setT();


  }

  //----------------------- PRIVATE METHODS---------------------------------//

  /**
   * This method creates a FragME tank and adds it to the action area
   */
  private void createFragMeTank(int i) {
    try {
      // This should be done better but it works for now.
      // For some reason the .getAllObjects(Tank.class) doesn't work!
      // Find the next TankID
      objs = ControlCenter.getAllObjects();
      Iterator it = objs.iterator();
      while (it.hasNext()) {
        FMeObject obj = (FMeObject) it.next();
        if (obj instanceof Tank) {
          tankID++;
        }
      }

      System.out.println("Creating new tank objects...");
      System.out.println("Setting the tank...");
      tank = (Tank) ControlCenter.createNewObject(Tank.class);
      aa.setTank(tank);
      tank.setTankID(tankID);
      tank.setImage(i);
      tank.setImages(i);
      tank.setMyPeerName(ControlCenter.getMyPeerName());
      this.lblPlayerActual.setText(ControlCenter.getMyPeerName());
      aa.setMyTankID(tankID);
      aa.setLblTurnActual(this.lblTurnActual);
      aa.setLblScoreActual(this.lblScoreActual);
      aa.setLblRoundActual(this.lblRoundActual);
      aa.setLblWind(this.lblWindActual);
      aa.gameSettings.addPlayer();
      //Change FRAGME objects
      tank.change();
      aa.gameSettings.change();
      aa.getLandscape().change();


    }
    catch (Exception e) {
      System.out.println("Problem creating tank object");
      System.out.println(e.getMessage());
    }
  }

  /**
   * This method gets all the FragMe tanks in the game and adds them to the an
   * array in the action area
   */
  private void getAllTanks() {
    objs = ControlCenter.getAllObjects();
    System.out.println("Number of FRAGME objects in the game: " + objs.size());

    Iterator it = objs.iterator();
    System.out.println("Checking for other tanks");
    System.out.println("Your tank ID: " + tank);
    while (it.hasNext()) {
      FMeObject obj = (FMeObject) it.next();

      if (obj instanceof Tank) {
        tempTank = (Tank) obj;
        System.out.println("Found Tank 2 ID: " + tempTank);
        aa.addTank( ( (Tank) obj));

      }



    }
  }

//-------------------------- KEY EVENT METHODS -----------------------------//

  /**
   * Sets the keys for the game
   */
  private void setKeys() {
    keyTable = new int[12];
    keyTable[POWERUP] = KeyEvent.VK_UP;
    keyTable[POWERDOWN] = KeyEvent.VK_DOWN;
    keyTable[LEFT1] = KeyEvent.VK_LEFT;
    keyTable[RIGHT1] = KeyEvent.VK_RIGHT;
    keyTable[SHOOT1] = KeyEvent.VK_SPACE;
    keyTable[SPLASH1] = KeyEvent.VK_ENTER;
    keyTable[ANGLEDOWN] = KeyEvent.VK_A;
    keyTable[ANGLEUP] = KeyEvent.VK_S;
    keyTable[TEST] = KeyEvent.VK_R;
    keyTable[TANK1] = KeyEvent.VK_1;
    keyTable[TANK2] = KeyEvent.VK_2;
    keyTable[TANK3] = KeyEvent.VK_3;
  }

  /**
   * Key pressed event
   */

  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == keyTable[RIGHT1] && tank.isVisible()
        && (aa.gameSettings.isGameStarted())) {

      if (tankMoving != true) {
        tankMoving = true;
        tank.setDirection(1);
        tank.setMove(true);
        tank.moveTank(1);
      }

    }
    if (e.getKeyCode() == keyTable[LEFT1] && tank.isVisible()
        && (aa.gameSettings.isGameStarted())) {

      if (tankMoving != true) {
        tankMoving = true;
        tank.setDirection(2);
        tank.setMove(true);
        tank.moveTank(2);
      }
    }
    if (e.getKeyCode() == keyTable[SHOOT1] && !aa.isFired() &&
        tank.isVisible() && (aa.gameSettings.getTankTurn() == tank.getTankID())
        && aa.gameSettings.isGameStarted()) {
      aa.tankFire();
      aa.setTurn(true);
      lblTimer.setText(String.valueOf(""));
      aa.timeStarted = false;
      aa.turnTimer.cancel();

     // System.out.println("Turn: " + aa.gameSettings.tankTurn);

      aa.repaint();
    }
    if (e.getKeyCode() == keyTable[POWERUP] && tank.isVisible()
        && (aa.gameSettings.isGameStarted())) {
      tank.increasePower();
      int tempPow = tank.getPower();
      this.lblPowerActual.setText(String.valueOf(tempPow));
    }
    if (e.getKeyCode() == keyTable[POWERDOWN] && tank.isVisible()
        && (aa.gameSettings.isGameStarted())) {
      tank.decreasePower();
      int tempPow = tank.getPower();
      this.lblPowerActual.setText(String.valueOf(tempPow));
    }
    if (e.getKeyCode() == keyTable[ANGLEUP] && tank.isVisible()
        && (aa.gameSettings.isGameStarted())) {
      tank.increaseAngle();
      int tempAng = tank.getAngle();
      this.lblAngleActual.setText(String.valueOf(tempAng));
    }
    if (e.getKeyCode() == keyTable[ANGLEDOWN] && tank.isVisible()
        && (aa.gameSettings.isGameStarted())) {
      tank.decreaseAngle();
      int tempAng = tank.getAngle();
      this.lblAngleActual.setText(String.valueOf(tempAng));
    }
    if ( (e.getKeyCode() == keyTable[SPLASH1])
         && aa.gameSettings.playersNum >= aa.gameSettings.playersMin) {
      if(!aa.gameSettings.isGameStarted()){
        aa.gameSettings.setGameStarted(true);
        aa.gameSettings.change(); //tell the other players that the game has started
      } else if(aa.gameSettings.isGameStarted() && aa.gameSettings.roundOver){
        aa.gameSettings.newRound = true;
        aa.gameSettings.change();
        aa.newRound();
      }
    }

    if ( (e.getKeyCode() == keyTable[TANK1]) && !pickedTank) {
      //Create tank objects
      System.out.println("Red");
      createFragMeTank(1);
      pickedTank = true;
      splash.setDrawTanks(false);

    }
    if ( (e.getKeyCode() == keyTable[TANK2]) && !pickedTank) {
      //Create tank objects
      System.out.println("Yellow");
      createFragMeTank(2);

      pickedTank = true;
      splash.setDrawTanks(false);

    }
    if ( (e.getKeyCode() == keyTable[TANK3]) && !pickedTank) {
      //Create tank objects
      System.out.println("Pink");
      createFragMeTank(3);
      pickedTank = true;
      splash.setDrawTanks(false);

    }

    if (e.getKeyCode() == keyTable[TEST]) {
      System.out.println("repaint");
      System.out.println(aa.gameSettings.isGameStarted());
      if (aa.gameSettings.isGameStarted()) {
        aa.setLandscapePainted(false);
        aa.setT();
        aa.tanksPainted = false;
        aa.repaint();
      }

    }

  }

  public void keyTyped(KeyEvent e) {

  }

  public void keyReleased(KeyEvent e) {
  }

//-------------------------- WINDOW EVENT METHODS ---------------------------//

  public void mouseClicked(MouseEvent e) {
    this.requestFocus();
    System.out.println("Mouse Clicked");
  }

  public void mouseEntered(MouseEvent e) {
    this.requestFocus();
    System.out.println("Mouse Clicked");
  }
  public void mouseExited(MouseEvent e) {
    this.requestFocus();
    System.out.println("Mouse Clicked");
 }
 public void mousePressed(MouseEvent e) {
   this.requestFocus();
    System.out.println("Mouse Clicked");
 }
 public void mouseReleased(MouseEvent e) {
   this.requestFocus();
    System.out.println("Mouse Clicked");
 }

  public void windowActivated(WindowEvent e) {
    this.requestFocus();
    System.out.println("Focus");
    if (aa.gameSettings.isGameStarted()) {
      //   aa.setLandscapePainted(false);
      //   aa.repaint();
    }
    else {
      splash.repaint();
    }
  }



  public void windowClosed(WindowEvent e) {
    // do nothing
  }

  public void windowOpened(WindowEvent e) {
    //  this.setFocusable(true);
  }

  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }

  public void windowIconified(WindowEvent e) {

  }

  public void windowDeiconified(WindowEvent e) {
    // do nothing
  }

  public void windowDeactivated(WindowEvent e) {
    this.requestFocus();
    System.out.println("de Focus");
  }

}
