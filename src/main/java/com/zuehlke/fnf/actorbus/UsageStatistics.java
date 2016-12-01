package com.zuehlke.fnf.actorbus;

import java.util.ArrayDeque;

class UsageStatistics {

    private final String name;
    private int total_busy;
    private int total_idle;
    private final int period; // period in milliseconds to keep statistics of
    final private ArrayDeque<UsageRecord> records = new ArrayDeque<>();

    UsageStatistics(String name, int period) {
        this.period = period;
        this.name = name;
    }

    public UsageStatistics with(UsageRecord newRecord) {
        total_idle += newRecord.d_idle;
        total_busy += newRecord.d_busy;
        records.add(newRecord);
        while ( records.size() > 1 && total_idle + total_busy > period ) {
                total_idle -= records.getFirst().d_idle;
                total_busy -= records.getFirst().d_busy;
                records.removeFirst();
        }
        return this;
    }

    public int getUsageInPercent () {
        return 100 * total_busy / (total_busy + total_idle + 1);
    }

    public String getName() {
        return name;
    }


}
