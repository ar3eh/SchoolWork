package edu.yu.registrar;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import edu.yu.registrar.model.*;



public class UserDatabase {

  private Map<Map<Integer, String>, User> users = new HashMap<Map<Integer, String>, User>();

  UserDatabase() {}

  protected User logIn(int id, String password) {
    //Loops through Users and return the User that should be logged in, returns null if no user.
    return new User("PlaceHolder", "PlaceHolder", 123);
  }

  protected void createUser(String type, String firstName, String lastName, int id) {}






}
