package course.project;

import java.io.Serializable;
import java.util.*;

/**
 * Created by KOT on 11.02.2016.
 */
public class Course implements MyClass {
    private int courseID;
    private String name;
    private String info;
    private Map<Integer,Student> studentsOnCourse;
    private Map<Integer,Trainer> trainersOnCourse;
    private GregorianCalendar startDate;
    private GregorianCalendar finishDate;
    private Set<String> trainingDays;
    private Set<Task> studentAssessment;

    public Course(String name) {this.name = name;}
    public Course(int courseID, String name, String info, Map<Integer,Student> studentsOnCourse,
                  Map<Integer,Trainer> trainersOnCourse, GregorianCalendar startDate, GregorianCalendar finishDate,
                  Set<String> trainingDays, Set<Task> studentAssessment) {
        this.courseID = courseID;
        this.name = name;
        this.info = info;
        this.studentsOnCourse = studentsOnCourse;
        this.trainersOnCourse = trainersOnCourse;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.trainingDays = trainingDays;
        this.studentAssessment = studentAssessment;
    }

    public Map<Integer,Student> getStudentsOnCourse() {
        return studentsOnCourse;
    }

    public String getName() {
        return name;
    }
    public Set<Task> getStudentAssessment() {
        return studentAssessment;
    }
    public Map<Integer, Trainer> getTrainersOnCourse() {
        return trainersOnCourse;
    }

    public void addStudentAssessment(Task task) {
        if(studentAssessment.contains(task)) studentAssessment.remove(task);
        studentAssessment.add(task);
    }
    public void addDay(String dayString){
        trainingDays.add(dayString);
    }

    public void addStudentsOnCourse(Student stu) {
        studentsOnCourse.put(stu.getID(), stu);
        Task[] tast = studentAssessment.toArray(new Task[studentAssessment.size()]);
        for (Task elem : tast) {
            Map<Student, Integer> myMap = elem.getEvaluation();
            myMap.put(stu, -1);
            elem.setEvaluation(myMap);
        }
    }
    public void removeStudentsOnCourse(Student stu) {
        studentsOnCourse.remove(stu.getID());
        Task[] tast = studentAssessment.toArray(new Task[studentAssessment.size()]);
        for (Task elem : tast) {
            Map<Student, Integer> myMap = elem.getEvaluation();
            myMap.remove(stu);
            elem.setEvaluation(myMap);
        }
    }

    public void addTrainerOnCourse(Trainer tra) {
        trainersOnCourse.put(tra.getID(), tra);
    }
    public void removeTrainerOnCourse(Trainer tra) {
        studentsOnCourse.remove(tra.getID());
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public String information() {
        String trainingDay = trainingDays.toString().replace("]", "").replace("[", "");
        return (char)27+"[34m" + "Course ID:\t\t\t" + (char)27+"[0m" +  courseID +
                        (char)27+"[34m" + "\nCourse name:\t\t" + (char)27+"[0m" + name +
                        (char)27+"[34m" + "\nCourse description: " + (char)27+"[0m" + info +
                        (char)27+"[34m" + "\nStart data:\t\t\t" + (char)27+"[0m" + startDate.get(Calendar.DAY_OF_MONTH) +"."+ startDate.get(Calendar.MONTH) +"."+ startDate.get(Calendar.YEAR)  +
                        (char)27+"[34m" + "\nFinish data:\t\t" + (char)27+"[0m" + finishDate.get(Calendar.DAY_OF_MONTH) +"."+ finishDate.get(Calendar.MONTH) +"."+ finishDate.get(Calendar.YEAR)  +
                        (char)27+"[34m" + "\nDays:\t\t\t\t" + (char)27+"[0m" + trainingDay;
    }
    @Override
    public String writeInfo(){
        return  "Course ID:\t\t\t" + courseID +
                "\nName:\t\t\t\t" + name +
                "\nInfo:\t\t\t\t" + info  +
                "\nStudents:\t\t\t" + studentsOnCourse.values()  +
                "\nTrainers:\t\t\t" + trainersOnCourse.values()  +
                "\nStart data:\t\t\t" + startDate.get(Calendar.DAY_OF_MONTH) +"."+ startDate.get(Calendar.MONTH) +"."+ startDate.get(Calendar.YEAR)  +
                "\nFinish data:\t\t" + finishDate.get(Calendar.DAY_OF_MONTH) +"."+ finishDate.get(Calendar.MONTH) +"."+ finishDate.get(Calendar.YEAR)  +
                "\nTraining days:\t\t" + trainingDays  +
                "\nStudent assessment: " + studentAssessment;
    }
    @Override
    public int getID() {
        return courseID;
    }

    public class Task implements Serializable{
        private String name;
        private String info;
        private Map<Student, Integer> evaluation;

        public Task(String name, String info) {
            this.name = name;
            this.info = info;
            this.evaluation = new HashMap<>();

            Student[] arrStudent = studentsOnCourse.values().toArray(new Student[studentsOnCourse.size()]);
            for (Student anArrStudent : arrStudent) {
                evaluation.put(anArrStudent, -1);
            }
        }
        public Task(String name, String info, Map<Student, Integer> evaluation) {
            this.name = name;
            this.info = info;
            this.evaluation = evaluation;
        }

        public Map<Student, Integer> getEvaluation() {
            return evaluation;
        }
        public void setEvaluation(Map<Student, Integer> evaluation) {
            this.evaluation = evaluation;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return  name + " :: " + info + " :: " + evaluation + ";";
        }

        public String showGradebook(){
            StringBuilder   sb1 = new StringBuilder(),
                            sb2 = new StringBuilder();

            Student[] arrStudent = studentsOnCourse.values().toArray(new Student[studentsOnCourse.size()]);
            Arrays.sort(arrStudent, new Comparator<Student>() {
                @Override
                public int compare(Student stu1, Student stu2) {
                    return stu1.toString().compareTo(stu2.toString());
                }
            });

            for (int i = 0; i < arrStudent.length; i++) {
                int number = arrStudent[i].toString().length();

                for (int j = 0; j < 24 - number; j++) sb2.append(" ");

                if(i%2 == 0) sb1.append(arrStudent[i]).append(sb2.toString()).append(evaluation.get(arrStudent[i])).append("\t\t\t\t");
                else sb1.append(arrStudent[i]).append(sb2.toString()).append(evaluation.get(arrStudent[i])).append("\n");
                sb2.delete(0,100);
            }
            sb1.append("\n\n");
            return  (char)27+"[34m" + "Name task: " + (char)27+"[0m" + name +
                    (char)27+"[34m" + "\nInfo task: " + (char)27+"[0m" + info +
                    (char)27+"[34m" + "\nStudent\t\t\t\tEvaluation\t\t\tStudent\t\t\t\tEvaluation\n" + (char)27+"[0m" +
                    sb1;
        }
        public String showGradebookPerStudent(Student student){
            return  (char)27+"[34m" +   "Name task:  \t\t" + (char)27+"[0m" + name +
                    (char)27+"[34m" + "\nInfo task:  \t\t" + (char)27+"[0m" + info +
                    (char)27+"[34m" + "\nEvaluation: \t\t" + (char)27+"[0m" + evaluation.get(student);
        }
    }
}
