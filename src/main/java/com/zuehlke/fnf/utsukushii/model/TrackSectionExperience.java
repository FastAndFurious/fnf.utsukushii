package com.zuehlke.fnf.utsukushii.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class TrackSectionExperience {

    @Getter
    private final List<TrackSectionSample> samples = new ArrayList<>();

    private Integer length;

    TrackSectionExperience(TrackSectionSample sample) {
        merge(sample);
    }

    public TrackSectionExperience merge(TrackSectionSample sample ) {
        this.samples.add(sample);
        calculateLength();
        return this;
    }

    /**
     * The alledged length of the track section. Calculated from the velocities at entry and exit
     * Gracefully degrading strategy if velocities are not well-associated or not available.
     */
    private void calculateLength() {
        List<Integer> goodResults = new ArrayList<>(); // those with well-associated velocities
        List<Integer> secondaryResults = new ArrayList<>(); // all other ones
        if ( samples.size() == 0 ) length = null;
        for ( TrackSectionSample sample : samples ) {
            if ( sample.velocitiesAreGood()) {
                goodResults.add(sample.getLengthInCm().get());
            } else {
                sample.getLengthInCm().ifPresent((l)->secondaryResults.add(l));
            }
        }
        if ( goodResults.size() > 0 ) {
            length = goodResults.stream().reduce(0, (l,r)->(l + r/goodResults.size()));
        } else if ( secondaryResults.size() > 0 ) {
            length = secondaryResults.stream().reduce(0, (l,r)->(l + r/secondaryResults.size()));
        }
    }

    /**
     * @return an optional length, if available
     */
    public Optional<Integer> getLength() {
        return Optional.ofNullable(length);
    }
}
