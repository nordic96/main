package com.typee.commons.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessagesTest {
    @Test
    void messageValidTest() {
        Messages messages = new Messages();
        assertEquals(Messages.MESSAGE_ENGAGEMENT_LISTED_OVERVIEW, "%1$d engagements listed!");
        assertEquals(Messages.MESSAGE_UNKNOWN_COMMAND, "Unknown command");
        assertEquals(Messages.MESSAGE_INVALID_COMMAND_FORMAT, "Invalid command format! \n%1$s");
        assertEquals(Messages.MESSAGE_INVALID_ENGAGEMENT_DISPLAYED_INDEX, "The engagement index provided is invalid");
    }
}
