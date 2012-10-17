package org.nzdis.robogame;

import org.nzdis.robogame.moveable.*;
import org.nzdis.robogame.nonmoveable.*;
import org.nzdis.robogame.consumeable.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.nzdis.fragme.*;

//import org.nzdis.fragme_old.*;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * This class is the main class of RoboJoust.
 */
public class RoboGame
    extends Frame
    implements KeyListener, WindowListener {

  /**
   * Define the map size. Every object takes one tile in the game, and
   * mapWidth * mapHeight = total number of tiles.
   */
  public static final int mapWidth = 17;
  public static final int mapHeight = 11;
  public static final int tileSize = 40;
  Object[][] map;

  /**
   * boolean to check whether a peer has existed before. Needed for peer
   * rejoining.
   */
  private boolean droppedOutBefore;

  /**
   * This variable defines whether the actual game has been started, if it is
   * false the startup splash screen will be shown.
   */
  boolean playing = false;

  /**
   * Restart our one level
   */
  boolean restart = false;

  /**
   * variable defines that the game has ended, with player loosing
   */
  boolean gameLost = false;

  /**
   * variable defines that the game has ended, with player winning
   */
  boolean gameWon = false;

  /**
   * variable to tell if any of the game keys has been pressed
   */
  boolean gameKey = false;

  /**
   * load the background image
   */
  private static Image background = Toolkit.getDefaultToolkit().getImage("res/images/BG.png");

  private EndAnimation endAnim = null;
  private int titleX;
  private int titleY;
  private int titleFont;
  private int colorCnt = 0;

  private Color myColor = Color.red;

  /**
   * Vector to store background objects that do not have to be changed later in
   * the game (bricks, pool, whirlpool)
   */
  private Vector backgroundObjs = new Vector();



  /**
   * Two images to draw. One image includes all background (nonmoveable)
   * objects, so it only has to be updated in case of background changes.
   * The "background image" is copied into the other one, so moveables can be
   * drawn on top of it.
   */
  private Image bgImage = null;
  private Image offscreen = null;
//
  private static Font font = new Font(null, 0, 40);

  /**
   * reconstructBG determines if the background has changed. Only if it is true,
   * it will be repainted.
   */
  private static boolean reconstructBG;

  /**
   * Panel and buttons for the start screen.
   */
  Panel pnlButton = new Panel();
  private Button buttonRobo1 = new Button("Start the game");

  /**
   * Text panel and text field for the start screen.
   */
  Panel pnlText = new Panel();
  private final static String enterPeerName = "Enter your peer name, max. 7 characters!            ";
  private TextField textRobo = new TextField();
  private Label labelRobo = new Label(enterPeerName);

  /**
   * Has game started? Important for the paint routine. The paint() method
   * tries to get number of peers, but if the connection is not setup, it
   * will thrown null pointer exception if it tries to get number of peers
   * from FRAGme
   */
  private boolean gameStarted = false;

  /**
   * roboNumber determines the actual player.
   */
  private int roboNumber = 0;

  /**
   * This is the key table.
   */
  private int keyTable[];
  private static final int UP = 0;
  private static final int DOWN = 1;
  private static final int LEFT = 2;
  private static final int RIGHT = 3;
  private static final int ACTION = 4;
  private static final int LANCE = 5;
  //private static final int BOMB = 6;

  /**
   * Message performatives
   */
  public static final String WIN_COND = "winCondition";
  public static final String RESTART_COND = "restartCondition";

  /**
   * Message contents
   */
  public static final String WIN = "win";
  public static final String LOOSE = "loose";
  public static final String RESTART = "restart";

  /**
   * Number of maximum peers in the game + number of died peers in the game
   * Needed to check for winning condidition - last man standing wins
   */
  static int maxPeers = -1;
  static int diedPeers = 0;

  /**
   * Robo restart waiting thread, ensures the peers are restarting the game
   * in the right order
   */
  static RestartWaitingThread rWT = null;
  static boolean restartThreadSuspended = false;
  static int restartedPeers = 0;

  /**
   * Number of bombs and lances
   */
  public int totalBombs  = 0;
  public int totalLances = 0;

//  private static long h1;
//  private static long h2;

  public String peername = new String(" ");

  /**
   * reference to the active player
   */
  public Robo robo;

  private static GCThread gcT;

  /**
   * Constructor
   */
  public RoboGame() {

    /**
     * Create the game map
     */
    map = GameObject.createMap(mapWidth, mapHeight);
    GameObject.game = this;
    GameObject.setTileSize(tileSize);
    BackgroundObject.init(map, this);

    /**
     * Create a media tracker that keeps the device waiting until graphics are
     * loaded
     */
    GameObject.mediaTracker = new MediaTracker(this);
    BackgroundObject.mediaTracker = new MediaTracker(this);
    try {
//      MediaTracker mediaTracker = new MediaTracker(this);
      GameObject.loadImage(background);

//      title = Toolkit.getDefaultToolkit().getImage("res/images/CME_work4.jpg");
//      mediaTracker.addImage(title, 0);
//      mediaTracker.addImage(background, 1);
//      try {
//        mediaTracker.waitForAll();
//      }
//      catch (InterruptedException ie) {
//        System.err.println(ie);
//        System.exit(1);
//      }

      /**
       * Set the layout of the splash screen
       */
      BorderLayout borderLayout1 = new BorderLayout();
      FlowLayout flowLayout1 = new FlowLayout();
      this.setLayout(borderLayout1);

      pnlText.setLayout(flowLayout1);
      this.add(pnlText, borderLayout1.CENTER);
      pnlText.setBackground(Color.black);

      labelRobo.setBackground(Color.white);
      pnlText.add(labelRobo, flowLayout1);

      textRobo.setColumns(7);
      pnlText.add(textRobo, flowLayout1);

      pnlButton.setLayout(flowLayout1);
      this.add(pnlButton, borderLayout1.SOUTH);
      //this.setResizable(false);
      pnlButton.setBackground(Color.black);
      pnlButton.add(buttonRobo1, flowLayout1);
      buttonRobo1.addActionListener(new RoboGame_buttonRobo1_actionAdapter(this));

      /**
       * set window size and title
       */
      setSize(mapWidth * tileSize, mapHeight * tileSize);
      setTitle("RoboJoust 0.5");



      this.addKeyListener(this);
      this.addWindowListener(this);
      setKeys(); // set the keyboard controls.

      /**
       * start the title animation
       */
      //splashAnim = new TitleAnimation();
//      splashAnim.start();

    }
    catch (Exception e) {
      System.out.println("Problem creating object");
      e.printStackTrace();
      System.exit(0); // quit application
    }
  }

  /**
   * main method of RoboJoust
   * @param args String[]
   */
  public static void main(String[] args) {
//    h1 = TestBoundary.usedMemory();
//    System.out.println("Before starting game,used memory :"+h1);
    gcT = new GCThread();
    RoboGame rj = new RoboGame();
    rj.show();
    runGC();
  }

  public static void runGC(){
    gcT.wakeUp();
  }

  /**
   * method is called when a player looses all his lives -> game ends for him
   */
  public void endGame(boolean condition) {
    //bring up another screen
    if (condition == false) {
      System.out.println(
          "-------A PLAYER HAS DIED--------THE GAME IS OVER----------");

      gameLost = true;
      reconstructBG(true);
//      endAnim = new EndAnimation();
      repaint();

      diedPeers = 0;

      restartThreadSuspended = true;
      rWT = new RestartWaitingThread();
    }
    else {
      diedPeers++;

      System.out.println("died:" + diedPeers + "max:" + maxPeers);

      if(diedPeers == maxPeers) {
        System.out.println("THIS PLAYER HAS WON!!!");

        gameWon = true;
        reconstructBG(true);

//        endAnim = new EndAnimation();
      repaint();
      }
    }

    if(diedPeers == maxPeers) {
      playing = false;
      restart = true;

      diedPeers = 0;
    }
  }

  /**
   * Method to restart the game --- not working at the moment, not called.
   * In the future, this would be called from the keyPressed method --- see
   * the commented part there!
   */
  public void restartGame() {
    System.out.println("deleting all objects");
    Iterator it = ControlCenter.getAllObjects(NonMoveable.class).iterator();
    while (it.hasNext()) {
      GameObject item = (GameObject) it.next();
      if(item.getOwnerAddr().equals(ControlCenter.getMyAddress())); {
        map[item.x][item.y] = null;
        item.delete();
      }
    }
    Iterator it2 = ControlCenter.getAllObjects(Consumable.class).iterator();
    while (it2.hasNext()) {
      GameObject item = (GameObject) it2.next();
      if(item.getOwnerAddr().equals(ControlCenter.getMyAddress())); {
        map[item.x][item.y] = null;
        item.delete();
      }
    }


    System.out.println("now reset the level stuff");
    reSetupLevel();

    if(gameWon) {
      Message restartMessage = (Message) ControlCenter.createNewObject(Message.class);
      restartMessage.setPerformative(RESTART_COND);
      restartMessage.setContent(RESTART);
      restartMessage.change();
    }

    gameWon = false;
    gameLost = false;

    System.out.println("repaint!");
    reconstructBG(true);
    repaint();

    diedPeers = 0;
  }

  /**
   * Call this function if you want to repaint the background (for example,
   * if a nonmoveable object changed
   * @param arg boolean
   */
  public static void reconstructBG(boolean arg) {
    reconstructBG = arg;
  }

  /**
   * main paint method
   * @param g Graphics
   */
  public void paint(Graphics g) {

    if(gameStarted) {
      int n = ControlCenter.getNoOfPeers();
      if (n > maxPeers) {
        maxPeers = n;
      }
    }

    /**
     * draw the splash screen if not playing
     */

//    if ((this.playing == false && this.restart == false) || gameLost || gameWon) {
//        switch (colorCnt) {
//          case 0:
//            myColor = Color.cyan;
//            break;
//          case 1:
//            myColor = Color.red;
//            break;
//          case 2:
//            myColor = Color.blue;
//            break;
//          case 3:
//            myColor = Color.green;
//            break;
//          case 4:
//            myColor = Color.yellow;
//            break;
//          case 5:
//            myColor = Color.white;
//            break;
//        }
//        colorCnt++;
//        if (colorCnt > 5) {
//          colorCnt = 0;
//        }
//    }
//    if (this.playing == false && this.restart == false) {
//      if (title == null) {
//        title = this.createImage(mapWidth * tileSize,
//                                 mapHeight * tileSize);
//        Graphics titleGraphics = title.getGraphics();
//
//        titleGraphics.drawRect(0, 0, mapWidth * tileSize, mapHeight * tileSize);
//        titleGraphics.setColor(Color.black);
//        titleGraphics.fillRect(0, 0, mapWidth * tileSize, mapHeight * tileSize);
//
//        titleGraphics.setColor(Color.red);
//        font = new Font(null, 0, 20);
//        titleGraphics.setFont(font);
//        titleGraphics.drawString("Buttons:", 80, 140);
//        titleGraphics.drawString("Left", 140, 160); titleGraphics.drawString("- Move left", 280, 160);
//        titleGraphics.drawString("Right", 140, 180); titleGraphics.drawString("- Move right", 280, 180);
//        titleGraphics.drawString("Up", 140, 200); titleGraphics.drawString("- Move up", 280, 200);
//        titleGraphics.drawString("Down", 140, 220); titleGraphics.drawString("- Move down", 280, 220);
//        titleGraphics.drawString("Q", 140, 240); titleGraphics.drawString("- Sword attack", 280, 240);
//        titleGraphics.drawString("W", 140, 260); titleGraphics.drawString("- Lance throw", 280, 260);
//        titleGraphics.drawString("Number keys", 140, 280); titleGraphics.drawString("- Bomb", 280, 280);
//
//        titleGraphics.drawString("Your mission: win the RoboJoust trophy by hitting your opponent.", 60, 320);
//        titleGraphics.drawString("Hits with the sword take off 10, the lance hits 15 and the bombs", 60, 340);
//        titleGraphics.drawString("get stronger the longer they lie down (according to number key).", 60, 360);
//      }
//
//      if (titleHeading == null) {
//        titleHeading = this.createImage(mapWidth * tileSize, mapHeight * tileSize);
//      }
//
//      // save original (screen) g
//      Graphics titleG = g;
//      g = titleHeading.getGraphics();
//      g.drawImage(title, 0, 0, null);
//
//      g.setColor(myColor);
//      font = new Font(null, 0, titleFont);
//      g.setFont(font);
//      g.drawString("RoboJoust", titleX, titleY);
//
//      // copy offscreen stuff back to screen
//      titleG.drawImage(titleHeading, 0, 0, null);
//    }
   /* else */ if (gameLost == true) {
      bgImage = null;
      bgImage = this.createImage(mapWidth * tileSize,
                                 mapHeight * tileSize);
      Graphics bgGraphics = bgImage.getGraphics();
//        bgGraphics.drawImage(background, 0, 0, null);

      bgGraphics.drawRect(0, 0, mapWidth * tileSize, mapHeight * tileSize);
      bgGraphics.setColor(Color.red);
      bgGraphics.fillRect(0, 0, mapWidth * tileSize, mapHeight * tileSize);

      bgGraphics.setColor(Color.black);
      font = new Font(null, 0, 20);
      bgGraphics.setFont(font);
//      bgGraphics.drawString("Press any key to restart game!", 200, 350);


      if (offscreen == null) {
        offscreen = this.createImage(mapWidth * tileSize, mapHeight * tileSize);
      }
      // save original (screen) g
      Graphics screenG = g;

      g = offscreen.getGraphics();
      g.drawImage(bgImage, 0, 0, null);
      g.setColor(Color.black);
      font = new Font(null, Font.ITALIC, 50);
      g.setFont(font);
      g.drawString("GAME OVER -- \n YOU LOOOOOOSE!!!", 50, 150);

      // copy offscreen stuff back to screen
      screenG.drawImage(offscreen, 0, 0, null);

    }

    else if (gameWon == true) {
      bgImage = null;
      bgImage = this.createImage(mapWidth * tileSize,
                                 mapHeight * tileSize);
      Graphics bgGraphics = bgImage.getGraphics();
//        bgGraphics.drawImage(background, 0, 0, null);

      bgGraphics.drawRect(0, 0, mapWidth * tileSize, mapHeight * tileSize);
      bgGraphics.setColor(Color.green);
      bgGraphics.fillRect(0, 0, mapWidth * tileSize, mapHeight * tileSize);

      bgGraphics.setColor(Color.black);
      font = new Font(null, 0, 20);
      bgGraphics.setFont(font);
//      bgGraphics.drawString("Press any key to restart game!", 200, 350);


      if (offscreen == null) {
        offscreen = this.createImage(mapWidth * tileSize, mapHeight * tileSize);
      }
      // save original (screen) g
      Graphics screenG = g;

      g = offscreen.getGraphics();
      g.drawImage(bgImage, 0, 0, null);
      g.setColor(Color.black);
      font = new Font(null, Font.ITALIC, 50);
      g.setFont(font);
      g.drawString("YOU'RE A WINNER!!!", 50, 150);

      // copy offscreen stuff back to screen
      screenG.drawImage(offscreen, 0, 0, null);
    }

//     else draw the game screen

//    else {

      /**
       * if background has not been created or has changed
       * (i.e., reconstructBG == true), then redraw the background and all
       * Nonmoveable objects
       */

      if (this.playing == true){

      if (bgImage == null || reconstructBG == true) {
        bgImage = null;
//        if(noMemory()){
//          runGC();
//        }
        bgImage = this.createImage(mapWidth * tileSize,
                                   mapHeight * tileSize);
        Graphics bgGraphics = bgImage.getGraphics();

        /* draw background tiles */
        for(int i = 0; i < mapWidth; i++) {
          for(int j = 0; j < mapHeight; j++) {
            bgGraphics.drawImage(background, i*tileSize, j*tileSize, null);
          }
        }

        Iterator bgObjItr = backgroundObjs.iterator();
        while(bgObjItr.hasNext()){
          ((BackgroundObject)bgObjItr.next()).paint(bgGraphics);
        }

        /* draw unchangeable background objects */
//        for(int i = 0; i < mapWidth; i++) {
//          for (int j = 0; j < mapHeight; j++) {
//            if(map[i][j] instanceof BackgroundObject) {
//              BackgroundObject bgItem = (BackgroundObject) map[i][j];
//              bgItem.paint(bgGraphics);
//            }
//          }
//        }

        /* draw changeable background objects */
        Vector allNonmoveableObjects = ControlCenter.getAllObjects(NonMoveable.class);
        synchronized(allNonmoveableObjects) {
          Iterator it = allNonmoveableObjects.iterator();
          while (it.hasNext()) {
            GameObject item = (GameObject) it.next();
            item.paint(bgGraphics);
          }
        }
        reconstructBG(false);
      }

      /**
       * create a second image to paint moveables on top of background
       */
      if (offscreen == null) {
        offscreen = this.createImage(mapWidth * tileSize, mapHeight * tileSize);
      }
      // save original (screen) g
      Graphics screenG = g;

      g = offscreen.getGraphics();
      g.drawImage(bgImage, 0, 0, null);

      Vector allMoveableObjects = ControlCenter.getAllObjects(Moveable.class);
      synchronized (allMoveableObjects) {
        Iterator it = allMoveableObjects.iterator();
        while (it.hasNext()) {
          Moveable item = (Moveable) it.next();
//        System.out.println("Drawable: "+item+" with x "+item.x+" and y "+item.y);
          item.paint(g);
        }
      }

      // copy offscreen stuff back to screen
      screenG.drawImage(offscreen, 0, 0, null);
    }
  }

  /**
   * Set up walls on the game field. Must be enhanced to achieve multiple
   * levels or random set up.
   */
  public void setBackgroundObjects() {

    Pool p1 = new Pool();
    p1.setup(2, 3);
    backgroundObjs.add(p1);

    WhirlePool wp1 = new WhirlePool();
    wp1.setup(4, 7);
    backgroundObjs.add(wp1);
    WhirlePool wp2 = new WhirlePool();
    wp2.setup(12, 3);
    backgroundObjs.add(wp2);

    Brick brick = new Brick();
    brick.setup(mapWidth,mapHeight);
    backgroundObjs.add(brick);
  }

  /**
   * put items onto the game field
   */
  public void setItems() {
    if(map[15][1] == null) {
      HealthP hp1 = (HealthP) ControlCenter.createNewObject(HealthP.class);
      hp1.initialize();
      hp1.setup(15, 1);
    }

    if(map[1][9] == null) {
      HealthP hp2 = (HealthP) ControlCenter.createNewObject(HealthP.class);
      hp2.initialize();
      hp2.setup(1, 9);
    }

    /**
     * set up false walls
     */
    FalseBrick fb1 = (FalseBrick) ControlCenter.createNewObject(FalseBrick.class);
    fb1.setup(14, 1);
    FalseBrick fb2 = (FalseBrick) ControlCenter.createNewObject(FalseBrick.class);
    fb2.setup(15, 2);
    FalseBrick fb3 = (FalseBrick) ControlCenter.createNewObject(FalseBrick.class);
    fb3.setup(1, 8);
    FalseBrick fb4 = (FalseBrick) ControlCenter.createNewObject(FalseBrick.class);
    fb4.setup(2, 9);

    for (int j = 2; j <= mapHeight; j++) {
      FalseBrick scater = (FalseBrick) ControlCenter.createNewObject(FalseBrick.class);
      scater.setup(8, j-1);
      j++;
    }

  }

  /**
   * set up player characters, two players at the moment
   */
  public void setPlayers() {
    Robo r1 = null;
    if(droppedOutBefore){
      Vector ownObjects = ControlCenter.getOwnObjects(Robo.class);
      r1 = (Robo)ownObjects.get(0);

    }else{
      r1 = (Robo) ControlCenter.createNewObject(Robo.class);
      //r1.setRoboName();

      //r1.setHealthBar();
      if (roboNumber == 0) {
        r1.setHealthBarColor(Color.red);
        r1.setup(1, 1);
      }
      else if (roboNumber == 1) {
        r1.setImageOrientation(DOWN);
        r1.checkOrientation();
        r1.setHealthBarColor(Color.green);
        r1.setup(mapWidth - 2, mapHeight - 2);
      }
      else if (roboNumber == 2) {
        r1.setImageOrientation(DOWN);
        r1.checkOrientation();
        r1.setHealthBarColor(Color.blue);
        r1.setup(3, mapHeight - 2);
      }
      else if (roboNumber == 3) {
        r1.setImageOrientation(UP);
        r1.checkOrientation();
        r1.setHealthBarColor(Color.yellow);
        r1.setup(mapWidth - 4, 1);
      }
    }
    this.robo = r1;
    r1.change();
  }


  /**
   * Used to set the keys for the players control. TODO: eventually take
   * parameters to set keys.
   */
  private void setKeys() {
    keyTable = new int[6];
    keyTable[RoboGame.UP] = KeyEvent.VK_UP;
    keyTable[RoboGame.DOWN] = KeyEvent.VK_DOWN;
    keyTable[RoboGame.LEFT] = KeyEvent.VK_LEFT;
    keyTable[RoboGame.RIGHT] = KeyEvent.VK_RIGHT;
    keyTable[RoboGame.ACTION] = KeyEvent.VK_W;
    keyTable[RoboGame.LANCE] = KeyEvent.VK_Q;
    //keyTable[RoboGame.BOMB] = KeyEvent.VK_B;
  }

  /**
   * EventHandler
   */
  public void keyPressed(KeyEvent e) {

    if (playing) {
      int keyCode = e.getKeyCode();
      if (keyCode > KeyEvent.VK_2 && keyCode <= KeyEvent.VK_9 && totalBombs < 3) {
        //System.out.println("bomb with " + (keyCode - (int) '0'));
        robo.bomb(keyCode - (int) '0');
        totalBombs++;
        gameKey = false;
      }
      else if (keyCode == keyTable[RoboGame.UP]) {
        robo.moveUp();
        gameKey = true;
        //this.repaint();
      }
      else if (keyCode == keyTable[RoboGame.LEFT]) {
        robo.moveLeft();
        gameKey = true;
        //this.repaint();
      }
      else if (keyCode == keyTable[RoboGame.RIGHT]) {
        robo.moveRight();
        gameKey = true;
        //this.repaint();
      }
      else if (keyCode == keyTable[RoboGame.DOWN]) {
        robo.moveDown();
        gameKey = true;
        //this.repaint();
      }
      else if (keyCode == keyTable[RoboGame.ACTION]) {
        robo.action();
        gameKey = true;
      }
      else if (keyCode == keyTable[RoboGame.LANCE] /*&& totalLances < 3*/) {
        //System.out.println("firing a lance");
        robo.throwLance();
        totalLances++;
        gameKey = false;
      }
      else {
        gameKey = false;
      }

//    System.out.println("robo.x " +robo.x+ ", robo.y " +robo.y);
      if (robo.moveDone && gameKey) {
        robo.change();
      }
    }
    else if(restart) {
     playing = true;
     restart = false;

     restartGame();
    }

  }

  public void keyReleased(KeyEvent e) {
    if(playing) {
      if (e.getKeyCode() == keyTable[RoboGame.ACTION]) {
        robo.actionR();
        if (robo.moveDone) {
          robo.change();
        }
      }
    }
//    robo.change();
  }

  /**
   * keyTyped
   *
   * @param e KeyEvent
   */
  public void keyTyped(KeyEvent e) {
  }

  /**
   * windowActivated
   *
   * @param e WindowEvent
   */
  public void windowActivated(WindowEvent e) {
    this.requestFocus();
    //this.setFocusable(true);
//    this.show();
  }

  /**
   * windowClosed
   *
   * @param e WindowEvent
   */
  public void windowClosed(WindowEvent e) {
  }

  /**
   * windowClosing
   *
   * @param e WindowEvent
   */
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }

  /**
   * windowDeactivated
   *
   * @param e WindowEvent
   */
  public void windowDeactivated(WindowEvent e) {
  }

  /**
   * windowDeiconified
   *
   * @param e WindowEvent
   */
  public void windowDeiconified(WindowEvent e) {
  }

  /**
   * windowIconified
   *
   * @param e WindowEvent
   */
  public void windowIconified(WindowEvent e) {
  }

  /**
   * windowOpened
   *
   * @param e WindowEvent
   */
  public void windowOpened(WindowEvent e) {
  }

  /**
   * repaint() only used for debugging
   * @param g Graphics
   */
  //  public void repaint(){
