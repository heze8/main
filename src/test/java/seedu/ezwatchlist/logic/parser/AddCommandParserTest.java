package seedu.ezwatchlist.logic.parser;

import static seedu.ezwatchlist.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.WATCHED_DESC_AMY;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.DATE_DESC_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.VALID_WATCHED_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.VALID_TAG_KID_FRIENDLY;
import static seedu.ezwatchlist.logic.commands.CommandTestUtil.VALID_TAG_HORROR;
import static seedu.ezwatchlist.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ezwatchlist.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ezwatchlist.testutil.TypicalPersons.AMY;
import static seedu.ezwatchlist.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.ezwatchlist.logic.commands.AddCommand;
import seedu.ezwatchlist.model.person.Address;
import seedu.ezwatchlist.model.person.Email;
import seedu.ezwatchlist.model.person.Name;
import seedu.ezwatchlist.model.person.Person;
import seedu.ezwatchlist.model.person.Phone;
import seedu.ezwatchlist.model.tag.Tag;
import seedu.ezwatchlist.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_KID_FRIENDLY).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_AMY + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_BOB + WATCHED_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_KID_FRIENDLY, VALID_TAG_HORROR)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + DATE_DESC_AMY + WATCHED_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + VALID_WATCHED_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + VALID_DESCRIPTION_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_WATCHED_BOB + VALID_DESCRIPTION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_KID_FRIENDLY, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + DATE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + DATE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
