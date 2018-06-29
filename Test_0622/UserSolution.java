import java.util.Arrays;

public class UserSolution {

    int piece_no;
    int N, M, K;
    int [][] pieces;
    int [] index; 
    int [] res;

    public void init(int n, int m, int k)
    {
        piece_no = 0;
        N = n;
        M = m;
        K = k;
        pieces = new int[n*n+k][4*m];
        index = new int[n*n];
        res = new int[]{-1};

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
        piece_no++;
        
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

        int[] arr = new int[N*N+K];
        int _N = N*N+K;
        int _K = N*N;
        int cnt=0;
        //_N = 4;
        //_K = 3;
        for (int i=0;i<_N;i++){
            arr[i] = i;
        }
            int[] ans = new int[]{ 1, 8, 3, 4, 2, 5, 0, 9, 6};
            //if (isRightPieces(arr)){
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
        //genKPermutation(arr,_N, _K); 

        return cnt;
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
            
            int[] ans = new int[]{ 1, 8, 3, 4, 2, 5, 0, 9, 6};
            //if (isRightPieces(arr)){
            if (isRightPieces(ans)){
                int center_idx = arr[(N-1)/2];
                int[] center = pieces[center_idx];
                System.out.println("center " + center_idx);
                System.out.println("center arr" + Arrays.toString(center));

                //TODO: to calculate the number of '1'
                // ?? : return how?
                return ;
            }
            //System.out.println(Arrays.toString(arr));
            //return;
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