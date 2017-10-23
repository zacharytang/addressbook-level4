package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BirthdayTest {

    @Test
    public void isValidBirthday() {
        // invalid birthdays
        assertFalse(Birthday.isValidBirthday("")); // empty string
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only
        assertFalse(Birthday.isValidBirthday("1324")); // less than 8 digits
        assertFalse(Birthday.isValidBirthday("dgd")); // non-numeric
        assertFalse(Birthday.isValidBirthday("23/05/1997")); // invalid slash
        assertFalse(Birthday.isValidBirthday("23 05 1997")); // invalid space

        // valid birthdays
        assertTrue(Birthday.isValidBirthday("23051997"));
    }
}
