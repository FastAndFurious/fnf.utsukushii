export class LogReport {
    entries: LogEntryCounter[];
}

export class LogEntryCounter {
    count: number;
    latestEntry: LogEntry;
}

export class LogEntry {
    source: string;
    code: string;
    severity: string;
    description: string;
    origin: string;
    timeStamp: number;
}