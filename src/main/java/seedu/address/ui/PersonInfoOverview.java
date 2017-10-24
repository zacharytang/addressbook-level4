package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * A UI component that displays a person's data on the main panel
 */
public class PersonInfoOverview extends UiPart<Region> {

    private static final String FXML = "PersonInfoOverview.fxml";
    private ReadOnlyPerson person;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Label name;
    @FXML
    private Label gender;
    @FXML
    private Label matricNo;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label remark;

    public PersonInfoOverview() {
        super(FXML);

        this.person = null;
        loadDefaultPerson();
        registerAsAnEventHandler(this);
    }

    /**
     * Loads the default person when the app is first started
     */
    private void loadDefaultPerson() {
        name.setText("No person selected");
        gender.setText("");
        matricNo.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        birthday.setText("");
        remark.setText("");
    }

    /**
     * Updates info with person selected
     * @param person
     */
    private void loadPerson(ReadOnlyPerson person) {
        this.person = person;

        name.setText(person.getName().toString());
        gender.setText(person.getGender().toString());
        matricNo.setText(person.getMatricNo().toString());
        phone.setText(person.getPhone().toString());
        address.setText(person.getAddress().toString());
        email.setText(person.getEmail().toString());
        birthday.setText(person.getBirthday().toString());
        remark.setText(person.getRemark().toString());
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPerson(event.getNewSelection().person);
    }
}
