# April0616
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
    public DeleteCommand(ArrayList<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
        this.targetTags = null;
    }


```
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
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

        for (ReadOnlyPerson p : deletePersonList) {
            formatBuilder.append("[");
            formatBuilder.append(p.getAsText());
            formatBuilder.append("]");
            formatBuilder.append("\n");
        }
        String resultMsg = formatBuilder.toString();

        return resultMsg;

    }

```
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
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
```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(ReadOnlyPerson personToEdit,
                                             EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Gender updatedGender = editPersonDescriptor.getGender().orElse(personToEdit.getGender());
        MatricNo updatedMatricNo = editPersonDescriptor.getMatricNo().orElse(personToEdit.getMatricNo());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Birthday updateBirthday = editPersonDescriptor.getBirthday().orElse(personToEdit.getBirthday());
        Timetable updatedTimetable = editPersonDescriptor.getTimetable().orElse(personToEdit.getTimetable());
        Remark updatedRemark = personToEdit.getRemark();
        PhotoPath updatedPhotoPath = personToEdit.getPhotoPath();
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedGender, updatedMatricNo,
                updatedPhone, updatedEmail, updatedAddress,
                updatedTimetable, updatedRemark, updatedPhotoPath, updatedTags, updateBirthday);
    }

```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Optional<Gender> getGender() {
            return Optional.ofNullable(gender);
        }

        public void setMatricNo(MatricNo matricNo) {
            this.matricNo = matricNo;
        }

        public Optional<MatricNo> getMatricNo() {
            return Optional.ofNullable(matricNo);
        }
```
###### \java\seedu\address\logic\commands\PhotoCommand.java
``` java
/**
 * Edits the photo path of a person to the address book.
 */
