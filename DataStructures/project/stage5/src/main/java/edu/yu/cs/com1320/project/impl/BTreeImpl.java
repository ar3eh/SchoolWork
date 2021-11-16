package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;


public class BTreeImpl<Key extends Comparable<Key>, Value> implements BTree<Key, Value> {

  private static final int MAX = 4;
  private Node root;
  private int height;
  private int n;
  private PersistenceManager pm;

  private static final class Node {
    private int entryCount;
    private Entry[] entries = new Entry[BTreeImpl.MAX];

    private Node(int k) {
        this.entryCount = k;
    }

  }

  private static class Entry {

      private Comparable key;
      private Object val;
      private Node child;
      private Boolean file = false;

      public Entry(Comparable key, Object val, Node child) {
          this.key = key;
          this.val = val;
          this.child = child;
      }
  }

  public BTreeImpl() {
    this.root = new Node(0);
  }

  public Value get(Key key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to get() is null");
    }

    Entry entry = this.get(this.root, key, this.height);
    if (entry != null) {
     if (entry.val != null) {
       return (Value) entry.val;
     } else if (entry.file) {
       try {
         entry.val = this.pm.deserialize(key);
       } catch (IOException e) {}
       entry.file = false;
       return (Value) entry.val;
     }
    }
    return null;
  }

  private Entry get(Node currentNode, Key key, int height) {
    Entry[] entries = currentNode.entries;

    if (height == 0) {
      for (int j = 0; j < currentNode.entryCount; j++) {
          if (isEqual(key, entries[j].key)) {
              return entries[j];
          }
      }
      return null;
    } else {
      for (int j = 0; j < currentNode.entryCount; j++) {
          if (j + 1 == currentNode.entryCount || less(key, entries[j + 1].key)) {
              return this.get(entries[j].child, key, height - 1);
          }
      }
      return null;
    }
  }

  private boolean less(Comparable k1, Comparable k2) {
      return k1.compareTo(k2) < 0;
  }

  public Value put(Key key, Value val) {

    if (key == null) {
        throw new IllegalArgumentException("argument key to put() is null");
    }

    Entry alreadyThere = this.get(this.root, key, this.height);
    if (alreadyThere != null) {


        Value prev = (Value) alreadyThere.val;
        alreadyThere.val = val;
        if (val == null) {
          if (alreadyThere.file == true) {
            try {
              this.pm.delete(key);
              alreadyThere.file = false;
            } catch (IOException e) {}

          }
        }
        return prev;
    }

    Node newNode = this.put(this.root, key, val, this.height);
    this.n += 1;
    if (newNode == null) {
        return null;
    }

    Node newRoot = new Node(2);
    newRoot.entries[0] = new Entry(this.root.entries[0].key, null, this.root);
    newRoot.entries[1] = new Entry(newNode.entries[0].key, null, newNode);
    this.root = newRoot;
    this.height += 1;
    return null;

  }

  private Node put(Node currentNode, Key key, Value val, int height) {
      int j;
      Entry newEntry = new Entry(key, val, null);

      if (height == 0) {
        for (j = 0; j < currentNode.entryCount; j++) {
          if (less(key,  currentNode.entries[j].key)) {
              break;
          }
        }
      } else {
        for (j = 0; j < currentNode.entryCount; j++) {
          if ((j + 1 == currentNode.entryCount) || less(key,  currentNode.entries[j + 1].key)) {
            Node newNode = this.put(currentNode.entries[j++].child, key, val, height - 1);
            if (newNode == null) {
                return null;
            }
            newEntry.key = newNode.entries[0].key;
            newEntry.val = null;
            newEntry.child = newNode;
            newEntry.file = false;
            break;
          }
        }
      }
      for (int i = currentNode.entryCount; i > j; i--) {
          currentNode.entries[i] = currentNode.entries[i - 1];
      }
      currentNode.entries[j] = newEntry;
      currentNode.entryCount++;
      if (currentNode.entryCount < BTreeImpl.MAX) {
          return null;
      } else {
          return this.split(currentNode, height);
      }
  }

  private Node split(Node currentNode, int height) {
      Node newNode = new Node(BTreeImpl.MAX / 2);

      for (int j = 0; j < BTreeImpl.MAX / 2; j++) {
          newNode.entries[j] = currentNode.entries[BTreeImpl.MAX / 2 + j];
          currentNode.entries[BTreeImpl.MAX / 2 + j] = null;
      }

      currentNode.entryCount = BTreeImpl.MAX / 2;
      newNode.entryCount = BTreeImpl.MAX / 2;

      return newNode;
  }

  private boolean isEqual(Comparable k1, Comparable k2) {
      return k1.compareTo(k2) == 0;
  }

  public void moveToDisk(Key k) {
    Entry entry = this.get(this.root, k, this.height);
    if (entry == null) {
      return;
    }
    if (entry.val == null) {
      return;
    }
    try {
      this.pm.serialize(k, entry.val);
      entry.val = null;
      entry.file = true;
    } catch (IOException e) {
      return;
    }
    entry.val = null;
    return;
  }

  public void setPersistenceManager(PersistenceManager<Key,Value> pm) {
    this.pm = pm;
  }


}
