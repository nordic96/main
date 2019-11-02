package com.typee.logic.interactive.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.typee.logic.commands.AddCommand;
import com.typee.logic.commands.Command;
import com.typee.logic.commands.CommandResult;
import com.typee.logic.interactive.parser.state.State;
import com.typee.logic.interactive.parser.state.StateTransitionException;
import com.typee.logic.interactive.parser.state.addmachine.TypeState;
import com.typee.logic.parser.Prefix;
import com.typee.logic.parser.exceptions.ParseException;

public class Parser implements InteractiveParser {

    private static final String MESSAGE_CLEAR_ALL = "// clear";
    private static final String MESSAGE_INVALID_COMMAND = "No such command exists!";

    private State currentState;

    public Parser() {
        this.currentState = null;
    }

    private boolean isActive() {
        return currentState != null;
    }

    @Override
    public void parseInput(String commandText) throws ParseException {
        if (commandText.equalsIgnoreCase(MESSAGE_CLEAR_ALL)) {
            resetParser();
            return;
        }

        Prefix[] arrayOfPrefixes = extractPrefixes(commandText);
        parse(commandText, arrayOfPrefixes);
    }

    private void parse(String commandText, Prefix... prefixes) throws ParseException {
        try {
            if (isActive()) {
                parseActive(commandText, prefixes);
            } else {
                parseInactive(commandText, prefixes);
            }
        } catch (StateTransitionException e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Override
    public CommandResult fetchResult() {
        assert currentState != null : "This shouldn't happen theoretically.";
        return new CommandResult(currentState.getStateConstraints());
    }

    @Override
    public boolean hasParsedCommand() {
        return currentState.isEndState();
    }

    @Override
    public Command makeCommand() {
        return null;
    }

    private void resetParser() {
        this.currentState = null;
    }

    private Prefix[] extractPrefixes(String commandText) {
        Pattern pattern = Pattern.compile("^[a-zA-z]/$");
        Matcher matcher = pattern.matcher(commandText);
        List<Prefix> prefixes = getMatches(matcher);

        // Convert to an array to allow the values to be processed by varargs.
        return prefixes.toArray(Prefix[]::new);
    }

    private List<Prefix> getMatches(Matcher matcher) {
        List<Prefix> prefixes = new ArrayList<>();
        while (matcher.find()) {
            prefixes.add(new Prefix(matcher.group()));
        }
        return prefixes;
    }

    private void parseActive(String commandText, Prefix... prefixes) throws ParseException {
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(commandText.trim(), prefixes);
        try {
            currentState = currentState.transition(new ArgumentMultimap(), argumentMultimap);
        } catch (StateTransitionException e) {
            throw new ParseException(currentState.getStateConstraints());
        }
    }

    private void parseInactive(String commandText, Prefix... prefixes) throws ParseException,
            StateTransitionException {
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(commandText.trim(), prefixes);
        String commandWord = getCommandWord(commandText);
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            currentState = (new TypeState()).transition(new ArgumentMultimap(), argumentMultimap);
            break;

        default:
            throw new ParseException(MESSAGE_INVALID_COMMAND);
        }
    }

    private String getCommandWord(String commandText) throws ParseException {
        String trimmedCommandText = commandText.trim();
        List<String> commandWords = getAllCommandWords(trimmedCommandText);

        // If there is no unique command word, throw an exception.
        if (commandWords.size() != 1) {
            throw new ParseException(MESSAGE_INVALID_COMMAND);
        }

        return commandWords.get(0);
    }

    private List<String> getAllCommandWords(String trimmedCommandText) {
        Pattern pattern = Pattern.compile("^[a-zA-z]+");
        Matcher matcher = pattern.matcher(trimmedCommandText);
        List<String> commandWords = new ArrayList<>();
        while (matcher.find()) {
            commandWords.add(matcher.group());
        }
        return commandWords;
    }

}
