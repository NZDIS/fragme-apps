package org.nzdis.boom;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;

public class GameSettingsSer
    extends FMeSerialised {

  public GameSettingsSer() {
  }

  //Fields in the GameSettings class that will change
  private boolean gameStarted = false;
  private int wind;
  public int playersNum = 0;
  public int tankTurn = 0;
  public int round = 0;
  public boolean roundOver = false;
  public boolean newRound = false;
  private final static int playersMin = 0;
  private final static int playersMax = 0;
  private final static int roundMax = 0;

  // ------------------------------ SETTERS --------------------------------//

  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }

  public void setWind(int wind) {
    this.wind = wind;
  }

  public void setPlayersNum(int players) {
    this.playersNum = players;
  }

  public void setRound(int round) {
    this.round = round;
  }

  public void setTankTurn(int tankTurn) {
    this.tankTurn = tankTurn;
  }

  public void setRoundOver(boolean roundOver) {
    this.roundOver = roundOver;
  }

  public void setNewRound(boolean newRound) {
    this.newRound = newRound;
  }

  // -------------------------------GETTERS -------------------------------//

  public boolean isGameStarted() {
    return gameStarted;
  }

  public int getWind() {
    return wind;
  }

  public int getPlayersNum() {
    return playersNum;
  }

  public int getTankTurn() {
    return tankTurn;
  }

  public int getRound() {
    return round;
  }

  public int getRoundMax() {
    return roundMax;
  }

  public int getPlayersMin() {
    return playersMin;
  }

  public int getPlayersMax() {
    return playersMax;
  }

  public boolean getRoundOver() {
    return roundOver;
  }

  public boolean getNewRound() {
    return newRound;
  }

//----------------FRAGME METHODS WHICH MUST BE IMPLEMENTED---------------//
  static {
    FragMeFactory.addFactory(new Factory(), GameSettingsSer.class);
  }

// static constructor is always called BEFORE the first call to a static method
  public String getSerializedObjectName() {
    return GameSettingsSer.class.getName();
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new GameSettingsSer();
    }
  }

  public Class getFMeObjectClassName() {
    return GameSettings.class;
  }

}
