package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author April0616
/**
 * Represents a Person's gender in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidInput(String)}
 */
public class Gender {

    public static final String MESSAGE_GENDER_CONSTRAINTS =
            "Person gender should be a case-insensitive string of either 'male', 'female', or 'm', 'f'";

    public static final String VALID_MALE_FIRST_WORD = "male";
    public static final String VALID_MALE_SECOND_WORD = "m";
    public static final String VALID_FEMALE_FIRST_WORD = "female";
    public static final String VALID_FEMALE_SECOND_WORD = "f";
    public static final String VALID_GENDER_UNSPECIFIED = "";

    public final String value;

    /**
     * Validates given gender and sets person's gender accordingly.
     * Accepts input (case-insensitive): male, female, m, f
     * @throws IllegalValueException if the given gender string is invalid.
     */
    public Gender(String gender) throws IllegalValueException {
        requireNonNull(gender);
        String trimmedGender = gender.trim();

        if (!isValidInput(trimmedGender)) {
            throw new IllegalValueException(MESSAGE_GENDER_CONSTRAINTS);
        }

        String ignoredCaseGender = trimmedGender.toLowerCase();
        if (ignoredCaseGender.equals(VALID_MALE_FIRST_WORD)
                || ignoredCaseGender.equals(VALID_MALE_SECOND_WORD)) {
            value = "Male";
        } else if (ignoredCaseGender.equals(VALID_FEMALE_FIRST_WORD)
                || ignoredCaseGender.equals(VALID_FEMALE_SECOND_WORD)) {
            value = "Female";
        } else {
            value = "";
        }
    }

    /**
     * Returns if a given input string is a valid person gender.
     */
    public static boolean isValidInput(String inputGender) {
        String ignoredCaseInputGender = inputGender.toLowerCase();
        return ignoredCaseInputGender.equals(VALID_MALE_FIRST_WORD)
                || ignoredCaseInputGender.equals(VALID_MALE_SECOND_WORD)
                || ignoredCaseInputGender.equals(VALID_FEMALE_FIRST_WORD)
                || ignoredCaseInputGender.equals(VALID_FEMALE_SECOND_WORD)
                || ignoredCaseInputGender.equals(VALID_GENDER_UNSPECIFIED);
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
