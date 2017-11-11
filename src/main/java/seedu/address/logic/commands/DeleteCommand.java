package seedu.address.logic.commands;

import static seedu.address.commons.core.MessageAlignmentFormatter.FORMAT_ALIGNMENT_TO_DELETE;
import static seedu.address.commons.core.MessageAlignmentFormatter.FORMAT_ALIGNMENT_TO_EXAMPLE;
import static seedu.address.commons.core.MessageAlignmentFormatter.FORMAT_ALIGNMENT_TO_PARAMETERS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Deletes persons identified using their last displayed indexes from the address book,
 * or a tag identified by the tag name
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";
    public static final String COMMAND_SECONDARY = "remove";

    public static final String MESSAGE_USAGE = "| " + COMMAND_WORD + " |"
            + ": Deletes the persons identified using their last displayed indexes used in the last person listing.\n"
            + FORMAT_ALIGNMENT_TO_DELETE + "OR the tag specified from all people containing the specific tag\n"
            + "Parameters: INDEX... (must be positive integers)\n"
            + FORMAT_ALIGNMENT_TO_PARAMETERS + "OR  " + PREFIX_TAG + "TAG... (case-sensitive)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + COMMAND_WORD + " 1, 2, 3\n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + COMMAND_WORD + " 2 3 4\n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + COMMAND_WORD + " " + PREFIX_TAG + "friend\n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + COMMAND_WORD + " " + PREFIX_TAG + "friend " + PREFIX_TAG + "enemy";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted Tags";

    private final ArrayList<Index> targetIndexes;

    private final Set<Tag> targetTags;

    public DeleteCommand(Index targetIndex) {
        this.targetIndexes = new ArrayList<>();
        targetIndexes.add(targetIndex);
        this.targetTags = null;
    }

    //@@author April0616
    public DeleteCommand(ArrayList<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
        this.targetTags = null;
    }


    //@@author nbriannl
    public DeleteCommand(Set<Tag> targetTags) {
        this.targetIndexes = null;
        this.targetTags = targetTags;
    }

    //@@author
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (targetTags == null && targetIndexes != null) {
            return executeCommandForPerson();

        } else {
            return executeCommandForTag();
        }
    }

    //@@author April0616
    /**
     * Command execution of {@code DeleteCommand} for a {@code Person}
     */
    private CommandResult executeCommandForPerson () throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        ArrayList<ReadOnlyPerson> deletePersonList = new ArrayList<>();

        for (Index index : targetIndexes) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            ReadOnlyPerson personToDelete = lastShownList.get(index.getZeroBased());
            deletePersonList.add(personToDelete);
        }

        try {
            model.deletePersons(deletePersonList);
        } catch (PersonNotFoundException pnfe) {
            assert false : "One of the target persons is missing";
        }
        return new CommandResult(generateResultMsg(deletePersonList));
    }

    //@@author nbriannl
    /**
     * Command execution of {@code DeleteCommand} for a {@code Tag}
     */
    private CommandResult executeCommandForTag () throws CommandException {
        ArrayList<Tag> arrayTags = new ArrayList<Tag>(targetTags);
        List<Tag> listOfExistingTags = model.getAddressBook().getTagList();

        if (!listOfExistingTags.containsAll(arrayTags)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAG_PROVIDED);
        }

        for (Tag tagToBeDeleted: arrayTags) {
            try {
                model.deleteTag(tagToBeDeleted);
            } catch (TagNotFoundException tnfe) {
                assert false : "[Delete Tag] A tag is not found";
            } catch (DuplicatePersonException dpe) {
                assert false : "[Delete Tag] A duplicate person is there";
            } catch (PersonNotFoundException pnfe) {
                assert false : "[Delete Tag] A person not found";
            }
        }

        return new CommandResult(MESSAGE_DELETE_TAG_SUCCESS);
    }

    //@@author April0616
    /**
     * Generate the command result of the deletePersonList.
     * @param deletePersonList
     * @return commandResult string
     */
    public static String generateResultMsg(ArrayList<ReadOnlyPerson> deletePersonList) {
        int numOfPersons = deletePersonList.size();
        StringBuilder formatBuilder = new StringBuilder();

        if (numOfPersons == 1) {
            formatBuilder.append("Deleted Person :\n");
        } else {
            formatBuilder.append("Deleted Persons :\n");
        }

        formatBuilder.append("[ ");

        for (int i = 0; i < deletePersonList.size(); i++) {
            formatBuilder.append((i + 1) + ". " + deletePersonList.get(i).getName() + " ");
        }

        formatBuilder.append("]\n");
        formatBuilder.append("Details: \n");

        for (ReadOnlyPerson p : deletePersonList) {
            formatBuilder.append("[");
            formatBuilder.append(p.getAsText());
            formatBuilder.append("]");
            formatBuilder.append("\n");
        }
        String resultMsg = formatBuilder.toString();

        return resultMsg;
    }

    //@@author April0616
    @Override
    public boolean equals(Object other) {
        if (targetIndexes != null) {
            return other == this // short circuit if same object
                    || (other instanceof DeleteCommand // instanceof handles nulls
                    && this.targetIndexes.equals(((DeleteCommand) other).targetIndexes)); // state check
        } else {
            return other == this // short circuit if same object
                    || (other instanceof DeleteCommand // instanceof handles nulls
                    && this.targetTags.equals(((DeleteCommand) other).targetTags)); // state check
        }
    }
}
