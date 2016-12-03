package com.zuehlke.fnf.utsukushii.replay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuehlke.fnf.actorbus.ActorBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
@Slf4j
public class ReplayRestResource {

    private final static String DATA_DIR = "/data/";

    private final ActorBus actorBus;
    private final RaceDataProvider provider;

    @Autowired
    public ReplayRestResource(ActorBus actorBus) throws Exception {

        this.actorBus = actorBus;
        provider = new ClassPathRaceDataProvider(DATA_DIR);
    }

    @RequestMapping(value = "/replay", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> postReplayCommand(@RequestBody ReplayCommand command) {

        log.info("Received command to replay file " + command.getFileName());

        Resource resource = new ClassPathResource( DATA_DIR + command.getFileName());

        try {
            RaceData raceData = new ObjectMapper().readValue(resource.getInputStream(), RaceData.class);
            actorBus.publish(raceData);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch ( IOException ioe ) {
            String message = "Failed to read " + command.getFileName() + ": " + ioe.getMessage();
            log.warn(message);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value="/replay", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getFileInfos () {

        log.debug("Received Request to provide all file info records.");

        return new ResponseEntity<>(provider.list(), HttpStatus.OK);
    }

}
