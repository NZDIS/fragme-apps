package org.nzdis.boom;

import java.util.Iterator;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;

public class GameSettings
    extends FMeObject {

  public static final double verNum = 0.60;
  private static boolean gameStarted = false;
  private static int wind;
  public static int playersNum = 0;
  public static int playersMin = 2;
  public static int playersMax = 2;
  public static int round = 1;
  public static int roundMax = 3;
  public static int tankTurn = 1;
  public static boolean roundOver = false;
  public static boolean newRound = false;
  public static ActionArea aa;
  private GameSettings() {}

  // ------------------------------ SET NEXT TANK TURN --------------------------------//

  public void nextTurn() {
    System.out.println("Next turn called in Game Settings");

    int count = 0;
    int tankNumber = 0;
    int nextTankID = -1;
    int preTankID = -1;
    boolean nextTank = false;
    Iterator it;
    preTankID = tankTurn;

    // Find the next tanks ID
    while (count <= ActionArea.tankArray.size() && !nextTank) {
      count++;
      if (ActionArea.tankArray.size() == tankTurn) {
        nextTankID = 1;
      }
      else {
        nextTankID = tankTurn + 1;
      }

      tankTurn = nextTankID;
      it = ActionArea.tankArray.iterator();
      tankNumber = 0;

      while (it.hasNext()) {
        Tank t = (Tank) it.next();

        if (t.isVisible()) {
          tankNumber++;
          System.out.println("t visible");
            System.out.println("Next Tank ID: " + nextTankID);
          if ( (t.getTankID() == nextTankID) && t.isVisible()) {
            System.out.println("inside t visible");
            System.out.println("inside Next Tank ID: " + nextTankID);
            nextTank = true;
          }
        }
      }
    }
//Check to see if there is more than one tank still in the game
    if (tankNumber < 2) {

      nextTank = false;
      it = ActionArea.tankArray.iterator();
      while (it.hasNext()) {
        Tank t = (Tank) it.next();
        if(t.isVisible()){
          t.addToScore(1000);
        }
        t.setVisible(false);
        t.change();
      }

    }



      if (nextTank) {
        ActionArea.lblTurn.setText(String.valueOf(tankTurn));

      } else {
       System.out.println("Round Over");
       round ++;
       aa.getLandscape().createLandscape();
       aa.getLandscape().change();
       roundOver = true;
       super.change();
       if(round > 3){
         aa.add(new GameOver(aa.tankArray));
         aa.repaint();
       } else{
         aa.preRoundOver();
         aa.roundOver();
       }
      }







  }

  // ------------------------------ SETTERS --------------------------------//

  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }

  public void setActionArea(ActionArea aa){
    this.aa = aa;
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

  public void setRoundOver(boolean roundOver){
    this.roundOver = roundOver;
  }
  public void setNewRound(boolean newRound){
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

  public boolean getRoundOver(){
    return roundOver;
  }

  public boolean getNewRound(){
   return newRound;
 }


  public void addPlayer() {
    playersNum++;
    System.out.println("Number of players in GameSetting Class + " + playersNum);
  }

//----------------FRAGME METHODS WHICH MUST BE IMPLEMENTED---------------//
//---------------------------SERIALISE METHOD--------------------//
  public FMeSerialised serialize(FMeSerialised gameSettingsSer) {
    try {
      ( (GameSettingsSer) gameSettingsSer).setGameStarted(this.gameStarted);
      ( (GameSettingsSer) gameSettingsSer).setWind(this.wind);
      ( (GameSettingsSer) gameSettingsSer).setPlayersNum(this.playersNum);
      ( (GameSettingsSer) gameSettingsSer).setRound(this.round);
      ( (GameSettingsSer) gameSettingsSer).setTankTurn(this.tankTurn);
      ( (GameSettingsSer) gameSettingsSer).setRound(this.round);
      ( (GameSettingsSer) gameSettingsSer).setRoundOver(this.roundOver);
      ( (GameSettingsSer) gameSettingsSer).setNewRound(this.newRound);
      return gameSettingsSer;
    }
    catch (Exception ex) {
      System.out.println(
          "There was an error in the Landscape serialize method!: " + ex);
      return null;
    }

  }

//---------------------------DESERIALISE METHOD--------------------//
  public void deserialize(FMeSerialised gameSettingsSer) {

    this.gameStarted = ( (GameSettingsSer) gameSettingsSer).isGameStarted();
    this.wind = ( (GameSettingsSer) gameSettingsSer).getWind();
    this.playersNum = ( (GameSettingsSer) gameSettingsSer).getPlayersNum();
    this.round = ( (GameSettingsSer) gameSettingsSer).getRound();
    this.tankTurn = ( (GameSettingsSer) gameSettingsSer).getTankTurn();
    this.round = ( (GameSettingsSer) gameSettingsSer).getRound();
    this.roundOver = ( (GameSettingsSer) gameSettingsSer).getRoundOver();
    this.newRound = ( (GameSettingsSer) gameSettingsSer).getNewRound();
  }

  public void changedObject() {
    if(round > 3){
         aa.add(new GameOver(aa.tankArray));
       } else{
         if (!roundOver) {
           ActionArea.lblTurn.setText(String.valueOf(tankTurn));

           if ( (GameFrame.tank.getTankID() == tankTurn)) {
             ActionArea.setActionAreaVar();
             if(aa.gameSettings.isGameStarted()){
               aa.trunTimer();
             }
           }
         }
         else {
           aa.roundOver();
         }

         if (newRound) {
           aa.newRound();
           newRound = false;
         }
       }

  }

  public void deletedObject() {
  }

  public Class getSerializedObjectClassName() {
    return GameSettingsSer.class;
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new GameSettings();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), GameSettings.class);
  }

}
