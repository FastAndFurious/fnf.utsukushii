package com.zuehlke.fnf.utsukushii.props;

import com.zuehlke.fnf.actorbus.ActorBus;
import com.zuehlke.fnf.mongo.MongoParamStore;
import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Slf4j
public class PropsRestResource {

    @Autowired
    private MongoParamStore mongoParamStore;

    @Autowired
    private UtsukushiiProperties defaultProps;

    @Autowired
    private ActorBus bus;

    // UtsukushiiBootstrapper guarantees that there will always be a record!
    @RequestMapping("/props")
    public ResponseEntity<UtsukushiiProperties> props (){
        UtsukushiiProperties properties = mongoParamStore.retrieve(defaultProps.getId());
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @RequestMapping(value = "/props", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> saveProps (@RequestBody UtsukushiiProperties props) {
        mongoParamStore.store(props);
        bus.publish(props);
        return ResponseEntity.ok().build();
    }
}
