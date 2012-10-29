
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

public class GameOver
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


  public GameOver(ArrayList tankArray) {
    setSize(FrameWidth, FrameHeight);
    this.tankArray = tankArray;
  }

  // ------------------------ WINDOW EVENT METHODS---------------------------//
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }

  public void sethaha() {
    this.haha = true;
  }

  public boolean gethaha() {
    this.haha = true;
    return haha;
  }

  // -------------------------- PAINTING METHODS ----------------------------//
  public void paint(Graphics g) {
    this.g = g;
    g.setColor(Color.blue);
    g.fillRect(0, 0, 640, 480);
    g.setColor(Color.blue);
    Logo logo = new Logo();
    logo.run();
    g.setColor(Color.blue);
    g.fillRect(0, 0, 640, 480);
    g.setColor(Color.red);
    Font gameOverFont = new Font(null, 0, 30);
    g.setFont(gameOverFont);
    g.drawString("GAME OVER",165,100);
    Iterator it = tankArray.iterator();
    Font normalFont = new Font(null, 0, 15);
    g.setFont(normalFont);
    while (it.hasNext()) {
      Tank t = (Tank) it.next();
      String tk = String.valueOf(t.getTankID());
      g.fillOval(140, 150 - 13 + (t.getTankID() * 20), 10, 6);
      g.setColor(Color.darkGray);
      g.fillOval(135, 150 - 8 + (t.getTankID() * 20), 20, 10);
      g.setColor(Color.yellow);

      g.drawString("Tank: " + t.getTankID(), 175 , 150 + (t.getTankID() * 20));
      g.drawString("Player: " + t.getMyPeerName(),280 , 150 + (t.getTankID() * 20));
      g.drawString("Score: " + t.getScore(), 415, 150 + (t.getTankID() * 20));
    this.sethaha();
  }

  }

  public void update(Graphics g) {
    paint(g);
  }

  class Logo
      extends Thread {
    public void paint(Graphics g) {
      int x = 200;
      int y = 200;
      int width = 30;
      int height = 30;

      for (int v =1;v <= 5;v++) {
        g.setColor(Color.yellow);
        g.fillOval(205, 205, 20, 20);
        g.setColor(Color.white);
        g.fillOval(210, 210, 10, 10);

        try {
          sleep(200);
        }
        catch (Exception e) {
          System.out.println("error while painting explosion");
        }

        g.setColor(Color.green);
        Font gameOverFont = new Font(null, 0, 50);
        g.setFont(gameOverFont);
        g.drawString("GAME OVER",200,200);



        g.setColor(Color.yellow);
        g.fillOval(200, 200, width, height);
        g.setColor(Color.white);
        g.fillOval(x + 10, y + 10, width - 20, height - 20);

        g.setColor(Color.yellow);
        g.fillOval(100, 100, width, height);
        g.setColor(Color.white);
        g.fillOval(110, 110, width - 20, height - 20);

        g.setColor(Color.yellow);
        g.fillOval(300, 300, width, height);
        g.setColor(Color.white);
        g.fillOval(300, 310, width - 20, height - 20);

        g.setColor(Color.yellow);
        g.fillOval(400, 300, width, height);
        g.setColor(Color.white);
        g.fillOval(400, 310, width - 20, height - 20);

        g.setColor(Color.yellow);
        g.fillOval(500, 200, width, height);
        g.setColor(Color.white);
        g.fillOval(500, 210, width - 20, height - 20);

        g.setColor(Color.yellow);
        g.fillOval(600, 100, width, height);
        g.setColor(Color.white);
        g.fillOval(600, 110, width - 20, height - 20);


        try {
          sleep(200);
        }
        catch (Exception e) {
          System.out.println("error while painting explosion");
        }
        g.setColor(Color.red);
        g.fillOval(x, y, width, height);
        g.setColor(Color.yellow);
        g.fillOval(x + 5, y + 5, width - 10, height - 10);
        g.setColor(Color.white);
        g.fillOval(x + 10, y + 10, width - 20, height - 20);

        g.setColor(Color.red);
        g.fillOval(100, 100, width, height);
        g.setColor(Color.yellow);
        g.fillOval(105, 105, width - 10, height - 10);
        g.setColor(Color.white);
        g.fillOval(110, 110, width - 20, height - 20);

        g.setColor(Color.red);
        g.fillOval(300, 300, width, height);
        g.setColor(Color.yellow);
        g.fillOval(305, 305, width - 10, height - 10);
        g.setColor(Color.white);
        g.fillOval(310, 310, width - 20, height - 20);

        g.setColor(Color.red);
        g.fillOval(400, 300, width, height);
        g.setColor(Color.yellow);
        g.fillOval(405, 305, width - 10, height - 10);
        g.setColor(Color.white);
        g.fillOval(410, 310, width - 20, height - 20);

        g.setColor(Color.red);
        g.fillOval(500, 200, width, height);
        g.setColor(Color.yellow);
        g.fillOval(505, 205, width - 10, height - 10);
        g.setColor(Color.white);
        g.fillOval(510, 210, width - 20, height - 20);

        g.setColor(Color.red);
        g.fillOval(600, 100, width, height);
        g.setColor(Color.yellow);
        g.fillOval(605, 105, width - 10, height - 10);
        g.setColor(Color.white);
        g.fillOval(610, 110, width - 20, height - 20);


        try {
          sleep(200);
        }
        catch (Exception e) {
          System.out.println("error while painting explosion");
        }
      }
      g.setColor(Color.blue);

      g.drawString("GAME OVER",200,200);
      g.fillOval(200,200,30,30);
      g.fillOval(100,100,30,30);
      g.fillOval(300,300,30,30);
      g.fillOval(400,300,30,30);
      g.fillOval(500,200,30,30);
      g.fillOval(600,100,30,30);

    }

    public void run() {
      paint(g);
    }
  }

}
