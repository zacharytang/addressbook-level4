package seedu.address.model.person.timetable;

import seedu.address.logic.parser.TimetableParserUtil;

/**
 * Represents a single day in a timetable
 */
public class TimetableDay {

    private static final int ARRAY_NUM_TIMESLOTS = 32;

    private TimetableSlot[] slots;

    public TimetableDay() {
        slots = new TimetableSlot[ARRAY_NUM_TIMESLOTS];
        for (int i = 0; i < ARRAY_NUM_TIMESLOTS; i++) {
            slots[i] = new TimetableSlot();
        }
    }

    /**
     * Sets all slots between two timings to have lessons
     */
    public void updateSlotsWithLesson(String startTime, String endTime) {
        int startTimeIndex = TimetableParserUtil.parseStartEndTime(startTime);
        int endTimeIndex = TimetableParserUtil.parseStartEndTime(endTime);

        for (int i = startTimeIndex; i < endTimeIndex; i++) {
            slots[i].setLesson();
        }
    }

    public boolean doesSlotHaveLesson(String timing) {
        return slots[TimetableParserUtil.parseStartEndTime(timing)].hasLesson();
    }

    private TimetableSlot getTimetableSlot(int index) {
        return slots[index];
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TimetableDay)) {
            return false;
        }

        for (int i = 0; i < ARRAY_NUM_TIMESLOTS; i++) {
            if (!this.slots[i].equals(((TimetableDay) other).getTimetableSlot(i))) {
                return false;
            }
        }

        return true;
    }
}
