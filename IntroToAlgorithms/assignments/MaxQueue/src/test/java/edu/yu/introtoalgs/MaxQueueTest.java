package edu.yu.introtoalgs;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

public class MaxQueueTest {
  
    
    @Test
    public void maxOnEmptyQueue() {
        MaxQueue mq = new MaxQueue();
        try {
            mq.max();
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    public void sizeOnEmptyQueue() {
        MaxQueue mq = new MaxQueue();
        assertEquals(0, mq.size());
    }

    @Test
    public void dequeueOnEmptyQueue() {
        MaxQueue mq = new MaxQueue();
        try {
            mq.dequeue();
            fail();
        } catch (NoSuchElementException e) {}
    }

    @Test
    public void basicQueueAndDequeue() {
        MaxQueue mq = new MaxQueue();
        mq.enqueue(1);
        mq.enqueue(2);
        mq.enqueue(3);
        assertEquals(1, mq.dequeue());
        assertEquals(2, mq.dequeue());
        assertEquals(3, mq.dequeue());
    }

    @Test
    public void basicSizeTest() {
        MaxQueue mq = new MaxQueue();
        mq.enqueue(1);
        assertEquals(1, mq.size());
        mq.enqueue(2);
        assertEquals(2, mq.size());
        mq.enqueue(3);
        assertEquals(3, mq.size());
        assertEquals(1, mq.dequeue());
        assertEquals(2, mq.size());
        assertEquals(2, mq.dequeue());
        assertEquals(1, mq.size());
        assertEquals(3, mq.dequeue());
    } 

    @Test 
    public void basicQueueAndDequeueWithMax() {
        MaxQueue mq = new MaxQueue();
        mq.enqueue(1);
        assertEquals(1, mq.max());
        mq.enqueue(2);
        assertEquals(2, mq.max());
        mq.enqueue(3);
        assertEquals(3, mq.max());

        
        assertEquals(1, mq.dequeue());
        assertEquals(3, mq.max());
        assertEquals(2, mq.dequeue());
        assertEquals(3, mq.max());
        assertEquals(3, mq.dequeue());
        
    } 

    public void testNOperations(int n) {
        MaxQueue queue = new MaxQueue();
        Random r = new Random();
        LinkedList<Integer> goodQueue = new LinkedList<Integer>();
        int num;
        for (int i = 0; i < n; i++) {
            if ((queue.size() == 0) || r.nextInt(3)<2) {
                num = r.nextInt(10000);
                queue.enqueue(num);
                goodQueue.add(num);
                assertTrue("Failed at Queue: " + Collections.max(goodQueue) + " is not equals to " + queue.max(), Collections.max(goodQueue).equals(queue.max()));
            } else {
                assertTrue("Fail at Dequeue: " + Collections.max(goodQueue) + " is not equals to " + queue.max(), Collections.max(goodQueue).equals(queue.max()));
                int expected = goodQueue.remove();
                int actual = queue.dequeue();
                assertTrue("Queues differ on dequeue", expected == actual);
            }
            assertEquals("goodQueue and queue do not match on isEmpty()", goodQueue.size()==0, queue.size()==0);
        }
        while (queue.size() != 0) {
            assertTrue("goodQueue is empty but queue isn't", goodQueue.size() != 0);
			assertTrue("End dequeues do not match",goodQueue.remove() == queue.dequeue());
        }
        assertTrue("queue is empty but goodQueue isn't", goodQueue.size()== 0);
       
    }

    @Test
	public void test10()
	{
		testNOperations(10);
	}
	
	@Test
	public void test100()
	{
		testNOperations(100);
	} 

    @Test
	public void test10000()
	{
		testNOperations(10000);
	}  

    



    @Test
    public void temp() {
        MaxQueue q = new MaxQueue();
        q.enqueue(69);
        assertEquals(69, q.max());
        q.enqueue(63);
        assertEquals(69, q.max());
        assertEquals(69, q.dequeue());
        assertEquals(63, q.max());

    } 
	




}