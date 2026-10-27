import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {
    @Column(name = "Name")
    String name;

    @Column(name = "Age")
    int age;

    @Column(name = "City")
    String city;

    public String toString() {
        return name + " " + age + " " + city;
    }
}

public class ColumnMapper {

    static Student map(String[] header, String[] data)
            throws Exception {

        Student student = new Student();

        for (Field field : Student.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            int index = -1;

            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(column.name())) {
                    index = i;
                    break;
                }
            }

            field.setAccessible(true);

            if (index == -1 || index >= data.length) {
                continue;
            }

            if (field.getType() == String.class) {
                field.set(student, data[index]);
            } else if (field.getType() == int.class) {
                field.setInt(student, Integer.parseInt(data[index]));
            }
        }

        return student;
    }

    public static void main(String[] args) throws Exception {

        String[] header = {"Name", "Age", "City"};
        String[] data = {"Om", "20", "Navsari"};

        Student student = map(header, data);

        System.out.println(student);
    }
}