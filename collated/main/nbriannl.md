# nbriannl
###### \java\seedu\address\commons\events\model\MasterTagListHasAnUnusedTagEvent.java
``` java
/** Indicates that the master tag list has an unused tag not used by any person*/
public class MasterTagListHasAnUnusedTagEvent extends BaseEvent {

    public final Set<Tag> outdatedTags;

    public MasterTagListHasAnUnusedTagEvent (Set<Tag> outdatedTags) {
        this.outdatedTags = outdatedTags;
    }

    @Override
    public String toString() {
        return "The tag list is outdated. With outdated tags: " + outdatedTags.toString();
    }
}
```
###### \java\seedu\address\commons\events\model\PersonAddressDisplayDirectionsEvent.java
``` java
/** Indicates a person's address as a map is to be displayed*/
public class PersonAddressDisplayDirectionsEvent extends BaseEvent {

    public final ReadOnlyPerson person;
    public final Address address;
    public final int targetIndex;

    public PersonAddressDisplayDirectionsEvent(ReadOnlyPerson person, Address address, int targetIndex) {
        this.person = person;
        this.address = address;
        this.targetIndex = targetIndex;
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
    public final int targetIndex;

    public PersonAddressDisplayMapEvent (ReadOnlyPerson person, int targetIndex) {
        this.person = person;
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return "Displaying location of " + person.getName() + ": " + person.getAddress();
    }
}
```
###### \java\seedu\address\commons\events\ui\PersonHasBeenDeletedEvent.java
``` java

/**
 * Represents a deletion of a person.
 */
public class PersonHasBeenDeletedEvent extends BaseEvent {

    public final ReadOnlyPerson deletedPerson;

    public PersonHasBeenDeletedEvent (ReadOnlyPerson deletedPerson) {
        this.deletedPerson = deletedPerson;
    }

    @Override
    public String toString() {
        return "Person has been deleted: " + deletedPerson.getName().toString();
    }
}
```
###### \java\seedu\address\commons\events\ui\PersonHasBeenModifiedEvent.java
``` java

/**
 * Represents a modification to a person.
 */
public class PersonHasBeenModifiedEvent extends BaseEvent {

    public final ReadOnlyPerson oldPerson;
    public final ReadOnlyPerson newPerson;

    public PersonHasBeenModifiedEvent (ReadOnlyPerson oldPerson, ReadOnlyPerson newPerson) {
        this.oldPerson = oldPerson;
        this.newPerson = newPerson;
    }

    @Override
    public String toString() {
        return "Person has been modified: " + newPerson.getName().toString();
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
    /**
     * Command execution of {@code DeleteCommand} for a {@code Tag}
     */
    private CommandResult executeCommandForTag () throws CommandException {
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

        return new CommandResult(generateResultMsgForTag(arrayTags));
    }

```
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
    /**
     * Generates the command result String for Delete Command when deleting tags
     */
    public static String generateResultMsgForTag(ArrayList<Tag> arrayTags) {
        int numOfTag = arrayTags.size();
        StringBuilder formatBuilder = new StringBuilder();

        if (numOfTag == 1) {
            formatBuilder.append("Deleted Tag :\n");
        } else {
            formatBuilder.append("Deleted Tags :\n");
        }

        formatBuilder.append("[ ");

        for (int i = 0; i < arrayTags.size(); i++) {
            if (i == 0) {
                formatBuilder.append(arrayTags.get(i).tagName);
            } else {
                formatBuilder.append(", " + arrayTags.get(i).tagName);
            }
        }

        formatBuilder.append(" ]\n");

        String resultMsg = formatBuilder.toString();

        return resultMsg;
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
            return executeCommandForPerson();
        } else {
            return executeCommandForTag();
        }
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     * Command execution of {@code EditCommand} for a {@code Tag}
     */
    private CommandResult executeCommandForTag () throws CommandException {
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
        return new CommandResult(generateResultMsgForTag(oldTag, newTag));
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     * Generates the command result String for Edit Command when editing tags
     */
    public static String generateResultMsgForTag(Tag oldTag, Tag newTag) {
        return "Edited Tag:\n" + "From '" + oldTag.tagName + "' to '" + newTag.tagName + "'";
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

    public static final String MESSAGE_USAGE = "| " + COMMAND_WORD + " |"
            + ": Opens a Google Maps view of a person’s address.\n"
            + FORMAT_ALIGNMENT_TO_GMAPS
            + " If an address is specified, shows the directions from the address to that person's address.\n"
            + "Format: " + COMMAND_WORD + " INDEX [a/ADDRESS]\n"
            + "Example: " + COMMAND_WORD + " 1 \n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + COMMAND_WORD + " 1 a/Blk 123, Yishun 75";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Showing Map View of %1$s's address";
    public static final String MESSAGE_DIRECTIONS_TO_PERSON_SUCCESS = "Showing directions to %1$s";
    public static final String MESSAGE_PERSON_HAS_NO_ADDRESS = "%1$s has no address!";

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
        if (personToShowMap.getAddress().toString().equals("")) {
            throw new CommandException(String.format(MESSAGE_PERSON_HAS_NO_ADDRESS, personToShowMap.getName()));
        }

        if (targetAddress != null) {
            model.showDirectionsTo(personToShowMap, targetAddress, targetIndex);
            return new CommandResult(String.format(MESSAGE_DIRECTIONS_TO_PERSON_SUCCESS, personToShowMap.getName()));
        } else {
            model.showMapOf(personToShowMap, targetIndex);
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
###### \java\seedu\address\logic\commands\ThemeCommand.java
``` java
/**
 * Changes the theme to the theme indicated
 */
public class ThemeCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "theme";
    public static final String COMMAND_ALIAS = "th";
    public static final String COMMAND_SECONDARY = "changetheme";

    public static final String MESSAGE_USAGE = "| " + COMMAND_WORD + " |"
            + ": Changes the theme to the specified theme word.\n"
            + "Parameters: THEMEWORD\n"
            + FORMAT_ALIGNMENT_TO_PARAMETERS + "(Possible theme words are: dark, light)\n"
            + "Example: " + COMMAND_WORD + " dark\n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + COMMAND_WORD + " light";

    public static final String MESSAGE_SUCCESS = "Theme switched: %1$s";
    public static final String VIEW_PATH = "/view/";

    private final String themeKeyword;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyPerson}
     */
    public ThemeCommand (String themeKeyword) {
        this.themeKeyword = themeKeyword;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        String themeToSwitch;
        String currentTheme = model.getCurrentTheme();

        HashMap<String, String> themes = model.getThemeMap();

        if (themes.containsKey(themeKeyword)) {
            themeToSwitch = themes.get(themeKeyword);
        } else {
            throw new CommandException(Messages.MESSAGE_THEME_NOT_FOUND);
        }

        if (currentTheme.equals(VIEW_PATH + themeToSwitch)) {
            throw new CommandException(Messages.MESSAGE_ALREADY_IN_THEME);
        }

        EventsCenter.getInstance().post(new ChangeThemeRequestEvent(themeToSwitch));

        return new CommandResult(String.format(MESSAGE_SUCCESS, themeToSwitch));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ThemeCommand // instanceof handles nulls
                && themeKeyword.equals(((ThemeCommand) other).themeKeyword));
    }
}
```
###### \java\seedu\address\logic\Logic.java
``` java
    ObservableList<Tag> getTagList();

    void checkAllMasterListTagsAreBeingUsed ();

```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public ObservableList<Tag> getTagList() {
        return model.getAddressBook().getTagList();
    }

    @Override
    public void checkAllMasterListTagsAreBeingUsed () {
        model.checkMasterTagListHasAllTagsUsed();
    }

```
###### \java\seedu\address\logic\parser\DeleteCommandParser.java
``` java
    /**
     * Parses the arguments of the delete command.
     * @return an DeleteCommand object for execution
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);
        String preamble = argMultimap.getPreamble();
        if (preamble.equals("")) {
            // there exists 't/'
            DeleteCommand deleteCommandForTag = parseForTags(argMultimap);
            if (deleteCommandForTag != null) {
                return deleteCommandForTag;
            }
        } else {
            DeleteCommand deleteCommandForPerson = parseForPersonIndexes(args, preamble);
            if (deleteCommandForPerson != null) {
                return deleteCommandForPerson;
            }
        }

        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

```
###### \java\seedu\address\logic\parser\DeleteCommandParser.java
``` java
    /**
     * Parses the {@code Tags} contained within {@code argMultimap} to return
     * a DeleteCommand object that executes a delete for Tags.
     * @throws ParseException if the values mapped as a tag does not conform as a valid tag
     * @see #parse(String)
     */
    private DeleteCommand parseForTags (ArgumentMultimap argMultimap) throws ParseException {
        try {
            if (arePrefixesPresent(argMultimap, PREFIX_TAG)) {
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                return new DeleteCommand(tagList);
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        return null;
    }

```
###### \java\seedu\address\logic\parser\EditCommandParser.java
``` java
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argsMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GENDER, PREFIX_MATRIC_NO,
                        PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TIMETABLE, PREFIX_TAG,
                        PREFIX_OLD_TAG, PREFIX_NEW_TAG, PREFIX_BIRTHDAY);
        String preamble = argsMultimap.getPreamble();

        if (preamble.matches("")) {
            return parseForTags(argsMultimap);
        } else if (preamble.matches("\\d+")) {
            return parseForPersonDetails(argsMultimap);
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
    }

    /**
     * Parses the old {@code Tag} and new {@code Tag} contained within {@code argMultimap} to return
     * a EditCommand object that executes a edit for Tag
     * @throws ParseException if the values mapped as a old or new tag does not conform as a valid tag
     * @see #parse(String)
     */
    private EditCommand parseForTags (ArgumentMultimap argsMultimap) throws ParseException {
        if (!arePrefixesPresent(argsMultimap, PREFIX_NEW_TAG, PREFIX_OLD_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        try {
            Tag oldTag = ParserUtil.parseSingleTag(argsMultimap.getValue(PREFIX_OLD_TAG)).get();
            Tag newTag = ParserUtil.parseSingleTag(argsMultimap.getValue(PREFIX_NEW_TAG)).get();
            return new EditCommand(oldTag, newTag);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
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

        if (address != null && address.toString().equals("")) {
            throw new ParseException(Address.MESSAGE_ADDRESS_CONSTRAINTS,
                    new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS));
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
###### \java\seedu\address\logic\parser\ThemeCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ThemeCommand object
 */
public class ThemeCommandParser implements Parser<ThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the GMapsCommand
     * and returns an GMapsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ThemeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE));
        }

        return new ThemeCommand(trimmedArgs);
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
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Checks if the master list {@link #tags} has every tag being used.
     *  @return true if all tags in the master list is being used by a person
     */
    public boolean hasAllTagsInUse () {
        HashSet<Tag> masterSet = new HashSet<Tag>();
        for (ReadOnlyPerson person: persons) {
            masterSet.addAll(person.getTags());
        }
        return masterSet.containsAll(tags.toSet());
    }

    /**
     *  Gets the tags in the master list {@link #tags} that is not being used
     *  @return {@code Set<Tag>} of Tags not being used by any person
     *  @see #hasAllTagsInUse()
     */
    public Set<Tag> getUnusedTags () {
        HashSet<Tag> actualSet = new HashSet<Tag>();
        for (ReadOnlyPerson person: persons) {
            actualSet.addAll(person.getTags());
        }
        Set<Tag> masterSet = tags.toSet();

        masterSet.removeAll(actualSet);
        return masterSet;
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

    //// Theme-level operations

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Initialises the themes in the address book.
     */
    private void initialiseThemes() {
        themes.put("light", "LightTheme.css");
        themes.put("dark", "DarkTheme.css");
    }

    HashMap<String, String> getThemeMap () {
        return themes;
    }

```
###### \java\seedu\address\model\Model.java
``` java
    /** Show map of the given person **/
    void showMapOf(ReadOnlyPerson person, Index index);

    /** Show direction to the given person from a given address **/
    void showDirectionsTo(ReadOnlyPerson target, Address address, Index index);

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
###### \java\seedu\address\model\Model.java
``` java
    /** Returns the theme map **/
    HashMap<String, String> getThemeMap ();

```
###### \java\seedu\address\model\Model.java
``` java
    /** Checks if the master list of tags in the address book has every tag being used */
    void checkMasterTagListHasAllTagsUsed ();

```
###### \java\seedu\address\model\ModelManager.java
``` java
    /** Raises an event to indicate a tag in the master list of tags is unused*/
    private void indicateMasterTagListHasAnUnusedTag () {
        raise(new MasterTagListHasAnUnusedTagEvent(addressBook.getUnusedTags()));
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void showMapOf(ReadOnlyPerson person, Index index) {
        raise(new PersonAddressDisplayMapEvent(person, index.getZeroBased()));
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void showDirectionsTo(ReadOnlyPerson target, Address address, Index index) {
        raise(new PersonAddressDisplayDirectionsEvent(target, address, index.getZeroBased()));
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void checkMasterTagListHasAllTagsUsed () {
        if (!addressBook.hasAllTagsInUse()) {
            indicateMasterTagListHasAnUnusedTag();
        }
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException,
            TagNotFoundException {
        addressBook.removeTag(tag);
        ObservableList<ReadOnlyPerson> personList = addressBook.getPersonList();
        for (int i = 0; i < personList.size(); i++) {
            ReadOnlyPerson oldPerson = personList.get(i);

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
        ObservableList<ReadOnlyPerson> personList = addressBook.getPersonList();
        for (int i = 0; i < personList.size(); i++) {
            ReadOnlyPerson oldPerson = personList.get(i);

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
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public HashMap<String, String> getThemeMap () {
        return this.addressBook.getThemeMap();
    }

```
###### \java\seedu\address\model\person\Birthday.java
``` java
    /**
     * Formats the unformatted input birthday string into dd/mm/yyyy and
     * @return the formatted String
     */
    public static String formatDate (String unformatted) {
        if (unformatted.equals("")) {
            return "";
        }
        DateFormat withoutFormat = new SimpleDateFormat("ddmmyyyy");
        DateFormat withFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date intermediateDate;
        try {
            intermediateDate = withoutFormat.parse(unformatted);
            String newDateString = withFormat.format(intermediateDate);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Removes the format from the date attribute
     * @return the unformatted String as {@code ddmmyyyy}
     */
    public String getUnformattedDate () {
        if (date.equals("")) {
            return "";
        }
        DateFormat withFormat = new SimpleDateFormat("dd/mm/yyyy");
        DateFormat withoutFormat = new SimpleDateFormat("ddmmyyyy");
        Date intermediateDate;
        try {
            intermediateDate = withFormat.parse(date);
            String newDateString = withoutFormat.format(intermediateDate);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

```
###### \java\seedu\address\model\tag\Tag.java
``` java
/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric, and should not be blank";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";
    public static final int TAG_ACCEPTABLE_LENGTH = 25;

    public final String tagName;

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!isValidTagName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        if (!isAcceptableTagLength(trimmedName)) {
            throw new IllegalValueException(generateExceptionMessageForLongTag(trimmedName));
        }
        this.tagName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }

    /**
     * Returns true if a valid tag name is too long.
     */
    public static boolean isAcceptableTagLength(String test) {
        return test.length() < TAG_ACCEPTABLE_LENGTH;
    }

    /**
     * Generates the exception message when trying to create a tag which is too long
     */
    public static String generateExceptionMessageForLongTag(String longTag) {
        return "The tag: " + longTag + " is too long!\n"
                + "Consider adding a remark instead?\n"
                + "Example: remark INDEX r/" + longTag + "\n"
                + "\n"
                + RemarkCommand.MESSAGE_USAGE;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.tagName.equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    private void loadGoogleMap(ReadOnlyPerson person) {
        loadPage(GOOGLE_MAPS_URL_PREFIX + person.getAddress());
    }


    private void loadGoogleMapDirections(ReadOnlyPerson person, Address address) {
        loadPage(GOOGLE_MAPS_DIRECTIONS_URL_PREFIX + address.toString() + "/" + person.getAddress());
    }

```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handlePersonAddressDisplayMapEvent(PersonAddressDisplayMapEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadGoogleMap(event.person);
    }

    @Subscribe
    private void handlePersonAddressDisplayDirectionsEvent(PersonAddressDisplayDirectionsEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadGoogleMapDirections(event.person, event.address);
    }
}
```
###### \java\seedu\address\ui\PersonInfoPanel.java
``` java
    /**
     * Clears the binds to allow to loadDefaultPerson() again
     */
    private void clearBind() {
        name.textProperty().unbind();;
        gender.textProperty().unbind();
        matricNo.textProperty().unbind();
        phone.textProperty().unbind();
        address.textProperty().unbind();
        email.textProperty().unbind();
        birthday.textProperty().unbind();
        remark.textProperty().unbind();
    }

```
###### \java\seedu\address\ui\PersonInfoPanel.java
``` java
    @Subscribe
    private void handlePersonHasBeenModifiedEvent(PersonHasBeenModifiedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (currentlyViewedPerson != null && currentlyViewedPerson.equals(event.oldPerson)) {
            loadPerson(event.newPerson);
        }
    }

```
###### \java\seedu\address\ui\PersonInfoPanel.java
``` java
    @Subscribe
    private void handlePersonHasBeenDeletedEvent(PersonHasBeenDeletedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (currentlyViewedPerson != null && currentlyViewedPerson.equals(event.deletedPerson)) {
            clearBind();
            loadDefaultPerson();
        }
    }

```
###### \java\seedu\address\ui\PersonInfoPanel.java
``` java
    @Subscribe
    private void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.data.getPersonList().size() == 0 && event.data.getTagList().size() == 0) {
            clearBind();
            loadDefaultPerson();
        }
    }
}
```
###### \java\seedu\address\ui\TagColorMap.java
``` java
/**
 * The mapping of the tag colors to be shared across any UI components containing tags
 */
public class TagColorMap {
    private static TagColorMap instance;
    private static final String[] COLORS = {"Crimson", "orange", "DarkSalmon", "LightSeaGreen",
        "RoyalBlue", "MediumPurple", "Teal", "Sienna", "HotPink", "MediumSeaGreen",
        "DarkSlateBlue", "CadetBlue", "MidnightBlue", "LightPink", "LightSalmon", "LightSkyBlue", "PaleVioletRed "};
    private static final int NUM_COLORS = COLORS.length;
    private static int colorIndex = 0;

    private HashMap<String, String> tagColors = new HashMap<String, String>();

    private TagColorMap() {
    }

    public static TagColorMap getInstance() {
        if (instance == null) {
            instance = new TagColorMap();
        }
        return instance;
    }

    /**
     * Gets a random unused color for the new tagName, or returns the corresponding color of the old tagName
     * @param tagName
     * @return the color of the tag
     */
    public String getTagColor(String tagName) {
        if (!tagColors.containsKey(tagName)) {
            tagColors.put(tagName, COLORS[colorIndex]);
            updateColorIndex();
        }
        return tagColors.get(tagName);
    }


```
###### \java\seedu\address\ui\TagListPanel.java
``` java
/**
 * An UI component that displays all the {@code Tags} in the {@code AddressBook} .
 */
public class TagListPanel extends UiPart<Region> {

    private static final String FXML = "TagListPanel.fxml";
    public final ObservableList<Tag> tagList;
    private final Logger logger = LogsCenter.getLogger(TagListPanel.class);

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */


    @FXML
    private FlowPane tags;

    public TagListPanel (ObservableList<Tag> tagList) {
        super(FXML);
        registerAsAnEventHandler(this);
        this.tagList = tagList;
        initTags(tagList);
        //bindListeners(person);
    }

    private void updateTagList (ObservableList<Tag> newtagList) {
        tags.getChildren().clear();
        initTags(newtagList);
    }

    /**
     * Initializes the tags for tag list
     */
    private void initTags (ObservableList<Tag> tagList) {
        Set<Tag> tagSet = tagList.stream().collect(Collectors.toSet());
        tagSet.forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: " + TagColorMap.getInstance().getTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        }
        );
    }

    /**
     *  Update the Tag List panel which has unused tags
     */
    private void updateTagListWithUnusedTag (ObservableList<Tag> tagList, Set<Tag> outdatedTags) {
        tags.getChildren().clear();
        Set<Tag> tagSet = tagList.stream().collect(Collectors.toSet());

        initTagsWithUnusedTags(outdatedTags, tagSet);
    }

    /**
     * Initializes the tags for a tag list which contains unused tags
     * and gives a different color to distinguish unused tags
     */
    private void initTagsWithUnusedTags (Set<Tag> outdatedTags, Set<Tag> tagSet) {
        tagSet.forEach(tag -> {
            if (!outdatedTags.contains(tag)) {
                Label tagLabel = new Label(tag.tagName);
                tagLabel.setStyle("-fx-background-color: " + TagColorMap.getInstance().getTagColor(tag.tagName));
                tags.getChildren().add(tagLabel);
            }
        }
        );
        outdatedTags.forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.setStyle("-fx-background-color: Gray  ");
            tags.getChildren().add(tagLabel);
        }
        );

    }

    @Subscribe
    public void handleAddressBookChangedEvent (AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateTagList(event.data.getTagList());
    }

    @Subscribe
    public void handleMasterTagListHasUnusedTagEvent (MasterTagListHasAnUnusedTagEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateTagListWithUnusedTag(tagList, event.outdatedTags);
    }
}
```
###### \resources\view\DarkTheme.css
``` css
.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: #383838;
}

```
###### \resources\view\LightTheme.css
``` css
.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: #FAFAFA;
}

```
###### \resources\view\MainWindow.fxml
``` fxml
                        <StackPane fx:id="tagListPanelPlaceholder" alignment="TOP_CENTER" styleClass="pane-with-border">
                     <padding>
                        <Insets bottom="10.0" left="10.0" right="10.0" />
                     </padding>
                        </StackPane>
                        <StackPane fx:id="personInfoPlaceholder" VBox.vgrow="ALWAYS" />

                    </VBox>
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
    <VBox alignment="CENTER_LEFT" GridPane.columnIndex="0">
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
