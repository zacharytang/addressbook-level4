package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author CindyTsai1
/**
 * Represents a Person's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBirthday(String)}
 */
public class Birthday {

    public static final String MESSAGE_BIRTHDAY_CONSTRAINTS =
            "Person's birthday should be in the format of DDMMYYYY, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String BIRTHDAY_VALIDATION_REGEX = "\\d{2}\\d{2}\\d{4}";

    public static final String DATE_FORMAT = "ddMMyyyy";

    public final String date;

    /**
     * Validates given birthday.
     *
     * @throws IllegalValueException if given birthday string is invalid.
     */
    public Birthday(String birthday) throws IllegalValueException {
        requireNonNull(birthday);
        String trimmedBirthday = birthday.trim();
        if (!isValidBirthday(trimmedBirthday)) {
            throw new IllegalValueException(MESSAGE_BIRTHDAY_CONSTRAINTS);
        }
        this.date = formatDate(birthday);
    }

    //@@author nbriannl
    /**
     * Formats the unformatted input birthday string into dd/mm/yyyy and
     * @return the formatted String
     */
    public static String formatDate (String unformatted) {
        DateFormat withoutFormat = new SimpleDateFormat("ddmmyyyy");
        DateFormat withFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date intermediateDate;
        try {
            intermediateDate = withoutFormat.parse(unformatted);
            String newDateString = withFormat.format(intermediateDate);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Removes the format from the date attribute
     * @return the unformatted String as {@code ddmmyyyy}
     */
    public String getUnformattedDate () {
        DateFormat withFormat = new SimpleDateFormat("dd/mm/yyyy");
        DateFormat withoutFormat = new SimpleDateFormat("ddmmyyyy");
        Date intermediateDate;
        try {
            intermediateDate = withFormat.parse(date);
            String newDateString = withoutFormat.format(intermediateDate);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //@@author CindyTsai1
    /**
     * Returns true if a given string is a valid person birthday.
     */
    public static boolean isValidBirthday(String test) {
        if (test.equals("")) {
            return true;
        }
        if (test.matches(BIRTHDAY_VALIDATION_REGEX)) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(test);
                return true;
            } catch (ParseException pe) {
                return false;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthday // instanceof handles nulls
                && this.date.equals(((Birthday) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

}
