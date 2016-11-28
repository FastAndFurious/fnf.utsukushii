package com.zuehlke.fnf.utsukushii.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class TrackSectionStartDetectorTest {

    @Test
    public void testAll () {

        // smoothing with a length of 4, threshold 100 and -100, and 3 subsequent values to trigger a section
        TrackSectionStartDetector detector = new TrackSectionStartDetector(4, 1000, -1000, 3, 6.0, 3.0 );

        int i = 1;

        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, 300));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, -500));
        Assert.assertEquals(TrackSectionType.STRAIGHT, detector.putAndDetect(i+=50, 200).get());

        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, 1110));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, 2000));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, 2200));
        Assert.assertEquals(TrackSectionType.RIGHT_CURVE, detector.putAndDetect(i+50, 1600).get());

        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, 900));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, 600));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, -600));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, -1300));
        Assert.assertEquals(Optional.empty(), detector.putAndDetect(i+=50, -1400));
        Assert.assertEquals(TrackSectionType.LEFT_CURVE, detector.putAndDetect(i++, -1100).get());

    }


    int [][]data = new int[][]{
        new int[] { 49247,                                       -3540},
        new int[] { 49273,                                -4763},
        new int[] { 49293,                         -5885},
        new int[] { 49309,                         -5908},
        new int[] { 49338,                                   -4270},
        new int[] { 49358,                                    -4001},
        new int[] { 49387,                                                 -1958},
        new int[] { 49400,                                                       -968},
        new int[] { 49413,                                                               427},
        new int[] { 49429,                                                                   1081},
        new int[] { 49452,                                                            -7},
        new int[] { 49481,                                                                 678},
        new int[] { 49494,                                                            -122},
        new int[] { 49509,                                                           -247},
        new int[] { 49525,                                                               340},
        new int[] { 49547,                                                               338},
        new int[] { 49570,                                                           -260},
        new int[] { 49593,                                                          -403},
        new int[] { 49620,                                                                535},
        new int[] { 49624,                                                              216},
        new int[] { 49646,                                                             53},
        new int[] { 49665,                                                            -33}
};

    @Test
    public void testLateStraightRecognition() {

        // smoothing with a length of 4, threshold 100 and -100, and 3 subsequent values to trigger a section
        TrackSectionStartDetector detector = new TrackSectionStartDetector(3, 1000, -1000, 3, 30.0, 2.0 );
        int i = 0;
        for ( int[] d: data ) {
            detector.putAndDetect(d[0], d[1]).ifPresent(System.out::println);
        }

    }
}


