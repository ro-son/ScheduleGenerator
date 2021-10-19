/* 
   Class Name: ChronoManager.java
   Author: Robin
   Creation Date: 2019-12-18
   Purpose: This class performs the menu actions from ChronoApp and makes the timetables.
*/

/* --------------------------------------------------------------------
 * Dec. 18
 * -Creation of class
 * -Added all fields
 * --------------------------------------------------------------------
 * Dec. 19
 * - Added methods (constructors, accessors and mutators)
 * --------------------------------------------------------------------
 * Dec. 20
 * - Added methods
 * --------------------------------------------------------------------
 * Dec. 28
 * - Added course-changing methods
 * --------------------------------------------------------------------
 * Dec. 29
 * - Finished course-changing methods. Began preference read-in
 * --------------------------------------------------------------------
 * Dec. 30
 * - Finished preference read-in and determining how many classrooms
 * 	 should be created
 * - Finished recursive function that randomly generates timetables
 * - *Some code ended up getting deleted*
 * --------------------------------------------------------------------
 * Jan 1
 * -Redid lost code from Dec. 30
 * -Completed algorithm that checks whether or not students can fit into the timetable
 * --------------------------------------------------------------------
 * Jan 2
 * -Completed parent selection and population swapping
 * -Began child creation
 * --------------------------------------------------------------------
*/

import java.io.*;
import java.util.*;

public class ChronoManager {
   private static final int POPULACE = 100;					// population size in genetic algorithm
   private static final double MUTATION_RATE = 0.01;		// chance of mutation
   private static final int MAX_ITERATIONS = 125;			// number of times that the algorithm is repeated
   
   private static final int MAXSTU = 250;	   // max number of students
   private static final int MAXTEA = 20;		// max number of classrooms
   private static final int MAXCOU = 25;		// max number of courses
   private static final int MAXCLA = 100;		// max number of classes. Necessary???????
   private static final int PERTT = 4;       // the number of periods that exist in a timetable. Excludes lunch
   
   private String schoolName;                // name of the current school
   private int stuSize;	                     // number of students in array
   private int teaSize;	                     // number of teachers in array
   private int couSize;	                     // number of courses in school
   private int claSize;	                     // number of classes running in school
   private Student[] students;					// all students in school
   private Teacher[] teachers;					// all teachers in school
   private Course[] courses;						// all available courses
   private Classroom[] classrooms;			   // all ongoing classes
   
   private MasterTable[] oldPop;				   // parent population of genetic algorithm
   private MasterTable[] newPop;				   // children population of genetic algorithm
   private MasterTable finalMT;				   // final generated mastertable
   
   // Constructors
   public ChronoManager (String schoolName) {
      this.schoolName = schoolName;
      students = new Student[MAXSTU];
      teachers = new Teacher[MAXTEA];
      courses = new Course[MAXCOU];
      classrooms = new Classroom[MAXCLA];
      stuSize = 0;
      teaSize = 0;
      couSize = 0;
      claSize = 0;
      oldPop = new MasterTable[POPULACE];
      newPop = new MasterTable[POPULACE];
      finalMT = null;
   }
   
   // Accessors
   public Student[] getStudents () {
      return students;
   }
   
   public Teacher[] getTeachers () {
      return teachers;
   }
   
   public Course[] getCourses () {
      return courses;
   }
   
   public Classroom[] getClassrooms () {
      return classrooms;
   }
   
   public String getSchoolName () {
      return schoolName;
   }
   
   public int getTeaSize () {
      return teaSize;
   }
   
   public int getClaSize () {
      return claSize;
   }
   
   public int getStuSize () {
      return stuSize;
   }
   
   public int getCouSize () {
      return couSize;
   }
   
   public MasterTable getFinalMT () {
      return finalMT;
   }
   
   // Instance Methods
   
