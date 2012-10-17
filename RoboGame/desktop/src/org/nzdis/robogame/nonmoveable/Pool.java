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
* Class of the Pool.
*/
public class Pool extends BackgroundObject {

  public static Image imgPool = null;

  /**
   * a static block that loads the image for pool objects
   */
  static{
      imgPool = Toolkit.getDefaultToolkit().getImage(
      "res/images/Pool.png");
      loadImage(imgPool);
  }

  /**
   * Constructor
   */
  public Pool() {
      image = imgPool;
  }

}
