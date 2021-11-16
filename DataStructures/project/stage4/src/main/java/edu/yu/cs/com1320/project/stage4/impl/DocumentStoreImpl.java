package edu.yu.cs.com1320.project.stage4.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.stage4.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.*;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class DocumentStoreImpl implements DocumentStore {

  private HashTableImpl<URI, Document> docs = new HashTableImpl<URI, Document>();
  private Stack<Undoable> history = new StackImpl<Undoable>();
  private Trie<Document> wordTrie = new TrieImpl<Document>();
  private MinHeapImpl<Document> heap = new MinHeapImpl<Document>();
  private Integer maxDoc, maxMem;
  private Integer docCount = 0, memUsed = 0;

  public DocumentStoreImpl() {}

  private Function<URI, Boolean> undoRemove(URI uri) {
    Document prev = this.privGetDocument(uri);
    Function <URI, Boolean> f = u -> {
      Document doc = prev;
      this.fillTrieWithDoc(doc);
      this.docs.put(u, doc);
      this.addDocToMem(doc);
      return true;
    };
    return f;
  }

  private Function <URI, Boolean> undoReplace(URI uri, Document doc) {
      Document prev = this.privGetDocument(uri);
      Function <URI, Boolean> f = u -> {
        Document o = prev, n = doc;
        this.removeDocFromTrie(n);
        this.removeDocFromMem(n);
        this.fillTrieWithDoc(o);
        this.addDocToMem(o);
        this.docs.put(u, o);
        return true;
      };
      return f;
  }

  private Function <URI, Boolean> undoNewPut(URI uri, Document doc) {
    Function<URI, Boolean> f = u -> {
      this.removeDocFromTrie(doc);
      this.removeDocFromMem(doc);
      docs.put(u, null);
      return true;
    };
    return f;
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

    Document prev = this.privGetDocument(uri);

    if (input == null) {
      return this.putNull(uri, prev);
    }

    byte[] bytes = input.readAllBytes();

    Document newDoc;
    if (format == DocumentFormat.TXT) {
      String txt = new String(bytes);
      newDoc = new DocumentImpl(uri, txt);
    } else {
      newDoc = new DocumentImpl(uri, bytes);
    }

    if (prev == null) {
      this.history.push(new GenericCommand(uri, this.undoNewPut(uri, newDoc)));
    } else {
      this.history.push(new GenericCommand(uri, this.undoReplace(uri, newDoc)));
      this.removeDocFromMem(prev);
      this.removeDocFromTrie(prev);
    }

    this.addDocToMem(newDoc);

    if (format == DocumentFormat.TXT) {

      docs.put(uri, newDoc);
      this.fillTrieWithDoc(newDoc);
      if (prev != null) {
        return prev.hashCode();
      }
      return 0;
    } else {
      docs.put(uri, newDoc);
      this.fillTrieWithDoc(newDoc);
      if (prev != null) {
        return prev.hashCode();
      }
      return 0;
    }
  }

  public Document getDocument(URI uri) {
   Document doc = docs.get(uri);
   if (doc != null) {
     doc.setLastUseTime(java.lang.System.nanoTime());
     this.heap.reHeapify(doc);
   }
   return doc;
 }

 private Document privGetDocument(URI uri) {
   return docs.get(uri);
 }

  public boolean deleteDocument(URI uri) {
   Document prev = this.privGetDocument(uri);

   if (prev == null) {
     this.history.push(new GenericCommand(uri,  u -> {return true;}));
     return false;
   }

   this.history.push(new GenericCommand(uri, this.undoRemove(uri)));

   this.removeDocFromTrie(prev);
   this.removeDocFromMem(prev);
   this.docs.put(uri, null);
   return true;
 }

  public void undo() throws IllegalStateException {
   if (this.history.peek() == null) {
     throw new IllegalStateException();
   }
   Undoable toUndo = this.history.pop();
   if (toUndo instanceof GenericCommand) {
     toUndo.undo();
   } else {
     CommandSet<URI> c = (CommandSet<URI>) toUndo;
     c.undoAll();
   }
 }

  public void undo(URI uri) throws IllegalStateException {
    if (this.history.peek() == null) {
      throw new IllegalStateException();
    }

    Stack<Undoable> temp = new StackImpl<Undoable>();
    Boolean found = false;

    while (this.history.peek() != null) {
      Undoable toUndo = this.history.pop();

      if (toUndo instanceof GenericCommand) {
        GenericCommand c = (GenericCommand) toUndo;
        if (c.getTarget().equals(uri)) {
          c.undo();
          found = true;
          break;
        } else {
          temp.push(c);
        }
      }
      else {
        CommandSet<URI> c = (CommandSet<URI>) toUndo;
        if (c.containsTarget(uri)) {
          c.undo(uri);
          if (c.size() > 0) {
            this.history.push(c);
          }
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
   String word = keyword.toLowerCase();
   List<Document> result = this.wordTrie.getAllSorted(word, (Document d1, Document d2) -> d2.wordCount(word) - d1.wordCount(word));
   long time = java.lang.System.nanoTime();
   for (Document d : result) {
     d.setLastUseTime(time);
     this.heap.reHeapify(d);
   }
   return result;
 }

 public List<Document> searchByPrefix(String keywordPrefix) {
   String word = keywordPrefix.toLowerCase();
   List<Document> result = this.wordTrie.getAllWithPrefixSorted(word, (Document d1, Document d2) -> { int d1sum = 0, d2sum = 0;
   for (String w : d1.getWords()) {
     if (w.startsWith(word)) {
       d1sum += d1.wordCount(w);
    }
   }
   for (String w : d2.getWords()) {
     if (w.startsWith(word)) {
       d2sum += d2.wordCount(w);
    }
   }
   return d2sum - d1sum;});
   long time = java.lang.System.nanoTime();
   for (Document d : result) {
     d.setLastUseTime(time);
     this.heap.reHeapify(d);
   }
   return result;
 }

 public Set<URI> deleteAll(String keyword) {
  String word = keyword.toLowerCase();
   Set<URI> uris = new HashSet<URI>();
   CommandSet<URI> set = new CommandSet<URI>();

   if (keyword == null) {
     GenericCommand<URI> com = new GenericCommand<URI>(null, u -> {return true;});
     set.addCommand(com);
     this.history.push(set);
     return uris;
   }
   List<Document> toDelete = this.search(word);


   if (toDelete.size() == 0) {
     return uris;
   }

   else if (toDelete.size() == 1) {
     Document doc = toDelete.get(0);
     this.history.push(new GenericCommand<URI>(doc.getKey(), this.undoRemove(doc.getKey())));
     uris.add(doc.getKey());
     this.docs.put(doc.getKey(), null);
     this.removeDocFromTrie(doc);
     this.removeDocFromMem(doc);
     return uris;
   }
   else {
     GenericCommand<URI> com;
     for (Document doc : toDelete) {
       uris.add(doc.getKey());
       com = new GenericCommand<URI>(doc.getKey(), this.undoRemove(doc.getKey()));
       set.addCommand(com);
       this.docs.put(doc.getKey(), null);
       this.removeDocFromTrie(doc);
       this.removeDocFromMem(doc);
     }
     this.history.push(set);
     return uris;
   }

 }

 public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
   String word = keywordPrefix.toLowerCase();
   List<Document> docs = this.searchByPrefix(word);
   Set<URI> uris = new HashSet<URI>();
   CommandSet<URI> set = new CommandSet<URI>();

   Function<URI, Boolean> noOp = u -> {return true;};

   if (keywordPrefix == null) {
     GenericCommand<URI> com = new GenericCommand<URI>(null, noOp);
     set.addCommand(com);
     this.history.push(set);
     return uris;
   }

   if (docs.size() == 0) {
     return uris;
   }
   else if (docs.size() == 1) {
     Document doc = docs.get(0);
     this.history.push(new GenericCommand<URI>(doc.getKey(), this.undoRemove(doc.getKey())));
     uris.add(doc.getKey());
     this.docs.put(doc.getKey(), null);
     this.removeDocFromTrie(doc);
     this.removeDocFromMem(doc);

     return uris;
   }
   else {
     GenericCommand<URI> com;
     for (Document doc : docs) {
       uris.add(doc.getKey());
       com = new GenericCommand<URI>(doc.getKey(), this.undoRemove(doc.getKey()));
       set.addCommand(com);
       this.docs.put(doc.getKey(), null);
       this.removeDocFromTrie(doc);
       this.removeDocFromMem(doc);
     }
     this.history.push(set);
     return uris;
   }
 }

 private void fillTrieWithDoc(Document doc) {
   for (String w : doc.getWords()) {
     this.wordTrie.put(w, doc);
   }
 }

 private void removeDocFromTrie(Document doc) {
   for (String w : doc.getWords()) {
     this.wordTrie.delete(w, doc);
   }
 }

 public void setMaxDocumentCount(int limit) {
  this.maxDoc = limit;
  while (this.docCount > this.maxDoc) {
    completeRemoveFromHeap();
  }
 }

 public void setMaxDocumentBytes(int limit) {
   this.maxMem = limit;
   while (this.memUsed > this.maxMem) {
     completeRemoveFromHeap();
   }
 }

 private void makeRoomForDocument(Document doc) {
   if (this.maxMem != null) {
     int memNeeded = getMemOfDoc(doc);
     while (((this.maxMem - this.memUsed) < memNeeded)) {
       completeRemoveFromHeap();
     }
   }

   if (this.maxDoc != null) {
     while (this.docCount >= this.maxDoc) {
       completeRemoveFromHeap();
     }
   }
 }

 private void completeRemoveFromHeap() {

   Document docToRemove = heap.remove();
   if (docToRemove == null) {
     return;
   }
   this.removeDocFromTrie(docToRemove);
   docs.put(docToRemove.getKey(), null);
   this.docCount -= 1;
   this.memUsed -= getMemOfDoc(docToRemove);
   this.removeUndosForDoc(docToRemove);
 }

 private int getMemOfDoc(Document doc) {
   if (doc.getDocumentTxt() != null) {
    return doc.getDocumentTxt().getBytes().length;
   }
    return doc.getDocumentBinaryData().length;
 }

 private void addDocToMem(Document doc) {
   makeRoomForDocument(doc);
   doc.setLastUseTime(java.lang.System.nanoTime());
   this.heap.insert(doc);
   this.memUsed += getMemOfDoc(doc);
   this.docCount += 1;
 }

 private void removeDocFromMem(Document doc) {
   doc.setLastUseTime(0);
   this.heap.reHeapify(doc);
   this.heap.remove();
   this.memUsed -= getMemOfDoc(doc);
   this.docCount -= 1;
 }

 private void removeUndosForDoc(Document doc) {
   StackImpl<Undoable> temp = new StackImpl<>();
   while (this.history.peek() != null) {
     Undoable u = this.history.pop();
     if (u instanceof GenericCommand) {
       GenericCommand<URI> c = (GenericCommand<URI>) u;
       if (c.getTarget() != doc.getKey()) {
         temp.push(c);
       }
     } else {
       CommandSet<URI> s = (CommandSet<URI>) u;
       CommandSet<URI> newCommandSet = new CommandSet<URI>();
       Iterator<GenericCommand<URI>> i = s.iterator();
       while (i.hasNext()) {
         GenericCommand c = i.next();
         if (c.getTarget() != doc.getKey()) {
           newCommandSet.addCommand(c);
         }
       }
       if (newCommandSet.size() == 0) {
         continue;
       } else if (newCommandSet.size() == 1) {
         i = newCommandSet.iterator();
         temp.push(i.next());
       } else {
         temp.push(s);
       }
       }
     }
     while (temp.peek() != null) {
       this.history.push(temp.pop());
     }
 }

}
