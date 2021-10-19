/*
   Class Name: Classroom.java
   Author: Juliet
   Creation Date: 2019-12-17
   Purpose: This class represents a classroom, which is a section of a course.
*/

public class Classroom {
   public static final int CAPACITY = 30;       // the maximum number of students in a class
   public static final int MINIMUM = 10;        // the minimum number of students in a class

   private int section;          // the section of the class
   private Course course;        // the associated course
   private Teacher teacher;      // the class's teacher
   private int period;           // the period that the course is in
   private Student[] students;   // the list of students enrolled in the class currently
   private int numStudents;      // the number of enrolled students
   private Waitlist waitlist;    // the waitlist for the class

  	// Constructors
   public Classroom (int s, Course c, Teacher t, int p, Student[] list) {
      section = s;
      course = c;
      teacher = t;
      period = p;
      students = new Student[CAPACITY];
        
      for (int i = 0; i < CAPACITY; i++) {
         students[i] = list[i];
         numStudents++;
      }
      
      int numWaiting = list.length - numStudents;
      Student[] wtList = new Student[numWaiting];
      int count = 0;
      for (int i = numStudents; i < list.length; i++) {
         wtList[count] = list[i];
         count++;
      }      
      
      waitlist = new Waitlist(wtList);
   }
  
   public Classroom (int s, Course c) {
      section = s;
      course = c;
      Student[] list = new Student[0];
      waitlist = new Waitlist(list);
      numStudents = 0;
      students = new Student[CAPACITY];
   }
  
   // Accessors and Mutators
   public int getSection () {
      return section;
   }  
   
   public void setSection (int section) {
      this.section = section;
   }
  
   public Teacher getTeacher () {
      return teacher;
   }
  
   public Waitlist getWaitlist () {
      return waitlist;
   }
   
   public int getPeriod () {
      return period;
   }
   
   public Course getCourse () {
      return course;
   }
  
   public int getNumStudents () {
      return numStudents;
   }
   
   public Student[] getStudents () {
      return students;
   }
  
   public String getClassCourseCode () {
      return course.getCourseCode();
   }

   public void setPeriod (int period) {
      this.period = period;
   }
   
   public void setTeacher (Teacher teacher) {
      if (teacher == null) {
         this.teacher = null;
      } else {
         this.teacher = teacher;
      }
   }

   // Instance Methods

  	/* 
  	* Adds a student to the class
   * Returns true if the addition was successful, and adds the student to the class waitlist and returns false if the class is full
  	* @param {Student} s - a Student object
  	*/
   public boolean addStudent (Student s) {
      if (findStudentIndex(s) == -1) 
         if (isFull()) {
            waitlist.addStudent(s);
            return false;
         } else if (s.addCourse(this)) {
            students[numStudents] = s;
            numStudents++;
            return true;
         } else
            return false;
      else
         return false;
   }
  
   /* 
  	* Removes a student from the class
  	* @param {Student} s - a Student object to be removed from the classroom
   * Returns boolean - indicating whether or not the change was successful
  	*/
   public boolean removeStudent (Student s) {
      int index = findStudentIndex(s);
      if (index == -1) 
         return false;
      
      students[index] = null;
      for (int i = index; i < numStudents-1; i++) {
         students[i] = students[i+1];
      }
      students[numStudents-1] = null;
      numStudents--;
   
      updateWaitlist();
      return true;
   }
  
   /* 
  	* Adds a student to the class waitlist
  	* @param {Student} s - a Student object
   * Returns void
  	*/
   public void addToWaitlist (Student s) {
      waitlist.addStudent(s);
   }
  
   /* 
	* Updates the waitlist, runs a single time and if there is a space in the classroom, add the first student on the waitlist to the class
   * Removes the student from the waitlist if there is a conflict with the student's timetable
  	* Returns boolean - indicating if a student was added to the classroom from the waitlist
	*/
   public boolean updateWaitlist () {
      if (!(isFull()) && waitlist.getNumWaiting() != 0)
         if (waitlist.addFirstWaiting(this)) {
            numStudents++;
            return true;
         }
      	
      return false;
   }
  
   // Returns boolean - indicating whether or not the class is full
   public boolean isFull () {
      return numStudents == CAPACITY; 
   }
  
  	// Returns a String containing formatted information of the waitlist
   public String toString () {
      String display = "Section: " + section + "\nPeriod: " + period;
      display += "\n" + course.toString();
      display += "\nStudents: " + numStudents;
      display += "\n" + waitlist.getNumWaiting() + " students on waitlist";
      
      return display;
   }
  
  	// A private method to find the index of the student on the class list given a student
  	// Returns int - -1 if the student was not found
   private int findStudentIndex (Student s) {
      for (int i = 0; i < numStudents; i++)
         if (students[i].getID() == s.getID())
            return i;
      return -1;
   }
}