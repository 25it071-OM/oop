import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    String name;

    @NotBlank
    @MaxLength(30)
    String email;

    @MaxLength(10)
    String password;

    SignupForm(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

public class FormValidator {

    static List<String> validate(Object obj)
            throws IllegalAccessException {

        List<String> errors = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            String text = value == null ? "" : value.toString();

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (text.trim().isEmpty()) {
                    errors.add(field.getName() + " cannot be blank");
                }
            }

            if (field.isAnnotationPresent(MaxLength.class)) {
                MaxLength max = field.getAnnotation(MaxLength.class);

                if (text.length() > max.value()) {
                    errors.add(field.getName()
                            + " exceeds maximum length " + max.value());
                }
            }
        }

        return errors;
    }

    public static void main(String[] args)
            throws IllegalAccessException {

        SignupForm form = new SignupForm(
                "", "longemailaddress@example.com", "12345678901");

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid");
        } else {
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}