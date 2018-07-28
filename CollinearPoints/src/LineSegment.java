import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Smolin on 01.12.2016.
 * To represent line segments in the plane
 */
public class LineSegment {
    private final Point p;
    private final Point q;

    // constructs the line segment between points p and q
    public LineSegment(Point p, Point q) {
        this.p = p;
        this.q = q;
    }

    // draws this line segment
    public void draw() {
        p.drawTo(q);
    }

    // string representation
    public String toString() {
        return p + "->" + q;
    }
}
