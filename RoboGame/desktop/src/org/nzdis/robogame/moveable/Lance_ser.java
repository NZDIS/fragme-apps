package org.nzdis.robogame.moveable;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;

/**
 * Serialised version of lance.
 */
public class Lance_ser
    extends FMeSerialised {
  public Lance_ser() {

  }

  private int x = 0;
  private int y = 0;
  private boolean positionSet = false;
  private int orientation;

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }

  public int getOrientation(){
    return this.orientation;
  }

  public boolean getPositionSet(){
    return this.positionSet;
  }

  public void setX(int x){
    this.x = x;
  }
  public void setY(int y){
    this.y = y;
  }

  public void setOrientation(int orientation){
    this.orientation = orientation;
  }

  public void setPositionSet(boolean positionSet){
    this.positionSet = positionSet;
  }

//must be implemented
  static {
    FragMeFactory.addFactory(new Factory(), Lance_ser.class);
  }


  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Lance_ser();
    }
  }

  public Class getFMeObjectClassName() {
   return Lance.class;
 }


}
