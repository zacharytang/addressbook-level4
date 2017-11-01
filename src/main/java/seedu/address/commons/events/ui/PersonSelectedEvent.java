package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyPerson;

//@@author zacharytang
/**
 * Represents a person being selected
 */
public class PersonSelectedEvent extends BaseEvent {

    public final ReadOnlyPerson person;

    public PersonSelectedEvent(ReadOnlyPerson person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Person selected: " + person.getName().toString();
    }
}
