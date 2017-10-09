package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;

import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

public class RemarkCommandParserTest {
    private static final String REMARK_EMPTY = " " + PREFIX_REMARK;
    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_remarkSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + REMARK_EMPTY + VALID_REMARK_COFFEE;

        RemarkCommand expectedCommand = new RemarkCommand(targetIndex, VALID_REMARK_COFFEE);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_remarkUnspecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + REMARK_EMPTY;

        RemarkCommand expectedCommand = new RemarkCommand(targetIndex, "");

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
