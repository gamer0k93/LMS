package course.project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KOT on 11.02.2016.
 */
public class Student implements Person{
    private final int studentID;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Map<Integer, Course> courseStudent;
    private Map<String, Map<String, Integer>> studentTasks;

    public Student(int studentID, String firstName, String lastName, String userName, String password,
                   Map<Integer, Course> courseStudent, Map<String, Map<String, Integer>> studentTasks) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.courseStudent = courseStudent;
        this.studentTasks = studentTasks;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
    @Override
    public String information() {
        String courseStuden = courseStudent.values().toString().replace("[","").replace("]","");
        return (char)27+"[34m" + "Student ID:\t\t\t" + (char)27+"[0m" + studentID +
                (char)27+"[34m" + "\nStudent first name:\t" + (char)27+"[0m" + firstName +
                (char)27+"[34m" + "\nStudent last name:\t" + (char)27+"[0m" + lastName +
                (char)27+"[34m" + "\nCourses:\t\t\t" + (char)27+"[0m" + courseStuden;
    }
    @Override
    public String writeInfo(){
        StringBuilder studentTasksWrite = new StringBuilder();
        Course[] arrayCourseStudent = courseStudent.values().toArray(new Course[courseStudent.size()]);
        for (Course anArrayCourseStudent : arrayCourseStudent) {
            if (studentTasks.get(anArrayCourseStudent.getName()) != null) {
                studentTasksWrite.append(anArrayCourseStudent.getName()).append(" :: ")
                        .append(studentTasks.get(anArrayCourseStudent.getName())).append(";  ");
            }
        }
        return "Student ID:\t\t\t" + studentID +
                "\nStudent first name:\t" + firstName +
                "\nStudent last name:\t" + lastName +
                "\nStudent user name:\t" + userName +
                "\nStudent password:\t" + password +
                "\nCourses:\t\t\t" + courseStudent.values() +
                "\nStudent tasks:\t\t" + studentTasksWrite;
    }
    @Override
    public Map<Integer, Course> getCourse() {
        return courseStudent;
    }
    @Override
    public void addCourse(Course course) {
        courseStudent.put(course.getID(), course);

        Course.Task[] task = course.getStudentAssessment().toArray(
                new Course.Task[course.getStudentAssessment().size()]);
        for (Course.Task element : task) {
            addStudentTasks(course.getName(), element.getName(), -1);
        }
    }
    @Override
    public int getID() {
        return studentID;
    }
    @Override
    public void removeCourse(Course course) {
        courseStudent.remove(course.getID());
        studentTasks.remove(course.getName());
    }

    public void addStudentTasks(String nameCourse, String nameTask, Integer assessment) {
        Map<String, Integer> myMap = new HashMap<>();
        myMap.put(nameTask, assessment);
        if(studentTasks.get(nameCourse) == null)studentTasks.put(nameCourse, myMap);
        else studentTasks.get(nameCourse).put(nameTask, assessment);
    }

    public String getPassword() {
        return password;
    }
    public String getUserName() {
        return userName;
    }
}
