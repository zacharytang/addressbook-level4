# nbriannl
###### \java\seedu\address\commons\events\model\PersonAddressDisplayDirectionsEvent.java
``` java
/** Indicates a person's address as a map is to be displayed*/
public class PersonAddressDisplayDirectionsEvent extends BaseEvent {

    public final ReadOnlyPerson person;
    public final Address address;

    public PersonAddressDisplayDirectionsEvent(ReadOnlyPerson person, Address address) {
        this.person = person;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Displaying location of " + person.getName() + ": " + person.getAddress()
                + " from " + address.toString();
    }
}
```
###### \java\seedu\address\commons\events\model\PersonAddressDisplayMapEvent.java
``` java
/** Indicates a person's address as a map is to be displayed*/
public class PersonAddressDisplayMapEvent extends BaseEvent {

    public final ReadOnlyPerson person;

    public PersonAddressDisplayMapEvent (ReadOnlyPerson person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Displaying location of " + person.getName() + ": " + person.getAddress();
    }
}
```
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
    public DeleteCommand(Set<Tag> targetTags) {
        this.targetIndexes = null;
        this.targetTags = targetTags;
    }

```
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        // this code block is command execution for delete [indexes]
        if (targetTags == null && targetIndexes != null) {
            List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
            ArrayList<ReadOnlyPerson> deletePersonList = new ArrayList<>();

            for (Index index : targetIndexes) {
                if (index.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                ReadOnlyPerson personToDelete = lastShownList.get(index.getZeroBased());
                deletePersonList.add(personToDelete);
            }

            try {
                model.deletePersons(deletePersonList);
            } catch (PersonNotFoundException pnfe) {
                assert false : "One of the target persons is missing";
            }

            //return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS));
            return new CommandResult(generateResultMsg(deletePersonList));

        } else { // this code block is command execution for delete t/[tag...]
            ArrayList<Tag> arrayTags = new ArrayList<Tag>(targetTags);
            List<Tag> listOfExistingTags = model.getAddressBook().getTagList();

            if (!listOfExistingTags.containsAll(arrayTags)) {
                throw new CommandException(Messages.MESSAGE_INVALID_TAG_PROVIDED);
            }

            for (Tag tagToBeDeleted: arrayTags) {
                try {
                    model.deleteTag(tagToBeDeleted);
                } catch (TagNotFoundException tnfe) {
                    assert false : "[Delete Tag] A tag is not found";
                } catch (DuplicatePersonException dpe) {
                    assert false : "[Delete Tag] A duplicate person is there";
                } catch (PersonNotFoundException pnfe) {
                    assert false : "[Delete Tag] A person not found";
                }
            }

            return new CommandResult(MESSAGE_DELETE_TAG_SUCCESS);
        }
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.isEditForPerson = true;
        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
        this.oldTag = null;
        this.newTag = null;
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     *
     * @param oldTag the old tag to be replaced by the new tag
     * @param newTag that will replace the old tag
     */
    public EditCommand(Tag oldTag, Tag newTag) {
        requireNonNull(oldTag);
        requireNonNull(newTag);

        this.isEditForPerson = false;
        this.index = null;
        this.editPersonDescriptor = null;
        this.oldTag = oldTag;
        this.newTag = newTag;
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (isEditForPerson) {
            List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
            Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

            try {
                model.updatePerson(personToEdit, editedPerson);
            } catch (DuplicatePersonException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            }
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
        } else {

            try {
                model.editTag(oldTag, newTag);
            } catch (DuplicatePersonException dpe) {
                throw new AssertionError("Updating the tags on one person cannot possibly make the person"
                        + " identical to another person.");
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            } catch (TagNotFoundException tgne) {
                throw new CommandException(MESSAGE_NONEXISTENT_TAG);
            }
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(String.format(MESSAGE_EDIT_TAG_SUCCESS, oldTag));
        }
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;

        if (isEditForPerson) {
            return  isEditForPerson == e.isEditForPerson
                    && index.equals(e.index)
                    && editPersonDescriptor.equals(e.editPersonDescriptor);
        } else {
            return isEditForPerson == e.isEditForPerson
                    && oldTag.equals(e.oldTag)
                    && newTag.equals(e.newTag);
        }
    }

```
###### \java\seedu\address\logic\commands\GMapsCommand.java
``` java
/**
 * Selects a person identified using it's last displayed index from the address book.
 */
public class GMapsCommand extends Command {

    public static final String COMMAND_WORD = "gmaps";
    public static final String COMMAND_ALIAS = "g";
    public static final String COMMAND_SECONDARY_ONE = "map";
    public static final String COMMAND_SECONDARY_TWO = "maps";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Opens a Google Maps view of a person’s address.\n"
            + " If you specify an address, you can find directions from the address to that person's address.\n"
            + "Format: " + COMMAND_WORD + " INDEX [a/ADDRESS]\n"
            + "Example: " + COMMAND_WORD + " 1 \n"
            + "         " + COMMAND_WORD + " 1 a/Blk 123, Yishun 75";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Showing Map View of %1$s 's address";
    public static final String MESSAGE_DIRECTIONS_TO_PERSON_SUCCESS = "Showing directions to %1$s";

    private final Index targetIndex;
    private final Address targetAddress;

    public GMapsCommand (Index targetIndex, Address targetAddress) {
        this.targetIndex = targetIndex;
        this.targetAddress = targetAddress;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToShowMap = lastShownList.get(targetIndex.getZeroBased());

        if (targetAddress != null) {
            model.showDirectionsTo(personToShowMap, targetAddress);
            return new CommandResult(String.format(MESSAGE_DIRECTIONS_TO_PERSON_SUCCESS, personToShowMap.getName()));
        } else {
            model.showMapOf(personToShowMap);
            return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, personToShowMap.getName()));
        }



    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GMapsCommand)) {
            return false;
        }

        // state check
        GMapsCommand e = (GMapsCommand) other;

        return Objects.equals(this.targetIndex, e.targetIndex)
                && Objects.equals(this.targetAddress, e.targetAddress);

    }
}
```
###### \java\seedu\address\logic\parser\DeleteCommandParser.java
``` java
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);
        String preamble = argMultimap.getPreamble();

        if (preamble.equals("")) { // code block for delete for a tag
            try {
                if (arePrefixesPresent(argMultimap, PREFIX_TAG)) {
                    Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                    return new DeleteCommand(tagList);
                }
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        } else if (preamble.matches("-?\\d+")) { // code block for delete for a person
            try {
                Index index = ParserUtil.parseIndex(args);
                return new DeleteCommand(index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        } else if (preamble.matches("((-?\\d([\\s+]*)\\,([\\s+]*)(?=-?\\d))|-?\\d)+")) {
            //code block for delete multiple persons
            try {
                ArrayList<Index> deletePersons = ParserUtil.parseIndexes(args);
                return new DeleteCommand(deletePersons);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        }
        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

```
###### \java\seedu\address\logic\parser\GMapsCommandParser.java
``` java
/**
 * Parses input arguments and creates a new GMapsCommand object
 */
public class GMapsCommandParser implements Parser<GMapsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the GMapsCommand
     * and returns an GMapsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GMapsCommand parse(String args) throws ParseException {
        Address address = null;
        Index index;
        requireNonNull(args);
        ArgumentMultimap argsMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADDRESS);
        try {
            index = ParserUtil.parseIndex(argsMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, GMapsCommand.MESSAGE_USAGE));
        }

        if (arePrefixesPresent(argsMultimap, PREFIX_ADDRESS)) {
            try {
                address = ParserUtil.parseAddress(argsMultimap.getValue(PREFIX_ADDRESS)).get();
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }
        }

        return new GMapsCommand(index, address);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a single {@code Optional<String> tag} into an {@code Optional<Tag>} if {@code tag} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Tag> parseSingleTag(Optional<String> tag) throws IllegalValueException {
        requireNonNull(tag);
        return tag.isPresent() ? Optional.of(new Tag(tag.get())) : Optional.empty();
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes {@code tag} from this {@code AddressBook}.
     * @throws TagNotFoundException if the {@code tag} is not in this {@code AddressBook}.
     */
    public boolean removeTag(Tag tag) throws TagNotFoundException {
        if (tags.remove(tag)) {
            return true;
        } else {
            throw new TagNotFoundException();
        }
    }

```
###### \java\seedu\address\model\Model.java
``` java
    /** Show map of the given person **/
    void showMapOf(ReadOnlyPerson person);

    /** Show direction to the given person from a given address **/
    void showDirectionsTo(ReadOnlyPerson target, Address address);

```
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Deletes the specified tag from everyone in the address book
     *
     * @throws DuplicatePersonException if deleting a tag from the person causes the person to be equivalent to
     *      another existing person in the list
     * @throws PersonNotFoundException if unable able to find a person in the list when iterating through
     *      all the persons
     * @throws TagNotFoundException if {@code tag} could not be found in the list.
     */
    void deleteTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException, TagNotFoundException;

