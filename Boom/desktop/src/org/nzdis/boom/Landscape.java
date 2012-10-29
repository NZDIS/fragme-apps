package org.nzdis.boom;

import java.awt.*;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;

public class Landscape
    extends FMeObject {

  private static int width = 640; //1280; // Width of the landscape
  public static int[] landscape = new int[width]; //Array of y values for lanscape
  public static int[] landscapeLevel = new int[width]; //Array of y values for lanscape
  private static int min = 100; // Min y value
  private static int max = 350; // Max y value
  private static int numInRow = 50; //Max number in a row
  private static int wind;
  private static int landscapeNumber = 0;

  private Landscape() {
  }

  /**
   * This method is used to modify the landscape after an exlposion.
   * @param x int is the x location of where the missile hit.
   * @param size int is the radius of the missile blast.
   * @return int[] the modified landscape array.
   */
  public int[] modifyLandscape(int x, int size ) {
    int[] tempLS = this.landscape;
    for(int i = 0; i < size; i++) {
      if(x+i < width && x-i > 0){
        tempLS[x + i] = tempLS[x + i] + (size - i);
      }
    }
    for(int i = 1; i < size; i++) {
      if(x-i < width && x-i > 0){
        tempLS[x - i] = tempLS[x - i] + (size - i);
      }
    }
    this.landscape = tempLS;
    return landscape;
  }

  /**
   * This method generates random y values for the lanscape.  The values should
   * be in between the min and max values.
   * @return int[] of y values
   */
  public int[] createLandscape() {
    landscapeNumber = GameSettings.round;
    System.out.println(" land number: " + landscapeNumber);
    int x = 0;
    int y = 0;
    double num;
    double randomNum;

    //Set the wind
    wind = (int)(Math.random() * 10);
    randomNum = Math.random();
// Note: this would change the a negative number half the time but it seems
// the current missile method can't handle negative numbers.  I haven't tried
// looking into it.  - Nick
//    if( randomNum < 0.5){
//      wind = 0 - wind;
//    }


    randomNum = Math.random();

    y = (int) (randomNum * max);

    while (x < width) {
      randomNum = Math.random();
      // land is moving down
      if (randomNum < .5) {
        randomNum = Math.random() * numInRow; //how long it is going down
        while (randomNum > 0 && y > min && x < width) {
          num = Math.random();
          if (num > 0.5) {
            y = y - 1;
          }
          randomNum--;
          landscape[x] = y;
          landscapeLevel[x] = 1;
          x++;
        }
      }
      // land is moving up
      else {
        randomNum = Math.random() * numInRow; //how long it is going up
        while (randomNum > 0 && y < max && x < width) {
          num = Math.random();
          if (num > 0.5) {
            y = y + 1;
          }
          randomNum--;
          landscape[x] = y;
          landscapeLevel[x] = 2;
          x++;
        }
      }
    }

    super.change();
    return landscape;
  }

  // -------------------------- PAINTING METHODS -----------------------------//

  /**
   * This method does all the painting of the landscape.
   * @param g Graphics
   */
  public void paint(Graphics g) {
    g.setColor(GameFrame.bgc);
    g.fillRect(0,0, 640, 440);
    g.setColor(new Color(0, 128,0));
    for (int i = 0; i < 640; i++) {
      g.drawLine(i, landscape[i], i, 440);
    }
  }

  /**
   * This paints a selected area of the landscape.
   * @param x int posistion of tank.
   * @param y int poistion of tank.
   * @param g Graphics graphcis context.
   */
  public void paint(int x, int y, Graphics g) {
     g.setClip(x-3,y-3,31,31);
     for(int xCoord = x-3; xCoord <= x + 31; xCoord++) {
       g.setColor(new Color(0, 128,0));
       if( xCoord > 0 && xCoord < this.width - 20){
         g.drawLine(xCoord, landscape[xCoord], xCoord, 440);
       }
     }
  }

  /**
 * This paints a selected area of the landscape.
 * @param x int posistion of tank.
 * @param y int poistion of tank.
 * @param g Graphics graphcis context.
 */
public void paint(int x, int y, int radius, Graphics g) {
   g.setClip(x-20,y-20,radius+10,radius+10);
   g.setColor(GameFrame.bgc);
   g.fillRect(x-30,0,640,480);
   for(int xCoord = x-40; xCoord <= x + 50; xCoord++) {
     g.setColor(new Color(0, 128,0));
     if( xCoord > 0 && xCoord < this.width-1){
       g.drawLine(xCoord, landscape[xCoord], xCoord, 440);
     }
   }
}


  // ------------------------------ SETTERS --------------------------------//


  public void setLandscape(int[] landscape) {
    this.landscape = landscape;

  }

  public void setWidth(int width) {
    this.width = width;
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

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public int[] getLandscape() {
    return landscape;
  }


  public int getWidth() {
    return width;
  }

  public boolean isPointValid(int x, int y) {
    if(x < this.width && x > 0 && y < landscape[x]) {
      return true;
    }else {
      return false;
    }
  }

  public int getWind(){
    return wind;
  }

  //----------------FRAGME METHODS WHICH MUST BE IMPLEMENTED---------------//
  //---------------------------SERIALISE METHOD--------------------//
  public FMeSerialised serialize(FMeSerialised landscapeSer) {
    try {
      ( (LandscapeSer) landscapeSer).setLandscape(this.landscape);
      ( (LandscapeSer) landscapeSer).setLandscapeLevel(this.landscapeLevel);
      ( (LandscapeSer) landscapeSer).setWind(this.wind);
      ( (LandscapeSer) landscapeSer).setLanscapeNumber(this.landscapeNumber);

      return landscapeSer;
    }
    catch (Exception ex) {
      System.out.println(
          "There was an error in the Landscape serialize method!: " + ex);
      return null;
    }

  }

//---------------------------DESERIALISE METHOD--------------------//
  public void deserialize(FMeSerialised landscapeSer) {

    this.landscape = ( (LandscapeSer) landscapeSer).getLandscape();
    this.landscapeLevel = ( (LandscapeSer) landscapeSer).getLandscapeLevel();
    this.wind= ( (LandscapeSer) landscapeSer).getWind();
    this.landscapeNumber = ( (LandscapeSer) landscapeSer).getLandscapeNumber();

  }
  public void changedObject(){


  }

  public void deletedObject() {
  }


  public Class getSerializedObjectClassName() {
    return LandscapeSer.class;
  }

  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Landscape();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), Landscape.class);
  }

}
