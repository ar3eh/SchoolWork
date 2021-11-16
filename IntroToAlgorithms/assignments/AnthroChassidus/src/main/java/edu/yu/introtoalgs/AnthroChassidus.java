package edu.yu.introtoalgs;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

/** Defines and implements the AnthroChassidus API per the requirements
 * documentation.
 *
 * @author Avraham Leff
 */

public class AnthroChassidus {

  /** Constructor.  When the constructor completes, ALL necessary processing
   * for subsequent API calls have been made such that any subsequent call will
   * incur an O(1) cost.
   *
   * @param n the size of the underlying population that we're investigating:
   * need not correspond in any way to the number of people actually
   * interviewed (i.e., the number of elements in the "a" and "b" parameters).
   * Must be greater than 2.
   * @param a interviewed people, element value corresponds to a unique "person
   * id" in the range 0..n-1
   * @param b interviewed people, element value corresponds to a unique "person
   * id" in the range 0..n-1.  Pairs of a_i and b_i entries represent the fact
   * that the corresponding people follow the same Chassidus (without
   * specifying what that Chassidus is).
   */

  Integer setNumber = 0;
  int n;
  Map<Integer, HashSet<Integer>> interviewed = new HashMap<Integer, HashSet<Integer>>();

  public AnthroChassidus(final int n, final int[] a, final int[] b) {

    if (a.length != b.length) {
      throw new IllegalArgumentException();
    }

    this.n = n;

    for (int i = 0; i < a.length; i++) {

      if ((a[i] < 0) || b[i] < 0) {
        throw new IllegalArgumentException();
      }

      if (!(interviewed.keySet().contains(a[i]) || interviewed.keySet().contains(b[i]))) {
        HashSet<Integer> newSet = new HashSet<Integer>();
        newSet.add(a[i]);
        newSet.add(b[i]);
        interviewed.put(a[i], newSet);
        interviewed.put(b[i], newSet);
        setNumber++;
      } else if ((interviewed.keySet().contains(a[i]) && interviewed.keySet().contains(b[i]))) {
        HashSet<Integer> chassidusA = interviewed.get(a[i]);
        HashSet<Integer> chassidusB = interviewed.get(b[i]);
        System.out.println(interviewed.get(a[i]));
        System.out.println(interviewed.get(b[i]));
        if (!(chassidusA == interviewed.get(b[i]))) {
          chassidusA.addAll(chassidusB);
          for (Integer j : interviewed.keySet()) {
            if (interviewed.get(j) == chassidusB) {
              interviewed.put(j, chassidusA);
            }
          }
          setNumber--;
        }
        System.out.println(interviewed.get(a[i]));
        System.out.println(interviewed.get(b[i]));

      } else if ((interviewed.keySet().contains(a[i]))) {
        HashSet<Integer> chassidus = interviewed.get(a[i]);
        chassidus.add(b[i]);
        interviewed.put(b[i], chassidus);
      } else {
        HashSet<Integer> chassidus = interviewed.get(b[i]);
        chassidus.add(a[i]);
        interviewed.put(a[i], chassidus);
      }
    }

  }

  /** Return the tightest value less than or equal to "n" specifying how many
   * types of Chassidus exist in the population: this answer is inferred from
   * the interviewers data supplied to the constructor
   *
   * @return tightest possible lower bound on the number of Chassidus in the
   * underlying population.
   */
  public int getLowerBoundOnChassidusTypes() {
    return (this.n - setNumber);
  }

  /** Return the number of interviewed people who follow the same Chassidus as
   * this person.
   *
   * @param id uniquely identifies the interviewed person
   * @return the number of interviewed people who follow the same Chassidus as
   * this person.
   */
  public int nShareSameChassidus(final int id) {
    return interviewed.get(id).size();
  }

} // class
