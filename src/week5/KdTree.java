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

    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return sizeNum;
    }

    private Node traverseInsert(Node node, Node newNode, Node parent){
        if(node == null){
            newNode.setOppDirection(parent.direction());
            return newNode;
        }
        if(node.compareTo(newNode) > 0) {
            node.right = traverseInsert(node.right, newNode, node);
        }else{
           node.left = traverseInsert(node.left, newNode, node);
        }
        return node;
    }

    public void insert(Point2D p){
        Node current = root;
        Node newNode = new Node(p);
        if(root == null){
            newNode.setOppDirection("y");
            RectHV rect = new RectHV(0, 0, 1, 1);
            newNode.setRect(rect);
            root = newNode;
            return;
        }
        traverseInsert(current, newNode, null);
        sizeNum ++;
    }

    private boolean traverseContains(Node node, Node p){
        if(node.point() == p.point()){
            return true;
        }
        if(node.compareTo(p) > 0) {
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

    public boolean contains(Point2D p){
        Node wrappedPoint = new Node(p);
        return traverseContains(root, wrappedPoint);
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
