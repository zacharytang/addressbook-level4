package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * Finds and lists all persons in address book whose information contains any of the argument keywords in the
 * specific field.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";
    public static final String COMMAND_SECONDARY = "search";

    //@@author CindyTsai1
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose information contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
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

        //to keep track of the number of times a single person matches different fields of keywords
        HashMap<String, Integer> predicateMap = new HashMap<>();

        //to keep track of how many prefixes are input
        int count = 0;

        for (int i = 0; i < predicate.size() - 1; i++) {
            String predicates = predicate.get(i);

            if (predicates.equals(PREFIX_NAME.getPrefix())) {
                count++;
                ArrayList<String> nameList = findName(predicate.get(i + 1));
                for (int j = 0; j < nameList.size(); j++) {
                    if (!predicateMap.containsKey(nameList.get(j))) {
                        predicateMap.put(nameList.get(j), 1);
                        predicateList.add(nameList.get(j));
                    } else {
                        predicateMap.put(nameList.get(j), predicateMap.remove(nameList.get(i)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_PHONE.getPrefix())) {
                count++;
                ArrayList<String> phoneList = findPhone(predicate.get(i + 1));
                for (int j = 0; j < phoneList.size(); j++) {
                    if (!predicateMap.containsKey(phoneList.get(j))) {
                        predicateMap.put(phoneList.get(j), 1);
                        predicateList.add(phoneList.get(j));
                    } else {
                        predicateMap.put(phoneList.get(j), predicateMap.remove(phoneList.get(i)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_ADDRESS.getPrefix())) {
                count++;
                ArrayList<String> addressList = findAddress(predicate.get(i + 1));
                for (int j = 0; j < addressList.size(); j++) {
                    if (!predicateMap.containsKey(addressList.get(j))) {
                        predicateMap.put(addressList.get(j), 1);
                        predicateList.add(addressList.get(j));
                    } else {
                        predicateMap.put(addressList.get(j), predicateMap.remove(addressList.get(i)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_EMAIL.getPrefix())) {
                count++;
                ArrayList<String> emailList = findEmail(predicate.get(i + 1));
                for (int j = 0; j < emailList.size(); j++) {
                    if (!predicateMap.containsKey(emailList.get(j))) {
                        predicateMap.put(emailList.get(j), 1);
                        predicateList.add(emailList.get(j));
                    } else {
                        predicateMap.put(emailList.get(j), predicateMap.remove(emailList.get(i)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_TAG.getPrefix())) {
                count++;
                ArrayList<String> tagList = findTags(predicate.get(i + 1));
                for (int j = 0; j < tagList.size(); j++) {
                    if (!predicateMap.containsKey(tagList.get(j))) {
                        predicateMap.put(tagList.get(j), 1);
                        predicateList.add(tagList.get(j));
                    } else {
                        predicateMap.put(tagList.get(j), predicateMap.remove(tagList.get(i)) + 1);
                    }
                }
            }

            if (predicates.equals(PREFIX_BIRTHDAY.getPrefix())) {
                count++;
                ArrayList<String> birthdayList = findBirthday(predicate.get(i + 1));
                for (int j = 0; j < birthdayList.size(); j++) {
                    if (!predicateMap.containsKey(birthdayList.get(j))) {
                        predicateMap.put(birthdayList.get(j), 1);
                        predicateList.add(birthdayList.get(j));
                    } else {
                        predicateMap.put(birthdayList.get(j), predicateMap.remove(birthdayList.get(i)) + 1);
                    }
                }
            }
        }

        //to eliminate the persons who did not match all keywords
        for (String word : predicateList) {
            if (predicateMap.get(word) != count) {
                predicateList.remove(word);
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
                if (names.contains(keyword) && !nameList.contains(person.getName().toString())) {
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
    public ArrayList<String> findPhone(String phone) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] phoneKeyword = phone.split(" ");
        ArrayList<String> phoneList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : phoneKeyword) {
                String phones = person.getPhone().toString();
                if (phones.contains(keyword) && !phoneList.contains(person.getName().toString())) {
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
    public ArrayList<String> findEmail(String email) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] emailKeyword = email.split(" ");
        ArrayList<String> emailList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : emailKeyword) {
                String emails = person.getEmail().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (emails.contains(keyword) && !emailList.contains(person.getName().toString())) {
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
    public ArrayList<String> findAddress(String address) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] addressKeyword = address.split(" ");
        ArrayList<String> addressList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : addressKeyword) {
                String addresses = person.getAddress().toString().toLowerCase();
                keyword = keyword.toLowerCase();
                if (addresses.contains(keyword) && !addressList.contains(person.getName().toString())) {
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
    public ArrayList<String> findTags(String tags) {
        ObservableList<ReadOnlyPerson> personList = model.getAddressBook().getPersonList();
        String[] tagKeyword = tags.split(" ");
        ArrayList<String> tagList = new ArrayList<>();
        for (ReadOnlyPerson person : personList) {
            for (String keyword : tagKeyword) {
                for (Tag tagging : person.getTags()) {
                    //the tag name returned is in the format of [TAGNAME] so extract the name without []
                    String tag1 = tagging.toString().substring(1, tagging.toString().length()).toLowerCase();
                    keyword = keyword.toLowerCase();
                    if (tag1.contains(keyword) && !tagList.contains(person.getName().toString())) {
                        tagList.add(person.getName().toString());
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
                String birth = person.getBirthday().toString();
                String birthdays = (!birth.equals("")) ? birth.substring(2, 4) : "";
                if (birthdays.equals(keyword) && !birthdayList.contains(person.getName().toString())) {
                    birthdayList.add(person.getName().toString());
                }
            }
        }
        return birthdayList;
    }
}