//    System.out.println("repaint called");
//    super.repaint();
//  }

  /**
   * update called by repaint()
   * @param g Graphics
   */
  public void update(Graphics g) {
    //if we don't do this,it will just call the default update,which clears the
    //background,and repaint the background
//    System.out.println("update called");
    paint(g);
  }

  /**
   * setup all items in the level
   */
  public void setupLevel() {
    this.playing = true;
    this.setBackgroundObjects();
    this.setPlayers();
    if(this.roboNumber == 0) {
      this.setItems();
    }
  }

  /**
   * resetup all items in the level
   */
  public void reSetupLevel() {
    this.playing = true;

    switch (roboNumber) {
      case 0:
        robo.setup(1, 1);
        break;
      case 1:
        robo.setup(mapWidth - 2, mapHeight - 2);
        break;
      case 2:
        robo.setup(3, mapHeight - 2);
        break;
      case 3:
        robo.setup(mapWidth - 4, 1);
        break;
    }

    robo.setLivesBackToMax();

    if (this.roboNumber == 0) {
      this.setItems();
    }
  }


  /**
   * button actions to start the game
   * @param e ActionEvent
   */
  void buttonRobo1_actionPerformed(ActionEvent e) {

    peername = textRobo.getText();

    /**
     * wrong peer name entered, do nothing
     */
    int pLength = peername.trim().length();
    System.out.println(pLength);
    if(pLength == 0) {
      labelRobo.setText(enterPeerName);
    }
    else if(pLength > 7) {
      labelRobo.setText("Your peer name is too long, max. 7 Characters!");
      textRobo.setText("");
    }

    else {
      labelRobo.setText("Please wait, "+peername+", game is starting!");

      /**
       * Set up connections to other peers using the FRAGme ControlCenter
       */
      droppedOutBefore = ControlCenter.setUpConnections("RoboJoust0.5 TEST_H",
          peername);

      /**
       * Get number of currently joined peers, set up Robo accordingly
       */
      this.roboNumber = ControlCenter.getNoOfPeers();
      System.out.println("NO of robos: " + this.roboNumber);

      this.setupLevel();

      this.gameStarted = true;

      this.buttonRobo1.setVisible(false);
      this.pnlButton.setVisible(false);
      this.buttonRobo1 = null;
      this.pnlButton = null;

      this.textRobo.setVisible(false);
      this.pnlText.setVisible(false);
      this.textRobo = null;
      this.pnlText = null;

      runGC();
      repaint();
      this.requestFocus();
//    h2 = TestBoundary.usedMemory();
//    System.out.println("After game shows,used memory: "+h2);
//
//    System.out.println("start up uses memory of "+(h2-h1)+" bytes");
    }
  }

  class RoboGame_buttonRobo1_actionAdapter
      implements java.awt.event.ActionListener {
    RoboGame adaptee;

    RoboGame_buttonRobo1_actionAdapter(RoboGame adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.buttonRobo1_actionPerformed(e);
    }
  }

  /**
   * Dispatch received messages
   */
  public void dispatchMessage(String performative, String content) {

    //System.out.println("Dispatch message called at all");

    if (performative.equals(WIN_COND)) {
      if (content.equals(WIN)) {

      }
      else {
        //System.out.println("other player lost, i won");
        endGame(true);
      }
    }
    else if (performative.equals(RESTART_COND)) {
      if (content.equals(RESTART)) {
//        restartedPeers++;
//        if(restartedPeers == roboNumber) {
        restartThreadSuspended = false;
        if (rWT != null) {
          rWT.wakeUp();
        }
        else {
          restartGame();
        }
//        }
      }
    }

  } // dispatchMessage()

  /**
   * Robo title animation thread class
   * Inner class of Robo
   */
