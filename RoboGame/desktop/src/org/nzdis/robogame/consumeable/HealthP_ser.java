package org.nzdis.robogame.consumeable;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;

/**
 * Serialised version of HealthP.
 */
public class HealthP_ser
    extends FMeSerialised {
  public HealthP_ser() {

  }

  private int x = 0;
  private int y = 0;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

//must be implemented
  static {
    FragMeFactory.addFactory(new Factory(), HealthP_ser.class);
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new HealthP_ser();
    }
  }

  public Class getFMeObjectClassName() {
    return HealthP.class;
  }

}
