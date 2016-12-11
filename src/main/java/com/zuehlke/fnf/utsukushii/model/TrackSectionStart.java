package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.fnf.util.ValueAndGradient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class TrackSectionStart {

    private TrackSectionType type;
    private ValueAndGradient trigger;
    private long timestamp;

    public String toString () {
        return type.toString() + ", triggered by " + trigger.toString() + " at t= " + timestamp;
    }
}
