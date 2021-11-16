package main.java.practice.Data;

import main.java.practice.Profiles.*;
import java.util.TreeSet;
import java.util.Set;

public class BannerSystem {

  //The University associated with the Banner System
  private University univ;
  private String currentSemester;

  //Current User logged in to the BS
  private User currentUser = null;

  //Creates the UserManagementSystem for the BS, which creates
  //the registrar account for the Univ/BS
  private UserManagementSystem UMS = new UserManagementSystem();

  public BannerSystem(String name) {
    //Associates the Banner System with the newly created University
    this.univ = new University(name, this.UMS.getRegistrar());
  }


  //Next 3 Methods are UserManagementSystem intergration
  public void logIn(int id, String password) {
    //Calls the LogIn from the UserManagementSystem
    this.currentUser = this.UMS.logIn(id, password);
  }

  public void logOut() {
    this.UMS.logOut();
    this.currentUser = null;
    return;
  }


  public void changePassword(int id, String oldPass, String newPass) {
    this.UMS.changePassword(id, oldPass, newPass);
  }

  //Methods for the Registrar
  //Make sure the logged in user is regisrtar before taking action
  public void updateSemester(String sem) {
    if (currentUser != this.univ.getRegistrar()) {
      //Stop here and don't continue
      //Same logic for other methods
    }
    this.currentSemester = sem;
  }

  public void addSchoolToUniversity(String schoolName, int deanID) {}

  public void removeSchoolFromUniveristy(String schoolName) {}

  //Methods for the Registrar or Dean
  //Make sure the logged in user is either the registrar or the dean of the school before taking action
  public void addDepartmentToSchool(String dptName, String abrev, String schoolName) {
    //If the logged in user is not registrar or dean of the school then STOP
  }

  public void removeDepartmentFromSchool(String dptName, String schoolName) {}

  public void addCourseToDepartment(String courseName, int courseID, String schoolName, String dptName) {}

  public void removeCourse(int courseID) {}

  public void addClassOfCourse(int courseID, int section, Professor prof, int slots) {}

  public void getStudentTranscript(int studentID) {}

  //Methods for the Registrar or the Dean or the Professor of the Class
  public void removeStudentFromClass(int classID, int section, int studentID) {
    //add if logic for user veri
  }

  public void addPreReqToCourse(int courseID, Course preReqCourse) {}

  public void removePreReqToCourse(int courseID, Course preReqCourse) {}

  //Methods for students Only to take effect on the student
  public void joinClass(int courseID) {}

  public void dropClass(int courseID) {}

  //Method for only Student
  public void getTranscript() {}

  //Method for only Professor of Class
  public void assignGradeToStudent(int courseID, int section, int studentID, int grade) {}










  //Methods for anyone

  public void getClass(int courseID, String section) {}

  public void getCourse(int courseID) {}

  public void getSchoolsInUniversity() {}

  public void getDepartmentsInSchool(School school) {}

  public void getProfessorsInUniversity() {}

  public void getProfessorsInSchool(School school) {}

  public void getProfessorsInDepartment(Department dpt) {}

  public void getCoursesInUniversity() {}

  public void getCoursesInSchool(School school) {  }

  public void getCoursesInDepartment(Department dpt) {}


}
