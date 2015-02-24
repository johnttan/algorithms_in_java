import java.util.ArrayList;

public class KdTree {

    private class Node {
        private Point2D data;
        //x || y
        private String directionStr;
        private Node right;
        private Node left;
        private RectHV rect;

        public Node(Point2D point) {
            data = point;
            right = null;
            left = null;
        }

        public double compareTo(Node that){
            if(directionStr.equals("y")){
                return point().y() - that.point().y();
            }else{
                return point().x() - that.point().x();
            }
        }

        public Point2D point(){
            return data;
        }

        public void setPoint(Point2D point){
            data = point;
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

        public void setRect(RectHV rectangle){
            rect = rectangle;
        }

        public RectHV getRect(){
            return rect;
        }
    }

    private Node root;
    private int sizeNum;

    public KdTree(){
        sizeNum = 0;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return sizeNum;
    }

    private Node traverseInsert(Node node, Node newNode, Node parent, String side){
        if(node == null){
            newNode.setOppDirection(parent.direction());
            RectHV rect;
            if(side == "right"){
                if(parent.direction() == "y"){
                    rect = new RectHV(parent.getRect().xmin(), parent.point().y(), parent.getRect().xmax(), parent.getRect().ymax());
                }else{
                    rect = new RectHV(parent.point().x(), parent.getRect().ymin(), parent.getRect().xmax(), parent.getRect().ymax());
                }
            }else{
                if(parent.direction() == "y"){
                    rect = new RectHV(parent.getRect().xmin(), parent.getRect().ymin(), parent.getRect().xmax(), parent.point().y());
                }else{
                    rect = new RectHV(parent.getRect().xmin(), parent.getRect().ymin(), parent.point().x(), parent.getRect().ymax());
                }
            }
            newNode.setRect(rect);
            sizeNum ++;
            return newNode;
        }
        if(node.point().compareTo(newNode.point()) == 0){
            return node;
        }
        if(node.compareTo(newNode) < 0) {
            node.right = traverseInsert(node.right, newNode, node, "right");
        }else{
           node.left = traverseInsert(node.left, newNode, node, "left");
        }
        return node;
    }

    public void insert(Point2D p) throws NullPointerException{
        if(p == null){
            throw new NullPointerException();
        }
        Node current = root;
        Node newNode = new Node(p);
        if(root == null){
            newNode.setOppDirection("y");
            RectHV rect = new RectHV(0, 0, 1, 1);
            newNode.setRect(rect);
            root = newNode;
            sizeNum ++;
            return;
        }
        traverseInsert(current, newNode, null, null);
    }

    private boolean traverseContains(Node node, Node p){
        if(node.point().compareTo(p.point()) == 0){
            return true;
        }
        if(node.compareTo(p) < 0) {
            if(node.right == null){
                return false;
            }else{
                return traverseContains(node.right, p);
            }
        }else{
            if(node.left == null){
                return false;
            }else{
                return traverseContains(node.left, p);
            }
        }
    }

    public boolean contains(Point2D p) throws NullPointerException{
        if(p == null){
            throw new NullPointerException();
        }
        if(root == null){
            return false;
        }
        Node wrappedPoint = new Node(p);
        return traverseContains(root, wrappedPoint);
    }

    private void traverseDraw(Node node){
        if(node == null){
            return;
        }

        node.point().draw();
        node.getRect().draw();

        traverseDraw(node.right);
        traverseDraw(node.left);
    }

    public void draw(){
        traverseDraw(root);
    }

    private void traverseRange(Node node, RectHV rect, ArrayList<Point2D> results){
        if(node == null){
            return;
        }
        if(rect.contains(node.point())){
            results.add(node.point());
        }
        if(node.getRect().intersects(rect)){
            traverseRange(node.right(), rect, results);
            traverseRange(node.left(), rect, results);
        }
    }

    public Iterable<Point2D> range(RectHV rect) throws NullPointerException{
        if(rect == null){
            throw new NullPointerException();
        }
        ArrayList<Point2D> results = new ArrayList<Point2D>();
        traverseRange(root, rect, results);
        return results;
    }

    private void traverseNearest(Node node, Point2D p, Node closest){
        if(node == null){
            return;
        }
        if(node.getRect().distanceSquaredTo(p) > closest.point().distanceSquaredTo(p)){
            return;
        }

        if(node.point().distanceSquaredTo(p) < closest.point().distanceSquaredTo(p)){
            closest.setPoint(node.point());
        }

        if(node.point().compareTo(p) < 0){
            traverseNearest(node.right, p, closest);
            traverseNearest(node.left, p, closest);
        }else{
            traverseNearest(node.left, p, closest);
            traverseNearest(node.right, p, closest);
        }
    }

    public Point2D nearest(Point2D p) throws NullPointerException{
        if(p == null){
            throw new NullPointerException();
        }
        if(root == null){
            return null;
        }
        Node closestNode = new Node(root.point());
        traverseNearest(root, p, closestNode);
        return closestNode.point();
    }

    public static void main(String[] args){
    }


}
