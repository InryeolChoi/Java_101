package part1.prac2;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class Teacher {
    private String name;
    protected String subject;

    public void teach() {
        System.out.println(name + " 선생님이 " + subject + " 수업을 시작합니다.");
    }
}
