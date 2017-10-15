package seedu.address.model.person.timetable;

/**
 * Represents a lesson that a module has
 */
public class Lesson {

    private final String classNo;
    private final String lessonType;
    private final String weekType;
    private final String day;
    private final String startTime;
    private final String endTime;

    public Lesson(String classNo, String lessonType, String weekType,
                  String day, String startTime, String endTime) {
        this.classNo = classNo;
        this.lessonType = lessonType;
        this.weekType = weekType;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getClassNo() {
        return classNo;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getWeekType() {
        return weekType;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Lesson)) {
            return false;
        }

        Lesson otherLesson = (Lesson) other;
        return this.classNo.equals(otherLesson.getClassNo())
                && this.lessonType.equals(otherLesson.getLessonType())
                && (this.day.equals(otherLesson.getDay()));
    }
}
