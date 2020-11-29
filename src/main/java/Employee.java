import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Employee {

    String name;
    String password;
    String username;
    String email;

    Employee(String name, String password) {
        this.name = name;
        this.password = password;

        if (!checkName(name)) {
            this.username = "default";
            this.email = "user@oracleacademy.Test";
        } else {
            setUsername(name);
        }

        if (isValidPassword(password)) {
            this.password = password;
        } else if (!isValidPassword(password))
            this.password = "pw";
    }

    void setUsername(String name) {
        // Setting username
        char firstInit = name.charAt(0);      // First initial, first name
        String lastName = name.substring(name.indexOf(" ") + 1);
        String username = firstInit + lastName;
        this.username = username.toLowerCase();

        // Setting email
        String firstName = name.substring(0, name.indexOf(" "));
        String email = firstName + "." + lastName + "@oracleacademy.Test";
        this.email = email.toLowerCase();
    }

    boolean isValidPassword(String password) {
        boolean isLowerCase = false;
        boolean isUpperCase = false;
        boolean isSpecialCharacter = false;

        // Check for lower case
        String regXLowerCase = ".*[a-z].*";
        Pattern patternLower = Pattern.compile(regXLowerCase);
        Matcher matcherLower = patternLower.matcher(password);
        if (matcherLower.matches())
            isLowerCase = true;

        // check for upper case
        String regXUpperCase = ".*[A-Z].*";
        Pattern patternUpper = Pattern.compile(regXUpperCase);
        Matcher matcherUpper = patternUpper.matcher(password);
        if (matcherUpper.matches())
            isUpperCase = true;

        // check for special characters
        String regXSpecChar = ".*[`~!@#$%^&*()\\\\-_=+\\\\|{};:'\",<.>/?].*";
        Pattern patternSpecial = Pattern.compile(regXSpecChar);
        Matcher matcherSpecial = patternSpecial.matcher(password);
        if (matcherSpecial.matches())
            isSpecialCharacter = true;

        return isLowerCase && isUpperCase && isSpecialCharacter;

    }

    boolean checkName(String name) {
        return name.contains(" ");
    }

    @Override
    public String toString() {

        return "Employee Details" +
                "\nName : " + this.name +
                "\nUsername : " + this.username +
                "\nEmail : " + this.email +
                "\nInitial Password : " + this.password;
    }
}
