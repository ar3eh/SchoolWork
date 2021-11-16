package edu.yu.cs.com1320.project.stage5.impl;

import java.net.URI;
import edu.yu.cs.com1320.project.stage5.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage5.Document;
import java.util.*;
import java.util.HashMap;
import java.util.TreeSet;

public class DocumentImpl implements Document {

  private DocumentFormat format;
  private URI uri;
  private String txt = null;
  private byte[] binaryData = null;
  private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
  private long lastUseTime;

  public DocumentImpl(URI uri, String txt) {
    if (uri == null || uri.toString() == "" || txt == null || txt == "") {
      throw new java.lang.IllegalArgumentException();
    }

    this.uri = uri;
    this.format = DocumentFormat.TXT;
    this.txt = txt;

  }

  public DocumentImpl(URI uri, byte[] binaryData) {
    if (uri == null || uri.toString() == "" || binaryData == null || binaryData.length == 0) {
      throw new java.lang.IllegalArgumentException();
    }
    this.uri = uri;
    this.format = DocumentFormat.BINARY;
    this.binaryData = binaryData;
  }

  public String getDocumentTxt() {
    return this.txt;
  }

  public byte[] getDocumentBinaryData() {
    return this.binaryData;
  }

  public URI getKey() {
    return this.uri;
  }

  public boolean equals(Object o) {
    if (o instanceof Document) {
      DocumentImpl otherDoc = (DocumentImpl) o;
      if (otherDoc.hashCode() == this.hashCode()) {
        return true;
      }
    }
    return false;
  }

  public int hashCode() {
    int result = this.uri.hashCode();
    result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(this.binaryData);
    return result;
  }

  public int wordCount(String word) {
    if (word == null) {
      throw new IllegalArgumentException();
    }

    String cleanTxt = new String(word).replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
    Map<String, Integer> map = this.makeWordMap(this.txt);
    Integer results = map.get(cleanTxt);

    if (results == null) {
      return 0;
    }
    return results;
  }

  public Set<String> getWords() {
    Set<String> results = new TreeSet<String>();
    Map<String, Integer> map = this.makeWordMap(this.txt);
    results.addAll(map.keySet());
    return results;
  }

  public long getLastUseTime() {
    return this.lastUseTime;
  }

  public void setLastUseTime(long timeInNanoseconds) {
    this.lastUseTime = timeInNanoseconds;
  }

  public int compareTo(Document otherDoc) {
    if (this.getLastUseTime() == otherDoc.getLastUseTime()) {
      return 0;
    } else if (this.getLastUseTime() > otherDoc.getLastUseTime()) {
      return 1;
    } else {
      return -1;
    }
  }

  public Map<String,Integer> getWordMap() {
    return this.wordMap;
  }

  public void setWordMap(Map<String,Integer> wordMap) {
    this.wordMap = new HashMap<String, Integer>();
    this.wordMap.putAll(wordMap);
    return;
  }

  private Map<String, Integer> makeWordMap(String txt) {

    Map<String, Integer> result = new HashMap<String, Integer>();
    if (txt == null) {
      return result;
    }

    String cleanTxt = new String(txt.trim());
    cleanTxt = new String(txt).replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();

    String[] wordArray = cleanTxt.split(" ");
    for (int i = 0; i < wordArray.length; i++) {
      if (wordArray[i].equals("")) {
        continue;
      }
      Integer prev = result.get(wordArray[i]);
      if (prev == null) {
        result.put(wordArray[i], 1);
      } else {
        result.put(wordArray[i], prev + 1);
      }
    }
    return result;
  }




}
