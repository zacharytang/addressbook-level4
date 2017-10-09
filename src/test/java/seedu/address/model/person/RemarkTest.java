package seedu.address.model.person;

import org.junit.Test;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Remark;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RemarkTest {

    @Test
    public void equals() {
        Remark likeCoffeeRemark = new Remark("Likes to drink coffee.");
        Remark capFiveRemark = new Remark("Got CAP 5.0.");

        // same object -> returns true
        assertTrue(likeCoffeeRemark.equals(likeCoffeeRemark));

        // same object -> returns true
        assertTrue(capFiveRemark.equals(capFiveRemark));

        // different types -> returns false
        assertFalse(capFiveRemark.equals(1));

        // null -> returns false
        assertFalse(capFiveRemark.equals(null));

        // different objects -> returns false
        assertFalse(likeCoffeeRemark.equals(capFiveRemark));
    }
}
