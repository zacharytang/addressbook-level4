package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author April0616
/**
 * Represents a Person's gender in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGender(String)}
 */
public class Gender {

    public static final String MESSAGE_GENDER_CONSTRAINTS =
            "Person gender should be a string of either 'Male', 'Female', 'male', 'female', 'm', 'f' or 'M', 'F'";
    public static final String GENDER_VALIDATION_WORD1 = "Male";
    public static final String GENDER_VALIDATION_WORD2 = "Female";

    public final String value;

    /**
     * Validates given gender.
     * If gender string is 'Male' or 'Female', it remains the same.
     * Converts 'M' into 'Male', 'F' into 'Female'.
     *
     * @throws IllegalValueException if given gender string is invalid.
     */
    public Gender(String gender) throws IllegalValueException {
        requireNonNull(gender);
        String trimmedGender = gender.trim();
        if (trimmedGender.equals("M") || trimmedGender.equals("male") || trimmedGender.equals("m")) {
            trimmedGender = "Male";
        } else if (trimmedGender.equals("F") || trimmedGender.equals("female") || trimmedGender.equals("f")) {
            trimmedGender = "Female";
        }
        if (!isValidGender(trimmedGender)) {
            throw new IllegalValueException(MESSAGE_GENDER_CONSTRAINTS);
        }
        this.value = trimmedGender;
    }

    /**
     * Returns if a given string is a valid person gender.
     */
    public static boolean isValidGender(String test) {
        return test.equals(GENDER_VALIDATION_WORD1) || test.equals(GENDER_VALIDATION_WORD2) || test.equals("");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Gender // instanceof handles nulls
                && this.value.equals(((Gender) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
