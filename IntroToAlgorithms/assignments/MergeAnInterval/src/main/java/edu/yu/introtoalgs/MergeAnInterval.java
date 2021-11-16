package edu.yu.introtoalgs;

import java.util.*;

/** Implements the "Add an Interval To a Set of Intervals" semantics defined in
 * the requirements document.
 * 
 * @author Avraham Leff 
 */

public class MergeAnInterval {

  /** An immutable class, holds a left and right integer-valued pair that
   * defines a closed interval
   *
   * IMPORTANT: students may not modify the semantics of the "left", "right"
   * instance variables, nor may they use any other constructor signature.
   * Students may (are encouraged to) add any other methods that they choose,
   * bearing in mind that my tests will ONLY DIRECTLY INVOKE the constructor
   * and the "merge" method.
   */
  public static class Interval implements Comparable<Interval>{
    public final int left;
    public final int right;

    /** Constructor
     * 
     * @param left the left endpoint of the interval, may be negative
     * @param right the right endpoint of the interval, may be negative
     * @throws IllegalArgumentException if left is >= right
     */
    public Interval(int l, int r) throws IllegalArgumentException {
      if (l >= r) {
        throw new IllegalArgumentException();
      }
      this.left = l;
      this.right = r;
    }

    public boolean overlap(Interval a) {
      if ((this.left >= a.left) && (this.left <= a.right)) {
        return true;
      } else if ((this.right <= a.right) && (this.right >= a.left)) {
        return true;
      } else if ((this.left <= a.left) && (this.right >= this.right)) {
        return true;
      }
      return false;
    }

    public Interval mergedInterval(Interval a) {
      int left = (this.left <= a.left ? this.left : a.left);
      int right = (this.right >= a.right ? this.right : a.right);
      return new Interval(left, right);
    }

    @Override
    public String toString() {
      return ("(" + this.left + ", " + this.right + ")");
    }

    @Override
    public int compareTo(Interval o) {
      if (this.left > o.left) {
        return 1;
      } else if (this.left < o.left) {
        return -1;
      } else {
        return 0;
      }
    }
  

  } // Interval class

  /** Merges the new interval into an existing set of disjoint intervals.
   *
   * @param intervals an set of disjoint intervals (may be empty)
   * @param newInterval the interval to be added
   * @return a new set of disjoint intervals containing the original intervals
   * and the new interval, merging the new interval if necessary into existing
   * interval(s), to preseve the "disjointedness" property.
   * @throws IllegalArgumentException if either parameter is null
   */
  
  public static Set<Interval> merge (final Set<Interval> intervals, Interval newInterval) throws IllegalArgumentException {
    if (intervals == null) {
      throw new IllegalArgumentException();
    } else if (newInterval == null) {
      throw new IllegalArgumentException();
    }

    if (intervals.isEmpty()) {
      Set<Interval> set = new HashSet<Interval>(intervals);
      set.add(newInterval);
      return set;
    }

    SortedSet<Interval> ints = new TreeSet<Interval>(intervals);

    if ((ints.last().right < newInterval.left) || (ints.first().left > newInterval.right)) {

      Set<Interval> set = new HashSet<Interval>(intervals);
      set.add(newInterval);
      ints.add(newInterval);
      return set;
    }

    Interval curInterval = newInterval;
    Set<Interval> toRemove = new TreeSet<Interval>();
    for (Interval i : ints) {
      if (curInterval.overlap(i)) {
        curInterval = curInterval.mergedInterval(i);
        toRemove.add(i);
      }
    }
    ints.removeAll(toRemove);
    ints.add(curInterval);

    for (Interval i : ints) {
      System.out.println(i);
    }

      return ints;
    }

 


}
