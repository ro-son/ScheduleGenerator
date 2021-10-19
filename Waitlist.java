/*
   Class Name: Classroom.java
   Author: Juliet
   Creation Date: 2019-12-18
   Purpose: This class is the waitlist for a course, the list of students waiting to join.
*/

public class Waitlist {
   private static int capacity = 100;                    // the capacity of the waitlist
  
   private Student[] waiting = new Student[capacity];    // the list of students on the waitlist
   private int numWaiting;							            // the number of students currently on the waitlist
  
  	// Constructors
   public Waitlist (Student[] stu) {
      numWaiting = stu.length;
      if (numWaiting > capacity) {
         numWaiting = capacity;
      }
      
      for (int i = 0; i < numWaiting; i++)
         waiting[i] = stu[i];
   }
  
   // Accessors and Mutators
   public Student getStudent (int index) {
      return waiting[index];
   }
   
   public static void setCapacity (int cap) {
      capacity = cap;
   }
  
   public int getNumWaiting() {
      return numWaiting;
   }
  
   // Instance Methods
   
  	/* Adds the student to the waitlist, returns false if limit has been reached
   * @param {Student} s - A student to be added to the waitlist
   * Returns boolean - indicating whether or not the addition was successful
   */
   public boolean addStudent (Student s) {
      if (waiting.length == capacity) 
         return false;
      waiting[numWaiting] = s;
      numWaiting++;
      return true;
   } 
  
  	/* Removes corresponding student on the waitlist, and shifts the list up
  	* @param {int} index - index of the student to remove
  	* Returns void
  	*/ 
   public void removeStudent (int index) {
      waiting[index] = null;
      numWaiting--;
      for (int i = index; i < numWaiting; i++)
         waiting[i] = waiting[i+1];
      waiting[numWaiting] = null;
      numWaiting--;
   }
      
   /* Adds the first student waiting on the waitlist to the classroom
   * @param {Classroom} c - The class where the student will be aded to
   * Returns boolean - indicating whether or not the change was successful
   */
   public boolean addFirstWaiting (Classroom c) {
      if (!(c.isFull()) && waiting[0].addCourse(c)) {
         c.addStudent(waiting[0]);
         removeStudent(0);
         return true;
      } else 
         return false;
   }
  
  	// Returns String - list of students on the waitlist, formatted
   public String toString() {
      String display = "Waitlist: " + numWaiting + " students";
      for (int i = 0; i < numWaiting; i++)
         display += "\n" + waiting[i].getName();
      return display;
   }
}