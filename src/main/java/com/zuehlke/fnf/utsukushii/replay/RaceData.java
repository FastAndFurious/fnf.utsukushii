package com.zuehlke.fnf.utsukushii.replay;

import com.zuehlke.carrera.relayapi.messages.PowerControl;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import com.zuehlke.carrera.relayapi.messages.VelocityMessage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RaceData {

    private String id;
    private long startTime;
    private String teamId;
    private String trackId;
    private String raceType;

    private List<SensorEvent> sensorEvents = new ArrayList<>();
    private List<PowerControl> powerControls = new ArrayList<>();
    private List<VelocityMessage> velocityMessages = new ArrayList<>();

}