export class UsageReport {

    entries: UsageReportEntry[];
}

export class UsageReportEntry {
    name: string;
    busyTime: number;
    idleTime: number;
    watchPeriod: number;
    usage: number;
}
