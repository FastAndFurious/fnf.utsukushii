package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for testing track section logic
 */
class SensorHelper {

    private final String trackId;

    SensorHelper(String trackId ) {
        this.trackId = trackId;
    }

    List<SensorEvent> randomRightCurve(int numEvents, int maxVAlue, int startAt ) {
        long t = startAt;
        List<SensorEvent> res = new ArrayList<>();
        for ( int i = 0; i < numEvents; i++ ) {
            res.add(new SensorEvent(trackId, new int[3],
                    new int[] { 0, 0, noisySineCurve(i, numEvents, maxVAlue)},
                    new int[3], t));
            t += 45 + Math.random() * 10;
        }
        for ( SensorEvent e: res ) {
            e.offSetTime(startAt);
        }

        return res;
    }

    private int noisySineCurve(int i, int numEvents, int maxValue) {
        return (int) (Math.sin(Math.PI * i / numEvents ) * ( 1 + Math.random() * 0.1 ) * maxValue);
    }
}
