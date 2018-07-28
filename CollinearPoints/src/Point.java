import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/**
 * To get started, use the data type Point.java, which implements the constructor and the draw(), drawTo(), and toString() methods.
 * Create an immutable data type Point that represents a point in the plane by implementing the following API:
 */
public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    /**
     * constructs the point (x, y)
     * x and y is between 0 and 32,767
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * compare two points by y-coordinates, breaking ties by x-coordinates
     * The compareTo() method should compare points by their y-coordinates, breaking ties by their x-coordinates.
     * Formally, the invoking point (x0, y0) is less than the argument point (x1, y1)
     * if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     */
    @Override
    public int compareTo(Point that) {
        if (this.y > that.y) {
            return 1;
        } else if (this.y == that.y) {
            if (this.x > that.x) {
                return 1;
            } else if (this.x == that.x) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * the slope between this point and that point
     * The slopeTo() method should return the slope between the invoking point (x0, y0) and the argument point (x1, y1),
     * which is given by the formula (y1 − y0) / (x1 − x0). Treat the slope of a horizontal line segment as positive zero;
     * treat the slope of a vertical line segment as positive infinity; treat the slope of a degenerate line segment
     * (between a point and itself) as negative infinity.
     */
    public double slopeTo(Point that) {
        final double SLOPE_HORIZONTAL = 0.0d;
        final double SLOPE_VERTICAL = Double.POSITIVE_INFINITY;
        final double SLOPE_DEGENERATE =  Double.NEGATIVE_INFINITY;
        boolean isDxZero = (that.x == this.x);
        boolean isDyZero = (that.y == this.y);

        if (isDxZero && isDyZero) {
            return SLOPE_DEGENERATE;
        } else if (isDyZero) {
            return SLOPE_HORIZONTAL;
        } else if (isDxZero) {
            return SLOPE_VERTICAL;
        } else {
            double dy = that.y - this.y;
            double dx = that.x - this.x;
            return dy / dx;
        }
    }

    private class PointComparator implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            double slope1 = Point.this.slopeTo(p1);
            double slope2 = Point.this.slopeTo(p2);
            if(slope1 > slope2) return 1;
            else if(slope1 < slope2) return -1;
            else return 0;
        }
    };


    /**
     * The slopeOrder() method should return a comparator that compares its two argument points by the slopes they make
     * with the invoking point (x0, y0). Formally, the point (x1, y1) is less than the point (x2, y2) if and only
     * if the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0).
     * Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.
     * compare two points by slopes they make with this point
     */
    public Comparator<Point> slopeOrder() {
        return new PointComparator();
    }
}