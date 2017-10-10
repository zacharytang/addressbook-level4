package seedu.address.model.person;

import java.util.HashMap;

/**
 * Class representing a person's module, to be used in Timetable
 */
public class Module {

    private String moduleCode;
    private HashMap<String, String> classSlots;

    public Module(String moduleCode) {
        this.moduleCode = moduleCode;
        classSlots = new HashMap<>();
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void addClassSlot(String slotType, String slotValue) {
        classSlots.put(slotType, slotValue);
    }

    public HashMap<String, String> getClassSlots() {
        return classSlots;
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
