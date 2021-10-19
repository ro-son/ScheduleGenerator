/*
   Class Name: Teacher.java
   Author: Juliet
   Creation Date: 2019-12-17
   Purpose: This class represents a teacher and extends the Person class.
*/

import java.io.*;
import java.util.*;

public class Teacher extends Person {
   private String dept;                   // Indicates the department that this teacher can teach, dicates all eligible courses
   private boolean teachingClass;         // Indicates whether the teacher is teaching a class or not (all teachers teach exactly one class)
  
  	// Constructors
   public Teacher (String name, int id, String dept) {
      super(name, id);
      this.dept = dept;
      teachingClass = false;
   }
    
  	// Accessors and Mutators
   public String getDept () {
      return dept;
   }
  
   // Instance Methods
   
  	/* 
   * Takes in an explicit Teacher argument and returns an integer indicating the relative difference in name
   * @param {Teacher} other - the other teacher to compare names with 
   * Returns int - returns 0 if the same name, positive if the implicit teacher comes alphabetically before the explicit teacher, and vice versa
   */
   public int compareToName (Teacher other) {
      return name.compareTo(other.name);
   }
  
  	/* 
  	* Takes in an explicit Teacher argument and returns an integer indicating the relative difference in ID
  	* @param {Teacher} other - the other teacher to compare IDs with 
   * Returns int - returns 0 if the same ID, positive if the implicit teacher's ID comes before the explicit teacher ID, and vice versa
  	*/
   public int compareToID (Teacher other) {
      return this.ID - other.ID;
   }
  
  	// Outputs information about the teacher
  	// Returns String - containing formatted information about the teacher
   public String toString () {
      String display = super.toString();
      display += "\nDepartment: " + dept;
      
      return display;
   }
}