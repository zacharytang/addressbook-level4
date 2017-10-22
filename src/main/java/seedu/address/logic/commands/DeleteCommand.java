package seedu.address.logic.commands;

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

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes persons identified using their last displayed indexes used in the last person listing.\n"
            + "  OR the tag specified from all people containing the specific tag\n"
            + "Parameters: INDEX (must be positive integers)\n"
            + "        OR  " + PREFIX_TAG + "TAG (case-sensitive)\n "
            + "Example: " + COMMAND_WORD + " 1\n"
            + "         " + COMMAND_WORD + " 1, 2, 3\n"
            + "         " + COMMAND_WORD + " " + PREFIX_TAG + "friend\n"
            + "         " + COMMAND_WORD + " " + PREFIX_TAG + "friend " + PREFIX_TAG + "enemy";

    //need to check here
    //public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Persons";
    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted Tags";

    private final ArrayList<Index> targetIndexes;

    private final Set<Tag> targetTags;

    public DeleteCommand(Index targetIndex) {
        this.targetIndexes = new ArrayList<>();
        targetIndexes.add(targetIndex);
        this.targetTags = null;
    }

    public DeleteCommand(ArrayList<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
        this.targetTags = null;
    }

    public DeleteCommand(Set<Tag> targetTags) {
        this.targetIndexes = null;
        this.targetTags = targetTags;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        // this code block is command execution for delete [indexes]
        if (targetTags == null && targetIndexes != null) {
            List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

            for (Index index : targetIndexes) {
                if (index.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                ReadOnlyPerson personToDelete = lastShownList.get(index.getZeroBased());

                try {
                    model.deletePerson(personToDelete);
                } catch (PersonNotFoundException pnfe) {
                    assert false : "The target person with index " + index.getOneBased() + "cannot be missing";
                }
            }

            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS));

        } else { // this code block is command execution for delete t/[tag...]
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
    }

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
