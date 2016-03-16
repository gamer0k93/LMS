package course.project;

import java.io.*;
import java.util.Map;

/**
 * Created by KOT on 29.02.2016.
 */
public class SerializationClass implements Serializable{

    public void serialization(String nameFile){
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("src\\course\\project\\Resources\\SerializationFile\\" + nameFile + ".ser"))){
            Storage storage = (Storage) ois.readObject();

            LearningManagementSystem.setAllCourse(storage.getAllCourse());
            LearningManagementSystem.setAllStudent(storage.getAllStudent());
            LearningManagementSystem.setAllTrainer(storage.getAllTrainer());
        } catch (ClassNotFoundException | IOException ignored) {   }
    }
    public void deserialization(String nameFile){
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("src\\course\\project\\Resources\\SerializationFile\\" + nameFile + ".ser"))){
                oos.writeObject(new Storage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class Storage implements Serializable{
        private Map<Integer, Student> allStudent = LearningManagementSystem.getAllStudent();
        private Map<Integer, Trainer> allTrainer = LearningManagementSystem.getAllTrainer();
        private Map<Integer, Course>  allCourse  = LearningManagementSystem.getAllCourse();

        public Map<Integer, Student> getAllStudent() {
            return allStudent;
        }
        public Map<Integer, Trainer> getAllTrainer() {
            return allTrainer;
        }
        public Map<Integer, Course> getAllCourse() {
            return allCourse;
        }
    }
}
