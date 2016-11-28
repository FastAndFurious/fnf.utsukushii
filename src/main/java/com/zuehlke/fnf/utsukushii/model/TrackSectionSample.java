package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TrackSectionSample {

    @Getter
    private final TrackSectionType type;

    @Getter
    private final List<SensorEvent> sensorEvents = new ArrayList<>();

    public TrackSectionSample(TrackSectionType type ) {
        this.type = type;
    }

    public void add ( SensorEvent event ) {
        sensorEvents.add(event);
    }

    public void finalizeSection () {
        sensorEvents.sort((l,r)->(int)(r.getTimeStamp() + r.getTimeStamp() - l.getTimeStamp() - l.getTimeStamp()));
    }

    public int lengthInMs () {
        int size = sensorEvents.size();
        if ( size <= 1 ) return 0;
        if ( sensorEvents.get(0).getTimeStamp() == 0 ) {
            return sensorEvents.get(size - 1).getT() - sensorEvents.get(0).getT();
        } else {
            return (int)(sensorEvents.get(size - 1).getTimeStamp() - sensorEvents.get(0).getTimeStamp());
        }
    }

}
