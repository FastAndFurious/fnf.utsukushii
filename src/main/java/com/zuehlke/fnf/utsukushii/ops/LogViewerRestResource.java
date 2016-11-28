package com.zuehlke.fnf.utsukushii.ops;

import com.zuehlke.fnf.actorbus.logging.*;
import com.zuehlke.fnf.utsukushii.replay.ReplayActor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class LogViewerRestResource {


    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<LogReport> getLogReport () {

        return new ResponseEntity<>(createDummyReport(), HttpStatus.OK);
    }

    public static LogReport createDummyReport () {
        List<LogEntryCounter> list = new ArrayList<>();
        LogEntry entry = new LogEntry(ReplayActor.class, MessageCode.USAGE, Severity.DEBUG, "nothing serious", "external");
        LogEntryCounter counter = new LogEntryCounter(entry);
        counter.add(entry);
        LogEntry entry2 = new LogEntry(ReplayActor.class, MessageCode.USAGE, Severity.ERROR, "Replay failed.", "dispatcher");
        counter.add (entry2);
        list.add(counter);
        return new LogReport(list);
    }
}
