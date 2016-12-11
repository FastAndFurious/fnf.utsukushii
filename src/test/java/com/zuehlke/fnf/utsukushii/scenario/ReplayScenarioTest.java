package com.zuehlke.fnf.utsukushii.scenario;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.zuehlke.carrera.relayapi.messages.RaceStartMessage;
import com.zuehlke.carrera.relayapi.messages.RaceStopMessage;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.fnf.actorbus.ActorBus;
import com.zuehlke.fnf.actorbus.UsageReport;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import com.zuehlke.fnf.utsukushii.replay.RaceData;
import com.zuehlke.fnf.utsukushii.replay.RaceDataFileInfo;
import com.zuehlke.fnf.utsukushii.replay.ReplayCommand;
import com.zuehlke.fnf.utsukushii.replay.ReplayRestResource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This is an exemplary scenario test.
 * A spy actor on the bus can be asked for the messages that it has intercepted.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ScenarioTestContext.class)
@SuppressWarnings({"unchecked"})
@Slf4j
public class ReplayScenarioTest {

    private static final int TEST_RACE_DURATION = 2000;

    @Autowired
    private ReplayRestResource replayRestResource;

    @Autowired
    private ActorBus bus;

    private ActorRef spy;

    @Before
    public void setUp () {
        spy = bus.register("Spy", ActorBusSpyActor.props(), ActorBusSpyActor.subscriptions);
    }

    @Test
    public void testCompleteReplay () throws Exception {

        ReplayCommand command = createSomeReplayCommand();

        replayRestResource.start(command);
        log.info("Letting the race proceed for {} seconds", TEST_RACE_DURATION / 1000.0);
        Thread.sleep(TEST_RACE_DURATION);
        replayRestResource.stop();
        Thread.sleep(100);

        Assert.assertEquals(1, askFor(RaceData.class).size());
        Assert.assertEquals(1, askFor(RaceStartMessage.class).size());
        Assert.assertEquals(1, askFor(RaceStopMessage.class).size());
        Assert.assertTrue(askFor(UsageReport.class).size() > 1);
        Assert.assertTrue(askFor(SensorEvent.class).size() > 1 );
        Assert.assertTrue(askFor(LogReport.class).size() > 1 );

    }


    private ReplayCommand createSomeReplayCommand() {
        Assert.assertNotNull(replayRestResource);
        List<RaceDataFileInfo> files = (List<RaceDataFileInfo>) replayRestResource.getFileInfos().getBody();
        Assert.assertNotNull(files);
        RaceDataFileInfo myFile = files.stream()
                .filter((f)->f.getTeamName().equals("Bernie's Burners"))
                .findAny().orElseThrow(()->new AssertionError("Was expecting a race date file info record here."));
        ReplayCommand command = new ReplayCommand();
        command.setFileName(myFile.getFileName());
        command.setFrequency(200);
        return command;
    }


    private <T> List<T> askFor(Class<T> clazz) throws Exception {
        Future<Object> answer = Patterns.ask(spy, clazz, 1000);
        Object res = Await.result(answer, Duration.apply(1000, TimeUnit.MILLISECONDS));
        return (List<T>) res;
    }
}
