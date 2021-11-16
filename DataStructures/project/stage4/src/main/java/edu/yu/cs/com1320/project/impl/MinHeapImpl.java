package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.*;
import java.util.NoSuchElementException;


public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E>{

  public MinHeapImpl() {
    this.elements = (E[]) new Comparable[10];
  }

  public void reHeapify(E element) {
    this.upHeap(this.getArrayIndex(element));
    this.downHeap(this.getArrayIndex(element));
  }

  protected int getArrayIndex(E element) {
    for (int i = 1; i < this.elements.length; i++) {
        if (element.equals(this.elements[i])) {
          return i;
        }
    }
    throw new NoSuchElementException();
  }

  protected void doubleArraySize() {
    E[] temp = (E[]) new Comparable[this.elements.length*2];
    for (int i = 1; i < this.elements.length; i++) {
      temp[i] = this.elements[i];
    }
    this.elements = temp;
    return;
  }

}
