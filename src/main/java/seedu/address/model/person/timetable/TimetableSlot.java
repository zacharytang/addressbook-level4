package seedu.address.model.person.timetable;

/**
 * Represents a single slot in a timetable
 */
public class TimetableSlot {

    private boolean hasLesson;

    public TimetableSlot() {
        this.hasLesson = false;
    }

    public void setLesson() {
        hasLesson = true;
    }

    public boolean hasLesson() {
        return hasLesson;
    }
}
