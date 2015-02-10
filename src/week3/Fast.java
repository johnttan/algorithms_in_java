import java.util.Arrays;

public class Fast {
    public static void main (String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        In file = new In(args[0]);
        int numPoints = file.readInt();
        Point[] points = new Point[numPoints];

        int i = 0;
        while(!file.isEmpty()) {
            int x = file.readInt();
            int y = file.readInt();
            points[i] = new Point(x, y);

            Arrays.sort(points, Point.SLOPE_ORDER);

            int j = 0;

            ArrayList<Point> current;
            for(int q=0;q<points.length;q++){
                while(j < points.length - 4){
                    if(points[q].slopeTo(points[j]))

                    j++;
                }
            }


            i++;
        }
    }
}
