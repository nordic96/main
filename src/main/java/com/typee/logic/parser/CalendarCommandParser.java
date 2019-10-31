package com.typee.logic.parser;

import java.time.LocalDate;

import com.typee.commons.core.Messages;
import com.typee.logic.commands.CalendarCommand;
import com.typee.logic.commands.CalendarDateDisplayEngagementsCommand;
import com.typee.logic.commands.CalendarNextMonthCommand;
import com.typee.logic.commands.CalendarPreviousMonthCommand;
import com.typee.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CalendarCommand object.
 */
public class CalendarCommandParser implements Parser<CalendarCommand> {

    public static final String INVALID_CALENDAR_COMMAND_MESSAGE = "Invalid calendar command.";

    /**
     * Parses the given {@code String} of arguments in the context of the CalendarCommand
     * and returns a CalendarCommand object for execution.
     * @throws ParseException If the user input does not conform the expected format.
     */
    @Override
    public CalendarCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CalendarCommand.MESSAGE_USAGE));
        }
        String[] individualArgs = trimmedArgs.split("\\s+");
        String calendarCommandType = individualArgs[0].toLowerCase();
        switch (calendarCommandType) {
        case CalendarDateDisplayEngagementsCommand.COMMAND_WORD:
            return prepareCalendarDateDisplayEngagementsCommand(individualArgs);
        case CalendarNextMonthCommand.COMMAND_WORD:
            return prepareCalendarNextMonthCommand(individualArgs);
        case CalendarPreviousMonthCommand.COMMAND_WORD:
            return prepareCalendarPreviousMonthCommand(individualArgs);
        default:
            throw new ParseException(INVALID_CALENDAR_COMMAND_MESSAGE);
        }
    }

    /**
     * Prepares a {@code CalendarDateDisplayEngagementsCommand} based on the specified arguments.
     * @param individualArgs The specified arguments.
     * @return A {@code CalendarDateDisplayEngagementsCommand} based on the specified arguments.
     * @throws ParseException If the specified arguments are invalid.
     */
    private CalendarDateDisplayEngagementsCommand prepareCalendarDateDisplayEngagementsCommand(
            String[] individualArgs) throws ParseException {
        if (individualArgs.length != 2) {
            throw new ParseException(CalendarDateDisplayEngagementsCommand.INVALID_COMMAND_FORMAT);
        }
        LocalDate date = ParserUtil.parseDate(individualArgs[1]);
        return new CalendarDateDisplayEngagementsCommand(date);
    }

    /**
     * Prepares a {@code CalendarNextMonthCommand}.
     * @param individualArgs The specified arguments.
     * @return A {@code CalendarNextMonthCommand}.
     * @throws ParseException If the specified arguments are invalid.
     */
    private CalendarNextMonthCommand prepareCalendarNextMonthCommand(
            String[] individualArgs) throws ParseException {
        if (individualArgs.length != 1) {
            throw new ParseException(CalendarNextMonthCommand.INVALID_COMMAND_FORMAT);
        }
        return new CalendarNextMonthCommand();
    }

    /**
     * Prepares a {@code CalendarPreviousMonthCommand}.
     * @param individualArgs The specified arguments.
     * @return A {@code CalendarPreviousMonthCommand}.
     * @throws ParseException If the specified arguments are invalid.
     */
    private CalendarPreviousMonthCommand prepareCalendarPreviousMonthCommand(
            String[] individualArgs) throws ParseException {
        if (individualArgs.length != 1) {
            throw new ParseException(CalendarPreviousMonthCommand.INVALID_COMMAND_FORMAT);
        }
        return new CalendarPreviousMonthCommand();
    }

}