
import java.util.Arrays;
import java.util.HashMap;

public class Brute {
    public static void main (String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        In file = new In(args[0]);
        int numPoints = file.readInt();
        Point[] points = new Point[numPoints];

        int i = 0;
        while(!file.isEmpty()){
            int x = file.readInt();
            int y = file.readInt();
            points[i] = new Point(x, y);
            i++;
        }
//        System.out.println("Completed loading");
//        System.out.println(points.length);
        HashMap table = new HashMap();

        for(int p = 0;p<points.length;p++){
            points[p].draw();
//            System.out.println(p);
            for(int j=0;j<points.length;j++){
                for(int k=0;k<points.length;k++){
                    for(int z=0;z<points.length;z++){
                        if(p != j && p != k && p!= z && j != k && j != z && k != z){
                            Point current = points[p];
                            Point[] result = new Point[4];

                            double slope1 = current.slopeTo(points[j]);
                            double slope2 = current.slopeTo(points[k]);
                            double slope3 = current.slopeTo(points[z]);
                            if(slope1 == slope2 && slope2 == slope3){
//                                System.out.println(String.format("%f %f %f", slope1, slope2, slope3));
                                result[0] = current;
                                result[1] = points[j];
                                result[2] = points[k];
                                result[3] = points[z];

                                Arrays.sort(result);

//                                System.out.println(String.format("%d %d %d %d", p, j, k, z));
                                String resultString = String.format("%s -> %s -> %s -> %s", result[0].toString(), result[1].toString(), result[2].toString(), result[3].toString());
                                if(!table.containsKey(resultString)){
                                    result[0].drawTo(result[3]);
                                    table.put(resultString, true);
                                    System.out.println(resultString);
                                }

                            }
                        }

                    }
                }
            }
        }
    }
}
