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
import seedu.address.model.person.Remark;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.commands.RemarkCommand.createRemarkedPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkCommand.
 */
public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexAddRemark_success() throws Exception {
        ReadOnlyPerson personToRemark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RemarkCommand.RemarkPersonDescriptor descriptor = new RemarkCommand.RemarkPersonDescriptor();

        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, REMARK_COFFEE);
        Person remarkedPerson = createRemarkedPerson(personToRemark, REMARK_COFFEE);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS, personToRemark);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updatePerson(personToRemark, remarkedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void equals() {
        RemarkCommand addFirstPersonCoffeeRemark = new RemarkCommand(INDEX_FIRST_PERSON, REMARK_COFFEE);
        RemarkCommand addFirstPersonCapRemark = new RemarkCommand(INDEX_FIRST_PERSON, REMARK_CAP);
        RemarkCommand addSecondPersonCoffeeRemark = new RemarkCommand(INDEX_SECOND_PERSON, REMARK_COFFEE);

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
    private RemarkCommand prepareCommand(Index index, Remark remark) {
        RemarkCommand remarkCommand = new RemarkCommand(index, remark);
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }
}
