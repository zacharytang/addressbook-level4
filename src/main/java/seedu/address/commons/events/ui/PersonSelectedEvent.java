package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyPerson;

//@@author zacharytang
/**
 * Represents a person being selected
 */
public class PersonSelectedEvent extends BaseEvent {

    public final ReadOnlyPerson person;
    public final int targetIndex;

    public PersonSelectedEvent(ReadOnlyPerson person, int targetIndex) {
        this.person = person;
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return "Person selected: " + person.getName().toString();
    }
}
