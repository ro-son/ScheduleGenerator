/* 
   Class Name: MasterTable.java
   Author: Lucas
   Creation Date: 2019-12-18
   Purpose: This class creates the MasterTable, which contains all courses that the school is running in its periods.
*/

import java.util.*;

public class MasterTable {
   public double fitnessLevel;                        // the fitness value of the MasterTable, representing how well it fits everyone's timetables
   public static final int PERIODS = 4;               // the number of periods in a timetable
   public ArrayList<ArrayList<Classroom>> table;      // the MasterTable's array of classrooms
  
   // Constructors
   public MasterTable () {
      table = new ArrayList<>(PERIODS);
      for (int i = 0; i < PERIODS; i++) {
         table.add(new ArrayList<Classroom>());
      }
   }
   
   // Accessors and Mutators
   public Classroom[] getClasses (int period) {
      Classroom[] classes = new Classroom[table.get(period).size()];
      
      for (int i = 0; i < classes.length; i++) {
         classes[i] = table.get(period).get(i);
      }
      
      return classes;
   }
   
   public double getFitnessLevel () {
      return fitnessLevel;
   }
  
   public ArrayList<ArrayList<Classroom>> getTable () {
      return table;
   }
  
   public void setFitnessLevel (double level) {
      fitnessLevel = level;
   }
  
   // Instance Methods
   
   public void addClass (int targetPeriod, Classroom newClass) {
      if (targetPeriod >= 0 && targetPeriod <4) {
         table.get(targetPeriod).add(newClass);
      }
   }
  
   public void remove (int period, Classroom target) {
      table.get(period).remove(target);
   }
   
   public void addFitness () {
      fitnessLevel++;
   }
  
	//returns the list of courses in a given period
   public List getClassrooms (int targetPeriod) {
      return table.get(targetPeriod);
   }
   
   public String toString () {
      String a = "Period 1";
      for (int i = 0 ; i < table.get(0).size() ; i++) {
         a += "\n"+table.get(0).get(i).getClassCourseCode() + "-" + table.get(0).get(i).getSection()+  " | " + table.get(0).get(i).getNumStudents() + " current students.";
      }
      a += "\nPeriod 2";
      for (int i = 0 ; i < table.get(1).size() ; i++) {
         a += "\n"+table.get(1).get(i).getClassCourseCode() + "-" + table.get(1).get(i).getSection() +" | " + table.get(1).get(i).getNumStudents()+ " current students.";
      }
      a += "\nPeriod 3";
      for (int i = 0 ; i < table.get(2).size() ; i++) {
         a += "\n"+table.get(2).get(i).getClassCourseCode() + "-" + table.get(2).get(i).getSection()+" | " + table.get(2).get(i).getNumStudents() + " current students.";
      }
      a += "\nPeriod 4";
      for (int i = 0 ; i < table.get(3).size() ; i++) {
         a += "\n"+table.get(3).get(i).getClassCourseCode() + "-" + table.get(3).get(i).getSection()+" | " + table.get(3).get(i).getNumStudents() + " current students.";
      }
      return a;
   }
}