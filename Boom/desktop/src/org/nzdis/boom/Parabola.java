package org.nzdis.boom;

import java.awt.Point;
import java.util.ArrayList;

public class Parabola {

  Point [] points;
  double x;
  double y;
  double width;
  double height;
  private static final double GRAVITY = 9.82;

  Parabola(double velocity, double angle, double wind) {
    double startX = 0;
    double startY = 0;

    int counter = 0;

    double endX = startX + 400;
    double endY = startY - 400;

    double currentX = startX;
    double currentY = startY;

    double radians = Math.toRadians(angle);
    double vComp = velocity * Math.sin(radians);
    double hComp = velocity * Math.cos(radians);
    double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE,
        maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
    ArrayList nodes = new ArrayList();
    double t = 0;
  //  while (currentX <= endX){// && currentY >= endY) {
  while(counter < 200) {
      double vd = (vComp * t) + (0.5 * -GRAVITY * t * t);
      double hd = (hComp * t) + (0.5 * wind * t * t);
      t += 0.2;

      currentX = hd;
      currentY = vd;

      if (currentX < minX) {
        minX = currentX;
      }
      if (currentX > maxX) {
        maxX = currentX;
      }
      if (currentY < minY) {
        minY = currentY;
      }
      if (currentY > maxY) {
        maxY = currentY;
      }
      nodes.add(new Point((int)currentX, (int)currentY));
      counter++;
    }

    points = (Point[])nodes.toArray(new Point[nodes.size()]);
    x = minX;
    y = minY;
    width = maxX - x;
    height = maxY - y;
  }


}
