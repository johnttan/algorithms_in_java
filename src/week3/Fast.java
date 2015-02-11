
import java.util.Arrays;
import java.util.HashMap;

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
        HashMap table = new HashMap();
        for(int q=0;q<points.length;q++){
            Arrays.sort(pointsCopy, points[q].SLOPE_ORDER);
            points[q].draw();
            int j = 0;

            while(j < points.length - 2){
                if(points[q].compareTo(pointsCopy[j]) != 0 && points[q].compareTo(pointsCopy[j+1]) != 0 && points[q].compareTo(pointsCopy[j+2]) != 0
                        && pointsCopy[j].compareTo(pointsCopy[j+1]) != 0 && pointsCopy[j].compareTo(pointsCopy[j+2]) != 0 && pointsCopy[j+1].compareTo(pointsCopy[j+2]) != 0){


                    if(points[q].slopeTo(pointsCopy[j]) == points[q].slopeTo(pointsCopy[j + 1]) && points[q].slopeTo(pointsCopy[j]) == points[q].slopeTo(pointsCopy[j + 2])){
                        Point[] result = new Point[4];
                        result[0] = points[q];
                        result[1] = pointsCopy[j];
                        result[2] = pointsCopy[j+1];
                        result[3] = pointsCopy[j+2];
                        Arrays.sort(result);
                        String resultStr = String.format("%s -> %s -> %s -> %s", result[0].toString(), result[1].toString(), result[2].toString(), result[3].toString());
                        if(!table.containsKey(resultStr)){
                            result[0].drawTo(result[3]);
                            table.put(resultStr, true);
                            System.out.println(resultStr);
                        }
                    }
                }
                j++;
            }
        }
    }
}
