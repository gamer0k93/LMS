package course.project;

import java.util.Map;

/**
 * Created by KOT on 28.02.2016.
 */
public interface Person extends MyClass{
    public Map<Integer, Course> getCourse();
    public void addCourse(Course course);
    public void removeCourse(Course course);
}
