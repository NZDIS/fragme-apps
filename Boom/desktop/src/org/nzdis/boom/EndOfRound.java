
package org.nzdis.boom;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class EndOfRound
    extends Panel {
  Image offscreen = null;
  public Image background = null;
  public static final int FrameWidth = 680;
  public static final int FrameHeight = 480;
  public static String message = "";
  public static String message1 = "";
  public static ArrayList moveables = new ArrayList();
  public static int count = 0;
  public static ArrayList tankArray = new ArrayList();
  public static boolean haha = false;
  public Graphics g;
  public static boolean paint = true;
  private Image tankImage = Toolkit.getDefaultToolkit().getImage("res/images/tankRed.png");

  public EndOfRound() {
    setSize(FrameWidth, FrameHeight);


  }

  public void setTankArray(ArrayList tankArray) {
    setSize(FrameWidth, FrameHeight);
   this.tankArray = tankArray;

  }


  // ------------------------ WINDOW EVENT METHODS---------------------------//
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }


  // -------------------------- PAINTING METHODS ----------------------------//
  public void paint(Graphics g) {

    this.g = g;
    g.setColor(Color.blue);
    g.fillRect(0, 0, 640, 480);
    g.setColor(Color.green);

    Font gameOverFont = new Font(null, 0, 50);
    g.setFont(gameOverFont);
    g.drawString("ROUND OVER",165,100);
    Iterator it = tankArray.iterator();
    Font normalFont = new Font(null, 0, 15);
    g.setFont(normalFont);
    while (it.hasNext()) {
      Tank t = (Tank) it.next();
      String tk = String.valueOf(t.getTankID());
      g.setColor(Color.yellow);
      g.setColor(t.getTankColour());
     g.fillOval(140, 200 - 13 + (t.getTankID() * 20), 10, 6);
     g.setColor(Color.darkGray);
     g.fillOval(135, 200 - 8 + (t.getTankID() * 20), 20, 10);
     g.setColor(Color.yellow);

      g.drawString("Tank: " + t.getTankID(), 175 , 200 + (t.getTankID() * 20));
      g.drawString("Player: " + t.getMyPeerName(),280 , 200 + (t.getTankID() * 20));
       g.drawString("Score: " + t.getScore(), 415, 200 + (t.getTankID() * 20));
      g.drawString("Press 'ENTER' to start the next round",200, 410 );
    }

  }

  public void update(Graphics g) {
    paint(g);
  }



    public void run() {
      paint(g);
    }


}
