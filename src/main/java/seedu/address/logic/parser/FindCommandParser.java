package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    //@@author CindyTsai1
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {

        ArgumentMultimap argsMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_TAG, PREFIX_BIRTHDAY);

        if (args.isEmpty() || args.substring(1, 2).equals("g") || args.substring(1, 2).equals("m")
                || args.substring(1, 3).equals("tt") || !args.substring(2, 3).equals("/")) {
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
            predicate.add(PREFIX_BIRTHDAY.getPrefix());
            predicate.add(birthdayList);
        }

        if (nameList.isEmpty() && phoneList.isEmpty() && emailList.isEmpty() && addressList.isEmpty()
                && tagList.isEmpty() && birthdayList.isEmpty()) {
            throw new ParseException(FindCommand.MESSAGE_NOT_FOUND);
        }
        return new FindCommand(predicate);

    }
}
