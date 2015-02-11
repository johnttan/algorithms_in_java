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
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    private class SlopeOrder implements Comparator<Point> {
        private Point currentPoint;
        SlopeOrder () {

        }
        public int compare(Point v, Point w)
        {
            Point currentPoint = Point.this;
            int result = (int) (v.slopeTo(currentPoint) - w.slopeTo(currentPoint));
            if(result < 0){
                return -1;
            }else if(result > 0){
                return 1;
            }else{
                return 0;
            }
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
        if(that.x == this.x && that.y == this.y){
            return Double.NEGATIVE_INFINITY;
        }
        if(that.x == this.x){
            return Double.POSITIVE_INFINITY;
        }
        if(that.y == this.y){
            if(that.x > this.x){
                return -0;
            }else{
                return +0;
            }
        }
        double x = that.x;
        double y = that.y;
        double x2 = this.x;
        double y2 = this.y;
        double result = (y - y2) / (x - x2);
        return result;
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
        Point test3;
        Point test = new Point(1, 5);
        Point test2 = new Point(1, 2);
        double compare = test.slopeTo(test2);
        assert compare == Double.POSITIVE_INFINITY : "should be negative infinity";

        test = new Point(30, 205);
        test2 = new Point(30, 467);
        compare = test.slopeTo(test2);
        assert compare == Double.POSITIVE_INFINITY : "should be positive infinity";

        test = new Point(8762, 7985);
        test2 = new Point(8762, 9234);
        compare = test.slopeTo(test2);
        assert compare == Double.POSITIVE_INFINITY : "should be positive infinity";

        test = new Point(181, 486);
        test2 = new Point(181, 156);
        compare = test.slopeTo(test2);
        assert compare == Double.POSITIVE_INFINITY : "should be positive infinity";

        test = new Point(181, 181);
        test2 = new Point(181, 181);
        compare = test.slopeTo(test2);
        assert compare == Double.NEGATIVE_INFINITY : "should be positive infinity";

        test = new Point(97, 407);
        test2 = new Point(377, 218);
        compare = test.slopeTo(test2);
        assert compare == -0.675 : compare + " should be positive infinity";

        test = new Point(1, 1);
        test2 = new Point(2, 2);
        compare = test.slopeTo(test2);
        assert compare == 1 : "should be 1";

        test = new Point(1, 1);
        test2 = new Point(2, 2);
        test3 = new Point(3, 3);
        compare = test.SLOPE_ORDER.compare(test2, test3);
        assert compare == 0.0 : "should be 0.0";

        Point p = new Point(492, 112);
        Point q = new Point(257, 470);
        Point r = new Point(492, 310);
        int compare1 = p.SLOPE_ORDER.compare(q, r);
        int compare2 = p.SLOPE_ORDER.compare(r, q);
        System.out.println(compare1);
        System.out.println(compare2);
    }
}
