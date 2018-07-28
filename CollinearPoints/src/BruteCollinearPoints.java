import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Smolin on 01.12.2016.
 * TIME ~N^4
 * MEMORY ~N
 */
public class BruteCollinearPoints {
    /**
     * finds all line segments containing 4 points
     * Throw a java.lang.NullPointerException either the argument to the constructor is null or if any point
     * in the array is null. Throw a java.lang.IllegalArgumentException if the argument to the constructor
     * contains a repeated point.
     */
    private ArrayList<LineSegment> lines = new ArrayList<>();

    public BruteCollinearPoints(Point[] pointsIm) {
        if (pointsIm == null) {
            throw new NullPointerException();
        }
        Point[] points = Arrays.copyOf(pointsIm, pointsIm.length);
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException();
            }
        }

        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].equals(points[i - 1])) {
                throw new IllegalArgumentException();
            }
        }

        if (points.length >= 4) {
            for (int i1 = 0; i1 < points.length; i1++) {
                for (int i2 = i1 + 1; i2 < points.length; i2++) {
                    for (int i3 = i2 + 1; i3 < points.length; i3++) {
                        for (int i4 = i3 + 1; i4 < points.length; i4++) {
                            if (isInOneLine(points[i1], points[i2], points[i3], points[i4])) {
                                lines.add(new LineSegment(points[i1], points[i4]));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isInOneLine(Point point, Point point1, Point point2, Point point3) {
        double slope1 = point.slopeTo(point1);
        double slope2 = point1.slopeTo(point2);
        double slope3 = point2.slopeTo(point3);
        return slope1 == slope2 && slope2 == slope3;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    /**
     * the line segments
     * The method segments() should include each line segment containing 4 points exactly once. If 4 points appear on a
     * line segment in the order p→q→r→s, then you should include either the line segment p→s or s→p (but not both)
     * and you should not include subsegments such as p→r or q→r. For simplicity, we will not supply any input to
     * BruteCollinearPoints that has 5 or more collinear points.
     */
    public LineSegment[] segments() {
        return lines.toArray(new LineSegment[lines.size()]);
    }

    /**
     * This client program takes the name of an input file as a command-line argument; read the input file (in the format specified below);
     * prints to standard output the line segments that your program discovers, one per line; and draws to standard draw the line segments.
     * java BruteCollinearPoints input8.txt
     * (10000, 0) -> (0, 10000)
     * (3000, 4000) -> (20000, 21000)
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
