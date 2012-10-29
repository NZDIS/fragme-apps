package org.nzdis.boom;

import java.awt.*;

public class Explosion extends Thread {

  private int x;
  private int y;
  private int width;
  private int height;
  private Graphics g;
//  private Thread t;

  public Explosion(int x, int y, int width, int height, Graphics g) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.g = g;
    this.run();
  }

  public void paint(Graphics g) {
    g.setColor(Color.white);
    g.fillOval(x + 10, y + 10, width - 20, height - 20);
    try {
      sleep(200);
    }
    catch (Exception e) {
      System.out.println("error while painting explosion");
    }
    g.setColor(Color.yellow);
    g.fillOval(x, y, width, height);
    g.setColor(Color.white);
    g.fillOval(x + 10, y + 10, width - 20, height - 20);
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
    try {
     sleep(200);
   }
   catch (Exception e) {
     System.out.println("error while painting explosion");
   }
   g.setColor(GameFrame.bgc);
   g.fillOval(x, y, width, height);
   g.setColor(Color.yellow);
   g.fillOval(x+5, y+5, width-10, height-10);
   g.setColor(Color.white);
   g.fillOval(x + 10, y + 10, width - 20, height - 20);
   try {
      sleep(200);
    }
    catch (Exception e) {
      System.out.println("error while painting explosion");
    }
    g.setColor(GameFrame.bgc);
    g.fillOval(x, y, width, height);
    g.setColor(Color.white);
    g.fillOval(x + 10, y + 10, width - 20, height - 20);
    try {
      sleep(200);
    }
    catch (Exception e) {
      System.out.println("error while painting explosion");
    }
    g.setColor(GameFrame.bgc);
    g.fillOval(x + 10, y + 10, width - 20, height - 20);
  }
  public void run() {
    paint(g);

  }
}
