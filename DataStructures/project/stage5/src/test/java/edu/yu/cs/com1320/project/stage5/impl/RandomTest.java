package edu.yu.cs.com1320.project.stage5.impl;

import static org.junit.jupiter.api.Assertions.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;

import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;

import edu.yu.cs.com1320.project.stage5.impl.DocumentPersistenceManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;

public class RandomTest {

 @Test
  public void mainTest() throws IOException, URISyntaxException {
    PersistenceManager<URI, Document> pm = new DocumentPersistenceManager(null);
    URI uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
    String txt1 = "This doc1 plain text string Computer Headphones";
    Document doc = new DocumentImpl(uri1, txt1);
    pm.serialize(uri1, doc);
    Document newDoc = pm.deserialize(uri1);




  }



}
