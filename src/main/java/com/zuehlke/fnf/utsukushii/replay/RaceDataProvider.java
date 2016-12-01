package com.zuehlke.fnf.utsukushii.replay;

import java.util.List;

public interface RaceDataProvider {

    List<RaceDataFileInfo> list();
    RaceData read (String fileName ) throws Exception;
}
