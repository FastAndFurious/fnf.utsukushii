package com.zuehlke.fnf.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class LinRegFilterTest {

    @Test
    public void testAll () {

        double delta = 0.1;

        LinRegFilter filter = new LinRegFilter(4);

        Assert.assertEquals(Optional.empty(), filter.current());

        filter.put (1, 2);
        Assert.assertEquals(2.0, filter.current().get().getValue(), delta);

        filter.put (2, 4);
        Assert.assertEquals(4.0, filter.current().get().getValue(), delta);

        filter.put (3, 6);
        Assert.assertEquals(6.0, filter.current().get().getValue(), delta);

        filter.put (4, 8);
        Assert.assertEquals(8.0, filter.current().get().getValue(), delta);

        filter.put (4, 2);
        Assert.assertEquals(5.27, filter.current().get().getValue(), delta);

        filter.put (5, 4);
        filter.put (6, 6);
        filter.put (7, 8);
        Assert.assertEquals(8.0, filter.current().get().getValue(), delta);
    }
}
