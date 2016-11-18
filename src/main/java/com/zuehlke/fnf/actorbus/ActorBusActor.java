package com.zuehlke.fnf.actorbus;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import com.zuehlke.fnf.actorbus.logging.AbstractLoggingActor;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class ActorBusActor extends AbstractLoggingActor {

    private ActorRef dispatcher;
    private ActorRef monitor;
    private String name;
    private long t_out;
    private Map<String, Cancellable> schedules = new HashMap<>();

    public ActorBusActor ( String name ) {
        this.name = name;
    }

    protected void publish ( Object message ) {

        if ( dispatcher != null ) {
            dispatcher.tell( message, getSender());
        }
    }

    @Override
    final public void onReceive(Object message) throws Exception {

        if ( message instanceof DispatcherAnnouncement ) {
            dispatcher = ((DispatcherAnnouncement) message).getDispatcher();
        } else if ( message instanceof MonitorAnnouncement ) {
            monitor = ((MonitorAnnouncement) message).getMonitor();
        } else if ( message instanceof LoggingReceiverAnnouncement  ) {
            logActor = ((LoggingReceiverAnnouncement) message).getLogger();


        } else {
            long now  = System.currentTimeMillis();
            int d_out = (int)(now - t_out);
            long t_in = now;
            onReceive2( message );
            now = System.currentTimeMillis();
            t_out = now;
            int d_in = (int) (now - t_in);

            if ( monitor == null ) return;
            if ( !(message instanceof UsageStatistics)) {
                monitor.tell(new UsageRecord(name, d_in, d_out), getSelf());
            }
        }
    }

    protected void scheduleRecurring(int millies, String taskName ) {
        cancelSchedule(taskName);
        Cancellable schedule = getContext().system().scheduler().schedule(
                Duration.create(millies, TimeUnit.MILLISECONDS),
                Duration.create(millies, TimeUnit.MILLISECONDS),
                getSelf(), new ScheduledTask(true, millies, taskName),
                getContext().system().dispatcher(), null);
        schedules.put ( taskName, schedule );
    }

    protected void cancelSchedule( String taskName ) {
        Cancellable schedule = schedules.get(taskName);
        if (schedule != null && !schedule.isCancelled()) {
            schedule.cancel();
        }
        schedules.remove(taskName);
    }


    abstract protected void onReceive2(Object message) throws Exception;
}
