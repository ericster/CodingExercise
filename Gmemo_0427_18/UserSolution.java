
class UserSolution {
	public static int NM = 10000; 
	public static int NSize = 1000;
	public static Memo[] memos; 
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

	}
	
	public void modify_memo(int mId, int mHeight, int mWidth, char str[])
	{

	}
	
	public void get_screen_context(int mY, int mX, char res[][])
	{

	}

	class Node { 
		int id;
		Node next;
		Node prev;
		public Node() {
			id = -1;
			next = null;
			prev = null;
		}
	}

	// layer, doubly linked list
	class Layer_LL {
		Node head;
		Node tail;
		public Layer_LL(){
			head = new Node();
			tail = new Node();
			head.next = tail;
			tail.prev = head;
		}

		// put new node after head
		public void put(int id){ 
			Node newNode = new Node();
			newNode.id = id;
			newNode.prev = head;
			newNode.next = head.next;
			head.next.prev = newNode;
			head.next = newNode;
		}
		public void get(int id){ 
		}

	}
}
