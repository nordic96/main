package com.typee.logic.parser;

import com.typee.commons.core.Messages;
import com.typee.logic.commands.SortCommand;
import com.typee.logic.parser.exceptions.ParseException;

/**
 * Parses a sort command and returns a {@code SortCommand} object
 */
public class SortCommandParser implements Parser<SortCommand> {

    private String trimmedArgs;
    @Override
    public SortCommand parse(String args) throws ParseException {
        trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(ParserUtil.parseComparator(parseOrder()));
    }

    /**
     * Parses user command and returns a formulated ordering method
     * @return {@code String} of formulated ordering method
     * @throws ParseException if the input format is incorrect
     */
    private String parseOrder() throws ParseException {
        try {
            switch (Order.valueOf(trimmedArgs.toUpperCase())) {

            case STARTA:
                return "START_TIME";

            case STARTD:
                return "START_TIME_REVERSE";

            case ENDA:
                return "END_TIME";

            case ENDD:
                return "END_TIME_REVERSE";

            case NAMEA:
                return "ALPHABETICAL";

            case NAMED:
                return "ALPHABETICAL_REVERSE";

            case PRIORITYA:
                return "PRIORITY";

            case PRIORITYD:
                return "PRIORITY_REVERSE";

            default:
                return "Not supposed to reach here";
            }
        } catch (IllegalArgumentException e) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }
}

/**
 * Specifies expected user input format
 */
enum Order {
    STARTA, STARTD, ENDA, ENDD, NAMEA, NAMED, PRIORITYA, PRIORITYD
}
