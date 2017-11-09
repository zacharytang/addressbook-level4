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
    private CommandResult getCommandResultForPerson() throws CommandException {
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

        //return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS));
        return new CommandResult(generateResultMsg(deletePersonList));
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

        EventsCenter.getInstance().post(new PersonSelectedEvent(photoedPerson));
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
            + "Accept multiple remarks at the same time.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK1] " + PREFIX_REMARK + "[REMARK2]\n"
            + "Example: (add remark) " + COMMAND_WORD + " 1 " + PREFIX_REMARK + "Likes to drink coffee\n"
            + "Example: (add remarks) " + COMMAND_WORD + " 1 " + PREFIX_REMARK + "Likes to drink coffee "
            + PREFIX_REMARK + "CAP5.0\n"
            + "Example: (delete remarks) " + COMMAND_WORD
            + " 2 "
            + PREFIX_REMARK + "\n";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added Remark(s) to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed Remark(s) from Person: %1$s";
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
     * Generates the successful Message accordingly.
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
###### \java\seedu\address\logic\parser\DeleteCommandParser.java
``` java
    public static final String DELETE_ONE_PERSON_VALIDATION_REGEX = "-?\\d+";

    public static final String DELETE_MULTIPLE_PERSON_COMMA_VALIDATION_REGEX =
            "((-?\\d([\\s+]*)\\,([\\s+]*)(?=-?\\d))|-?\\d)+";

    public static final String DELETE_MULTIPLE_PERSON_WHITESPACE_VALIDATION_REGEX =
            "(((-?\\d)([\\s]+)(?=-?\\d))|-?\\d)+";


    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);
        String preamble = argMultimap.getPreamble();

        if (preamble.equals("")) { // code block for delete for a tag
            DeleteCommand tagList = getDeleteCommandForTags(argMultimap);
            if (tagList != null) {
                return tagList;
            }
        } else if (preamble.matches(DELETE_ONE_PERSON_VALIDATION_REGEX)) { // code block for delete for a person
            try {
                Index index = ParserUtil.parseIndex(args);
                return new DeleteCommand(index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        } else if (preamble.matches(DELETE_MULTIPLE_PERSON_COMMA_VALIDATION_REGEX)) {
            //code block for delete multiple persons, input string separated by comma
            try {
                ArrayList<Index> deletePersons = ParserUtil.parseIndexes(args, ",");
                return new DeleteCommand(deletePersons);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        } else if (preamble.matches(DELETE_MULTIPLE_PERSON_WHITESPACE_VALIDATION_REGEX)) {
            //code block for delete multiple persons, input indexes separated by whitespace
            try {
                ArrayList<Index> deletePersons = ParserUtil.parseIndexes(args, " ");
                return new DeleteCommand(deletePersons);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        }

        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses {@code oneBasedIndexes} separated with commas into a {@Code ArrayList<Index>} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @param splitString is the sign used to separate indexes, can be either comma or whitespace(s)
     * @throws IllegalValueException if one of the specified indexes is invalid (not non-zero unsigned integer).
     *
     */
    public static ArrayList<Index> parseIndexes(String oneBasedIndexes, String splitString)
            throws IllegalValueException {
        String trimmedIndexes = oneBasedIndexes.trim();
        String[] indexes;

        if (splitString.equals(",")) {
            indexes = trimmedIndexes.split(splitString);
        } else {
            indexes = trimmedIndexes.split(" +");
        }

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

        remark = getAllRemarks(argMultimap);

        return new RemarkCommand(index, remark);
    }

    /**
     * Concatenates all the remarks into one string.
     * @param argumentMultimap
     * @return the remark contains all the remark string.
     */
    private Remark getAllRemarks(ArgumentMultimap argumentMultimap) {

        List<String> allRemarks = argumentMultimap.getAllValues(PREFIX_REMARK);
        String allRemarkString = "";

        if (allRemarks.size() > 1 || (allRemarks.size() == 1 && (!allRemarks.get(0).equals("")))) {
            allRemarkString = allRemarks.toString();
        }

        return new Remark(allRemarkString);
    }

}
```
###### \java\seedu\address\model\AddressBook.java
``` java

    public void setPhotoPaths(List<PhotoPath> photoPaths) throws DuplicatePhotoPathException {
        this.photoPaths.setPhotoPaths(photoPaths);
    }

    /**
     * Adds a photopath to the address book.
     *
     * @throws DuplicatePhotoPathException if an equivalent photo path already exists.
     */
    public void addPhotoPath(PhotoPath newPhotoPath) throws DuplicatePhotoPathException {
        photoPaths.add(newPhotoPath);
    }

    /**
     * Checks if the master list {@link #photoPaths} has every photo path being used.
     *  @return true if all photo paths in the master list are being used
     */
    public boolean hasAllPhotoPathsInUse () {
        List<PhotoPath> masterList = new ArrayList<>();
        for (ReadOnlyPerson person: persons) {
            masterList.add(person.getPhotoPath());
        }
        return masterList.containsAll(photoPaths.toList());
    }

    /**
     *  Gets the unused photo paths in the master list {@link #photoPaths}
     *  @return {@code List<PhotoPath>} of photo paths not being used by any person
     *  @see #hasAllPhotoPathsInUse()
     */
    public List<PhotoPath> getUnusedPhotoPaths () {
        List<PhotoPath> actualList = new ArrayList<>();
        for (ReadOnlyPerson person: persons) {
            PhotoPath thisPhotoPath = person.getPhotoPath();
            if (!thisPhotoPath.value.equals("")) {
                actualList.add(thisPhotoPath);
            }
        }
        List<PhotoPath> masterList = photoPaths.toList();

        masterList.removeAll(actualList);
        return masterList;
    }

    /**
     * Removes all the unused photos specified by the unused photo paths
     * @see #getUnusedPhotoPaths()
     */
    public void removeAllUnusedPhotosAndPaths() throws PhotoPathNotFoundException {
        List<PhotoPath> unusedPhotoPathList = getUnusedPhotoPaths();
        for (PhotoPath unusedPhotoPath : unusedPhotoPathList) {
            removeContactPhoto(unusedPhotoPath);
            this.photoPaths.remove(unusedPhotoPath);
            logger.info("Delete photo and its path: " + unusedPhotoPath);
        }
    }

    /**
     * Updates the master list of photo paths saved in the default folder of this
     * address book and delete the empty paths.
     */
    public void updatePhotoPathSavedInMasterList() {
        final File folder = new File(FILE_SAVED_PARENT_PATH);
        if (!folder.exists()) {
            return;
        }

        if (!folder.isDirectory()) {
            assert false : "The File is not the default folder to save photos!";
        }

        for (File photo : folder.listFiles()) {
            try {
                //covert the photo path string to standard format in the app
                String photoPathString = photo.getPath().replace("\\", "/");
                PhotoPath thisPhotoPath = new PhotoPath(photoPathString);

                if (!this.photoPaths.contains(thisPhotoPath)) {
                    this.photoPaths.add(thisPhotoPath);
                }
            } catch (IllegalValueException e) {
                assert false : "The string of the photo path has wrong format!";
            }
        }

        //delete empty path in the master list
        for (PhotoPath photoPath : this.photoPaths) {
            if (photoPath.value.equals("")) {
                try {
                    this.photoPaths.remove(photoPath);
                } catch (PhotoPathNotFoundException e) {
                    assert false : "This photo path cannot be found: " + photoPath;
                }
            }
        }
    }

    /**
     * Removes the photo of the specified contact.
     * @param photoPath
     */
    public void removeContactPhoto(PhotoPath photoPath) {
        removeAppFile(photoPath.value);
    }

    /**
     * Check whether the contact photo is the default photo
     * @param photoPath of the photo
     * @return true if the photo is the default photo
     */
    public static boolean isDefaultPhoto(PhotoPath photoPath) {
        String photoPathValue = photoPath.value;
        return photoPathValue.equals(DEFAULT_PHOTO_PATH);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(ReadOnlyPerson key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            EventsCenter.getInstance().post(new PersonHasBeenDeletedEvent(key));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes {@code keys} from this {@code AddressBook}
     * @throws PersonNotFoundException if one of the {@code keys} is not in this {@code AddressBook}.
     */
    public boolean removePersons(ArrayList<ReadOnlyPerson> keys) throws PersonNotFoundException {
        for (ReadOnlyPerson key : keys) {
            removePerson(key);
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

    /** Adds the given photo path */
    void addPhotoPath(PhotoPath photoPath) throws DuplicatePhotoPathException;

```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);

        logger.fine("Updating all photopaths...");
        this.addressBook.updatePhotoPathSavedInMasterList();

        logger.fine("Deleting all unused photos...");
        try {
            this.addressBook.removeAllUnusedPhotosAndPaths();
        } catch (PhotoPathNotFoundException e) {
            assert false : "Some of the photopaths cannot be found!";
        }

        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        currentTheme = userPrefs.getCurrentTheme();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
        addressBook.removePerson(target);
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

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void addPhotoPath(PhotoPath photoPath) throws DuplicatePhotoPathException {
        addressBook.addPhotoPath(photoPath);
        indicateAddressBookChanged();
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
        return test.equals(GENDER_VALIDATION_WORD1) || test.equals(GENDER_VALIDATION_WORD2) || test.equals("");
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
        return test.matches(MATRIC_NO_VALIDATION_REGEX) || test.equals("");
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
###### \java\seedu\address\model\photo\PhotoPath.java
``` java
/**
 * Represents the path of a person's photo in the address book.
 */
public class PhotoPath {

    public static final String FILE_SAVED_PARENT_PATH = "src/main/resources/images/contactPhotos/";
    public static final String MESSAGE_APP_PHOTOPATH_CONSTRAINTS =
            "The app photo path should be a string starting with '" + FILE_SAVED_PARENT_PATH
                    + "', following by the file name, like'photo.jpg'.";

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
        if (test.equals("")) {
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
###### \java\seedu\address\storage\XmlAdaptedPhotoPath.java
``` java
/**
 * JAXB-friendly adapted version of the photo path.
 */
public class XmlAdaptedPhotoPath {

    @XmlValue
    private String photoPathName;

    /**
     * Constructs an XmlAdaptedTag.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPhotoPath() {}

    /**
     * Converts a given PhotoPath into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedPhotoPath(PhotoPath source) {
        photoPathName = source.value;
    }

    /**
     * Converts this jaxb-friendly adapted PhotoPath object into the model's PhotoPath object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public PhotoPath toModelType() throws IllegalValueException {
        return new PhotoPath(photoPathName);
    }

}
```
###### \java\seedu\address\storage\XmlSerializableAddressBook.java
``` java
/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableAddressBook implements ReadOnlyAddressBook {

    @XmlElement
    private List<XmlAdaptedPerson> persons;
    @XmlElement
    private List<XmlAdaptedTag> tags;
    @XmlElement
    private List<XmlAdaptedPhotoPath> photoPaths;
    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAddressBook() {
        persons = new ArrayList<>();
        tags = new ArrayList<>();
        photoPaths = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableAddressBook(ReadOnlyAddressBook src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        photoPaths.addAll(src.getPhotoPathList().stream().map(XmlAdaptedPhotoPath::new).collect(Collectors.toList()));
    }

    @Override
    public ObservableList<PhotoPath> getPhotoPathList() {
        final ObservableList<PhotoPath> photoPaths = this.photoPaths.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return FXCollections.unmodifiableObservableList(photoPaths);
    }

```
###### \java\seedu\address\ui\MainWindow.java
``` java
/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {

    private static final String ICON = "/images/icon.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 450;
    private static final String VIEW_PATH = "/view/";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private TagColorMap tagColorMap;
    private PersonInfoPanel personInfoPanel;
    private InfoPanel infoPanel;
    private PersonListPanel personListPanel;
    private TagListPanel tagListPanel;
    private Config config;
    private UserPrefs prefs;

    @FXML
    private StackPane infoPlaceholder;

    @FXML
    private StackPane personInfoPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane tagListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        setWindowDefaultTheme(prefs);
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        setAccelerators();
        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    //author@@ nbriannl
    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        infoPanel = new InfoPanel();
        infoPlaceholder.getChildren().add(infoPanel.getRoot());

        personInfoPanel = new PersonInfoPanel();
        personInfoPlaceholder.getChildren().add(personInfoPanel.getRoot());

        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        tagListPanel = new TagListPanel(logic.getTagList());
        tagListPanelPlaceholder.getChildren().add(tagListPanel.getRoot());
        logic.checkAllMasterListTagsAreBeingUsed();

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getAddressBookFilePath(),
                logic.getFilteredPersonList().size());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    /**
     * Changes the current theme to the given theme.
     */
    public void handleChangeTheme(String theme) {
        if (getRoot().getStylesheets().size() > 1) {
            getRoot().getStylesheets().remove(1);
        }
        getRoot().getStylesheets().add(VIEW_PATH + theme);
    }

    private void setWindowDefaultTheme(UserPrefs prefs) {
        getRoot().getStylesheets().add(prefs.getCurrentTheme());
    }

    String getCurrentTheme() {
        return getRoot().getStylesheets().get(1);
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public PersonListPanel getPersonListPanel() {
        return this.personListPanel;
    }

    void releaseResources() {
        infoPanel.freeResources();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }

    @Subscribe
    private void handleChangeThemeEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleChangeTheme(event.themeToChangeTo);
        logic.setCurrentTheme(getCurrentTheme());
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
###### \java\seedu\address\ui\PersonInfoPanel.java
``` java

/**
 * A UI component that displays a person's data on the main panel
 */
public class PersonInfoPanel extends UiPart<Region> {

    private static final String FXML = "PersonInfoPanel.fxml";
    private static String DEFAULT_PHOTO_PATH = "/images/defaultPhoto.jpg";

    private ReadOnlyPerson person;
    private ReadOnlyPerson currentlyViewedPerson;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Circle photoCircle;
    @FXML
    private Label name;
    @FXML
    private Label gender;
    @FXML
    private Label matricNo;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;


    public PersonInfoPanel() {
        super(FXML);
        this.person = null;
        loadDefaultPerson();

        registerAsAnEventHandler(this);
    }

    /**
     * Loads the default person when the app is first started
     */
    private void loadDefaultPerson() {
        name.setText("Person X");
        gender.setText("");
        matricNo.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        birthday.setText("");
        remark.setText("");
        tags.getChildren().clear();

        setDefaultContactPhoto();
        currentlyViewedPerson = null;
        logger.info("Currently Viewing: Default Person");
    }

    /**
     * Updates info with person selected
     */
    private void loadPerson(ReadOnlyPerson person) {
        this.person = person;
        tags.getChildren().clear();
        initTags(person);

        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        gender.textProperty().bind(Bindings.convert(person.genderProperty()));
        matricNo.textProperty().bind(Bindings.convert(person.matricNoProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        remark.textProperty().bind(Bindings.convert(person.remarkProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });

        loadPhoto(person);

        currentlyViewedPerson = person;
        logger.info("Currently Viewing: " + currentlyViewedPerson.getName());
    }

```
###### \java\seedu\address\ui\PersonInfoPanel.java
``` java
    /**
     * Set the default contact photo.
     */
    public void setDefaultContactPhoto() {
        Image defaultImage = new Image(MainApp.class.getResourceAsStream(DEFAULT_PHOTO_PATH));
        photoCircle.setFill(new ImagePattern(defaultImage));
    }

    /**
     * Load the photo of the specified person.
     * @param person
     */
    public void loadPhoto(ReadOnlyPerson person) {
        String prefix = "src/main/resources";
        //String photoPath = person.getPhotoPath().value.substring(prefix.length());
        String photoPathString = person.getPhotoPath().value;
        Image image;

        if (photoPathString.equals("")) {  //default male and female photos
            image = getDefaultPhotoByGender();
        } else {
            File contactImg = new File(photoPathString);
            if (contactImg.exists() && !contactImg.isDirectory()) {
                String url = contactImg.toURI().toString();
                image = new Image(url);
            } else {
                image = getDefaultPhotoByGender();
            }
            image = new Image(MainApp.class.getResourceAsStream(photoPath));

        } else {
            File contactImg = new File(photoPath);
            String url = contactImg.toURI().toString();
            image = new Image(url);
        }

        photoCircle.setFill(new ImagePattern(image));
    }

    /**
     * Get the default photo by gender. If the gender is not specifed, then return the default photo without gender.
     * @return Image of the according default photo
     */
    private Image getDefaultPhotoByGender() {
        String photoPathString = "";

        if (person.getGender().toString().equals("Male")) {
            photoPathString = "/images/default_male.jpg";
        } else if (person.getGender().toString().equals("Female")) {
            photoPathString = "/images/default_female.jpg";
        } else {
            photoPathString = "/images/defaultPhoto.jpg";
        }
        return new Image(MainApp.class.getResourceAsStream(photoPathString));
    }

```
###### \java\seedu\address\ui\TagColorMap.java
``` java
    /**
     * Updates the color index to pick a new color for the new tag.
     */
    private static void updateColorIndex() {
        if (colorIndex == NUM_COLORS - 1) {
            colorIndex = 0;
        } else {
            colorIndex++;
        }
    }
}
```
###### \resources\view\MainWindow.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">
    <stylesheets>
        <URL value="@Extensions.css"/>
    </stylesheets>
    <MenuBar fx:id="menuBar" maxHeight="22.0" prefHeight="22.0" prefWidth="2000.0" VBox.vgrow="NEVER">
        <Menu mnemonicParsing="false" text="File">
            <MenuItem mnemonicParsing="false" onAction="#handleExit" text="Exit"/>
        </Menu>
        <Menu mnemonicParsing="false" text="Help">
            <MenuItem fx:id="helpMenuItem" mnemonicParsing="false" onAction="#handleHelp" text="Help"/>
        </Menu>
    </MenuBar>
    <HBox prefWidth="1020.0">
        <VBox fx:id="personList" minWidth="200" prefWidth="250.0">
            <StackPane fx:id="personListPanelPlaceholder" prefWidth="145.0" VBox.vgrow="ALWAYS"/>
        </VBox>
        <AnchorPane>

            <SplitPane dividerPositions="0.2" orientation="VERTICAL" prefWidth="2000" AnchorPane.bottomAnchor="0.0"
                       AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0">

```
###### \resources\view\PersonInfoPanel.fxml
``` fxml

<?import javafx.geometry.Insets?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.image.Image?>
<?import javafx.scene.image.ImageView?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.FlowPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.shape.Circle?>
<?import javafx.scene.text.Font?>

```
###### \resources\view\PersonInfoPanel.fxml
``` fxml
<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="250.0"
            prefWidth="550.0" xmlns="http://javafx.com/javafx/8.0.111" xmlns:fx="http://javafx.com/fxml/1">

    <HBox prefHeight="250.0" prefWidth="448.0" AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0"
          AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0">
        <VBox prefHeight="250.0" prefWidth="198.0">
            <AnchorPane prefHeight="216.0" prefWidth="210.0" VBox.vgrow="ALWAYS">
                <ImageView fitHeight="210.0" fitWidth="221.0" pickOnBounds="true" preserveRatio="true">
                    <image>
                        <Image url="@../images/personInfoBg.jpg"/>
                    </image>
                </ImageView>
                <Circle fx:id="photoCircle" fill="WHITE" layoutX="111.0" layoutY="100.0" radius="86.0"
                        stroke="floralwhite" strokeType="INSIDE" strokeWidth="10"/>
                <VBox.margin>
                    <Insets/>
                </VBox.margin>
            </AnchorPane>
        </VBox>
        <VBox alignment="TOP_LEFT" prefHeight="250.0" prefWidth="237.0" AnchorPane.bottomAnchor="0.0"
              AnchorPane.leftAnchor="20.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0" HBox.hgrow="ALWAYS">
            <StackPane alignment="TOP_LEFT" prefHeight="20.0" prefWidth="304.0" VBox.vgrow="NEVER">
                <Label fx:id="name" prefHeight="35.0" prefWidth="300.0" styleClass="display_big_label" text="\$name">
                    <font>
                        <Font size="24.0"/>
                    </font>
                </Label>
                <VBox.margin>
                    <Insets left="15.0" top="20.0"/>
                </VBox.margin>
            </StackPane>
            <FlowPane fx:id="tags" columnHalignment="CENTER" minHeight="20.0" minWidth="100.0" prefHeight="15.0"
                      prefWidth="207.0" VBox.vgrow="ALWAYS">
                <VBox.margin>
                    <Insets bottom="0.0" left="15.0" right="10.0" top="0.0"/>
                </VBox.margin>
            </FlowPane>
            <HBox prefHeight="100.0" prefWidth="200.0" VBox.vgrow="ALWAYS">
                <VBox prefHeight="150.0" prefWidth="10.0" HBox.hgrow="ALWAYS">
                    <Label styleClass="display_small_label" text="Gender"/>
                    <Label styleClass="display_small_label" text="Matric No"/>
                    <Label styleClass="display_small_label" text="Phone"/>
                    <Label styleClass="display_small_label" text="Address"/>
                    <Label styleClass="display_small_label" text="Email"/>
                    <Label styleClass="display_small_label" text="Birthday"/>
                    <Label styleClass="display_small_label" text="Remark"/>
                </VBox>
                <VBox prefHeight="200.0" prefWidth="100.0" HBox.hgrow="ALWAYS">
                    <Label fx:id="gender" prefHeight="17.0" prefWidth="196.0" styleClass="display_small_value"
                           text="\$gender" VBox.vgrow="ALWAYS"/>
                    <Label fx:id="matricNo" prefHeight="17.0" prefWidth="199.0" styleClass="display_small_value"
                           text="\$matricNo"/>
                    <Label fx:id="phone" prefHeight="17.0" prefWidth="193.0" styleClass="display_small_value"
                           text="\$phone"/>
                    <Label fx:id="address" prefHeight="17.0" prefWidth="196.0" styleClass="display_small_value"
                           text="\$address"/>
                    <Label fx:id="email" prefHeight="17.0" prefWidth="188.0" styleClass="display_small_value"
                           text="\$email"/>
                    <Label fx:id="birthday" prefHeight="17.0" prefWidth="214.0" styleClass="display_small_value"
                           text="\$birthday"/>
                    <Label fx:id="remark" prefHeight="17.0" prefWidth="186.0" styleClass="display_small_value"
                           text="\$remark"/>
                </VBox>
                <VBox.margin>
                    <Insets bottom="10.0" left="15.0"/>
                </VBox.margin>
            </HBox>
            <HBox.margin>
                <Insets/>
            </HBox.margin>
        </VBox>
    </HBox>
</AnchorPane>
```