   // Adds a student from a text file
   // @param {String} filename - the name of the file to import students from
   public void addStudent (String filename) {
      try {
         BufferedReader in = new BufferedReader(new FileReader("_System Files\\" + filename));
         int num = Integer.parseInt(in.readLine());
         String line;
         
         while((line = in.readLine()) != null) {
            addStudent(line, Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), (in.readLine()).split(" "));
         }
         
         in.close();
      } catch (IOException iox) {
         System.out.println("Error loading file.");
      }
   }
   
   // Adds a teacher from a text file
   // @param {String} filename - the name of the file to import students from
   public void addTeacher (String filename) {
      try {
         BufferedReader in = new BufferedReader(new FileReader("_System Files\\" + filename));
         int num = Integer.parseInt(in.readLine());
         String line;
         
         while((line = in.readLine()) != null) {
            addTeacher(line, Integer.parseInt(in.readLine()), in.readLine());
         }
         
         in.close();
      } catch (IOException iox) {
         System.out.println("Error loading file.");
      }
   }   
   
   // Adds courses from a text file
   // @param {String} filename - the name of the file to import courses from
   public void addCourse (String filename) {
      try {
         BufferedReader in = new BufferedReader(new FileReader("_System Files\\" + filename));
         int num = Integer.parseInt(in.readLine());
         String line;
         
         while((line = in.readLine()) != null) {
            addCourse(line, in.readLine(), Integer.parseInt(in.readLine()));
         }
         
         in.close();
      } catch (IOException iox) {
         System.out.println("Error loading file.");
      }
   }    
   
   /* 
   * Adds a student to the student database
   * @param {String} name - the name of the student being added
   * @param {int} grade - the grade of the student being added
   * @param {int} id - the ID of the student being added
   * @param {String[]} preferCourse - a list of course codes that the students wants to have in their timetable
   */
   public void addStudent (String name, int grade, int id, String[] preferCourse) {
      boolean duplicate = false;
      
      for (int i = 0; i < stuSize; i++) {
         if (students[i].getID() == id) duplicate = true;
      }
      
      if (stuSize < MAXSTU && !duplicate) {
         students[stuSize] = new Student(name, grade, id, preferCourse);
         stuSize++;
      } else if (stuSize >= MAXSTU) {
         System.out.println("Failure. Student capacity reached.");
      } else if (duplicate) {
         System.out.println(name + " is a duplicate student.");
      }
   }
   
   /* 
   * Adds a teacher to the teacher database
   * @param {String} name - the name of the teacher being added
   * @param {int} id - the ID of the teacher being added
   * @param {String} dept - department of the teacher, dictates the types of courses they are eligible to teach
   */
   public void addTeacher (String name, int id, String department) {
      boolean duplicate = false;
      
      for (int i = 0; i < teaSize; i++) {
         if (teachers[i].getID() == id) duplicate = true;
      }
      
      if (teaSize < MAXTEA && !duplicate) {
         teachers[teaSize] = new Teacher(name, id, department);
         teaSize++;
      } else if (teaSize >= MAXTEA) {
         System.out.println("Failure. Teacher capacity reached.");
      } else if (duplicate) {
         System.out.println(name + " is a duplicate teacher. ");
      }
   }
   
   /* 
   * Adds a course to the couses database
   * @param {String} courseCode - the course code of the course being added
   * @param {String} department - the department that the course belongs to
   * @param {int} grade - the grade level of the course
   */
   public void addCourse (String courseCode, String department, int grade) {
      boolean duplicate = false;
      
      for (int i = 0; i < couSize; i++) {
         if (courses[i].getCourseCode().equals(courseCode)) duplicate = true;
      }
      
      if (couSize < MAXCOU && !duplicate) {
         courses[couSize] = new Course (courseCode, department, grade);
         couSize++;
      } else if (couSize >= MAXCOU) {
         System.out.println("Course capacity reached.");
      } else if (duplicate) {
         System.out.println(courseCode + " is a duplicate course.");
      }
   }
   
   // Locates a student based on their ID through binary search. List of students is sorted first
   // @param {int} id - identifier/number of the student that needs to be found
   // Returns int - the most recent index of the student in the database
   public int findStudent (int id) {
      sortStudent();
      
      int top = stuSize-1;
      int bottom = 0;
      boolean found = false;
      int i = -1;
      int middle;
      
      while (top >= bottom && !found) {
         middle = (top+bottom)/2;
         if (id == students[middle].getID()) {
            found = true;
            i = middle;
         } else if (id > students[middle].getID()) {
            bottom = middle+1;
         } else if (id < students[middle].getID()) {
            top = middle-1;
         }
      }
      
      return i;
   }
   
   // Locates a teacher based on their id with binary search
   // @param {int} id - identifier/number of the teacher that needs to be found
   // Returns int - the most recent index of the teacher in the database
   public int findTeacher (int id) {
      sortTeacher();
      
      int top = teaSize-1;
      int bottom = 0;
      boolean found = false;
      int i = -1;
      int middle;
      
      while (top >= bottom && !found) {
         middle = (top+bottom)/2;
         if (id == teachers[middle].getID()) {
            found = true;
            i = middle;
         } else if (id > teachers[middle].getID()) {
            bottom = middle+1;
         } else if (id < teachers[middle].getID()) {
            top = middle-1;
         }
      }
      
      return i;
   }
   
   
   // Locates a teacher based on their department. Uses sequential search to find first matching teacher.
   // @param {String} department - name of department to find teacher in.
   // Returns Teacher - the first teacher that teaches the matching department in the database
   public Teacher findTeacher (String department) {
      for (int i = 0 ; i < teaSize ; i++) {
         if (teachers[i].getDept().equals(department)) {
            return teachers[i];
         }
      }
      return null;
   }
   
   // Locates a course based on their id
   // @param {String} id - Course code of the course that needs to be found
   // Returns int - the most recent index of the course in the database
   public int findCourse (String id) {
      sortCourse();
      
      int top = couSize-1;
      int bottom = 0;
      boolean found = false;
      int i = -1;
      int middle;
      
      while (top >= bottom && !found) {
         middle = (top+bottom)/2;
         if (id.equals(courses[middle].getCourseCode())) {
            found = true;
            i = middle;
         } else if (id.compareTo(courses[middle].getCourseCode()) > 0) {
            bottom = middle + 1;
         } else if (id.compareTo(courses[middle].getCourseCode()) < 0) {
            top = middle - 1;
         }
      }
      return i;
   }
   
   // Sorts students with bubble sorting
   private void sortStudent () {
      boolean sorted = false;
      Student swap;
      
      for (int upperBound = stuSize-1; upperBound >= 1 && !sorted; upperBound--) {
         sorted = true;
         for (int i = 0; i < upperBound; i++) {
            if (students[i].compareToID(students[i+1]) > 0) {
               swap = students[i];
               students[i] = students[i+1];
               students[i+1] = swap;
               sorted = false;
            }
         }
      }
   }
   
   // Sorts teachers with bubble sorting
   private void sortTeacher () {
      boolean sorted = false;
      Teacher swap;
      
      for (int upperBound = teaSize - 1; upperBound >= 1 && !sorted; upperBound--) {
         sorted = true;
         for (int i = 0; i < upperBound; i++) {
            if (teachers[i].compareToID(teachers[i+1]) > 0) {
               swap = teachers[i];
               teachers[i] = teachers[i+1];
               teachers[i+1] = swap;
               sorted = false;
            }
         }
      }
   }
   
   // Sorts courses with bubble sorting
   private void sortCourse () {
      boolean sorted = false;
      Course swap;
      
      for (int upperBound = couSize - 1; upperBound >= 1 && !sorted; upperBound--) {
         sorted = true;
         for (int i = 0; i < upperBound; i++) {
            if (courses[i].compareToName(courses[i+1]) > 0) {
               swap = courses[i];
               courses[i] = courses[i+1];
               courses[i+1] = swap;
               sorted = false;
            }
         }
      }
   }
   
   // Saves all files
   public void saveAll (boolean newSchool) {
      if (newSchool) saveSchool();
      saveCourses();
      saveStudents();
      saveTeachers();
   }
   
   // Saves a new school into the school file
   public void saveSchool () {
      int numSchool;
      String[] schools;
      
      try {
         BufferedReader in = new BufferedReader(new FileReader("_System Files\\schools.txt"));
         
         // Takes in previous school file data from file
         numSchool = Integer.parseInt(in.readLine());
         schools = new String[numSchool];
         
         for (int i = 0; i < numSchool; i++) {
            schools[i] = in.readLine();
         }
         
         in.close();
         
         // Writes previous data and new school data to file
         BufferedWriter out = new BufferedWriter(new FileWriter("_System Files\\schools.txt"));
         out.write("" + (numSchool+1));
         out.newLine();
         
         for (int i = 0; i < numSchool; i++) {
            out.write(schools[i]);
            out.newLine();
         }
         out.write(schoolName);
      
         out.close();
      } catch (IOException iox) {
         System.out.println("Error accessing file.");
      }
   }
   
   // Saves the courses into the corresponding file
   public void saveCourses () {
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter("_System Files\\" + schoolName + "Courses.txt"));
         out.write("" + couSize);
         for (int i = 0; i < couSize; i++) {
            out.newLine();
            out.write(courses[i].getCourseCode());
            out.newLine();
            out.write(courses[i].getDepartment());
            out.newLine();
            out.write("" + courses[i].getGrade());
         } 
         out.close();
      } catch (IOException iox) {
         System.out.println("Error accessing file.");
      }
   }
   
   // Saves students into the corresponding file
   public void saveStudents () {
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter("_System Files\\" + schoolName + "Students.txt"));
         out.write("" + stuSize);
         for (int i = 0; i < stuSize; i++) {
            out.newLine();
            out.write(students[i].getName());
            out.newLine();
            out.write("" + students[i].getGrade());
            out.newLine();
            out.write("" + students[i].getID());
            out.newLine();
            for (int j = 0 ; j < PERTT ; j++) {
               out.write(students[i].getOriginalPreferCourse()[j] + " ");
            }
         } 
         out.close();
      } catch (IOException iox) {
         System.out.println("Error accessing file.");
      }
   }
   
   // Saves teachers into the corresponding file
   public void saveTeachers () {
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter("_System Files\\" + schoolName + "Teachers.txt"));
         out.write("" + teaSize);
         for (int i = 0; i < teaSize; i++) {
            out.newLine();
            out.write(teachers[i].getName());
            out.newLine();
            out.write("" + teachers[i].getID());
            out.newLine();
            out.write("" + teachers[i].getDept());
         } 
         out.close();
      } catch (IOException iox) {
         System.out.println("Error accessing file.");
      }
   }
   
   // Outputs information about the school
   // Returns String - contains formatted information about the school
   public String toString () {
      String output = "";
      output += "Name of School: " + schoolName + "\n";
      output += "Number of students: " + stuSize + "\n";
      output += "Number of teachers: " + teaSize + "\n";
      output += "Number of courses: " + couSize;
      return output;
   }
   

   // Removes a course from the course database
	// @param {String} code - the course code of the course to be deleted
	// Returns boolean - indicating if the deleting was successful
   public boolean removeCourse (String code) {
      int index = findCourse(code);
      
      if (index == -1) {
         return false;
      } else {
         //shifts all elements of an array down one
         for (int j = index; j < couSize - 1; j++) {
            courses[j] = courses[j+1];
         }
         //updates the number of courses in the array
         couSize--;
         return true;
      }
   }
     
     
   // Removes a student from the database
   // @param {int} ID - the ID of the student to be removed
   // Returns boolean - indicating if the removal was successful
   public boolean removeStudent (int ID) {
      int i = findStudent(ID);
      
      if (i == -1) 
         return false;
      else {
         // Shifts all elements of an array down one
         for (int j = i; j < stuSize-1; j++) {
            students[j] = students[j+1];
         }
         // students[stuSize] = null;
         // Updates the number of students in the array
         stuSize--;
         return true;
      }
   }
   
   // Removes a teacher from the teacher database
	// @param {int} ID - the ID of the teacher to be deleted
   // Returns boolean - indicating if the deletion was successful
   public boolean removeTeacher (int ID) {
      int i = findTeacher(ID);
      
      if (i == -1) 
         return false;
      else {
         // Shifts all elements of an array down one
         for (int j = i; i < teaSize - 1; j++) {
            teachers[j] = teachers[j+1];
         }
         //updates the number of teachers in the array
         teaSize--;
         return true;
      }
   }
   
   // Locates a classroom, if possible, with a matching course code and period
   // @param {int} period - period of the classroom to find
	// @param {String} targer - course code of the classroom to find
   // @param MasterTable mt - the all encompassing table of the database
   // Returns a classroom if found, returns null if not found;
   public Classroom findClass (int period, String target, MasterTable mt) {
      Classroom targetClassroom;
      Classroom[] coursesInPeriod = mt.getClasses(period);
      for (int i = 0 ; i < coursesInPeriod.length; i++) {
         targetClassroom = coursesInPeriod[i];
         if (targetClassroom.getClassCourseCode().equals(target) && !(targetClassroom.isFull())) {
            return targetClassroom;
         }
      }
      return null;
   }
   
   // Changes a student's course for another one, checking if the new course is available in the old timeslot. Assumes the creation of the mastertable
   // @param {int} ID - ID of the student to find
   // @param {String} oldCourse - the course to be removed frrom the student timetable
   // @param {String} newCourse - the course to be added in place of the old course
   // Return boolean - indicating if the change was successful
   public boolean changeSCourse (int ID, String oldCourse, String newCourse) {
      int period;
      Classroom newClass;
      Student targetStudent;
      
      //finds student and checks that the student exists within the database
      if (findStudent(ID) != -1) {
         targetStudent = students[findStudent(ID)];
      } else {
         return false;
      }
      
      //locates the period of the course on the Student's timetable
      period = targetStudent.findPeriod(oldCourse);
      
      //checks to see if the desired course exists on the mastertable 
      if (period != -1) {
         newClass = findClass(period, newCourse, finalMT);
         if (newClass == null) {
            return false;
         } else {
            targetStudent.changeCourse(period, newClass);
            return true;
         }
      }
      
      return false;
   }
   
   // Removes a course for a student
   // @param {int} ID - the ID of the student to find
   // @param {String} targetCode - the course code of the course to remove
   // Returns boolean - indicating if the removal was successful
   public boolean removeSCourse (int ID, String targetCode) {
      Student targetStudent;
   
      if (findStudent(ID) != -1) {
         targetStudent = students[findStudent(ID)];
      } else {
         return false;
      }
      return targetStudent.removeClassForPerson(targetCode);
   }
   
   // Adds a course for the student. Takes up slots starting from the first spare onwards, 
   // if the student has any more than one. A spare is marked by a 'null' on their timetable.
   // @param {int} ID - the ID of the student to find
   // @param {String} targetCode - the course code of the course to be added
   // Returns boolean - indicating if the change was successful
   public boolean addSCourse (int ID, String targetCode) {
      boolean[] spares;
      Student targetStudent;
      Classroom newClass;
      
      //checks that student exists
      if (findStudent(ID) != -1) {
         targetStudent = students[findStudent(ID)];
      } else {
         return false;
      }
      
      spares = (targetStudent.getTimetable()).findSpares();
      //searches for classes that match the placement of the student's spare
      for (int i = 0 ; i < PERTT ; i++) {
         if (spares[i]) {
            newClass = findClass(i, targetCode, finalMT);
            if (newClass != null) {
               if ((targetStudent.getTimetable()).addClass(newClass)) {
                  return true;
               }
            }
         }
      }
      return false;
   }
   
   public boolean checkReady () {
      if (stuSize > 0 && teaSize > 0 && couSize > 0) 
         return true;
      return false;
   }

      
