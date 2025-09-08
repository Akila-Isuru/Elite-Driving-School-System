package lk.ijse.elitedrivingschoolsystem.bo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern CONTACT_NUMBER_PATTERN = Pattern.compile("^\\d{10}$");

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidContactNumber(String contactNumber) {
        if (contactNumber == null) {
            return false;
        }
        Matcher matcher = CONTACT_NUMBER_PATTERN.matcher(contactNumber);
        return matcher.matches();
    }
}
