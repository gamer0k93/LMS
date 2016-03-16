package course.project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by KOT on 11.02.2016.
 */
public class LearningManagementSystem {
    private static Scanner scn;

    private static Map<Integer, Student> allStudent = new TreeMap<>();
    private static Map<Integer, Trainer> allTrainer = new TreeMap<>();
    private static Map<Integer, Course>  allCourse  = new TreeMap<>();

    public static Map<Integer, Course> getAllCourse() {
        return allCourse;
    }
    public static Map<Integer, Student> getAllStudent() {
        return allStudent;
    }
    public static Map<Integer, Trainer> getAllTrainer() {
        return allTrainer;
    }
    public static void setAllStudent(Map<Integer, Student> allStudent) {
        LearningManagementSystem.allStudent = allStudent;
    }
    public static void setAllTrainer(Map<Integer, Trainer> allTrainer) {
        LearningManagementSystem.allTrainer = allTrainer;
    }
    public static void setAllCourse (Map<Integer, Course> allCourse) {
        LearningManagementSystem.allCourse = allCourse;
    }

    public void startProgram() {
        new SerializationClass().serialization("Storage"); //read a serialization files
        new Consol().newConsole( logon() );
    }

    //  \/ Student && Trainer \/
    private void consolAddNewStudentOrTrainer(Map<Integer, ? extends Person> myMap, String capitalLetter, String smallLetter) {
        o:
        while (true) {
            int idCourse = printById(allCourse, "Enter ID course", "Course", true);

            if (studentsOnTheCourse12(idCourse)) break ;

            System.out.print(   "1. Add existing " + smallLetter + "\n" +
                                "2. Create " + smallLetter + "\n" +
                                "3. Go to the console\n" +
                                "\tPlease, enter the command: ");
            switch (scn.nextLine()) {
                case "1": {
                    showAllName(myMap, capitalLetter);                                               // show name
                    final int ID = printById(myMap, "Enter ID " + smallLetter, capitalLetter, true); //enter id

                    if (myMap.get(ID).getCourse().containsValue(allCourse.get(idCourse))) {          //checks whether there is a student(trainer) on the course
                        printError(myMap.get(ID).toString() + " already recorded on the course " +
                                allCourse.get(idCourse).getName());
                    } else {
                        myMap.get(ID).addCourse(allCourse.get(idCourse));                           //the addition of a

                        if(smallLetter.equals("student"))
                             allCourse.get(idCourse).addStudentsOnCourse(allStudent.get(ID));  // student on the course
                        else allCourse.get(idCourse).addTrainerOnCourse(allTrainer.get(ID));   // trainer on the course

                        System.out.println(myMap.get(ID).toString() + " add in the course of " +
                                allCourse.get(idCourse).getName() + ":");
                        System.out.println(myMap.get(ID).information() + "\n\n");
                    }
                    break;
                }
                case "2": {
                    System.out.println("\n\t-=  create " + smallLetter + " =-");
                    final int ID = nextID(allStudent);                                        // create ID

                    String firstName = enterString(capitalLetter + " first name:\t\t");
                    String lastName = enterString( capitalLetter + " last name:\t\t" );
                    String userName = enterString( capitalLetter + " user name:\t\t" );
                    String password = enterString( capitalLetter + " password:\t\t"  );

                    if(smallLetter.equals("student")) {
                        addNewStudent(ID, firstName, lastName, userName, password);       // addNewStudent
                        allCourse.get(idCourse).addStudentsOnCourse(allStudent.get(ID));  // student on the course
                    } else {
                        addNewTrainer(ID, firstName, lastName, userName, password);       // addNewTrainer
                        allCourse.get(idCourse).addTrainerOnCourse(allTrainer.get(ID));   // trainer on the course
                    }

                    myMap.get(ID).addCourse(allCourse.get(idCourse));                     // addCourse

                    System.out.println("\nNew " + smallLetter + " has been successfully created in the course of " +
                            allCourse.get(idCourse).getName() + ":");
                    System.out.println(myMap.get(ID).information() + "\n\n");
                    break;
                }

                case "3": break ;

                default: {
                    printError("Enter 1 to 3");
                    continue o;
                }
            }
            break;
        }
    }
    private void transferStudentOrTrainer(Map<Integer, ? extends Person> myMap, String smallLetter){
        int idCursStart = printById(allCourse,
                "Enter ID courses for which you want to transfer " + smallLetter, "Course", false); // enter id

        int idPerson;
        if(smallLetter.equals("student"))
            idPerson = printById(allCourse.get(idCursStart).getStudentsOnCourse(),
                    "Enter ID student which you want to transfer", "Student", true); //enter id student
        else idPerson = printById(allCourse.get(idCursStart).getTrainersOnCourse(),
                "Enter ID trainer which you want to transfer", "Trainer", true); //enter id trainer

        System.out.println("You choosed " + myMap.get(idPerson));
        int idCursEnd = printById(allCourse, "Enter ID courses on which you want to " + smallLetter, "Course", false); //enter id

        if(smallLetter.equals("student")) {
            allCourse.get(idCursStart).removeStudentsOnCourse(allStudent.get(idPerson)); //removeStudentsOnCourse
            allCourse.get(idCursEnd).addStudentsOnCourse(allStudent.get(idPerson));   //addStudentsOnCourse
        }else{
            allCourse.get(idCursStart).removeTrainerOnCourse(allTrainer.get(idPerson)); //removeTrainersOnCourse
            allCourse.get(idCursEnd).addTrainerOnCourse(allTrainer.get(idPerson));   //addTrainersOnCourse
        }

        myMap.get(idPerson).removeCourse(allCourse.get(idCursStart)); //removeCourseStudent
        myMap.get(idPerson).addCourse(allCourse.get(idCursEnd)); //addCourseStudent

        System.out.println(smallLetter + " successfully transferred");
    }
    public void addNewStudent(int id, String firstName, String lastName, String userName, String password) {
        allStudent.put(id, new Student(id, firstName, lastName, userName, password, new HashMap<Integer, Course>(),
                new HashMap<String, Map<String, Integer>>()));
    }
    public void addNewTrainer(int id, String firstName, String lastName,  String userName, String password) {
        allTrainer.put(id, new Trainer(id, firstName, lastName, userName, password, new HashMap<Integer, Course>()));
    }

