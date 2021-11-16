package edu.yu.cs.com1320.project.stage5.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.stage5.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.*;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import java.util.NoSuchElementException;


public class DocumentStoreImpl implements DocumentStore {

  private BTreeImpl<URI, Document> docs = new BTreeImpl<URI, Document>();
  private Stack<Undoable> history = new StackImpl<Undoable>();
  private Trie<DocumentURI> wordTrie = new TrieImpl<DocumentURI>();
  private MinHeapImpl<DocumentURI> heap = new MinHeapImpl<DocumentURI>();
  private Set<DocumentURI> heapSet = new HashSet<DocumentURI>();
  private Integer maxDoc, maxMem;
  private Integer docCount = 0, memUsed = 0;

  public DocumentStoreImpl() {
    this.docs.setPersistenceManager(new DocumentPersistenceManager(null));
  }

  public DocumentStoreImpl(File path) {
    this.docs.setPersistenceManager(new DocumentPersistenceManager(path));
  }

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

  private Function <URI, Boolean> undoReplace(URI uri, Document doc, Set<Document> removed) {
      Document prev = this.privGetDocument(uri);
      Function <URI, Boolean> f = u -> {
        Document o = prev, n = doc;
        this.removeDocFromTrie(n);
        this.removeDocFromMem(n);
        this.docs.put(u, o);
        this.fillTrieWithDoc(o);
        this.addDocToMem(o);
        long time = java.lang.System.nanoTime();
        for (Document d : removed) {
          d.setLastUseTime(time);
          this.addDocToMem(d);
        }
        return true;
      };
      return f;
  }

  private Function <URI, Boolean> undoNewPut(URI uri, Document doc, Set<Document> removed) {
    Function<URI, Boolean> f = u -> {
      this.removeDocFromTrie(doc);
      this.removeDocFromMem(doc);
      docs.put(u, null);
      long time = java.lang.System.nanoTime();
      for (Document d : removed) {
        d.setLastUseTime(time);
        this.addDocToMem(d);
      }
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
      newDoc.setWordMap(this.getWordMap(newDoc.getDocumentTxt()));
    } else {
      newDoc = new DocumentImpl(uri, bytes);
    }

    if (prev != null) {
      this.removeDocFromMem(prev);
      this.docs.put(uri, null);
      this.removeDocFromTrie(prev);
    }
    docs.put(uri, newDoc);
    this.fillTrieWithDoc(newDoc);
    Set<Document> removedFromMemory = this.addDocToMem(newDoc);
    docs.get(uri);

    if (prev == null) {
      this.history.push(new GenericCommand(uri, this.undoNewPut(uri, newDoc, removedFromMemory)));
    } else {
      this.history.push(new GenericCommand(uri, this.undoReplace(uri, newDoc, removedFromMemory)));
    }


    if (prev != null) {
      return prev.hashCode();
    }
    return 0;

  }

  public Document getDocument(URI uri) {
   Document doc = docs.get(uri);
   if (doc != null) {
     this.addDocToMem(doc);
   }
   return doc;
 }

 private Document privGetDocument(URI uri) {
   Document doc = docs.get(uri);
   if (doc == null) {
     return null;
   }

   Boolean contains = false;
   for (DocumentURI u : heapSet) {
     if (u.getKey().equals(uri)) {
       contains = true;
     }
   }
   if (!contains) {
     this.docs.moveToDisk(uri);
   }

   return doc;
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
   List<DocumentURI> duris = this.wordTrie.getAllSorted(word, (DocumentURI d1, DocumentURI d2) -> privGetDocument(d2.getKey()).wordCount(word) - privGetDocument(d1.getKey()).wordCount(word));
   List<Document> result = new ArrayList<Document>();
   for (DocumentURI d : duris) {
     result.add(privGetDocument(d.getKey()));
   }
   long time = java.lang.System.nanoTime();
   for (Document d : result) {
     d.setLastUseTime(time);
     this.heap.reHeapify(new DocumentURI(d.getKey()));
   }
   return result;
 }

 public List<Document> searchByPrefix(String keywordPrefix) {
   String word = keywordPrefix.toLowerCase();
   List<DocumentURI> duris = this.wordTrie.getAllWithPrefixSorted(word, (DocumentURI d1, DocumentURI d2) -> { int d1sum = 0, d2sum = 0;
   for (String w : privGetDocument(d1.getKey()).getWords()) {
     if (w.startsWith(word)) {
       d1sum += privGetDocument(d1.getKey()).wordCount(w);
    }
   }
   for (String w : privGetDocument(d2.getKey()).getWords()) {
     if (w.startsWith(word)) {
       d2sum += privGetDocument(d2.getKey()).wordCount(w);
    }
   }
   return d2sum - d1sum;});
   List<Document> result = new ArrayList<Document>();
   for (DocumentURI d : duris) {
     result.add(privGetDocument(d.getKey()));
   }
   long time = java.lang.System.nanoTime();
   for (Document d : result) {
     d.setLastUseTime(time);
     addDocToMem(d);
     this.heap.reHeapify(new DocumentURI(d.getKey()));
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
     this.removeDocFromTrie(doc);
     this.removeDocFromMem(doc);
     this.docs.put(doc.getKey(), null);
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
     this.removeDocFromTrie(doc);
     this.removeDocFromMem(doc);
     this.docs.put(doc.getKey(), null);

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
     this.wordTrie.put(w, new DocumentURI(doc.getKey()));
   }
 }

