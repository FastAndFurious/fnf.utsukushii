package com.zuehlke.fnf.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Special smoothing filter.
 * given with a sequence of (x,y) pairs, method current() returns the
 * linear regression function evaluated at the latest value of x
 * WARNING: NOT THREAD-SAFE!
 */
public class LinRegFilter {

    private final int size;
    private double a;
    private double b;

    public LinRegFilter(int size) {
        this.size = size;
    }

    class Rep {
        Rep(double x, double y, double xy, double x2) {
            this.x = x;
            this.y = y;
            this.xy = xy;
            this.x2 = x2;
        }

        double x;
        double xy;
        double y;
        double x2;
    }

    final Rep sums = new Rep(0, 0, 0, 0);
    Deque<Rep> history = new ArrayDeque<>();

    public void put(double x, double y) {

        Rep rep = new Rep(x, y, x * y, x * x);
        history.addLast(rep);
        addToSums(rep);

        if (history.size() > size) {
            subtractFromSums(history.getFirst());
            history.removeFirst();
        }
        a = a();
        b = b();
    }

    private double a() {
        return (sums.y * sums.x2 - sums.x * sums.xy) / det();
    }

    private double b() {
        return (size * sums.xy - sums.x * sums.y) / det();
    }

    private double det() {
        return size * sums.x2 - sums.x * sums.x;
    }

    private void subtractFromSums(Rep rep1) {
        sums.x -= rep1.x;
        sums.y -= rep1.y;
        sums.xy -= rep1.xy;
        sums.x2 -= rep1.x2;
    }

    private void addToSums(Rep rep) {
        sums.x += rep.x;
        sums.y += rep.y;
        sums.xy += rep.xy;
        sums.x2 += rep.x2;
    }

    public Optional<ValueAndGradient> current() {
        if (history.size() == 0) {
            return Optional.empty();
        } else if (history.size() <= 2) {
            return Optional.of(new ValueAndGradient(history.getLast().y, b));
        }
        double x = history.getLast().x;
        return Optional.of(new ValueAndGradient(a + b * x, b));
    }


}