```
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Deletes the specified tag from everyone in the address book
     *
     * @throws DuplicatePersonException if updating a tag for the person causes the person to be equivalent to
     *      another existing person in the list
     * @throws PersonNotFoundException if unable to find a person in the list when iterating through
     *      all the persons
     * @throws TagNotFoundException if {@code tag} could not be found in the list.
     */
    void editTag(Tag oldTag, Tag newTag) throws DuplicatePersonException, PersonNotFoundException,
            TagNotFoundException;

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void showMapOf(ReadOnlyPerson person) {
        raise(new PersonAddressDisplayMapEvent(person));
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void showDirectionsTo(ReadOnlyPerson target, Address address) {
        raise(new PersonAddressDisplayDirectionsEvent(target, address));
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException,
            TagNotFoundException {
        addressBook.removeTag(tag);
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson oldPerson = addressBook.getPersonList().get(i);

            Person newPerson = new Person(oldPerson);
            Set<Tag> newTags = new HashSet<Tag>(newPerson.getTags());
            newTags.remove(tag);
            newPerson.setTags(newTags);

            addressBook.updatePerson(oldPerson, newPerson);
        }
        indicateAddressBookChanged();
        checkMasterTagListHasAllTagsUsed();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void editTag(Tag oldTag, Tag newTag) throws DuplicatePersonException, PersonNotFoundException,
            TagNotFoundException {
        addressBook.removeTag(oldTag);
        for (int i = 0; i < addressBook.getPersonList().size(); i++) {
            ReadOnlyPerson oldPerson = addressBook.getPersonList().get(i);

            Person newPerson = new Person(oldPerson);
            Set<Tag> newTags = new HashSet<Tag>(newPerson.getTags());
            if (newTags.remove(oldTag)) {
                newTags.add(newTag);
                newPerson.setTags(newTags);
                addressBook.updatePerson(oldPerson, newPerson);
            }
        }
        try {
            addressBook.addTag(newTag);
        } catch (DuplicateTagException dpe) {
            //do nothing. It's perfectly fine if the new tag already exists in the address book. Enabled merge
        }

        indicateAddressBookChanged();
        checkMasterTagListHasAllTagsUsed();
    }

```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    private void loadGoogleMapDirections(ReadOnlyPerson person, Address address) {
        loadPage("https://www.google.com.sg/maps/dir/" + address.toString() + "/" + person.getAddress());
    }

