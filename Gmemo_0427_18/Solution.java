import java.util.Scanner;

class Solution {

	static final int CREATE = 100;
	static final int SELECT = 200;
	static final int MOVE = 300;
	static final int MODIFY = 400;
	static final int GETRES = 999;	
 
	private static int status[] = new int[5];
	private static int commandList[] = new int[100005];
	private static int listsize = 0;
	private static int newCommand[] = { CREATE, SELECT, MOVE, MODIFY };
	
	private static int seed;
	private static char input_str[] = new char[2555];
	private static char user_ans_str[][] = new char[5][5];
	
	private static int totalScore, curScore;

	private static Scanner sc;
	private static UserSolution user = new UserSolution();

	public static void main(String[] args) throws Exception {
		System.setIn(new java.io.FileInputStream("sample_input.txt"));
		sc = new Scanner(System.in);

		int T = sc.nextInt();
		totalScore = 0;

		for (int test_case = 1; test_case <= T; test_case++) {
			curScore = 0;
			if(run())
				curScore = 100;

			System.out.println("#" + test_case + " " + curScore);
			totalScore += curScore;
		}

		System.out.println("Total Score = " + totalScore/T);
		sc.close();
	}
	
	private static boolean run() {
		int accepted = 0, id_size = 0, input_cnt = 0, ans_cnt = 0, H, W, Y, X;
		int screen_size, memo_size, correct_ans, user_ans;
		AXIS cur_axis = new AXIS();
		
		seed = sc.nextInt();
		screen_size = sc.nextInt();
		memo_size = sc.nextInt();
		id_size = sc.nextInt();

		for (int i = 0; i < 4; i++)
		{
			status[i] = sc.nextInt();
			input_cnt += status[i]*2;
			ans_cnt += status[i];
		}
		user.init(screen_size);
		
		for (int mid = 0; mid < id_size; mid++)
		{
			H = pseudo_rand() % memo_size + 1;
			W = pseudo_rand() % memo_size + 1;
			cur_axis.y = Y = pseudo_rand() % (screen_size - H + 1);
			cur_axis.x = X = pseudo_rand() % (screen_size - W + 1);
			make_input_string(input_str, H*W);

			user.create_memo(mid, Y, X, H, W, input_str);
		}

		makeComList();
		
		for (int i = 0; i < input_cnt; i++)
		{
			int comm = commandList[i], target_id;

			switch (comm)
			{
			case CREATE:
				H = pseudo_rand() % memo_size + 1;
				W = pseudo_rand() % memo_size + 1;
				cur_axis.y = Y = pseudo_rand() % (screen_size - H + 1);
				cur_axis.x = X = pseudo_rand() % (screen_size - W + 1);
				make_input_string(input_str, H*W);

				user.create_memo(id_size, Y, X, H, W, input_str);
				id_size++;
				break;
			case SELECT:
				target_id = pseudo_rand() % id_size;

				cur_axis = user.select_memo(target_id);
				break;
			case MOVE:
				target_id = pseudo_rand() % id_size;
				cur_axis.y = Y = pseudo_rand() % (screen_size - memo_size + 1);
				cur_axis.x = X = pseudo_rand() % (screen_size - memo_size + 1);

				user.move_memo(target_id, Y, X);
				break;
			case MODIFY:
				target_id = pseudo_rand() % id_size;
				H = pseudo_rand() % memo_size + 1;
				W = pseudo_rand() % memo_size + 1;
				make_input_string(input_str, memo_size * memo_size);

				user.modify_memo(target_id, H, W, input_str);
				break;
			case GETRES:
				Y = cur_axis.y + pseudo_rand() % 11 - 5;
				X = cur_axis.x + pseudo_rand() % 11 - 5;

				if (Y < 0)
					Y = 0;
				if (Y + 5 >= screen_size)
					Y = screen_size - 5;

				if (X < 0)
					X = 0;
				if (X + 5 >= screen_size)
					X = screen_size - 5;

				user.get_screen_context(Y, X, user_ans_str);
				user_ans = make_hash(user_ans_str);

				correct_ans = sc.nextInt();

				if (correct_ans == user_ans)
					accepted++;

				break;
			default:
				break;
			}
		}
		return (accepted == ans_cnt);
	}

	private static int pseudo_rand()
	{
		seed = seed * 431345 + 2531999;
		return seed & 0x7FFFFFFF;
	}
	
	private static void makeComList()
	{
		int tot = 0, listsize = 0;
		for (int i = 0; i < 4; i++)
		{
			tot += status[i];
		}
		
		for (int i = tot; i > 0; i--)
		{
			int val = pseudo_rand() % i;
			int pos = 0;
			val -= status[pos];
			while (val > 0 || status[pos] == 0)
			{
				pos++;
				val -= status[pos];
				if (pos >= 4)
					break;
			}

			switch (pos)
			{
			case 0:
				commandList[listsize++] = CREATE;
				commandList[listsize++] = GETRES;
				break;
			case 1:
				commandList[listsize++] = SELECT;
				commandList[listsize++] = GETRES;
				break;
			case 2:
				commandList[listsize++] = MOVE;
				commandList[listsize++] = GETRES;
				break;
			case 3:
				commandList[listsize++] = MODIFY;
				commandList[listsize++] = GETRES;
				break;
			default:
				break;
			}
			status[pos]--;
		}
	}
	
	private static void make_input_string(char[] a, int max_len)
	{
		int len = pseudo_rand() % max_len + 10;
		for (int i = 0; i < len; i++)
			a[i] = (char)(pseudo_rand() % 26 + 'a');

		a[len] = '\0';
	}
	
	private static int make_hash(char a[][])
	{
		int ret = 9875;

		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 5; x++)
			{
				if (a[y][x] == '\0')
					continue;

				ret = (ret*a[y][x]) % 32853;
			}
		}

		return ret;		
	}

}

class AXIS
{
	public int x;
	public int y;
}
