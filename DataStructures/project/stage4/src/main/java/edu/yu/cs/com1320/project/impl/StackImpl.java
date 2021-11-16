package edu.yu.cs.com1320.project.impl;

import java.util.LinkedList;
import edu.yu.cs.com1320.project.*;
import java.util.NoSuchElementException;

public class StackImpl<T> implements Stack<T> {

  class Node<E> {
    E element;
    Node<E> next;

    Node (E e) {
      if (e == null) {
         throw new IllegalArgumentException();
      }
      element = e;
      next = null;
    }

    Node (E e, Node<E> n) {
      if (e == null) {
        throw new IllegalArgumentException();
      }
      element = e;
      next = n;
    }
  }

  private Node<T> head = null;

  public StackImpl() {}

  public void push(T element) {
    if (head == null) {
      head = new Node(element);
      return;
    }
    Node next = head;
    head = new Node(element, next);
    return;
  }

  public T pop() {
    if (head == null) {
      return null;
    }
    T result ;
    result = head.element;
    head = head.next;
    return result;
  }

  public T peek() {
    if (this.head == null) {
      return null;
    }
    return head.element;
  }

  public int size() {
    int i = 0;
    Node currentNode = head;
    while (currentNode != null) {
      i++;
      currentNode = currentNode.next;
    }
    return i;
  }

}
