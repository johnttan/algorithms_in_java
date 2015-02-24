import java.util.ArrayList;
import java.util.TreeSet;

public class KdTree {

    private class Node {
        private Point2D data;
        //x || y
        private String directionStr;
        private Node right;
        private Node left;
        public Node(Point2D point) {
            data = point;
            right = null;
            left = null;
        }

        public double compareTo(Node that){
            if(direction.equals("y")){
                return point().y() - that.point().y();
            }else{
                return point().x() - that.point().x();
            }
        }

        public Point2D point(){
            return data;
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

        public String direction(){
            return directionStr;
        }

        public void setOppDirection(String dir){
            if(dir == "y"){
                directionStr = "x";
            }else{
                directionStr = "y";
            }
        }
    }

    private Node root;
    private int sizeNum;

    public KdTree(){

    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return sizeNum;
    }

    private void traverseInsert(Node node, Node newNode){
        if(node.compareTo(newNode) > 0) {
            if(node.right == null){
                node.right = newNode;
                newNode.setOppDirection(node.direction());
            }else{
                traverseInsert(node.right, newNode);
            }
        }else{
            if(node.left == null){
                node.left = newNode;
                newNode.setOppDirection(node.direction());
            }else{
                traverseInsert(node.left, newNode);
            }
        }
    }

    public void insert(Point2D p){
        Node current = root;
        Node newNode = new Node(p);
        newNode.setOppDirection("y");
        if(root == null){
            root = newNode;
            return;
        }
        traverseInsert(current, newNode);
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
