import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by Smolin on 01.12.2016.
 * The order of growth of the running time of your program should be N^2*logN in the worst case and it should use
 * space proportional to n plus the number of line segments returned.
 * FastCollinearPoints should work properly even if the input has 5 or more collinear points.
 * A faster, sorting-based solution. Remarkably, it is possible to solve the problem much faster than the brute-force
 * solution described above. Given a point p, the following method determines whether p participates in a set of 4 or more collinear points.
 * <p>
 * Think of p as the origin.
 * For each other point q, determine the slope it makes with p.
 * Sort the points according to the slopes they makes with p.
 * Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.
 * Applying this method for each of the n points in turn yields an efficient algorithm to the problem. The algorithm solves the problem because points that have equal slopes with respect to p are collinear, and sorting brings such points together. The algorithm is fast because the bottleneck operation is sorting.
 */
public class FastCollinearPoints {

    private int maxSlopeNum = 2;
    private ArrayList<LineSegment> lines = new ArrayList<>();
    private ArrayList<ArrayList<Double>> pointHasSlope;

    /**
     * finds all line segments containing 4 or more points
     * Throw a java.lang.NullPointerException either the argument to the constructor is null or if any point
     * in the array is null. Throw a java.lang.IllegalArgumentException if the argument to the constructor
     * contains a repeated point.
     */
    public FastCollinearPoints(Point[] pointsIm) {
        if (pointsIm == null) {
            throw new NullPointerException();
        }
        for (Point p : pointsIm) {
            if (p == null) {
                throw new NullPointerException();
            }
        }

        Point[] points = Arrays.copyOf(pointsIm, pointsIm.length);
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].equals(points[i - 1])) {
                throw new IllegalArgumentException();
            }
        }

        pointHasSlope = new ArrayList<ArrayList<Double>>(points.length);
        for (int i = 0; i < points.length; i++) {
            pointHasSlope.add(new ArrayList<>());
        }
        ArrayList<Point> pointsList = new ArrayList<>(Arrays.asList(points));
        if (points.length >= 4) {
            for (Point pi : points) {
                Collections.sort(pointsList);
                Collections.sort(pointsList, pi.slopeOrder());
                for (int ie = 1, sum = 2; ie < pointsList.size() - 1; ie++) {
                    if (pi.slopeTo(pointsList.get(ie)) == pi.slopeTo(pointsList.get(ie + 1))) {
                        sum++;

                        if (ie == pointsList.size() - 2) {
                            if (sum >= 4 && checkSlope(points, pointsList, pi, ie + 1)) {
                                lines.add(new LineSegment(pi, pointsList.get(ie + 1)));
                            }
                            sum = 2;
                        }
                    } else {
                        if (sum >= 4 && checkSlope(points, pointsList, pi, ie)) {
                            lines.add(new LineSegment(pi, pointsList.get(ie)));
                        }
                        sum = 2;
                    }
                }
                assert pointsList.get(0).equals(pi);
                //TODO: not optimal. use array instead
                pointsList.remove(0);
            }
        }
    }

    private boolean checkSlope(Point[] points, ArrayList<Point> pointsList, Point p, int qInd) {
        Point q = pointsList.get(qInd);
        int indexOfQ = Arrays.binarySearch(points, q);
        ArrayList<Double> slopes = pointHasSlope.get(indexOfQ);
        double slope = p.slopeTo(q);
        if (slopes.contains(slope)) {
            return false;
        } else {
            slopes.add(slope);
            return true;
        }
    }

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
        StdDraw.setXscale(-1000, 32768);
        StdDraw.setYscale(-1000, 32768);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.02);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        //print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        //BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

//        StdDraw.setPenRadius();
//        StdDraw.setPenColor(StdDraw.BLACK);
//        Arrays.sort(points, points[0].slopeOrder());
//        for (int i = 0; i < n; i++) {
//            points[0].drawTo(points[i]);
//            StdDraw.show();
//            StdDraw.pause(1000);
//        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    /**
     * the line segments
     * The method segments() should include each maximal line segment containing 4 (or more) points exactly once.
     * For example, if 5 points appear on a line segment in the order p→q→r→s→t, then do not include the subsegments p→s or q→t.
     */
    public LineSegment[] segments() {
        return lines.toArray(new LineSegment[lines.size()]);
    }
}