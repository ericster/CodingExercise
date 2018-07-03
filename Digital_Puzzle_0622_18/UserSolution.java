import java.util.Arrays;

public class UserSolution {

    int piece_no;
    int N, M, K;
    int [][] pieces;
    int [] index; 
    int [] res;
    GridNode[] puzzle;
    enum Dirs {LEFT, RIGHT, UP, DOWN};
    class GridNode {
        int lt;
        int rt;
        int up;
        int dn;
        public GridNode(){
            lt=-1;
            rt=-1;
            up=-1;
            dn=-1;
        }

        public boolean isCorner(){
            boolean ul = (lt == -1 && rt >=0 && up == -1 && dn >=0) ? true : false;
            boolean ur = (lt >= 0 && rt == -1 && up == -1 && dn >=0) ? true : false;
            boolean dl = (lt == -1 && rt >=0 && up >= 0 && dn == -1) ? true : false;
            boolean dr = (lt >= 0 && rt == -1 && up >= 0 && dn == -1) ? true : false;
            boolean corner = ul || ur || dl || dr ; 
            return corner;
        }

        public boolean isUL() {
            return (lt == -1 && rt >=0 && up == -1 && dn >=0);
        }

        public boolean isDR() {
            return (lt >= 0 && rt == -1 && up >= 0 && dn == -1) ;
        }

        
        public void display(){
            System.out.println("lt " + lt + " , rt " + rt + " , up " + up + " , dn " + dn );
            if (isCorner()){
                System.out.println(" -> corner node");
            }
        }
    }


    public void init(int n, int m, int k)
    {
        piece_no = 0;
        N = n;
        M = m;
        K = k;
        int nodeNo = n*n+k;
        pieces = new int[n*n+k][4*m];
        index = new int[n*n];
        res = new int[]{-1};

        puzzle = new GridNode[nodeNo];
        for (int i=0;i<nodeNo;i++){
            puzzle[i] = new GridNode();
        }


    }


    public void addPiece(int piece[])
    {
        for (int i=0;i<4*M;i++){
            pieces[piece_no][i] = piece[i];
        }
        //System.out.println("no " + piece_no + Arrays.toString(pieces[piece_no]));
        for (int i=0;i<piece_no;i++){
            int [] prev = pieces[i];
            boolean lt = true, rt = true, up = true, dn = true;
                // 0...M-1 :     M ... 2*M-1 : 2*M .. 3*M-1 : 3*M .. 4*M-1
                // 2*M .. 3*M-1  3*M .. 4*M-1  0..M-1         M.. 2*M-1
                /*
                    1 8 3 
                    4 2 5
                    0 9 6
                
                */
            for (int j=0;j<M;j++){
                if (prev[j] + piece[3*M-1 - j] != 0 ) dn = false;
            }
            
            for (int j=0;j<M;j++){
                if (prev[j+M] + piece[4*M -1 - j] != 0) lt = false;
            }
            for (int j=0;j<M;j++){
                if (prev[j + 2*M] + piece[M -1 - j] != 0) up  = false;
            }

            for (int j=0;j<M;j++){
                if (prev[j + 3*M] + piece[2*M-1 - j] != 0) rt = false;
            }

            if (lt){
                puzzle[i].rt = piece_no;
                puzzle[piece_no].lt = i;
            }
            if (rt){
                puzzle[i].lt = piece_no;
                puzzle[piece_no].rt = i;
            }
            if (up){
                puzzle[i].dn = piece_no;
                puzzle[piece_no].up = i;
            }
            if (dn){
                puzzle[i].up = piece_no;
                puzzle[piece_no].dn = i;
            }
            /*
            4 corners
            uplt = up, lt
            uprt = up, rt
            dnlt = dn, lt
            dnrt = dn, rt
            */
        }
        piece_no++;
    }

    void displayPuzzle(GridNode[] puzzle) {
        int nodeNum = N*N +K;
        for(int v = 0; v < nodeNum; v++) {
            System.out.println(" Puzzle node "+ v);
            puzzle[v].display();;
        }
    }


    public int findCenterPiece()
    {
        System.out.println("*** Puzzle *** ");
        //displayPuzzle(puzzle);

        int[] arr = new int[N*N+K];
        int _N = N*N+K;
        int _K = N*N;
        int cnt=0;
        //_N = 4;
        //_K = 3;
        int ul, dr;
        int depth = 0;
        int step = N/2 + 1;
        for (int i=0;i<_N;i++){
            //arr[i] = i;
            if (puzzle[i].isUL()){
                System.out.println("******** piece at 00 : " + i);
                dfs(i,0, Dirs.RIGHT);
            }
        }
        /*
            int[] ans = new int[]{ 1, 8, 3, 4, 2, 5, 0, 9, 6};
       1 8 3 
       4 2 5
       0 9 6
        */

        return res[0];
    }

    public void dfs(int loc, int depth, Dirs dir){
        GridNode node = puzzle[loc];
        //System.out.println("puzze " + loc + " depth " + depth + "node lt, rt, up, dn " 
        //+ node.lt + " " + node.rt + " " + node.up + " " + node.dn);

        if (depth == N-1){
            int cnt=0;
            int[] center = pieces[loc];
            System.out.println("center candidate " );
            if (isCenter_dfs(loc, 0, Dirs.RIGHT)){
                for (int i=0;i<4*M;i++){
                    if (center[i] == 1){
                        cnt++;
                    }
                }
                res[0] = cnt;
                System.out.println("center " + loc + " count " + cnt);
                return;
            }
        }

        if (depth > N-1) return;

        int next;
        if (dir == Dirs.RIGHT){
            next = node.rt;
            if (next <0){
                return;
            }
            dfs( next, depth+1, Dirs.DOWN);
        } else if (dir == Dirs.DOWN){
            next = node.dn;
            if (next <0){
                return;
            }
            dfs( next, depth+1, Dirs.RIGHT);
        }


    }

    public boolean isCenter_dfs(int loc, int depth, Dirs dir){
        GridNode node = puzzle[loc];
        //System.out.println("puzzle isDR() " + loc + " depth " + depth );
    
        if (depth == N-1){
            System.out.println("puzzle isDR() " + loc + " depth " + depth + "node lt, rt, up, dn " 
            + node.lt + " " + node.rt + " " + node.up + " " + node.dn);
            if (node.isDR()) return true;
            else return false;
        }
        if (depth > N-1) return false;
        int next;
        if (dir == Dirs.RIGHT){
            next = node.rt;
            if (next <0){
                return false;
            }
            return isCenter_dfs( next, depth+1, Dirs.DOWN);
        } else if (dir == Dirs.DOWN){
            next = node.dn;
            if (next <0){
                return false;
            }
            return isCenter_dfs( next, depth+1, Dirs.RIGHT);
        }
        return false;
    }

}