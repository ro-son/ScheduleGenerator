/* 
   Class Name: ChronoApp.java
   Author: Robin
   Creation Date: 2019-12-19
   Purpose: This class provides the menu and the user interface for the guidance counselor.
*/

import java.io.*;
import java.util.*;

public class ChronoApp {
   public static void main (String[] args) {
      Scanner sc = new Scanner(System.in);
      
      final String SYSTEM_FILE = "_System Files";
      final String SCHOOL_FILE = "schools.txt";
      final String STU_FILE = "Students.txt";
      final String TEA_FILE = "Teachers.txt";
      final String COU_FILE = "Courses.txt";
   
      String schoolName = "";
      String[] schools;
      int numSchool = -1;
      int choice = -1;
      int schoolIndex = -1;
      boolean newSchool;
      boolean success;
      ChronoManager cm;
    
      try {
         BufferedReader in = new BufferedReader(new FileReader(SYSTEM_FILE + "\\" + SCHOOL_FILE));
         numSchool = Integer.parseInt(in.readLine());
         in.close();
      } catch (IOException iox) {
         System.out.print("Error accessing file. ");
      }
    
      if (numSchool > 0) {
         System.out.println("Welcome to Chrono++!");
         System.out.println("Option 1: Start a new school.");
         System.out.println("Option 2: Select an existing school.");
         
         do {
            try {
               System.out.print("\nEnter an option: ");
               choice = sc.nextInt();
               if (choice != 1 && choice != 2) System.out.println("Enter a valid choice!");
            } catch (InputMismatchException ime) {
               sc.nextLine();
               System.out.println("Enter a valid number!");
            }
         } while (choice != 1 && choice != 2);  
         sc.nextLine();
         
         if (choice == 1) {
            System.out.print("Enter the name of the school: ");
            schoolName = sc.nextLine();
            
            cm = new ChronoManager(schoolName);
            newSchool = true;
         } else {
            try {
               success = (new File(SYSTEM_FILE)).mkdir();
               if (success) {
                  System.out.println("Directory: " + SYSTEM_FILE + " created");
               }
               BufferedReader in = new BufferedReader(new FileReader(SYSTEM_FILE + "\\" + SCHOOL_FILE));
               numSchool = Integer.parseInt(in.readLine());
               schools = new String[numSchool];
            
               for (int i = 0; i < numSchool; i++) {
                  schools[i] = in.readLine();
               }
            
               in.close();
               
               for (int i = 0; i < numSchool; i++) {
                  System.out.println((i+1) + ": " + schools[i]);
               }
               
               do {
                  try {
                     System.out.print("\nEnter the school's number: ");
                     schoolIndex = sc.nextInt()-1;
                     if (schoolIndex < 0 || schoolIndex > numSchool - 1) System.out.println("Enter a valid choice!");
                  } catch (InputMismatchException ime) {
                     sc.nextLine();
                     System.out.println("Enter a valid number!");
                  }
               } while (schoolIndex < 0 || schoolIndex > numSchool-1);
               
               schoolName = schools[schoolIndex];
            } catch (IOException iox) {
               System.out.println("Error accessing file.");
            }
         
            cm = new ChronoManager(schoolName);
            cm.addStudent(schoolName + STU_FILE);
            cm.addTeacher(schoolName + TEA_FILE);
            cm.addCourse(schoolName + COU_FILE);
            newSchool = false;
         }
         
         System.out.println();
         menu(cm, newSchool);
      } else if (numSchool == 0) {
         System.out.println("Welcome to Chrono++, new user!");
         System.out.print("Enter the name of your school: ");
         schoolName = sc.nextLine();
            
         cm = new ChronoManager(schoolName);
         newSchool = true;
         
         System.out.println();
         menu(cm, newSchool);
      } else {
         System.out.println("The schools.txt file in the System Files folder is missing.");
      }
      
      System.out.println();
      System.out.println("Now closing program.");
   }
  
