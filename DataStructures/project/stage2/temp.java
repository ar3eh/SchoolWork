package edu.yu.cs.com1320.project.stage3.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.stage3.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.*;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class DocumentStoreImpl implements DocumentStore {

  private HashTableImpl<URI, Document> docs = new HashTableImpl<URI, Document>();
  private Stack<Undoable> history = new StackImpl<Undoable>();
  private Trie<Document> wordTrie = new TrieImpl<Document>();


  public DocumentStoreImpl() {}

  private Function<URI, Boolean> createFun(URI uri, Boolean put) {
    if (put) {

      Document prev = this.getDocument(uri);

      Function<URI, Boolean> funPut = u -> {
        Document doc;
        if (prev.getDocumentTxt() != null) {
          doc = new DocumentImpl(prev.getKey(), prev.getDocumentTxt());
        } else {
          doc = new DocumentImpl(prev.getKey(), prev.getDocumentBinaryData());
        }
        if (prev != null) {
          this.removeDocFromTrie(prev);
        }
        this.docs.put(u, doc);
        this.fillTrieWithDoc(doc);
        return true;
      };

      return funPut;

    } else {

      Document prev = this.getDocument(uri);

      Function<URI, Boolean> funRemove = u -> {
        Document doc;
        if (prev.getDocumentTxt() != null) {
          doc = new DocumentImpl(prev.getKey(), prev.getDocumentTxt());
        } else {
          doc = new DocumentImpl(prev.getKey(), prev.getDocumentBinaryData());
        }
        this.docs.put(u, null);
        this.removeDocFromTrie(doc);
        return true;
      };
      return funRemove;
    }


  }

  private int putNull(URI uri, Document prev) {
    Boolean deleted = this.deleteDocument(uri);
    if (deleted) {
      return prev.hashCode();
    } else {
      return 0;
    }
  }

  public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {

    if ((uri == null) || (format == null)) {
     throw new IllegalArgumentException();
    }

    Document prev = this.getDocument(uri);

    if (input == null) {
      this.putNull(uri, prev);
    }

    byte[] bytes = input.readAllBytes();

    if (prev == null) {
      this.history.push(new GenericCommand<URI>(uri, this.createFun(uri, false)));
    } else {
      this.history.push(new GenericCommand<URI>(uri, this.createFun(uri, true)));
    }

    if (format == DocumentFormat.TXT) {

      String txt = new String(bytes);
      this.docs.put(uri, new DocumentImpl(uri, txt));

      if (prev != null) {
        this.removeDocFromTrie(prev);
      }

      this.fillTrieWithDoc(this.docs.get(uri));

      if (prev != null) {
        return prev.hashCode();
      }
      return 0;

    } else {
      this.docs.put(uri, new DocumentImpl(uri, bytes));
      if (prev != null) {
        return prev.hashCode();
      }
      return 0;
    }
  }

  public Document getDocument(URI uri) {
    return this.docs.get(uri);
  }

  public boolean deleteDocument(URI uri) {
   Document prev = this.getDocument(uri);

   Function<URI, Boolean> noOp = u -> {return true;};

   if (prev == null) {
     this.history.push(new GenericCommand<URI>(uri, noOp));
     return false;
   }

   this.history.push(new GenericCommand<URI>(uri, this.createFun(uri, true)));
   Document doc = this.getDocument(uri);
   this.docs.put(uri, null);

   if (doc.getDocumentTxt() != null) {
     this.removeDocFromTrie(doc);
   }
   return true;
 }

  public void undo() throws IllegalStateException {
   if (this.history.peek() == null) {
     throw new IllegalStateException();
   }
   if (this.history.peek() instanceof GenericCommand) {
     GenericCommand<URI> c = (GenericCommand) this.history.pop();
     c.undo();
   }
 }

  public void undo(URI uri) throws IllegalStateException {
   if (this.history.peek() == null) {
     throw new IllegalStateException();
   }

  Stack<Undoable> temp = new StackImpl<Undoable>();
  Boolean found = false;

  while (this.history.peek() != null) {
    if (this.history.peek() instanceof GenericCommand) {
      GenericCommand<URI> c = (GenericCommand<URI>) this.history.pop();
      if (c.getTarget().equals(uri)) {
        c.undo();
        found = true;
        break;
      } else {
        temp.push(c);
      }
    }
  }

  while(temp.peek() != null) {
    this.history.push(temp.pop());
  }

  if (found == false) {
    throw new IllegalStateException();
  }

 }

  public List<Document> search(String keyword) {
    return new ArrayList<Document>();
  }

  public List<Document> searchByPrefix(String keywordPrefix) {
    return new ArrayList<Document>();
  }

  public Set<URI> deleteAll(String keyword) {
    return new HashSet<URI>();
  }

  public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
    return new HashSet<URI>();
  }

  private void fillTrieWithDoc(Document doc) {
    for (String w : doc.getAllWords()) {
      this.wordTrie.put(w, doc);
    }
  }

  private void removeDocFromTrie(Document doc) {
    for (String w : doc.getAllWords()) {
      this.wordTrie.delete(w, doc);
    }
  }


}
