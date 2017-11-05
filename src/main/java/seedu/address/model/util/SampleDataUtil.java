package seedu.address.model.util;

import static seedu.address.logic.commands.PhotoCommand.DEFAULT_PHOTO_PATH;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.MatricNo;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PhotoPath;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.timetable.Timetable;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        try {
            return new Person[]{
                new Person(new Name("Labayna Neil Brian Narido"), new Gender("Male"), new MatricNo("A0155006R"),
                        new Phone("96474278"), new Email("neilbrian.nl@gmail.com"),
                        new Address("Blk 630, Yishun St 61"), new Timetable("http://modsn.us/VLQ3g"),
                        new Remark(""), new PhotoPath(DEFAULT_PHOTO_PATH), getTagSet("CS2101", "CS2103T"),
                        new Birthday("02101995")),
                new Person(new Name("Fan Yuting"), new Gender("Female"), new MatricNo("A0162531F"),
                        new Phone("96855667"), new Email("april06@gmail.com"),
                        new Address("Prince George Park Residences NUS"),
                        new Timetable("http://modsn.us/aHN0q"),
                        new Remark("[Likes to drink tea]"), new PhotoPath(DEFAULT_PHOTO_PATH),
                        getTagSet("CS2105", "CS2101", "CS2103T"), new Birthday("16021993")),
                new Person(new Name("Tsai Yu Hsuan"), new Gender("Female"), new MatricNo("A0163331N"),
                        new Phone("95569202"), new Email("cindy93@gmail.com"),
                        new Address("7 Serangoon Avenue 2, #13-07"),
                        new Timetable("http://modsn.us/VLQ3g"),
                        new Remark(""), new PhotoPath(DEFAULT_PHOTO_PATH), getTagSet("CS2101", "CS2103T"),
                        new Birthday("18081993")),
                new Person(new Name("Zachary Tang"), new Gender("Male"), new MatricNo("A0133788N"),
                        new Phone("91676489"), new Email("zachtang@gmail.com"),
                        new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                        new Timetable("http://modsn.us/9UKRW"),
                        new Remark("[Can't drink coffee]"), new PhotoPath(DEFAULT_PHOTO_PATH),
                        getTagSet("CS2101", "CS2103"), new Birthday("23101995")),
                new Person(new Name("Isabel Tan"), new Gender("Female"), new MatricNo("A0466707R"),
                        new Phone("92492021"), new Email("isabel.tan@gmail.com"),
                        new Address("Blk 47 Tampines Street 20, #17-35"), new Timetable("http://modsn.us/9UKRW"),
                        new Remark(""), new PhotoPath(DEFAULT_PHOTO_PATH), getTagSet("ComputingClub"),
                        new Birthday("07121998")),
                new Person(new Name("Thomas Spencer"), new Gender("Male"), new MatricNo("A0155531K"),
                        new Phone("92624417"), new Email("royb@example.com"),
                        new Address("RC4 NUS UTown"), new Timetable("http://modsn.us/0ECRP"),
                        new Remark("[From London]"), new PhotoPath(DEFAULT_PHOTO_PATH),
                        getTagSet("ExchangeStudent", "Business"), new Birthday("05111998")),
                new Person(new Name("Bobby Chin"), new Gender("Male"), new MatricNo("A0177348L"),
                        new Phone("99635723"), new Email("chinnybob@gmail.com"),
                        new Address("Blk 320, Ang Mo Kio St 32"), new Timetable("http://modsn.us/9UKRW"),
                        new Remark(""), new PhotoPath(DEFAULT_PHOTO_PATH),
                        getTagSet("Arts", "Sociology"), new Birthday("01101994"))
            };
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAb = new AddressBook();
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) throws IllegalValueException {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }
        return tags;
    }
}
