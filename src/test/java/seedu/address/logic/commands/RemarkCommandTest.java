package seedu.address.logic.commands;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkCommand.
 */
public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person remarkedPerson = new PersonBuilder().build();
        RemarkCommand.RemarkPersonDescriptor descriptor = new RemarkPersonDescriptorBuilder(remarkedPerson).build();
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMRRK_PERSON_SUCCESS, remarkedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), remarkedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        ReadOnlyPerson lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person remarkedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        RemarkCommand.RemarkPersonDescriptor descriptor = new RemarkPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        RemarkCommand remarkCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS, remarkedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, remarkedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, new RemarkCommand.RemarkPersonDescriptor());
        ReadOnlyPerson remarkedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS, remarkedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person remarkedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON,
                new RemarkPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS, remarkedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), remarkedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = new Person(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
        RemarkCommand.RemarkPersonDescriptor descriptor = new RemarkPersonDescriptorBuilder(firstPerson).build();
        RemarkCommand remarkCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(remarkCommand, model, RemarkCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showFirstPersonOnly(model);

        // remark person in filtered list into a duplicate in address book
        ReadOnlyPerson personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON,
                new RemarkPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(remarkCommand, model, RemarkCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand.RemarkPersonDescriptor descriptor = new RemarkPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        RemarkCommand remarkCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Remark filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showFirstPersonOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemarkCommand remarkCommand = prepareCommand(outOfBoundIndex,
                new RemarkPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        RemarkCommand addFirstPersonCoffeeRemark = new RemarkCommand(INDEX_FIRST_PERSON, VALID_REMARK_COFFEE);
        RemarkCommand addFirstPersonCapRemark = new RemarkCommand(INDEX_FIRST_PERSON, VALID_REMARK_CAP);
        RemarkCommand addSecondPersonCoffeeRemark = new RemarkCommand(INDEX_SECOND_PERSON, VALID_REMARK_COFFEE);

        // same values -> returns true
        assertTrue(addFirstPersonCoffeeRemark.equals(addFirstPersonCoffeeRemark));

        // same object -> returns true
        assertTrue(addFirstPersonCapRemark.equals(addFirstPersonCapRemark));

        // null -> returns false
        assertFalse(addFirstPersonCapRemark.equals(null));

        // different index -> returns false
        assertFalse(addFirstPersonCoffeeRemark.equals(addSecondPersonCoffeeRemark));

        // different types -> returns false
        assertFalse(addFirstPersonCoffeeRemark.equals(new ClearCommand()));
    }

    /**
     * Returns an {@code RemarkCommand} with parameters {@code index} and {@code descriptor}
     */
    private RemarkCommand prepareCommand(Index index, RemarkCommand.RemarkPersonDescriptor descriptor) {
        RemarkCommand remarkCommand = new RemarkCommand(index, descriptor);
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }
}
