package org.nzdis.boom;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;

public class LandscapeSer
    extends FMeSerialised {

  //Fields in the Tank class that will change
  private int[] landscape;
  public  int[] landscapeLevel;
  private int wind;
  private int landscapeNumber = 0;


  // ------------------------------ SETTERS --------------------------------//


  public void setLandscape(int[] landscape) {
    this.landscape = landscape;
  }

  public void setLandscapeLevel(int[] landscapeLevel) {
  this.landscapeLevel = landscapeLevel;
}


  public void setWind(int wind){
    this.wind = wind;
  }

  public void setLanscapeNumber(int num){
     this.landscapeNumber = num;
   }

   // -------------------------------GETTERS -------------------------------//


   public int getLandscapeNumber(){
     return landscapeNumber;
   }

  public int[] getLandscape() {
    return landscape;
  }

   public int getWind(){
    return wind;
  }

  public int[] getLandscapeLevel() {
    return landscapeLevel;
  }




//----------------FRAGME METHODS WHICH MUST BE IMPLEMENTED---------------//
  static {
    FragMeFactory.addFactory(new Factory(), LandscapeSer.class);
  }

// static constructor is always called BEFORE the first call to a static method
  public String getSerializedObjectName() {
    return LandscapeSer.class.getName();
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new LandscapeSer();
    }
  }

  public Class getFMeObjectClassName() {
    return Landscape.class;
  }

}
