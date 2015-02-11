/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder(Point.this);       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    private static class SlopeOrder implements Comparator<Point> {
        private Point currentPoint;
        SlopeOrder (Point point) {
            currentPoint = point;
        }
        public int compare(Point v, Point w)
        {
            return (int)v.slopeTo(currentPoint) - (int)w.slopeTo(currentPoint);
        }
    }

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if(that.x == this.x){
            if(this.y <= that.y){
                return Double.POSITIVE_INFINITY;
            }else if(this.y > that.y){
                return Double.NEGATIVE_INFINITY;
            }
        }
        return (that.y - this.y) / (that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if(this.y > that.y){
            return 1;
        }else if(this.y == that.y){
            if(this.x > that.x){
                return 1;
            }else if(this.x == that.x){
                return 0;
            }else{
                return -1;
            }
        }else {
            return -1;
        }
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        Point test = new Point(1, 1);
        Point test2 = new Point(1, 2);
        double compare = test.slopeTo(test2);
        assert compare == Double.POSITIVE_INFINITY : "should be positive infinity";

        test = new Point(0, 0);
        test2 = new Point(0, -1);
        compare = test.slopeTo(test2);
        assert compare == Double.NEGATIVE_INFINITY : "should be negative infinity";

        test = new Point(1, 1);
        test2 = new Point(2, 2);
        compare = test.slopeTo(test2);
        assert compare == 1 : "should be 1";
    }
}
