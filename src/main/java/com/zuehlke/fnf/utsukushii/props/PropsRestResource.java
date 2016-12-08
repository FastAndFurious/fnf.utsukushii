package com.zuehlke.fnf.utsukushii.props;

import com.zuehlke.fnf.utsukushii.UtsukushiiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Slf4j
public class PropsRestResource {

    @Autowired
    private UtsukushiiProperties properties;

    @RequestMapping("/props")
    public ResponseEntity<UtsukushiiProperties> props (){
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
}
