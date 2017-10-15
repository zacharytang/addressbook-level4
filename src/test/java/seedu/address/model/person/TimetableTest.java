package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.person.timetable.Timetable;

public class TimetableTest {

    @Test
    public void isValidUrl() {
        // invalid urls
        assertFalse(Timetable.isValidUrl("")); // empty string
        assertFalse(Timetable.isValidUrl(" ")); // spaces only
        assertFalse(Timetable.isValidUrl("abcde12345")); // string
        assertFalse(Timetable.isValidUrl("https://www.youtube.com/")); // other url
        assertFalse(Timetable.isValidUrl("http://modsn.us")); // invalid request
        assertFalse(Timetable.isValidUrl("http://mods.us/TDSEt")); // invalid domain name

        // valid urls
        assertTrue(Timetable.isValidUrl("http://modsn.us/TEt")); // bad short url, but still valid
        assertTrue(Timetable.isValidUrl("http://modsn.us/TDSEt")); // valid url
        assertTrue(Timetable.isValidUrl("http://modsn.us/5tN3z")); // valid url, empty timetable
    }
}
