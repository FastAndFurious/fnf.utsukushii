package com.zuehlke.fnf.actorbus;

import lombok.Data;

/**
 *   represents a "reminder" that repeatedly drops into the mailbox
 */
@Data
public class ScheduledTask {

    private final boolean repeat;
    private final int millies;
    private final String taskId;

}
