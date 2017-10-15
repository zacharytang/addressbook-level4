package seedu.address.commons.util;

import static org.junit.Assert.assertArrayEquals;

import static seedu.address.commons.util.TimetableParserUtil.MESSAGE_INVALID_SHORT_URL;
import static seedu.address.model.person.timetable.Timetable.MESSAGE_TIMETABLE_URL_CONSTRAINTS;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.timetable.TimetableWeek;

public class TimetableParserUtilTest {

    private static final String INVALID_URL = "https://www.google.com";
    private static final String INVALID_URL_NOT_URL = "hello123";
    private static final String INVALID_SHORT_URL = "http://modsn.us/abc";

    private static final String VALID_URL_EMPTY = "http://modsn.us/5tN3z";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseUrl_invalidUrl_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_TIMETABLE_URL_CONSTRAINTS);
        TimetableParserUtil.parseUrl(INVALID_URL);
    }

    @Test
    public void parseUrl_notUrl_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_TIMETABLE_URL_CONSTRAINTS);
        TimetableParserUtil.parseUrl(INVALID_URL_NOT_URL);
    }

    @Test
    public void parseUrl_invalidShortUrl_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_SHORT_URL);
        TimetableParserUtil.parseUrl(INVALID_SHORT_URL);
    }

    @Test
    public void parseUrl_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        TimetableParserUtil.parseUrl(null);
    }

    @Test
    public void parseUrl_validUrlEmptyTimetable_success() throws Exception {
        TimetableWeek[] emptyTimetable = {
            new TimetableWeek(),
            new TimetableWeek()
        };

        assertArrayEquals(TimetableParserUtil.parseUrl(VALID_URL_EMPTY), emptyTimetable);
    }
}
