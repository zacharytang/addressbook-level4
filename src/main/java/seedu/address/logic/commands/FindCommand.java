package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";
    public static final String COMMAND_SECONDARY = "search";

    //@@author CindyTsai1
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose information contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with targetIndex numbers.\n"
            + "Parameters: ["
            + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_BIRTHDAY + "BIRTHDAY] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_BIRTHDAY + "21051994 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_NOT_FOUND = "At least one field to find must be provided.";
    private ArrayList<String> predicate;

    public FindCommand(ArrayList<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        List<String> predicateList = new ArrayList<>();

        for (int i = 0; i < predicate.size() - 1; i++) {
            String predicates = predicate.get(i);

            if (predicates.equals(PREFIX_NAME.getPrefix())) {
                ArrayList<String> nameList = findName(predicate.get(i + 1));
                for (int j = 0; j < nameList.size(); j++) {
                    if (!predicateList.contains(nameList.get(j))) {
                        predicateList.add(nameList.get(j));
                    }
                }
            }

            if (predicates.equals(PREFIX_PHONE.getPrefix())) {
                ArrayList<String> phoneList = findPhone(predicate.get(i + 1));
                for (int j = 0; j < phoneList.size(); j++) {
                    if (!predicateList.contains(phoneList.get(j))) {
                        predicateList.add(phoneList.get(j));
                    }
                }
            }

            if (predicates.equals(PREFIX_ADDRESS.getPrefix())) {
                ArrayList<String> addressList = findAddress(predicate.get(i + 1));
                for (int j = 0; j < addressList.size(); j++) {
                    if (!predicateList.contains(addressList.get(j))) {
                        predicateList.add(addressList.get(j));
                    }
                }
            }

            if (predicates.equals(PREFIX_EMAIL.getPrefix())) {
                ArrayList<String> emailList = findEmail(predicate.get(i + 1));
                for (int j = 0; j < emailList.size(); j++) {
                    if (!predicateList.contains(emailList.get(j))) {
                        predicateList.add(emailList.get(j));
                    }
                }
            }

            if (predicates.equals(PREFIX_TAG.getPrefix())) {
                ArrayList<String> tagList = findTags(predicate.get(i + 1));
                for (int j = 0; j < tagList.size(); j++) {
                    if (!predicateList.contains(tagList.get(j))) {
                        predicateList.add(tagList.get(j));
                    }
                }
            }

            if (predicates.equals(PREFIX_BIRTHDAY.getPrefix())) {
                ArrayList<String> birthdayList = findBirthday(predicate.get(i + 1));
                for (int j = 0; j < birthdayList.size(); j++) {
                    if (!predicateList.contains(birthdayList.get(j))) {
                        predicateList.add(birthdayList.get(j));
                    }
                }
            }
        }

        NameContainsKeywordsPredicate predicates = new NameContainsKeywordsPredicate(predicateList);
        model.updateFilteredPersonList(predicates);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    //@@author
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.predicate.equals(((FindCommand) other).predicate)); // state check
    }

    //@@author CindyTsai1
    /**
     * Search for persons that contain the {@String keyword} in their name
     * @param name
     */
    public ArrayList<String> findName(String name) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] nameKeyword = name.split(" ");
        ArrayList<String> nameList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : nameKeyword) {
                String names = person.getName().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (names.contains(keyword)) {
                    nameList.add(person.getName().toString(0));
                }
            }
        }
        return nameList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their phone number
     * @param phone
     */
    public ArrayList<String> findPhone(String phone) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] phoneKeyword = phone.split(" ");
        ArrayList<String> phoneList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : phoneKeyword) {
                String phones = person.getPhone().toString();
                if (phones.contains(keyword)) {
                    phoneList.add(person.getName().toString(0));
                }
            }
        }
        return phoneList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their email
     * @param email
     */
    public ArrayList<String> findEmail(String email) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] emailKeyword = email.split(" ");
        ArrayList<String> emailList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : emailKeyword) {
                String emails = person.getEmail().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (emails.contains(keyword)) {
                    emailList.add(person.getName().toString(0));
                }
            }
        }
        return emailList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their address
     * @param address
     */
    public ArrayList<String> findAddress(String address) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] addressKeyword = address.split(" ");
        ArrayList<String> addressList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : addressKeyword) {
                String addresses = person.getAddress().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (addresses.contains(keyword)) {
                    addressList.add(person.getName().toString(0));
                }
            }
        }
        return addressList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their tag
     * @param tags
     */
    public ArrayList<String> findTags(String tags) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] tagKeyword = tags.split(" ");
        ArrayList<String> tagList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : tagKeyword) {
                for (Tag tagging : person.getTags()) {
                    String tag1 = tagging.toString().substring(1, tagging.toString().length()).toLowerCase();
                    keyword = keyword.toLowerCase();
                    if (tag1.contains(keyword)) {
                        tagList.add(person.getName().toString(0));
                    }
                }
            }
        }
        return tagList;
    }

    /**
     * Search for persons that contain the {@String keyword} in their birthday
     * @param birthday
     */
    public ArrayList<String> findBirthday(String birthday) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] birthdayKeyword = birthday.split(" ");
        ArrayList<String> birthdayList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : birthdayKeyword) {
                String birthdays = person.getBirthday().toString().substring(2, 4);
                if (birthdays.equals(keyword)) {
                    birthdayList.add(person.getName().toString(0));
                }
            }
        }
        return birthdayList;
    }
}
