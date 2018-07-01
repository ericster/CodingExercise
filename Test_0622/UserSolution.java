//import UserSolution._LinkedList;
//import UserSolution._LinkedList.ListNode;
import java.util.Arrays;

public class UserSolution {

    int piece_no;
    int N, M, K;
    int [][] pieces;
    int [] index; 
    int [] res;
    _LinkedList [] graph ;
    GridNode[] puzzle;
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

        
        public void display(){
            System.out.println("lt " + lt + " , rt " + rt + " , up " + up + " , dn " + dn );
            if (isCorner()){
                System.out.println(" -> corner node");
            }
        }
    }

    class _LinkedList {
        ListNode head;
        class ListNode {
            int data;
            ListNode next;
        }
        public _LinkedList(){
            //head = new ListNode();
            head = null;
        }

        public void add(int data){
            ListNode newNode = new ListNode();
            newNode.data = data;
            if (head == null){
                head = newNode;
            }
            else {
                newNode.next = head;
                head = newNode;
            }
        }

        public boolean search(int data){
            ListNode tmp = head;
            while (tmp !=null){
                if (tmp.data == data) return true;
                tmp = tmp.next;
            }
            return false;
        }

        public void display(){
            ListNode tmp = head;
            while (tmp !=null) {
                System.out.print(" : " + tmp.data );
                tmp = tmp.next;
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

        graph = new _LinkedList[nodeNo];
        puzzle = new GridNode[nodeNo];
        for (int i=0;i<nodeNo;i++){
            puzzle[i] = new GridNode();
        }
        for (int i=0;i<nodeNo;i++){
            graph[i] = new _LinkedList();
        }


/*
    0: 1 2 3 4
    1: 1 2 3 4
    2: 1 2 3 4
    3: 1 2 3 4
    4: 1 2 3 4
    5: 1 2 3 4
    6: 1 2 3 4
    7: 1 2 3 4
    8: 1 2 3 4
    9: 1 2 3 4

    N: size of puzzle pieces
*/

    }


    public void addPiece(int piece[])
    {
        for (int i=0;i<4*M;i++){
            pieces[piece_no][i] = piece[i];
        }
        System.out.println("no " + piece_no + Arrays.toString(pieces[piece_no]));
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
            if ( lt  || rt || up || dn){
                graph[i].add(piece_no);
                graph[piece_no].add(i);
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

    void displayGraph(_LinkedList[] graph)
    {       
        int nodeNum = N*N +K;
        for(int v = 0; v < nodeNum; v++)
        {
            System.out.println("Adjacency list of vertex "+ v);
            _LinkedList tmp = graph[v];
            tmp.display();
            System.out.println("\n");
        }
    }

    public int findCenterPiece()
    {

        // step1: permutation
        // (n*n+k)P(n*n) => index[n*n]
        // pieces[n*n+k][4*m]
        

        // step2: matching
        /*

        [0,1,2,
         3,4,5,  0+3(m), 1+3, 2+3
         6,7,8]

         one index array from permutation  nPr
         index = new int[n*n]
         visited = new int[n*n]
         neighbor:     [-1,1,-m,m]

        */
        // combination dfs


        // permutation dfs
        System.out.println("graph display");
        displayGraph(graph);
        System.out.println("*** Puzzle *** ");
        displayPuzzle(puzzle);

        int[] arr = new int[N*N+K];
        int _N = N*N+K;
        int _K = N*N;
        int cnt=0;
        //_N = 4;
        //_K = 3;
        for (int i=0;i<_N;i++){
            arr[i] = i;
        }
        /*
            int[] ans = new int[]{ 1, 8, 3, 4, 2, 5, 0, 9, 6};
       1 8 3 
       4 2 5
       0 9 6
            0
            1 
            2 
            3 
            4, 0, 1, 2
            5, 2, 3
            6, 5
            7, 
            8, 1, 2, 3
            9 0, 2, 6

            if (isRightPieces(ans)){
                int center_idx = ans[(N*N-1)/2];
                int[] center = pieces[center_idx];
                for (int i=0;i<4*M;i++){
                    if (center[i] == 1){
                        cnt++;
                    }
                }
                System.out.println("center " + center_idx);
                System.out.println("center arr" + Arrays.toString(center));
            }
        */
        //genKPermutation(arr,_N, _K); 

        return res[0];
    }


    /* 
       1 8 3 
       4 2 5
       0 9 6
    
       [0 | 1 | 2   ; 0/1:1/3, 1/1:2/3,   odd , left:1, right:2  
        -   -   -   ; 0/2:3/0, 1/2:4/0, 2/2:5/0  even
        3 | 4 | 5   ; 3/1:4/3, 4/1:5/3, odd
        -   -   -   ; 3/2:6/0, 4/2:7/0, 5/2:8/0 even
        6 | 7 | 8 ] ; 6/1:7/3, 7/1:8/3  odd  


        (N-1)*N
        0(0,1), 1(1,2) ,2(3,4) 3(4,5), 4(6,7), 5(7,8)

     */
    public boolean isRightPieces(int[] pieceArr){
        /*
         index = new int[n*n]
         visited = new int[n*n]
         neighbor:     [-1,1,-m,m]
         */
        System.out.println("peieceArr " + Arrays.toString(pieceArr));
        //int[] neighbor = new int[]{-1*M,1,M, -1};

        /* between sides
        (N-1)*N
        0(0,1), 1(1,2),
        2(3,4) 3(4,5), 
        4(6,7), 5(7,8)
        pieces = new int[n*n+k][4*m];
        */
        int[] left, right;
        int[] left_1 = new int[M];
        int[] right_3 = new int[M];
        for (int i=0;i<N;i++){
            for (int j=0;j<N-1;j++){
                // 0~M-1, M~2M-1, 2*M~3M-1, 3M~4M-1 
                left = pieces[pieceArr[j+i*N]]; // left[1]  M~2M-1, 
                right = pieces[pieceArr[j+1+i*N]]; // right[3] 3M~4M-1
                System.out.println("left " + pieceArr[j+i*N]); // left[1]  M~2M-1, 
                System.out.println("left arr" + Arrays.toString(left));
                System.out.println("right " + pieceArr[j+1+i*N]); // left[1]  M~2M-1, 
                System.out.println("right arr" + Arrays.toString(right));
                for (int k=0;k<M;k++){
                    left_1[k]=left[k+M];
                    right_3[k]= right[k+3*M];
                }
                System.out.println("left_1 " + Arrays.toString(left_1));
                System.out.println("right_3 " + Arrays.toString(right_3));

                for (int k=0;k<M;k++){
                    if (left_1[k] + right_3[M-k-1] != 0){
                        return false;
                    }

                }
            }
        }
        /* between up/down
        (N)*(N-1)
        -   -   -   ; 0/2:3/0, 1/2:4/0, 2/2:5/0  even
        -   -   -   ; 3/2:6/0, 4/2:7/0, 5/2:8/0 even
        0(0,3), 1(1,4), 2(2,5)
        3(3,6), 4(4,7), 5(5,8)
        pieces = new int[n*n+k][4*m];
        peieceArr [1, 8, 3, 
                   4, 2, 5, 
                   0, 9, 6]
        */
        int[] up, dn;
        int[] up_2 = new int[M];
        int[] dn_0 = new int[M];
        for (int i=0;i<N-1;i++){
            for (int j=0;j<N;j++){
                up = pieces[pieceArr[j + i*N]]; // up[0]  M~2M-1, 
                dn = pieces[pieceArr[j + (i+1)*N]]; // dn[2] 3M~4M-1
                System.out.println("up " + pieceArr[j + i*N]); // left[1]  M~2M-1, 
                System.out.println("up arr" + Arrays.toString(up));
                System.out.println("dn " + pieceArr[j + (i+1)*N]); // left[1]  M~2M-1, 
                System.out.println("dn arr" + Arrays.toString(dn));
                for (int k=0;k<M;k++){
                    up_2[k]= up[k+2*M];
                    dn_0[k]= dn[k];
                }
                System.out.println("up_2 " + Arrays.toString(up_2));
                System.out.println("dn_0 " + Arrays.toString(dn_0));

                for (int k=0;k<M;k++){
                    if (up_2[k] + dn_0[M-k-1] != 0){
                        return false;
                    }

                }

            }

        }


        return true;
    }

    public void genKPermutation(int arr[], int n, int r) {
        int data[]= new int[r];
        combinationUtil(arr, n, r, 0, data, 0);
    }

    public void combinationUtil(int arr[], int n, int r, int index, int data[], int i) {
        if (index == r)
        {
            permute(data,0,r-1);
            // data is fixed
            return;
        }

        // When no more elements are there to put in data[]
        if (i >= n)
        return;

        // current is included, put next at next location
        data[index] = arr[i];
        combinationUtil(arr, n, r, index+1, data, i+1);

        // current is excluded, replace it with next (Note that
        // i+1 is passed, but index is not changed)
        combinationUtil(arr, n, r, index, data, i+1);
    }


    public void permute(int[] arr, int l, int r)
    {
        if (l == r) {
            System.out.println("peieceArr " + Arrays.toString(arr));
            
            //int[] ans = new int[]{ 1, 8, 3, 4, 2, 5, 0, 9, 6};
            int cnt = 0;
            if (isRightPieces(arr)){
                int center_idx = arr[(N*N-1)/2];
                int[] center = pieces[center_idx];
                for (int i=0;i<4*M;i++){
                    if (center[i] == 1){
                        cnt++;
                    }
                }
                System.out.println("center " + center_idx);
                System.out.println("center arr" + Arrays.toString(center));
                res[0] = cnt;

            }
            //System.out.println(Arrays.toString(arr));
            return;
        }
        else
        {
            for (int i = l; i <= r; i++)
            {
                swap(arr,l,i);
                permute(arr, l+1, r);
                swap(arr,l,i);
            }
        }
    }

    public void swap(int[] a, int i, int j)
    {
        int temp;
        temp = a[i] ;
        a[i] = a[j];
        a[j] = temp;
    }

}