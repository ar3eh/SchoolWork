package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;

public class TrieImpl<Value> implements Trie<Value> {

    class Node<Value> {
      private Set<Value> val = new TreeSet<Value>();
      private Node[] links = new Node[TrieImpl.alphabetSize]

      Node() {}

      Set<Value> getValues() {
        return this.val;
      }

      void addValue(Value v) {
        this.val.add(v);
      }

      void removeValue(Value v) {
        this.remove(v);
      }

      void removeAllValues() {
        for (Value v : this.val) {
          this.remove(v);
        }
      }

      Node[] getLinks() {
        return this.links;
      }

      void setLink(int i, Node n) {
        this.links[i] = n;
      }

      void removeLink(int i) {
        this.links[i] = null;
      }

      boolean containsValue(Value v) {
        if (this.val.contains(v)) {
          return true;
        }
        return false;
      }

    }

    private static final int alphabetSize = 36;
    private Node root = new Node<Value>();

    public TrieImpl() {}

    private List<Node> getChildren(Node n) {
      List<Node> results = new ArrayList<Node>();
      Node[] links = n.getLinks();

      for (int i = 0; i < TrieImpl.alphabetSize; i++) {
        if (links[i] != null) {
          results.add(links[i]);
        }
      }
      return results;
    }

    private getNode(String key) {
      Node current = this.root;
      for (char l : key.toCharArray()) {
        Node[] currentLinks = this.current.getLinks();
        int index = Character.toUpperCase(l) - 65;
        if (currentLinks[index] == null) {
          return null;
        } else {
          current = currentLinks[index];
        }
      }
    }

    private Set<Node> getAllNodes(Node x) {
      Set<Node> results = new TreeSet<Node>();
      this.getAllNodes(x, results);
      return results;
    }

    private Set<Node> getAllNodes(Node x, Set<Node> nodes) {
      nodes.add(x);
      for (Node n : this.getAllChildren(x)) {
        nodes.addAll(getAlNodes(n));
      }
    }

    private Set<Value> getValues(String key) {
      Node n = this.getNode(key);
      return n.getValues();
    }

    public void put(String key, Value val) {
      Node current = this.root;
      for (char l : key.toCharArray()) {
        Node[] currentLinks = this.current.getLinks();
        int index = Character.toUpperCase(l) - 65;
        if (currentLinks[index] == null) {
          current.setLink(index, new Node<Value>());
          currentLinks = this.current.getLinks();;
          current = currentLinks[index];
        } else {
          current = currentLinks[index];
        }
      }
      current.addValue(val);
    }

    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
      List<Value> results = new ArrayList<Value>();
      results.addAll(this.getValues(key));
      Collections.sort(results, comparator);
      return results;
    }

    private List<Value> getAllWithPrefix(String prefix) {
      List<Value> results = new ArrayList<Value>();
      Node prefixNode = this.getNode(prefix);
      results.addAll(this.getAllNodes(prefixNode));
    }

    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        List<Value> results = this.getAllWithPrefix(prefix);
        Collections.sort(results, comparator);
        return results;
    }

    public Set<Value> deleteAllWithPrefix(String prefix) {
      Set<Value> values = new TreeSet<Value>();
      List<Value> nodesToDelete = this.getAllWithPrefix(prefix);
      for (Node n : nodesToDelete) {
        values.addAll(n.getValues());
      }

      char l = prefix.charAt(prefix.length()-1);
      int index = Character.toUpperCase(l) - 65;
      String prePrefix = prefix.substring(0, prefix.length()-1);
      Node n = this.getNode(prePrefix);
      Node[] links = n.getLinks();
      links[index] == null;

      return values;
    }

    public Set<Value> deleteAll(String key) {
      Set<Value> results = this.getValues(key);
      Node n = this.getNode(key);
      n.removeAllValues();
      return results;
    }

    public Value delete(String key, Value val) {
      Node n = this.getNode(key);
      if n.containsValue(val) {
        n.removeValue(val);
        return val;
      }
      return null;
    }
}

package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;

public class TrieImpl<Value> implements Trie<Value> {

  public static class Node<Value> {
      protected Set<Value> val = new TreeSet<Value>();
      protected Node[] links = new Node[TrieImpl.alphabetSize];
  }

  private static final int alphabetSize = 36;
  private Node root = new Node<Value>();


  public TrieImpl() {}

  public void put(String key, Value val) {
    if (val == null) {
      this.deleteAll(key);
    } else {
      this.root = this.put(this.root, key, val, 0);
    }
  }

  private Node put(Node x, String key, Value val, int d) {
    if (x == null) {
      x = new Node();
    }

    if (d == key.length()) {
      x.val.add(val);
      return x;
    }

    char c = Character.toUpperCase(key.charAt(d));
    x.links[c - 65] = this.put(x.links[c-65], key, val, d + 1);
    return x;
  }

  private Node<Value> getNode(String key) {
    return this.getNode(this.root, key, 0);
  }

  private Node<Value> getNode(Node x, String key, int d) {
    if (x == null) {
      return null;
    }

    if (d == key.length()) {
      return x;
    }

    char c = key.charAt(d);
    return this.getNode(x.links[c], key, d + 1);
  }

  private Set<Value> getValues(String key) {
    Node n = this.getNode(key);
    return n.val;
  }

  public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
    List<Value> res = new ArrayList<Value>();
    res.addAll(this.getValues(key));
    Collections.sort(res, comparator);
    return res;
  }

  private void getAllNodes(Node x, Set<Node> nodes) {
    if (x != null) {
      nodes.addAll(x);
    }

    for (int i = 0; i < TrieImpl.alphabetSize; i++) {
      if (x.links[i] != null) {
        getAllNodes(x.links[i], nodes);
      }
    }
  }

  private List<Value> getAllWithPrefix(String prefix) {
    List<Value> res = new ArrayList<Value>();
    Node<Value> startingNode = this.getNode(prefix);
    Set<Node> nodes = new TreeSet<Node>();
    this.getAllNodes(startingNode, nodes);

    for (Node n : nodes) {
      res.addAll(n.val);
    }

    return res;
  }

  public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
    List<Value> res = this.getAllWithPrefix(prefix);
    Collections.sort(res, comparator);
    return res;
  }

  public Set<Value> deleteAllWithPrefix(String prefix) {

    Set<Value> results = new TreeSet<Value>();
    results.addAll(this.getAllWithPrefix(prefix));

    char l = prefix.charAt(prefix.length()-1);
    String prePrefix = prefix.substring(0, prefix.length()-1);
    Node n = this.getNode(prePrefix);
    n.links[l] = null;
    return results;
  }

  public Set<Value> deleteAll(String key) {
    Node n = this.getNode(key);
    Set<Value> results = new TreeSet<Value>();
    results.addAll(n.val);
    n.val.removeAll(results);
    return results;
  }

  public Value delete(String key, Value val) {
    Node n = this.getNode(key);
    Boolean deleted = n.val.remove(val);
    if (deleted) {
      return val;
    } else {
      return null;
    }
  }

}
