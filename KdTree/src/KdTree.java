import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Stack;

public class KdTree {

    private static enum NodeType {VERTICAL, HORIZONTAL}

    ;
    private Node headNode;
    private int size;

    private Node getHeadNode() {
        return headNode;
    }

    private void setHeadNode(Node headNode) {
        this.headNode = headNode;
    }

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return headNode == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }


    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!isEmpty()) {
            Stack<Node> needToVisitNodes = new Stack<>();
            needToVisitNodes.push(headNode);

            while (!needToVisitNodes.isEmpty()) {
                Node curNode = needToVisitNodes.pop();
                Point2D curPoint = curNode.getPoint();
                Node nextNode;
                if (curNode.isVertical()) {
                    nextNode = (p.x() > curPoint.x()) ? curNode.getRight() : curNode.getLeft();
                } else {
                    nextNode = (p.y() > curPoint.y()) ? curNode.getRight() : curNode.getLeft();
                }
                if (nextNode == null) {
                    Node insertedNode = new Node(p, curNode.getChildNodeType(), curNode);
                    curNode.setChildNode(insertedNode);
                    size++;
                } else {
                    if (curPoint.equals(p)) return;
                    else needToVisitNodes.push(nextNode);
                }
            }
        } else {
            headNode = new Node(p, NodeType.VERTICAL, null);
            size++;
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!isEmpty()) {
            Stack<Node> needToVisitNodes = new Stack<>();
            needToVisitNodes.push(headNode);
            while (!needToVisitNodes.isEmpty()) {
                Node curNode = needToVisitNodes.pop();
                Point2D curPoint = curNode.getPoint();
                if (curPoint.equals(p)) return true;
                Node nextNode;
                if (curNode.isVertical()) {
                    nextNode = (p.x() > curPoint.x()) ? curNode.getRight() : curNode.getLeft();
                } else {
                    nextNode = (p.y() > curPoint.y()) ? curNode.getRight() : curNode.getLeft();
                }
                if (nextNode != null) needToVisitNodes.push(nextNode);
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        if (!isEmpty()) {
            headNode.getRect().draw();
            drawNodeRecursively(headNode);
        }
    }

    private void drawNodeRecursively(Node node) {
        if (node != null) {
            StdDraw.setPenRadius(0.01);
            node.getPoint().draw();
            StdDraw.setPenRadius(0.002);
            node.getStartPoint().drawTo(node.getEndPoint());
            drawNodeRecursively(node.getLeft());
            drawNodeRecursively(node.getRight());
        }
    }

    //TODO: all points that are inside the rectangle (or on the boundary) with KdTrees
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> pointsRange = new ArrayList<>();

        if (headNode != null) {
            Stack<Node> needToVisitNodes = new Stack<>();
            needToVisitNodes.push(headNode);
            while (!needToVisitNodes.isEmpty()) {
                Node curNode = needToVisitNodes.pop();
                if (rect.contains(curNode.getPoint())) {
                    pointsRange.add(curNode.getPoint());
                }
                if (curNode.isVertical()) {
                    if (rect.xmin() < curNode.x() && curNode.getLeft() != null)
                        needToVisitNodes.push(curNode.getLeft());
                    if (curNode.x() <= rect.xmax() && curNode.getRight() != null)
                        needToVisitNodes.push(curNode.getRight());
                } else {
                    if (rect.ymin() < curNode.y() && curNode.getLeft() != null)
                        needToVisitNodes.push(curNode.getLeft());
                    if (curNode.y() <= rect.ymax() && curNode.getRight() != null)
                        needToVisitNodes.push(curNode.getRight());
                }
            }
        }
        return pointsRange;
    }

    //TODO: rewrite a nearest neighbor in the set to point p; null if the set is empty with KdTrees
    public Point2D nearest(Point2D curPoint) {
        if (curPoint == null) throw new IllegalArgumentException();
        Point2D pointsNearest = null;


        if (headNode != null) {
            Stack<Node> needToVisitNodes = new Stack<>();
            needToVisitNodes.push(headNode);
            pointsNearest = headNode.getPoint();
            double distanceSqMin = pointsNearest.distanceSquaredTo(curPoint);

            while (!needToVisitNodes.isEmpty()) {
                Node curNode = needToVisitNodes.pop();
                double curDistance = curNode.getPoint().distanceSquaredTo(curPoint);
                if (curDistance < distanceSqMin) {
                    pointsNearest = curNode.getPoint();
                    distanceSqMin = curDistance;
                }
                if (curNode.getLeft() != null && curNode.getLeft().getRect().distanceSquaredTo(curPoint) < distanceSqMin)
                    needToVisitNodes.push(curNode.getLeft());
                if (curNode.getRight() != null && curNode.getRight().getRect().distanceSquaredTo(curPoint) < distanceSqMin)
                    needToVisitNodes.push(curNode.getRight());

            }
        }
        return pointsNearest;
    }

    /**
     * even is vertical nodes, left is to the left, right is to the right
     * odd is horizontal nodes, left is above, right is below
     */
    private class Node {
        static final float X_START = 0;
        static final float Y_START = 0;
        static final float X_END = 1;
        static final float Y_END = 1;

        private Point2D point;
        private Node left;
        private Node right;
        private RectHV rect = null;
        private KdTree.NodeType type;
        private Node parent;

        public Node(Point2D point, KdTree.NodeType type, Node parent) {
            this.point = point;
            this.type = type;
            this.parent = parent;
            this.rect = getRect();

        }

        public double x() {
            return point.x();
        }

        public double y() {
            return point.y();
        }

        //lazy init rect
        public RectHV getRect() {
            if (rect == null) {
                if (!isHeadNode()) {
                    RectHV OutRect = parent.getRect();
                    Point2D parP = parent.getPoint();
                    if (!isVertical()) {
                        RectHV virtLeft = new RectHV(OutRect.xmin(), OutRect.ymin(), parP.x(), OutRect.ymax());
                        if (virtLeft.contains(point)) {
                            rect = virtLeft;
                        } else {
                            rect = new RectHV(parP.x(), OutRect.ymin(), OutRect.xmax(), OutRect.ymax());
                        }
                    } else {
                        RectHV virtDown = new RectHV(OutRect.xmin(), OutRect.ymin(), OutRect.xmax(), parP.y());
                        if (virtDown.contains(point)) {
                            rect = virtDown;
                        } else {
                            rect = new RectHV(OutRect.xmin(), parP.y(), OutRect.xmax(), OutRect.ymax());
                        }
                    }
                } else {
                    rect = new RectHV(X_START, Y_START, X_END, Y_END);
                }
            }
            return rect;
        }

        public boolean isVertical() {
            return type == KdTree.NodeType.VERTICAL;
        }

        public Node traverseNext(Point2D point) {
            Node nextNode;
            if (isVertical()) {
                nextNode = (point.x() >= this.point.x()) ? right : left;
            } else {
                nextNode = (point.y() > this.point.y()) ? right : left;
            }
            return nextNode;
        }

        public void setChildNode(Node childNode) {
            if (isVertical()) {
                if (childNode.getPoint().x() > getPoint().x()) this.setRight(childNode);
                else this.setLeft(childNode);
            } else {
                if (childNode.getPoint().y() > getPoint().y()) this.setRight(childNode);
                else this.setLeft(childNode);
            }
        }

        public KdTree.NodeType getChildNodeType() {
            if (isVertical()) return KdTree.NodeType.HORIZONTAL;
            else return KdTree.NodeType.VERTICAL;
        }

        public Point2D getPoint() {
            return point;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public KdTree.NodeType getType() {
            return type;
        }

        public void setPoint(Point2D point) {
            this.point = point;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public void setLeft(Node connectedLeftNode) {
            this.left = connectedLeftNode;
        }

        public void setType(KdTree.NodeType type) {
            this.type = type;
        }

        public Point2D getStartPoint() {
            if (isVertical()) {
                //look for the next horizontal's node y cordinates
                double yStart = rect.ymin();
                return new Point2D(point.x(), yStart);
            } else {
                //look for the next vertical's node x cordinates
                double xStart = rect.xmin();
                return new Point2D(xStart, point.y());
            }

        }

        public Point2D getEndPoint() {
            if (isVertical()) {
                //look for the next horizontal's node y cordinates
                double yEnd = rect.ymax();
                return new Point2D(point.x(), yEnd);
            } else {
                //look for the next vertical's node x cordinates
                double xEnd = rect.xmax();
                return new Point2D(xEnd, point.y());
            }
        }

        boolean isRightChild() {
            if (parent.isVertical()) return point.x() > parent.getPoint().x();
            else return point.y() > parent.getPoint().y();
        }

        boolean isLeftChild() {
            return !isRightChild();
        }

        boolean isHeadNode() {
            return parent == null;
        }

        public Node getParent() {
            return parent;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            System.out.println(kdtree.size());
        }
        System.out.println(kdtree.contains(new Point2D(0.75, 0.25)));
    }
}