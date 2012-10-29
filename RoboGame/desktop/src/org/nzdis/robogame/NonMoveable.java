package org.nzdis.robogame;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Base class for all nonmoveable objects in RoboJoust. It includes a setup()
 * method that tells the super.setup() that the BG has to be redrawn.
 */
public class NonMoveable extends GameObject {

  /**
   * setup an object, since NonMoveables construct part of the background,
   * we need to notify the game to reconstruct the background
   * @param x int
   * @param y int
   */
  public void setup(int x, int y) {
    game.reconstructBG(true);
    super.setup(x,y);

  }

}
