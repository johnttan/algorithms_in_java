import java.util.Arrays;

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

            for(int i = 0;i<points.length;i++){
                points[i].draw();
                for(int j=0;j<points.length;j++){
                    for(int k=0;k<points.length;k++){
                        for(int z=0;z<points.length;z++){
                            Point current = points[i];

                            Point[] result = new Point[4];

                            double slope1 = current.slopeTo(points[j]);
                            double slope2 = current.slopeTo(points[k]);
                            double slope3 = current.slopeTo(points[z]);


                            if(slope1 == slope2 && slope1 == slope3){
                                result[0] = current;
                                result[1] = points[j];
                                result[2] = points[k];
                                result[3] = points[z];

                                Arrays.sort(result);
                                result[0].drawTo(result[1]);
                                result[1].drawTo(result[2]);
                                result[2].drawTo(result[3]);
                                
                                System.out.println(String.format("(%d, %d) -> (%d, %d) -> (%d, %d) -> (%d, %d)", result[0].x, result[0].y, result[1].x, result[1].y, result[2].x, result[2].y, result[3].x, result[3].y));
                            }
                        }
                    }
                }
            }

            i++;
        }
    }
}