public class PhotoCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "photo";
    public static final String COMMAND_ALIAS = "ph";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": If adds photo path to the person identified by the index number used in the last person listing,"
            + " add the photo path to the person.\n"
            + "If the photo path field is empty, the old photo path is removed for the person.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_PHOTO + "[PHOTO PATH] \n"
            + "Example: (add photo path) " + COMMAND_WORD + " 1 " + PREFIX_PHOTO
            + "C:\\Users\\User\\Desktop\\photo.jpg\n"
            + "Example: (delete photo path) " + COMMAND_WORD
            + " 2 "
            + PREFIX_PHOTO + "\n";

    public static final String MESSAGE_ADD_PHOTO_SUCCESS =
            "Successfully saved photo and added the photo path to Person: %1$s";
    public static final String MESSAGE_DELETE_PHOTO_SUCCESS = "Removed photo path from Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    public static final String LOCAL_PHOTOPATH_VALIDATION_REGEX = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?";
    public static final String MESSAGE_LOCAL_PHOTOPATH_CONSTRAINTS =
            "Photo Path should be the absolute path in your PC. It should be a string started with the name of "
                    + "your disk, followed by several groups of backslash and string, like c:\\desktop\\happy.jpg";
    public static final String FILE_SAVED_PARENT_PATH = "src/main/resources/images/contactPhotos/";
    public static final String DEFAULT_PHOTO_PATH = "src/main/resources/images/help_icon.png";

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

        if (trimmedPhotoPath.equals("")) { //not specified yet
            this.localPhotoPath = "";
            this.targetIndex = targetIndex;
            this.photoPath = new PhotoPath("");

        } else if (isValidLocalPhotoPath(trimmedPhotoPath)) {
            //not specified yet

            this.localPhotoPath = trimmedPhotoPath;
            String extension = getFileExtension(this.localPhotoPath);
            String savePath = FILE_SAVED_PARENT_PATH + getSavedFileName(extension);

            createAppPhotoFile(savePath);

            try {
                copyFile(this.localPhotoPath, savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.targetIndex = targetIndex;
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

        //if the command is 'ph/' or the contact has one original photo, then delete it.
        String originAppPhotoPath = personToPhoto.getPhotoPath().value;
        if (localPhotoPath.equals("") || !(originAppPhotoPath.equals(DEFAULT_PHOTO_PATH))) {
            removeAppFile(originAppPhotoPath);
        }

        Person photoedPerson = createPhotoedPerson(personToPhoto, photoPath);

        try {
            model.updatePerson(personToPhoto, photoedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

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
        if (photoPath.toString().isEmpty()) {
            return String.format(MESSAGE_DELETE_PHOTO_SUCCESS, personToPhoto);
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

```
###### \java\seedu\address\logic\commands\RemarkCommand.java
``` java
/**
 * Edits the remark of a person to the address book.
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
    public static final String COMMAND_ALIAS = "rm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": If remarks the person identified by the index number used in the last person listing,"
            + " add the remark to the person.\n"
            + "If the remark field is empty, the remark is removed for the person.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK] \n"
            + "Example: (add remark) " + COMMAND_WORD + " 1 " + PREFIX_REMARK + "Likes to drink coffee.\n"
            + "Example: (delete remark) " + COMMAND_WORD
            + " 2 "
            + PREFIX_REMARK + "\n";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added Remark to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed Remark from Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index targetIndex;
    private final Remark remark;

    /**
     * @param targetIndex of the person in the list to edit the remark
     * @param remark of the person
     */
    public RemarkCommand(Index targetIndex, Remark remark) {
        requireNonNull(targetIndex);
        requireNonNull(remark);

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
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMsg(remarkedPerson));
    }

    /**
     * Generate the Successful Message accordingly.
     * @param personToRemark
     * @return successful message for adding remark if the remark string is not empty.
     */
    private String generateSuccessMsg(ReadOnlyPerson personToRemark) {
        if (remark.toString().isEmpty()) {
            return String.format(MESSAGE_DELETE_REMARK_SUCCESS, personToRemark);
        } else {
            return String.format(MESSAGE_ADD_REMARK_SUCCESS, personToRemark);
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToRemark}
     * remarked with {@code remarkPersonDescriptor}.
     */
    public static Person createRemarkedPerson(ReadOnlyPerson personToRemark,
                                                 Remark remark) {
        assert personToRemark != null;

        Person remarkPerson = new Person(personToRemark.getName(), personToRemark.getGender(),
                personToRemark.getMatricNo(), personToRemark.getPhone(),
                personToRemark.getEmail(), personToRemark.getAddress(), personToRemark.getTimetable(),
                remark, personToRemark.getPhotoPath(), personToRemark.getTags(), personToRemark.getBirthday());

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
}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses {@code oneBasedIndexes} separated with commas into a {@Code ArrayList<Index>} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @throws IllegalValueException if one of the specified indexes is invalid (not non-zero unsigned integer).
     *
     */
    public static ArrayList<Index> parseIndexes(String oneBasedIndexes) throws IllegalValueException {
        String trimmedIndexes = oneBasedIndexes.trim();
        String[] indexes = trimmedIndexes.split(",");
        ArrayList<Index> deletePersons = new ArrayList<>();

        for (String index : indexes) {
            index = index.trim();
            if (!StringUtil.isNonZeroUnsignedInteger(index)) {
                throw new IllegalValueException(MESSAGE_INVALID_INDEX);
            }

            Index thisIndex = Index.fromOneBased(Integer.valueOf(index));
            if (!deletePersons.contains(thisIndex)) {
                deletePersons.add(thisIndex);
            }
        }

        return deletePersons;
    }
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> gender} into an {@code Optional<Gender>} if {@code gender} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Gender> parseGender(Optional<String> gender) throws IllegalValueException {
        requireNonNull(gender);
        return gender.isPresent() ? Optional.of(new Gender(gender.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> matricNo} into an {@code Optional<MatricNo>} if {@code matricNo} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<MatricNo> parseMatricNo(Optional<String> matricNo) throws IllegalValueException {
        requireNonNull(matricNo);
        return matricNo.isPresent() ? Optional.of(new MatricNo(matricNo.get())) : Optional.empty();
    }
```
###### \java\seedu\address\logic\parser\PhotoCommandParser.java
``` java
/**
 * Parses input arguments and creates a new PhotoCommand object
 */
public class PhotoCommandParser implements Parser<PhotoCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the PhotoCommand
     * and returns an PhotoCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PhotoCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PHOTO);

        Index index;
        String localPhotoPath;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            localPhotoPath = argMultimap.getValue(PREFIX_PHOTO).orElse("");


        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PhotoCommand.MESSAGE_USAGE));
        }

        try {
            return new PhotoCommand(index, localPhotoPath);
        } catch (FileNotFoundException fnfe) {
            throw new ParseException(
                    String.format(MESSAGE_FILE_NOT_FOUND, PhotoCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_FILE_PATH, PhotoCommand.MESSAGE_USAGE));
        }

    }
}
```
###### \java\seedu\address\logic\parser\RemarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new RemarkCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns an RemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        Remark remark;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        remark = new Remark(argMultimap.getValue(PREFIX_REMARK).orElse(""));

        return new RemarkCommand(index, remark);
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes the photo of the specified contact.
     * @param photoPath
     */
    public void removeContactPhoto(PhotoPath photoPath) {
        removeAppFile(photoPath.value);
    }


    /**
     * Removes {@code key} from this {@code AddressBook}, and delete the related contact photos.
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(ReadOnlyPerson key) throws PersonNotFoundException {
        removeContactPhoto(key.getPhotoPath());
        return persons.remove(key);
    }

    /**
     * Removes {@code keys} from this {@code AddressBook}, and delete the related contact photos.
     * @throws PersonNotFoundException if one of the {@code keys} is not in this {@code AddressBook}.
     */
    public boolean removePersons(ArrayList<ReadOnlyPerson> keys) throws PersonNotFoundException {
        for (ReadOnlyPerson key : keys) {
            removeContactPhoto(key.getPhotoPath());
            persons.remove(key);
        }
        return true;
    }
```
###### \java\seedu\address\model\Model.java
``` java
    /** Deletes the given person. */
    void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException;

    /** Deletes the given list of persons. */
    void deletePersons(ArrayList<ReadOnlyPerson> targets) throws PersonNotFoundException;
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        addressBook.removeContactPhoto(target.getPhotoPath());
        indicateAddressBookChanged();
        checkMasterTagListHasAllTagsUsed();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deletePersons(ArrayList<ReadOnlyPerson> targets) throws PersonNotFoundException {
        addressBook.removePersons(targets);
        indicateAddressBookChanged();
        checkMasterTagListHasAllTagsUsed();
    }

    //author@@ nbriannl
    @Override
    public void checkMasterTagListHasAllTagsUsed () {
        if (!addressBook.hasAllTagsInUse()) {
            indicateMasterTagListHasAnUnusedTag();
        }
    }

```
###### \java\seedu\address\model\person\Gender.java
``` java
/**
 * Represents a Person's gender in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGender(String)}
 */
public class Gender {

    public static final String MESSAGE_GENDER_CONSTRAINTS =
            "Person gender should be a string of either 'Male', 'Female' or 'M', 'F'";
    public static final String GENDER_VALIDATION_WORD1 = "Male";
    public static final String GENDER_VALIDATION_WORD2 = "Female";

    public final String value;

    /**
     * Validates given gender.
     * If gender string is 'Male' or 'Female', it remains the same.
     * Converts 'M' into 'Male', 'F' into 'Female'.
     *
     * @throws IllegalValueException if given gender string is invalid.
     */
    public Gender(String gender) throws IllegalValueException {
        requireNonNull(gender);
        String trimmedGender = gender.trim();
        if (trimmedGender.equals("M")) {
            trimmedGender = "Male";
        } else if (trimmedGender.equals("F")) {
            trimmedGender = "Female";
        }
        if (!isValidGender(trimmedGender)) {
            throw new IllegalValueException(MESSAGE_GENDER_CONSTRAINTS);
        }
        this.value = trimmedGender;
    }

    /**
     * Returns if a given string is a valid person gender.
     */
    public static boolean isValidGender(String test) {
        return test.equals(GENDER_VALIDATION_WORD1) || test.equals(GENDER_VALIDATION_WORD2);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Gender // instanceof handles nulls
                && this.value.equals(((Gender) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\person\MatricNo.java
``` java
/**
 * Represents a Person's Matriculation Number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMatricNo(String)}
 */
public class MatricNo {

    public static final String MESSAGE_MATRIC_NO_CONSTRAINTS =
            "Person matric number should be a 9-character string starting with 'A or a' and ending with a letter";
    public static final String MATRIC_NO_VALIDATION_REGEX = "([Aa])(\\d{7})([a-zA-Z])";

    public final String value;

    /**
     * Validates given matricNo.
     *
     * @throws IllegalValueException if given matricNo string is invalid.
     */
    public MatricNo(String matricNo) throws IllegalValueException {
        requireNonNull(matricNo);
        String trimmedMatricNo = matricNo.trim();
        if (!isValidMatricNo(trimmedMatricNo)) {
            throw new IllegalValueException(MESSAGE_MATRIC_NO_CONSTRAINTS);
        }
        this.value = trimmedMatricNo;
    }

    /**
     * Returns if a given string is a valid person matricNo.
     */
    public static boolean isValidMatricNo(String test) {
        //return test.matches(MATRIC_NO_VALIDATION_REGEX) && test.length() == 9;
        return test.matches(MATRIC_NO_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns true if two matric numbers are the same.
     * 'A0162533K' equals 'a0162533k'
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MatricNo // instanceof handles nulls
                && this.value.toUpperCase().equals(((MatricNo) other).value.toUpperCase())); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\person\PhotoPath.java
``` java
/**
 * Represents the path of a person's photo in the address book.
 */
public class PhotoPath {

    public static final String MESSAGE_APP_PHOTOPATH_CONSTRAINTS =
            "The app photo path should be a string starting with 'docs/images/contactPhotos/',"
                    + "following by the file name, like'photo.jpg'.";
    public static final String FILE_SAVED_PARENT_PATH = "src/main/resources/images/contactPhotos/";

    public final String value;  //photo path

    public PhotoPath(String photoPath) throws IllegalValueException {
        requireNonNull(photoPath);

        if (!isValidPhotoPath(photoPath)) {
            throw new IllegalValueException(MESSAGE_APP_PHOTOPATH_CONSTRAINTS);
        }
        this.value = photoPath;
    }

    /**
     * Returns if a given string is a valid photo path.
     */
    public static boolean isValidPhotoPath(String test) {
        if (test.equals(DEFAULT_PHOTO_PATH)) {
            return true;
        }
        String[] parts = test.split("\\.");
        Boolean isFileSpecified = (parts.length == 2);
        return test.startsWith(FILE_SAVED_PARENT_PATH) && isFileSpecified;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PhotoPath // instanceof handles nulls
                && this.value.equals(((PhotoPath) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\address\model\person\Remark.java
``` java
/**
 * Represents the remark of a person in the address book.
 */
public class Remark {

    public static final String MESSAGE_REMARK_CONSTRAINTS =
            "Person remarks can take any values, including blank";

    public final String value;

    public Remark(String remark) {
        requireNonNull(remark);
        this.value = remark;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\address\ui\PersonInfoOverview.java
``` java
    /**
     * Set the default contact photo to the default person.
     */
    public void setDefaultContactPhoto() {
        String defaultPhotoPath = "src/main/resources/images/help_icon.png";
        File defaultPhoto = new File(defaultPhotoPath);
        URI defaultPhotoUri = defaultPhoto.toURI();
        Image defaultImage = new Image(defaultPhotoUri.toString());
        centerImage(defaultImage);
        contactPhoto.setImage(defaultImage);
    }

    /**
     * Load the photo of the specified person.
     * @param person
     */
    public void loadPhoto(ReadOnlyPerson person) {
        String photoPath = person.getPhotoPath().value;
        File photo = new File(photoPath);
        URI photoUri = photo.toURI();
        Image image = new Image(photoUri.toString());

        contactPhoto.setPreserveRatio(true);
        centerImage(image);
        contactPhoto.setImage(image);
    }

```
