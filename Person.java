/*
   Class Name: Person.java
   Author: Winston
   Creation Date: 2019-12-17
   Purpose: This is the general parent class to the Student and Teacher classes.
*/

/* --------------------------------------------------------------------
 * Dec. 17
 * -Creation of class
 * -Added all fields and methods
 * --------------------------------------------------------------------
 * Dec. 20
 * -adding timetable to person
 * --------------------------------------------------------------------
*/

import java.util.*;

// Example of Encapsulation and Inheritance
abstract public class Person {
   protected String name;           // the person's name
   protected int ID;                // the person's associated ID
   protected Timetable tb;          // the person's school timtable (delete?)
   
  // Constructors
   public Person (String name, int ID) {
      this.name = name;
      this.ID = ID;
   }
   
  // Accessors and Mutators
   public String getName () {
      return name;
   }
   
   public int getID () {
      return ID;
   }
   
   public void setName (String name) {
      this.name = name;
   }
   
   public void setID (int ID) {
      this.ID = ID;
   }

   // Instance Methods
   
   /* 
   * Takes in an explicit Person argument and returns an integer indicating the relative difference in name
   * @param {Person} other - the other Person to compare names with 
   * Returns int - returns 0 if the same name, positive if the implicit Person comes alphabetically before the explicit Person, and vice versa
   */
   public int compareToName (Person other) {
      return name.compareTo(other.name);
   }
  
  	/* 
  	* Takes in an explicit Person argument and returns an integer indicating the relative difference in ID
  	* @param {Person} other - the other Person to compare IDs with 
   * Returns int - returns 0 if the same ID, positive if the implicit Person's ID comes before the explicit Person ID, and vice versa
  	*/
   public int compareToID (Person other) {
      return this.ID - other.ID;
   } 
    
   /* 
  	* Takes in an explicit Person argument and returns true if the IDs are the same and false otherwise
  	* @param {Person} other - the other Person to compare IDs with 
  	*/
   public boolean equals (Person other) {
      return (this.compareToID(other) == 0);
   }
   
   // Outputs information about the Person
   // Returns String - containing formatted information
   public String toString () {
      String display = "Name: " + name;
      display += "\nID: " + ID;
    
      return display;
   }
}