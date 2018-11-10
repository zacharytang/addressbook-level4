# CindyTsai1
###### \java\seedu\address\commons\events\ui\NewResultAvailableEvent.java
``` java
    public NewResultAvailableEvent(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

```
###### \java\seedu\address\commons\util\StringUtil.java
``` java
    /**
     * Returns true if the {@code sentence} equals the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc def") == true
     *       containsWordIgnoreCase("ABc def", "abc DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedSentence = sentence.trim();
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");
        String preppedWord = word.trim();
        String[] wordsInPreppedWord = preppedWord.split("\\s+");
        checkArgument(!wordsInPreppedWord[0].equals(""), "Word parameter cannot be empty");
        //checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        //check if the names and the word have the same number of words
        if (wordsInPreppedSentence.length != wordsInPreppedWord.length) {
            return false;
        }

        //check if all words in sentence matches all words in word
        for (int i = 0; i < wordsInPreppedSentence.length; i++) {
            if (!wordsInPreppedSentence[i].equalsIgnoreCase(wordsInPreppedWord[i])) {
                return false;
            }
        }
        return true;
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        public void setBirthday(Birthday birthday) {
            this.birthday = birthday;
        }

        public Optional<Birthday> getBirthday() {
            return Optional.ofNullable(birthday);
        }

```
###### \java\seedu\address\logic\commands\FindCommand.java
``` java
    public static final String MESSAGE_USAGE = "| " + COMMAND_WORD + " |"
            + ": Finds all persons whose information contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: ["
            + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_BIRTHDAY + "BIRTHDAY] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: \n" + COMMAND_WORD + " "
            + PREFIX_NAME + "ian zach\n"
            + "Returns any person with name including 'ian' and 'zach'.\n"
            + COMMAND_WORD + " "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney\n"
            + "Returns all persons in UniFy containing *both* 'JCfriends' and 'computing'.";

    public static final String MESSAGE_NOT_FOUND = "At least one field to find must be provided.";
    public static final String MESSAGE_BIRTHDAYKEYWORD_INVALID = "You should type 0%1$s instead of %1$s.";
    public static final String MESSAGE_BIRTHDAYKEYWORD_NONEXIST = "Month %1$s does not exist.";
    public static final String MESSAGE_BIRTHDAYKEYWORD_NONNUMBER = "Keyword input must be in integer.";
    private ArrayList<String> predicate;
    //to keep track of how many prefixes are input
    private int count = 0;
    private String birthdaySearch = new String();

    public FindCommand(ArrayList<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        List<String> predicateList = new ArrayList<>();

        //to keep track of the number of times a single person matches different fields of keywords
        HashMap<String, Integer> predicateMap = new HashMap<>();

        for (int i = 0; i < predicate.size() - 1; i++) {
            String predicates = predicate.get(i);

            if (predicates.equals(PREFIX_NAME.getPrefix())) {
                ArrayList<String> personsWithName = findPersonsWithName(predicate.get(i + 1));
                for (int j = 0; j < personsWithName.size(); j++) {
                    if (!predicateMap.containsKey(personsWithName.get(j))) {
                        predicateMap.put(personsWithName.get(j), 1);
                        predicateList.add(personsWithName.get(j));
                    } else {
                        predicateMap.put(personsWithName.get(j), predicateMap.remove(personsWithName.get(j)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_PHONE.getPrefix())) {
                ArrayList<String> personsWithPhone = findPersonsWithPhone(predicate.get(i + 1));
                for (int j = 0; j < personsWithPhone.size(); j++) {
                    if (!predicateMap.containsKey(personsWithPhone.get(j))) {
                        predicateMap.put(personsWithPhone.get(j), 1);
                        predicateList.add(personsWithPhone.get(j));
                    } else {
                        predicateMap.put(personsWithPhone.get(j), predicateMap.remove(personsWithPhone.get(j)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_ADDRESS.getPrefix())) {
                ArrayList<String> personsWithAddress = findPersonsWithAddress(predicate.get(i + 1));
                for (int j = 0; j < personsWithAddress.size(); j++) {
                    if (!predicateMap.containsKey(personsWithAddress.get(j))) {
                        predicateMap.put(personsWithAddress.get(j), 1);
                        predicateList.add(personsWithAddress.get(j));
                    } else {
                        predicateMap.put(personsWithAddress.get(j), predicateMap.remove(personsWithAddress.get(j)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_EMAIL.getPrefix())) {
                ArrayList<String> personsWithEmail = findPersonsWithEmail(predicate.get(i + 1));
                for (int j = 0; j < personsWithEmail.size(); j++) {
                    if (!predicateMap.containsKey(personsWithEmail.get(j))) {
                        predicateMap.put(personsWithEmail.get(j), 1);
                        predicateList.add(personsWithEmail.get(j));
                    } else {
                        predicateMap.put(personsWithEmail.get(j), predicateMap.remove(personsWithEmail.get(j)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_TAG.getPrefix())) {
                ArrayList<String> personsWithTags = findPersonsWithTags(predicate.get(i + 1));
                for (int j = 0; j < personsWithTags.size(); j++) {
                    if (!predicateMap.containsKey(personsWithTags.get(j))) {
                        predicateMap.put(personsWithTags.get(j), 1);
                        predicateList.add(personsWithTags.get(j));
                    } else {
                        predicateMap.put(personsWithTags.get(j), predicateMap.remove(personsWithTags.get(j)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_BIRTHDAY.getPrefix())) {
                birthdaySearch = predicate.get(i + 1).trim();
                ArrayList<String> personsWithBirthday = findPersonsWithBirthday(predicate.get(i + 1));
                for (int j = 0; j < personsWithBirthday.size(); j++) {
                    if (!predicateMap.containsKey(personsWithBirthday.get(j))) {
                        predicateMap.put(personsWithBirthday.get(j), 1);
                        predicateList.add(personsWithBirthday.get(j));
                    } else {
                        predicateMap.put(personsWithBirthday.get(j),
                                predicateMap.remove(personsWithBirthday.get(j)) + 1);
                    }
                }
            }
        }

        //to eliminate the persons who did not match all keywords
        ArrayList<String> predicatesList = new ArrayList<>();
        for (String word : predicateList) {
            if (predicateMap.get(word) == count) {
                predicatesList.add(word);
            }
        }

        NameContainsKeywordsPredicate predicates = new NameContainsKeywordsPredicate(predicatesList);
        model.updateFilteredPersonList(predicates);

        //Different display message for birthday
        if (!birthdaySearch.isEmpty()) {
            return new CommandResult(getMessageForPersonListShownSummary(
                    model.getFilteredPersonList().size())
            + getMessageForMonthSearch(birthdaySearch));
        }
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons with the same
     * birthday month.
     * @param month used to generate Month
     * @return summary message for the birthday month of persons displayed
     */
    public static String getMessageForMonthSearch(String month) {
        switch (month) {
        case "01":
            month = "January";
            break;
        case "02":
            month = "February";
            break;
        case "03":
            month = "March";
            break;
        case "04":
            month = "April";
            break;
        case "05":
            month = "May";
            break;
        case "06":
            month = "June";
            break;
        case "07":
            month = "July";
            break;
        case "08":
            month = "August";
            break;
        case "09":
            month = "September";
            break;
        case "10":
            month = "October";
            break;
        case "11":
            month = "November";
            break;
        case "12":
            month = "December";
            break;
        default:
        }
        return String.format(Messages.MESSAGE_BIRTHDAY_MONTH_SEARCHED, month);
    }

```
###### \java\seedu\address\logic\commands\FindCommand.java
``` java
    /**
     * Search for persons that contain the {@String keyword} in their name
     * @param name
     */
    public ArrayList<String> findPersonsWithName(String name) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] nameKeyword = name.split(" ");
        count += nameKeyword.length;
        ArrayList<String> nameList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : nameKeyword) {
                String names = person.getName().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (names.contains(keyword)) {
                    nameList.add(person.getName().toString());
                }
            }
        }
        return nameList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their phone number
     * @param phone
     */
    public ArrayList<String> findPersonsWithPhone(String phone) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] phoneKeyword = phone.split(" ");
        count += phoneKeyword.length;
        ArrayList<String> phoneList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : phoneKeyword) {
                String phones = person.getPhone().toString();
                if (phones.contains(keyword)) {
                    phoneList.add(person.getName().toString());
                }
            }
        }
        return phoneList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their email
     * @param email
     */
    public ArrayList<String> findPersonsWithEmail(String email) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] emailKeyword = email.split(" ");
        count += emailKeyword.length;
        ArrayList<String> emailList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : emailKeyword) {
                String emails = person.getEmail().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (emails.contains(keyword)) {
                    emailList.add(person.getName().toString());
                }
            }
        }
        return emailList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their address
     * @param address
     */
    public ArrayList<String> findPersonsWithAddress(String address) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] addressKeyword = address.split(" ");
        count += addressKeyword.length;
        ArrayList<String> addressList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : addressKeyword) {
                String addresses = person.getAddress().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (addresses.contains(keyword)) {
                    addressList.add(person.getName().toString());
                }
            }
        }
        return addressList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their tag
     * @param tags
     */
    public ArrayList<String> findPersonsWithTags(String tags) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] tagKeyword = tags.split(" ");
        count += tagKeyword.length;
        ArrayList<String> tagList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : tagKeyword) {
                //Used to keep track if a person's tags matches one keyword multiple times, if yes only record once
                ArrayList<String> tagsList = new ArrayList<>();
                for (Tag tagging : person.getTags()) {
                    //the tag name returned is in the format of [TAGNAME] so extract the name without []
                    String tag1 = tagging.toString().substring(1, tagging.toString().length()).toLowerCase();
                    keyword = keyword.toLowerCase();
                    if (tag1.contains(keyword) && !tagsList.contains(person.getName().toString())) {
                        tagsList.add(person.getName().toString());
                    }
                }
                tagList.addAll(tagsList);
            }
        }
        return tagList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their birthday
     * @param birthday
     */
    public ArrayList<String> findPersonsWithBirthday(String birthday) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] birthdayKeyword = birthday.split(" ");
        count += birthdayKeyword.length;
        ArrayList<String> birthdayList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : birthdayKeyword) {
                String birth = person.getBirthday().toString();
                String birthdays = (!birth.equals("")) ? birth.substring(3, 5) : "";
                if (birthdays.equals(keyword)) {
                    birthdayList.add(person.getName().toString());
                }
            }
        }
        return birthdayList;
    }
}
```
###### \java\seedu\address\logic\commands\SuggestCommand.java
``` java
/**
 * Suggests a correct command
 */
public class SuggestCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Do you mean %1$s?";

    private final String possibleCommand;

    /**
     * Creates an SuggestCommand to suggest the specified {@code String} for command
     */
    public SuggestCommand(String command) {
        this.possibleCommand = command;
    }

    @Override
    public CommandResult execute() throws CommandException {
        throw new CommandException(String.format(MESSAGE_SUCCESS, possibleCommand));
    }
}
```
###### \java\seedu\address\logic\commands\UniqueCommandList.java
``` java
/**
 * A list of all command words, including alias and secondary word
 */
public class UniqueCommandList {
    private static ArrayList<String> commandList;
    private static TreeSet<String> possibleCommandList;

    public static ArrayList<String> getCommandList() {
        commandList = new ArrayList<>();
        commandList.add(AddCommand.COMMAND_WORD);
        commandList.add(AddCommand.COMMAND_ALIAS);
        commandList.add(AddCommand.COMMAND_SECONDARY);
        commandList.add(ClearCommand.COMMAND_WORD);
        commandList.add(ClearCommand.COMMAND_ALIAS);
        commandList.add(ClearCommand.COMMAND_SECONDARY);
        commandList.add(DeleteCommand.COMMAND_WORD);
        commandList.add(DeleteCommand.COMMAND_ALIAS);
        commandList.add(DeleteCommand.COMMAND_SECONDARY);
        commandList.add(EditCommand.COMMAND_WORD);
        commandList.add(EditCommand.COMMAND_ALIAS);
        commandList.add(EditCommand.COMMAND_SECONDARY_ONE);
        commandList.add(EditCommand.COMMAND_SECONDARY_TWO);
        commandList.add(ExitCommand.COMMAND_WORD);
        commandList.add(ExitCommand.COMMAND_ALIAS);
        commandList.add(ExitCommand.COMMAND_SECONDARY_ONE);
        commandList.add(ExitCommand.COMMAND_SECONDARY_TWO);
        commandList.add(FindCommand.COMMAND_WORD);
        commandList.add(FindCommand.COMMAND_ALIAS);
        commandList.add(FindCommand.COMMAND_SECONDARY);
        commandList.add(HelpCommand.COMMAND_WORD);
        commandList.add(HistoryCommand.COMMAND_WORD);
        commandList.add(HistoryCommand.COMMAND_ALIAS);
        commandList.add(HistoryCommand.COMMAND_SECONDARY);
        commandList.add(ListCommand.COMMAND_WORD);
        commandList.add(ListCommand.COMMAND_ALIAS);
        commandList.add(ListCommand.COMMAND_SECONDARY_ONE);
        commandList.add(ListCommand.COMMAND_SECONDARY_TWO);
        commandList.add(RedoCommand.COMMAND_WORD);
        commandList.add(RedoCommand.COMMAND_ALIAS);
        commandList.add(RemarkCommand.COMMAND_WORD);
        commandList.add(RemarkCommand.COMMAND_ALIAS);
        commandList.add(SelectCommand.COMMAND_WORD);
        commandList.add(SelectCommand.COMMAND_ALIAS);
        commandList.add(SelectCommand.COMMAND_SECONDARY);
        commandList.add(UndoCommand.COMMAND_WORD);
        commandList.add(UndoCommand.COMMAND_ALIAS);
        commandList.add(GMapsCommand.COMMAND_WORD);
        commandList.add(GMapsCommand.COMMAND_ALIAS);
        commandList.add(GMapsCommand.COMMAND_SECONDARY_ONE);
        commandList.add(GMapsCommand.COMMAND_SECONDARY_TWO);
        commandList.add(PhotoCommand.COMMAND_WORD);
        commandList.add(PhotoCommand.COMMAND_ALIAS);
        commandList.add(ThemeCommand.COMMAND_WORD);
        commandList.add(ThemeCommand.COMMAND_ALIAS);
        commandList.add(ThemeCommand.COMMAND_SECONDARY);
        commandList.add(TimetableCommand.COMMAND_WORD);
        commandList.add(TimetableCommand.COMMAND_ALIAS);
        commandList.add(TimetableCommand.COMMAND_SECONDARY);
        return commandList;
    }

    public static TreeSet<String> getPossibleCommandList(String command) {
        possibleCommandList = new TreeSet<>();

        // Swapping i with i+1
        for (int i = 1; i < command.length() - 1; i++) {
            possibleCommandList.add(command.substring(0, i) + command.charAt(i + 1)
                    + command.charAt(i) + command.substring(i + 2));
        }

        // deleting one char, skipping i
        for (int i = 0; i < command.length(); i++) {
            possibleCommandList.add(command.substring(0, i) + command.substring(i + 1));
        }

        // inserting one char
        for (int i = 0; i < command.length() + 1; i++) {
            for (char j = 'a'; j <= 'z'; j++) {
                possibleCommandList.add(command.substring(0, i) + j + command.substring(i));
                // replacing one char
                if (i < command.length()) {
                    possibleCommandList.add(command.substring(0, i) + j + command.substring(i + 1));
                }
            }
        }
        return possibleCommandList;
    }
}
```
###### \java\seedu\address\logic\parser\AddCommandParser.java
``` java
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_GENDER, PREFIX_MATRIC_NO,
                        PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_TAG, PREFIX_BIRTHDAY, PREFIX_TIMETABLE);
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Gender gender = (arePrefixesPresent(argMultimap, PREFIX_GENDER))
                    ? ParserUtil.parseGender(argMultimap.getValue(PREFIX_GENDER)).get() : new Gender("");
            MatricNo matricNo = (arePrefixesPresent(argMultimap, PREFIX_MATRIC_NO))
                    ? ParserUtil.parseMatricNo(argMultimap.getValue(PREFIX_MATRIC_NO)).get() : new MatricNo("");
            Phone phone = (arePrefixesPresent(argMultimap, PREFIX_PHONE))
                    ? ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get() : new Phone("");
            Email email = (arePrefixesPresent(argMultimap, PREFIX_EMAIL))
                    ? ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get() : new Email("");
            Address address = (arePrefixesPresent(argMultimap, PREFIX_ADDRESS))
                    ? ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get() : new Address("");
            Timetable timetable = (arePrefixesPresent(argMultimap, PREFIX_TIMETABLE))
                    ? ParserUtil.parseTimetable(argMultimap.getValue(PREFIX_TIMETABLE)).get() : new Timetable("");
            Remark remark = new Remark("");
            PhotoPath photoPath = new PhotoPath("");
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            Birthday birthday = (arePrefixesPresent(argMultimap, PREFIX_BIRTHDAY))
                    ? ParserUtil.parseBirthday(argMultimap.getValue(PREFIX_BIRTHDAY)).get() : new Birthday("");

            ReadOnlyPerson person = new Person(name, gender, matricNo, phone, email, address, timetable,
                    remark, photoPath, tagList, birthday);

            return new AddCommand(person);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

```
###### \java\seedu\address\logic\parser\FindCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {

        ArgumentMultimap argsMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_TAG, PREFIX_BIRTHDAY);

        if (args.isEmpty() || arePrefixesPresent(argsMultimap, PREFIX_GENDER, PREFIX_TIMETABLE, PREFIX_MATRIC_NO,
                PREFIX_NEW_TAG, PREFIX_OLD_TAG, PREFIX_PHOTO, PREFIX_REMARK) || !arePrefixesPresent(argsMultimap,
                PREFIX_TAG, PREFIX_BIRTHDAY, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE, PREFIX_NAME)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArrayList<String> predicate = new ArrayList<String>();

        String nameList = new String();
        String phoneList = new String();
        String emailList = new String();
        String addressList = new String();
        String tagList = new String();
        String birthdayList = new String();

        if (argsMultimap.getValue(PREFIX_NAME).isPresent()) {
            nameList = argsMultimap.getValue(PREFIX_NAME).get();
            predicate.add(PREFIX_NAME.getPrefix());
            predicate.add(nameList);
        }

        if (argsMultimap.getValue(PREFIX_PHONE).isPresent()) {
            phoneList = argsMultimap.getValue(PREFIX_PHONE).get();
            predicate.add(PREFIX_PHONE.getPrefix());
            predicate.add(phoneList);
        }

        if (argsMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            emailList = argsMultimap.getValue(PREFIX_EMAIL).get();
            predicate.add(PREFIX_EMAIL.getPrefix());
            predicate.add(emailList);
        }

        if (argsMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            addressList = argsMultimap.getValue(PREFIX_ADDRESS).get();
            predicate.add(PREFIX_ADDRESS.getPrefix());
            predicate.add(addressList);
        }

        if (argsMultimap.getValue(PREFIX_TAG).isPresent()) {
            tagList = argsMultimap.getValue(PREFIX_TAG).get();
            predicate.add(PREFIX_TAG.getPrefix());
            predicate.add(tagList);
        }

        if (argsMultimap.getValue(PREFIX_BIRTHDAY).isPresent()) {
            birthdayList = argsMultimap.getValue(PREFIX_BIRTHDAY).get();

            /** validity of birthday keyword input check
             *  keyword input must have a value between 1 to 12
             *  keyword input must be 2 digits
             *  keyword input must be in Integers
             */
            if (!birthdayList.equals("")) {
                if (!birthdayList.matches("[0-9]+")) {
                    throw new ParseException(FindCommand.MESSAGE_BIRTHDAYKEYWORD_NONNUMBER);
                } else if (Integer.parseInt(birthdayList.trim()) > 12 || Integer.parseInt(birthdayList.trim()) < 1) {
                    throw new ParseException(String.format(FindCommand.MESSAGE_BIRTHDAYKEYWORD_NONEXIST,
                            birthdayList.trim()));
                } else if (birthdayList.trim().length() == 1) {
                    throw new ParseException(String.format(FindCommand.MESSAGE_BIRTHDAYKEYWORD_INVALID,
                            birthdayList.trim()));
                }
            }

            predicate.add(PREFIX_BIRTHDAY.getPrefix());
            predicate.add(birthdayList);
        }

        if (nameList.isEmpty() && phoneList.isEmpty() && emailList.isEmpty() && addressList.isEmpty()
                && tagList.isEmpty() && birthdayList.isEmpty()) {
            throw new ParseException(FindCommand.MESSAGE_NOT_FOUND);
        }

        return new FindCommand(predicate);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses {@code Optional<int> birthday} into a {@code HashMap<Birthday>} if {@code birthday} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Birthday> parseBirthday(Optional<String> birthday) throws IllegalValueException {
        requireNonNull(birthday);
        return birthday.isPresent() ? Optional.of(new Birthday(birthday.get())) : Optional.empty();
    }

```
###### \java\seedu\address\logic\parser\SuggestCommandParser.java
``` java
/**
 * Suggest user input command
 */
public class SuggestCommandParser implements Parser<SuggestCommand> {

    private UniqueCommandList list;

    /**
     * Parses the given {@code String} of commandWord in the context of the SuggestCommand
     * and returns an SuggestCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format or if a suggested spelling is found
     */
    public SuggestCommand parse (String commandWord) throws ParseException {
        list = new UniqueCommandList();
        TreeSet<String> possibleCommand = list.getPossibleCommandList(commandWord);
        ArrayList<String> commandList = list.getCommandList();

        for (String command: possibleCommand) {
            if (commandList.contains(command)) {
                return new SuggestCommand(command);
            }
        }
        throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
    }
}
```
###### \java\seedu\address\model\person\Birthday.java
``` java
/**
 * Represents a Person's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBirthday(String)}
 */
public class Birthday {

    public static final String MESSAGE_BIRTHDAY_CONSTRAINTS =
            "Person's birthday should be in the format of DDMMYYYY";
    public static final String MESSAGE_BIRTHDAY_INVALID =
            "This date does not exist.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String BIRTHDAY_VALIDATION_REGEX = "\\d{8}";

    public static final String DATE_FORMAT = "ddMMyyyy";

    public final String date;

    /**
     * Validates given birthday.
     *
     * @throws IllegalValueException if given birthday string is invalid.
     */
    public Birthday(String birthday) throws IllegalValueException {
        requireNonNull(birthday);
        String trimmedBirthday = birthday.trim();

        if (!isValidBirthday(trimmedBirthday)) {
            if (trimmedBirthday.length() == 8) {
                throw new IllegalValueException(MESSAGE_BIRTHDAY_INVALID);
            }
            throw new IllegalValueException(MESSAGE_BIRTHDAY_CONSTRAINTS);
        }
        this.date = formatDate(birthday);

    }

```
###### \java\seedu\address\model\person\Birthday.java
``` java
    /**
     * Returns true if a given string is a valid person birthday.
     */
    public static boolean isValidBirthday(String test) {
        if (test.equals("")) {
            return true;
        } else if (test.matches(BIRTHDAY_VALIDATION_REGEX)) {
            try {
                DateFormat df = new SimpleDateFormat(DATE_FORMAT);
                df.setLenient(false);
                df.parse(test);
                return true;
            } catch (ParseException pe) {
                return false;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthday // instanceof handles nulls
                && this.date.equals(((Birthday) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

}
```
###### \java\seedu\address\model\person\Person.java
``` java
    public void setBirthday(Birthday birthday) {
        this.birthday.set(requireNonNull(birthday));
    }

    @Override
    public ObjectProperty<Birthday> birthdayProperty() {
        return birthday;
    }

    @Override
    public Birthday getBirthday() {
        return birthday.get();
    }

```
###### \java\seedu\address\storage\AddressBookStorage.java
``` java
    void backupAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

}
```
###### \java\seedu\address\storage\StorageManager.java
``` java
    @Override
    public void backupAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        addressBookStorage.saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath() + "_backUp.xml");
    }


```
###### \java\seedu\address\storage\XmlAddressBookStorage.java
``` java
    public void backupAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath + "_backup.xml");
    }

}
```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

}
```
###### \java\seedu\address\ui\ResultDisplay.java
``` java
    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> displayed.setValue(event.message));

        if (event.isError) {
            setErrorStyle();
        } else {
            setDefaultStyle();
        }
    }

    private void setDefaultStyle() {
        resultDisplay.getStyleClass().remove("error");
    }

    private void setErrorStyle() {
        ObservableList<String> style = resultDisplay.getStyleClass();
        if (style.contains("error")) {
            return;
        }

        style.add("error");
    }

}
```
