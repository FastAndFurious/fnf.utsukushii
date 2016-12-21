package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;



public class TrackModelCreatorTest {

    /**
     *            The test scenario
     *
     *  first line: Velocities as measured. second line: the track sections, third line: velocities
     *  as associated with entry and exit of the respective section
     *
     *    V1                                       V2          V3
     * ----------|----------''''''''''|''''''''''----------|---------_________|_________--------|------
     *                   V1  V1              V2  V2              V3   V3            V3   V3
     * Good:             n    n               y   y              y     y             n    n
      */
    @Test
    public void test_findAndSetEntryAndExitFor () {

        List<VelocityMessage> velos = createVelocities();

        TrackSectionSample first = createTestSampleForInterval(TrackSectionType.STRAIGHT, 1000, 1999);
        TrackSectionSample second = createTestSampleForInterval(TrackSectionType.RIGHT_CURVE, 2000, 2999);
        TrackSectionSample third = createTestSampleForInterval(TrackSectionType.STRAIGHT, 3000, 3999);
        TrackSectionSample fourth = createTestSampleForInterval(TrackSectionType.LEFT_CURVE, 4000, 4999);
        TrackSectionSample fifth = createTestSampleForInterval(TrackSectionType.STRAIGHT, 5000, 5999);

        first.setNext(second);
        second.setPrevious(first);
        second.setNext(third);
        third.setPrevious(second);
        third.setNext((fourth));
        fourth.setPrevious(third);
        fourth.setNext(fifth);
        fifth.setPrevious(fourth);


        TrackModelCreatorActor.findAndSetEntryAndExitFor(first, velos);
        assertVelocities(first, "V1", "V1", false);

        TrackModelCreatorActor.findAndSetEntryAndExitFor(second, velos);
        assertVelocities(second, "V1", "V2", false);

        TrackModelCreatorActor.findAndSetEntryAndExitFor(third, velos);
        assertVelocities(third, "V2", "V3", true);

        TrackModelCreatorActor.findAndSetEntryAndExitFor(fourth, velos);
        assertVelocities(fourth, "V3", "V3", false);

        TrackModelCreatorActor.findAndSetEntryAndExitFor(fifth, velos);
        assertVelocities(fifth, "V3", "V3", false);

    }

    private void assertVelocities (TrackSectionSample sample, String veloEntry, String veloExit, boolean good ) {
        Assert.assertEquals(veloEntry, sample.getVelocityAtEntry().getRacetrackId());
        Assert.assertEquals(veloExit, sample.getVelocityAtExit().getRacetrackId());
        Assert.assertEquals(good, sample.velocitiesAreGood());
    }

    private List<VelocityMessage> createVelocities() {
        List<VelocityMessage> res = new ArrayList<>();
        res.add(new VelocityMessage("V1", 1200, 100, ""));
        res.add(new VelocityMessage("V2", 3100, 100, ""));
        res.add(new VelocityMessage("V3", 3800, 100, ""));
        res.forEach((v)->v.offSetTime(0));
        return res;
    }

    private TrackSectionSample createTestSampleForInterval(TrackSectionType type, long from, long to) {
        TrackSectionSample sample = new TrackSectionSample(type);
        sample.add(new SensorEvent("", new int[0], new int[0], new int[0], from));
        sample.add(new SensorEvent("", new int[0], new int[0], new int[0], to));
        sample.getSensorEvents().forEach((e)->e.offSetTime(0));
        return sample;
    }
}
