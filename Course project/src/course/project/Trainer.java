package course.project;

import java.util.Map;

/**
 * Created by KOT on 11.02.2016.
 */
public class Trainer implements Person {
    private final int TrainerID;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Map<Integer, Course> courseTrainer;

    public Trainer(int TrainerID, String firstName, String lastName, String userName, String password,
                   Map<Integer, Course> courseTrainer) {
        this.TrainerID = TrainerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.courseTrainer = courseTrainer;
    }

    @Override
    public String toString() {
        return firstName +" " + lastName;
    }
    @Override
    public String information() {
        String courseTraine = courseTrainer.values().toString().replace("[","").replace("]","");
        return (char)27+"[34m" + "Trainer ID:\t\t\t" + (char)27+"[0m" + TrainerID +
                (char)27+"[34m" + "\nTrainer first name:\t" + (char)27+"[0m" + firstName +
                (char)27+"[34m" + "\nTrainer last name:\t" + (char)27+"[0m" + lastName +
                (char)27+"[34m" + "\nCourses:\t\t\t" + (char)27+"[0m" + courseTraine;
    }
    @Override
    public String writeInfo() {
        String retu = "Trainer ID:\t\t\t" + TrainerID +
                "\nTrainer first name:\t" + firstName +
                "\nTrainer last name:\t" + lastName +
                "\nTrainer user name:\t" + userName +
                "\nTrainer password:\t" + password +
                "\nCourses:\t\t\t" + courseTrainer.values();
        retu = retu.replace("[","");
        retu = retu.replace("]","");
        return retu;
    }
    @Override
    public Map<Integer, Course> getCourse() {
        return courseTrainer;
    }
    @Override
    public void addCourse(Course cou) {
        courseTrainer.put(cou.getID(), cou);
    }
    @Override
    public int getID() {
        return TrainerID;
    }
    @Override
    public void removeCourse(Course course) {
        courseTrainer.remove(course.getID());
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getPassword() {
        return password;
    }
    public String getUserName() {
        return userName;
    }
}
