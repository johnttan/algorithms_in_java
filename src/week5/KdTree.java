import java.util.ArrayList;
import java.util.TreeSet;

public class KdTree {

    private class Node {
        private Point2D data;
        //x || y
        private String direction;
        private Node right;
        private Node left;
        public Node(Point2D point, String dir) {
            data = point;
            direction = dir;
            right = null;
            left = null;
        }

        public double compareTo(Point2D that){
            if(direction.equals("y")){
                return this.data.y() - that.y();
            }else{
                return this.data.x() - that.x();
            }
        }

        public void setRight(Node p){
            right = p;
        }

        public void setLeft(Node p){
            left = p;
        }

        public Node right(){
            return right;
        }

        public Node left(){
            return left;
        }
    }

    public KdTree(){

    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
    }

    public void insert(Point2D p){
    }

    public boolean contains(Point2D p){
    }

    public void draw(){
    }

    public Iterable<Point2D> range(RectHV rect){
    }

    public Point2D nearest(Point2D p) {
    }

    public static void main(String[] args){
    }


}
