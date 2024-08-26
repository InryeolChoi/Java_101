package part1.withMultipleClass;

public class MultipleClass {
    public static void main(String[] args) {
        HelperClass hClass = new HelperClass("안녕");
        hClass.help();

        UtilityClass uClass = new UtilityClass(5);
        uClass.performUtility();

        withOtherFile wClass = new withOtherFile("하이하이");
        System.out.println(wClass.getStr());
    }
}

class HelperClass {
    private String name;

    public HelperClass(String name) {
        this.name = name;
    }

    void help() {
        System.out.println("HelperClass");
    }
}

class UtilityClass {
    private int value;

    public UtilityClass(int value) {
        this.value = value;
    }

    void performUtility() {
        System.out.println("UtilityClass is performing " + value);
    }
}