```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handlePersonAddressDisplayMapEvent(PersonAddressDisplayMapEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadGoogleMap(event.person);
    }

```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handlePersonAddressDisplayDirectionsEvent(PersonAddressDisplayDirectionsEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadGoogleMapDirections(event.person, event.address);
    }
}
```
###### \resources\view\DarkTheme.css
``` css
.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#1d1d1d, 20%);
}

```
###### \resources\view\MainWindow.fxml
``` fxml
  <StackPane fx:id="tagListPanelPlaceholder" maxHeight="80" minHeight="80" prefHeight="80" styleClass="pane-with-border" VBox.vgrow="NEVER">
    <padding>
      <Insets bottom="5" left="10" right="10" top="5" />
    </padding>
  </StackPane>

```
###### \resources\view\TagListPanel.fxml
``` fxml
<?import javafx.geometry.Insets?>
<?import javafx.scene.layout.ColumnConstraints?>
<?import javafx.scene.layout.FlowPane?>
<?import javafx.scene.layout.GridPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.RowConstraints?>
<?import javafx.scene.layout.VBox?>
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
  <GridPane HBox.hgrow="ALWAYS">
    <columnConstraints>
      <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
    </columnConstraints>
    <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
      <padding>
        <Insets bottom="5" left="15" right="5" top="5" />
      </padding>
      <FlowPane fx:id="tags" />
    </VBox>
      <rowConstraints>
         <RowConstraints />
      </rowConstraints>
  </GridPane>
</HBox>
```
