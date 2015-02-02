class UnionFind {
    private int[] ids;
    private int[] sizes;

    UnionFind(int N){
        ids = new int[N];
        sizes = new int[N];

        for(int i=0;i<N;i++) {
            ids[i] = i;
            sizes[i] = i;
        }
    }

    public int[] getIds(){
        return ids;
    }

    private int root(int i){
        while(i != ids[i]){
            i = ids[i];
        }
        return i;
    }

    private boolean connected(int origin, int dest){
        return root(origin) == root(dest);
    }


    private void union(int origin, int dest){
        int originRoot = root(origin);
        int destRoot = root(dest);
        if(sizes[originRoot] >= sizes[destRoot]){
            ids[destRoot] = originRoot;
            sizes[originRoot] += sizes[destRoot];
        }else{
            ids[originRoot] = destRoot;
            sizes[destRoot] += sizes[originRoot];
        }
    }
}

public class UnionFindTest {
    public static void main(String args[]){
        UnionFind UF = new UnionFind(10);
        int [] ids = UF.getIds();
        for(int i=0;i<ids.length;i++){
            System.out.println(ids[i]);
        }
    }
}
