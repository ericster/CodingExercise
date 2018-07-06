
class UserSolution {
	public static int NM = 10000; 
	public static int NSize = 1000;
	public static char[][] Note;

	class Layer {
		int [] layer;
		int top;
		public Layer () {
			layer = new int[NM];
			top = -1 ;
		}

		public void push(int x) {
			layer[++top] = x;
		}

		/*
		1 2 4 3 5 6
			^
		1 2   3 5 6 4
		*/
		// move, modify, select 
		public void get(int x){
			for (int i=0;i<=top;i++){
				if (layer[i] == x) {
					for (int j=i;j<top;j++){
						layer[i] = layer[i+1];
					}
					layer[top] = x;
					break;
				}
			}
		}

	}


	class Memo {
		
		int id;
		int y;
		int x;
		int h;
		int w;
		char[] txt;
		
		Memo(int id, int y, int x, int h, int w, char[] txt) {
			this.id = id;
			this.y = y;
			this.x = x;
			this.h = h;
			this.w = w;
			this.txt = txt;
		}
		
		public int getId() {
			return this.id;
		}

		public int[] getCoord() {
			return new int[] {this.y, this.x};
		}
		public void moveCoord(int x, int y){
			this.x = x;
			this.y = y;
		}

		public void modify(int height, int width, char[] txt){
			this.h = height;
			this.w = width;
			this.txt = txt;
		}


		/*

			 0 1 2 3 4 5
				 2 3 4 5 6 
				         6 7 9 ...
		*/
		public boolean isInScreen(int _y, int _x){
			if ((_x + 4 < x) || (_x > x + w) || (_y + 4 < y) || (_y > y + h)) return false;
			return true;
		}
		
	}

	Layer layers;
	Memo[] memos; 
	public void init(int mScreenSize)
	{
		// # of memos
		memos = new Memo[NM];
		Note = new char[NSize][NSize];
		layers = new Layer();
		
	}

	public void create_memo(int mId, int mY, int mX, int mHeight, int mWidth, char str[])
	{

		// #1, #2, #3
		memos[mId] = new Memo(mId, mY, mX, mHeight, mWidth, str);
		layers.push(mId);

	}

	public AXIS select_memo(int mId)
	{
		AXIS ret = new AXIS();
		int[] coord = new int[2];
		coord = memos[mId].getCoord();
		ret.y = coord[0];
		ret.x = coord[1];
		layers.get(mId);
		
		return ret;
	}
	
	public void move_memo(int mId, int mY, int mX)
	{
		memos[mId].moveCoord(mY, mX);
		layers.get(mId);
	}
	
	public void modify_memo(int mId, int mHeight, int mWidth, char str[])
	{
		memos[mId].modify(mHeight, mWidth, str);
		layers.get(mId);
	}
	
	public void get_screen_context(int mY, int mX, char res[][])
	{
		int cnt = layers.top;
		System.out.println("layer no " + cnt);


	}

}