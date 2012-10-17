package org.nzdis.boom;

import java.awt.*;
import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;

public class TankSer
    extends FMeSerialised {

  //Fields in the Tank class that will change
  private int x = 0;
  private int y = 0;
  private boolean turn;
  private boolean fire;
  private int power;
  private int angle;
  private int tankID = 0;
  private int score = 0;
  private boolean move = false;
  private int direction;
  private boolean reset = false;
  private int image;
  private String myPeerName;
  private boolean visible = false;
  private int round = 0;

  // ------------------------------ SETTERS --------------------------------//
  public void setTurn(boolean turn) {
    this.turn = turn;
  }

  public void setImage(int image) {
    this.image = image;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setFire(boolean fire) {
    this.fire = fire;
  }

  public void setPower(int power) {
    this.power = power;
  }

  public void setAngle(int angle) {
    this.angle = angle;
  }

  public void setTankID(int id) {
    this.tankID = id;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setVisible(boolean visible){
    this.visible = visible;
  }

  public void setMove(boolean move) {
    this.move = move;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public void setReset(boolean reset) {
    this.reset = reset;
  }

  public void setMyPeerName(String name) {
    this.myPeerName = name;
  }

  public void setRound(int round){
      this.round = round;
    }

  // -------------------------------GETTERS -------------------------------//


  public int getRound(){
    return round;
  }

  public boolean isTurn() {
    return turn;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isFire() {
    return fire;
  }

  public boolean isVisible(){
    return visible;
  }

  public boolean isMove() {
    return move;
  }

  public int getPower() {
    return power;
  }

  public int getAngle() {
    return angle;
  }

  public int getTankID() {
    return tankID;
  }

  public int getScore() {
    return score;
  }

  public int getDirection() {
    return direction;
  }

  public boolean getReset() {
    return reset;
  }

  public int getImage() {
    return image;
  }

  public String getMyPeerName() {
    return this.myPeerName;
  }

  //----------------FRAGME METHODS WHICH MUST BE IMPLEMENTED---------------//
  static {
    FragMeFactory.addFactory(new Factory(), TankSer.class);
  }

// static constructor is always called BEFORE the first call to a static method
  public String getSerializedObjectName() {
    return TankSer.class.getName();
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new TankSer();
    }
  }

  public Class getFMeObjectClassName() {
    return Tank.class;
  }

}
