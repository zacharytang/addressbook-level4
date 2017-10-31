package seedu.address.ui;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonSelectedEvent;
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
    @FXML
    private ImageView contactPhoto;

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

    /**
     * Set the default contact photo to the default person.
     */
    public void setDefaultContactPhoto() {
        String defaultPhotoPath = "src/main/resources/images/help_icon.png";
        File defaultPhoto = new File(defaultPhotoPath);
        URI defaultPhotoUri = defaultPhoto.toURI();
        Image defaultImage = new Image(defaultPhotoUri.toString());
        contactPhoto.setImage(defaultImage);
    }

    /**
     * Load the photo of the specified person.
     * @param person
     */
    public void loadPhoto(ReadOnlyPerson person) {
        String photoPath = person.getPhotoPath().value;
        File photo = new File(photoPath);
        URI photoUri = photo.toURI();
        Image image = new Image(photoUri.toString());

        contactPhoto.setImage(image);
    }

    @Subscribe
    private void handlePersonSelectedEvent(PersonSelectedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPerson(event.person);
    }
}
