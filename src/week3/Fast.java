
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

            i++;
        }
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        for(int q=0;q<points.length;q++){
            Arrays.sort(pointsCopy, points[q].SLOPE_ORDER);
            int j = 0;

            while(j < points.length - 4){
                if(points[q].compareTo(pointsCopy[j]) == 0 && points[q].compareTo(pointsCopy[j+1]) == 0 && points[q].compareTo(pointsCopy[j+2]) == 0){
                    points[j].drawTo(pointsCopy[j+1]);
                    pointsCopy[j+1].drawTo(pointsCopy[j+2]);
                    pointsCopy[j+2].drawTo(pointsCopy[j+3]);
                    Point[] result = new Point[4];
                    result[0] = points[q];
                    result[1] = points[j];
                    result[2] = points[j+1];
                    result[3] = points[j+2];
                    System.out.println(String.format("%s -> %s -> %s -> %s", result[0].toString(), result[1].toString(), result[2].toString(), result[3].toString()));
                }

                j++;
            }
        }
    }
}
