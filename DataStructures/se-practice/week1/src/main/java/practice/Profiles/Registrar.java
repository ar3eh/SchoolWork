package main.java.practice.Profiles;
import java.util.TreeSet;

public class Registrar extends User {

  protected Registrar() {
    this.name = "Reg";
    this.id = 1;
    this.password = "RegPassword";
  }

  //Only the registar can create new users
  protected void createUser(String type, String name, String password, int id) {
    //Switch case for different types of users
  }






}
