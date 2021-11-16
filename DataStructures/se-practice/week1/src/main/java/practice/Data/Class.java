package main.java.practice.Data;

import java.util.TreeSet;
import java.util.Set;
import main.java.practice.Profiles.*;

public class Class {

  private Course c;
  private String sem;
  private Professor prof;
  private int section, slots;

  private Set<Student> students = new TreeSet<Student>();

  public Class(Course c, int section, String sem, Professor prof, int slots) {
    this.c = c;
    this.section = section;
    this.sem = sem;
    this.prof = prof;
    this.slots = slots;
  }

  protected void addClass() {}

  protected void dropClass() {}

  protected void removeFromClass() {}

  protected void assignGradeToStudent(int studentID) {}


}
