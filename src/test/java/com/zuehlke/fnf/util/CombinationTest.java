package com.zuehlke.fnf.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

public class CombinationTest {

    private int count = 0;

    @Test
    public void testAll () {

        new Combination(3,1).observable()
                .filter((v)->Stream.of(v).reduce(0, (l, r) -> l + r) == 1)
                .subscribe((n)->{print(n); count();}, this::print, ()->
                    System.out.println("Done.")
                );

        Assert.assertEquals(6, count);
    }

    private void count () {
        count++;
    }

    private void print ( Integer[] v) {
        for (Integer i : v) {
            System.out.print(" " + i);
        }
        System.out.println();
    }

    private void print ( Throwable t ) {
        System.out.println("Error: " + t.getMessage());
    }
}

