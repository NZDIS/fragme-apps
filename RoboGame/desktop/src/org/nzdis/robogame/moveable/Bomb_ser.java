package org.nzdis.robogame.moveable;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import java.awt.Color;

/**
 * Serialised version of bomb
 */
public class Bomb_ser
    extends FMeSerialised {
  public Bomb_ser() {

  }

  private int x = 0;
  private int y = 0;
  private boolean positionSet = false;
  private int timestamp;
  private Color bombColor;

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }

  public int getTimestamp(){
    return this.timestamp;
  }

  public Color getColor(){
    return this.bombColor;
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

  public void setTimestamp(int timestamp){
    this.timestamp = timestamp;
  }

  public void setColor(Color color){
    this.bombColor = color;
  }

  public void setPositionSet(boolean positionSet){
    this.positionSet = positionSet;
  }

//must be implemented
  static {
    FragMeFactory.addFactory(new Factory(), Bomb_ser.class);
  }


  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Bomb_ser();
    }
  }

  public Class getFMeObjectClassName() {
   return Bomb.class;
 }

}
