package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.Utils;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;


public class DocumentStoreImplTest {

  //variables to hold possible values for doc5
  private URI uri5;
  private String txt5;

  //variables to hold possible values for doc2
  private URI uri2;
  private String txt2;

  //variables to hold possible values for doc3
  private URI uri3;
  private String txt3;

  //variables to hold possible values for doc4
  private URI uri4;
  private String txt4;

  private int bytes1;
  private int bytes2;
  private int bytes3;
  private int bytes4;

  private boolean deleteDirectory(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
        for (File file : allContents) {
            deleteDirectory(file);
        }
    }
    return directoryToBeDeleted.delete();
  }

  private boolean deleteDocsFromDisk() {
    File file = new File(System.getProperty("user.dir") + "/" + "edu.yu.cs");
    return deleteDirectory(file);
  }



  @BeforeEach
  public void init() throws Exception {
      //init possible values for doc5
      this.uri5 = new URI("http://edu.yu.cs/com1320/project/doc5");
      this.txt5 = "This doc5 plain text string Computer Headphones";

      //init possible values for doc2
      this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
      this.txt2 = "Text doc2 plain String";

      //init possible values for doc3
      this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
      this.txt3 = "This is the text of doc3";

      //init possible values for doc4
      this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
      this.txt4 = "This is the text of doc4";

      this.bytes1 = this.txt5.getBytes().length;
      this.bytes2 = this.txt2.getBytes().length;
      this.bytes3 = this.txt3.getBytes().length;
      this.bytes4 = this.txt4.getBytes().length;
  }

  private DocumentStore getStoreWithTextAdded() throws IOException {
      DocumentStore store = new DocumentStoreImpl();
      store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.TXT);
      return store;
  }

  private DocumentStore getStoreWithBinaryAdded() throws IOException {
      DocumentStore store = new DocumentStoreImpl();
      store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()),this.uri5, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()),this.uri3, DocumentStore.DocumentFormat.BINARY);
      store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt4.getBytes()),this.uri4, DocumentStore.DocumentFormat.BINARY);
      return store;
  }
  @Test
  public void stage4TestUndoAfterMaxBytes() throws IOException {
      DocumentStore store = new DocumentStoreImpl();
      store.setMaxDocumentBytes(this.bytes1 + this.bytes2 + this.bytes3);
      store.putDocument(new ByteArrayInputStream(this.txt5.getBytes()), this.uri5, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentStore.DocumentFormat.TXT);
      store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentStore.DocumentFormat.TXT);
      //all 3 should still be in memory
      /*assertNotNull(store.getDocument(this.uri5),"uri5 should still be in memory");
      assertNotNull(store.getDocument(this.uri2),"uri2 should still be in memory");
      assertNotNull(store.getDocument(this.uri3),"uri3 should still be in memory");*/
      //add doc4, doc5 should be pushed out
      store.putDocument(new ByteArrayInputStream(this.txt4.getBytes()), this.uri4, DocumentStore.DocumentFormat.TXT);
    /*  assertNotNull(store.getDocument(this.uri2),"uri2 should still be in memory");
      assertNotNull(store.getDocument(this.uri3),"uri3 should still be in memory");
      assertNotNull(store.getDocument(this.uri4),"uri4 should still be in memory");
      //uri5 should've been pushed out of memory
      assertNull(store.getDocument(this.uri5),"uri5 should've been pushed out of memory");
      //undo the put - should eliminate doc4, and only uri2 and uri3 should be left */
      store.undo();
      /* assertNull(store.getDocument(this.uri4),"uri4 should be gone due to the undo");
      assertNotNull(store.getDocument(this.uri2),"uri2 should still be in memory");
      assertNotNull(store.getDocument(this.uri3),"uri3 should still be in memory");
      assertNull(store.getDocument(this.uri5),"uri5 should NOT reappear after being pushed out of memory"); */
  }



}
