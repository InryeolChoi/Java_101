package part1.prac2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
    private String name;
    private int GPA;

    @Getter
    public static int studentCount;
    // static 변수에 lombok을 이용한 Getter를 만들려면 직접 위에 붙여야 함.

    public Student(String name) {
        this.name = name;
    }

    public void introduce() {
        System.out.println("My name is " + name);
    }

    private void calculateGPA() {
        System.out.println("My gpa is " + GPA);
    }
}