 private void removeDocFromTrie(Document doc) {
   for (String w : doc.getWords()) {
     this.wordTrie.put(w, new DocumentURI(doc.getKey()));
   }
 }

 private Map<String, Integer> getWordMap(String txt) {

   Map<String, Integer> result = new HashMap<String, Integer>();

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

 public void setMaxDocumentCount(int limit) {
  this.maxDoc = limit;
  while (this.docCount > this.maxDoc) {
    removeLeastDocFromMem();
  }
 }

 public void setMaxDocumentBytes(int limit) {
   this.maxMem = limit;
   while (this.memUsed > this.maxMem) {
     removeLeastDocFromMem();
   }
 }

 private Set<Document> makeRoomForDocument(Document doc) {
   Set<Document> removed = new HashSet<Document>();
   if (this.maxMem != null) {
     int memNeeded = getMemOfDoc(doc);
     while (((this.maxMem - this.memUsed) < memNeeded)) {
       removed.add(removeLeastDocFromMem());
     }
   }

   if (this.maxDoc != null) {
     while (this.docCount >= this.maxDoc) {
       removed.add(removeLeastDocFromMem());
     }
   }
   return removed;
 }

 private Document removeLeastDocFromMem() {
   DocumentURI docURIToSend = heap.remove();
   boolean rem = heapSet.remove(docURIToSend);

   if (docURIToSend == null) {
     return null;
   }
   Document docToSend = privGetDocument(docURIToSend.getKey());
   this.docCount -= 1;
   this.memUsed -= getMemOfDoc(docToSend);
   this.docs.moveToDisk(docToSend.getKey());
   return docToSend;
 }


 private int getMemOfDoc(Document doc) {
   if (doc.getDocumentTxt() != null) {
    return doc.getDocumentTxt().getBytes().length;
   }
    return doc.getDocumentBinaryData().length;
 }

 private Set<Document> addDocToMem(Document doc) {

   Set<Document> removed = new HashSet<Document>();
   removed.addAll(makeRoomForDocument(doc));
   doc.setLastUseTime(java.lang.System.nanoTime());
   this.docs.get(doc.getKey());
   Boolean contains = false;
   for (DocumentURI u : heapSet) {
     if (u.getKey().equals(doc.getKey())) {
       contains = true;
     }
   }
   if (contains) {
     this.heap.reHeapify(new DocumentURI(doc.getKey()));
   } else {
     this.heapSet.add(new DocumentURI(doc.getKey()));
     this.heap.insert(new DocumentURI(doc.getKey()));
     this.memUsed += getMemOfDoc(doc);
     this.docCount += 1;
   }
   return removed;
 }

 private void removeDocFromMem(Document doc) {
   if (doc == null) {
     return;
   }
   Boolean contains = false;
   for (DocumentURI u : heapSet) {
     if (u.getKey().equals(doc.getKey())) {
       contains = true;
     }
   }
   if (!contains) {
     return;
   }
   doc.setLastUseTime(0);
   this.heap.reHeapify(new DocumentURI(doc.getKey()));
   this.removeLeastDocFromMem();
 }


 private class DocumentURI implements Comparable<DocumentURI> {

   private URI uri;

   private DocumentURI(URI uri) {
     this.uri = uri;
   }

   private URI getKey() {
     return this.uri;
   }

   @Override
   public int compareTo(DocumentURI other) {
    Document thisDoc = privGetDocument(this.getKey());
    Document thatDoc = privGetDocument(other.getKey());
    if (thisDoc == null && thatDoc == null) {
      return 0;
    } else if (thatDoc == null) {
      return 1;
    } else if (thisDoc == null) {
      return -1;
    }
    return thisDoc.compareTo(thatDoc);
   }

   @Override
   public boolean equals(Object other) {
     if (this == other) {
       return true;
     }
     if (!(other instanceof DocumentURI)) {
       return false;
     }
     DocumentURI that = (DocumentURI) other;
     Document thisDoc = privGetDocument(this.getKey());
     Document thatDoc = privGetDocument(that.getKey());
     if (thisDoc == null && thatDoc == null) {
       return true;
     } else if (thatDoc == null) {
       return false;
     } else if (thisDoc == null) {
       return false;
     }
     return thisDoc.equals(thatDoc);
   }

   @Override
   public int hashCode() {
     return this.uri.hashCode();
   }
 }


}
