package org.nzdis.robogame.nonmoveable;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;


public class FalseBrick_ser
    extends FMeSerialised {

  private int x = 0;
  private int y = 0;

  /**
   * getter for x coordinate
   * @return int x coordinate
   */
  public int getX(){
    return x;
  }

  /**
   * getter for y coordinate
   * @return int y coordinate
   */
  public int getY(){
    return y;
  }

  /**
   * setter for x coordinate
   * @return int x coordinate
   */
  public void setX(int x){
    this.x = x;
  }

  /**
   * setter for y coordinate
   * @return int y coordinate
   */
  public void setY(int y){
    this.y = y;
  }

  /**
   * a static block that adds the factory for creating FalseBrick_ser objects into
   * FragMeFactory's factory storage
   */
  static {
    FragMeFactory.addFactory(new Factory(), FalseBrick_ser.class);
  }

  /**
   * the factory for creating FalseBrick_ser object
   */
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new FalseBrick_ser();
    }
  }

  /**
   * Implementation of the getFMeObjectClassName method from FMeSerialized class
   * @return Class
   */
  public Class getFMeObjectClassName() {
   return FalseBrick.class;
 }


}
