package main.java.practice.Data;

import main.java.practice.Profiles.*;
import java.util.TreeSet;
import java.util.Set;

public class School {

  private Set<Department> dpts = new TreeSet<Department>();
  private String name;
  private Dean dean;

  protected School(String name, Dean dean) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public Dean getDean() {
    return this.dean;
  }

  protected void createDepartment(String name, String abrev){
    this.dpts.add(new Department(name, abrev, this));
  }

  protected void removeDepartment(Department dpt) {
    this.dpts.remove(dpt);
  }

  public void getDepartments() {}

  public void getCourses() {}

  public void getProfessors() {}




}
