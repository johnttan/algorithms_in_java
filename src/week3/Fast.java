
import java.util.ArrayList;
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

            ArrayList<Point> current = new ArrayList<Point>();

            for(int j=0;j<points.length;j++){
                if(pointsCopy[j].compareTo(points[q]) != 0){
                    if(current.isEmpty()){
                        current.add(pointsCopy[j]);
                    }else if(points[q].slopeTo(pointsCopy[j]) == points[q].slopeTo(current.get(current.size() - 1))){
                        current.add(pointsCopy[j]);
                        if(j == points.length -1 && current.size() >= 3){
                            Point[] temp = new Point[current.size()];
                            temp = current.toArray(temp);
                            Arrays.sort(temp);
                            temp[0].drawTo(temp[temp.length-1]);

                            String result = "";

                            for(Point point : temp){
                                result += point.toString();
                                if(temp[temp.length-1] != point){
                                    result += " -> ";
                                }
                            }
                            System.out.println(result);
                        }
                    }else if(current.size() >= 3){
                        Point[] temp = new Point[current.size()];
                        temp = current.toArray(temp);
                        Arrays.sort(temp);
                        temp[0].drawTo(temp[temp.length-1]);

                        String result = "";

                        for(Point point : temp){
                            result += point.toString();
                            if(temp[temp.length-1] != point){
                                result += " -> ";
                            }
                        }
                        System.out.println(result);
                        current = new ArrayList<Point>();
                        current.add(pointsCopy[j]);
                    }else{
                        current = new ArrayList<Point>();
                        current.add(pointsCopy[j]);
                    }
                }
            }
        }
    }
}
