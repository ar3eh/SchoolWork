package edu.yu.cs.com1320.project.impl;

import java.util.*;
import edu.yu.cs.com1320.project.*;

public class HashTableImpl <Key, Value> implements HashTable<Key, Value> {

  class Node<Key, Value> {
    Key key;
    Value value;
    Node<Key, Value> next;
    Node (Key k, Value v) {
      if (k == null) {
        throw new IllegalArgumentException();
      }
      key = k;
      value = v;
      next = null;
    }
    Node (Key k, Value v, Node<Key, Value> n) {
      if (k == null) {
        throw new IllegalArgumentException();
      }
      key = k;
      value = v;
      next = n;
    }
  }

  private Node<?,?>[] table = new Node[5];

  public HashTableImpl() {}

  private int hashFunction(Key key) {
    return (key.hashCode() & 0x7fffffff) % this.table.length;
  }


  public Value get(Key k) {
    int index = this.hashFunction(k);
    Node currentNode = this.table[index];
    while (currentNode != null) {
      if (currentNode.key.equals(k)) {
        return (Value) currentNode.value;
      } else {
        currentNode = currentNode.next;
      }
    }
    return null;
  }

  private boolean remove(Key k) {
    int index = this.hashFunction(k);


          Node currentNode = this.table[index];
          Node previousNode = null;

          //If there is node doesn't ex
          if (currentNode == null) {
            return false;
          }

          //If the node you are deleting is the first node
          if (currentNode.key == k) {
            if (previousNode == null) {
              currentNode = currentNode.next;
              this.table[index] = currentNode;
              return true;
            }
          } else {
            previousNode = currentNode;
            currentNode = currentNode.next;
          }

          //If the node you are deleting a middle node
          while (currentNode.next != null) {
            if (currentNode.key == k) {
              previousNode.next = currentNode.next;
              return true;
            } else {
              previousNode = currentNode;
              currentNode = currentNode.next;
            }
          }

        //If the node is the last one;
        if (currentNode.key == k) {
          previousNode.next = null;
          return true;
        }

        return false;
  }


  public Value put(Key k, Value v) {
    int index = this.hashFunction(k);
    Value old = this.get(k);

    //If value is nothing
    if (v == null) {
      this.remove(k);
      return old;
    }

    //If there is no list yet
    if (this.table[index] == null) {
      this.table[index] = new Node<Key, Value>(k, v);
      return old;
    }

    //If there is a list:
      //If the key already exists

    Node currentNode = this.table[index];
    while(currentNode != null) {
      if (currentNode.key == k) {
        currentNode.value = v;
        return old;
      } else {
        currentNode = currentNode.next;
      }
    }

      //If they key doesn't exist;
    Node oldHead = this.table[index];
    this.table[index] = new Node(k, v, oldHead);
    return old;
  }
}
