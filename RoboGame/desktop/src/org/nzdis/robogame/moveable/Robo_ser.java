package org.nzdis.robogame.moveable;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import java.awt.Color;

/**
 * Robo's serialised version.
 */
public class Robo_ser
    extends FMeSerialised {
  public Robo_ser() {

  }

  private String peerName;
  private int x = 0;
  private int y = 0;
  private int oldX = 0;
  private int oldY = 0;
  private int imgOrientation = 0;
  private boolean move = true;
  private Color healthBarColor = Color.red;

  private int direction = -1;
  /**
  * maximum health of a Robo -- fixed for the moment
  */
 private int maximumHealth = 100;

 /**
  * total lives for a Robo
  */
 private int totalLives = 3;

 /**
  * number of lives set at 3 for the mo
  */
 private int noOfLives;

  /**
   * The health level, initialized at 100 (for the moment)
   */
  private int health;

  public int getDirection(){
    return direction;
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }

  public int getOrientation(){
    return this.imgOrientation;
  }

  public boolean getMove(){
    return this.move;
  }

  public int getOldX(){
    return oldX;
  }

  public int getOldY(){
    return oldY;
  }

  public int getMaxHealth(){
    return maximumHealth;
  }

  public int getHealth(){
    return health;
  }

  public int getTotalLives(){
    return totalLives;
  }

  public int getLives(){
    return noOfLives;
  }

  public String getPeerName(){
    return peerName;
  }

  public void setPeerName(String pn){
    this.peerName = pn;
  }

  public void setDirection(int direction){
    this.direction = direction;
  }

  public void setX(int x){
    this.x = x;
  }
  public void setY(int y){
    this.y = y;
  }

  public void setOldX(int x){
    this.oldX = x;
  }
  public void setOldY(int y){
    this.oldY = y;
  }

  public void setOrientation(int orientation){
    this.imgOrientation = orientation;
  }

  public void setMove(boolean move){
    this.move = move;
  }

  public void setMaxHealth(int maxH){
    this.maximumHealth = maxH;
 }

 public void setHealth(int h){
   this.health = h;
 }

 public void setTotalLives(int totalL){
   this.totalLives = totalL;
 }

 public void setLives(int l){
   this.noOfLives = l;
 }

 /**
  * getters / setters for the health bar color
  */
 public Color getHealthBarColor() {
   return healthBarColor;
 }

 public void setHealthBarColor(Color c) {
   healthBarColor = c;
 }


//must be implemented
  static {
    FragMeFactory.addFactory(new Factory(), Robo_ser.class);
  }


  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Robo_ser();
    }
  }

  public Class getFMeObjectClassName() {
   return Robo.class;
 }


}
