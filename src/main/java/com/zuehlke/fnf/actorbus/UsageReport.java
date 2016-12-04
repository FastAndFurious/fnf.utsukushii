package com.zuehlke.fnf.actorbus;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UsageReport {

    private final List<UsageReportEntry> entries;

    public UsageReport() {
        entries = new ArrayList<>();
    }

}
