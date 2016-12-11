package com.zuehlke.fnf.util;

import lombok.Data;

@Data
public class ValueAndGradient {

    private final double value;
    private final double gradient;

    public String toString () {
        return String.format("(v=%.2f, g=%.2f", value, gradient);
    }
}
