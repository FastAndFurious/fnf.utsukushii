package com.zuehlke.fnf.utsukushii.replay;

import lombok.Data;

@Data
public class ReplayCommand {

    private String fileName;
    private float frequency;
    private long runUntil;
}
