public class Brute {
    public static void main (String[] args) {
        In file = new In(args[0]);
        int numPoints = file.readInt();
        Point[] points = new Point[numPoints];

        int i = 0;
        while(!file.isEmpty()){
            int x = file.readInt();
            int y = file.readInt();
            points[i] = new Point(x, y);

            for(int i = 0;i<points.length;i++){
                for(int j=0;j<points.length;j++){
                    for(int k=0;k<points.length;k++){
                        for(int z=0;z<points.length;z++){
                            Point current = points[i];

                            
                            double slope1 = current.slopeTo(points[j]);
                            double slope2 = current.slopeTo(points[k]);
                            double slope3 = current.slopeTo(points[z]);


                            if(slope1 == slope2 && slope1 == slope3){

                            }
                        }
                    }
                }
            }

            i++;
        }
    }
}
