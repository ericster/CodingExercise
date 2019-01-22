import java.util.Scanner;

class Solution {
	
	private static Scanner sc;
	private static UserSolution usersolution = new UserSolution();
	
	private final static int ADDRATE		= 45;
	private final static int REMOVERATE		= 50;
	private final static int SEARCH1RATE	= 90;
	private final static int SEARCH2RATE	= 100;
	
	private static int seed;
	private static int radiusTb[] = new int [5050];
	private static int K;

	private static int pseudo_rand()
	{
		seed = seed * 431345 + 2531999;
		return seed & 0x7FFFFFFF;
	}

	private static boolean run() {
		int N, M, initAdd;
		int id = 1;
		boolean removeChk[];
		boolean isCorrect = true;
		
		seed= sc.nextInt();
		N = sc.nextInt();
		M = sc.nextInt();
		initAdd = sc.nextInt();
		
		removeChk = new boolean [M + N * initAdd];

		if (N >= 100) {
			K = 5050;
		}
		else {
			K = 0;

			for (int i = 1; i <= 100; ++i) {
				if (radiusTb[K] > N) {
					break;
				}

				K += i;
			}
		}

		usersolution.init(N);

		for (int i = 0; i < N * initAdd; ++i) {
			int type = pseudo_rand() % 10 + 1;
			int y = pseudo_rand() % N + 1;
			int x = pseudo_rand() % N + 1;

			usersolution.addFacility(id++, type, y, x);
		}

		while (M-- > 0) {
			int cmd = pseudo_rand() % 100;

			if (cmd < ADDRATE) {
				int type = pseudo_rand() % 10 + 1;
				int y = pseudo_rand() % N + 1;
				int x = pseudo_rand() % N + 1;

				usersolution.addFacility(id++, type, y, x);
			}
			else if (cmd < REMOVERATE) {
				int removeId = pseudo_rand() % (id - 1) + 1;

				while (removeChk[removeId]) {
					removeId = pseudo_rand() % (id - 1) + 1;
				}

				usersolution.removeFacility(removeId);

				removeChk[removeId] = true;
			}
			else if (cmd < SEARCH1RATE) {
				int type = pseudo_rand() % 11;
				int y = pseudo_rand() % N + 1;
				int x = pseudo_rand() % N + 1;
				int radius = pseudo_rand() % K;
				int ret, ans;

				ret = usersolution.search1(type, y, x, radiusTb[radius]);

				ans = sc.nextInt();

				if (ret != ans) {
					System.out.println("Search1 incorrect");
					isCorrect = false;
				}
			}
			else {
				int type = pseudo_rand() % 10 + 1;
				int y = pseudo_rand() % N + 1;
				int x = pseudo_rand() % N + 1;
				int ret[] = new int[5];
				int ansN, ans;

				usersolution.search2(type, y, x, ret);

				ansN = sc.nextInt();
				for (int i = 0; i < ansN; ++i) {
					ans = sc.nextInt();

					if (ret[i] != ans) {
						System.out.println("Search2 incorrect");
						isCorrect = false;
					}
				}
				for (int i = ansN; i < 5; ++i) {
					if (ret[i] != 0) {
						isCorrect = false;
					}
				}
			}
		}

		return isCorrect;
	}

	public static void main(String[] args) throws Exception {

		int test, T;
		int answerScore;

		System.setIn(new java.io.FileInputStream("sample_input2.txt"));
		sc = new Scanner(System.in);
		
		T = sc.nextInt();
		answerScore = sc.nextInt();

		for (int i = 1; i <= 100; ++i) {
			for (int j = 0; j < i; ++j) {
				radiusTb[K++] = i;
			}
		}

		for (test = 1; test <= T; ++test) {

			if (run()) {
				System.out.println("#" + test + " " + answerScore);
			}
			else {
				System.out.println("#" + test + " 0");
			}
		}
	}
}
