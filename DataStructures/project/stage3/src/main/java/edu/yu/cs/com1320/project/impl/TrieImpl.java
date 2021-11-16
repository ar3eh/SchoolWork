package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.lang.StringBuilder;

public class TrieImpl<Value> implements Trie<Value> {

    class Node<Value> {
      private Set<Value> val = new HashSet<Value>();
      private Node[] links = new Node[TrieImpl.alphabetSize];

      Node() {}

      Set<Value> getValues() {
        HashSet<Value> results = new HashSet<Value>();
        results.addAll(this.val);
        return results;
      }

      Value getSpecificValue(Value v) {
        for (Value value : this.val) {
          if (value.equals(v)) {
            return value;
          }
        }
        return null;
      }

      void addValue(Value v) {
        this.val.add(v);
      }

      void removeValue(Value v) {
        for (Value value : this.val) {
          if (value.equals(v)) {
            this.val.remove(value);
            return;
          }
        }
      }

      void removeAllValues() {
        this.val = new HashSet<Value>();
      }

      Node[] getLinks() {
        return this.links;
      }

      void setLink(int i, Node<Value> n) {
        this.links[i] = n;
      }

      void removeLink(int i) {
        this.links[i] = null;
      }

      boolean containsValue(Value v) {
        boolean res = false;
        for (Value value : this.val) {
          if (value.equals(v)) {
            res = true;
          }
        }
        return res;
      }

      Set<Node> getChildren() {
        Set<Node> results = new HashSet<Node>();
        for (int i = 0; i < TrieImpl.alphabetSize; i++) {
          if (links[i] != null) {
            results.add(links[i]);
          }
        }
        return results;
      }


    }

    private static final int alphabetSize = 36;
    private Node<Value> root = new Node<Value>();

    public TrieImpl() {}

    public void put(String key, Value val) {
      if (key == null) {
        throw new IllegalArgumentException();
      }

      if (val == null) {
        return;
      }

       Node<Value> current = this.root;

      for (char l : key.toCharArray()) {
        Node[] currentLinks = current.getLinks();
        int index = charToIndex(l);
        if (currentLinks[index] == null) {
          current.setLink(index, new Node<Value>());
          currentLinks = current.getLinks();;
          current = currentLinks[index];
        } else {
          current = currentLinks[index];
        }
      }
      current.addValue(val);
    }

    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
      if (key == null || comparator == null) {
        throw new IllegalArgumentException();
      }
      List<Value> results = new ArrayList<Value>();
      results.addAll(this.getValues(key));
      Collections.sort(results, comparator);
      return results;
    }

    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {

      if ((prefix == null) || (comparator == null)) {
       throw new IllegalArgumentException();
      }

      List<Value> results = new ArrayList<Value>();
      Set<Value> values = new HashSet<Value>();

      if (prefix == "") {
        return results;
      }

        List<Node> nodes = this.getAllNodesWithPrefix(prefix);

        for (Node<Value> n : nodes) {
          values.addAll(n.getValues());
        }
        results.addAll(values);
        Collections.sort(results, comparator);
        return results;
    }

    public Set<Value> deleteAllWithPrefix(String prefix) {
      if (prefix == null) {
        throw new IllegalArgumentException();
      }

      Set<Value> values = new HashSet<Value>();

      if (prefix == "") {
        return values;
      }


      List<Node> nodesToDelete = this.getAllNodesWithPrefix(prefix);
      for (Node<Value> n : nodesToDelete) {
        values.addAll(n.getValues());
      }

      char l = prefix.charAt(prefix.length()-1);
      int index = charToIndex(l);
      String prePrefix = prefix.substring(0, prefix.length()-1);
      Node<Value> n = this.getNode(prePrefix);
      Node[] links = n.getLinks();
      links[index] = null;

      return values;
    }

    public Set<Value> deleteAll(String key) {
      if (key == null) {
        throw new IllegalArgumentException();
      }

      Set<Value> results = new HashSet<Value>();

      if (key == "") {
        return results;
      }

      results.addAll(this.getValues(key));
      Node<Value> n = this.getNode(key);
      n.removeAllValues();
      this.cleanUp(key);
      return results;
    }

    public Value delete(String key, Value val) {
      if ((val == null) || ( key == null)) {
        throw new IllegalArgumentException();
      }

      Node<Value> n = this.getNode(key);

      if (n == null) {
        return null;
      }

      if (n.containsValue(val)) {
        Value valToDel = n.getSpecificValue(val);
        n.removeValue(valToDel);
        this.cleanUp(key);
        return valToDel;
      }

      return null;
    }

    private Node<Value> getNode(String key) {
      Node<Value> current = this.root;
      for (char l : key.toCharArray()) {
        Node[] currentLinks = current.getLinks();
        int index = charToIndex(l);
        if (currentLinks[index] == null) {
          return null;
        } else {
          current = currentLinks[index];
        }
      }
      return current;
    }

    private Set<Node> getAllNodes(Node<Value> x) {
      Set<Node> results = new HashSet<Node>();
      this.getAllNodes(x, results);
      return results;
    }

    private Set<Node> getAllNodes(Node<Value> x, Set<Node> nodes) {
      Set<Node> toAdd = new HashSet<Node>();
      toAdd.add(x);
      for (Node<Value> n : x.getChildren()) {
        toAdd.addAll(getAllNodes(n, nodes));
      }
      nodes.addAll(toAdd);
      return nodes;
    }

    private Set<Value> getValues(String key) {
      Node<Value> n = this.getNode(key);
      Set<Value> values = new HashSet<Value>();
      if (n == null) {
        return values;
      }
      values.addAll(n.getValues());
      return values;
    }

    private List<Node> getAllNodesWithPrefix(String prefix) {
      List<Node> results = new ArrayList<Node>();
      Node<Value> prefixNode = this.getNode(prefix);
      if (this.getNode(prefix) == null) {
        return results;
      }
      results.addAll(this.getAllNodes(prefixNode));
      return results;
    }

    private void cleanUp(String key) {
      StringBuilder sb = new StringBuilder();
      sb.append(key);
      while (sb.length() != 0) {
        Node n = this.getNode(sb.toString());

        if (!n.getChildren().isEmpty() || n.getValues().size() != 0) {
          return;
        }

        char l = sb.charAt(sb.length() - 1);
        int index = charToIndex(l);
        sb.deleteCharAt(sb.length() - 1);
        Node parent = this.getNode(sb.toString());

        if (parent.getChildren().size() == 1) {
          parent.removeLink(index);
        } else {
          return;
        }
      }
    }

    private int charToIndex(char l) {
      int index;
      if (l > 64) {
        index = Character.toUpperCase(l) - 65;
      } else {
        index = Character.toUpperCase(l) - 22;
      }
      return index;
    }
}
