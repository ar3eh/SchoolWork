package edu.yu.cs.com1320.project.stage4.impl;

import java.net.URI;
import edu.yu.cs.com1320.project.stage4.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage4.Document;
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


    String cleanTxt = new String(txt.trim());
    cleanTxt = new String(this.txt).replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();


    String[] wordArray = cleanTxt.split(" ");
    for (int i = 0; i < wordArray.length; i++) {
      if (wordArray[i].equals("")) {
        continue;
      }
      Integer prev = this.wordMap.get(wordArray[i]);
      if (prev == null) {
        this.wordMap.put(wordArray[i], 1);
      } else {
        this.wordMap.put(wordArray[i], prev + 1);
      }
    }

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
    Integer results = this.wordMap.get(cleanTxt);

    if (results == null) {
      return 0;
    }
    return results;
  }

  public Set<String> getWords() {
    Set<String> results = new TreeSet<String>();
    results.addAll(this.wordMap.keySet());
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

}
