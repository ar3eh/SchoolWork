package edu.yu.introtoalgs;

import org.junit.Test;
import edu.yu.introtoalgs.MergeAnInterval.Interval;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class MergeAnIntervalTest {

    @Test 
    public void question219() {
        //Input
        Interval a = new Interval(1, 2);
        Set<Interval> intervals = new TreeSet<Interval>();
        intervals.add(a);
        Interval newInterval = new Interval(2, 3);

        //Expected Output
        Interval b = new Interval(1, 3);
        Set<Interval> expectedSet = new TreeSet<Interval>();
        expectedSet.add(b);

        //Test
        assertEquals(expectedSet, MergeAnInterval.merge(intervals, newInterval));
    }

    @Test
    public void question220() {
        //Input
        Interval a = new Interval(1, 2);
        Interval b = new Interval(4, 6);
        Set<Interval> intervals = new TreeSet<>();
        intervals.add(a);
        intervals.add(b);
        Interval newInterval = new Interval(5, 7);

        //Expected Output
        Interval c = new Interval(1, 2);
        Interval d = new Interval(4, 7);
        Set<Interval> expectedSet = new TreeSet<Interval>();
        expectedSet.add(c);
        expectedSet.add(d);

        //Test
        assertEquals(expectedSet, MergeAnInterval.merge(intervals, newInterval));
    }

    @Test
    public void question221() {
        //Input
        Interval a = new Interval(2, 3);
        Set<Interval> intervals = new TreeSet<>();
        intervals.add(a);
        Interval newInterval = new Interval(1, 10);

        //Expected Output
        Interval b = new Interval(1, 10);
        Set<Interval> expectedSet = new TreeSet<Interval>();
        expectedSet.add(b);

        //Test
        assertEquals(expectedSet, MergeAnInterval.merge(intervals, newInterval));
    }

    @Test
    public void question226() {
        //Input
        Interval a = new Interval(1, 4);
        Set<Interval> intervals = new TreeSet<>();
        intervals.add(a);
        Interval newInterval = new Interval(2, 5);

        //Expected Output
        Interval b = new Interval(1, 5);
        Set<Interval> expectedSet = new TreeSet<Interval>();
        expectedSet.add(b);

        //Test
        assertEquals(expectedSet, MergeAnInterval.merge(intervals, newInterval));
    }




}
