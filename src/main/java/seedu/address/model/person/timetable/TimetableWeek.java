package seedu.address.model.person.timetable;

import seedu.address.logic.parser.TimetableParser;

/**
 * Represents a full timetable for a week
 */
public class TimetableWeek {

    private static final int ARRAY_NUM_DAYS = 5;

    private TimetableDay[] days;

    public TimetableWeek() {
        days = new TimetableDay[ARRAY_NUM_DAYS];
        for (int i = 0; i < ARRAY_NUM_DAYS; i++) {
            days[i] = new TimetableDay();
        }
    }

    public boolean doesSlotHaveLesson(String day, String timing) {
        return days[TimetableParser.convertDay(day)].doesSlotHaveLesson(timing);
    }

    public void updateSlotWithLesson(String day, String startTime, String endTime) {
        days[TimetableParser.convertDay(day)].updateSlotsWithLesson(startTime, endTime);
    }
}
