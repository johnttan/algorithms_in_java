import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> storage;

    public PointSET(){
        storage = new TreeSet<Point2D>();

    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return storage.size();
    }

    public void insert(Point2D p) throws NullPointerException{
        if(p == null){
            throw new NullPointerException();
        }
        storage.add(p);
    }

    public boolean contains(Point2D p) throws NullPointerException{
        if(p == null){
            throw new NullPointerException();
        }
        return storage.contains(p);
    }

    public void draw(){
        for(Point2D point : storage){
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) throws NullPointerException{
        if(rect == null){
            throw new NullPointerException();
        }
        ArrayList<Point2D> results;
        results = new ArrayList<Point2D>();
        for(Point2D point : storage){
            if(rect.contains(point)){
                results.add(point);
            }
        }
        return results;
    }

    public Point2D nearest(Point2D p) throws NullPointerException{
        if(p == null){
            throw new NullPointerException();
        }
        if(isEmpty()){
            return null;
        }
        Point2D nearestPoint = new Point2D(0, 0);
        double nearestDistance = Double.POSITIVE_INFINITY;
        for(Point2D point : storage){
            if(point.distanceTo(p) < nearestDistance){
                nearestPoint = point;
                nearestDistance = point.distanceTo(p);
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args){
        PointSET test = new PointSET();
    }

}
