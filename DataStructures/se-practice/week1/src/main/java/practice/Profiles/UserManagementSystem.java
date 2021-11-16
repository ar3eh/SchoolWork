package main.java.practice.Profiles;

import main.java.practice.Data.*;
import java.util.TreeSet;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;

public class UserManagementSystem {

  private Registrar reg;
  private User currentUser;
  private Set<User> users = new TreeSet<User>();

  public UserManagementSystem() {
    this.reg = new Registrar();
    this.currentUser = this.reg;
    this.users.add(this.reg);
  }

  public Registrar getRegistrar() {
    return this.reg;
  }

  public User logIn(int id, String password) {
    //Search through users and set the one who's id and password match as the current user
    //Return the user so you can sign in the User into the BS
    return new Student("Test", 123, "password");
  }

  public void logOut() {
    this.currentUser = null;
  }

  public void changePassword(int id, String oldPass, String newPass) {
    //Confirm the users id and password before changing
  }

  //Only the registrar can make new users
  public void createUser(String type, String name, String password, int id) {
    if (currentUser == this.reg) {
      //Calls the logic from the Registrar class
      //Add user to user database.

    }
  }

  public Map<String, Map<Course, Integer>> getStudentTranscript(int studentID) {
    //Only the registrar can see the transcript for anyone
    return new TreeMap<String, Map<Course, Integer>>();
  }

  public Map<String, Map<Course, Integer>> getSelfTranscript(int studentID) {
    if (this.currentUser.getID() != studentID) {
      //Stop here don't Dont return it
    }
    Student user = (Student) this.currentUser;
    return user.getTranscript();
  }

  public void getUser(int studentID) {}



}
