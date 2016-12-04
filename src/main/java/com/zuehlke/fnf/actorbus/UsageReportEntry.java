package com.zuehlke.fnf.actorbus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageReportEntry {
    private String name;
    private int busyTime;
    private int idleTime;
    private int watchPeriod;
    private int usage;
}
