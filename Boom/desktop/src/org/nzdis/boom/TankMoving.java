package org.nzdis.boom;

import java.awt.Graphics;

public class TankMoving extends Thread {

  private ActionArea aa;
  private int i;
  private Tank tank;

  public TankMoving(int i, ActionArea aa, Tank tank) {
    this.aa = aa;
    this.i = i;
    this.tank = tank;
  }

  public void run() {
    for(int j = 0; j < 10; j++) {
      tank.move(i);
      aa.moveTank();
      try {
        sleep(50);
      }catch (Exception e) {
        System.out.println("tankmomving execetilsdfh");
      }
    }
    GameFrame.tankMoving = false;
  }
}