//---------------------------------------------------------------------------------------------------

  	// Creates the final timetable and places students within
   public void generateTimetable () {
      generateMasterTable();
      placement();
   }
  
   // Generate a MasterTable with the highest fitness score
   private void generateMasterTable () {
      double totalScores = 0;
      MasterTable[] parents = new MasterTable[2];
      boolean foundPerfect = false; // boolean that keeps track of whether a perfect mastertable has been located
      
      preliminaryCheck();
      
      oldPop = constructPopulation();
      
      
      for (int j = 0 ; j < MAX_ITERATIONS && !foundPerfect; j++) {
         
         
         totalScores = 0;
        
         if (calculateFitness()) {
            foundPerfect = true;
         }
      
        
         for (int i = 0 ; i < POPULACE ; i++) {
            totalScores += oldPop[i].getFitnessLevel();  
         }     	
      
         for (int i = 0 ; i < POPULACE ; i++) {
            parents = selectParents(totalScores);
            newPop[i] = createChild(parents);
         }
      
         
         mutateChild();
         replacePopulation();
        
      
      }
   }

	// Initial read-in of all the students' course preferences. Determines how many of each course should be had.
   private void preliminaryCheck () {
      String[] preferCourse;
      int numWantedCourse, numClassesCreated;
      
    	// Monitoring which classes won't be used
      ArrayList<String> unusedClasses = new ArrayList<String>();
      boolean found = false;
    
      // Reading through all the student preferences, adding one onto each course's numWanted (number of people signed up)
      for (int i = 0 ; i < stuSize ; i++) {
         preferCourse = students[i].getPreferCourse();
        
         // Locating a corresponding course and updating the numWanted counter (the number of people signed up for it)
         for (int j = 0 ; j < PERTT ; j++) {
            found = false;
            for (int k = 0 ; k < couSize && !found ; k++) {
               if (courses[k].getCourseCode().equals(preferCourse[j])) {
                  courses[k].setNumWanted(courses[k].getNumWanted() + 1);
                  found = true;
               }
            }		
         }
      }
        
      // Going through all the courses, and making an appropriate number of classrooms.
      for (int i = 0 ; i < couSize ; i++) {
         numWantedCourse = courses[i].getNumWanted();
         numClassesCreated = 0;
         if (numWantedCourse >= Course.MINIMUM) {
            // One more class than the amount that can be completely filled to alleviate space. (eg. 92 students in a capacity of 30 will make 4 classes insted of 3)
            numClassesCreated = numWantedCourse / Course.CAPACITY + 1;
            if (claSize + numClassesCreated > MAXCLA) {
               numClassesCreated = MAXCLA - claSize;
            }
            // Adding onto the list of unused classes if the number does not meet the minimum requirement
         } else {
            unusedClasses.add(courses[i].getCourseCode());
         }
            // Creates new classroom objects in the aray
         for (int o = claSize ; o < claSize + numClassesCreated; o++) {
            classrooms[o] = new Classroom(o-claSize+1, courses[i]);
         }
            // Updating the number of classrooms in the database
         claSize += numClassesCreated;
          	// Updating the number of classes in that course object 
         courses[i].setNumClasses(courses[i].getNumClasses() + numClassesCreated);
      }
          
        
      // For all students who selected a course that didn't end up getting created, their preferences need to be changed to spare. Otherwise there would never be a possibility where they fit in
      for (int i = 0 ; i < stuSize ; i++) {
         preferCourse = students[i].getPreferCourse();
         for (int j = 0 ; j < PERTT ; j++) {
            found = false;
            for (int k = 0 ; k < unusedClasses.size() && !found; k++) {
               if (preferCourse[j] != null) {
                  if (preferCourse[j].equals(unusedClasses.get(k))) {
                     students[i].switchPreferred(preferCourse[j], null);
                     found = true;
                  }
               }
            }
         }
      }
   }


	// The initial population constructor. Loops through the recursive function a number of times to create a completely random population of mastertables
   // Returns MasterTable[] - an array of randomly generated MasterTables to undergo genetic algorithm
   private MasterTable[] constructPopulation () {
      MasterTable[] pop = new MasterTable[POPULACE];
      for (int i = 0 ; i < POPULACE ; i++) {
         pop[i] = createRandomMT();
      }
      return pop;
   }

	// Wrapper function to create mastertables
   // Returns MasterTable - an empty table that will have classrooms randomly placed within
   private MasterTable createRandomMT () {
      MasterTable mt = new MasterTable();
      Classroom[] classDuplicate = new Classroom[claSize];
      for (int i = 0 ; i < claSize ; i++) {
         classDuplicate[i] = classrooms[i];
      }
      
      // The "1" means that the first classroom will be put into the first period of the timetable
      return createRandomMT(mt, classDuplicate, 1);
   }

	// Recursive function that randomly creates a sample timetable given the list of remaining classrooms as well as the period they are to go into (1->2->3->4->1...)
   // @param {Mastertable} mt - Current MasterTable to add classrooms
   // @param {Classroom[]} classDuplicate - Remaining classrooms left to be added to MasterTable
   // @param {int} insertionPeriod - Current period to insert classroom in
   // Returns MasterTable - MasterTable to continue adding classrooms to
   private MasterTable createRandomMT (MasterTable mt, Classroom[] classDuplicate, int insertionPeriod) {
      int randIndex;
      int length = classDuplicate.length;
        
      // Returns the mastertable if there are no more classes to copy over
      if (length == 0) {
         return mt;
      } else {
         // Randomly selecting a class to be put into the period
         randIndex = (int) (Math.random() * length);
         mt.addClass(insertionPeriod,classDuplicate[randIndex]);
         // Modulo ensures that the insertion period will never exceed 4.
         //The random classroom added is removed when recursion is called
         return createRandomMT(mt, removeClassroomElement(classDuplicate, randIndex), (insertionPeriod+1) % PERTT); 
      }
   }
			
	// A method that removes a specified classroom for recursion.
   // @param {Classroom[]} oldList - Old Classroom[] that needs a classroom removed
   // @param {int} target - Index of Classroom in array to remove
   // Returns Classroom[] - Updated Classroom[] without the specified classroom
   private Classroom[] removeClassroomElement (Classroom[] oldList, int target) {
      Classroom[] newList = new Classroom[oldList.length-1];
      for (int i = 0 ; i < target ; i++) {
         newList[i] = oldList[i];
      }
      for (int i = target ; i < newList.length ; i++) {
         newList[i] = oldList[i+1];
      }
      return newList;
   }
    
    
  	// Calculating fitness only needs whether or not a person can fit
  	// The final placement tries to make compensations.
   // @param {Mastertable} mt - Current MasterTable to check best fit for Student
   // @param {Student} stu - Student to match courses into the MasterTable
   // @param {boolean} returnBest - Best order for courses that fits in MasterTable
   // Returns String[] - The order of their preferred courses that would fit into the Mastertable
   private String[] findPermutationFit (MasterTable mt, Student stu, boolean returnBest) {
      boolean canFit = false;
      int numFits = 0;
      String[] copyPref = stu.getPreferCourse();
      String placeholder;
      int numPermutations = 24;           // 24 is the factorial of 4 and the number of possible permutations.
      String[] bestPermut = copyPref;     // the best possible permuation
      int maxPermut = 0;                  // the highest number of permutations associated with bestPermut
        
      // Swapping consecutive elements in array
      for (int i = 0 ; i < numPermutations/2 && !canFit ; i++) {
         numFits = 0;
         canFit = true;
         placeholder = copyPref[i % PERTT];
         copyPref[i%PERTT] = copyPref[(i+1)%PERTT];
         copyPref[(i+1)%PERTT] = placeholder;
          		
         for (int o = 0 ; o < PERTT; o++) {
            // If the student has a spare or has a class that exists on the current table, then it will count
            if ((copyPref[o] == null || findClass(o, copyPref[o], mt) != null) && (o == 0 || canFit)) {
               numFits++;
            } else {
               canFit = false;
            }
         }
          		
         if (canFit) {
            bestPermut = copyPref;
            return bestPermut;
         } else if (numFits > maxPermut) {
            maxPermut = numFits;
            bestPermut = copyPref;
         }
      }
        
      // Reversing the array
      for (int k = 0; k < copyPref.length / 2 && !canFit; k++) {
         placeholder = copyPref[k];
         copyPref[k] = copyPref[copyPref.length - k - 1];
         copyPref[copyPref.length - k - 1] = placeholder;
              
         for (int o = 0 ; o < PERTT; o++) {
            if (findClass(o, copyPref[o], oldPop[o]) != null&& (o == 0 || canFit)) {
               canFit = true;
            } else {
               canFit = false;
            }
         }
         if (canFit) {
            bestPermut = copyPref;
            return bestPermut;
         } else if (numFits > maxPermut) {
            maxPermut = numFits;
            bestPermut = copyPref;
         }
      }
      
      // Permuting the array after 
      for (int i = 0 ; i < numPermutations && !canFit; i++) {
         placeholder = copyPref[i % PERTT];
         copyPref[i%PERTT] = copyPref[(i+1)%PERTT];
         copyPref[(i+1)%PERTT] = placeholder;
              
         for (int o = 0 ; o < PERTT; o++) {
            if (findClass(o, copyPref[o], oldPop[i]) != null && (o == 0 || canFit)) {
               canFit = true;
            } else {
               canFit = false;
            }
         }
         if (canFit) {
            bestPermut = copyPref;
            return bestPermut;
         } else if (numFits > maxPermut) {
            maxPermut = numFits;
            bestPermut = copyPref;
         }
      }
        
      // At this point, no possible perfect permuation exists.
      if (returnBest) {
         for (int i = 0 ; i < PERTT ; i++) {
            // Figuring out which period in the imperfect permutation doesn't have a corresponding period, and changing it into a spare.
            if (findClass(i, bestPermut[i], mt) == null) {
               bestPermut[i] = null;
            }
         }
         return bestPermut;
      } else {
         return null;
      }
   }

   // Method that calculates the fitness of each member of the population. The more students a table can accomodate, the higher its score
   // Returns boolean - if perfect timetable has been found
   private boolean calculateFitness () {        
      // Looping through every element of the mastertable array
      for (int i = 0 ; i < POPULACE ; i++) {
        
         // Looping through everystudent
         for (int j = 0 ; j < stuSize ; j++) {
            
            // Calling a method to see if a student in the array could possibly fit into the timetable
            if (findPermutationFit(oldPop[i], students[j], false) != null) {
               oldPop[i].addFitness();
            }
         
         }
         // Setting fitness as less than or equal to one for an easy comparison
         oldPop[i].setFitnessLevel(oldPop[i].getFitnessLevel() / stuSize);
        
         if (finalMT == null) {
            finalMT = oldPop[i];
         } else if (oldPop[i].getFitnessLevel() > finalMT.getFitnessLevel()) {
            System.out.printf("Current Highest MasterTable Preliminary Score: %.2f", (oldPop[i].getFitnessLevel()*100));
            System.out.println();
            finalMT = oldPop[i];
         }
      }
        
      // Checking if the current best one has a perfect score
      if (finalMT.getFitnessLevel() == 1) {
         return true;
      }
      return false;
   }

	// Selects two parents to make a child out of
   private MasterTable[] selectParents (double totalScores) {
      MasterTable[] parents = new MasterTable[2];
      boolean found = false;
      double randNum = Math.random() * totalScores; 
             
      // This randomly selects a parent, and in doing so takes into account the "weight" of their fitness 
      // (i.e. having a higher fitness offers a greater chance at being selected)
      for (int i = 0 ; i < POPULACE && !found; i++) {
         randNum -= oldPop[i].getFitnessLevel();
         if (randNum <= 0) {
            parents[0] = oldPop[i];
         }
      }
        
      found = false;
      randNum = Math.random() * totalScores;
        
      for (int i = 0 ; i < POPULACE && !found; i++) {
         randNum -= oldPop[i].getFitnessLevel();
         if (randNum <= 0) {
            parents[1] = oldPop[i];
            found = true;
         }
      }
      return parents;
   }
			
  	// Given two parents, creates a "child" mastertable object
   // @param {Mastertable[]} parents - Two parent MasterTables to create an even fitter Child MasterTable
   // Returns MasterTable - Child MasterTable created from parents
   private MasterTable createChild (MasterTable[] parents) {
      MasterTable child = new MasterTable();
      int periodPlacement;
      boolean found;
      // System.out.println(parents[0]);
        
      // Half of the existing classrooms of the child will go in the same place as their first parent
      for (int i = 0 ; i < claSize / 2 ; i++) {
         found = false;
      // System.out.println(classrooms[i].getClassCourseCode());
      
         for (int k = 0 ; k < PERTT && !found; k++) {
            for (int j = 0 ; j < parents[0].getTable().get(k).size() && !found; j++) {
               if ((parents[0].getTable().get(k).get(j).getClassCourseCode().equals(classrooms[i].getClassCourseCode())) 
               && (parents[0].getTable().get(k).get(j).getSection() == (classrooms[i].getSection()))) {
                  child.addClass(k, classrooms[i]);
                  found = true;
               }
            }
         }
      }
        
   
   	// The other half of the classrooms for the child will go according to their second parent
      for (int i = claSize / 2 ; i < claSize ; i++) {
         found = false;
         for (int k = 0 ; k < PERTT && !found; k++) {
            for (int j = 0 ; j < parents[1].getTable().get(k).size() && !found; j++) {
               if ((parents[1].getTable().get(k).get(j).getClassCourseCode().equals(classrooms[i].getClassCourseCode())) 
               && (parents[1].getTable().get(k).get(j).getSection() == (classrooms[i].getSection()))) {                  
                  child.addClass(k, classrooms[i]);
                  found = true;
               }
            }
         }
      }
   
      return child;
   }
      
			
	// Changes the child to ensure that there is variety
   private void mutateChild () {
      for (int i = 0; i < POPULACE; i++) {
         for (int j = 0; j < PERTT; j++) {
            for (int k = 0; k < newPop[i].getTable().get(j).size(); k++) {
               int rand = (int)(Math.random() * (1 / MUTATION_RATE));
               if (rand == 0) {
                  int randPeriod = -1;
                  do {
                     randPeriod = (int)(Math.random() * PERTT);
                  } while (randPeriod == k);
                  int numClass = newPop[i].getTable().get(randPeriod).size();
                  int randClass = (int)(Math.random() * numClass);
                  
                  if (newPop[i].getTable().get(j).size() > 0 && newPop[i].getTable().get(randPeriod).size() > 0) {
                     Classroom dummy = newPop[i].getTable().get(j).get(k);
                     Classroom dummy2 = newPop[i].getTable().get(randPeriod).get(randClass);
                  
                     // Switch classes
                     newPop[i].addClass(j,dummy2);
                     newPop[i].addClass(randPeriod,dummy);
                     newPop[i].remove(j,dummy);
                     newPop[i].remove(randPeriod,dummy2);
                  }
               }
            }
         }
      }
   }

	// Changes the new population into the old (children become the parent)
   private void replacePopulation () {
      for (int i = 0 ; i < 100 ; i++) {
         oldPop[i] = newPop[i];
      }
   }

	// Finds the number of ways a student can fit into the finalized timetable.
   private void countNumFits () {
      String[] copyPref = new String[4];
      int numPermutations = 24; // 4 factorial
      boolean canFit = false;
      String placeholder;
        
      // Looping through every student
      for (int i = 0 ; i < stuSize ; i++) {
         for (int j = 0 ; j < PERTT ; j++) {
            copyPref[j] = students[i].getPreferCourse()[j];
         }
            
         for (int k = 0 ; k < numPermutations/2 && !canFit; k++) {
            placeholder = copyPref[i % PERTT];
            copyPref[i%PERTT] = copyPref[(i+1)%PERTT];
            copyPref[(i+1)%PERTT] = placeholder;
              
            // Checking if this permutation of courses can possibly fit into the mastertable
            for (int o = 0 ; o < PERTT; o++) {
               if (findClass(o, copyPref[o], finalMT) != null && (o == 0 || canFit)) {
                  canFit = true;
               } else {
                  canFit = false;
               }
               if (canFit) {
                  students[i].updateNumTBFits();
               }
            }
         }
                  
         // Reversing the array and doing the swapping again. This gets the other half of the permutations
         for (int k = 0; i < copyPref.length / 2 && !canFit; i++) {
            placeholder = copyPref[i];
            copyPref[i] = copyPref[copyPref.length - i - 1];
            copyPref[copyPref.length - i - 1] = placeholder;
              
            for (int o = 0 ; o < PERTT; o++) {
               if (findClass(o, copyPref[o], finalMT) != null&& (o == 0 || canFit)) {
                  students[i].updateNumTBFits();
               } 
            }
         }
      
         for (int k = 0 ; i < numPermutations/2 && !canFit; i++) {
            placeholder = copyPref[i % PERTT];
            copyPref[i%PERTT] = copyPref[(i+1)%PERTT];
            copyPref[(i+1)%PERTT] = placeholder;
              
            for (int o = 0 ; o < PERTT; o++) {
               if (findClass(o, copyPref[o], finalMT) != null && (o == 0 || canFit)) {
                  students[i].updateNumTBFits();
                
               } else {
                  canFit = false;
               }
               if (canFit) {
                  students[i].updateNumTBFits();
               }
            }
         }
      }
   }

   // Sorts the students list based on the number of ways they can fit into the timetable. Lowest to highest
   private void sortByFits () {	
      boolean sorted = false;
      Student swap;
    
      for (int upperBound = stuSize-1; upperBound >= 1 && !sorted; upperBound--) {
         sorted = true;
         for (int i = 0; i < upperBound; i++) {
            if (students[i].getNumTBFits() > students[i+1].getNumTBFits()) {
               swap = students[i];
               students[i] = students[i+1];
               students[i+1] = swap;
               sorted = false;
            }
         }
      }
   }
      
      
      
      
  	// Places students into the timetable, as well as final classroom allocations. Probably very messy
   // extra steps are taken into the final placement because the genetic algorithm only checks to see whether or not a student can fit or not,
   // but not actually the number of students in the class. That being the case, there is the possibility that all students are put into just one classroom
   // or that there is a classroom that has no people (which typically means an overflow elsewhere).
   private void placement () {
      //    System.out.println(finalMT.getFitnessLevel());
      //    System.out.println(finalMT);
      String a;
      String b;
      String[] order;               // the order by which students are placed.
      
      Classroom posClass;           // a possible classroom that can be modified (either deleted, added upon, given students, removed students)
      Waitlist classWaitlist;       // the waitlist corresponding to the posClass
      int numStudents;              // corresponding number of students in the posClass
      int count = 0;
      
      
      Classroom lastMatch ;         // the most previous classroom that has the desire course code
      boolean added;                // boolean for whether or not a course had been added. 
      int numClassesToBeMade;       // number of extra classes that should be created
      int numStudentsToBeAdded;     // number of students (from a waitlist) that should be added onto 
      int section = 0;
      int numListedClass;           // number of people waitlisted for a class
      int numStudentsToDelete;      // the number of students that need to be deleted should a class be too small. 
      
   
      // Sorting the students so that the ones that only have a few ways to fit into the timetable to first
      countNumFits();
      sortByFits();
           
      // System.out.println(students[50]);
      // Reorganizing and changing the classrooms array to have the same order
      claSize = 0;
      for (int i = 0 ; i < PERTT ; i++) {
         for (int j = 0 ; j <finalMT.getTable().get(i).size() ; j++) {
            classrooms[claSize] = finalMT.getTable().get(i).get(j);
            claSize++;
         }
      }
      
      // Assigning teachers into classes
      for (int i = 0 ; i < claSize ; i++) {
         classrooms[i].setTeacher(findTeacher(classrooms[i].getCourse().getDepartment()));
         for (int j = 0 ; j < couSize ; j++) {
            if (classrooms[i].getClassCourseCode().equals(courses[j].getCourseCode())) {
               courses[j].newClass(classrooms[i]);
            }
         }
      }
      	
      // Updates the period field for each of the classes
      for (int i = 0 ; i < PERTT ; i ++) {
         for (int o = 0 ; o < finalMT.getTable().get(i).size() ; o++) {
            finalMT.getTable().get(i).get(o).setPeriod(i);
            posClass = finalMT.getTable().get(i).get(o);
            posClass.setSection(section);
            section++;
         }
      }
      
      for (int i = 0 ; i < stuSize ; i++) {
        
         // Finding an order that is suitable for the student
         order = findPermutationFit(finalMT, students[i], true);
         // for (int j = 0 ; j < 4 && i == 50; j++) {
         //    System.out.println(order[j]);
         // }
         
         for (int j = 0 ; j < PERTT ; j++) {
            lastMatch = null;
            added = false;
            if (order[j] != null) { // not a spare
               for (int k = 0 ; k < finalMT.getTable().get(j).size() ; k++) {
                  posClass = finalMT.getTable().get(j).get(k);
                  
                  // Placing students into the classroom if the code matches and if they are not at full capacity
                  if(posClass.getClassCourseCode().equals(order[j]) && !posClass.isFull()) {
                  // System.out.println(posClass.getClassCourseCode());
                     students[i].addCourse(posClass, j);
                     posClass.addStudent(students[i]);
                     added = true;
                  } else if (posClass.getClassCourseCode().equals(order[j])) {
                     lastMatch = posClass;
                  }
               }
               if (!added && lastMatch != null) {
                  // This adds the student into the most recent matching course. If, at this point, there has been no successful additon, then all classrooms are full.
                  // This method autmoatically adds the student to the waitlist if the courses are full.
                  lastMatch.addToWaitlist(students[i]);
               }
            } else { // Their entered classroom is a spare/null
               // System.out.println("Spare");
               students[i].addSpare(j);
            }
          		
         }
      }
        
      // Running through all the classes to see if there are any with an excessive waitlist or too little people. 
      for (int i = 0 ; i < PERTT ; i++) {
         for (int j = 0 ; j < finalMT.getTable().get(i).size() ; j++) {
            posClass = finalMT.getTable().get(i).get(j);
            classWaitlist = posClass.getWaitlist();
            numListedClass = classWaitlist.getNumWaiting();
            
            // If there are enough people on the waitlist to create a new class
            if (numListedClass >= Classroom.MINIMUM) {
               // Determining the number of classrooms that should be created
               numClassesToBeMade = numListedClass / Classroom.MINIMUM;
              
               for (int k = 0 ; k < numClassesToBeMade ; k++) {
                  // Creating a certain number of classrooms based on the previously determined numter that should be made
                  classrooms[claSize] = new Classroom(section, posClass.getCourse());
                  finalMT.addClass(i, classrooms[claSize]);
                  if (numListedClass <= Classroom.CAPACITY) {
                     numStudentsToBeAdded = numListedClass;
                  } else {
                     numStudentsToBeAdded = 30;
                  }
                
                  for (int l = 0 ; l < numStudentsToBeAdded ; l++) {
                     // Moving students from a waitlist onto that class
                     classrooms[claSize].addStudent(posClass.getWaitlist().getStudent(0));
                     posClass.getWaitlist().removeStudent(0);
                  }
                  claSize++;
               }
            }
                
                
         }
      }
   }     

   // Searches for the index of a classroom
   // @param {Classroom} target - Classroom to find index for
   // Returns int - index of requested Classroom
   private int classIndex(Classroom target) {
      for (int i = 0 ; i < claSize ; i++) {
         if (classrooms[i] == target) {
            return i;
         }
      }
      return -1;
   }
}
