package org.nzdis.robogame.consumeable;

import org.nzdis.robogame.*;

import java.awt.*;

import org.nzdis.fragme.factory.*;
import org.nzdis.fragme.objects.*;

/**
* @author GAME DEVELOPMENT TEAM ONE INFO401 2004
* GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
*                                Mengqiu Wang
*                                Lincoln Johnston
*                                Hamed Hawwari
*
* Class for a Health Potion.
*/
public class HealthP extends NonMoveable implements Consumable, Transferable{

  private static Image imgHealthP = null;

  /* static image loader to save memory */
  static{
      imgHealthP = Toolkit.getDefaultToolkit().getImage(
      "res/images/HPotion.png");
      loadImage(imgHealthP);
    }

  /**
   * Constructor
   */
  public HealthP() {
      image = imgHealthP;
  }

  /**
   * needed in case you have to reuse an object
   */
  public void initialize() {
    image = imgHealthP;
  }

  /**
   * Delete a consumeable, maybe add some more code here?
   */
  public void deleteConsumeable() {
    delete();

    /* run GC after picking up a HP to keep memory low */
    RoboGame.runGC();
  }

  // Classes that must be implemented
  public FMeSerialised serialize(FMeSerialised hpSer) {
    try {

      ( (HealthP_ser) hpSer).setX(this.x);
      ( (HealthP_ser) hpSer).setY(this.y);
      return hpSer;
    }
    catch (Exception ex) {
      return null;
    }
  }


  public void deserialize(FMeSerialised serObject) {
      this.setup(( (HealthP_ser) serObject).getX(),( (HealthP_ser) serObject).getY());
  }

  public Class getSerializedObjectClassName() {
    return HealthP_ser.class;
  }

  /**
   * called when object is deleted. needed to tell other peers to remove HP.
   */
  public void deletedObject() {
    image = null;
    setup(x, y);
    remove();
  }

// the factory
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new HealthP();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), HealthP.class);
  }
}
