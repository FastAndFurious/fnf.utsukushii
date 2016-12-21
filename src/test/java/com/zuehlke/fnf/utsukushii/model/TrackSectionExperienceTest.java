package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import org.junit.Assert;
import org.junit.Test;

public class TrackSectionExperienceTest {

    // max difference allowed due to noise
    private static final int THRESHOLD = 20;

    private TrackSectionExperience exp;
    private SensorHelper helper = new SensorHelper("track");


    @Test
    public void testLengthFromCompleteSample () {

        TrackSectionSample sample = given_a_sample(TrackSectionType.RIGHT_CURVE, 200.0, 100.0, 30, true);

        when_creating_a_new_experience(sample);

        the_section_length_should_be_about(225);
    }

    @Test
    public void testLengthFromSampleWithoutVelocityAtExit () {

        TrackSectionSample sample = given_a_sample(TrackSectionType.RIGHT_CURVE, 200.0, null, 30, false);

        when_creating_a_new_experience(sample );

        the_section_length_should_be_about(285);
    }

    @Test
    public void testLengthFromMultipleGoodSamples () {

        TrackSectionSample sample = given_a_sample(TrackSectionType.RIGHT_CURVE, 200.0, 150.0, 30, true);
        TrackSectionSample anotherSample = given_a_sample(TrackSectionType.RIGHT_CURVE, 220.0, 180.0, 25, true);

        when_creating_a_new_experience(sample);
        when_adding_another_experience ( anotherSample );

        the_section_length_should_be_about(240);
    }


    private TrackSectionSample given_a_sample(TrackSectionType type, Double veloAtEntry, Double veloAtExit, int numValues, boolean good) {

        TrackSectionSample sample = new TrackSectionSample(type);
        if ( veloAtEntry != null ) {
            sample.setVelocityAtEntry(new VelocityMessage("", 10L, veloAtEntry, ""));
        }
        if ( veloAtExit != null ) {
            sample.setVelocityAtExit(new VelocityMessage("", 10L, veloAtExit, ""));
        }
        sample.getSensorEvents().addAll(helper.randomRightCurve(numValues, 4000, 10));

        sample.setVelocitiesAreGood(good);

        return sample;
    }


    private void when_creating_a_new_experience( TrackSectionSample sample ) {
        exp = new TrackSectionExperience( sample );
    }
    private void when_adding_another_experience( TrackSectionSample sample) {
        exp.merge(sample);
    }


    private void the_section_length_should_be_about(Integer expected) {
        exp.getLength().ifPresent((actual)-> Assert.assertTrue("expected: " + expected + ", actual " + actual,
                Math.abs(expected-actual) < THRESHOLD));
    }


}
