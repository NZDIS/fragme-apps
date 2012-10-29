package org.nzdis.boom;

import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.ControlCenter;

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
import java.util.Vector;
import java.util.Iterator;

public class Splash
    extends Panel {
  Image offscreen = null;
  //public Image background = null;
  public static final int FrameWidth = 640;
  public static final int FrameHeight = 480;
  public static String message = "";
  public static String message1 = "";
  public static ArrayList moveables = new ArrayList();
  public static int count = 0;
  private static boolean drawTanks = false;
  private Image tankYellowImage = Toolkit.getDefaultToolkit().getImage(
      "res/images/tankYellow3.png");
  private Image tankRedImage = Toolkit.getDefaultToolkit().getImage(
      "res/images/tankRed2.png");
  private Color background = Color.blue;
  private Vector objs;

  public Splash() {
    setSize(FrameWidth, FrameHeight);

    // background = Toolkit.getDefaultToolkit().getImage("res/images/Nuke.jpg");

  }

  // ------------------------ WINDOW EVENT METHODS---------------------------//
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }

  // -------------------------- PAINTING METHODS ----------------------------//
  public void paint(Graphics g) {

    g.setColor(Color.blue);
    g.fillRect(0, 0, 640, 480);

    // main title
    g.setColor(Color.red);
    Font titleFont = new Font("BOLD", Font.BOLD, 40);
    g.setFont(titleFont);
    g.drawString("BOOM!", 270, 80);

    // draw images
    g.drawImage(tankRedImage, 100, 100, background, this);
    g.drawImage(tankYellowImage, 520, 100, background, this);

    // normal text
    Font normalFont = new Font(null, 0, 15);
    g.setFont(normalFont);
    g.drawString("INFO401", 305, 110);
    g.setColor(Color.yellow);
    g.drawString("Bing, Hui, Nick and Duncan", 255, 150);
    g.drawString("Version " + GameSettings.verNum, 300, 190);
    objs = ControlCenter.getAllObjects(Tank.class);

    Iterator it = objs.iterator();
    int height = 190;
    Font players = new Font("BOLD", Font.BOLD, 15);

    g.setFont(players);
    g.drawString("Current players", 17, height);
    g.setFont(normalFont);

    while (it.hasNext()) {
      FMeObject obj = (FMeObject) it.next();
      Tank tempTank = (Tank) obj;
      g.setColor(Color.yellow);
      g.drawString("Tank: " + tempTank.getTankID(), 40,
                   height + (tempTank.getTankID() * 25));
      g.drawString(tempTank.getMyPeerName(), 100,
                   height + (tempTank.getTankID() * 25));
      g.setColor(tempTank.getTankColour());
      g.fillOval(20, height - 13 + (tempTank.getTankID() * 25), 10, 6);
      g.setColor(Color.darkGray);
      g.fillOval(15, height - 8 + (tempTank.getTankID() * 25), 20, 10);
      g.setColor(Color.yellow);

    }

    if (!drawTanks) {
      if (count == 0) {
        g.drawString("Loading", 305, 250);
        count++;
      }
      else if (count == 1) {
        g.drawString("Loading.", 305, 250);
        count++;
      }
      else if (count == 2) {
        g.drawString("Loading..", 305, 250);
        count++;
      }
      else if (count == 3) {
        g.drawString("Loading...", 305, 250);
        count = 0;
      }
      g.drawString("Loading", 305, 250);
      g.drawString(message, 255, 375);
    }
    else {
      g.setColor(Color.red);
      g.fillOval(225, 345, 10, 6);
      g.setColor(Color.darkGray);
      g.fillOval(220, 350, 20, 10);
      g.setColor(Color.yellow);
      g.fillOval(325, 345, 10, 6);
      g.setColor(Color.darkGray);
      g.fillOval(320, 350, 20, 10);
      g.setColor(Color.magenta);
      g.fillOval(425, 345, 10, 6);
      g.setColor(Color.darkGray);
      g.fillOval(420, 350, 20, 10);
      g.setColor(Color.white);
      drawString(g);
    }
    g.drawString(message1, 265, 280);
  }

  public void update(Graphics g) {
    paint(g);
  }

  public void drawString(Graphics g) {
    g.drawString("Press 1 ", 210, 310);
    g.drawString("Press 2 ", 310, 310);
    g.drawString("Press 3 ", 410, 310);
    g.drawString(message, 275, 280);

  }

  // ------------------------------ SETTERS --------------------------------//

  public void setMessage(String message) {
    this.message = message;
    this.repaint();
  }

  public void setMessage1(String message) {
    this.message1 = message;
    this.repaint();
  }

  public void setDrawTanks(boolean draw) {
    this.drawTanks = draw;
  }

  public void addToMessage(String message) {
    this.message1 = message1 + message;
    this.repaint();
  }

  // ------------------------------ GETTERS ---------------------------------//

}
