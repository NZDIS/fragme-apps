package org.nzdis.boom;

import java.awt.*;


/**
 *
 * <p>Title: PaintTester </p>
 * <p>Description: This class is used to test painting, think of it like a
 * scribble pad.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Duncan.
 * @version 1.0
 */
public class PaintTester extends Frame{

  private Image tankImage = Toolkit.getDefaultToolkit().getImage("res/images/tank.png");
  private Image tankYellowImage = Toolkit.getDefaultToolkit().getImage("res/images/tankYellow3.png");
  private Image tankRedImage = Toolkit.getDefaultToolkit().getImage("res/images/tankRed3.png");
  Graphics g;
  private Color background = Color.blue;

  public static void main (String args[]) {
    PaintTester pt = new PaintTester();
  }

  public PaintTester() {
    this.setSize(640,480);
    this.setBackground(Color.black);
    this.g = this.getGraphics();
    this.setVisible(true);
  }

  public void paint(Graphics g) {
    g.setColor(Color.red);
    g.fillOval(0,20,40,20);
    g.setColor(Color.white);
    g.fillOval(20, 20, 20,40);
    g.drawImage(tankRedImage, 100, 100, background, this);
    g.drawImage(tankYellowImage, 520, 100, background, this);


  }

}
