package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.PersonAddressDisplayDirectionsEvent;
import seedu.address.commons.events.model.PersonAddressDisplayMapEvent;
import seedu.address.commons.events.ui.PersonSelectedEvent;

/**
 * Container for both browser panel and person information panel
 */
public class InfoPanel extends UiPart<Region> {

    private static final String FXML = "InfoPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private BrowserPanel browserPanel;
    private PersonInfoOverview infoOverview;

    @FXML
    private StackPane browserPlaceholder;

    @FXML
    private StackPane infoOverviewPlaceholder;


    public InfoPanel() {
        super(FXML);

        registerAsAnEventHandler(this);
        browserPanel = new BrowserPanel();
        browserPlaceholder.getChildren().add(browserPanel.getRoot());

        infoOverview = new PersonInfoOverview();
        infoOverviewPlaceholder.getChildren().add(infoOverview.getRoot());
    }

    public void freeResources() {
        browserPanel.freeResources();
    }

    @Subscribe
    public void handlePersonSelectedEvent(PersonSelectedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        infoOverviewPlaceholder.toFront();
    }

    @Subscribe
    public void handlePersonAddressDisplayDirectionsEvent(PersonAddressDisplayDirectionsEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        browserPlaceholder.toFront();
    }

    @Subscribe
    public void handlePersonAddressDisplayMapEvent(PersonAddressDisplayMapEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        browserPlaceholder.toFront();
    }
}
