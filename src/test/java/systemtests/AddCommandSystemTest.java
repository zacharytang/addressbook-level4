package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GENDER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GENDER_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_BIRTHDAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GENDER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MATRIC_NO_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SHORT_URL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIMETABLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MATRIC_NO_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.MATRIC_NO_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TIMETABLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TIMETABLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRIC_NO_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRIC_NO_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIMETABLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIMETABLE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SuggestCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.MatricNo;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();
        /* Case: add a person without tags to a non-empty address book, command with leading spaces and trailing spaces
         * -> added
         */
        ReadOnlyPerson toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_AMY + "  " + GENDER_DESC_AMY + "  "
                + MATRIC_NO_DESC_AMY + "   " + PHONE_DESC_AMY + " " + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   "
                + BIRTHDAY_DESC_AMY + "   " + TIMETABLE_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: add a person without tags to a non-empty address book using alias,
         * command with leading spaces and trailing spaces -> added
         */
        executeCommand(UndoCommand.COMMAND_WORD);
        command = "   " + AddCommand.COMMAND_ALIAS + "  " + NAME_DESC_AMY + "  " + GENDER_DESC_AMY + "  "
                + MATRIC_NO_DESC_AMY + "   " + PHONE_DESC_AMY + " " + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   "
                + BIRTHDAY_DESC_AMY + "   " + TIMETABLE_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: add a person without tags to a non-empty address book using secondary keyword,
         * command with leading spaces and trailing spaces -> added
         */
        executeCommand(UndoCommand.COMMAND_WORD);
        command = "   " + AddCommand.COMMAND_SECONDARY + "  " + NAME_DESC_AMY + "  " + GENDER_DESC_AMY + "  "
                + MATRIC_NO_DESC_AMY + "   " + PHONE_DESC_AMY + " " + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   "
                + BIRTHDAY_DESC_AMY + "   " + TIMETABLE_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        //@@author CindyTsai1
        /* Case: add a person without tags to a non-empty address book using mixed case keyword,
         * command with leading spaces and trailing spaces -> added
         */
        executeCommand(UndoCommand.COMMAND_WORD);
        command = "   " + "aDd" + "  " + NAME_DESC_AMY + "  " + GENDER_DESC_AMY + "  "
                + MATRIC_NO_DESC_AMY + "   " + PHONE_DESC_AMY + " " + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   "
                + BIRTHDAY_DESC_AMY + "   " + TIMETABLE_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

        //@@author
        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addPerson(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a duplicate person -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate person except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalPersons#ALICE
        // This test will fail is a new tag that is not in the model is used, see the bug documented in
        // AddressBook#addPerson(ReadOnlyPerson)
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + " "
                + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a person with all fields same as another person in the address book except name -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_BOB).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_BOB + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        //@@author April0616
        /* Case: add a person with all fields same as another person in the address book except gender -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_BOB)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_BOB + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        //@@author April0616
        /* Case: add a person with all fields same as another person in the address book except matricNo -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_BOB).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_BOB + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);
        //@@author

        /* Case: add a person with all fields same as another person in the address book except phone -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except email -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_BOB + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        /* Case: add a person with all fields same as another person in the address book except address -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_BOB).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_BOB + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        //@@author zacharytang
        /* Case: add a person with all fields same as another person in the address book except timetable -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_AMY).withTimetable(VALID_TIMETABLE_BOB)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_BOB + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        //@@author CindyTsai1
        /* Case: add a person with all fields same as another person in the address book except birthday -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_BOB).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + BIRTHDAY_DESC_BOB + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

        //@@author
        /* Case: filters the person list before adding -> added */
        executeCommand(FindCommand.COMMAND_WORD + " " + PREFIX_NAME + KEYWORD_MATCHING_MEIER);
        assert getModel().getFilteredPersonList().size()
                < getModel().getAddressBook().getPersonList().size();
        assertCommandSuccess(IDA);

        /* Case: add to empty address book -> added */
        executeCommand(ClearCommand.COMMAND_WORD);
        assert getModel().getAddressBook().getPersonList().size() == 0;
        assertCommandSuccess(ALICE);

        /* Case: add a person with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_FRIEND + GENDER_DESC_BOB + MATRIC_NO_DESC_BOB + PHONE_DESC_BOB
                + TIMETABLE_DESC_BOB + ADDRESS_DESC_BOB + NAME_DESC_BOB + TAG_DESC_HUSBAND + EMAIL_DESC_BOB
                + BIRTHDAY_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: selects first card in the person list, add a person -> added, card selection remains unchanged */
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        assert getPersonListPanel().isAnyCardSelected();
        assertCommandSuccess(CARL);

        /* Case: add a person, missing tags -> added */
        assertCommandSuccess(HOON);

        /* Case: add to address book with shorthand alias -> added */
        executeCommand(ClearCommand.COMMAND_WORD);
        command = AddCommand.COMMAND_ALIAS + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;

        assertCommandSuccess(command, AMY);

        /* Case: add to address book with secondary keyword -> added */
        executeCommand(ClearCommand.COMMAND_WORD);
        command = AddCommand.COMMAND_SECONDARY + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, AMY);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> suggested */
        command = "adds " + PersonUtil.getPersonDetails(toAdd);
        assertCommandFailure(command, String.format(SuggestCommand.MESSAGE_SUCCESS, "add"));

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        //@@author April0616
        /* Case: invalid gender -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + INVALID_GENDER_DESC + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, Gender.MESSAGE_GENDER_CONSTRAINTS);

        /* Case: invalid matricNo -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + INVALID_MATRIC_NO_DESC + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, MatricNo.MESSAGE_MATRIC_NO_CONSTRAINTS);
        //@@author


        /* Case: invalid phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + INVALID_PHONE_DESC
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + INVALID_EMAIL_DESC + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, Email.MESSAGE_EMAIL_CONSTRAINTS);

        //@@author zacharytang
        /* Case: invalid timetable -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + INVALID_TIMETABLE_DESC;
        assertCommandFailure(command, Timetable.MESSAGE_TIMETABLE_URL_CONSTRAINTS);

        /* Case: invalid short url -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + INVALID_SHORT_URL_DESC;
        assertCommandFailure(command, Timetable.MESSAGE_INVALID_SHORT_URL);

        //@@author
        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + TIMETABLE_DESC_AMY + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: invalid birthday -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + INVALID_BIRTHDAY_DESC + TIMETABLE_DESC_AMY;
        assertCommandFailure(command, Birthday.MESSAGE_BIRTHDAY_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and verifies that the command box displays
     * an empty string, the result display box displays the success message of executing {@code AddCommand} with the
     * details of {@code toAdd}, and the model related components equal to the current model added with {@code toAdd}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class, the status bar's sync status changes,
     * the browser url and selected card remains unchanged.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(ReadOnlyPerson toAdd) {
        assertCommandSuccess(PersonUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(ReadOnlyPerson)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(ReadOnlyPerson)
     */
    private void assertCommandSuccess(String command, ReadOnlyPerson toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addPerson(toAdd);
        } catch (DuplicatePersonException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, ReadOnlyPerson)} except that the result
     * display box displays {@code expectedResultMessage} and the model related components equal to
     * {@code expectedModel}.
     * @see AddCommandSystemTest#assertCommandSuccess(String, ReadOnlyPerson)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardChanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
