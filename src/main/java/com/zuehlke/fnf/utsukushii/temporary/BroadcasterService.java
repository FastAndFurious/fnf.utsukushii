package com.zuehlke.fnf.utsukushii.temporary;

import com.zuehlke.fnf.utsukushii.ops.LogViewerRestResource;
import com.zuehlke.fnf.utsukushii.web.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BroadcasterService {

    public BroadcasterService() {
        System.out.println("Starting broadcaster service...");
    }

    private AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Scheduled(fixedDelay = 10000)
    public void sendLogReport() {
        webSocketHandler.send(LogViewerRestResource.createDummyReport());
    }

    Integer getValue() {
        return counter.get();
    }
}
