package main.java.practice.Data;

import java.util.TreeSet;
import java.util.Set;
import main.java.practice.Profiles.*;

public class University {

  private String name;
  private Set<School> schools = new TreeSet<School>();
  private Registrar reg;

  protected University(String name, Registrar reg) {
    this.name = name;
    this.reg = reg;
  }

  protected void createSchool(String name, Dean dean) {
    this.schools.add(new School(name, dean));
  }

  protected void removeSchool(String name) {}

  protected Registrar getRegistrar() {
    return this.reg;
  }

  public Set<School> getSchools() {
    return this.schools;
  }

  public void getProfessors() {}

  public void getCourses() {}
}
