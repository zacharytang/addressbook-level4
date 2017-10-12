package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.TimetableParser;

/**
 * Represents a person's timetable in the address book
 * Guarantees: immutable
 */
public class Timetable {

    private static final String NUSMODS_SHORT = "modsn.us";
    private static final String NUSMODS_HOST = "nusmods.com";
    private static final String URL_HOST_REGEX = "\\/\\/.*?\\/";

    private static final String MESSAGE_INVALID_TIMETABLE_URL =
            "Timetable URLs should be either a full NUSMods url or a shortened URL";

    public final String value;

    public Timetable(String url) throws IllegalValueException {
        requireNonNull(url);
        String trimmedUrl = url.trim();
        if (!isValidUrl(trimmedUrl)) {
            throw new IllegalValueException(MESSAGE_INVALID_TIMETABLE_URL);
        }
        this.value = trimmedUrl;
    }

    /**
     * Returns if a url is a valid NUSMods url
     */
    public static boolean isValidUrl(String test) throws IllegalValueException {
        Matcher matcher = Pattern.compile(URL_HOST_REGEX).matcher(test);
        if (!matcher.find()) {
            return false;
        }

        String hostName = matcher.group()
                .substring(2, matcher.group().length() - 1);

        if (!(hostName.equals(NUSMODS_SHORT) || hostName.equals(NUSMODS_HOST))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Timetable // instanceof handles nulls
                && this.value.equals(((Timetable) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
