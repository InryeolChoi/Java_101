package part1.prac2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
public class Classroom {
    private String className;
    private ArrayList<Student> students;

    public Classroom(String className) {
        this.className = className;
        students = new ArrayList<>();
    }

    public void addStudents(Student student) {
        Student.studentCount++;
        students.add(student);
    }

    @AllArgsConstructor
    public static class Schedule {
        private String time;
    }

    @AllArgsConstructor
    public class Seat {
        private int seats;
    }

    public void printClassInfo() {
        System.out.println("학급 이름: " + className);
        System.out.println("학생 명단:");
        for (Student student : students) {
            System.out.println("- " + student.getName());
        }
    }
}
