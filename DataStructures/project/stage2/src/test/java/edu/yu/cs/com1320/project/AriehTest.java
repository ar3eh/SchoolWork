import static org.junit.jupiter.api.Assertions.*;
import edu.yu.cs.com1320.project.impl.*;

import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.URISyntaxException;
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage2.*;
import edu.yu.cs.com1320.project.stage2.impl.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;


public class AriehTest{
  @Test
  void ariehtest() {
   DocumentStore documentStore = new DocumentStoreImpl();
   String string1 = "It was a dark and stormy night";
   String string2 = "It was the best of times, it was the worst of times";
   String string3 = "It was a bright cold day in April, and the clocks were striking thirteen";
   String string4 = "I am free, no matter what rules surround me.";
   String string5 = "I know everything";
   InputStream inputStream1 = new ByteArrayInputStream(string1.getBytes());
   InputStream inputStream2 = new ByteArrayInputStream(string2.getBytes());
   InputStream inputStream3 = new ByteArrayInputStream(string3.getBytes());
   InputStream inputStream4 = new ByteArrayInputStream(string4.getBytes());
   InputStream inputStream5 = new ByteArrayInputStream(string5.getBytes());
   URI uri1 =  URI.create("www.wrinkleintime.com");
   URI uri2 =  URI.create("www.taleoftwocities.com");
   URI uri3 =  URI.create("www.1984.com");
   URI uri4 =  URI.create("www.themoonisaharshmistress.com");
   URI uri5 =  URI.create("www.google.com");
   try {
    documentStore.putDocument(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
    documentStore.putDocument(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);
    documentStore.putDocument(inputStream3, uri3, DocumentStore.DocumentFormat.TXT);
    documentStore.putDocument(inputStream4, uri4, DocumentStore.DocumentFormat.TXT);
    documentStore.putDocument(inputStream5, uri5, DocumentStore.DocumentFormat.TXT);
   } catch (java.io.IOException e) {
    fail();
   }

   Document document1 = new DocumentImpl(uri1, string1);
   Document document2 = new DocumentImpl(uri2, string2);
   Document document3 = new DocumentImpl(uri3, string3);
   Document document4 = new DocumentImpl(uri4, string4);
   Document document5 = new DocumentImpl(uri5, string5);

   assertEquals(document1.hashCode(),documentStore.getDocument(uri1).hashCode());
   assertEquals(document2.hashCode(),documentStore.getDocument(uri2).hashCode());
   assertEquals(document3.hashCode(),documentStore.getDocument(uri3).hashCode());
   assertEquals(document4.hashCode(),documentStore.getDocument(uri4).hashCode());
   assertEquals(document5.hashCode(),documentStore.getDocument(uri5).hashCode());


   documentStore.undo();
   assertEquals(document1.hashCode(),documentStore.getDocument(uri1).hashCode());
   assertEquals(document2.hashCode(),documentStore.getDocument(uri2).hashCode());
   assertEquals(document3.hashCode(),documentStore.getDocument(uri3).hashCode());
   assertEquals(document4.hashCode(),documentStore.getDocument(uri4).hashCode());
   assertEquals(documentStore.getDocument(uri5), null);
   documentStore.undo();
   documentStore.undo();
   documentStore.undo();
   documentStore.undo();
   assertEquals(documentStore.getDocument(uri1), null);
   assertEquals(documentStore.getDocument(uri2), null);
   assertEquals(documentStore.getDocument(uri3), null);
   assertEquals(documentStore.getDocument(uri4), null);
   assertEquals(documentStore.getDocument(uri5), null);

   try {
    inputStream1 = new ByteArrayInputStream(string1.getBytes());
    documentStore.putDocument(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
   } catch (java.io.IOException e) {
    fail();
   }

   assertEquals(document1.hashCode(), documentStore.getDocument(uri1).hashCode());


  documentStore.deleteDocument(uri1);
  assertEquals(documentStore.getDocument(uri1), null);

  documentStore.undo();

  assertEquals(document1.hashCode(), documentStore.getDocument(uri1).hashCode());


  documentStore.deleteDocument(uri1);
  assertEquals(documentStore.getDocument(uri1), null);

   inputStream1 = new ByteArrayInputStream(string1.getBytes());
   inputStream2 = new ByteArrayInputStream(string2.getBytes());
   inputStream3 = new ByteArrayInputStream(string3.getBytes());
   inputStream4 = new ByteArrayInputStream(string4.getBytes());
   inputStream5 = new ByteArrayInputStream(string5.getBytes());


  try {
   documentStore.putDocument(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
   documentStore.putDocument(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);
   documentStore.putDocument(inputStream3, uri3, DocumentStore.DocumentFormat.TXT);
   documentStore.putDocument(inputStream4, uri4, DocumentStore.DocumentFormat.TXT);
   documentStore.putDocument(inputStream5, uri5, DocumentStore.DocumentFormat.TXT);
  } catch (java.io.IOException e) {
   fail();
  }

  documentStore.undo(uri3);
  documentStore.undo(uri5);


  assertEquals(document1.hashCode(),documentStore.getDocument(uri1).hashCode());
  assertEquals(document2.hashCode(),documentStore.getDocument(uri2).hashCode());
  assertEquals(documentStore.getDocument(uri3), null);
  assertEquals(document4.hashCode(),documentStore.getDocument(uri4).hashCode());
  assertEquals(documentStore.getDocument(uri5), null);

  documentStore.undo(uri4);
  documentStore.undo(uri2);

  assertEquals(document1.hashCode(),documentStore.getDocument(uri1).hashCode());
  assertEquals(documentStore.getDocument(uri2), null);
  assertEquals(documentStore.getDocument(uri3), null);
  assertEquals(documentStore.getDocument(uri4), null);
  assertEquals(documentStore.getDocument(uri5), null);





  try {
    inputStream2 = new ByteArrayInputStream(string2.getBytes());
    documentStore.putDocument(inputStream2, uri1, DocumentStore.DocumentFormat.TXT);
  } catch (java.io.IOException e) {
   fail();
  }



  Document documentTest1 = new DocumentImpl(uri1, string2);

  assertEquals(documentTest1.hashCode(), documentStore.getDocument(uri1).hashCode());
  assertNotEquals(document1.hashCode(), documentStore.getDocument(uri1).hashCode());

  documentStore.undo();



  assertEquals(document1.hashCode(), documentStore.getDocument(uri1).hashCode());







  }




}
