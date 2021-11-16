package edu.yu.cs.com1320.project.stage1.impl;

import java.net.URI;
import edu.yu.cs.com1320.project.stage1.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage1.Document;
import java.util.*;

public class DocumentImpl implements Document {

  private DocumentFormat format;
  private URI uri;
  private String txt = null;
  private byte[] binaryData = null;

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
    switch(this.format) {
      case TXT:
        result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
        break;
      case BINARY:
        result = 31 * result + Arrays.hashCode(this.binaryData);
        break;
    }
    return result;
  }

}
