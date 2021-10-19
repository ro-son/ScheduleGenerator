/*
   Class Name: Course.java
   Author: Winston
   Creation Date: 2019-12-17
   Purpose: This class represents a course in the school curriculum.
*/ 
 
/* --------------------------------------------------------------------
 * Dec. 17
 * -Creation of class
 * -Added all fields and accessors.mutator methods
 * --------------------------------------------------------------------
 * Dec. 18
 * -Added remaining methods
 * --------------------------------------------------------------------
 * Dec. 20
 * -Finished classes
 * -added compareTo methods
 * --------------------------------------------------------------------
*/

import java.util.*;

public class Course {
   public static final int CAPACITY = 30;       // the maximum capacity of each class
   public static final int MINIMUM = 15;        // the minimum number of people who need to sign up for a course for it to exist
   
   private String courseCode;                         // the course's ID
   private String department;                         // the course's associated department (e.g. Science, Math, Technology, English)
   private int grade;                                 // the grade level of the course
   private ArrayList<Classroom> existingClasses;      // a list of all the classes of this course
   private int numTeachers;                           // the total number of teachers for department
   private int numClasses;                            // the number of existing classes
   private int numWanted;                             // the number of people who want the course (signed up for it on their preferences)
  
  // Constructors
  // Assuming that the number of classes and the number of signups is zero (when the course is completely new)
   public Course (String courseCode, String department, int grade) {
      this.courseCode = courseCode;
      this.department = department;
      this.grade = grade;
      existingClasses = new ArrayList<Classroom>();
      numClasses = 0;
      numWanted = 0;
   }
  
  // Assuming that there already are people signed up (late entrance into system)
   public Course (String courseCode, String department, int grade, int numClasses, int numWanted) {
      this.courseCode = courseCode;
      this.department = department;
      this.grade = grade;
      this.numClasses = numClasses;
      this.numWanted = numWanted;
      existingClasses = new ArrayList<Classroom>();
   }
  
   // Accessors and Mutators
   public String getCourseCode () {
      return courseCode;
   }
  
   public String getDepartment () {
      return department;
   }
  
   public int getGrade () {
      return grade;
   }
  
   public int getNumClasses () {
      return numClasses;
   }
  
   public int getNumWanted () {
      return numWanted;
   }
   
   public ArrayList<Classroom> getClasses () {
      return existingClasses;
   }
   
   public void setCourseCode (String courseCode) {
      this.courseCode = courseCode;
   }
  
   public void setDepartment (String department) {
      this.department = department;
   }
  
   public void setNumClasses (int numClasses) {
      this.numClasses = numClasses;
   }
  
   public void setGrade (int grade) {
      this.grade = grade;
   }
  
   public void setNumWanted (int numWanted) {
      this.numWanted = numWanted;
   }
  
   // Instance Methods
   
   // Increments the number of wanted by one
   public void setOneNewWanted () {
      numWanted ++;
   }
  
   // Adds a class into the course for documentation
   // @param {Classroom} newClassroom - the Classroom to be added   
   public void newClass (Classroom newClassroom) {
      existingClasses.add(newClassroom);
   }
  
   /*
   * Compares the course names
   * @param {Course} other - the Course to be compared
   * Return int - the value from comparing the names of two courses
   */   
   public int compareToName (Course other) {
      return this.courseCode.compareTo(other.courseCode);
   }
   

   /*
   * Compares the course grade levels
   * @param {Course} other - the Course to be compared
   * Return int - the value from comparing the grades of two courses
   */   
   public int compareToGrade (Course other) {
      return this.grade - other.grade;
   }
  
   /*
   * Compares the course grade levels
   * @param {Course} other - the Course to be compared
   * Return int - the value from comparing the departments of two courses
   */   
   public boolean compareToDepartment (Course other) {
      return this.department.equals(other.department);
   }
   
   // Returns String - the course code, department, and grade, formatted
   public String toString () {
      String display = courseCode;
      display += "\nDepartment: " + department;
      display += "\nGrade: " + grade;
      return display;
   }
}