//  class TitleAnimation
//      extends Thread {
//
//    public TitleAnimation() {
//      start();
//    }
//
//    public void run() {
//      for (int i = 0; i < 60; i++) {
//        try {
//          titleX = 300 - i * 2;
//          titleY = 100;
//          titleFont = i;
//          repaint();
//          sleep(50);
//        }
//        catch (InterruptedException e) {
//          e.printStackTrace();
//        }
//
//      }
//      return;
//    }
//  }


  /**
   * Robo end screen animation thread class
   * Inner class of Robo
   */
  class EndAnimation
      extends Thread {

    public EndAnimation() {
      start();
    }

    public void run() {
      for (int i = 0; i < 30; i++) {
        try {
          titleX = 130 - i * 2;
          titleY = 200;
          titleFont = i;
          repaint();
          sleep(20);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
      return;
    }

  }

  // end of animator class

  class RestartWaitingThread
      extends Thread {

    public RestartWaitingThread() {
      start();
    }

    public void wakeUp() {
      restartThreadSuspended = false;
      synchronized (rWT) {
        rWT.notify();
      }
    }

    public void run() {
      try {
        if (restartThreadSuspended) {
          synchronized (rWT) {
            while (restartThreadSuspended) {
              rWT.wait();
            }
          }
        }
      }
      catch (InterruptedException ex) {
        ex.printStackTrace();
      }

      restartGame();
    }
  }

// waiting thread class

}