   public static void menu (ChronoManager cm, boolean newSchool) {
      Scanner sc = new Scanner(System.in);
      final int DEF = -1;
   
      int option = DEF;
      int save = DEF;
      int personID = DEF;
      int stuIndex;
      int teaIndex;
      int couIndex;
      int grade;
      int period;
      int stuSize;
      String courseID;
      String fileName;
      String dept;
      String courseCode;
      String name;
      String courseToAdd;
      String[] preferCourse;
      boolean exit;
      boolean delSuccess = false;
      boolean timetablesCreated = false;
      Course course;
      Classroom classToFind; // This stores the classroom returned when findClass is called      
      Student[] students;
      Teacher[] teachers;
      Course[] courses;
      ArrayList<Classroom> classes;
    
      do {
         System.out.println("------------------------------------------");
         System.out.println("                Main Menu");
         System.out.println("School: " + cm.getSchoolName());
         System.out.println("------------------------------------------");
         System.out.println("1.  Add/Delete new courses");
         System.out.println("2.  Add/Delete new students");
         System.out.println("3.  Add/Delete new teachers");
         System.out.println("4.  Create timetables for semester");
         System.out.println("5.  Print timetables");
         System.out.println("6.  Search for person");
         System.out.println("7.  Search for course");
         System.out.println("8.  Print list of courses");
         System.out.println("9.  Print list of students");
         System.out.println("10. Print list of teachers");
         System.out.println("11. Print the classes running each period");
         System.out.println("12. Exit");
         System.out.println("------------------------------------------");
      
         do {
            try {
               option = DEF;
               
               System.out.print("\nEnter an option: ");
               option = sc.nextInt();
               if (option < 1 || option > 12) System.out.println("Enter a valid choice!");
            } catch (InputMismatchException ime) {
               sc.nextLine();
               System.out.println("Enter a valid number!");
            }
         } while (option < 1 || option > 12);
         System.out.println();
         sc.nextLine();
      
         switch (option) {
            case 1:	// Add/Delete new courses
               System.out.println("Adding/Deleting Courses");
               System.out.println("1. Add Courses");
               System.out.println("2. Delete Courses");
            
               do {
                  try {
                     option = DEF;
                  
                     System.out.print("\nEnter an option: ");
                     option = sc.nextInt();
                     if (option < 1 || option > 2) System.out.println("Enter a valid choice!");
                  } catch (InputMismatchException ime) {
                     sc.nextLine();
                     System.out.println("Enter a valid number!");
                  }
               } while (option < 1 || option > 2);
               sc.nextLine();
            
               if (option == 1) {
                  System.out.println("Now adding courses");
                  System.out.println("1. Enter course information manually");
                  System.out.println("2. Enter course information from file");
                  System.out.println("3. Cancel and return to main menu");
               
                  do {
                     try {
                        option = DEF;
                        
                        System.out.print("\nEnter an option: ");
                        option = sc.nextInt();
                        if (option < 1 || option > 3) System.out.println("Enter a valid choice!");
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter a valid number!");
                     }
                  } while (option < 1 || option > 3);
                  sc.nextLine();
               
                  if (option == 1) {
                     System.out.print("Enter the course code: ");
                     courseCode = sc.nextLine();
                     System.out.print("Enter the department: ");
                     dept = sc.nextLine();
                     System.out.print("Enter the grade level: ");
                     grade = sc.nextInt();
                     sc.nextLine();
                  
                     cm.addCourse(courseCode, dept, grade);
                  } else if (option == 2) {
                     System.out.print("Enter the file name (the file must be placed in the root directory): ");
                     fileName = sc.nextLine();
                     cm.addCourse(fileName + ".txt");
                  } else {
                     System.out.println("Returning to main menu...");
                  }              
               } else {
                  exit = false;
                  do {
                     System.out.print("Enter the course code of the course to be deleted: ");
                     
                     courseID = sc.nextLine();
                     if (courseID.toLowerCase().equals("exit")) exit = true;
                  
                     if (!exit) {
                        delSuccess = cm.removeCourse(courseID);
                        if (!delSuccess) { 
                           System.out.println("Course not found, try again or enter EXIT as the course code to return to main menu.\n");
                        } else {
                           System.out.println("Course deleted successfully.");
                        }
                     } else {
                        System.out.println("Returning to main menu...");
                     }
                  } while (!delSuccess && !exit);
               } 
               break;
            case 2: // Add/Delete new students
               System.out.println("Adding/Deleting Students");
               System.out.println("1. Add Students");
               System.out.println("2. Delete Students");
            
               do {
                  try {
                     option = DEF;
                     
                     System.out.print("\nEnter an option: ");
                     option = sc.nextInt();
                     if (option < 1 || option > 2) System.out.println("Enter a valid choice!");
                  } catch (InputMismatchException ime) {
                     sc.nextLine();
                     System.out.println("Enter a valid number!");
                  }
               } while (option < 1 || option > 2);
               sc.nextLine();
            
               if (option == 1) {
                  System.out.println("Now adding students");
                  System.out.println("1. Enter student information manually");
                  System.out.println("2. Enter student information from file");
                  System.out.println("3. Cancel and return to main menu");
               
                  do {
                     try {
                        option = DEF;
                        
                        System.out.print("\nEnter an option: ");
                        option = sc.nextInt();
                        if (option < 1 || option > 3) System.out.println("Enter a valid choice!");
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter a valid number!");
                     }
                  } while (option < 1 || option > 3);
                  sc.nextLine();
                  
                  if (option == 1) {
                     System.out.print("Enter the student name: ");
                     name = sc.nextLine();
                     System.out.print("Enter the student's ID: ");
                     personID = sc.nextInt();
                     sc.nextLine();
                     System.out.print("Enter the grade: ");
                     grade = sc.nextInt();
                     sc.nextLine();
                     System.out.print("Enter their list of prefered courses (in their course code, separated by a space): ");
                     preferCourse = sc.nextLine().split(" ");
                  
                     cm.addStudent(name, grade, personID, preferCourse);
                  } else if (option == 2) {
                     System.out.print("Enter the file name (the file must be placed in the root directory): ");
                     fileName = sc.nextLine();
                     cm.addStudent(fileName + ".txt");
                  } else {
                     System.out.println("Returning to main menu...");
                  }
               
               } else {
                  exit = false;
                  do {
                     try {
                        personID = DEF;
                     
                        System.out.print("\nEnter the student ID of the student to be deleted: ");
                        personID = sc.nextInt();
                        if (personID == 1) exit = true;
                     
                        if (!exit) {
                           delSuccess = cm.removeStudent(personID);
                           if (!delSuccess) {
                              System.out.println("Student not found, try again or enter 1 as the student ID to return to main menu");
                           } else {
                              System.out.println("Student deleted successfully");
                           }
                        } else {
                           System.out.println("Returning to main menu...");
                        }
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter an integer!");
                     }
                  } while (!delSuccess && !exit);
                  sc.nextLine();
               } 
               break;
            case 3: // Add/Delete new teachers
               System.out.println("Adding/Deleting Teachers");
               System.out.println("1. Add Teachers");
               System.out.println("2. Delete Teachers");
            
               do {
                  try {
                     option = DEF;
                  
                     System.out.print("\nEnter an option: ");
                     option = sc.nextInt();
                     if (option < 1 || option > 2) System.out.println("Enter a valid choice!");
                  } catch (InputMismatchException ime) {
                     sc.nextLine();
                     System.out.println("Enter a valid number!");
                  }
               } while (option < 1 || option > 2);
               sc.nextLine();
            
               if (option == 1) {
                  System.out.println("Now adding Teachers");
                  System.out.println("1. Enter teacher information manually");
                  System.out.println("2. Enter teacher information from file");
                  System.out.println("3. Cancel and return to main menu");
               
                  do {
                     try {
                        option = DEF;
                        
                        System.out.print("\nEnter an option: ");
                        option = sc.nextInt();
                        if (option < 1 || option > 3) System.out.println("Enter a valid choice!");
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter a valid number!");
                     }
                  } while (option < 1 || option > 3);
                  sc.nextLine();
               
                  if (option == 1) {
                     System.out.print("Enter the teacher name: ");
                     name = sc.nextLine();
                     System.out.print("Enter the teacher's ID: ");
                     personID = sc.nextInt();
                     sc.nextLine();
                     System.out.print("Enter the teacher's department: ");
                     dept = sc.nextLine();
                  
                     cm.addTeacher(name, personID, dept);
                  } else if (option == 2) {
                     System.out.print("Enter the file name (the file must be placed in the root directory): ");
                     fileName = sc.nextLine();
                     cm.addTeacher(fileName + ".txt");
                  } else {
                     System.out.println("Returning to main menu...");
                  }           
               } else {
                  exit = false;
                  do {
                     try {
                        personID = DEF;
                        
                        System.out.print("\nEnter the teacher ID of the teacher to be deleted: ");
                        personID = sc.nextInt();
                        if (personID == 1) exit = true;
                     
                        if (!exit) {
                           delSuccess = cm.removeTeacher(personID);
                           if (!delSuccess) 
                              System.out.println("Teacher not found, try again or enter 1 as the teacher ID to return to main menu");
                           else
                              System.out.println("Teacher deleted successfully");
                        } else 
                           System.out.println("Returning to main menu...");
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter an integer!");
                     }
                  } while (!delSuccess && !exit);
                  sc.nextLine();
               } 
               break;
            case 4: // Create timetables for semester
               if (!timetablesCreated && cm.checkReady()) {
                  System.out.println("Generating timetables. Please wait.");
                  cm.generateTimetable();
                  System.out.println("Generating complete: all students and teachers now have a suitable timetable.");
                  timetablesCreated = true;;
               } else {
                  System.out.println("Option not available. Returning to menu.");
                  System.out.println();
               }
               break;
            case 5: // Print timetables
               if (timetablesCreated) {
                  System.out.println("Saving all timetables into " + cm.getSchoolName() + "Timetables");
                  System.out.println("Please wait while the files are being updated.");
               
                  students = cm.getStudents();
                  stuSize = cm.getStuSize();
                  for (int i = 0; i < stuSize; i++) {
                     students[i].printTimetable(cm.getSchoolName());
                  }
                  
                  System.out.println("Saved " + stuSize + " student timetables to corresponding directory.");
               } else {
                  System.out.println("Option not available. Returning to menu.");
                  System.out.println();
               }
               break;
            case 6: // Search for person
               System.out.println("Search for people");
               try {
                  personID = DEF;
                  
                  System.out.print("\nEnter an ID: ");
                  personID = sc.nextInt();
               } catch (InputMismatchException ime) {
                  sc.nextLine();
                  System.out.println("Enter an integer!");
               }
               sc.nextLine();
            
               students = cm.getStudents();
               teachers = cm.getTeachers();
               stuIndex = cm.findStudent(personID);
               teaIndex = cm.findTeacher(personID);
               if (stuIndex == -1 && teaIndex == -1) {
                  System.out.println("Person not found.");
               } else if (stuIndex > -1) {
                  System.out.println("\nA student was found:");
                  System.out.println(students[stuIndex]);
               
                  System.out.println();
                  System.out.println("1. Print student timetable");
                  System.out.println("2. Change courses for student");
                  System.out.println("3. Return to main menu");
               
                  do {
                     try {
                        option = DEF;
                        
                        System.out.print("\nEnter an option: ");
                        option = sc.nextInt();
                        if (option < 1 || option > 3) System.out.println("Enter a valid choice!");
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter a valid option!");
                     }
                  } while (option < 1 || option > 3);         
                  sc.nextLine();
                  
                  if (timetablesCreated && option == 1) {                    
                     students[stuIndex].printTimetable(cm.getSchoolName());
                     System.out.println("File saved to corresponding school folder.");
                  } else if (timetablesCreated && option == 2) {
                     System.out.println();
                     System.out.println("1. Drop out of a course");
                     System.out.println("2. Switch courses");
                     System.out.println("3. Add a course into an empty slot");
                  
                     do {
                        try {
                           option = DEF;
                        
                           System.out.print("\nEnter an option: ");
                           option = sc.nextInt();
                           if (option < 1 || option > 3) System.out.println("Enter a valid choice!");
                        } catch (InputMismatchException ime) {
                           sc.nextLine();
                           System.out.println("Enter a valid number!");
                        }	
                     } while (option < 1 || option > 3);                
                     sc.nextLine();
                     
                     if (option == 1) {
                        System.out.print("Enter the period of the course to be dropped: ");
                        String sPeriod = sc.nextLine();
                        period = Integer.valueOf(sPeriod) - 1 ;
                        
                        while (!(period >= 0 && period <= 3)) {
                           System.out.println("Please enter between 1 and 4");
                           period = Integer.valueOf(sPeriod) - 1;                       
                        }
                     
                        students[stuIndex].getTimetable().getClass(period+1).removeStudent(students[stuIndex]);
                        students[stuIndex].addSpare(period);
                     
                     } else if (option == 2) {
                        System.out.print("Enter the period of the course to be switched out: ");
                        String sPeriod = sc.nextLine();
                        period = Integer.valueOf(sPeriod) - 1;
                        
                        while (!(period >= 0 && period <= 3)) {
                           System.out.println("Please enter between 1 and 4");
                           sPeriod = sc.nextLine();
                           period = Integer.valueOf(sPeriod) - 1;                     
                        }                        
                        
                        System.out.print("Enter the course to be added: ");
                        courseToAdd = sc.nextLine();
                        
                        classToFind = cm.findClass(period, courseToAdd, cm.getFinalMT());
                        
                        if (classToFind != null) {
                           students[stuIndex].getTimetable().getClass(period).removeStudent(students[stuIndex]);
                           students[stuIndex].getTimetable().getClass(period).addStudent(students[stuIndex]);
                           students[stuIndex].changeCourse(period, courseToAdd, cm);
                           System.out.println("Change successful");
                        }
                        else
                           System.out.println("Invalid class or class is full!");
                     
                     } else {
                        System.out.print("Enter the course to be added: ");
                        courseToAdd = sc.nextLine();
                     
                        if (students[stuIndex].addCourse(courseToAdd)) {
                           System.out.println("Change successful");
                        } else {
                           System.out.println("Change unsuccessful");
                        }
                     }
                  } else { 
                     if (!timetablesCreated && option!= 3) System.out.print("Option not available. ");
                     System.out.println("Redirecting...");
                  }       
               } else if (teaIndex > -1) {
                  System.out.println("A teacher was found:");
                  System.out.println(teachers[teaIndex]);
               }
               break;
            case 7: // Search for course
               System.out.println("Search for courses");
               System.out.print("Enter a course code: ");
               courseID = sc.nextLine();
               couIndex = cm.findCourse(courseID);
               
               if (couIndex == -1) {
                  System.out.println("Course not found.");
                  break;
               } else if (couIndex > -1) {
                  System.out.println("\nA course was found:");
                  course = (cm.getCourses())[couIndex];
                  
                  System.out.println();
                  System.out.println("1. Display course information");
                  System.out.println("2. Display all classes of the course");
                  System.out.println("3. Print list of students on the waitlist");
                  System.out.println("4. Produce list of students currently enrolled in the course");
                  System.out.println("5. Return to main menu");
               
                  do {
                     try {
                        option = DEF;
                        
                        System.out.print("\nEnter an option: ");
                        option = sc.nextInt();
                        if (option < 1 || option > 5) System.out.println("Enter a valid choice!");
                     } catch (InputMismatchException ime) {
                        sc.nextLine();
                        System.out.println("Enter a valid number!");
                     }
                  } while (option < 1 || option > 5);
                  sc.nextLine();
               
                  switch(option) {
                     case 1:	
                        System.out.println(course);
                        break;
                     case 2:
                        classes = course.getClasses();
                        for (int i = 0; i < course.getNumClasses(); i++) {
                           System.out.println(classes.get(i));
                        }
                        break;
                     case 3:
                        classes = course.getClasses();
                        for (int i = 0; i < course.getNumClasses(); i++) {
                           System.out.println("Section: " + classes.get(i).getSection());
                           System.out.println(classes.get(i).getWaitlist());
                        }
                        break;
                     case 4:
                        classes = course.getClasses();
                        Student[] classStudents;
                        for (int i = 0; i < course.getNumClasses(); i++) {
                           System.out.println("Section: " + classes.get(i).getSection());
                           classStudents = classes.get(i).getStudents();
                           for (int j = 0; j < classStudents.length; j++) {
                              if (classStudents[j] != null) {
                                 System.out.println(classStudents[j]);
                              }
                           }
                           System.out.println();
                        }
                        break;
                     case 5:
                        System.out.println("Returning to main menu...");
                        break;
                  }             
               }             
               break;
            case 8: // Print list of courses
               courses = cm.getCourses();
            
               for (int i = 0; i < cm.getCouSize(); i++) {
                  System.out.println(courses[i] + "\n");
               }
               break;
            case 9: // Print list of students
               students = cm.getStudents();
            
               for (int i = 0; i < cm.getStuSize() ; i++) {
                  System.out.println(students[i] + "\n");
               }
               break; // Print list of teachers
            case 10:
               teachers = cm.getTeachers();
            
               for (int i = 0; i < cm.getTeaSize(); i++) {
                  System.out.println(teachers[i] + "\n");
               }
               break;
            case 11:
               if (timetablesCreated) {
                  System.out.println(cm.getFinalMT());
               } else {
                  System.out.println("Option not available. Returning to menu.");
                  System.out.println();
               }
               break;
            case 12: // Exit
               System.out.println("Thank you for using Chrono++!");
               System.out.println("Enter 1 to close without saving.");
               System.out.println("Enter 2 to save and close.");
            
               do {
                  try {
                     save = DEF;
                     
                     System.out.print("\nEnter an option: ");
                     save = sc.nextInt();
                     if (save < 1 || save > 2) System.out.println("Enter a valid choice!");
                  } catch (InputMismatchException ime) {
                     sc.nextLine();
                     System.out.println("Enter a valid number!");
                  }
               } while (save < 1 || save > 2);
               sc.nextLine();
            
               if (save == 2) {
                  cm.saveAll(newSchool);
               }
               
               break;
         }
         
         if (option != 12) {
            System.out.println("Confirm --- Press ENTER to return to main menu.");
            sc.nextLine();
         }        
      } while (option != 12);
   }
}