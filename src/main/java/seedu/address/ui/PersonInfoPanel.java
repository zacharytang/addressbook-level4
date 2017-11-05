package seedu.address.ui;

//import static seedu.address.logic.commands.PhotoCommand.DEFAULT_PHOTO_PATH;

import static seedu.address.logic.commands.PhotoCommand.DEFAULT_PHOTO_PATH;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.PersonSelectedEvent;
import seedu.address.model.person.ReadOnlyPerson;

//@@author April0616

/**
 * A UI component that displays a person's data on the main panel
 */
public class PersonInfoPanel extends UiPart<Region> {

    private static final String FXML = "PersonInfoPanel.fxml";
    private static String DEFAULT_PHOTO_PATH = "/images/defaultPhoto.jpg";
    private ReadOnlyPerson person;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Circle photoCircle;
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


    public PersonInfoPanel() {
        super(FXML);
        this.person = null;
        loadDefaultPerson();

        registerAsAnEventHandler(this);
    }

    /**
     * Loads the default person when the app is first started
     */
    private void loadDefaultPerson() {
        name.setText("Person X");
        gender.setText("");
        matricNo.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        birthday.setText("");
        remark.setText("");

        setDefaultContactPhoto();
    }

    /**
     * Updates info with person selected
     */
    private void loadPerson(ReadOnlyPerson person) {
        this.person = person;

        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        gender.textProperty().bind(Bindings.convert(person.genderProperty()));
        matricNo.textProperty().bind(Bindings.convert(person.matricNoProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        remark.textProperty().bind(Bindings.convert(person.remarkProperty()));

        loadPhoto(person);
    }

    //@@author April0616
    /**
     * Set the default contact photo to the default person.
     */
    public void setDefaultContactPhoto() {
        /*String defaultPhotoPath = "/images/defaultPhoto.jpg";
        File defaultPhoto = new File(defaultPhotoPath);
        URI defaultPhotoUri = defaultPhoto.toURI();
        Image defaultImage = new Image(defaultPhotoUri.toString());*/

        //photoCircle.setFill(new ImagePattern(defaultImage));

        //File defaultPhoto = new File(defaultPhotoPath);
        Image defaultImage = new Image(MainApp.class.getResourceAsStream(DEFAULT_PHOTO_PATH));
        photoCircle.setFill(new ImagePattern(defaultImage));

    }

    /**
     * Load the photo of the specified person.
     * @param person
     */
    public void loadPhoto(ReadOnlyPerson person) {
        String prefix = "src/main/resources";
        String photoPath = person.getPhotoPath().value.substring(prefix.length());

        if (photoPath.equals(DEFAULT_PHOTO_PATH)) {
            System.out.println(person.getGender().toString());
            if (person.getGender().toString().equals("Male")) {
                photoPath = "/images/default_male.jpg";
            } else {
                photoPath = "/images/default_female.jpg";
            }
        }

        Image image = new Image(MainApp.class.getResourceAsStream(photoPath));
        photoCircle.setFill(new ImagePattern(image));

      /*  File photo = new File(photoPath);
        URI photoUri = photo.toURI();
        Image image = new Image(photoUri.toString());

        photoCircle.setFill(new ImagePattern(image));*/
    }

    //@@author


    //@@author zacharytang
    @Subscribe
    private void handlePersonSelectedEvent(PersonSelectedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPerson(event.person);
    }

    @Subscribe
    private void handlePersonPanelSelectionChangeEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPerson(event.getNewSelection().person);
    }
}
