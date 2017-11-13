# CindyTsai1
###### \java\seedu\address\commons\util\StringUtilTest.java
``` java
    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertExceptionThrown(NullPointerException.class, null, "abc", Optional.empty());
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches all words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc")); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // Partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb")); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb")); // Query word bigger than sentence word

        // Matches all word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("ALICE", "ALICE")); // both upper case
        assertTrue(StringUtil.containsWordIgnoreCase("alice", "alice")); // both lower case
        assertTrue(StringUtil.containsWordIgnoreCase("Alice", "aLice")); // mixed cases
        assertTrue(StringUtil.containsWordIgnoreCase("Alice choo", "Alice Choo")); // multiple words
        assertTrue(StringUtil.containsWordIgnoreCase("   Alice   choo", "alice Choo  ")); // Leading/trailing spaces

    }

```
###### \java\seedu\address\logic\commands\EditPersonDescriptorTest.java
``` java
        //different birthday -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withBirthday(VALID_BIRTHDAY_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }
}
```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
    @Test
    public void execute_singleNameKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindCommand command = prepareCommand("n/ Ku");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, FIONA));
    }

    @Test
    public void execute_singlePhoneKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindCommand command = prepareCommand("p/ 94");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_singleEmailKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareCommand("e/ anna");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(GEORGE));
    }

    @Test
    public void execute_singleAddressKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        FindCommand command = prepareCommand("a/ ave");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, ELLE));
    }

    @Test
    public void execute_singleTagKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        FindCommand command = prepareCommand("t/ money");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_singleBirthdayKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + FindCommand.getMessageForMonthSearch("07");
        FindCommand command = prepareCommand("b/ 07");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL));
    }

