package edu.yu.registrar;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import edu.yu.registrar.model.*;
import edu.yu.registrar.model.CourseOffering.Semester;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class AccessPointTest {

  @Test
  public void gettingCourseOfferings() {
    AccessPoint yu = new AccessPoint("masterpassword");
    School yc = yu.createSchool("YC");
    Employee jd = yu.createDean("John", "Doe", 123, yc, "johndoe123", "masterpassword");
    yu.logIn(123, "johndoe123");
    Department cs = yu.createDepartment("CS", yc);
    Course Intro = yu.createCourse("Intro", cs, 1);
    Course Ds = yu.createCourse("DS", cs, 2);
    Course Algo = yu.createCourse("Algo", cs, 3);
    Professor Judah = yu.createProfessor("Judah", "Diament", 456, cs, "judahpass", "masterpassword");
    CourseOffering IntroOff = yu.createCourseOffering(Intro, 2021, Semester.FALL, Judah);
    CourseOffering DsOff = yu.createCourseOffering(Ds, 2021, Semester.FALL, Judah);
    CourseOffering AlgoOff = yu.createCourseOffering(Algo, 2021, Semester.FALL, Judah);

    Set<CourseOffering> results = yu.CourseOfferingsSearch(Semester.FALL, 2021, null, null, -1, "YC", null, null);
    //There should be 3 Course Offerings from the Fall Semester in YC in 2021
    assertEquals(3, results.size());
    //The dean logged in so the data should be editable (i.e. not immutable)
    for (CourseOffering c : results) {
      assertTrue(!(c instanceof ImmutableCourseOffering));
    }
    yu.logOut();
    Student Arieh = yu.createStudent("Arieh", "Chaikin", 789, "ariehpass", "masterpassword");
    yu.logIn(789, "ariehpass");
    results = yu.CourseOfferingsSearch(Semester.FALL, 2021, null, null, -1, "YC", null, null);
    //The student logged in so the data should NOT be editable
    for (CourseOffering c : results) {
      assertTrue(c instanceof ImmutableCourseOffering);
    }
  }


  @Test
  public void courseOfferingsSearchVagueTest() {

    AccessPoint yu = new AccessPoint("masterpassword");
    School yc = yu.createSchool("YC");
    Employee jd = yu.createDean("John", "Doe", 123, yc, "johndoe123", "masterpassword");
    yu.logIn(123, "johndoe123");

    Department cs = yu.createDepartment("CS", yc);
    Course IntroCS = yu.createCourse("IntroCS", cs, 1);
    Course Ds = yu.createCourse("DS", cs, 2);
    Course Algo = yu.createCourse("Algo", cs, 3);
    Professor Judah = yu.createProfessor("Judah", "Diament", 456, cs, "judahpass", "masterpassword");
    CourseOffering IntroOff = yu.createCourseOffering(IntroCS, 2021, Semester.FALL, Judah);
    CourseOffering DsOff = yu.createCourseOffering(Ds, 2021, Semester.FALL, Judah);
    CourseOffering AlgoOff = yu.createCourseOffering(Algo, 2021, Semester.FALL, Judah);

    Department bio = yu.createDepartment("Biology", yc);
    Course bio1 = yu.createCourse("IntroCS", bio, 4);
    Course bio2 = yu.createCourse("DS", bio, 5);
    Professor BioProf = yu.createProfessor("Bi", "Ology", 012, bio, "bioprofpass", "masterpassword");
    CourseOffering bio1Off = yu.createCourseOffering(bio1, 2022, Semester.SPRING, BioProf);
    CourseOffering bio2Off = yu.createCourseOffering(bio2, 2022, Semester.SPRING, BioProf);

    Student Arieh = yu.createStudent("Arieh", "Chaikin", 789, "ariehpass", "masterpassword");

    Set<CourseOffering> results = yu.CourseOfferingsSearch(null, -1, null, null, -1, null, null, null);
    //There should be 5 Course Offerings ever in any school with any teacher, any school, etc.
    for (CourseOffering c : results) {
      assertTrue(!(c instanceof ImmutableCourseOffering));
    }
    yu.logOut();
    yu.logIn(789, "ariehpass");
    results = yu.CourseOfferingsSearch(Semester.FALL, 2021, null, null, -1, "YC", null, null);
    //The student logged in so the data should NOT be editable
    for (CourseOffering c : results) {
      assertTrue(c instanceof ImmutableCourseOffering);
    }

    Set<Course> courseresults = yu.CoursesSearch("NotACouse", "999", "YC", "NotADepartment");
    //There should be no schools
    assertTrue(courseresults.isEmpty());

    Ds.addPrerequisite(IntroCS);
    Set<Course> introSet = yu.CoursesSearch("IntroCS", "1", "YC", "CS");
    Set<Course> dsSet = yu.CoursesSearch("DS", "2", "YC", "CS");
    assertEquals(1, introSet.size());
    assertEquals(1, dsSet.size());
    for (Course c : introSet) {
      assertTrue(c instanceof ImmutableCourse);
    }
    for (Course c : dsSet) {
      assertTrue(c instanceof ImmutableCourse);
    }
    Iterator it = introSet.iterator();
    Course intro = (Course) it.next();
    it = dsSet.iterator();
    Course dataStructures = (Course) it.next();
    assertTrue(dataStructures.getPrerequisites().contains(intro));
  }


  @Test
  public void DepartmentSearchTest() {
    AccessPoint yu = new AccessPoint("masterpassword");
    School yc = yu.createSchool("YC");
    Employee jd = yu.createDean("John", "Doe", 123, yc, "johndoe123", "masterpassword");
    yu.logIn(123, "johndoe123");
    Department cs = yu.createDepartment("CS", yc);
    Course Intro = yu.createCourse("Intro", cs, 1);
    Course Ds = yu.createCourse("DS", cs, 2);
    Course Algo = yu.createCourse("Algo", cs, 3);
    Professor Judah = yu.createProfessor("Judah", "Diament", 456, cs, "judahpass", "masterpassword");
    CourseOffering IntroOff = yu.createCourseOffering(Intro, 2021, Semester.FALL, Judah);
    CourseOffering DsOff = yu.createCourseOffering(Ds, 2021, Semester.FALL, Judah);
    CourseOffering AlgoOff = yu.createCourseOffering(Algo, 2021, Semester.FALL, Judah);


    Set<Department> results = yu.DepartmentsSearch(null, "YC");
    for (Department d : results) {
      assertTrue(!(d instanceof ImmutableDepartment));
    }
    assertEquals(2, results.size());
  }

  @Test
  public void MajorAndSchoolSearchTest() {

    AccessPoint yu = new AccessPoint("masterpassword");
    School yc = yu.createSchool("YC");
    Employee jd = yu.createDean("John", "Doe", 123, yc, "johndoe123", "masterpassword");
    yu.logIn(123, "johndoe123");
    Department cs = yu.createDepartment("CS", yc);
    Course Intro = yu.createCourse("Intro", cs, 1);
    Course Ds = yu.createCourse("DS", cs, 2);
    Course Algo = yu.createCourse("Algo", cs, 3);
    Professor Judah = yu.createProfessor("Judah", "Diament", 456, cs, "judahpass", "masterpassword");
    CourseOffering IntroOff = yu.createCourseOffering(Intro, 2021, Semester.FALL, Judah);
    CourseOffering DsOff = yu.createCourseOffering(Ds, 2021, Semester.FALL, Judah);
    CourseOffering AlgoOff = yu.createCourseOffering(Algo, 2021, Semester.FALL, Judah);
    Set<Course> csCourses = new HashSet<>();
    csCourses.add(Intro);
    csCourses.add(Ds);
    csCourses.add(Algo);


    Department bio = yu.createDepartment("Biology", yc);
    Course bio1 = yu.createCourse("IntroCS", bio, 4);
    Course bio2 = yu.createCourse("DS", bio, 5);
    Professor BioProf = yu.createProfessor("Bi", "Ology", 012, bio, "bioprofpass", "masterpassword");
    CourseOffering bio1Off = yu.createCourseOffering(bio1, 2022, Semester.SPRING, BioProf);
    CourseOffering bio2Off = yu.createCourseOffering(bio2, 2022, Semester.SPRING, BioProf);
    Set<Course> bioCourses = new HashSet<>();
    bioCourses.add(bio1);
    bioCourses.add(bio2);



    Major csDegree = yu.createMajor("CompSci", yc, csCourses);
    Major bioDegree = yu.createMajor("BioDegree", yc, bioCourses);

    Set<Major> results = yu.MajorsSearch(null, "CS", null);
    assertEquals(1, results.size());
    Iterator it = results.iterator();
    Major csmaj = (Major) it.next();

    assertTrue(!(csmaj instanceof ImmutableMajor));


    yu.logOut();
    Student Arieh = yu.createStudent("Arieh", "Chaikin", 789, "ariehpass", "masterpassword");
    yu.logIn(789, "ariehpass");

    assertTrue(csmaj instanceof ImmutableMajor);


    Set<School> school = yu.SchoolsSearch("YC", null, null);
    it = school.iterator();
    School s = (School) it.next();
    assertTrue(s == csmaj.getSchool());

    
  }



}
