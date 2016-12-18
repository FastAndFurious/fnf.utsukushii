package com.zuehlke.fnf.util;

import rx.Observable;
import rx.subjects.ReplaySubject;

import java.util.Arrays;

/**
 * A class representing the combinatorial sequence of n values, each in the range of [-k, k]
 * E.g. Combination(3, 1) represents all combinations of 3 numbers from -1 to 1
 */
public class Combination {

    private final int n;
    private final int k;

    public Combination ( int n, int k ) {
        this.n = n;
        this.k = k;
    }

    public Observable<Integer[]> observable() {
        ReplaySubject<Integer[]> subject = ReplaySubject.create();
        produce ( subject );
        return subject;
    }

    private void produce( ReplaySubject<Integer[]> subject ) {
        produce ( subject, new Integer[n], 0);
        subject.onCompleted();
    }

    private void produce ( ReplaySubject<Integer[]> subject, Integer[] c, int m) {
        if ( m == n ) {
            subject.onNext( Arrays.copyOf(c, n) );
        } else {
            for ( int i = -k; i <= k; i++ ) {
                c[m] = i;
                produce ( subject, c, m+1);
            }
        }
    }
}
