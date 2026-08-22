package part1.prac2;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student("Alice");
        Student s2 = new Student("Bob");

        Teacher t1 = new Teacher("Mrs. Kim", "Math");

        Classroom c1 = new Classroom("3-A");
        Classroom.Schedule schedule = new Classroom.Schedule("09:00 ~ 15:00");
        Classroom.Seat seat = c1.new Seat(5);

        s1.introduce();
        t1.teach();
        c1.printClassInfo();

        System.out.println("총 학생 수: " + Student.studentCount);
    }
}