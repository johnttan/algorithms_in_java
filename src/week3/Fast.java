
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
            current.add(points[q]);

            for(int j=0;j<points.length;j++){
//                System.out.println(points[q].slopeTo(pointsCopy[j]));
//                System.out.println(pointsCopy[j]);
                if(pointsCopy[j].compareTo(points[q]) != 0){
                    if(current.isEmpty()){
                        System.out.println("SHOULD NOT SEE THIS");
//                        current.add(pointsCopy[j]);
                    }else if(points[q].slopeTo(pointsCopy[j]) == points[q].slopeTo(current.get(current.size() - 1))){
                        current.add(pointsCopy[j]);
                        if(j == points.length -1 && current.size() >= 4){
                            Point[] temp = new Point[current.size()];
                            temp = current.toArray(temp);
                            Arrays.sort(temp);

                            String result = "";

                            for(Point point : temp){
                                result += point.toString();
                                if(temp[temp.length-1] != point){
                                    result += " -> ";
                                }
                            }
                            String key = String.format("%s %s", temp[0].toString(), temp[temp.length-1].toString());
                            if(!table.containsKey(key)){
                                System.out.println(result);
                                temp[0].drawTo(temp[temp.length-1]);
                                table.put(key, true);
                            }

                        }
                    }else if(current.size() >= 4){
                        Point[] temp = new Point[current.size()];
                        temp = current.toArray(temp);
                        Arrays.sort(temp);

                        String result = "";

                        for(Point point : temp){
                            result += point.toString();
                            if(temp[temp.length-1] != point){
                                result += " -> ";
                            }
                        }
                        String key = String.format("%s %s", temp[0].toString(), temp[temp.length-1].toString());
                        if(!table.containsKey(key)){
                            System.out.println(result);
                            temp[0].drawTo(temp[temp.length-1]);
                            table.put(key, true);
                        }
                        current = new ArrayList<Point>();
                        current.add(points[q]);
                        current.add(pointsCopy[j]);
                    }else{
                        current = new ArrayList<Point>();
                        current.add(points[q]);
                        current.add(pointsCopy[j]);
                    }
                }
            }
        }
    }
}
