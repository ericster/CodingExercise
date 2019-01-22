import java.util.Scanner;

class Solution {	
	private static Scanner sc;
	private static UserSolution2 usersolution = new UserSolution2();
	
	private final static boolean LOG		= true;
	private final static int INIT			= 0;
	private final static int ADD			= 1;
	private final static int DELETE			= 2;
	private final static int CHANGE			= 3;
	private final static int GETCOUNT		= 4;
	
	private static char str1[] = new char[15];
	private static char str2[] = new char[15];
	private static int mSeed;

	private static int pseudo_rand()
	{
		mSeed = mSeed * 431345 + 2531999;
		return mSeed & 0x7FFFFFFF;
	}
	
	private static void make_string(char str[], int seed)
	{
		mSeed = seed;
		int length = 5 + pseudo_rand() % 10;
		for (int i = 0; i < length; ++i) {
			str[i] = (char)('a' + pseudo_rand() % 26);
		}
		str[length] = 0;
	}

	private static int run()
	{
		int cmd, p1, p2, p3, p4;
		int N, ret, flag;

		cmd = sc.nextInt();
		N = sc.nextInt();
		flag = sc.nextInt();
		
		usersolution.init();

		int answer = 100;
		for (int i = 1; i < N; ++i) {
			cmd = sc.nextInt();

			switch (cmd) {
			case ADD:
				p2 = 0;
				if (flag == 1){
					p1 = sc.nextInt();
					String str = sc.next();
					for (int k = 0; k < str.length(); ++k) {
						str1[k] = str.charAt(k);
					}
					str1[str.length()] = 0;
					p3 = sc.nextInt();
				}
				else {
					p1 = sc.nextInt();
					p2 = sc.nextInt();
					p3 = sc.nextInt();
					make_string(str1, p2);
				}
				if (LOG && (p1 == 499 || p1 == 434 || p2 == 4513)) {
					System.out.println("	command: " + i + " Add " + p1 + " " + usersolution.getStr(str1)+ " " + p3 );
				}
				usersolution.addEvent(p1, str1, p3);
				break;

			case DELETE:
				p2 = 0;
				if (flag == 1){
					p1 = sc.nextInt();
					String str = sc.next();
					for (int k = 0; k < str.length(); ++k) {
						str1[k] = str.charAt(k);
					}
					str1[str.length()] = 0;
					p3 = sc.nextInt();
				}
				else {
					p1 = sc.nextInt();
					p2 = sc.nextInt();
					p3 = sc.nextInt();
					make_string(str1, p2);
				}
				if (LOG && ( (p1 == 499) || (p1 == 434) )) {
					System.out.println("	command: " + i + " Delete " + p1 + " " + usersolution.getStr(str1) );
				}
				ret = usersolution.deleteEvent(p1, str1);
				if (ret != p3) {
					System.out.println("command: " + i + " Delete " + p1 + " : p2 " + p2 + " " + usersolution.getStr(str1) + " ret: " + ret + " expected: " + p3);
					answer = 0;
				}
				break;

			case CHANGE:
				p2 = 0;
				p3 = 0;
				if (flag == 1){
					p1 = sc.nextInt();
					String str = sc.next();
					for (int k = 0; k < str.length(); ++k) {
						str1[k] = str.charAt(k);
					}
					str1[str.length()] = 0;
					str = sc.next();
					for (int k = 0; k < str.length(); ++k) {
						str2[k] = str.charAt(k);
					}
					str2[str.length()] = 0;
					p4 = sc.nextInt();
				}
				else {
					p1 = sc.nextInt();
					p2 = sc.nextInt();
					p3 = sc.nextInt();
					p4 = sc.nextInt();
					make_string(str1, p2);
					make_string(str2, p3);
				}
				ret = usersolution.changeEvent(p1, str1, str2);
				if (p1 == 499 || p1 == 434 || p2 == 4513 || p3 == 4513) {
					System.out.println("	command: " + i + " Change " + p1 + " " + usersolution.getStr(str1)+ " " + usersolution.getStr(str2) );
				}
				if (ret != p4) {
					System.out.println("command: " + i + " Change ret: " + ret + " expected: " + p4);
					answer = 0;
				}
				break;

			case GETCOUNT:
				p1 = sc.nextInt();
				p2 = sc.nextInt();
				ret = usersolution.getCount(p1);
				if (ret != p2) {
					System.out.println("command: " + i + " uid: " + p1 + " getCount ret: " + ret + " expected: " + p2);
					answer = 0;
				}
				break;

			default:
				break;
			}
		}

		return answer;
	}
	
	public static void main(String[] args) throws Exception {
	
		System.setIn(new java.io.FileInputStream("sample_input2.txt"));
		sc = new Scanner(System.in);
		long startTime = System.currentTimeMillis();

		int TC = sc.nextInt();
		for (int testcase = 1; testcase <= TC; ++testcase) {
            System.out.println("#" + testcase + " " + run());
		}
		long stopTime = System.currentTimeMillis();
		System.out.println((stopTime - startTime));

		sc.close();
	}
}
