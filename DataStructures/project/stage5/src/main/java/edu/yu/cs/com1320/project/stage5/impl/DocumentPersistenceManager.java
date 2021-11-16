package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.lang.reflect.*;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.*;
import com.google.gson.reflect.*;

import javax.xml.bind.DatatypeConverter;
import java.lang.*;


/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

    String path;

    public DocumentPersistenceManager(File baseDir) {
      if (baseDir == null) {
          this.path = System.getProperty("user.dir");
      } else {
        this.path = baseDir.getPath();
      }
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
      if (uri == null || val == null) {
        throw new IllegalArgumentException("Arg");
      }
      final Boolean isTxtDoc = !(val.getDocumentTxt() == null);

      JsonSerializer<Document> serializer = new JsonSerializer<Document>() {
        @Override
        public JsonElement serialize(Document src, Type typOfSrc, JsonSerializationContext context) {
          JsonObject jsonDocument = new JsonObject();
          jsonDocument.addProperty("uri", src.getKey().toString());
          if (isTxtDoc) {
            jsonDocument.addProperty("txt", src.getDocumentTxt());
            jsonDocument.add("wordMap", new Gson().toJsonTree(src.getWordMap()).getAsJsonObject());
          } else {
            String base64Encoded = DatatypeConverter.printBase64Binary(src.getDocumentBinaryData());
            jsonDocument.addProperty("binaryData", base64Encoded);
          }

          return jsonDocument;
        }
      };

      Gson gson = new GsonBuilder()
        .registerTypeAdapter(DocumentImpl.class, serializer)
        .setPrettyPrinting()
        .create();
      try {
        File file = new File(this.path + "/" + uri.getHost() + "/" + uri.getPath() + ".json");
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file);
        fw.write(gson.toJson(val));
        fw.close();
      } catch (IOException e) {
        System.out.println("Threw IOException");
      }
    }


    @Override
    public Document deserialize(URI uri) throws IOException {

      if (uri == null) {
        return null;
      }

      JsonDeserializer<Document> deserializer = new JsonDeserializer<Document>() {
        @Override
        public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
          JsonObject jsonObj = (JsonObject) json;

          String str = jsonObj.get("uri").getAsString();
          URI docURI = URI.create(str);

          Document doc;
          if (jsonObj.has("txt")) {
            String docTxt = jsonObj.get("txt").getAsString();
            doc = new DocumentImpl(docURI, docTxt);
            JsonObject mapObj = jsonObj.get("wordMap").getAsJsonObject();
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            for (String s : mapObj.keySet()) {
              map.put(s, mapObj.get(s).getAsInt());
            }
            doc.setWordMap(map);
          } else {
            String base64Encoded = jsonObj.get("binaryData").getAsString();
            byte[] bytes = DatatypeConverter.parseBase64Binary(base64Encoded);
            doc = new DocumentImpl(docURI, bytes);
          }
          return doc;

        }
      };


      JsonParser parser = new JsonParser();
      FileReader reader = new FileReader(this.path + "/" + uri.getHost() + "/" + uri.getPath() + ".json");
      Object object = parser.parse(reader);
      reader.close();

      JsonObject jsonObj = (JsonObject) object;

      Gson gson = new GsonBuilder()
        .registerTypeAdapter(DocumentImpl.class, deserializer)
        .create();

      Document doc = gson.fromJson(jsonObj, DocumentImpl.class);
      try {
        this.delete(uri);
      } catch(IOException e) {}
      return doc;
    }


    @Override
    public boolean delete(URI uri) throws IOException {
      File file = new File(this.path + "/" + uri.getHost() + "/" + uri.getPath() + ".json");

      return file.delete();


    }












}
