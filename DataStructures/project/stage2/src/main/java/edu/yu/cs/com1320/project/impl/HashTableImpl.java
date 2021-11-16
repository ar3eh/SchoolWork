package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.*;


//Test
public class HashTableImpl <Key, Value> implements HashTable<Key, Value> {

  class Node<Key, Value> {
    Key key;
    Value value;
    Node<Key, Value> next;

    Node (Node<Key, Value> n) {
      key = n.key;
      value = n.value;
      next = n.next;
    }

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
  private double percentFilled = 0;

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

    if (currentNode == null) {
      return false;
    }

    this.percentFilled = (this.percentFilled * this.table.length - 1) / this.table.length;

    //If the node you are deleting is the first node
    if (currentNode.key.equals(k)) {
      this.table[index] = currentNode.next;
      return true;
    }

    //If the node you are deleting a middle node
    while (currentNode.next != null) {
      if (currentNode.key.equals(k)) {
        previousNode.next = currentNode.next;
        return true;
      } else {
        previousNode = currentNode;
        currentNode = currentNode.next;
      }
    }

    //If the node is the last one;
    if (currentNode.key.equals(k)) {
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
      checkDouble();
      return old;
    }

    this.percentFilled = ((this.percentFilled * this.table.length) + 1) / this.table.length;


    //If there is no list yet
    if (this.table[index] == null) {
      this.table[index] = new Node<Key, Value>(k, v);
      checkDouble();
      return old;
    }

    //If there is a list:
      //If the key already exists
    Node currentNode = this.table[index];
    while(currentNode != null) {
      if (currentNode.key.equals(k)) {
        currentNode.value = v;
        checkDouble();
        return old;
      } else {
        currentNode = currentNode.next;
      }
    }

      //If they key doesn't exist;
    Node oldHead = this.table[index];
    this.table[index] = new Node(k, v, oldHead);
    checkDouble();
    return old;
  }

  private void checkDouble() {
    if (this.percentFilled > 0.75) {
      Node[] oldTable = new Node[this.table.length];
      for (int i = 0; i < table.length; i++) {
        if (this.table[i] != null) {
          oldTable[i] = new Node(this.table[i]);
        }
      }

      this.table = new Node[this.table.length * 2];
      this.percentFilled = 0;

      for (int i = 0; i < oldTable.length; i++) {
        Node currentNode = oldTable[i];
        while (currentNode != null) {
          this.put((Key)currentNode.key, (Value)currentNode.value);
          currentNode = currentNode.next;
        }
      }
    }
  }

}
