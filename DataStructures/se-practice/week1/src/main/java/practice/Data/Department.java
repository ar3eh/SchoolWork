package main.java.practice.Data;
import java.util.TreeSet;
import java.util.Set;
import main.java.practice.Profiles.*;

public class Department {

  private Set<Course> courses = new TreeSet<Course>();
  private String name, abrev;
  private School school;

  protected Department(String name, String abrev, School school) {
    this.name = name;
    this.abrev = abrev;
    this.school = school;
  }

  protected void createCourse(String name, int id) {}

  protected void removeCourse(Course course) {}

  public void getCourses() {}

  public void getProfessors() {}


}
