class Memo {
	
	int NSize = 1000;
	int id;
	int x;
	int y;
	int row;
	int col;
	char[] txt;
	char[][] table = new char[NSize][NSize]; 
	
	Memo(int id, int y, int x, int row, int col, char[] txt) {
		this.id = id;
		this.y = y;
		this.x = x;
		this.row = row;
		this.col = col;
		this.txt = txt;
	}
	
	public int getId() {
		return this.id;
	}

	public int[] getCoord() {
		return new int[] {this.y, this.x};
	}
	
}


class UserSolution {
	public static int NM = 10000; 
	public static int NSize = 1000;
	public static Memo[] memos; 
	public static char[][] Note;

	public void init(int mScreenSize)
	{
		// # of memos
		memos = new Memo[NM];
		Note = new char[NSize][NSize];
			
	}

	public void create_memo(int mId, int mY, int mX, int mHeight, int mWidth, char str[])
	{

		// #1, #2, #3
		memos[mId] = new Memo(mId, mY, mX, mHeight, mWidth, str);

	}

	public AXIS select_memo(int mId)
	{
		AXIS ret = new AXIS();
		int[] coord = new int[2];
		coord = memos[mId].getCoord();
		ret.y = coord[0];
		ret.x = coord[1];
		return ret;
	}
	
	public void move_memo(int mId, int mY, int mX)
	{

	}
	
	public void modify_memo(int mId, int mHeight, int mWidth, char str[])
	{

	}
	
	public void get_screen_context(int mY, int mX, char res[][])
	{

	}
}
