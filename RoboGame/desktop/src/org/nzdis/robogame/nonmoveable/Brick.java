package org.nzdis.robogame.nonmoveable;

import org.nzdis.robogame.*;

import java.awt.*;

/**
 * @author GAME DEVELOPMENT TEAM ONE INFO401 2004
 * GAME DEVELOPMENT TEAM ONE are: Heiko Wolf
 *                                Mengqiu Wang
 *                                Lincoln Johnston
 *                                Hamed Hawwari
 *
 * Class of the Brick.
 */
public class Brick
    extends BackgroundObject {

  public static Image brickImage = null;

  private int mapHeight;
  private int mapWidth;

  private int i;
  private int j;

  /**
   * a static block that loads the image for bricks
   */
  static {
    brickImage = Toolkit.getDefaultToolkit().getImage(
        "res/images/block.png");
    loadImage(brickImage);
  }

  /**
   * Constructor
   */
  public Brick() {
    image = brickImage;
  }

  /**
   * Brick overrides BackgroundObject's setup method,and takes mapHeight and
   * mapWidth as parameters
   * @param mapWidth int
   * @param mapHeight int
   */
  public void setup(int mapWidth, int mapHeight) {

    this.mapHeight = mapHeight;
    this.mapWidth = mapWidth;

    /**
     * set up surrounding walls
     */
    for (i = 0; i < 2; i++) {
      for (j = 0; j < mapHeight; j++) {
        map[i * (mapWidth - 1)][j] = (Object)this;
      }
    }
    for (i = 0; i < 2; i++) {
      for (j = 0; j < mapWidth - 2; j++) {
        map[1 + j][i * (mapHeight - 1)] = (Object)this;
      }
    }

    /**
     * set up brings on the game field
     */
    for (i = 2; i < mapWidth - 1; i++) {
      for (j = 2; j < mapHeight - 1; j++) {
        map[i][j] = (Object)this;
        j++;
      }
      i++;
    }

    game.reconstructBG(true);
    game.repaint();

  }

  /**
   * paint method for bricks, which drop the same brick on multiple locations
   * in the game
   * @param g Graphics
   */
  public void paint(Graphics g) {

    if (image != null) {

      for (i = 0; i < 2; i++) {
        for (j = 0; j < mapHeight; j++) {
          x = i * (mapWidth - 1);
          y = j;
          aY = map[0].length - (y + 1);
          g.drawImage(image,
                      x * tileSize,
                      aY * tileSize, this);
        }
      }
      for (i = 0; i < 2; i++) {
        for (j = 0; j < mapWidth - 2; j++) {
          map[1 + j][i * (mapHeight - 1)] = (Object)this;

          x = 1 + j;
          y = i * (mapHeight - 1);
          aY = map[0].length - (y + 1);
          g.drawImage(image,
                      x * tileSize,
                      aY * tileSize, this);
        }
      }

      /**
       * set up brings on the game field
       */
      for (i = 2; i < mapWidth - 1; i++) {
        for (j = 2; j < mapHeight - 1; j++) {
          x = i;
          y = j;
          aY = map[0].length - (y + 1);
          g.drawImage(image,
                      x * tileSize,
                      aY * tileSize, this);

          j++;
        }
        i++;
      }
    }
    else {
      System.out.println(this +"'s image is null");
    }
  }

}