```
###### \java\seedu\address\logic\commands\SuggestCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code SuggestCommand}.
 */
public class SuggestCommandTest {
    @Test
    public void execute_suggest_success() {
        String command = "edit";
        String expectedMessage = String.format(MESSAGE_SUCCESS, command);

        // assert that suggest command is executed
        try {
            new SuggestCommand(command).execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}

```
###### \java\seedu\address\logic\parser\AddCommandParserTest.java
``` java
        // multiple birthdays - last birthday accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + GENDER_DESC_BOB
                + MATRIC_NO_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + BIRTHDAY_DESC_AMY + BIRTHDAY_DESC_BOB + TIMETABLE_DESC_BOB
                + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

```
###### \java\seedu\address\logic\parser\AddCommandParserTest.java
``` java
        // invalid birthday
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + GENDER_DESC_BOB
                        + MATRIC_NO_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_BIRTHDAY_DESC + TIMETABLE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Birthday.MESSAGE_BIRTHDAY_CONSTRAINTS);

```
###### \java\seedu\address\logic\parser\EditCommandParserTest.java
``` java
        assertParseFailure(parser, "1" + INVALID_BIRTHDAY_DESC,
                Birthday.MESSAGE_BIRTHDAY_CONSTRAINTS); // invalid birthday

```
###### \java\seedu\address\logic\parser\EditCommandParserTest.java
``` java
        // birthday
        userInput = targetIndex.getOneBased() + BIRTHDAY_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withBirthday(VALID_BIRTHDAY_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

```
###### \java\seedu\address\logic\parser\FindCommandParserTest.java
``` java
    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new ArrayList<String>(Arrays.asList("n/", "li a")));
        assertParseSuccess(parser, " n/li a", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " n/ \n li a  \t", expectedFindCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        //birthday month is out of bound
        assertParseFailure(parser, " b/15", String.format(FindCommand.MESSAGE_BIRTHDAYKEYWORD_NONEXIST, "15"));

        //birthday month is a single digit
        assertParseFailure(parser, " b/1", String.format(FindCommand.MESSAGE_BIRTHDAYKEYWORD_INVALID, "1"));

        //birthday month is not integer
        assertParseFailure(parser, " b/abc", FindCommand.MESSAGE_BIRTHDAYKEYWORD_NONNUMBER);
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_wrongPrefix_throwsParseException() {
        assertParseFailure(parser, " tt/abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseBirthday_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseBirthday(null);
    }

    @Test
    public void parseBirthday_invalidValue_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseBirthday(Optional.of(INVALID_TIMETABLE));
    }

    @Test
    public void parseBirthday_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseBirthday(Optional.empty()).isPresent());
    }

    @Test
    public void parseBirthday_validValue_returnsTimetable() throws Exception {
        Birthday expectedBirthday = new Birthday(VALID_BIRTHDAY);
        Optional<Birthday> actualBirthday = ParserUtil.parseBirthday(Optional.of(VALID_BIRTHDAY));

        assertEquals(expectedBirthday, actualBirthday.get());
    }

```
###### \java\seedu\address\logic\parser\SuggestCommandParserTest.java
``` java
public class SuggestCommandParserTest {

    private SuggestCommandParser parser = new SuggestCommandParser();

    @Test
    public void parse_validCommand_returnsSuggestCommand() {
        String expectedMessage = String.format(MESSAGE_SUCCESS, "edit");

        //extra character at the back of the commandWord
        assertParseSuccess(parser, "editt", expectedMessage);

        //extra character at the front of the commandWord
        assertParseSuccess(parser, "eedit", expectedMessage);

        //extra character in the middle of the commandWord
        assertParseSuccess(parser, "ediit", expectedMessage);

        //missing character at the back of the commandWord
        assertParseSuccess(parser, "edi", expectedMessage);

        //missing character at the front of the commandWord
        assertParseSuccess(parser, "dit", expectedMessage);

        //missing character in the middle of the commandWord
        assertParseSuccess(parser, "edt", expectedMessage);

        //swapping character in the middle of the commandWord
        assertParseSuccess(parser, "eidt", expectedMessage);

        //swapping character at the back of the commandWord
        assertParseSuccess(parser, "edti", expectedMessage);

        //swapping character at the front of the commandWord
        assertParseSuccess(parser, "deit", expectedMessage);

        //wrong character in the middle of the commandWord
        assertParseSuccess(parser, "edet", expectedMessage);

        //wrong character at the back of the commandWord
        assertParseSuccess(parser, "edip", expectedMessage);

        //wrong character at the front of the commandWord
        assertParseSuccess(parser, "adit", expectedMessage);
    }

    @Test
    public void parse_invalidCommand_returnsfailure() {
        //suggested command not found
        assertParseSuccess(parser, "amend", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Asserts that the parsing of {@code command} by {@code parser} is successful and the command created
     * equals to {@code expectedCommand}.
     */
    public static void assertParseSuccess(Parser parser, String command, String expectedMessage) {
        try {
            parser.parse(command).execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
        } catch (ParseException e) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, e.getMessage());
        }
    }
}
```
###### \java\seedu\address\model\person\BirthdayTest.java
``` java
public class BirthdayTest {

    @Test
    public void isValidBirthday() {
        // invalid birthdays
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only
        assertFalse(Birthday.isValidBirthday("1324")); // less than 8 digits
        assertFalse(Birthday.isValidBirthday("dgd")); // non-numeric
        assertFalse(Birthday.isValidBirthday("23/05/1997")); // invalid slash
        assertFalse(Birthday.isValidBirthday("23 05 1997")); // invalid space
        assertFalse(Birthday.isValidBirthday("99999999")); //invalid dates
        assertFalse(Birthday.isValidBirthday("00000000")); //invalid dates

        // valid birthdays/ empty when optional
        assertTrue(Birthday.isValidBirthday("")); // empty string
        assertTrue(Birthday.isValidBirthday("23051997"));
    }
}
```
###### \java\seedu\address\testutil\EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Birthday} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withBirthday(String birthday) {
        try {
            ParserUtil.parseBirthday(Optional.of(birthday)).ifPresent(descriptor::setBirthday);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("birthday is expected to be unique.");
        }
        return this;
    }
    public EditPersonDescriptor build() {
        return descriptor;
    }


}
```
###### \java\seedu\address\testutil\PersonBuilder.java
``` java
    /**
     * Sets the {@code Birthday} of the {@code Person} that we are building.
     */
    public PersonBuilder withBirthday(String birthday) {
        try {
            this.person.setBirthday(new Birthday(birthday));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("birthday is expected to be unique.");
        }
        return this;
    }

