package seedu.address.model.person;

import java.util.ArrayList;

/**
 * Class representing a person's module, to be used in Timetable
 */
public class Module {

    private String moduleCode;
    private ArrayList<Lesson> lessons;

    public Module(String moduleCode) {
        this.moduleCode = moduleCode;
        this.lessons = new ArrayList<>();
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Module)) {
            return false;
        }

        Module otherMod = (Module) other;
        return this.getModuleCode().equals(otherMod.getModuleCode());
    }
}
