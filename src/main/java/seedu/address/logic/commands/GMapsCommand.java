package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Selects a person identified using it's last displayed index from the address book.
 */
public class GMapsCommand extends Command {

    public static final String COMMAND_WORD = "gmaps";
    public static final String COMMAND_ALIAS = "g";
    public static final String COMMAND_SECONDARY = "map";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": show maps\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Showed maps of Person: %1$s";

    private final Index targetIndex;

    public GMapsCommand (Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToShowMap = lastShownList.get(targetIndex.getZeroBased());

        model.showMapOf(personToShowMap);

        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GMapsCommand // instanceof handles nulls
                && this.targetIndex.equals(((GMapsCommand) other).targetIndex)); // state check
    }
}
