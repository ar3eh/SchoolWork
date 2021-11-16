package main.java.practice.Profiles;
import java.util.TreeSet;

public abstract class User {
  protected String name, password;
  protected int id;

  protected String getName() {
    return this.name;
  }

  protected int getID() {
    return this.id;
  }

  //Will be used to check if this is the ID and password of the user
  protected boolean verifyLogIn(int id, String password) {
    if (id == this.id && password == this.password) {
      return true;
    }
    return false;
  }

  protected boolean changePassword(int id, String oldPass, String newPass) {
    if (oldPass == this.password) {
      this.password = newPass;
      return true;
    }
    return false;
  }

}
