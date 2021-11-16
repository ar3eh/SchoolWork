package main.java.practice.Profiles;

import main.java.practice.Data.*;
import java.util.Map;
import java.util.TreeMap;

public class Student extends User {

  //Map of semester to map of courses and the students corresponding grade.
  private Map<String, Map<Course, Integer>> courses = new TreeMap<String, Map<Course, Integer>>();

  protected Student(String name, int id, String password) {
    this.name = name;
    this.id = id;
    this.password = password;
  }

  protected Map<String, Map<Course, Integer>> getTranscript() {
    return new TreeMap<String, Map<Course, Integer>>();
  }



}