```
###### \java\systemtests\AddCommandSystemTest.java
``` java
        /* Case: add a person without tags to a non-empty address book using mixed case keyword,
         * command with leading spaces and trailing spaces -> added
         */
        executeCommand(UndoCommand.COMMAND_WORD);
        command = "   " + "aDd" + "  " + NAME_DESC_AMY + "  " + GENDER_DESC_AMY + "  "
                + MATRIC_NO_DESC_AMY + "   " + PHONE_DESC_AMY + " " + EMAIL_DESC_AMY + "   " + ADDRESS_DESC_AMY + "   "
                + BIRTHDAY_DESC_AMY + "   " + TIMETABLE_DESC_AMY + "   " + TAG_DESC_FRIEND + " ";
        assertCommandSuccess(command, toAdd);

```
###### \java\systemtests\AddCommandSystemTest.java
``` java
        /* Case: add a person with all fields same as another person in the address book except birthday -> added */
        toAdd = new PersonBuilder().withName(VALID_NAME_AMY).withGender(VALID_GENDER_AMY)
                .withMatricNo(VALID_MATRIC_NO_AMY).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY)
                .withAddress(VALID_ADDRESS_AMY).withBirthday(VALID_BIRTHDAY_BOB).withTimetable(VALID_TIMETABLE_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + GENDER_DESC_AMY + MATRIC_NO_DESC_AMY
                + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + BIRTHDAY_DESC_BOB + TIMETABLE_DESC_AMY + TAG_DESC_FRIEND;
        assertCommandSuccess(command, toAdd);

```
###### \java\systemtests\ClearCommandSystemTest.java
``` java
        /* Case: mixed case command word -> cleared */
        assertCommandSuccess("ClEaR");
        assertSelectedCardUnchanged();

        /* Case: invalid keyword -> suggested */
        assertCommandFailure("claer", String.format(SuggestCommand.MESSAGE_SUCCESS, "clear"));
    }

```
###### \java\systemtests\EditCommandSystemTest.java
``` java
        /* Case: invalid birthday -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + INVALID_BIRTHDAY_DESC,
                Birthday.MESSAGE_BIRTHDAY_CONSTRAINTS);

```
###### \java\systemtests\FindCommandSystemTest.java
``` java
        /* Case: mixed case command word -> 1 person found */
        command = "FiNd n/Meier";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in address book, keyword is substring of name -> 1 persons found */
        command = FindCommand.COMMAND_WORD + " n/Mei";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

```
###### \java\systemtests\FindCommandSystemTest.java
``` java
        /* Case: find phone number of person in address book -> 1 persons found */
        command = FindCommand.COMMAND_WORD + " " + "p/" + DANIEL.getPhone().value;
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of person in address book -> 1 persons found */
        command = FindCommand.COMMAND_WORD + " " + "a/" + DANIEL.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of person in address book -> 1 persons found */
        command = FindCommand.COMMAND_WORD + " " + "e/" + DANIEL.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of person in address book -> 1 persons found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FindCommand.COMMAND_WORD + " " + "t/" + tags.get(0).tagName;
        ModelHelper.setFilteredList(expectedModel, TypicalPersons.getTypicalPersons());
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

```
###### \java\systemtests\FindCommandSystemTest.java
``` java
        /* Case: invalid keyword -> suggested */
        assertCommandFailure("fnid", String.format(SuggestCommand.MESSAGE_SUCCESS, "find"));
    }

```
###### \java\systemtests\SelectCommandSystemTest.java
``` java
        /* Case: mixed case command word -> rejected */
        assertCommandSuccess("SeLeCt 1", INDEX_FIRST_PERSON);

```
###### \java\systemtests\SelectCommandSystemTest.java
``` java
        /* Case: invalid keyword -> suggested */
        assertCommandFailure("salect", String.format(SuggestCommand.MESSAGE_SUCCESS, "select"));
    }

```