    // \/ Course  \/
    private void consolAddNewCourse() {
        System.out.println("\n\n\t-=  create course =-");
        int id = nextID(allCourse);
        String corseName = enterCourseName();          //check whether there is a course with the same name
        String courseDescription = enterString("Course description:\t");  //add Course description
        GregorianCalendar start = enterData(   "Start date:\t\t\t"    );  //add Start date
        GregorianCalendar end = enterData(     "End date:\t\t\t"      );  //add End date
        addNewCourse(id, corseName, courseDescription, start, end);       //create course
        while (true){
            if(enterDays(id, enterString("Days:\t\t\t\t")))break;
        }
        System.out.println("\nNew course has been successfully created:\n" +
                            allCourse.get(id).information() + "\n\n");
    }
    public void addNewCourse(int id, String corseName, String courseDescription, GregorianCalendar start, GregorianCalendar end) {
        allCourse.put(id, new Course(id, corseName, courseDescription, new HashMap<Integer, Student>(),
                new HashMap<Integer, Trainer>(), start, end, new TreeSet<String>(), new HashSet<Course.Task>()));
    }
    private void showGradebook(){
        int idCurse = printById(allCourse, "Enter ID course gradebook who want to see", "Course", true);

        Course.Task[] arrayTask = allCourse.get(idCurse).getStudentAssessment().toArray(
                new Course.Task[allCourse.get(idCurse).getStudentAssessment().size()]);
        for (Course.Task anArrayTask : arrayTask) {
            System.out.println(anArrayTask.showGradebook());
        }
    }
    private void writeGradebook(){
        int idCurse = printById(allCourse, "Enter ID course gradebook who want to write", "Course", false);
        System.out.print("Enter the file name in which will be recorded gradebook: ");
        String strFile = scn.nextLine().trim();
        if(strFile.equals("") || strFile.equals(" ")) strFile = allCourse.get(idCurse).getName() + "Gradebook";
        Course.Task[] arrayTask = allCourse.get(idCurse).getStudentAssessment().toArray(
                new Course.Task[allCourse.get(idCurse).getStudentAssessment().size()]);

        try (FileWriter writer = new FileWriter("src\\course\\project\\Resources\\" + strFile + ".txt", false)) {

            scn = new Scanner(allCourse.get(idCurse).information());
            while (scn.hasNextLine()) {
                writer.write("\n" + scn.nextLine());
            }

            writer.write("\n");

            for (Course.Task anArrayTask : arrayTask) {
                scn = new Scanner(anArrayTask.showGradebook());
                while (scn.hasNextLine()) {
                    writer.write("\n" + scn.nextLine());
                }
            }

            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void creationTasks() {
        int idCourse = printById(allCourse, "Enter ID course", "Course", false);
        System.out.println("\t-=  create tasks =-");

        String tasksName;
        o:
        while (true) {
            System.out.print("Tasks name:\t\t\t");
            tasksName = scn.nextLine();
            // \/ check that this name is not the problem \/
            Course.Task[] arrayTask = allCourse.get(idCourse).getStudentAssessment().toArray(
                    new Course.Task[allCourse.get(idCourse).getStudentAssessment().size()]);
            for (Course.Task anArrayTask : arrayTask) {
                if (anArrayTask.getName().equals(tasksName)) {
                    printError("Task with the same name in this course already exists");
                    continue o;
                }
            }
            break;
        }
        System.out.print("Information:\t\t");
        String informationAboutTheJob = scn.nextLine();

        allCourse.get(idCourse).addStudentAssessment(allCourse.get(idCourse)
                .new Task(tasksName, informationAboutTheJob));

        // \/ add students to the task \/
        if(allCourse.get(idCourse).getStudentsOnCourse().size() > 0) {
            Student[] arrStudent = allCourse.get(idCourse).getStudentsOnCourse().values()
                    .toArray(new Student[allCourse.get(idCourse).getStudentsOnCourse().size()]);
            for (Student anArrStudent : arrStudent) {
                    anArrStudent.addStudentTasks(allCourse.get(idCourse).getName(), tasksName, -1);
            }
        }
    }

    // \/ Checks \/
    private boolean studentsOnTheCourse12(int idCourse){
        if (allCourse.get(idCourse).getStudentsOnCourse().size() >= 12) {
            printError("\nOn this course for 12 students select another course!");
            return true;
        } else return false;
    }
    private int nextID(Map map){
        int id = 1;
        while (true) {
            if (map.get(id) != null) id++;
            else return id;
        }
    }
    private MyClass logon(){
        System.out.println( "Welcome to the Learning Management System\n" +
                "Please enter your username and password to login");

        while (true) {
            scn = new Scanner(System.in);
            System.out.print("user name:\t\t");
            String userName = scn.nextLine();
            System.out.print("password:\t\t");
            String password = scn.nextLine();

            if(userName.equals("0k") && password.equals("456")){
                System.out.println("\n\n" + (char)27+"[34m" + "ADMINOOSHKA, i greet in the system" + (char)27+"[0m");
                return new Course("ADMINOOSHKA");    //log in as administrator
            }

            for (Student elem : allStudent.values().toArray(new Student[allStudent.size()]))  //entrance as a student
                if (elem.getUserName().equals(userName) && elem.getPassword().equals(password)) {
                    System.out.println("\n\n" + (char)27+"[34m" + elem.toString() + ", i greet in the system" + (char)27+"[0m");
                    return elem;
                }

            for (Trainer elem : allTrainer.values().toArray(new Trainer[allTrainer.size()]))  //input as a teacher
                if (elem.getUserName().equals(userName) && elem.getPassword().equals(password)){
                    System.out.println("\n\n" + (char)27+"[34m" + elem.toString() + ", i greet in the system" + (char)27+"[0m");
                    return elem;
                }

            printError( "Ssername or password you entered is incorrect\n");
            System.out.println( "1. Try again\n" +
                    "2. Go out" +
                    "\n\tPlease, enter the command: ");
            o:
            while (true) {
                switch (scn.nextLine()) {
                    case "1": break;
                    case "2": System.exit(0);
                    default: {
                        printError("Enter 1 to 2");
                        continue o;
                    }
                }
                break ;
            }
        }
    }
    private GregorianCalendar enterData(String informationToEnter){
        while (true) {
            try {
                System.out.print(informationToEnter);
                String strData = scn.nextLine().trim();
                String[] strArray = strData.replace(",", " ").replace("-", " ").replace(".", " ").replace("  ", " ").split(" ");
                if (strArray[0].length() == 4) return new GregorianCalendar(Integer.valueOf(strArray[0]),
                        Integer.valueOf(strArray[1]), Integer.valueOf(strArray[2]));
                else if (strArray[2].length() == 4) return new GregorianCalendar(Integer.valueOf(strArray[2]),
                        Integer.valueOf(strArray[1]), Integer.valueOf(strArray[0]));
            }catch (Exception e) {
                printError("Date entered, incorrectly enter again!");
            }
        }
    }
    private String enterString(String informationToEnter){
        while (true) {
            System.out.print(informationToEnter);
            String string = scn.nextLine().trim();
            if (string.equals(""))
                printError("You enter the field is empty, try again!");
            else return string;
        }
    }
    private String enterCourseName(){
        String corseName;
        while(true){
            boolean flag = true;
            corseName = enterString("Course name:\t\t");
            Course[] courseArray = allCourse.values().toArray( new Course[ allCourse.values().size()] );
            for(Course elem : courseArray){
                if(elem.getName().equals(corseName)) {
                    printError("Course of the same name exists");
                    flag = false;
                    break;
                }
            }
            if(flag) return corseName;
        }
    }
    public boolean enterDays(int id, String days){
        while (true) {
            days = days.replace(",", " ").replace(".", " ").replace("-", " ").replace("   ", " ").replace("  ", " ").replace("   ", " ");
            String[] arrDays = days.split(" ");
            for (String elem : arrDays) {
                switch (elem){
                case "1":case "MON":case "mon":case "MO":case "mo":case "Monday":case "monday":{
                    allCourse.get(id).addDay("MON");
                    break;
                }
                case "2":case "TUE":case "tue":case "TU":case "tu":case "Tuesday":case "tuesday":{
                    allCourse.get(id).addDay("TUE");
                    break;
                }
                case "3":case "WED":case "wed":case "WE":case "we":case "Wednesday":case "wednesday":{
                    allCourse.get(id).addDay("WED");
                    break;
                }
                case "4":case "THU":case "thu":case "TH":case "th":case "Thursday":case "thursday":{
                    allCourse.get(id).addDay("THU");
                    break;
                }
                case "5":case "FRI":case "fri":case "FR":case "fr":case "Friday":case "friday":{
                    allCourse.get(id).addDay("FRI");
                    break;
                }
                case "6":case "SAT":case "sat":case "SA":case "sa":case "Saturday":case "saturday":{
                    allCourse.get(id).addDay("SAT");
                    break;
                }
                case "7":case "SUN":case "sun":case "SU":case "su":case "Sunday":case "sunday":{
                    allCourse.get(id).addDay("SUN");
                    break;
                }
                default: printError("Such as the day of the week '" + elem + "' does not exist");
                return false;
                }
            }
            return true;
        }
    }

    // \/ Other \/
    private int printById(Map<Integer, ? extends MyClass> map, String message, String strName, boolean printinformation) {
        showAllName(map, strName);
        while (true) {
            try {
                System.out.print(message + ": ");
                final int ID = Integer.valueOf(scn.nextLine());

                if (map.get(ID) == null) {
                    printError(strName + " with id does not exist");
                } else {
                    if (printinformation) System.out.println("\n" + map.get(ID).information() + "\n");
                    return ID;
                }
            }catch (NumberFormatException e){
                printError("Input Error! Enter the number");
            }
        }
    }
    private boolean showAllName(Map map, String name){
        if(map.size() > 0) System.out.println((char)27+"[34m" + "\nID\t" + name + (char)27+"[0m");
        else {
            printError("Element exists");
            return false;
        }
        int numberToIterate1 = 1;
        for (int i = 0; i < map.size(); ) {
            if (map.get(numberToIterate1) != null)
            {
                System.out.println(numberToIterate1 + "  " + map.get(numberToIterate1).toString());
                i++;
            }
            numberToIterate1++;
        }
        return true;
    }
    private void printError(String str){
        System.out.println( (char)27+"[31m" + str + (char)27+"[0m" );
    }

    class Consol{
        private void oldConsole() {
            while (true) {
                System.out.print("\n1. Creating a course\n" +
                        "2. Display information about the course by its ID\n" +
                        "3. The conclusion of all courses title list\n" +
                        "4. Creation of a student within a particular course\n" +
                        "5. Transfer of students from one course to another\n" +
                        "6. Displays information about the student by ID\n" +
                        "7. Create a coach as part of a course\n" +
                        "8. Displaying information about a coach by its ID\n" +
                        "9. Creation of tasks within a particular course\n" +
                        "10. The conclusion of names of all the students on the course ID\n" +
                        "11. The conclusion of a course of academic journal\n" +
                        "12. Save the file in the gradebook\n" +
                        "13. Exit the program\n" +
                        "\tPlease, enter the command: ");
                scn = new Scanner(System.in);
                switch (scn.nextLine()) {
                    case "1": {
                        consolAddNewCourse();
                        break;
                    }
                    case "2": {
                        printById(allCourse, "Enter ID course", "Course", true);
                        break;
                    }
                    case "3": {
                        showAllName(allCourse, "Course");
                        break;
                    }
                    case "4": {
                        consolAddNewStudentOrTrainer(allStudent, "Student", "student");
                        break;
                    }
                    case "5": {
                        transferStudentOrTrainer(allStudent, "student");
                        break;
                    }
                    case "6": {
                        printById(allStudent, "Enter the student id that you want to see", "Student", true);
                        break;
                    }
                    case "7": {
                        consolAddNewStudentOrTrainer(allTrainer, "Trainer", "trainer");
                        break;
                    }
                    case "8": {
                        printById(allTrainer, "Enter the trainer id that you want to see", "Trainer", true);
                        break;
                    }
                    case "9": {
                        creationTasks();
                        break;
                    }
                    case "10": {
                        printById(allCourse.get(
                                printById(allCourse, "Enter ID course students who want to see", "Course", true))
                                .getStudentsOnCourse(),"Enter ID student", "Student", true);
                        break;
                    }
                    case "11": {
                        showGradebook();
                        break;
                    }
                    case "12": {
                        writeGradebook();
                        break;
                    }
                    case "13": {
                        System.exit(0);
                    }
                    case "456": {
                        new WorkingWithTxt().writeBook(allCourse,  "Course");
                        new WorkingWithTxt().writeBook(allStudent, "Student");
                        new WorkingWithTxt().writeBook(allTrainer, "Trainer");
                        break;
                    }
                    case "567": {
                    new SerializationClass().deserialization("Storage");
                    break;
                    }
                    default: {
                        printError("Enter 1 to 13");
                        break;
                    }
                }
            }
        }
        private void newConsole(MyClass user) {
            while (true) {
                try {
                    System.out.print("\tPlease, enter the command: ");
                    scn = new Scanner(System.in);
                    String[] arrayStr = scn.nextLine().trim().toLowerCase()  // \/ removal of the input string these elements \/
                            .replace("\t", " ").replace(",", "").replace(".", "").replace(" a ", " ").replace(" in ", " ")
                            .replace(" new ", " ").replace(" to ", " ").replace(" the ", " ").replace(" by ", " ").replace(" of ", " ")
                            .replace("     ", " ").replace("    ", " ").replace("   ", " ").replace("  ", " ").split(" ", 8);

                    String[] arrayCommand = new String[8];
                    for (int i = 0; i < arrayCommand.length; i++) {
                        if (i == 0)
                            for (String elem : arrayStr) {
                                arrayCommand[i] = elem;
                                i++;
                            }
                        arrayCommand[i] = "";
                    }

                    switch (arrayCommand[0]) {
                        case "add":case "append":case "create":case "build":case "form": {
                            if(user.getClass().getSimpleName().equals("Student"))
                                printError("A student can not be " + arrayCommand[0] + "ed, please contact your teacher");
                            else {
                                consoleMethodAdd(arrayCommand);
                            }
                            break;
                        }
                        case "print":case "type":case "publish":case "show":case "showing":case "demonstration": {
                            consoleMethodPrint(arrayCommand, user);
                            break;
                        }
                        case "transfer":case "delivery": {
                            if(user.getClass().getSimpleName().equals("Student"))
                                printError("A student can not be " + arrayCommand[0] + "ed, please contact your teacher");
                            else {
                                consoleMethodTransfer(arrayCommand);
                                user = redefinition(user);
                            }
                            break;
                        }
                        case "write":case "record": {
                            consoleMethodWrite( arrayCommand);
                            break;
                        }
                        case "save": {
                            new SerializationClass().deserialization("Storage");
                            System.out.println((char)27+"[34m" + "Preservation was successful" + (char)27+"[0m");
                            break;
                        }
                        case "read": {
                            new WorkingWithTxt().reader(); //read a book
                            user = redefinition(user);
                            System.out.println((char)27+"[34m" + "Read successfully" + (char)27+"[0m");
                            break;
                        }
                        case "help":{
                            System.out.println( consoleMethodHelp() );
                            break;
                        }
                        case "helo":{
                            consoleMethodAI(user);
                            break;
                        }
                        case "exit":case "output": {
                            System.out.println((char)27+"[34m" + user.toString() +
                                    ", thank you for having taken advantage of our system" + (char)27+"[0m");
                            System.exit(0);
                        }
                        default: {
                            printError( "Command is entered incorrectly!\n" +
                                        "Type \"help\" to see the full list of commands");
                            break;
                        }
                    }
                }catch (NumberFormatException e){
                    printError("Input Error! Enter the number");
                }catch (NullPointerException e){
                    printError("Recording with id does not exist");
                }
            }
        }

        private MyClass redefinition(MyClass user){
            switch (user.getClass().getSimpleName()) {
                case "Student": {
                    return allStudent.get( user.getID() );
                }
                case "Trainer": {
                    return allTrainer.get( user.getID() );
                }
                default: {
                    return user;
                }
            }
        }
        private void consoleMethodAdd(String[] arrayCommand){
            switch (arrayCommand[1]) {   //       AA      DDDDD     DDDDD
                case "course": {         //      AAAA     DD  DD    DD  DD
                    consolAddNewCourse();//     AA  AA    DD   DD   DD   DD
                    break;               //    AA AA AA   DD  DD    DD  DD
                }                        //   AA      AA  DDDDD     DDDDD
                case "student":case "undergrad": {
                    consolAddNewStudentOrTrainer(allStudent, "Student", "student");
                    break;
                }
                case "trainer":case "teacher":case "instructor": {
                    consolAddNewStudentOrTrainer(allTrainer, "Trainer", "trainer");
                    break;
                }
                case "task":case "job":case "tasks": {
                    creationTasks();
                    break;
                }
                default: {
                    printError("You can not add an " + arrayCommand[1] +
                            "\nYou may have had in mind the student or teacher or course?\n");
                    break;
                }
            }
        }
        private void consoleMethodPrint(String[] arrayCommand, MyClass user) {
            switch (arrayCommand[1]) {
                case "all": {
                    switch (arrayCommand[2]) {
                        case "": {
                            showAllName(allCourse, "Course");
                            showAllName(allStudent, "Student");
                            showAllName(allTrainer, "Trainer");
                            break;
                        }
                        case "course": {
                            showAllName(allCourse, "Course");
                            break;
                        }
                        case "student":
                        case "undergrad": {
                            switch (arrayCommand[3]) {
                                case "course": {
                                    switch (arrayCommand[4]) {
                                        case "id": {
                                            showAllName(allCourse.get(Integer.valueOf(arrayCommand[5])).getStudentsOnCourse(), "Student");
                                            break;
                                        }
                                        default: {
                                            showAllName(allCourse.get(Integer.valueOf(arrayCommand[4])).getStudentsOnCourse(), "Student");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    showAllName(allStudent, "Student");
                                    break;
                                }
                            }
                            break;
                        }
                        case "trainer":
                        case "teacher":
                        case "instructor": {
                            switch (arrayCommand[3]) {
                                case "course": {
                                    switch (arrayCommand[4]) {
                                        case "id": {
                                            showAllName(allCourse.get(Integer.valueOf(arrayCommand[5])).getTrainersOnCourse(), "Trainer");
                                            break;
                                        }
                                        default: {
                                            showAllName(allCourse.get(Integer.valueOf(arrayCommand[4])).getTrainersOnCourse(), "Trainer");
                                            break;
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    showAllName(allTrainer, "Trainer");
                                    break;
                                }
                            }
                            break;
                        }
                        default: {
                            printError("You can not " + arrayCommand[0] + " all " + arrayCommand[2] +
                                    "\nYou may have had in mind the student or teacher or course?\n");
                            break; //    ppppp   RRRRR    II  NN   NN  TTTTTTTT
                        }          //    pp  pp  RR  RR       NNN  NN     TT
                    }              //    PP  PP  RR RR    II  NNNN NN     TT
                    break;         //    ppppp   RRRRR    II  NN NNNN     TT
                }                  //    pp      RR  RR   II  NN NNNN     TT
                case "course": {   //    pp      RR   RR  II  NN   NN     TT
                    switch (arrayCommand[2]) {
                        case "id": {
                            System.out.println(allCourse.get(Integer.valueOf(arrayCommand[3])).information());
                            break;
                        }
                        default:{
                            System.out.println(allCourse.get(Integer.valueOf(arrayCommand[2])).information());
                            break;
                        }
                    }
                    break;
                }
                case "student":
                case "undergrad": {
                    switch (arrayCommand[2]) {
                        case "id": {
                            System.out.println(allStudent.get(Integer.valueOf(arrayCommand[3])).information());
                            break;
                        }
                        default:{
                            System.out.println(allStudent.get(Integer.valueOf(arrayCommand[2])).information());
                            break;
                        }
                    }
                    break;
                }
                case "trainer":
                case "teacher":
                case "instructor": {
                    switch (arrayCommand[2]) {
                        case "id": {
                            System.out.println(allTrainer.get(Integer.valueOf(arrayCommand[3])).information());
                            break;
                        }
                        default:{
                            System.out.println(allTrainer.get(Integer.valueOf(arrayCommand[2])).information());
                            break;
                        }
                    }
                    break;
                }
                case "gradebook":case "tasks":case "task":case "job": {
                    showGradebook();
                    break;
                }
                case "my":{
                    switch (user.getClass().getSimpleName()){
                        case "Student": {
                            Student studentUser = (Student)user;
                            switch (arrayCommand[2]){
                                case "name":{
                                    System.out.println((char) 27 + "[34m" + "Your name is:\t" + (char)27+"[0m" + studentUser.toString());
                                    break;
                                }
                                case "course":case "courses": {
                                    showAllName(studentUser.getCourse() , "Course");
                                    break;
                                }
                                case "gradebook":case "tasks":case "task":case "job":  {
                                    Course[] courseArray = studentUser.getCourse().values().toArray(new Course[studentUser.getCourse().values().size()]);
                                    for (Course elem : courseArray) {
                                        if(elem.getStudentAssessment().size() > 0){
                                            System.out.println((char)27+"[34m" + "Course name: \t" + (char)27+"[0m" +  elem.toString());
                                            Course.Task[] taskArray = elem.getStudentAssessment().toArray(new Course.Task[elem.getStudentAssessment().size()]);
                                            for(Course.Task elemTask : taskArray){
                                                System.out.println(elemTask.showGradebookPerStudent(studentUser));
                                                System.out.println();
                                            }
                                        }
                                    }
                                    break;
                                }
                                default:{
                                    printError("You can not " + arrayCommand[0] + " you " + arrayCommand[2] +
                                            "\nYou may have had in mind the tasks or course?\n");
                                    break;
                                }
                            }
                            break;
                        }
                        case "Trainer":{
                            Trainer trainerUser = (Trainer)user;
                            switch (arrayCommand[2]){
                                case "name":{
                                    System.out.println((char) 27 + "[34m" + "Your name is:\t" + (char)27+"[0m" + trainerUser.getName());
                                    break;
                                }
                                case "course":case "courses":{
                                    showAllName(trainerUser.getCourse() , "Course");
                                    break;
                                }
                                case "gradebook":case "tasks":case "task":case "job":  {
                                    printError( "Task trainers do not ask!\n" +
                                                "To view tasks write \"Print task\"");
                                    break;
                                }
                                default:{
                                    printError("You can not " + arrayCommand[0] + " you " + arrayCommand[2] +
                                            "\nYou may have had in mind the tasks or course?\n");
                                    break;
                                }
                            }
                            break;
                        }
                        case "Course":{
                            printError("He forgot?\n" + "You are an administrator - not a student," +
                                    " you already know how all of you have no " + arrayCommand[2]);
                            break;
                        }
                    }
                    break;
                }
                default: {
                    printError("You can not " + arrayCommand[0] + " - " + arrayCommand[1] +
                            "\nYou may have had in mind the student or teacher or course?\n");
                    break;
                }
            }
        }
        private void consoleMethodTransfer(String[] arrayCommand){
            switch (arrayCommand[1]) {
                case "student":case "undergrad":{
                    transferStudentOrTrainer(allStudent, "student");
                    break;
                }
                case "trainer":case "teacher":case "instructor":{
                    transferStudentOrTrainer(allTrainer, "trainer");
                    break;
                }
                default:{
                    printError("You can not " + arrayCommand[0] + " an " + arrayCommand[1] +
                            "\nYou may have had in mind the student or teacher?\n");
                    break;
                }
            }
        }
        private void consoleMethodWrite(String[] arrayCommand){
            switch (arrayCommand[1]){
                case "all": {
                    new WorkingWithTxt().writeBook(allCourse,  "Course");
                    new WorkingWithTxt().writeBook(allStudent, "Student");
                    new WorkingWithTxt().writeBook(allTrainer, "Trainer");
                    System.out.println((char)27+"[34m" + "All books successfully written" + (char)27+"[0m");
                    break;
                }
                case "gradebook":case "tasks":case "task":case "job": {
                    writeGradebook();
                    break;
                }
                case "course": {
                    if(arrayCommand[2].equals(""))new WorkingWithTxt().writeBook(allCourse, "Course");
                    else new WorkingWithTxt().writeBook(allCourse, arrayCommand[2]);
                    System.out.println((char)27+"[34m" + "Book " + arrayCommand[2] + " successfully written" + (char)27+"[0m");
                    break;
                }
                case "student":case "undergrad": {
                    if(arrayCommand[2].equals(""))new WorkingWithTxt().writeBook(allStudent, "Student");
                    else new WorkingWithTxt().writeBook(allStudent, arrayCommand[2]);
                    System.out.println((char)27+"[34m" + "Book " + arrayCommand[2] + " successfully written" + (char)27+"[0m");
                    break;
                }
                case "trainer":case "teacher":case "instructor": {
                    if(arrayCommand[2].equals(""))new WorkingWithTxt().writeBook(allTrainer, "Trainer");
                    else new WorkingWithTxt().writeBook(allTrainer, arrayCommand[2]);
                    System.out.println((char)27+"[34m" + "Book " + arrayCommand[2] + " successfully written" + (char)27+"[0m");
                    break;
                }
                default: {
                    printError("You can not write an " + arrayCommand[1] +
                            "\nYou may have had in mind the student or teacher or course or gradebook?\n");
                    break;
                }
            }
        }
        private void consoleMethodAI(MyClass user){
            System.out.println((char)27+"[35m" + "Hello " + user.toString() + ", how are you?" + (char)27+"[0m");
        }
        private String consoleMethodHelp(){
            return "add\ta\tnew\tcourse\t\t\t\t\t\t\n" +
                    "add\ta\tnew\tinstructor\t\t\t\t\t\t\n" +
                    "add\ta\tnew\tstudent\t\t\t\t\t\t\n" +
                    "add\ta\tnew\tteacher\t\t\t\t\t\t\n" +
                    "add\ta\tnew\ttrainer\t\t\t\t\t\t\n" +
                    "add\ta\tnew\tundergrad\t\t\t\t\t\t\n" +
                    "add\tcourse\t\t\t\t\t\t\t\t\n" +
                    "add\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "add\tjob\t\t\t\t\t\t\t\t\n" +
                    "add\tstudent\t\t\t\t\t\t\t\t\n" +
                    "add\ttask\t\t\t\t\t\t\t\t\n" +
                    "add\ttasks\t\t\t\t\t\t\t\t\n" +
                    "add\tteacher\t\t\t\t\t\t\t\t\n" +
                    "add\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "add\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "append\ta\tnew\tcourse\t\t\t\t\t\t\n" +
                    "append\ta\tnew\tinstructor\t\t\t\t\t\t\n" +
                    "append\ta\tnew\tstudent\t\t\t\t\t\t\n" +
                    "append\ta\tnew\tteacher\t\t\t\t\t\t\n" +
                    "append\ta\tnew\ttrainer\t\t\t\t\t\t\n" +
                    "append\ta\tnew\tundergrad\t\t\t\t\t\t\n" +
                    "append\tcourse\t\t\t\t\t\t\t\t\n" +
                    "append\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "append\tjob\t\t\t\t\t\t\t\t\n" +
                    "append\tstudent\t\t\t\t\t\t\t\t\n" +
                    "append\ttask\t\t\t\t\t\t\t\t\n" +
                    "append\ttasks\t\t\t\t\t\t\t\t\n" +
                    "append\tteacher\t\t\t\t\t\t\t\t\n" +
                    "append\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "append\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "build\ta\tnew\tcourse\t\t\t\t\t\t\n" +
                    "build\ta\tnew\tinstructor\t\t\t\t\t\t\n" +
                    "build\ta\tnew\tstudent\t\t\t\t\t\t\n" +
                    "build\ta\tnew\tteacher\t\t\t\t\t\t\n" +
                    "build\ta\tnew\ttrainer\t\t\t\t\t\t\n" +
                    "build\ta\tnew\tundergrad\t\t\t\t\t\t\n" +
                    "build\tcourse\t\t\t\t\t\t\t\t\n" +
                    "build\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "build\tjob\t\t\t\t\t\t\t\t\n" +
                    "build\tstudent\t\t\t\t\t\t\t\t\n" +
                    "build\ttask\t\t\t\t\t\t\t\t\n" +
                    "build\ttasks\t\t\t\t\t\t\t\t\n" +
                    "build\tteacher\t\t\t\t\t\t\t\t\n" +
                    "build\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "build\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "create\ta\tnew\tcourse\t\t\t\t\t\t\n" +
                    "create\ta\tnew\tinstructor\t\t\t\t\t\t\n" +
                    "create\ta\tnew\tstudent\t\t\t\t\t\t\n" +
                    "create\ta\tnew\tteacher\t\t\t\t\t\t\n" +
                    "create\ta\tnew\ttrainer\t\t\t\t\t\t\n" +
                    "create\ta\tnew\tundergrad\t\t\t\t\t\t\n" +
                    "create\tcourse\t\t\t\t\t\t\t\t\n" +
                    "create\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "create\tjob\t\t\t\t\t\t\t\t\n" +
                    "create\tstudent\t\t\t\t\t\t\t\t\n" +
                    "create\ttask\t\t\t\t\t\t\t\t\n" +
                    "create\ttasks\t\t\t\t\t\t\t\t\n" +
                    "create\tteacher\t\t\t\t\t\t\t\t\n" +
                    "create\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "create\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "delivery\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "delivery\tstudent\t\t\t\t\t\t\t\t\n" +
                    "delivery\tteacher\t\t\t\t\t\t\t\t\n" +
                    "delivery\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "delivery\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "demonstration\tall\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tall\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tall\tinstructor\tcourse\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tinstructor\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tinstructor\tcourse\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tinstructor\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "demonstration\tall\tinstructor\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "demonstration\tall\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "demonstration\tall\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tall\tstudent\tcourse\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tstudent\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tstudent\tcourse\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tstudent\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "demonstration\tall\tstudent\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "demonstration\tall\tstudent\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "demonstration\tall\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tall\tteacher\tcourse\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tteacher\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tteacher\tcourse\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tteacher\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "demonstration\tall\tteacher\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "demonstration\tall\tteacher\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "demonstration\tall\tthe\tcourse\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tthe\tinstructor\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tthe\tinstructor\tcourse\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tthe\tinstructor\tcourse\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tthe\tinstructor\tcourse\tby\tid\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tinstructor\ton\tthe\tcourse\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tinstructor\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "demonstration\tall\tthe\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "demonstration\tall\tthe\tstudent\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tthe\tstudent\tcourse\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tthe\tstudent\tcourse\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tthe\tstudent\tcourse\tby\tid\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tstudent\ton\tthe\tcourse\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tstudent\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "demonstration\tall\tthe\tstudent\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "demonstration\tall\tthe\tteacher\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tthe\tteacher\tcourse\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tthe\tteacher\tcourse\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tthe\tteacher\tcourse\tby\tid\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tteacher\ton\tthe\tcourse\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tteacher\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "demonstration\tall\tthe\tteacher\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "demonstration\tall\tthe\ttrainer\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tthe\ttrainer\tcourse\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tthe\ttrainer\tcourse\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tthe\ttrainer\tcourse\tby\tid\t(number)\t\t\n" +
                    "demonstration\tall\tthe\ttrainer\ton\tthe\tcourse\t(number)\t\t\n" +
                    "demonstration\tall\tthe\ttrainer\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "demonstration\tall\tthe\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "demonstration\tall\tthe\tundergrad\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tthe\tundergrad\tcourse\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tthe\tundergrad\tcourse\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tthe\tundergrad\tcourse\tby\tid\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tundergrad\ton\tthe\tcourse\t(number)\t\t\n" +
                    "demonstration\tall\tthe\tundergrad\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "demonstration\tall\tthe\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "demonstration\tall\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tall\ttrainer\tcourse\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\ttrainer\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\ttrainer\tcourse\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tall\ttrainer\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "demonstration\tall\ttrainer\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "demonstration\tall\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "demonstration\tall\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tall\tundergrad\tcourse\t(number)\t\t\t\t\t\n" +
                    "demonstration\tall\tundergrad\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "demonstration\tall\tundergrad\tcourse\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tall\tundergrad\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "demonstration\tall\tundergrad\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "demonstration\tall\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "demonstration\tall\t\t\t\t\t\t\t\t\n" +
                    "demonstration\tcourse\t(number)\t\t\t\t\t\t\t\n" +
                    "demonstration\tcourse\tby\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tcourse\tby\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tcourse\tid\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "demonstration\tinstructor\t(number)\t\t\t\t\t\t\t\n" +
                    "demonstration\tinstructor\tby\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tinstructor\tby\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tinstructor\tid\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tjob\t\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\tcourse\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\tcourses\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\tgradebook\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\tjob\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\tname\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\ttask\t\t\t\t\t\t\t\n" +
                    "demonstration\tmy\ttasks\t\t\t\t\t\t\t\n" +
                    "demonstration\tstudent\t(number)\t\t\t\t\t\t\t\n" +
                    "demonstration\tstudent\tby\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tstudent\tby\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tstudent\tid\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\ttask\t\t\t\t\t\t\t\t\n" +
                    "demonstration\ttasks\t\t\t\t\t\t\t\t\n" +
                    "demonstration\tteacher\t(number)\t\t\t\t\t\t\t\n" +
                    "demonstration\tteacher\tby\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tteacher\tby\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tteacher\tid\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\tcourse\tby\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tthe\tcourse\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tthe\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\tinstructor\tby\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tthe\tinstructor\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tthe\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\tstudent\tby\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tthe\tstudent\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tthe\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\tteacher\tby\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tthe\tteacher\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tthe\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\ttrainer\tby\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tthe\ttrainer\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tthe\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tthe\tundergrad\tby\tid\t(number)\t\t\t\t\n" +
                    "demonstration\tthe\tundergrad\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\ttrainer\t(number)\t\t\t\t\t\t\t\n" +
                    "demonstration\ttrainer\tby\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\ttrainer\tby\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\ttrainer\tid\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tundergrad\t(number)\t\t\t\t\t\t\t\n" +
                    "demonstration\tundergrad\tby\t(number)\t\t\t\t\t\t\n" +
                    "demonstration\tundergrad\tby\tid\t(number)\t\t\t\t\t\n" +
                    "demonstration\tundergrad\tid\t(number)\t\t\t\t\t\t\n" +
                    "exit\t\t\t\t\t\t\t\t\t\n" +
                    "form\ta\tnew\tcourse\t\t\t\t\t\t\n" +
                    "form\ta\tnew\tinstructor\t\t\t\t\t\t\n" +
                    "form\ta\tnew\tstudent\t\t\t\t\t\t\n" +
                    "form\ta\tnew\tteacher\t\t\t\t\t\t\n" +
                    "form\ta\tnew\ttrainer\t\t\t\t\t\t\n" +
                    "form\ta\tnew\tundergrad\t\t\t\t\t\t\n" +
                    "form\tcourse\t\t\t\t\t\t\t\t\n" +
                    "form\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "form\tjob\t\t\t\t\t\t\t\t\n" +
                    "form\tstudent\t\t\t\t\t\t\t\t\n" +
                    "form\ttask\t\t\t\t\t\t\t\t\n" +
                    "form\ttasks\t\t\t\t\t\t\t\t\n" +
                    "form\tteacher\t\t\t\t\t\t\t\t\n" +
                    "form\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "form\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "helo\t\t\t\t\t\t\t\t\t\n" +
                    "output\t\t\t\t\t\t\t\t\t\n" +
                    "print\tall\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "print\tall\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "print\tall\tinstructor\tcourse\t(number)\t\t\t\t\t\n" +
                    "print\tall\tinstructor\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "print\tall\tinstructor\tcourse\tid\t(number)\t\t\t\t\n" +
                    "print\tall\tinstructor\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "print\tall\tinstructor\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "print\tall\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "print\tall\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "print\tall\tstudent\tcourse\t(number)\t\t\t\t\t\n" +
                    "print\tall\tstudent\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "print\tall\tstudent\tcourse\tid\t(number)\t\t\t\t\n" +
                    "print\tall\tstudent\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "print\tall\tstudent\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "print\tall\tstudent\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "print\tall\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "print\tall\tteacher\tcourse\t(number)\t\t\t\t\t\n" +
                    "print\tall\tteacher\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "print\tall\tteacher\tcourse\tid\t(number)\t\t\t\t\n" +
                    "print\tall\tteacher\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "print\tall\tteacher\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "print\tall\tteacher\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "print\tall\tthe\tcourse\t(number)\t\t\t\t\t\n" +
                    "print\tall\tthe\tinstructor\t(number)\t\t\t\t\t\n" +
                    "print\tall\tthe\tinstructor\tcourse\t(number)\t\t\t\t\n" +
                    "print\tall\tthe\tinstructor\tcourse\tid\t(number)\t\t\t\n" +
                    "print\tall\tthe\tinstructor\tcourse\tby\tid\t(number)\t\t\n" +
                    "print\tall\tthe\tinstructor\ton\tthe\tcourse\t(number)\t\t\n" +
                    "print\tall\tthe\tinstructor\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "print\tall\tthe\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "print\tall\tthe\tstudent\t(number)\t\t\t\t\t\n" +
                    "print\tall\tthe\tstudent\tcourse\t(number)\t\t\t\t\n" +
                    "print\tall\tthe\tstudent\tcourse\tid\t(number)\t\t\t\n" +
                    "print\tall\tthe\tstudent\tcourse\tby\tid\t(number)\t\t\n" +
                    "print\tall\tthe\tstudent\ton\tthe\tcourse\t(number)\t\t\n" +
                    "print\tall\tthe\tstudent\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "print\tall\tthe\tstudent\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "print\tall\tthe\tteacher\t(number)\t\t\t\t\t\n" +
                    "print\tall\tthe\tteacher\tcourse\t(number)\t\t\t\t\n" +
                    "print\tall\tthe\tteacher\tcourse\tid\t(number)\t\t\t\n" +
                    "print\tall\tthe\tteacher\tcourse\tby\tid\t(number)\t\t\n" +
                    "print\tall\tthe\tteacher\ton\tthe\tcourse\t(number)\t\t\n" +
                    "print\tall\tthe\tteacher\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "print\tall\tthe\tteacher\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "print\tall\tthe\ttrainer\t(number)\t\t\t\t\t\n" +
                    "print\tall\tthe\ttrainer\tcourse\t(number)\t\t\t\t\n" +
                    "print\tall\tthe\ttrainer\tcourse\tid\t(number)\t\t\t\n" +
                    "print\tall\tthe\ttrainer\tcourse\tby\tid\t(number)\t\t\n" +
                    "print\tall\tthe\ttrainer\ton\tthe\tcourse\t(number)\t\t\n" +
                    "print\tall\tthe\ttrainer\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "print\tall\tthe\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "print\tall\tthe\tundergrad\t(number)\t\t\t\t\t\n" +
                    "print\tall\tthe\tundergrad\tcourse\t(number)\t\t\t\t\n" +
                    "print\tall\tthe\tundergrad\tcourse\tid\t(number)\t\t\t\n" +
                    "print\tall\tthe\tundergrad\tcourse\tby\tid\t(number)\t\t\n" +
                    "print\tall\tthe\tundergrad\ton\tthe\tcourse\t(number)\t\t\n" +
                    "print\tall\tthe\tundergrad\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "print\tall\tthe\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "print\tall\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "print\tall\ttrainer\tcourse\t(number)\t\t\t\t\t\n" +
                    "print\tall\ttrainer\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "print\tall\ttrainer\tcourse\tid\t(number)\t\t\t\t\n" +
                    "print\tall\ttrainer\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "print\tall\ttrainer\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "print\tall\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "print\tall\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "print\tall\tundergrad\tcourse\t(number)\t\t\t\t\t\n" +
                    "print\tall\tundergrad\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "print\tall\tundergrad\tcourse\tid\t(number)\t\t\t\t\n" +
                    "print\tall\tundergrad\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "print\tall\tundergrad\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "print\tall\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "print\tall\t\t\t\t\t\t\t\t\n" +
                    "print\tcourse\t(number)\t\t\t\t\t\t\t\n" +
                    "print\tcourse\tby\t(number)\t\t\t\t\t\t\n" +
                    "print\tcourse\tby\tid\t(number)\t\t\t\t\t\n" +
                    "print\tcourse\tid\t(number)\t\t\t\t\t\t\n" +
                    "print\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "print\tinstructor\t(number)\t\t\t\t\t\t\t\n" +
                    "print\tinstructor\tby\t(number)\t\t\t\t\t\t\n" +
                    "print\tinstructor\tby\tid\t(number)\t\t\t\t\t\n" +
                    "print\tinstructor\tid\t(number)\t\t\t\t\t\t\n" +
                    "print\tjob\t\t\t\t\t\t\t\t\n" +
                    "print\tstudent\t(number)\t\t\t\t\t\t\t\n" +
                    "print\tstudent\tby\t(number)\t\t\t\t\t\t\n" +
                    "print\tstudent\tby\tid\t(number)\t\t\t\t\t\n" +
                    "print\tstudent\tid\t(number)\t\t\t\t\t\t\n" +
                    "print\ttask\t\t\t\t\t\t\t\t\n" +
                    "print\ttasks\t\t\t\t\t\t\t\t\n" +
                    "print\tteacher\t(number)\t\t\t\t\t\t\t\n" +
                    "print\tteacher\tby\t(number)\t\t\t\t\t\t\n" +
                    "print\tteacher\tby\tid\t(number)\t\t\t\t\t\n" +
                    "print\tteacher\tid\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\tcourse\tby\tid\t(number)\t\t\t\t\n" +
                    "print\tthe\tcourse\tid\t(number)\t\t\t\t\t\n" +
                    "print\tthe\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\tinstructor\tby\tid\t(number)\t\t\t\t\n" +
                    "print\tthe\tinstructor\tid\t(number)\t\t\t\t\t\n" +
                    "print\tthe\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\tstudent\tby\tid\t(number)\t\t\t\t\n" +
                    "print\tthe\tstudent\tid\t(number)\t\t\t\t\t\n" +
                    "print\tthe\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\tteacher\tby\tid\t(number)\t\t\t\t\n" +
                    "print\tthe\tteacher\tid\t(number)\t\t\t\t\t\n" +
                    "print\tthe\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\ttrainer\tby\tid\t(number)\t\t\t\t\n" +
                    "print\tthe\ttrainer\tid\t(number)\t\t\t\t\t\n" +
                    "print\tthe\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "print\tthe\tundergrad\tby\tid\t(number)\t\t\t\t\n" +
                    "print\tthe\tundergrad\tid\t(number)\t\t\t\t\t\n" +
                    "print\ttrainer\t(number)\t\t\t\t\t\t\t\n" +
                    "print\ttrainer\tby\t(number)\t\t\t\t\t\t\n" +
                    "print\ttrainer\tby\tid\t(number)\t\t\t\t\t\n" +
                    "print\ttrainer\tid\t(number)\t\t\t\t\t\t\n" +
                    "print\tundergrad\t(number)\t\t\t\t\t\t\t\n" +
                    "print\tundergrad\tby\t(number)\t\t\t\t\t\t\n" +
                    "print\tundergrad\tby\tid\t(number)\t\t\t\t\t\n" +
                    "print\tundergrad\tid\t(number)\t\t\t\t\t\t\n" +
                    "print \tmy\tcourse\t\t\t\t\t\t\t\n" +
                    "print \tmy\tcourses\t\t\t\t\t\t\t\n" +
                    "print \tmy\tgradebook\t\t\t\t\t\t\t\n" +
                    "print \tmy\tjob\t\t\t\t\t\t\t\n" +
                    "print \tmy\tname\t\t\t\t\t\t\t\n" +
                    "print \tmy\ttask\t\t\t\t\t\t\t\n" +
                    "print \tmy\ttasks\t\t\t\t\t\t\t\n" +
                    "publish\tall\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "publish\tall\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "publish\tall\tinstructor\tcourse\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tinstructor\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "publish\tall\tinstructor\tcourse\tid\t(number)\t\t\t\t\n" +
                    "publish\tall\tinstructor\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "publish\tall\tinstructor\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "publish\tall\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "publish\tall\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "publish\tall\tstudent\tcourse\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tstudent\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "publish\tall\tstudent\tcourse\tid\t(number)\t\t\t\t\n" +
                    "publish\tall\tstudent\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "publish\tall\tstudent\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "publish\tall\tstudent\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "publish\tall\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "publish\tall\tteacher\tcourse\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tteacher\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "publish\tall\tteacher\tcourse\tid\t(number)\t\t\t\t\n" +
                    "publish\tall\tteacher\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "publish\tall\tteacher\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "publish\tall\tteacher\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "publish\tall\tthe\tcourse\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tthe\tinstructor\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tthe\tinstructor\tcourse\t(number)\t\t\t\t\n" +
                    "publish\tall\tthe\tinstructor\tcourse\tid\t(number)\t\t\t\n" +
                    "publish\tall\tthe\tinstructor\tcourse\tby\tid\t(number)\t\t\n" +
                    "publish\tall\tthe\tinstructor\ton\tthe\tcourse\t(number)\t\t\n" +
                    "publish\tall\tthe\tinstructor\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "publish\tall\tthe\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "publish\tall\tthe\tstudent\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tthe\tstudent\tcourse\t(number)\t\t\t\t\n" +
                    "publish\tall\tthe\tstudent\tcourse\tid\t(number)\t\t\t\n" +
                    "publish\tall\tthe\tstudent\tcourse\tby\tid\t(number)\t\t\n" +
                    "publish\tall\tthe\tstudent\ton\tthe\tcourse\t(number)\t\t\n" +
                    "publish\tall\tthe\tstudent\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "publish\tall\tthe\tstudent\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "publish\tall\tthe\tteacher\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tthe\tteacher\tcourse\t(number)\t\t\t\t\n" +
                    "publish\tall\tthe\tteacher\tcourse\tid\t(number)\t\t\t\n" +
                    "publish\tall\tthe\tteacher\tcourse\tby\tid\t(number)\t\t\n" +
                    "publish\tall\tthe\tteacher\ton\tthe\tcourse\t(number)\t\t\n" +
                    "publish\tall\tthe\tteacher\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "publish\tall\tthe\tteacher\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "publish\tall\tthe\ttrainer\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tthe\ttrainer\tcourse\t(number)\t\t\t\t\n" +
                    "publish\tall\tthe\ttrainer\tcourse\tid\t(number)\t\t\t\n" +
                    "publish\tall\tthe\ttrainer\tcourse\tby\tid\t(number)\t\t\n" +
                    "publish\tall\tthe\ttrainer\ton\tthe\tcourse\t(number)\t\t\n" +
                    "publish\tall\tthe\ttrainer\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "publish\tall\tthe\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "publish\tall\tthe\tundergrad\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tthe\tundergrad\tcourse\t(number)\t\t\t\t\n" +
                    "publish\tall\tthe\tundergrad\tcourse\tid\t(number)\t\t\t\n" +
                    "publish\tall\tthe\tundergrad\tcourse\tby\tid\t(number)\t\t\n" +
                    "publish\tall\tthe\tundergrad\ton\tthe\tcourse\t(number)\t\t\n" +
                    "publish\tall\tthe\tundergrad\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "publish\tall\tthe\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "publish\tall\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "publish\tall\ttrainer\tcourse\t(number)\t\t\t\t\t\n" +
                    "publish\tall\ttrainer\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "publish\tall\ttrainer\tcourse\tid\t(number)\t\t\t\t\n" +
                    "publish\tall\ttrainer\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "publish\tall\ttrainer\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "publish\tall\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "publish\tall\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "publish\tall\tundergrad\tcourse\t(number)\t\t\t\t\t\n" +
                    "publish\tall\tundergrad\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "publish\tall\tundergrad\tcourse\tid\t(number)\t\t\t\t\n" +
                    "publish\tall\tundergrad\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "publish\tall\tundergrad\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "publish\tall\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "publish\tall\t\t\t\t\t\t\t\t\n" +
                    "publish\tcourse\t(number)\t\t\t\t\t\t\t\n" +
                    "publish\tcourse\tby\t(number)\t\t\t\t\t\t\n" +
                    "publish\tcourse\tby\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tcourse\tid\t(number)\t\t\t\t\t\t\n" +
                    "publish\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "publish\tinstructor\t(number)\t\t\t\t\t\t\t\n" +
                    "publish\tinstructor\tby\t(number)\t\t\t\t\t\t\n" +
                    "publish\tinstructor\tby\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tinstructor\tid\t(number)\t\t\t\t\t\t\n" +
                    "publish\tjob\t\t\t\t\t\t\t\t\n" +
                    "publish\tmy\tcourse\t\t\t\t\t\t\t\n" +
                    "publish\tmy\tcourses\t\t\t\t\t\t\t\n" +
                    "publish\tmy\tgradebook\t\t\t\t\t\t\t\n" +
                    "publish\tmy\tjob\t\t\t\t\t\t\t\n" +
                    "publish\tmy\tname\t\t\t\t\t\t\t\n" +
                    "publish\tmy\ttask\t\t\t\t\t\t\t\n" +
                    "publish\tmy\ttasks\t\t\t\t\t\t\t\n" +
                    "publish\tstudent\t(number)\t\t\t\t\t\t\t\n" +
                    "publish\tstudent\tby\t(number)\t\t\t\t\t\t\n" +
                    "publish\tstudent\tby\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tstudent\tid\t(number)\t\t\t\t\t\t\n" +
                    "publish\ttask\t\t\t\t\t\t\t\t\n" +
                    "publish\ttasks\t\t\t\t\t\t\t\t\n" +
                    "publish\tteacher\t(number)\t\t\t\t\t\t\t\n" +
                    "publish\tteacher\tby\t(number)\t\t\t\t\t\t\n" +
                    "publish\tteacher\tby\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tteacher\tid\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\tcourse\tby\tid\t(number)\t\t\t\t\n" +
                    "publish\tthe\tcourse\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tthe\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\tinstructor\tby\tid\t(number)\t\t\t\t\n" +
                    "publish\tthe\tinstructor\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tthe\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\tstudent\tby\tid\t(number)\t\t\t\t\n" +
                    "publish\tthe\tstudent\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tthe\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\tteacher\tby\tid\t(number)\t\t\t\t\n" +
                    "publish\tthe\tteacher\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tthe\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\ttrainer\tby\tid\t(number)\t\t\t\t\n" +
                    "publish\tthe\ttrainer\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tthe\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "publish\tthe\tundergrad\tby\tid\t(number)\t\t\t\t\n" +
                    "publish\tthe\tundergrad\tid\t(number)\t\t\t\t\t\n" +
                    "publish\ttrainer\t(number)\t\t\t\t\t\t\t\n" +
                    "publish\ttrainer\tby\t(number)\t\t\t\t\t\t\n" +
                    "publish\ttrainer\tby\tid\t(number)\t\t\t\t\t\n" +
                    "publish\ttrainer\tid\t(number)\t\t\t\t\t\t\n" +
                    "publish\tundergrad\t(number)\t\t\t\t\t\t\t\n" +
                    "publish\tundergrad\tby\t(number)\t\t\t\t\t\t\n" +
                    "publish\tundergrad\tby\tid\t(number)\t\t\t\t\t\n" +
                    "publish\tundergrad\tid\t(number)\t\t\t\t\t\t\n" +
                    "read\t\t\t\t\t\t\t\t\t\n" +
                    "record\tall\t\t\t\t\t\t\t\t\n" +
                    "record\tcourse\t\t\t\t\t\t\t\t\n" +
                    "record\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "record\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "record\tjob\t\t\t\t\t\t\t\t\n" +
                    "record\tstudent\t\t\t\t\t\t\t\t\n" +
                    "record\ttask\t\t\t\t\t\t\t\t\n" +
                    "record\ttasks\t\t\t\t\t\t\t\t\n" +
                    "record\tteacher\t\t\t\t\t\t\t\t\n" +
                    "record\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "record\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "save\t\t\t\t\t\t\t\t\t\n" +
                    "show\tall\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "show\tall\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "show\tall\tinstructor\tcourse\t(number)\t\t\t\t\t\n" +
                    "show\tall\tinstructor\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "show\tall\tinstructor\tcourse\tid\t(number)\t\t\t\t\n" +
                    "show\tall\tinstructor\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "show\tall\tinstructor\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "show\tall\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "show\tall\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "show\tall\tstudent\tcourse\t(number)\t\t\t\t\t\n" +
                    "show\tall\tstudent\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "show\tall\tstudent\tcourse\tid\t(number)\t\t\t\t\n" +
                    "show\tall\tstudent\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "show\tall\tstudent\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "show\tall\tstudent\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "show\tall\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "show\tall\tteacher\tcourse\t(number)\t\t\t\t\t\n" +
                    "show\tall\tteacher\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "show\tall\tteacher\tcourse\tid\t(number)\t\t\t\t\n" +
                    "show\tall\tteacher\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "show\tall\tteacher\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "show\tall\tteacher\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "show\tall\tthe\tcourse\t(number)\t\t\t\t\t\n" +
                    "show\tall\tthe\tinstructor\t(number)\t\t\t\t\t\n" +
                    "show\tall\tthe\tinstructor\tcourse\t(number)\t\t\t\t\n" +
                    "show\tall\tthe\tinstructor\tcourse\tid\t(number)\t\t\t\n" +
                    "show\tall\tthe\tinstructor\tcourse\tby\tid\t(number)\t\t\n" +
                    "show\tall\tthe\tinstructor\ton\tthe\tcourse\t(number)\t\t\n" +
                    "show\tall\tthe\tinstructor\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "show\tall\tthe\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "show\tall\tthe\tstudent\t(number)\t\t\t\t\t\n" +
                    "show\tall\tthe\tstudent\tcourse\t(number)\t\t\t\t\n" +
                    "show\tall\tthe\tstudent\tcourse\tid\t(number)\t\t\t\n" +
                    "show\tall\tthe\tstudent\tcourse\tby\tid\t(number)\t\t\n" +
                    "show\tall\tthe\tstudent\ton\tthe\tcourse\t(number)\t\t\n" +
                    "show\tall\tthe\tstudent\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "show\tall\tthe\tstudent\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "show\tall\tthe\tteacher\t(number)\t\t\t\t\t\n" +
                    "show\tall\tthe\tteacher\tcourse\t(number)\t\t\t\t\n" +
                    "show\tall\tthe\tteacher\tcourse\tid\t(number)\t\t\t\n" +
                    "show\tall\tthe\tteacher\tcourse\tby\tid\t(number)\t\t\n" +
                    "show\tall\tthe\tteacher\ton\tthe\tcourse\t(number)\t\t\n" +
                    "show\tall\tthe\tteacher\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "show\tall\tthe\tteacher\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "show\tall\tthe\ttrainer\t(number)\t\t\t\t\t\n" +
                    "show\tall\tthe\ttrainer\tcourse\t(number)\t\t\t\t\n" +
                    "show\tall\tthe\ttrainer\tcourse\tid\t(number)\t\t\t\n" +
                    "show\tall\tthe\ttrainer\tcourse\tby\tid\t(number)\t\t\n" +
                    "show\tall\tthe\ttrainer\ton\tthe\tcourse\t(number)\t\t\n" +
                    "show\tall\tthe\ttrainer\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "show\tall\tthe\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "show\tall\tthe\tundergrad\t(number)\t\t\t\t\t\n" +
                    "show\tall\tthe\tundergrad\tcourse\t(number)\t\t\t\t\n" +
                    "show\tall\tthe\tundergrad\tcourse\tid\t(number)\t\t\t\n" +
                    "show\tall\tthe\tundergrad\tcourse\tby\tid\t(number)\t\t\n" +
                    "show\tall\tthe\tundergrad\ton\tthe\tcourse\t(number)\t\t\n" +
                    "show\tall\tthe\tundergrad\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "show\tall\tthe\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "show\tall\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "show\tall\ttrainer\tcourse\t(number)\t\t\t\t\t\n" +
                    "show\tall\ttrainer\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "show\tall\ttrainer\tcourse\tid\t(number)\t\t\t\t\n" +
                    "show\tall\ttrainer\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "show\tall\ttrainer\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "show\tall\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "show\tall\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "show\tall\tundergrad\tcourse\t(number)\t\t\t\t\t\n" +
                    "show\tall\tundergrad\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "show\tall\tundergrad\tcourse\tid\t(number)\t\t\t\t\n" +
                    "show\tall\tundergrad\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "show\tall\tundergrad\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "show\tall\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "show\tall\t\t\t\t\t\t\t\t\n" +
                    "show\tcourse\t(number)\t\t\t\t\t\t\t\n" +
                    "show\tcourse\tby\t(number)\t\t\t\t\t\t\n" +
                    "show\tcourse\tby\tid\t(number)\t\t\t\t\t\n" +
                    "show\tcourse\tid\t(number)\t\t\t\t\t\t\n" +
                    "show\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "show\tinstructor\t(number)\t\t\t\t\t\t\t\n" +
                    "show\tinstructor\tby\t(number)\t\t\t\t\t\t\n" +
                    "show\tinstructor\tby\tid\t(number)\t\t\t\t\t\n" +
                    "show\tinstructor\tid\t(number)\t\t\t\t\t\t\n" +
                    "show\tjob\t\t\t\t\t\t\t\t\n" +
                    "show\tmy\tcourse\t\t\t\t\t\t\t\n" +
                    "show\tmy\tcourses\t\t\t\t\t\t\t\n" +
                    "show\tmy\tgradebook\t\t\t\t\t\t\t\n" +
                    "show\tmy\tjob\t\t\t\t\t\t\t\n" +
                    "show\tmy\tname\t\t\t\t\t\t\t\n" +
                    "show\tmy\ttask\t\t\t\t\t\t\t\n" +
                    "show\tmy\ttasks\t\t\t\t\t\t\t\n" +
                    "show\tstudent\t(number)\t\t\t\t\t\t\t\n" +
                    "show\tstudent\tby\t(number)\t\t\t\t\t\t\n" +
                    "show\tstudent\tby\tid\t(number)\t\t\t\t\t\n" +
                    "show\tstudent\tid\t(number)\t\t\t\t\t\t\n" +
                    "show\ttask\t\t\t\t\t\t\t\t\n" +
                    "show\ttasks\t\t\t\t\t\t\t\t\n" +
                    "show\tteacher\t(number)\t\t\t\t\t\t\t\n" +
                    "show\tteacher\tby\t(number)\t\t\t\t\t\t\n" +
                    "show\tteacher\tby\tid\t(number)\t\t\t\t\t\n" +
                    "show\tteacher\tid\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\tcourse\tby\tid\t(number)\t\t\t\t\n" +
                    "show\tthe\tcourse\tid\t(number)\t\t\t\t\t\n" +
                    "show\tthe\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\tinstructor\tby\tid\t(number)\t\t\t\t\n" +
                    "show\tthe\tinstructor\tid\t(number)\t\t\t\t\t\n" +
                    "show\tthe\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\tstudent\tby\tid\t(number)\t\t\t\t\n" +
                    "show\tthe\tstudent\tid\t(number)\t\t\t\t\t\n" +
                    "show\tthe\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\tteacher\tby\tid\t(number)\t\t\t\t\n" +
                    "show\tthe\tteacher\tid\t(number)\t\t\t\t\t\n" +
                    "show\tthe\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\ttrainer\tby\tid\t(number)\t\t\t\t\n" +
                    "show\tthe\ttrainer\tid\t(number)\t\t\t\t\t\n" +
                    "show\tthe\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "show\tthe\tundergrad\tby\tid\t(number)\t\t\t\t\n" +
                    "show\tthe\tundergrad\tid\t(number)\t\t\t\t\t\n" +
                    "show\ttrainer\t(number)\t\t\t\t\t\t\t\n" +
                    "show\ttrainer\tby\t(number)\t\t\t\t\t\t\n" +
                    "show\ttrainer\tby\tid\t(number)\t\t\t\t\t\n" +
                    "show\ttrainer\tid\t(number)\t\t\t\t\t\t\n" +
                    "show\tundergrad\t(number)\t\t\t\t\t\t\t\n" +
                    "show\tundergrad\tby\t(number)\t\t\t\t\t\t\n" +
                    "show\tundergrad\tby\tid\t(number)\t\t\t\t\t\n" +
                    "show\tundergrad\tid\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\tinstructor\tcourse\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tinstructor\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "showing\tall\tinstructor\tcourse\tid\t(number)\t\t\t\t\n" +
                    "showing\tall\tinstructor\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "showing\tall\tinstructor\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "showing\tall\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "showing\tall\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\tstudent\tcourse\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tstudent\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "showing\tall\tstudent\tcourse\tid\t(number)\t\t\t\t\n" +
                    "showing\tall\tstudent\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "showing\tall\tstudent\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "showing\tall\tstudent\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "showing\tall\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\tteacher\tcourse\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tteacher\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "showing\tall\tteacher\tcourse\tid\t(number)\t\t\t\t\n" +
                    "showing\tall\tteacher\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "showing\tall\tteacher\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "showing\tall\tteacher\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "showing\tall\tthe\tcourse\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tthe\tinstructor\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tthe\tinstructor\tcourse\t(number)\t\t\t\t\n" +
                    "showing\tall\tthe\tinstructor\tcourse\tid\t(number)\t\t\t\n" +
                    "showing\tall\tthe\tinstructor\tcourse\tby\tid\t(number)\t\t\n" +
                    "showing\tall\tthe\tinstructor\ton\tthe\tcourse\t(number)\t\t\n" +
                    "showing\tall\tthe\tinstructor\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "showing\tall\tthe\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "showing\tall\tthe\tstudent\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tthe\tstudent\tcourse\t(number)\t\t\t\t\n" +
                    "showing\tall\tthe\tstudent\tcourse\tid\t(number)\t\t\t\n" +
                    "showing\tall\tthe\tstudent\tcourse\tby\tid\t(number)\t\t\n" +
                    "showing\tall\tthe\tstudent\ton\tthe\tcourse\t(number)\t\t\n" +
                    "showing\tall\tthe\tstudent\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "showing\tall\tthe\tstudent\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "showing\tall\tthe\tteacher\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tthe\tteacher\tcourse\t(number)\t\t\t\t\n" +
                    "showing\tall\tthe\tteacher\tcourse\tid\t(number)\t\t\t\n" +
                    "showing\tall\tthe\tteacher\tcourse\tby\tid\t(number)\t\t\n" +
                    "showing\tall\tthe\tteacher\ton\tthe\tcourse\t(number)\t\t\n" +
                    "showing\tall\tthe\tteacher\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "showing\tall\tthe\tteacher\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "showing\tall\tthe\ttrainer\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tthe\ttrainer\tcourse\t(number)\t\t\t\t\n" +
                    "showing\tall\tthe\ttrainer\tcourse\tid\t(number)\t\t\t\n" +
                    "showing\tall\tthe\ttrainer\tcourse\tby\tid\t(number)\t\t\n" +
                    "showing\tall\tthe\ttrainer\ton\tthe\tcourse\t(number)\t\t\n" +
                    "showing\tall\tthe\ttrainer\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "showing\tall\tthe\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "showing\tall\tthe\tundergrad\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tthe\tundergrad\tcourse\t(number)\t\t\t\t\n" +
                    "showing\tall\tthe\tundergrad\tcourse\tid\t(number)\t\t\t\n" +
                    "showing\tall\tthe\tundergrad\tcourse\tby\tid\t(number)\t\t\n" +
                    "showing\tall\tthe\tundergrad\ton\tthe\tcourse\t(number)\t\t\n" +
                    "showing\tall\tthe\tundergrad\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "showing\tall\tthe\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "showing\tall\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\ttrainer\tcourse\t(number)\t\t\t\t\t\n" +
                    "showing\tall\ttrainer\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "showing\tall\ttrainer\tcourse\tid\t(number)\t\t\t\t\n" +
                    "showing\tall\ttrainer\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "showing\tall\ttrainer\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "showing\tall\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "showing\tall\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "showing\tall\tundergrad\tcourse\t(number)\t\t\t\t\t\n" +
                    "showing\tall\tundergrad\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "showing\tall\tundergrad\tcourse\tid\t(number)\t\t\t\t\n" +
                    "showing\tall\tundergrad\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "showing\tall\tundergrad\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "showing\tall\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "showing\tall\t\t\t\t\t\t\t\t\n" +
                    "showing\tcourse\t(number)\t\t\t\t\t\t\t\n" +
                    "showing\tcourse\tby\t(number)\t\t\t\t\t\t\n" +
                    "showing\tcourse\tby\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tcourse\tid\t(number)\t\t\t\t\t\t\n" +
                    "showing\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "showing\tinstructor\t(number)\t\t\t\t\t\t\t\n" +
                    "showing\tinstructor\tby\t(number)\t\t\t\t\t\t\n" +
                    "showing\tinstructor\tby\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tinstructor\tid\t(number)\t\t\t\t\t\t\n" +
                    "showing\tjob\t\t\t\t\t\t\t\t\n" +
                    "showing\tmy\tcourse\t\t\t\t\t\t\t\n" +
                    "showing\tmy\tcourses\t\t\t\t\t\t\t\n" +
                    "showing\tmy\tgradebook\t\t\t\t\t\t\t\n" +
                    "showing\tmy\tjob\t\t\t\t\t\t\t\n" +
                    "showing\tmy\tname\t\t\t\t\t\t\t\n" +
                    "showing\tmy\ttask\t\t\t\t\t\t\t\n" +
                    "showing\tmy\ttasks\t\t\t\t\t\t\t\n" +
                    "showing\tstudent\t(number)\t\t\t\t\t\t\t\n" +
                    "showing\tstudent\tby\t(number)\t\t\t\t\t\t\n" +
                    "showing\tstudent\tby\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tstudent\tid\t(number)\t\t\t\t\t\t\n" +
                    "showing\ttask\t\t\t\t\t\t\t\t\n" +
                    "showing\ttasks\t\t\t\t\t\t\t\t\n" +
                    "showing\tteacher\t(number)\t\t\t\t\t\t\t\n" +
                    "showing\tteacher\tby\t(number)\t\t\t\t\t\t\n" +
                    "showing\tteacher\tby\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tteacher\tid\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\tcourse\tby\tid\t(number)\t\t\t\t\n" +
                    "showing\tthe\tcourse\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tthe\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\tinstructor\tby\tid\t(number)\t\t\t\t\n" +
                    "showing\tthe\tinstructor\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tthe\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\tstudent\tby\tid\t(number)\t\t\t\t\n" +
                    "showing\tthe\tstudent\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tthe\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\tteacher\tby\tid\t(number)\t\t\t\t\n" +
                    "showing\tthe\tteacher\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tthe\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\ttrainer\tby\tid\t(number)\t\t\t\t\n" +
                    "showing\tthe\ttrainer\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tthe\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "showing\tthe\tundergrad\tby\tid\t(number)\t\t\t\t\n" +
                    "showing\tthe\tundergrad\tid\t(number)\t\t\t\t\t\n" +
                    "showing\ttrainer\t(number)\t\t\t\t\t\t\t\n" +
                    "showing\ttrainer\tby\t(number)\t\t\t\t\t\t\n" +
                    "showing\ttrainer\tby\tid\t(number)\t\t\t\t\t\n" +
                    "showing\ttrainer\tid\t(number)\t\t\t\t\t\t\n" +
                    "showing\tundergrad\t(number)\t\t\t\t\t\t\t\n" +
                    "showing\tundergrad\tby\t(number)\t\t\t\t\t\t\n" +
                    "showing\tundergrad\tby\tid\t(number)\t\t\t\t\t\n" +
                    "showing\tundergrad\tid\t(number)\t\t\t\t\t\t\n" +
                    "transfer\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "transfer\tstudent\t\t\t\t\t\t\t\t\n" +
                    "transfer\tteacher\t\t\t\t\t\t\t\t\n" +
                    "transfer\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "transfer\tundergrad\t\t\t\t\t\t\t\t\n" +
                    "type\tall\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "type\tall\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "type\tall\tinstructor\tcourse\t(number)\t\t\t\t\t\n" +
                    "type\tall\tinstructor\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "type\tall\tinstructor\tcourse\tid\t(number)\t\t\t\t\n" +
                    "type\tall\tinstructor\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "type\tall\tinstructor\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "type\tall\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "type\tall\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "type\tall\tstudent\tcourse\t(number)\t\t\t\t\t\n" +
                    "type\tall\tstudent\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "type\tall\tstudent\tcourse\tid\t(number)\t\t\t\t\n" +
                    "type\tall\tstudent\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "type\tall\tstudent\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "type\tall\tstudent\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "type\tall\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "type\tall\tteacher\tcourse\t(number)\t\t\t\t\t\n" +
                    "type\tall\tteacher\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "type\tall\tteacher\tcourse\tid\t(number)\t\t\t\t\n" +
                    "type\tall\tteacher\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "type\tall\tteacher\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "type\tall\tteacher\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "type\tall\tthe\tcourse\t(number)\t\t\t\t\t\n" +
                    "type\tall\tthe\tinstructor\t(number)\t\t\t\t\t\n" +
                    "type\tall\tthe\tinstructor\tcourse\t(number)\t\t\t\t\n" +
                    "type\tall\tthe\tinstructor\tcourse\tid\t(number)\t\t\t\n" +
                    "type\tall\tthe\tinstructor\tcourse\tby\tid\t(number)\t\t\n" +
                    "type\tall\tthe\tinstructor\ton\tthe\tcourse\t(number)\t\t\n" +
                    "type\tall\tthe\tinstructor\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "type\tall\tthe\tinstructor\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "type\tall\tthe\tstudent\t(number)\t\t\t\t\t\n" +
                    "type\tall\tthe\tstudent\tcourse\t(number)\t\t\t\t\n" +
                    "type\tall\tthe\tstudent\tcourse\tid\t(number)\t\t\t\n" +
                    "type\tall\tthe\tstudent\tcourse\tby\tid\t(number)\t\t\n" +
                    "type\tall\tthe\tstudent\ton\tthe\tcourse\t(number)\t\t\n" +
                    "type\tall\tthe\tstudent\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "type\tall\tthe\tstudent\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "type\tall\tthe\tteacher\t(number)\t\t\t\t\t\n" +
                    "type\tall\tthe\tteacher\tcourse\t(number)\t\t\t\t\n" +
                    "type\tall\tthe\tteacher\tcourse\tid\t(number)\t\t\t\n" +
                    "type\tall\tthe\tteacher\tcourse\tby\tid\t(number)\t\t\n" +
                    "type\tall\tthe\tteacher\ton\tthe\tcourse\t(number)\t\t\n" +
                    "type\tall\tthe\tteacher\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "type\tall\tthe\tteacher\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "type\tall\tthe\ttrainer\t(number)\t\t\t\t\t\n" +
                    "type\tall\tthe\ttrainer\tcourse\t(number)\t\t\t\t\n" +
                    "type\tall\tthe\ttrainer\tcourse\tid\t(number)\t\t\t\n" +
                    "type\tall\tthe\ttrainer\tcourse\tby\tid\t(number)\t\t\n" +
                    "type\tall\tthe\ttrainer\ton\tthe\tcourse\t(number)\t\t\n" +
                    "type\tall\tthe\ttrainer\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "type\tall\tthe\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "type\tall\tthe\tundergrad\t(number)\t\t\t\t\t\n" +
                    "type\tall\tthe\tundergrad\tcourse\t(number)\t\t\t\t\n" +
                    "type\tall\tthe\tundergrad\tcourse\tid\t(number)\t\t\t\n" +
                    "type\tall\tthe\tundergrad\tcourse\tby\tid\t(number)\t\t\n" +
                    "type\tall\tthe\tundergrad\ton\tthe\tcourse\t(number)\t\t\n" +
                    "type\tall\tthe\tundergrad\ton\tthe\tcourse\tid\t(number)\t\n" +
                    "type\tall\tthe\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\n" +
                    "type\tall\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "type\tall\ttrainer\tcourse\t(number)\t\t\t\t\t\n" +
                    "type\tall\ttrainer\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "type\tall\ttrainer\tcourse\tid\t(number)\t\t\t\t\n" +
                    "type\tall\ttrainer\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "type\tall\ttrainer\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "type\tall\ttrainer\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "type\tall\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "type\tall\tundergrad\tcourse\t(number)\t\t\t\t\t\n" +
                    "type\tall\tundergrad\tcourse\tby\tid\t(number)\t\t\t\n" +
                    "type\tall\tundergrad\tcourse\tid\t(number)\t\t\t\t\n" +
                    "type\tall\tundergrad\ton\tthe\tcourse\t(number)\t\t\t\n" +
                    "type\tall\tundergrad\ton\tthe\tcourse\tid\t(number)\t\t\n" +
                    "type\tall\tundergrad\ton\tthe\tcourse\tby\tid\t(number)\t\n" +
                    "type\tall\t\t\t\t\t\t\t\t\n" +
                    "type\tcourse\t(number)\t\t\t\t\t\t\t\n" +
                    "type\tcourse\tby\t(number)\t\t\t\t\t\t\n" +
                    "type\tcourse\tby\tid\t(number)\t\t\t\t\t\n" +
                    "type\tcourse\tid\t(number)\t\t\t\t\t\t\n" +
                    "type\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "type\tinstructor\t(number)\t\t\t\t\t\t\t\n" +
                    "type\tinstructor\tby\t(number)\t\t\t\t\t\t\n" +
                    "type\tinstructor\tby\tid\t(number)\t\t\t\t\t\n" +
                    "type\tinstructor\tid\t(number)\t\t\t\t\t\t\n" +
                    "type\tjob\t\t\t\t\t\t\t\t\n" +
                    "type\tmy\tcourse\t\t\t\t\t\t\t\n" +
                    "type\tmy\tcourses\t\t\t\t\t\t\t\n" +
                    "type\tmy\tgradebook\t\t\t\t\t\t\t\n" +
                    "type\tmy\tjob\t\t\t\t\t\t\t\n" +
                    "type\tmy\tname\t\t\t\t\t\t\t\n" +
                    "type\tmy\ttask\t\t\t\t\t\t\t\n" +
                    "type\tmy\ttasks\t\t\t\t\t\t\t\n" +
                    "type\tstudent\t(number)\t\t\t\t\t\t\t\n" +
                    "type\tstudent\tby\t(number)\t\t\t\t\t\t\n" +
                    "type\tstudent\tby\tid\t(number)\t\t\t\t\t\n" +
                    "type\tstudent\tid\t(number)\t\t\t\t\t\t\n" +
                    "type\ttask\t\t\t\t\t\t\t\t\n" +
                    "type\ttasks\t\t\t\t\t\t\t\t\n" +
                    "type\tteacher\t(number)\t\t\t\t\t\t\t\n" +
                    "type\tteacher\tby\t(number)\t\t\t\t\t\t\n" +
                    "type\tteacher\tby\tid\t(number)\t\t\t\t\t\n" +
                    "type\tteacher\tid\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\tcourse\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\tcourse\tby\tid\t(number)\t\t\t\t\n" +
                    "type\tthe\tcourse\tid\t(number)\t\t\t\t\t\n" +
                    "type\tthe\tinstructor\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\tinstructor\tby\tid\t(number)\t\t\t\t\n" +
                    "type\tthe\tinstructor\tid\t(number)\t\t\t\t\t\n" +
                    "type\tthe\tstudent\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\tstudent\tby\tid\t(number)\t\t\t\t\n" +
                    "type\tthe\tstudent\tid\t(number)\t\t\t\t\t\n" +
                    "type\tthe\tteacher\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\tteacher\tby\tid\t(number)\t\t\t\t\n" +
                    "type\tthe\tteacher\tid\t(number)\t\t\t\t\t\n" +
                    "type\tthe\ttrainer\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\ttrainer\tby\tid\t(number)\t\t\t\t\n" +
                    "type\tthe\ttrainer\tid\t(number)\t\t\t\t\t\n" +
                    "type\tthe\tundergrad\t(number)\t\t\t\t\t\t\n" +
                    "type\tthe\tundergrad\tby\tid\t(number)\t\t\t\t\n" +
                    "type\tthe\tundergrad\tid\t(number)\t\t\t\t\t\n" +
                    "type\ttrainer\t(number)\t\t\t\t\t\t\t\n" +
                    "type\ttrainer\tby\t(number)\t\t\t\t\t\t\n" +
                    "type\ttrainer\tby\tid\t(number)\t\t\t\t\t\n" +
                    "type\ttrainer\tid\t(number)\t\t\t\t\t\t\n" +
                    "type\tundergrad\t(number)\t\t\t\t\t\t\t\n" +
                    "type\tundergrad\tby\t(number)\t\t\t\t\t\t\n" +
                    "type\tundergrad\tby\tid\t(number)\t\t\t\t\t\n" +
                    "type\tundergrad\tid\t(number)\t\t\t\t\t\t\n" +
                    "write\tall\t\t\t\t\t\t\t\t\n" +
                    "write\tcourse\t\t\t\t\t\t\t\t\n" +
                    "write\tgradebook\t\t\t\t\t\t\t\t\n" +
                    "write\tinstructor\t\t\t\t\t\t\t\t\n" +
                    "write\tjob\t\t\t\t\t\t\t\t\n" +
                    "write\tstudent\t\t\t\t\t\t\t\t\n" +
                    "write\ttask\t\t\t\t\t\t\t\t\n" +
                    "write\ttasks\t\t\t\t\t\t\t\t\n" +
                    "write\tteacher\t\t\t\t\t\t\t\t\n" +
                    "write\ttrainer\t\t\t\t\t\t\t\t\n" +
                    "write\tundergrad\t\t\t\t\t\t\t\t\n";
        }
    }
}