package org.nzdis.robogame.nonmoveable;

import org.nzdis.robogame.*;

import java.awt.*;
import org.nzdis.fragme.factory.*;
import org.nzdis.fragme.objects.*;
//import org.nzdis.fragme_old.factory.*;
//import org.nzdis.fragme_old.objects.*;

/**
* @author GAME DEVELOPMENT TEAM ONE INFO401 2004
* GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
*                                Mengqiu Wang
*                                Lincoln Johnston
*                                Hamed Hawwari
*
* Class of the Brick.
*/
public class FalseBrick extends NonMoveable implements Transferable{

  public static Image falseBrickImage = null;

  /**
   * a static block that loads the image for false bricks
   */
  static{
      falseBrickImage = Toolkit.getDefaultToolkit().getImage(
      "res/images/BreakableBrick.png");
      loadImage(falseBrickImage);
  }

  /**
   * Constructor
   */
  public FalseBrick() {
      image = falseBrickImage;
  }

  /**
   * Implementation of the getSerializedObjectClassName method from FMeObject class
   * @return Class
   */
  public Class getSerializedObjectClassName() {
    return FalseBrick_ser.class;
  }

  /**
   * Implementation of the serialize method from FMeObject class
   * @param falseBrickSer FMeSerialised
   * @return FMeSerialised
   */
  public FMeSerialised serialize(FMeSerialised falseBrickSer) {
    try {

      ( (FalseBrick_ser) falseBrickSer).setX(this.x);
      ( (FalseBrick_ser) falseBrickSer).setY(this.y);

      return falseBrickSer;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * implementation of the deserialized method from FMeObject class
   * @param serObject FMeSerialised
   */
  public void deserialize(FMeSerialised serObject) {
    this.setup(( (FalseBrick_ser) serObject).getX(),( (FalseBrick_ser) serObject).getY());
  }


  /**
   * delete the object,reconstruct the background
   */
  public void deletedObject() {
    setup(x, y);
    remove();
  }


// the factory for creating FalseBrick objects
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new FalseBrick();
    }
  }

  /**
   * a static block that adds the factory for creating FalseBrick objects into
   * FragMeFactory's factory storage
   */
  static {
    FragMeFactory.addFactory(new Factory(), FalseBrick.class);
  }

}
