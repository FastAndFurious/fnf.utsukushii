package com.zuehlke.fnf.utsukushii.replay;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
class StepForwardCommand {

    public enum Type {
        Steps,
        Until
    }

    Long runUntil;
    Integer numSteps;
    Type type;
}
