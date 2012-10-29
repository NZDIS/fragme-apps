package org.nzdis.boom;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.awt.*;
import java.awt.image.*;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;

public class Tank
    extends FMeObject
    implements ImageObserver {

//  // Fields:
  private Image tankImage;
  private int x = 0;
  private int y = 0;
  private int oldX = 0;
  private int oldY = 0;
  private int width = 0;
  private int height = 0;
  private int power;
  private int angle;
  private static ActionArea aa;
  public boolean fire;
  private boolean turn;
  private boolean startTurn;
  private int tankID = 0;
  private int score = 0;
  private boolean visible = true;
  private TankSer temp;
  private boolean move = false;
  private int direction;
  private int count = 0;
  public int image = 0;
  private boolean imagesSet = false;
  private Color bgc = GameFrame.bgc;
  private Color tankColor = null;
  private String myPeerName;
  private int round = 0;

  public Tank() {
    //Setup the tank
    x = 0;
    y = 0;
    width = 30;
    height = 25;
    angle = 45;
    power = 20;
  }

  public void setImages(int image) {
    if (image == 1) {
      tankColor = Color.red;
    } else if(image == 2) {
      tankColor = Color.yellow;

    } else if(image == 3){
         tankColor = Color.magenta;
    }
  }


  // -------------------------- MOVING METHODS -----------------------------//

  /**
   * This method takes care of all the moving of the tank.  It the param is 1
   * then the tank moves to the right otherwise it moves to the left.
   * @param i int The way the tank moves
   */

  public void moveTank(int i) {
    TankMoving tankmoving = new TankMoving(i, aa, this);
    tankmoving.start();
  }

  public void move(int i) {
    if (i == 1 && this.getX() < (aa.getLandscape().getWidth() - 17)) {
      x = this.getX() + 3;
      y = Landscape.landscape[this.getX() + 1] - 17;
    }
    else if (this.getX() > 0) {
      x = this.getX() - 3;
      if(x <= 0) {
        x = 0;
      }
      y = Landscape.landscape[this.getX() + 1] - 17;
    }
  }

  // -------------------------- FIREING METHODS -----------------------------//

  public void increasePower() {
    power++;
  }

  public void decreasePower() {
    power--;
  }

  public void increaseAngle() {
    angle++;
  }

  public void decreaseAngle() {
    angle--;
  }

// -------------------------- PAINTING METHODS -----------------------------//

  /**
   * This method does all the painting of the tank.  It sets the cliping and
   * repaints the tank.
   * @param g Graphics
   */
//  public void paint(Graphics g) {
//    g.setClip(this.getX() - 3, this.getY()-3, width +3, height +3);
//    g.setColor(GameFrame.bgc);
//    g.fillRect(this.getX() - 5, this.getY()-5,33,26 );
//    g.drawImage(tankImage, getX(), getY(), GameFrame.bgc, this);
//  }


  /**
   * This method does all the painting of the tank.  It sets the cliping and
   * repaints the tank.
   * @param g Graphics
   */
  public void paint(Graphics g) {
    g.setClip(this.getX() - 5, this.getY() - 3, width + 3, height + 3);
    g.setColor(bgc);
    g.fillOval(oldX - 2, oldY + 7, 20, 10);
    g.fillOval(oldX + 3, oldY + 2, 10, 6);
    g.setColor(tankColor);
    g.fillOval(getX() + 3, getY() + 2, 10, 6);
    g.setColor(Color.darkGray);
    g.fillOval(getX() - 2, getY() + 7, 20, 10);
    oldX = getX();
    oldY = getY();

  }
//    if ( (Landscape.landscapeLevel[getX()] == 2) && (imageUp == false)) {
//      this.tankImage = this.tankImageDown;
//      g.setClip(this.getX() - 3, this.getY() - 3, width + 3, height + 3);
//      g.drawImage(tankImageDowna, oldX, oldY, this);
//      g.drawImage(tankImage, getX(), getY(), this);
//      oldX = getX();
//      oldY = getY();
//      imageUp = false;
//    }
//    else if ( (Landscape.landscapeLevel[getX()] == 2) && (imageUp == true)) {
//      this.tankImage = this.tankImageDown;
//      g.setClip(this.getX() - 3, this.getY() - 3, width + 3, height + 3);
//      g.drawImage(tankImageUpa, oldX, oldY, this);
//      g.drawImage(tankImage, getX(), getY(), this);
//      oldX = getX();
//      oldY = getY();
//      imageUp = false;
//    }
//    else if ( (Landscape.landscapeLevel[getX()] == 1) && (imageUp == true)) {
//      this.tankImage = this.tankImageUp;
//      g.setClip(this.getX() - 3, this.getY() - 3, width + 3, height + 3);
//      g.drawImage(tankImageUpa, oldX, oldY, this);
//      g.drawImage(tankImage, getX(), getY(), this);
//      oldX = getX();
//      oldY = getY();
//      imageUp = true;
//    }
//    else {
//      this.tankImage = this.tankImageUp;
//      g.setClip(this.getX() - 3, this.getY() - 3, width + 3, height + 3);
//      g.drawImage(tankImageDowna, oldX, oldY, this);
//      g.drawImage(tankImage, getX(), getY(), this);
//      oldX = getX();
//      oldY = getY();
//      imageUp = true;
//    }
//  }

  /**
   * This method just updates the tank.
   * @param g Graphics
   */
  public void update(Graphics g) {
    paint(g);
  }

  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width,
                             int height) {
    return true;
  }

  // ------------------------------ SETTERS --------------------------------//

  public void setStartTurn(boolean startTurn) {
    this.startTurn = startTurn;
  }

  public void setRound(int round) {
    this.round = round;
  }



  public void setLocation(int x, int y) {
    round = GameSettings.round;
    System.out.println("Set Location");
    this.x = x;
    this.y = y;
    super.change();
  }

  public void setActionArea(ActionArea aa) {
    this.aa = aa;
  }

  public void setFire(boolean fire) {
    this.fire = fire;
    this.change();
    this.fire = !fire;
  }

  public void setMove(boolean move) {
    this.move = move;
    this.change();
    this.move = !move;
  }

  public void setTankID(int id) {
    this.tankID = id;
    super.change();
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public void setMyPeerName(String name){
    this.myPeerName = name;
  }



  public void setImage(int image) {
    this.image = image;
  }

  public void addToScore(int add) {
    score += add;
  }

  public void subScore(int sub) {
    score -= sub;
  }

  // -------------------------------GETTERS -------------------------------//

  public int getPower() {
    return this.power;
  }

  public int getAngle() {
    return this.angle;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getTankID() {
    return tankID;
  }

  public Color getTankColour(){
    return tankColor;
  }

  public int getRound() {
    return round;
  }

  public boolean isStartTurn() {
    return startTurn;
  }

  public boolean isFire() {
    return fire;
  }

  public ActionArea getActionArea() {
    return aa;
  }

  public boolean isVisible() {
    return visible;
  }

  public int getScore() {
    return score;
  }

  public int getDirection() {
    return direction;
  }

  public int getImage() {
    return image;
  }

  public String getMyPeerName(){
    return this.myPeerName;
  }


  //----------------FRAGME METHODS WHICH MUST BE IMPLEMENTED---------------//
  //---------------------------SERIALISE METHOD--------------------//
  public FMeSerialised serialize(FMeSerialised tankSer) {
    try {
      ( (TankSer) tankSer).setX(this.x);
      ( (TankSer) tankSer).setY(this.y);
      ( (TankSer) tankSer).setAngle(this.angle);
      ( (TankSer) tankSer).setPower(this.power);
      ( (TankSer) tankSer).setFire(this.fire);
      ( (TankSer) tankSer).setMove(this.move);
      ( (TankSer) tankSer).setRound(this.round);
      ( (TankSer) tankSer).setTankID(this.tankID);
      ( (TankSer) tankSer).setDirection(this.direction);
      ( (TankSer) tankSer).setImage(this.image);
      ( (TankSer) tankSer).setMyPeerName(this.myPeerName);
      ( (TankSer) tankSer).setScore(this.score);
      ( (TankSer) tankSer).setVisible(this.visible);

      return tankSer;
    }
    catch (Exception ex) {
      System.out.println("There was an error in the tank serialize method!: " +
                         ex);
      return null;
    }

  }

  //---------------------------DESERIALISE METHOD--------------------//
  public void deserialize(FMeSerialised tankSer) {
    this.x = ( ( (TankSer) tankSer).getX());
    this.y = ( ( (TankSer) tankSer).getY());
    this.power = ( ( (TankSer) tankSer).getPower());
    this.angle = ( ( (TankSer) tankSer).getAngle());
    this.round = ( ( (TankSer) tankSer).getRound());
    this.tankID = ( ( (TankSer) tankSer).getTankID());
    this.direction = ( ( (TankSer) tankSer).getDirection());
    this.image = ( ( (TankSer) tankSer).getImage());
    this.myPeerName = ( ( (TankSer) tankSer).getMyPeerName());
    this.score = ( ( (TankSer) tankSer).getScore());
    this.visible = ( ( (TankSer) tankSer).isVisible());
    temp = (TankSer) tankSer;

  }

  public void changedObject() {
    if ( ( (TankSer) temp).isFire()) {
      System.out.println("Fired tank on other players");
      System.out.println(aa);
      this.aa.tankFire(this);
    }

    if ( ( (TankSer) temp).isMove()) {
      System.out.println("Tank moving " + this.getTankID());
      this.moveTank(this.direction);

    }

    if(this.imagesSet != true){
      setImages(( ( (TankSer) temp).getImage()));
      System.out.println("Image: " + ( ( (TankSer) temp).getImage()));
    }

    aa.moveTank();
  }

  public void deletedObject() {
  }

  public Class getSerializedObjectClassName() {
    return TankSer.class;
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Tank();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), Tank.class);
  }

}
