package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.*;

import java.util.List;
import java.util.Optional;

public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
    public static final String COMMAND_ALIAS = "rm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Remarks the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + PREFIX_REMARK + "REMARK \n"
            + "Example: " + COMMAND_WORD + " 1" + PREFIX_REMARK + "Likes to drink coffee";

    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "Remarked Person: %1$s";
    public static final String MESSAGE_SAME_REMARK = "This remark already exists.";

    private final Index targetIndex;
    private final Remark remark;

    public RemarkCommand(Index targetIndex, Remark remark) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
        this.remark = remark;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToRemark = lastShownList.get(targetIndex.getZeroBased());

        Person remarkedPerson = createRemarkedPerson(personToRemark, remark);

        try {
            model.updatePerson(personToRemark, remarkedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_SAME_REMARK);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, personToRemark));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToRemark}
     * remarked with {@code remarkPersonDescriptor}.
     */
    public static Person createRemarkedPerson(ReadOnlyPerson personToRemark,
                                                 Remark remark) {
        assert personToRemark != null;
        Person remarkPerson = new Person(personToRemark.getName(), personToRemark.getPhone(),
                personToRemark.getEmail(), personToRemark.getAddress(), remark, personToRemark.getTags());

        return remarkPerson;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        // state check
        RemarkCommand r = (RemarkCommand) other;
        return targetIndex.equals(r.targetIndex)
                && remark.equals(r.remark);
    }

    /**
     * Stores the details to remark the person with. New remark value will replace the
     * old remark value of the person.
     */
    public static class RemarkPersonDescriptor {

        private Remark remark;

        public RemarkPersonDescriptor() {
        }

        public RemarkPersonDescriptor(RemarkCommand.RemarkPersonDescriptor toCopy) {
            this.remark = toCopy.remark;
        }

        /**
         * Returns true if the remark is going to be deleted.
         */
        public boolean isRemarkDeleted() {
            return !CollectionUtil.isAnyNonNull(this.remark);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof RemarkCommand.RemarkPersonDescriptor)) {
                return false;
            }

            // state check
            RemarkPersonDescriptor r = (RemarkPersonDescriptor) other;

            return getRemark().equals(r.getRemark());
        }
    }
}
