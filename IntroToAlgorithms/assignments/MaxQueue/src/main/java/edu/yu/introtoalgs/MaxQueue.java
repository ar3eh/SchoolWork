package edu.yu.introtoalgs;

import java.util.NoSuchElementException;
import java.util.Stack;

public class MaxQueue {

  private Stack<Element> in = new Stack<Element>();
  private Stack<Element> out = new Stack<Element>();
  private Integer sizeIn = 0, sizeOut = 0, maxOut, maxIn;


  public MaxQueue() {}

 
  public void enqueue(int x) {
    //If there is nothing in the stack
    if (sizeIn == 0 || x > maxIn) {
      Element e = new Element(x, x);
      in.push(e);
      sizeIn++;
      maxIn = x;
      return;
    } else {
      Element e = new Element (x, maxIn);
      in.push(e);
      sizeIn++;
      return;
    }
    
  }

  public int dequeue() {

    if (size() == 0) {
      throw new NoSuchElementException();
    }

    if (sizeOut >= 1) {
      return privateDequeue();
    } else {
      for (int i = 0; i < sizeIn; i++) {
        Element e = in.pop();
        if (maxOut == null) {
          out.push(new Element(e.getValue(), e.getValue()));
          maxOut = e.getValue();
        } else if (e.getValue() > maxOut) {
          out.push(new Element(e.getValue(), e.getValue()));
          maxOut = e.getValue();
        } else {
          out.push(new Element(e.getValue(), maxOut));
        }
        sizeOut++;
      }
      sizeIn = 0;
      maxIn = null;
      return privateDequeue();
    }
  }

  private int privateDequeue() {
    
    if (sizeOut > 1) {
      Element e = out.pop();
      maxOut = out.peek().getMax();
      sizeOut--;
      return e.getValue();
    } else {
      Element e = out.pop();
      maxOut = null;
      sizeOut--;
      return e.getValue();
    }
  }

  public int size() {
      return sizeIn + sizeOut;
  }

  public int max() {
      if ((maxIn == null) && (maxOut == null)) {
        throw new NoSuchElementException(); 
      } else if (maxIn == null) {
        return maxOut;
      } else if (maxOut == null) {
        return maxIn;
      } else {
        return (maxIn >= maxOut ? maxIn : maxOut);
      }
  }

  class Element {
    private int value, min, max;
  
    public Element(int value, int max) {
      this.value = value;
      this.max = max;
    }

    int getValue() {
      return this.value;
    }

    int getMax() {
      return this.max;
    }

  }
  
} 

