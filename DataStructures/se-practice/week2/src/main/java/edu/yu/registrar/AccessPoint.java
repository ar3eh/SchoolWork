package edu.yu.registrar;

import edu.yu.registrar.model.*;
import edu.yu.registrar.model.CourseOffering.Semester;
import edu.yu.registrar.model.Employee.Role;
import edu.yu.registrar.query.*;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.GregorianCalendar;


public class AccessPoint {

    UserDatabase UD = new UserDatabase();
    Set<School> schools = new HashSet<School>();
    User currentUser = null;
    String MasterPassword;

    public AccessPoint(String MasterPassword) {
      this.MasterPassword = MasterPassword;
    }

    /// *** User Management ***

    public void logIn(int id, String password) {
      this.currentUser = UD.logIn(id, password);
    }

    public void logOut() {
      this.currentUser = null;
    }

    // *** Data Creation ***

    public Student createStudent(String firstName, String lastName, int id, String userPassword, String masterPassword) {
      return new Student(firstName, lastName, id);
    }

    public Professor createProfessor(String firstName, String lastName, int id, Department dep, String userPassword, String masterPassword) {
      return new Professor(firstName, lastName, id, dep);
    }

    public Employee createDean(String firstName, String lastName, int id, School school, String userPassword, String masterPassword) {
      return new Employee(firstName, lastName, id, Role.DEAN, school);
    }

    public Employee createRegistrar(String firstName, String lastName, int id, School school, String userPassword, String masterPassword) {
        return new Employee(firstName, lastName, id, Role.DEAN, school);
    }

    public School createSchool(String name) {
      return new School(name);
    }

    public Course createCourse(String name, Department department, int number) {
      return new Course( name,  department,  number);
    }

    public Course createCourse(String name, Department department, int number, Set<Course> prerequisites) {
      return new Course( name,  department,  number, prerequisites);
    }

    public CourseOffering createCourseOffering(Course course, int year, Semester semester, Professor professor) {
      return new CourseOffering(course, year, semester, professor);
    }

    public CourseOffering createCourseOffering(Course course, int year, Semester semester) {
      return new CourseOffering(course, year, semester);
    }

    public Department createDepartment(String  name, School school) {
      return new Department(name, school);
    }

    public Major createMajor(String name, School school, Set<Course> requiredCourses) {
      return new Major(name, school, requiredCourses);
    }

    public Major createMajor(String name, School school) {
      return new Major(name, school);
    }

    // *** Searches/Queries ***

    public Set<CourseOffering> CourseOfferingsSearch(CourseOffering.Semester semester, int year, String professorFirstName, String professorLastName, int professorID, String school, String courseName, String department)  {
      Set<CourseOffering> results = new HashSet<>();
      return results;
    }

    public Set<Course> CoursesSearch(String name, String number, String school, String department) {
      Set<Course> results = new HashSet<>();
      return results;
    }

    public Set<Department> DepartmentsSearch(String name, String school) {
      Set<Department> results = new HashSet<>();
      return results;
    }

    public Set<Major> MajorsSearch(String school, String name, Set<Course> requiredCourses) {
      Set<Major> results = new HashSet<>();
      return results;
    }

    public Set<Professor> ProfessorsSearch(String school, String department, String firstName, String lastname, Set<CourseOffering> coursesTaught) {
      Set<Professor> results = new HashSet<>();
      return results;
    }

    public Set<School> SchoolsSearch(String name, String deanLastName, String deanFirstName) {
      Set<School> results = new HashSet<>();
      return results;
    }

    public Set<Student> StudentsSearch (String firstName, String lastname, String major, Set<CourseOffering> coursesTaken, GregorianCalendar graduationDate) {
      Set<Student> results = new HashSet<>();
      return results;
    }

    // *** Get all methods ***
    public Set<School> getAllSchools() {
      return new HashSet<School>();
    }



}
