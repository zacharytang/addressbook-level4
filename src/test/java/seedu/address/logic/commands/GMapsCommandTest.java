package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code GMapsCommand}.
 */
public class GMapsCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyPerson personToFind = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        GMapsCommand gMapsCommand = prepareCommand(INDEX_FIRST_PERSON, null);

        String expectedMessage = String.format(GMapsCommand.MESSAGE_SELECT_PERSON_SUCCESS, personToFind.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.showMapOf(personToFind);

        assertCommandSuccess(gMapsCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        GMapsCommand gMapsCommand = prepareCommand(outOfBoundIndex, null);

        assertCommandFailure(gMapsCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personToFind = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        GMapsCommand gMapsCommand = prepareCommand(INDEX_FIRST_PERSON, null);

        String expectedMessage = String.format(GMapsCommand.MESSAGE_SELECT_PERSON_SUCCESS, personToFind.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.showMapOf(personToFind);
        showFirstPersonOnly(expectedModel);

        assertCommandSuccess(gMapsCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstPersonOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        GMapsCommand gMapsCommand = prepareCommand(outOfBoundIndex, null);

        assertCommandFailure(gMapsCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexValidAddressUnfilteredList_success() throws Exception {

    }

    @Test
    public void execute_validIndexValidAddressFilteredList_success() throws Exception {

    }

    @Test
    public void execute_validIndexInvalidAddressUnfilteredList_throwsCommandException() throws Exception {

    }

    @Test
    public void execute_validIndexInvalidAddressFilteredList_throwsCommandException() throws Exception {

    }

    @Test
    public void execute_invalidIndexInvalidAddressFilteredList_throwsCommandException() throws Exception {

    }


    @Test
    public void equals() throws Exception {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand == null);

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        Set<Tag> firstSet = Stream.of(new Tag("word")).collect(Collectors.toSet());
        Set<Tag> secondSet = Stream.of(new Tag("other")).collect(Collectors.toSet());
        Set<Tag> thirdSet = Stream.of(new Tag("multiple"), new Tag("words")).collect(Collectors.toSet());

        DeleteCommand deleteFirstCommandTags = new DeleteCommand(firstSet);
        DeleteCommand deleteSecondCommandTags = new DeleteCommand(secondSet);
        DeleteCommand deleteThirdCommandTags = new DeleteCommand(thirdSet);

        // same object -> returns true
        assertTrue(deleteFirstCommandTags.equals(deleteFirstCommandTags));

        // same values -> returns true
        DeleteCommand deleteFirstCommandTagCopy = new DeleteCommand(firstSet);
        assertTrue(deleteFirstCommandTags.equals(deleteFirstCommandTagCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommandTags.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommandTags == null);

        // different tag -> returns false
        assertFalse(deleteFirstCommandTags.equals(deleteSecondCommandTags));
        assertFalse(deleteFirstCommandTags.equals(deleteThirdCommandTags));
    }

    /**
     * Returns a {@code GMapsCommand} with the parameter {@code index}, and {@code address}.
     */
    private GMapsCommand prepareCommand(Index index, Address address) {
        GMapsCommand gmapsCommand = new GMapsCommand(index , address);
        gmapsCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return gmapsCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assert model.getFilteredPersonList().isEmpty();
    }
}
