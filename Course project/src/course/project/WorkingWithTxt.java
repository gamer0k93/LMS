package course.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by User on 22.02.2016.
 */
public class WorkingWithTxt {
    private Scanner scn;
    private int numberToIterate1 = 1,
                numberToIterate2 = 1;

    public void writeBook(Map<Integer, ? extends MyClass> myMap, String str) {
        int numberToIterate1 = 1;
        try (FileWriter writer = new FileWriter("src\\course\\project\\Resources\\Books\\" + str + "Book.txt", false)) {
            while (true) {
                if (myMap.get(numberToIterate1) != null) {
                    scn = new Scanner( stringer( myMap.get(numberToIterate1).writeInfo() ) );////////////////////
                    while (scn.hasNextLine()) {
                        writer.write("\n" + scn.nextLine());
                    }
                    writer.write("\n");
                    numberToIterate1++;
                } else break;
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private String stringer(String str) {
        String retu = str;
        retu = retu.replace("[", "");
        retu = retu.replace("]", "");
        retu = retu.replace("{", "");
        retu = retu.replace("}", "");
        retu = retu.replace("=", " ");
        retu = retu.replace("::", "=");
        return retu;
    }

    // \/ Readers \/
    public void reader() {
        Map<Integer, String> mapStudentsOnCourse = new HashMap<>();
        Map<Integer, String> mapTrainersOnCourse = new HashMap<>();
        Map<Integer, String> mapStudentAssessment = new HashMap<>();

        //readerCourseBook
        try {
            scn = new Scanner(new File("src\\course\\project\\Resources\\Books\\CourseBook.txt"));
            while (scn.hasNextLine()) {
                scn.next();
                scn.next();
                int id = Integer.valueOf(scn.nextLine().trim());
                scn.next();
                String corseName = scn.nextLine().trim();
                scn.next();
                String courseDescription = scn.nextLine().trim();
                scn.next();
                mapStudentsOnCourse.put(id, scn.nextLine().trim()); //
                scn.next();
                mapTrainersOnCourse.put(id, scn.nextLine().trim()); //
                scn.next();
                scn.next();
                String[] startStr = scn.nextLine().trim().replace(",", " ").replace("."," ").split(" ");
                GregorianCalendar start = new GregorianCalendar(
                        Integer.valueOf(startStr[2]),Integer.valueOf(startStr[1]),Integer.valueOf(startStr[0]));
                scn.next();
                scn.next();
                String[] endStr = scn.nextLine().trim().replace(","," ").replace("."," ").split(" ");
                GregorianCalendar end = new GregorianCalendar(
                        Integer.valueOf(endStr[2]),Integer.valueOf(endStr[1]),Integer.valueOf(endStr[0]));
                scn.next();
                scn.next();
                String days = scn.nextLine().trim();
                scn.next();
                scn.next();
                mapStudentAssessment.put(id, scn.nextLine().trim()); //

                new LearningManagementSystem().addNewCourse(id, corseName, courseDescription, start, end);/////
                new LearningManagementSystem().enterDays(id ,days);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        readerStudentBook();
        readerTrainerBook();
        readStudentsOnCourse(mapStudentsOnCourse);
        readTrainersOnCourse(mapTrainersOnCourse);
        readStudentAssessment(mapStudentAssessment);

    }
    private void readerStudentBook(){
        try {
            scn = new Scanner(new File("src\\course\\project\\Resources\\Books\\StudentBook.txt"));
            while (scn.hasNextLine()) {
                scn.next();
                scn.next();
                int id = scn.nextInt();
                scn.nextLine();
                scn.next();
                scn.next();
                scn.next();
                String firstName = scn.nextLine().trim();
                scn.next();
                scn.next();
                scn.next();
                String lastName = scn.nextLine().trim();
                scn.next();
                scn.next();
                scn.next();
                String userName = scn.nextLine().trim();
                scn.next();
                scn.next();
                String password = scn.nextLine().trim();
                scn.next();
                scn.nextLine(); //
                scn.next();
                scn.next();
                scn.nextLine(); //

                new LearningManagementSystem().addNewStudent(id, firstName, lastName, userName, password);////////////
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void readerTrainerBook(){
        try {
            scn = new Scanner(new File("src\\course\\project\\Resources\\Books\\TrainerBook.txt"));
            while (scn.hasNextLine()) {
                scn.next();
                scn.next();
                int id = scn.nextInt();
                scn.nextLine();
                scn.next();
                scn.next();
                scn.next();
                String firstName = scn.nextLine().trim();
                scn.next();
                scn.next();
                scn.next();
                String lastName = scn.nextLine().trim();
                scn.next();
                scn.next();
                scn.next();
                String userName = scn.nextLine().trim();
                scn.next();
                scn.next();
                String password = scn.nextLine().trim();
                scn.next();
                scn.nextLine();

                new LearningManagementSystem().addNewTrainer(id, firstName, lastName, userName, password);///////////
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void readStudentsOnCourse(Map<Integer, String> mapStudentsOnCourse) {
        for (int i = 1; i <= mapStudentsOnCourse.size(); ) {
            if (mapStudentsOnCourse.get(numberToIterate1) == null) numberToIterate1++;
            else {
                String[] arrCS = mapStudentsOnCourse.get(numberToIterate1)
                        .replace(", ", "  ")
                        .split("  ");
                for (int j = 1; j <= LearningManagementSystem.getAllStudent().size(); ) {
                    if (LearningManagementSystem.getAllStudent().get(numberToIterate2) == null) numberToIterate2++;
                    else {
                        for (String arrC : arrCS) {
                            if (arrC.equals(LearningManagementSystem.getAllStudent().get(numberToIterate2).toString())) {
                                LearningManagementSystem.getAllStudent().get(numberToIterate2).addCourse(LearningManagementSystem.getAllCourse().get(numberToIterate1));
                                LearningManagementSystem.getAllCourse().get(numberToIterate1).addStudentsOnCourse(LearningManagementSystem.getAllStudent().get(numberToIterate2));
                            }
                        }
                        numberToIterate2++;
                        j++;
                    }
                }
                numberToIterate2 = 1;
                numberToIterate1++;
                i++;
            }
        }
        numberToIterate1 = 1;
    }
    private void readTrainersOnCourse(Map<Integer, String> mapTrainersOnCourse) {
        for (int i = 1; i <= mapTrainersOnCourse.size(); ) {
            if (mapTrainersOnCourse.get(numberToIterate1) == null) numberToIterate1++;
            else {
                String[] arrCS = mapTrainersOnCourse.get(numberToIterate1)
                        .replace(", ", "  ")
                        .split("  ");
                for (int j = 1; j <= LearningManagementSystem.getAllTrainer().size(); ) {
                    if (LearningManagementSystem.getAllTrainer().get(numberToIterate2) == null) numberToIterate2++;
                    else {
                        for (String arrC : arrCS) {
                            if (arrC.equals(LearningManagementSystem.getAllTrainer().get(numberToIterate2).getName())) {
                                LearningManagementSystem.getAllTrainer().get(numberToIterate2)
                                        .addCourse(LearningManagementSystem.getAllCourse().get(numberToIterate1));
                                LearningManagementSystem.getAllCourse().get(numberToIterate1)
                                        .addTrainerOnCourse(LearningManagementSystem.getAllTrainer().get(numberToIterate2));
                            }
                        }
                        numberToIterate2++;
                        j++;
                    }
                }
                numberToIterate2 = 1;
                numberToIterate1++;
                i++;
            }
        }
        numberToIterate1 = 1;
    }
    private void readStudentAssessment(Map<Integer, String> mapStudentAssessment){
        for (int i = 1; i <= mapStudentAssessment.size(); ) {

            if (mapStudentAssessment.get(numberToIterate1) == null) numberToIterate1++;
            else if(mapStudentAssessment.get(numberToIterate1).equals("")){
                numberToIterate1++;
                i++;
            } else {
                String[] arrSA = mapStudentAssessment.get(numberToIterate1)
                        .replace(";,", "    ")
                        .replace(";", "     ")
                        .split("     ");

                for (String anArrSA : arrSA) {

                    if(! (anArrSA.equals("") || anArrSA.equals(" "))) {

                        String[] arrSAElement = anArrSA.replace("=", " ").split("   ");

                        Map<Student, Integer> mapStudent = new HashMap<>();

                        if (arrSAElement.length == 3 && !arrSAElement[2].equals(" ")) {

                            String[] arrStud = arrSAElement[2].replace(",", "  ").split("   ");
                            for (String anArrStud : arrStud) {
                                String[] arrStudElement = anArrStud.split(" ");
                                for (int l = 1; l <= LearningManagementSystem.getAllStudent().size(); ) {
                                    if (LearningManagementSystem.getAllStudent().get(numberToIterate2) != null) {
                                        if (LearningManagementSystem.getAllStudent().get(numberToIterate2).toString()
                                                .equals(arrStudElement[0] + " " + arrStudElement[1])) {
                                            mapStudent.put(LearningManagementSystem.getAllStudent()
                                                    .get(numberToIterate2), Integer.valueOf(arrStudElement[2]));

                                            LearningManagementSystem.getAllStudent().get(numberToIterate2).addStudentTasks(
                                                    LearningManagementSystem.getAllCourse().get(numberToIterate1)
                                                            .getName(), arrSAElement[0], Integer.valueOf(arrStudElement[2]));
                                        }
                                        l++;
                                    }
                                    numberToIterate2++;
                                }
                                numberToIterate2 = 1;
                            }
                        }

                        LearningManagementSystem.getAllCourse().get(numberToIterate1)
                                .addStudentAssessment(LearningManagementSystem.getAllCourse()
                                        .get(numberToIterate1).new Task(arrSAElement[0], arrSAElement[1], mapStudent));

                    }
                }
                numberToIterate1++;
                i++;
            }
        }
        numberToIterate1 = 1;
    }
}
