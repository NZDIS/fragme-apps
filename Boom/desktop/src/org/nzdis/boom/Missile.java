package org.nzdis.boom;

import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import java.awt.*;
import java.awt.image.*;
import java.awt.Point;
import java.util.ArrayList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Missile

    implements ImageObserver {

  private Image missileImage;
  private int oldX = 0;
  private int oldY = 0;
  private int x = 0;
  private int y = 0;
  private int width = 0;
  private int height = 0;

  private int distanceTravelled = 0;
  private int angle = 0;
  private int power = 0;
  private int startX = 0;
  private int startY = 0;
  private ArrayList points = new ArrayList();
  private Parabola para;
  private Color bgc = GameFrame.bgc;


  public Missile() {

  }

  public void setup(int xStart, int yStart, int angle, int power, int wind) {
    System.out.println("Setup");
    this.setLocation(xStart, yStart); // set the missile start point.
    this.missileImage = initImage(); // set the image.
    this.startX = xStart;
    this.startY = yStart;
    this.angle = angle;
    this.power = power;
    para = new Parabola( (double) power, (double) angle, wind);

  }

  // -------------------------- MOVING METHODS -----------------------------//
  public void move() {
    this.oldX = getX();
    this.oldY = getY();

   // System.out.println("Para " + para);
    Point tempPoint = para.points[distanceTravelled];

    int x = (int) tempPoint.x + this.startX;
    int y = ( (int) tempPoint.y - (int) tempPoint.y * 2) + startY;
    this.setLocation(x, y);
    distanceTravelled++;

  }

  // -------------------------- PAINTING METHODS -----------------------------//

  public void paint(Graphics g, boolean explode) {
    g.setClip(oldX-20,oldY-20,40,40);
    Explosion explosion = new Explosion(oldX-20, oldY-20, 40, 40, g);
  }

  public void paint(Graphics g) {
    move();
    g.setColor(bgc);
    g.setClip(oldX,oldY,width,height);
    g.fillOval(oldX,oldY,width,height);
    g.setClip(x,y,width,height);
    g.setColor(Color.black);
    g.fillOval(x,y,width,height);
  }

  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width,
                             int height) {
    return true;
  }

  public Image initImage() {
    Image img = Toolkit.getDefaultToolkit().getImage("res/images/missile.png");
    this.width = 10;
    this.height = 10;
    return img;
  }

  public void update(Graphics g) {
    paint(g);
  }

  // ------------------------------ SETTERS --------------------------------//

  public void setLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }



  // -------------------------------GETTERS --------------------------------//

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }



}
