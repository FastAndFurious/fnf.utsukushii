package com.zuehlke.fnf.utsukushii.model;

import com.zuehlke.fnf.util.DisplayableProperties;
import lombok.Data;

@Data
public class TrackModelActorProperties extends DisplayableProperties {

    private int awaitSectionsBeforeRecognition;
    private int minStraightLength;
    private int minNumberOfCurves;

    public TrackModelActorProperties () {
        super("Track Model");
    }
}
