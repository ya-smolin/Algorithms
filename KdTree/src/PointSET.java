import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private Set<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p: points){
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> pointsRange = new ArrayList<>();
        for (Point2D p: points){
            if(rect.contains(p)){
                pointsRange.add(p);
            }
        }
        return pointsRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D curPoint) {
        if(curPoint == null) throw new IllegalArgumentException();
        Point2D pointsNearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D p: points){
            if(curPoint.distanceSquaredTo(p) < nearestDistance) {
                nearestDistance = curPoint.distanceSquaredTo(p);
                pointsNearest = p;
            }
        }
        return pointsNearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET PS = new PointSET();
        StdRandom.uniform();
        for (int i = 0; i < 100; i++) {
            PS.insert(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
        }
        PS.draw();
        RectHV rect = new RectHV(0.5, 0.5,0.7, 0.7);
        rect.draw();

        StdOut.print(PS.range(rect));
        Point2D pointFrom = new Point2D(0.5, 0.5);
        Point2D nearestPoint = PS.nearest(pointFrom);
        pointFrom.drawTo(nearestPoint);

    }
}