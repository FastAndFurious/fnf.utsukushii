package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import com.zuehlke.fnf.utsukushii.ScheduleNames;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class MonitorActor extends UntypedActor {

    private static final int UPDATE_EVERY_AFTER = 1000;
    private Cancellable schedule;

    private final Map<String, UsageStatistics> actorStats = new HashMap<>();

    private final ActorRef dispatcher;
    private final int usagePeriod;

    private MonitorActor(ActorRef dispatcher, int usagePeriod ) {
        this.dispatcher = dispatcher;
        this.usagePeriod = usagePeriod;
    }

    public static Props props (ActorRef dispatcher, int usagePeriod ) {
        return Props.create(MonitorActor.class, ()->new MonitorActor( dispatcher, usagePeriod ));
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if ( message instanceof UsageRecord) {

            String senderName = getSender().path().name();
            actorStats.putIfAbsent(senderName, new UsageStatistics(senderName, usagePeriod));
            UsageStatistics stats = actorStats.get(senderName);
            stats.with((UsageRecord) message);

        } else if ( message instanceof ScheduledTask ) {

            List<UsageReportEntry> entries = actorStats.values().stream().map((s)->{
                String name = s.getName();
                int busy = s.getTotal_busy();
                int idle = s.getTotal_idle();
                int period = s.getPeriod();
                int usage = s.getUsageInPercent();
                return new UsageReportEntry(name, busy, idle, period, usage);
            }).collect(Collectors.toList());
            entries.sort(Comparator.comparing(UsageReportEntry::getName));
            UsageReport usageReport = new UsageReport (entries);
            dispatcher.tell(usageReport, getSelf());
        } else {
            unhandled(message);
        }

    }

    @Override
    public void preStart () {
        scheduleRecurring();
    }

    @Override
    public void postStop () {
        cancelSchedule();
    }

    private void scheduleRecurring() {
        schedule = getContext().system().scheduler().schedule(
                Duration.create(UPDATE_EVERY_AFTER, TimeUnit.MILLISECONDS),
                Duration.create(UPDATE_EVERY_AFTER, TimeUnit.MILLISECONDS),
                getSelf(), new ScheduledTask(true, UPDATE_EVERY_AFTER, ScheduleNames.PUBLISH),
                getContext().system().dispatcher(), null);
    }


    private void cancelSchedule() {
        if (schedule != null && !schedule.isCancelled()) {
            schedule.cancel();
            schedule = null;
        }
    }

}
