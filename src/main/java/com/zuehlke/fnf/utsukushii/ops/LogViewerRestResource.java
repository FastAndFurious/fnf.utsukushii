package com.zuehlke.fnf.utsukushii.ops;

import com.zuehlke.fnf.actorbus.ActorBus;
import com.zuehlke.fnf.actorbus.logging.LogReport;
import com.zuehlke.fnf.actorbus.logging.QueryLogReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
@Slf4j
public class LogViewerRestResource {

    private final ActorBus actorBus;

    @Autowired
    LogViewerRestResource (ActorBus actorBus ) {
        this.actorBus = actorBus;
    }

    /**
     * There's no implementation yet to actually retrieve the logReports. So what we do is simply query a report
     * from the bus (its logger, actually). The logger will then publish a fresh report, which the WebSocketPublisher
     * is subscribed to. At a later stage we want to use the ask pattern to actually deliver the latest report here.
     * @return an empty log report
     */
    @RequestMapping(value = "/logs", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<LogReport> getLogReport () {

        actorBus.publish(new QueryLogReport());
        LogReport report = new LogReport(Collections.emptyList());
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
