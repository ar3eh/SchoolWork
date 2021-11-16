package edu.yu.introtoalgs;

import java.lang.IllegalArgumentException;
import java.util.logging.Logger;

public class IntersectRectangles {

  /** This constant represents the fact that two rectangles don't intersect.
   *
   * @see #intersectRectangle
   * @warn you may not modify this constant in any way
   */
  public final static Rectangle NO_INTERSECTION =
    new Rectangle(0, 0, -1, -1);

  /** An immutable class that represents a 2D Rectangle.
   *
   * @warn you may not modify the instance variables in any way, you are
   * encouraged to add to the current set of variables and methods as you feel
   * necesssary.
   */
  public static class Rectangle {
    // safe to make instance variables public because they are final, now no
    // need to make getters
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    /** Constructor: see the requirements doc for the precise semantics.
     *
     * @warn you may not modify the currently defined semantics in any way, you
     * may add more code if you so choose.
     */
    public Rectangle
      (final int x, final int y, final int width, final int height)
    {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    @Override
    public boolean equals(Object r) {

      if (this == r) {
        return true;
      }

      if (!(r instanceof Rectangle)) {
        return false;
      }

      Rectangle rec = (Rectangle) r;

       if ((this.x == rec.x) &&
        (this.y == rec.y) &&
        (this.width == rec.width) &&
        (this.height == rec.height)) {
         return true;
       }
       return false;
   }

   @Override
   public String toString() {
     return new String("Rectangle: [x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + "]");
   }

}



  /** If the two rectangles intersect, returns the rectangle formed by their
   * intersection; otherwise, returns the NO_INTERSECTION Rectangle constant.
   *
   * @param r1 one rectangle
   * @param r2 the other rectangle
   * @param a rectangle representing the intersection of the input parameters
   * if they intersect, NO_INTERSECTION otherwise.  See the requirements doc
   * for precise definition of "rectangle intersection"
   * @throws IllegalArgumentException if either parameter is null.
   */
  public static Rectangle intersect (final Rectangle r1, final Rectangle r2) throws IllegalArgumentException
  {

    if (r1 == null || r2 == null) {
      throw new IllegalArgumentException("Null Rectangle");
    }

  /*  if (r2.x > r1.x + r1.width) {
      return Rectangle NO_INTERSECTION;
    }
    if (r2.y > r1.y + r1.height) {
      return Rectangle NO_INTERSECTION;
    } */

    //Find the new rectangle's starting point
    int newX = (r1.x >= r2.x) ? r1.x : r2.x;
    int newY = (r1.y >= r2.y) ? r1.y : r2.y;
    int newWidth = ((r1.x + r1.width) >= (r2.x + r2.width)) ? (r2.x + r2.width - newX) : (r1.x + r1.width - newX);
    int newHeight = ((r1.y + r1.height) >= (r2.y + r2.height)) ? (r2.y + r2.height - newY) : (r1.y + r1.height - newY);

    if ((newWidth < 0) || (newHeight < 0)) {
      return NO_INTERSECTION;
    }

    final Rectangle result = new Rectangle(newX, newY, newWidth, newHeight);

    return result;

    // supply a more useful implementation!
  }


} // class
