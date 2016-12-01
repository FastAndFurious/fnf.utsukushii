package com.zuehlke.fnf.utsukushii.replay;

import org.junit.Assert;
import org.junit.Test;

public class ClassPathResourceTest {

    @Test
    public void testListResources () throws Exception {

        ClassPathRaceDataProvider provider = new ClassPathRaceDataProvider("/data");
        RaceDataFileInfo info = provider.list().get(0);
        Assert.assertEquals(9, info.getNumSensors());

    }
}
