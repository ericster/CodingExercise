class UserSolution {

    int piece_no;
    int N, M, K;
    int [][] pieces;
    int [] index; 

    public void init(int n, int m, int k)
    {
        piece_no = 0;
        N = n;
        M = m;
        K = k;
        pieces = new int[n*n+k][4*m];
        index = new int[n*n];

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
        for (int i=0;i<N*N+K;i++){
            arr[i] = i;
        }
        genKPermutation(arr,N,K); 


        for (int i=0;i<N*N;i++){

        }
        return 0;
    }


    // [0,1,3,4...8]
    public boolean isRightPieces(int[] pieceArr){
        /*
         index = new int[n*n]
         visited = new int[n*n]
         neighbor:     [-1,1,-m,m]
         */
        int[] neighbor = new int[]{-1*M,1,M, -1};

        for (int i=0;i<N*N;i++){
            int[] curr = pieces[i];
            for (int j=0;j<4;i++){
                // 0:up, 1:right, 2:down, 3:left
                if ((i+neighbor[j]) >=0 && (i+neighbor[j]) < N){
                    int[] neigh = pieces[i+neighbor[j]];
                    for (int k=0;k<M;k++){
                        if (curr[k] != neigh[k]){
                            return false;
                        }

                    }

                }

            }
        }

        return true;
    }

    public void combinationUtil(int arr[], int n, int r, int index, int data[], int i) {
        if (index == r)
        {
            int len = data.length;
            permute(data,0,len-1);
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

    public void genKPermutation(int arr[], int n, int r) {
        int data[]= new int[r];
        combinationUtil(arr, n, r, 0, data, 0);
    }

    public void permute(int[] arr, int l, int r)
    {
        if (l == r) {
            if (isRightPieces(arr)){
                int center_idx = 0;
                int[] center = pieces[center_idx];

                //TODO: to calculate the number of '1'
                // ?? : return how?
                return ;
            }
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