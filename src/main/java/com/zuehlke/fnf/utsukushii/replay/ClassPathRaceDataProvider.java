package com.zuehlke.fnf.utsukushii.replay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuehlke.carrera.relayapi.messages.SensorEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ClassPathRaceDataProvider implements RaceDataProvider {

    private final File dataDirectory;

    public ClassPathRaceDataProvider(String directory) throws IOException {
        Resource resource = new ClassPathResource(directory);
        this.dataDirectory = resource.getFile();

        if ( !dataDirectory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory in the class path.");
        }
    }


    @Override
    public List<RaceDataFileInfo> list()  {
        File[] fileArray = dataDirectory.listFiles();
        if ( fileArray == null ) return Collections.emptyList();

        List<RaceDataFileInfo> res = new ArrayList<>();
        for ( File file : fileArray ) {
            res.add(extractInfo(file));
        }
        return res;
    }

    @Override
    public RaceData read(String fileName) throws Exception {
        File[] all = dataDirectory.listFiles((File dir, String name)-> name.equals(fileName));

        if ( all.length == 0 ) {
            throw new FileNotFoundException(fileName);
        }
        return new ObjectMapper().readValue(new FileInputStream(all[0]), RaceData.class);
    }

    public RaceDataFileInfo extractInfo ( File file ) {

        try {
            RaceData data = read(file.getName());

            long endTime = data.getSensorEvents().get(data.getSensorEvents().size() - 1).getT();
            long startTime = data.getSensorEvents().get(0).getT();

            return RaceDataFileInfo.builder()
                    .duration((int) (endTime - startTime))
                    .fileName(file.getName())
                    .numSensors(determineNumSensors(data))
                    .teamName(data.getTeamId())
                    .trackName(data.getTrackId())
                    .status(RaceDataFileInfo.Status.OK)
                    .build();
        } catch ( Exception e ) {

            return RaceDataFileInfo.builder()
                    .fileName(file.getName())
                    .status(RaceDataFileInfo.Status.CORRUPT)
                    .build();
        }
    }

    /**
     * Helper to determine the source: life or simulator. The simulator provides only gyro-z (may change though)
     * @param data the dataset to look at
     * @return the number of sensors that provide at least a single non-zero reading
     */
    private int determineNumSensors(RaceData data) {

        Set<String> res = new HashSet<>();
        for (SensorEvent event: data.getSensorEvents()) {
            for ( int axis = 0; axis < 3; axis ++ ) {
                if (event.getA()[axis] != 0) {
                    res.add("A" + axis);
                }
                if (event.getM()[axis] != 0) {
                    res.add("M" + axis);
                }
                if (event.getG()[axis] != 0) {
                    res.add("G" + axis);
                }
            }
        }
        return res.size();
    }


}
