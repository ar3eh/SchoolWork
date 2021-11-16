package edu.yu.cs.com1320.project.stage2.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.stage2.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.*;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {

  private HashTableImpl<URI, Document> docs = new HashTableImpl<URI, Document>();
  private Stack<Command> history = new StackImpl<Command>();

  public DocumentStoreImpl() {}

  private Function<URI, Boolean> createFunPut(URI uri) {
    Document prev = this.getDocument(uri);

    Function<URI, Boolean> funPut = u -> {
      Document doc;
      if (prev.getDocumentTxt() != null) {
        doc = new DocumentImpl(prev.getKey(), prev.getDocumentTxt());
      } else {
        doc = new DocumentImpl(prev.getKey(), prev.getDocumentBinaryData());
      }
      this.docs.put(u, doc);
      return true;
    };

    return funPut;
  }

  private Function<URI, Boolean> createFunRemove(URI uri) {
    Function<URI, Boolean> funRemove = u -> {
      docs.put(u, null);
      return true;
    };
    return funRemove;
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
      return this.putNull(uri, prev);
    }

    Function<URI, Boolean> funPut = this.createFunPut(uri);
    Function<URI, Boolean> funRemove = this.createFunRemove(uri);

    byte[] bytes = input.readAllBytes();

    if (prev == null) {
      this.history.push(new Command(uri, funRemove));
    } else {
      this.history.push(new Command(uri, funPut));
    }

    if (format == DocumentFormat.TXT) {
      String txt = new String(bytes);
      docs.put(uri, new DocumentImpl(uri, txt));
      if (prev != null) {
        return prev.hashCode();
      }
      return 0;
    } else {
      docs.put(uri, new DocumentImpl(uri, bytes));
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
   Document prev = this.getDocument(uri);

   Function<URI, Boolean> noOp = u -> {return true;};
   Function<URI, Boolean> funPut = this.createFunPut(uri);

   if (prev == null) {
     this.history.push(new Command(uri, noOp));
     return false;
   }

   this.history.push(new Command(uri, funPut));
   this.docs.put(uri, null);
   return true;
 }

  public void undo() throws IllegalStateException {
   if (this.history.peek() == null) {
     throw new IllegalStateException();
   }
   Command c = this.history.pop();
   c.undo();
 }

  public void undo(URI uri) throws IllegalStateException {
   if (this.history.peek() == null) {
     throw new IllegalStateException();
   }

  Stack<Command> temp = new StackImpl<Command>();
  Boolean found = false;

  while (this.history.peek() != null) {
    Command c = this.history.pop();
    if (c.getUri().equals(uri)) {
      c.undo();
      found = true;
      break;
    } else {
      temp.push(c);
    }
  }

  while(temp.peek() != null) {
    this.history.push(temp.pop());
  }

  if (found == false) {
    throw new IllegalStateException();
  }

 }

}
