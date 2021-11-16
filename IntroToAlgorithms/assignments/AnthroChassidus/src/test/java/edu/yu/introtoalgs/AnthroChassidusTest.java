package edu.yu.introtoalgs;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnthroChassidusTest {

    @Test
    public void WengerTest() {
        final int n = 50;
        final int[] a = {0};
        final int[] b = {1};
        final AnthroChassidus ac = new AnthroChassidus(n, a, b);
        assertEquals(ac.getLowerBoundOnChassidusTypes(), 49);
        assertEquals(ac.nShareSameChassidus(0), 2);
        assertEquals(ac.nShareSameChassidus(1), 2);
    }

     @Test
        public void Test2() {
        final int n = 50;
        final int[] a = {1, 2};
        final int[] b = {2, 3};
        final AnthroChassidus ac = new AnthroChassidus(n, a, b);
        assertEquals(ac.getLowerBoundOnChassidusTypes(), 49);
        assertEquals(ac.nShareSameChassidus(1), 3);
        assertEquals(ac.nShareSameChassidus(2), 3);
        assertEquals(ac.nShareSameChassidus(3), 3);
    }

    @Test
    public void Test3() {
      final int n = 50;
      final int[] a = {0, 1, 3, 4};
      final int[] b = {1, 2, 4, 5};
      final AnthroChassidus ac = new AnthroChassidus(n, a, b);
      assertEquals(ac.getLowerBoundOnChassidusTypes(), 48);
      assertEquals(ac.nShareSameChassidus(0), 3);
      assertEquals(ac.nShareSameChassidus(1), 3);
      assertEquals(ac.nShareSameChassidus(2), 3);
      assertEquals(ac.nShareSameChassidus(3), 3);
      assertEquals(ac.nShareSameChassidus(4), 3);
      assertEquals(ac.nShareSameChassidus(5), 3);
    }


    @Test
    public void Test4() {
      final int n = 50;
      final int[] a = {0, 1, 3, 4, 0, 0};
      final int[] b = {1, 2, 4, 5, 5, 6};
      final AnthroChassidus ac = new AnthroChassidus(n, a, b);
      assertEquals(ac.getLowerBoundOnChassidusTypes(), 49);
      assertEquals(ac.nShareSameChassidus(0), 6);
      assertEquals(ac.nShareSameChassidus(1), 6);
      assertEquals(ac.nShareSameChassidus(2), 6);
      assertEquals(ac.nShareSameChassidus(3), 6);
      assertEquals(ac.nShareSameChassidus(4), 6);
      assertEquals(ac.nShareSameChassidus(5), 6);
    }





}
