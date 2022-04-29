# schedule-generator
ChronoApp (or schedule-generator) is a Java application that allows a user to keep track of students and course information. Most notably, once student information and course preferences are inputted, one can generate timetables for the next semester. Most public schools undergo this process annually in preparations for the coming school year.

## Installation & How to Use

All provided files should be put in a single directory. To start use, one should compile and run the file **ChronoApp.java**.

The interface is text-based (standard output), and the program will prompt the user to enter what they would like to do at each step. The user should use standard input to interact with the program. See the next section for more detailed examples.

## Examples of Use

What follows is a few examples of user input during use of the program.

**Example 1**: Creating new school
```
Welcome to Chrono++!
Option 1: Start a new school.
Option 2: Select an existing school.

Enter an option: 1
Enter the name of the school: GitHubHigh
```

**Example 2**: Adding a course
```
------------------------------------------
                Main Menu
School: GitHubHigh
------------------------------------------
1.  Add/Delete new courses
2.  Add/Delete new students
3.  Add/Delete new teachers
4.  Create timetables for semester
5.  Print timetables
6.  Search for person
7.  Search for course
8.  Print list of courses
9.  Print list of students
10. Print list of teachers
11. Print the classes running each period
12. Exit
------------------------------------------

Enter an option: 1

Adding/Deleting Courses
1. Add Courses
2. Delete Courses

Enter an option: 1
Now adding courses
1. Enter course information manually
2. Enter course information from file
3. Cancel and return to main menu

Enter an option: 1
Enter the course code: ICS4U
Enter the department: Computer Science
Enter the grade level: 12
Confirm --- Press ENTER to return to main menu.
```

**Example 3**: Generating timetables

* Note that there must be enough students, teachers, and courses to generate timetables. Once the generating is complete, the user can run option 5 (print timetables), and the timetables can be found in a new folder created in the main directory with the same name as the school.
```
------------------------------------------
                Main Menu
School: GitHubHigh
------------------------------------------
1.  Add/Delete new courses
2.  Add/Delete new students
3.  Add/Delete new teachers
4.  Create timetables for semester
5.  Print timetables
6.  Search for person
7.  Search for course
8.  Print list of courses
9.  Print list of students
10. Print list of teachers
11. Print the classes running each period
12. Exit
------------------------------------------

Enter an option: 4

Generating timetables. Please wait.
Generating complete: all students and teachers now have a suitable timetable.
```

## Acknowledgements
I would like to thank my peers Winston, Lucas, and Juliet, as their help in the development of this project was central to its completion. The files everyone was mainly responsible for are commented in the files, though we each had a hand in all parts of the project.
