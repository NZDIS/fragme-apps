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
import java.awt.Component;
import java.awt.image.*;
import java.util.*;

public class Bird
    implements ImageObserver {

  private Image missileImage;
  private Rectangle bounds = new Rectangle(1, 1);
  private int distanceTravelled = 0;
  protected Graphics birdGraphics = null;

  public Bird(int xStart, int yStart) {
    this.setLocation(xStart, yStart); // set the missile start point.
    this.missileImage = initImage(); // set the image.
  }

  public int getX() {
    return (int) bounds.getX();
  }

  public int getY() {
    return (int) bounds.getY();
  }

  public int getWidth() {
    return (int) bounds.getWidth();
  }

  public int getHeight() {
    return (int) bounds.getHeight();
  }

  public void setLocation(int x, int y) {
    bounds.setBounds(x, y, getWidth(), getHeight());
  }

  public Image initImage() {
    Image img = Toolkit.getDefaultToolkit().getImage("res/images/face.png");
    return img;
  }

  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width,
                             int height) {
    return true;
  }

  public void update(Graphics g) {
    paint(g);
  }

  public void move() {
    bounds = getBounds();
    if (this.getX() < 640) {
      this.setLocation(this.getX() + 1, getY());
    }
    else {
      this.setLocation(30, 0);
    }
  }

  public Rectangle getBounds() {
    return bounds;
  }

  public void paint(Graphics g) {
    if (birdGraphics == null) {
      birdGraphics = g;
    }
    move();
    g.setColor(GameFrame.bgc);
    g.setClip(this.getX() - 5, this.getY() - 5, 20, 20);
    g.fillRect(this.getX() - 5, this.getY() - 5, 20, 20);
    g.drawImage(missileImage, getX(), getY(), this);
  }
}
