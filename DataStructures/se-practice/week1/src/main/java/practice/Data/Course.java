package main.java.practice.Data;
import main.java.practice.Profiles.*;
import java.util.TreeSet;
import java.util.Set;

public class Course {

  private String name;
  private int id;
  private Set<Course> preReqs = new TreeSet<Course>();

  protected Course(String name, int id) {
    this.name = name;
    this.id = id;
  }

  protected void addPreReq(Course course) {}

  protected void removePreReq(Course course) {}

}
