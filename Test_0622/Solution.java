import java.util.Scanner;

class Solution {	
	private static Scanner sc;

	private static int N;
	private static int M;
	private static int K;

	private static int piece[] = new int[60];
	
	private static UserSolution usersolution = new UserSolution();
	
	private static int mSeed;
	private static int pseudo_rand()
	{
		mSeed = mSeed * 431345 + 2531999;
		return mSeed & 0x7FFFFFFF;
	}
	
	private static int run()
	{
		N = sc.nextInt();
		M = sc.nextInt();
		K = sc.nextInt();
		
		usersolution.init(N, M, K);
		
		for (int i = 0; i < N * N + K; ++i) {
			for (int k = 0; k < 4; ++k) {
				int idx = k * M;
				piece[idx] = piece[idx + M - 1] = 0;
				
				mSeed = sc.nextInt();
				if (mSeed > 0) {
					for (int m = 1; m < M - 1; ++m)
						piece[idx + m] = pseudo_rand() % 3 - 1;
				}
				else {
					mSeed *= -1;
					for (int m = M - 2; m > 0; --m)
						piece[idx + m] = 1 - pseudo_rand() % 3;
				}
			}
			usersolution.addPiece(piece);
		}
		
		int result = usersolution.findCenterPiece();
		return result;
	}
	
	public static void main(String[] args) throws Exception {
	
		System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		sc = new Scanner(System.in);

		int TC = sc.nextInt();
		for (int testcase = 1; testcase <= TC; ++testcase) {
            System.out.println("#" + testcase + " " + run());
		}

		sc.close();
	}
}