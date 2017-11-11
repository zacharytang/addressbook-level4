package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.MessageAlignmentFormatter.FORMAT_ALIGNMENT_TO_EXAMPLE;
import static seedu.address.commons.core.MessageAlignmentFormatter.FORMAT_ALIGNMENT_TO_PARAMETERS;
import static seedu.address.commons.util.FileUtil.copyFile;
import static seedu.address.commons.util.FileUtil.createIfMissing;
import static seedu.address.commons.util.FileUtil.getFileExtension;
import static seedu.address.commons.util.FileUtil.haveSameContent;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.PersonSelectedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.photo.PhotoPath;
import seedu.address.model.photo.exceptions.DuplicatePhotoPathException;

//@@author April0616
/**
 * Edits the photo path of a person to the address book.
 */
public class PhotoCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "photo";
    public static final String COMMAND_ALIAS = "ph";

    public static final String MESSAGE_USAGE = "| " + COMMAND_WORD + " |"
            + ": Adds a photo to the person identified by the index number used in the last person listing"
            + "by specifying the path of the photo.\n"
            + "If the path field is empty, the old photo path is removed for the person.\n"
            + "Parameters: INDEX " + PREFIX_PHOTO + "[PHOTO PATH] \n"
            + FORMAT_ALIGNMENT_TO_PARAMETERS + "(INDEX must be a positive integer)\n"
            + "Example: (add photo)     " + COMMAND_WORD + " 1 " + PREFIX_PHOTO
            + "C:\\Users\\User\\Desktop\\photo.jpg\n"
            + FORMAT_ALIGNMENT_TO_EXAMPLE + "(delete photo) " + COMMAND_WORD + " 2 " + PREFIX_PHOTO + "\n";

    public static final String MESSAGE_ADD_PHOTO_SUCCESS =
            "Successfully saved photo and added the photo path to Person: %1$s";
    public static final String MESSAGE_DELETE_PHOTO_SUCCESS = "Removed photo path from Person: %1$s";
    public static final String MESSAGE_NO_PHOTO_TO_DELETE = "No photo path to remove from Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    public static final String LOCAL_PHOTOPATH_VALIDATION_REGEX = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?";
    public static final String MESSAGE_LOCAL_PHOTOPATH_CONSTRAINTS =
            "Photo Path should be the absolute path of a valid file in your PC. It should be a string started "
                    + "with the name of your disk, "
                    + "followed by several groups of backslash and string, like \"c:\\desktop\\happy.jpg\","
                    + "and the file should exist.";
    public static final String FILE_SAVED_PARENT_PATH = "src/main/resources/images/contactPhotos/";
    public static final String DEFAULT_PHOTO_PATH = "src/main/resources/images/defaultPhoto.jpg";

    private final Index targetIndex;
    private final String localPhotoPath;
    private PhotoPath photoPath;

    /**
     * @param targetIndex of the person in the list to edit the photo path
     * @param localPhotoPath path of the photo store in the computer
     */
    public PhotoCommand(Index targetIndex, String localPhotoPath) throws IllegalValueException, FileNotFoundException {
        requireNonNull(targetIndex);
        requireNonNull(localPhotoPath);

        String trimmedPhotoPath = localPhotoPath.trim();

        if (trimmedPhotoPath.equals("")) { //not specified yet, delete photo
            this.localPhotoPath = "";
            this.targetIndex = targetIndex;
            this.photoPath = new PhotoPath("");

        } else if (isValidLocalPhotoPath(trimmedPhotoPath)) {

            File targetFile = new File(trimmedPhotoPath);
            if (!FileUtil.isFileExists(targetFile)) {
                throw new FileNotFoundException(MESSAGE_LOCAL_PHOTOPATH_CONSTRAINTS);
            }

            this.localPhotoPath = trimmedPhotoPath;
            String extension = getFileExtension(this.localPhotoPath);
            String savePath = FILE_SAVED_PARENT_PATH + getSavedFileName(extension);

            createAppPhotoFile(savePath);

            try {
                copyFile(this.localPhotoPath, savePath);
            } catch (IOException e) {
                assert false : "Cannot copy the file!";
            }

            this.targetIndex = targetIndex;

            //update photo path
            this.photoPath = new PhotoPath(savePath);

        } else {
            throw new IllegalValueException(MESSAGE_LOCAL_PHOTOPATH_CONSTRAINTS);
        }
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToPhoto = lastShownList.get(targetIndex.getZeroBased());
        Person photoedPerson = createPhotoedPerson(personToPhoto, photoPath);

        try {
            model.addPhotoPath(photoPath);

        } catch (DuplicatePhotoPathException e) {
            assert false : "Duplicated photo path!";
        }

        try {
            model.updatePerson(personToPhoto, photoedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        EventsCenter.getInstance().post(new PersonSelectedEvent(photoedPerson, targetIndex.getZeroBased()));
        return new CommandResult(generateSuccessMsg(photoedPerson));
    }


    /**
     * Returns if a given string is a valid local photo path.
     */
    public static boolean isValidLocalPhotoPath(String test) {
        return test.matches(LOCAL_PHOTOPATH_VALIDATION_REGEX);
    }


    /**
     * Get a unique file name for the picture of the person by combining the time this method executes and
     * its original file extension.
     * @return a unique file name and its extension
     */
    public String getSavedFileName(String fileExtension) {
        Date date = new Date();
        Long num = date.getTime();
        return num.toString() + fileExtension;
    }


    /**
     * Creates and returns a {@code Person} with the details of {@code personToPhoto}.
     */
    public static Person createPhotoedPerson(ReadOnlyPerson personToPhoto,
                                              PhotoPath photoPath) {
        assert personToPhoto != null;

        Person photoPerson = new Person(personToPhoto.getName(), personToPhoto.getGender(),
                personToPhoto.getMatricNo(), personToPhoto.getPhone(),
                personToPhoto.getEmail(), personToPhoto.getAddress(), personToPhoto.getTimetable(),
                personToPhoto.getRemark(), photoPath, personToPhoto.getTags(), personToPhoto.getBirthday());

        return photoPerson;
    }

    /**
     * Create the empty app photo file if it doesn't exist.
     * @param path of the app photo
     * @throws IOException
     */
    public void createAppPhotoFile(String path) {
        File photoFile = new File(path);
        try {
            createIfMissing(photoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the Successful Message accordingly.
     * @param personToPhoto
     * @return successful message for adding photo if the photo path string is not empty.
     */
    private String generateSuccessMsg(ReadOnlyPerson personToPhoto) {
        if (photoPath.toString().equals("")) {
            if (personToPhoto.getPhotoPath().value.equals("")) {
                return String.format(MESSAGE_NO_PHOTO_TO_DELETE, personToPhoto);
            } else {
                return String.format(MESSAGE_DELETE_PHOTO_SUCCESS, personToPhoto);
            }
        } else {
            return String.format(MESSAGE_ADD_PHOTO_SUCCESS, personToPhoto);
        }
    }

    /**
     * Get the local photo path of the file.
     * @return local photo path string
     */
    public String getLocalPhotoPath() {
        return this.localPhotoPath;
    }

    /**
     * Get the photo path of the file stored in app.
     * @return app photo path string
     */
    public String getAppPhotoPath() {
        return this.photoPath.value;
    }

    /**
     * Get the index of the person
     * @return the target index of the person
     */
    public Index getIndex() {
        return this.targetIndex;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PhotoCommand)) {
            return false;
        }

        // state check
        PhotoCommand ph = (PhotoCommand) other;
        return targetIndex.equals(ph.targetIndex)
                && haveSameContent(ph.photoPath.value, ((PhotoCommand) other).photoPath.value);
    }
}

