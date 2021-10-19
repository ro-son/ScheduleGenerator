/*
   Class Name: Student.java
   Author: Robin
   Creation Date: 2019-12-17
   Purpose: This class represents a student that attends the school and manages the student's courses.
*/

/* --------------------------------------------------------------------
 * Dec. 17
 * -Creation of class
 * -Added all fields and methods
 * -Class completed
 * --------------------------------------------------------------------
*/

import java.io.*;
import java.util.*;

public class Student extends Person {
   private int grade;							// the student's grade
   private String[] preferCourse;		   // the student's preferred courses (subject to change)
   private int numTBFits;						// the number of timetable fits
   private Timetable tb;                  // the student's timetable
   private String[] originalPreferCourse; // the student's original courses
   
   // Constructors
   public Student (String name, int grade, int id, String[] preferCourse) { // also check that all courses are available
      super(name, id);
      this.grade = grade;
      this.preferCourse = preferCourse;
      this.originalPreferCourse = preferCourse.clone();
      
      for (int i = 0 ; i < preferCourse.length ; i++) {
         if (preferCourse[i].equals("Spare")) {
            preferCourse[i] = null;
         }
      }
      tb = new Timetable();
      numTBFits = 0;
   }
   
   // Accessors and Mutators
   public int getGrade () {
      return grade;
   }
   
   public void setGrade (int grade) {
      this.grade = grade;
   }
   
   public void updateNumTBFits () {
      numTBFits ++;
   }
     
   public int getNumTBFits () {
      return numTBFits;
   }
   
   public String[] getPreferCourse () {
      return preferCourse;
   }
   
   public Timetable getTimetable () {
      return tb;
   }
   
   public String[] getOriginalPreferCourse () {
      return originalPreferCourse;
   }
   
   // Instance Methods
   
   /* 
   * Switches out a students' list of preferred courses for a new one. Their desired course  and the one to be replaced is entered. True if replacement is successful, false if not.
   * @param {String} oldC - name of course that will be switched out from the student timetable
   * @param {String} newC - name of the new course that will be added in place of the old course 
   * Returns boolean - indicating if the change was successful
   */
   public boolean switchPreferred (String oldC, String newC) {
      String p;
      if (oldC == null) {
         for (int i = 0 ; i < preferCourse.length; i++) {
            if (preferCourse[i] == null) {
               preferCourse[i] = newC;
               return true;
            }
         }
      } else { 
         for (int i = 0 ; i < 4 ; i++) {
            p = preferCourse[i];
            if (preferCourse[i] != null) {
               if (preferCourse[i].equals(oldC)) {
                  preferCourse[i] = newC;
                  return true;
               }
            }
         }
      }
      return false;
   }
   
   /*
   * Adds a course to the student timetable
   * @param {String} courseCode - Course object of the course that is to be added to the student timetable
   * Returns boolean - indicating if the addition was successful
   */
   public boolean addCourse (String courseCode) {
      return true;
        // WORK IN PROGRESS
   }
   
   /* 
   * Adds a class to the student timetable
   * @param {Classroom} classroom - Classroom object of the class that is to be added to the student timetable
   * Returns boolean - indicating if the addition was successful
   */
   public boolean addCourse (Classroom clsrm) {
   
      if (tb.hasClass(clsrm.getClassCourseCode()) != -1) {
         return true;
      }
      
      return (tb.addClass(clsrm) && clsrm.addStudent(this)); 
   } 
   
   public void addSpare (int target) {
      tb.addSpare(target);
   }
   
   public void addCourse (Classroom c, int period) {
      tb.addClass(c, period);
   }
   
   /*
   * Removes a class from the student timetable
   * @param {Classroom} classroom - Classroom object indicating the class that is to be removed from the student timetable
   * Returns boolean - indicating if the removeal was succesful
   */
   public boolean removeCourse (Classroom classroom) {
      tb.removeClass(classroom.getPeriod());
      return classroom.removeStudent(this);
   }
   
   /*
  	* Removes a class from the student timetable identitified by the period
  	* @param {int} period - period of the class that is to be removed
  	* Returns boolean - indicating if the change was successful 
  	*/
   public boolean removeCourse (int period) {
      if (tb.getClass(period).removeStudent(this)) {
         tb.removeClass(period);
         return true;
      } 
      return false;
   }   
   
   /* 
   * Takes in an explicit Student argument and returns an integer indicating the relative difference in grade
   * @param {Student} other - the other student to compare names, IDs, or grades with 
   * Returns int - returns 0 if the same, positive if the implicit student's, or grade comes before the explicit student, and vice versa
   */ 
   public int compareToGrade (Student other) {
      return grade - other.grade;
   }
   
   /* 
   * Creates the base timetable for the person, adding all wanted classes if possible
   * @param {Classroom[]} classes - array of classes to add to the timetable, in any order 
   */
   public void makeTimetable (Classroom[] classes) {
      for (int i = 0 ; i < 4 ; i++) {
         tb.addClass(classes[i]);
      }
   }
   
   // generates a formatted html file of the person's timetable
   // no parameters or return type
   public void printTimetable (String schoolName) {
      tb.printTimetable(ID, schoolName); 
   }
   
   /*
   * Changes a course on the timetable if possible
   * @param {int} period - the period of the class to be changed
   * @param {Classroom} newClass - the class to be added in place of the removed class
   */
   public void changeCourse (int period, Classroom newClass) {
      tb.removeClass(period);
      tb.addClass(newClass);
   }
   
   /*
   * Removes a course from the timetable
   * @param {String} target - the period of the class to remove
   * Returns boolean - indicating if the removal was successful
   */
   public boolean removeClassForPerson (String target) {
      if (findPeriod(target) == -1) {
         return false;
      }  else {
         tb.removeClass(findPeriod(target));
         return true;
      }
   }
   
   /* 
   * Adds a classroom to the schedule of the person
   * @param {Classroom} newclass - addition of the class 
   * Returns boolean - indicates if the addition was successful
   */
   public boolean addClassForPerson (Classroom newClass) {
      return tb.addClass(newClass);
   }
   
   /*
   * Locates the period of a class on timetable
   * @param {String} oldCourse - course code of the course to find
   * Returns the period of the course, -1 if the course was not found
   */
   public int findPeriod (String oldCourse) {
      return tb.hasClass(oldCourse);
   }
   
   /*
   * Changes a course on the timetable if possible
   * @param {int} period - the period of the class to be changed
   * @param {String} changeCourse - the class to be added in place of the removed class
   * @param {ChronoManager} cm - the database of courses and classes to search through
   * Returns boolean - indicates if the change was successful
   */
   public boolean changeCourse (int period, String changeCourse, ChronoManager cm) {
      int courseIndex = cm.findCourse(changeCourse);
      if (courseIndex == -1) 
         return false;
      Course course = (cm.getCourses())[courseIndex]; 
     
      ArrayList<Classroom> classes = course.getClasses();
      Classroom newClass = null;
     
      for (int i = 0; i < classes.size(); i++)
         if (classes.get(i).getPeriod() == period)
            newClass = classes.get(i);
     
      if (newClass == null)	
         return false;
     
      tb.removeClass(period);
      return tb.addClass(newClass);
   }
   
   // Generates a formatted html file of the person's timetable
   // No parameters or return type
   public void printTimeTable (String schoolName) {
      tb.printTimetable(ID, schoolName); 
   }
   
   // Outputs information about the teacher
   // Returns String - contains formatted information about the teacher
   public String toString () {
      String display = super.toString();
      display += "\nGrade: " + grade;
        
      return display;
   }
}