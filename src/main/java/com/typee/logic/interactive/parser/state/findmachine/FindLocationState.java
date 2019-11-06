package com.typee.logic.interactive.parser.state.findmachine;

import static com.typee.logic.interactive.parser.CliSyntax.PREFIX_LOCATION;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.typee.logic.interactive.parser.ArgumentMultimap;
import com.typee.logic.interactive.parser.Prefix;
import com.typee.logic.interactive.parser.state.State;
import com.typee.logic.interactive.parser.state.StateTransitionException;
import com.typee.model.engagement.Location;

public class FindLocationState extends State {

    private static final String MESSAGE_CONSTRAINTS = "Please enter a location to search for, prefixed by \"l/\".";

    protected FindLocationState(ArgumentMultimap soFar) {
        super(soFar);
    }

    @Override
    public State transition(ArgumentMultimap newArgs) throws StateTransitionException {
        requireNonNull(newArgs);

        Optional<String> location = newArgs.getValue(PREFIX_LOCATION);
        performGuardChecks(newArgs, location);
        if (location.isPresent()) {
            collateArguments(this, newArgs, PREFIX_LOCATION);
        }

        return new FindAttendeesState(soFar);
    }

    private void performGuardChecks(ArgumentMultimap newArgs, Optional<String> location)
            throws StateTransitionException {
        disallowDuplicatePrefix(newArgs);
        if (location.isPresent()) {
            enforceValidity(location.get());
        }
    }

    private void enforceValidity(String location) throws StateTransitionException {
        if (!Location.isValid(location)) {
            throw new StateTransitionException(Location.MESSAGE_CONSTRAINTS);
        }

    }

    @Override
    public String getStateConstraints() {
        return MESSAGE_CONSTRAINTS;
    }

    @Override
    public boolean isEndState() {
        return false;
    }

    @Override
    public Prefix getPrefix() {
        return PREFIX_LOCATION;
    }
}
