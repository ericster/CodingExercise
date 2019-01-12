import java.util.Scanner;

class Solution {	
	private static Scanner sc;
	private static UserSolution usersolution = new UserSolution();
	
	private final static int INIT 			= 0;
	private final static int EDIT 			= 1;
	private final static int EDITDONE		= 2;
	private final static int ADD 			= 3;
	private final static int DELETE 		= 4;
	private final static int SUBSTITUTE		= 5;
	
	private static char initbuf[] = new char[100000];
	private static char strbuf[] = new char[200];
	private static int mSeed;

	private static int pseudo_rand()
	{
		mSeed = mSeed * 431345 + 2531999;
		return mSeed & 0x7FFFFFFF;
	}
	
	private static int initstr(int N, int Max, int Min, int RatioN)
	{
		int line = 0;
		int cnt = 0;
		int idx = 0;
		while (line <= N) {
			if (cnt == Max || (Min < cnt && pseudo_rand() % 100 >= RatioN)) {
				if (line == N) break;
				initbuf[idx++] = '\n';
				++line;
				cnt = 0;
			}
			else {
				initbuf[idx++] = (char)('a' + pseudo_rand() % 26);
				++cnt;
			}
		}
		return idx;
	}

	private static int run()
	{
		int cmd, p1, p2, p3, p4, p5, p6, result, length;
		int sample_test;		

		cmd = sc.nextInt();
		p1 = sc.nextInt();
		p2 = sc.nextInt();
		p3 = sc.nextInt();
		p4 = sc.nextInt();
		p5 = sc.nextInt();
		p6 = sc.nextInt();
		sample_test = sc.nextInt();
		
		if (sample_test == 1) {
			length = p2;
			String str = sc.next();
			for (int i = 0; i < length; ++i) {
				if (str.charAt(i) == 'N') initbuf[i] = '\n';
				else initbuf[i] = str.charAt(i);
			}
			
		}
		else {
			mSeed = p2;
			length = initstr(p3, (p4 / 1000), (p4 % 1000), p5);
		}

		usersolution.Init(p1, length, initbuf);

		int cmd_length = p6;
		int answer = 100;
		for (int i = 1; i < cmd_length; ++i) {
			cmd = sc.nextInt();

			switch (cmd) {
			case EDIT:
				p1 = sc.nextInt();
				p2 = sc.nextInt();
				p3 = sc.nextInt();
				usersolution.Edit(p1, p2, p3);
				break;

			case EDITDONE:
				p1 = sc.nextInt();
				p2 = sc.nextInt();
				result = usersolution.EditDone(p1);
				if (result != p2)
					answer = 0;
				break;

			case ADD:
				p1 = sc.nextInt();
				p2 = sc.nextInt();
				p3 = sc.nextInt();
				if (sample_test == 1) {
					String str = sc.next();
					for (int k = 0; k < p2; ++k)
						strbuf[k] = str.charAt(k);
				}
				else {
					mSeed = p3;
					for (int k = 0; k < p2; ++k)
						strbuf[k] = (char)('a' + pseudo_rand() % 26);
				}
				usersolution.Add(p1, p2, strbuf);
				break;

			case DELETE:
				p1 = sc.nextInt();
				p2 = sc.nextInt();
				usersolution.Delete(p1, p2);
				break;

			case SUBSTITUTE:
				p1 = sc.nextInt();
				p2 = sc.nextInt();
				p3 = sc.nextInt();
				if (sample_test == 1) {
					String str = sc.next();
					for (int k = 0; k < p2; ++k)
						strbuf[k] = str.charAt(k);
				}
				else {
					mSeed = p3;
					for (int k = 0; k < p2; ++k)
						strbuf[k] = (char)('a' + pseudo_rand() % 26);
				}
				usersolution.Substitute(p1, p2, strbuf);
				break;

			default:
				break;
			}
		}

		return answer;
	}
	
	public static void main(String[] args) throws Exception {
	
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		sc = new Scanner(System.in);

		int TC = sc.nextInt();
		for (int testcase = 1; testcase <= TC; ++testcase) {
            System.out.println("#" + testcase + " " + run());
		}

		sc.close();
	}
}