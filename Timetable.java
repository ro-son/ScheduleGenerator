/* 
   Class Name: Timetable.java
   Author: Lucas
   Creation Date: 2019-12-17
   Purpose: This class edits the independent timetables of each student.
*/

import java.io.*;

public class Timetable {
   public static final int NUM_PERIODS = 4;     // the number of periods in the timetable
   
   public Classroom[] periods;                 // the classes in the timetable
   
   // Constructors
   public Timetable () {
      periods = new Classroom[4];
   }
   
   public Classroom getClass (int period) {
      return periods[period-1];
   }
   
   // Instance Methods
   
   /* 
   * Adds a classroom to the timetable
   * @param {Classroom} userClass - Classroom to be added to the timetable
   * Returns boolean - indicating the successful addition of the classroom to the timetable
   */
   public boolean addClass (Classroom userClass) {
      int classPeriod = userClass.getPeriod();
      if (periods[classPeriod] == null) {
         periods[classPeriod] = userClass;
         return true;
      } else {
         return false;
      }
   }
   
   public void addClass (Classroom c, int period) {
      periods[period] = c;
   }
   
   public void addSpare (int target) {
      periods[target] = null;
   }
   
   /* 
   * Removes a class from the timetable
   * @param {int} classPeriod - period of the class to be removed (0-3)
   * Returns void
   */
   public void removeClass (int classPeriod) {
      periods[classPeriod] = null;
   }
   
   /*
   * Indicates if the timetable has a course
   * @param {String} courseCode - The code of the course to check for in the timetable
   * Returns int - the period of a course on the timetable, -1 if the course does not exist
   */
   public int hasClass (String courseCode) {
      for (int i = 0; i < NUM_PERIODS; i++) {
         return i;
      }    
        
      return -1;
   }
   
   /*
   * Saves the formatted timetable as an html with all the courses of the timetable
   * @param {int} userID - the ID of the student or teacher to print the timetable of
   */
   public void printTimetable (int userID, String schoolName) {
      String firstCode, secondCode, firstName, secondName;
      Teacher t1, t2;
      Classroom c1, c2;
      try {
      // Create one directory
         boolean success = (new File(schoolName)).mkdir();
         if (success) {
            System.out.println("Directory: " + schoolName + " created");
         }
               
         BufferedWriter out = new BufferedWriter(new FileWriter(schoolName + "\\" + schoolName + " " + userID + ".html"));
         out.write("<html><head>");
         out.newLine();
         out.write("<title>Timetable</title>");
         out.newLine();
         out.write("</head>");
         out.newLine();
         out.write("<body>");
         out.newLine();
         out.write("<table border cellpadding = 3>");
         out.newLine();
         out.write("<tr><th><b>Period</b></th><th><b>Day 1</b></th><th><b>Day 2</b></th></tr>");
         out.newLine();
         for (int i = 0; i < 4; i ++) {
         // This variable allows the classroom to be displayed for both day 1 and day 2 periods
            int dummy = i + (int)Math.pow(-1,i);
            c1 = periods[i];
            c2 = periods[dummy];
         // Replaces code with Spare if class is a spare
            if (c1 == null) {
               firstCode = "Spare";
               firstName = "";
            }        
            else {
               firstCode = c1.getClassCourseCode() + " - " + c1.getSection();
               t1 = c1.getTeacher();
               if (t1 != null) {
                  firstName = t1.getName();
               } else {
                  firstName = "ZzTeacher";
               }
            }
            
            if (c2 == null) {
               secondCode = "Spare";
               secondName = "";
            }        
            else {
               secondCode = c2.getClassCourseCode() + " - " + c2.getSection();
               t2 = c2.getTeacher();
               if (t2 != null) {
                  secondName = t2.getName();
               } else {
                  secondName = "ZzTeacher";
               }
            }
         
            out.write("<tr><td>Period " + (i + 1) + "</td><td>" + firstCode + "</br>" + firstName + "</br></td><td>" 
                  + secondCode + "</br>" + secondName + "</br></td></tr>");
            out.newLine();
         }
         out.write("</table>");
         out.newLine();
         out.write("</body></html>");
         out.close();
      } catch (IOException ioe) {
         System.out.println("Error printing timetable!");
      }
   
   }
   
   // Returns a boolean array representing the spares in timetable
   public boolean[] findSpares () {
      boolean[] output = new boolean[4];
      for (int i = 0; i < 4; i++) {
         if (periods[i] == null)
            output[i] = true;
      }
      return output;
   }
   
   // Returns String - list of all the courses in the timetable
   public String toString () {
      String listPeriods = "";
      for (int i = 0; i < 4; i++) {
         listPeriods += periods[i] + " ";
      }
      return (listPeriods);
   }
}