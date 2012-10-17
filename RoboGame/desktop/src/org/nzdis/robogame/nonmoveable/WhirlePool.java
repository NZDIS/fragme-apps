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
* Class of the WhirlePool.
*/
public class WhirlePool extends BackgroundObject{

  public static Image imgWhirl = null;

  /**
   * a static block that loads the image for WhirlePool objects
   */
  static {
    imgWhirl = Toolkit.getDefaultToolkit().getImage(
        "res/images/WhirlPool.png");
    loadImage(imgWhirl);
  }

  /**
   * Constructor
   */
  public WhirlePool() {
      image = imgWhirl;
  }

}
