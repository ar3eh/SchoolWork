package edu.yu.cs.com1320.project.stage1.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.stage1.*;
import edu.yu.cs.com1320.project.impl.*;

public class DocumentStoreImpl implements DocumentStore {

  private HashTableImpl<URI, Document> docs = new HashTableImpl<URI, Document>();

  public DocumentStoreImpl() {}

  public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {

    if (input == null) {
      Document prev = this.getDocument(uri);
      Boolean deleted = this.deleteDocument(uri);
      if (deleted) {
        return prev.hashCode();
      }
    }

    byte[] bytes = input.readAllBytes();

    if (format == DocumentFormat.TXT) {
      String txt = new String(bytes);
      Document prev = docs.put(uri, new DocumentImpl(uri, txt));
      if (prev != null) {
        return prev.hashCode();
      }
      return 0;
    } else /*If the document is binary*/ {
      Document prev = docs.put(uri, new DocumentImpl(uri, bytes));
      if (prev != null) {
        return prev.hashCode();
      }
      return 0;
    }
  }

 public Document getDocument(URI uri) {
   return docs.get(uri);
 }

 public boolean deleteDocument(URI uri) {
   if (docs.put(uri, null) == null) {
     return false;
   }
   return true;
 }

}
