package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GenderTest {

    @Test
    public void isValidGender() {
        // invalid Gender
        assertFalse(Gender.isValidGender("")); // empty string
        assertFalse(Gender.isValidGender(" ")); // spaces only
        assertFalse(Gender.isValidGender("^")); // only non-alphanumeric characters
        assertFalse(Gender.isValidGender("peter*")); // contains non-alphanumeric characters
        assertFalse(Gender.isValidGender("apple")); // unrelated description
        assertFalse(Gender.isValidGender("male")); // first letter not uppercase

        // valid Gender
        assertTrue(Gender.isValidGender("Male")); // GENDER_VALIDATION_WORD1
        assertTrue(Gender.isValidGender("Female")); // GENDER_VALIDATION_WORD2
    }
}
