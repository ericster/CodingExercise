import java.util.Arrays;

import java.util.Arrays;

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

		// move, modify, select 
		public void get(int x){
			int idx=-1;
			for (int i=0;i<=top;i++){
				if (layer[i] == x) {
					idx = i;
				}
			}

			for (int i=idx;i<top;i++){
				layer[i] = layer[i+1];
			}
			layer[top] = x;
		}

		public void display(){
			int[]  tmp = new int[top+1];
			for (int i=0;i<=top;i++){
				tmp[i] = layer[i];
			}

			System.out.println("layers " + Arrays.toString(tmp));
 
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
			int cnt=0;
			for (int i=0;i<txt.length;i++){
				if (txt[i] < 'a' || txt[i] > 'z') break;
				cnt++;
			}
			int size = (h*w > cnt) ? cnt : h*w;
			this.txt = new char[h*w];
			for (int i=0;i<h*w;i++){
				if (i < size ){
					this.txt[i] = txt[i];
				}
				else{
					this.txt[i] = '\0';	
				}
			}

			//System.out.println("Memo " + "length " + cnt + " "  + Arrays.toString(this.txt));
		}
		
		public int getId() {
			return this.id;
		}

		public int[] getCoord() {
			return new int[] {this.y, this.x};
		}
		public void moveCoord(int y, int x){
			this.x = x;
			this.y = y;
		}

		public void modify(int height, int width, char[] txt){
			this.h = height;
			this.w = width;
			//this.txt = txt;
			int cnt=0;
			for (int i=0;i<txt.length;i++){
				if (txt[i] < 'a' || txt[i] > 'z') break;
				cnt++;
			}
			int size = (h*w > cnt) ? cnt : h*w;
			this.txt = new char[h*w];
			for (int i=0;i<h*w;i++){
				if (i < size ){
					this.txt[i] = txt[i];
				}
				else{
					this.txt[i] = '\0';	
				}
			}

			//System.out.println("Modified Memo " + "length " + cnt + " "  + Arrays.toString(this.txt));
		}


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
		//System.out.println("create_memo");
		//System.out.println("layers " + mId + " " + " mY " + mY + " mX " + mX + " h " + mHeight + " w " + mWidth);
		memos[mId] = new Memo(mId, mY, mX, mHeight, mWidth, str);
		layers.push(mId);

	}

	public AXIS select_memo(int mId)
	{
		//System.out.println("select_memo " + mId);
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
		//System.out.println("move_memo");
		memos[mId].moveCoord(mY, mX);
		//System.out.println("layers " + mId + " " + " mY " + mY + " mX " + mX );
		//layers.display();
		layers.get(mId);
		//layers.display();
	}
	
	public void modify_memo(int mId, int mHeight, int mWidth, char str[])
	{
		//System.out.println("modify_memo " + mId);
		memos[mId].modify(mHeight, mWidth, str);
		layers.get(mId);
	}
	
	public void get_screen_context(int mY, int mX, char res[][])
	{
		//res = new char[5][5];
		//System.out.println("get_screen_context");
		//System.out.println("select window " + mY + " " + mX );
		int cnt = layers.top;
		//System.out.println("# of layers " + cnt);
		for (int i=0;i<=cnt;i++){
			if (memos[layers.layer[i]].isInScreen(mY, mX)){
				//System.out.println("included layer " + layers.layer[i]); 
				int xx = memos[layers.layer[i]].x;
				int yy = memos[layers.layer[i]].y;
				int ww = memos[layers.layer[i]].w;
				int hh = memos[layers.layer[i]].h;
				char [] tmp = memos[layers.layer[i]].txt;
				////System.out.println("tmp yy " + yy + " xx " + xx + " hh " + hh + " ww " + ww + " " + Arrays.toString(tmp));
				char [][] note = new char [1000][1000];
				for (int k=0;k<1000;k++){
					for (int m=0;m<1000;m++){
						note[k][m] =  (k-yy >= 0 && m-xx >=0 && (k-yy<hh) && (m-xx < ww)) ? tmp[(k-yy)*ww + (m-xx)] : '\0';
					}
				}
				for (int k=0;k<5;k++){
					for (int m=0;m<5;m++) {
						// overwrite with the new value
						//res[k][m] = (k+mY < 1000 && m+mX < 1000 &&  note[k+mY][m+mX] != '\0') ? note[k+mY][m+mX] : res[k][m] ;
						res[k][m] = (k+mY >= yy && k+mY < yy+hh && m+mX >= xx && m+mX < xx + ww ) ? note[k+mY][m+mX] : res[k][m] ;
					}
				}
				for (int p=0;p<5;p++){
					//System.out.println("res ## " + Arrays.toString(res[p]));
				}
			}
		}

		for (int i=0;i<5;i++){
			//System.out.println("res " + Arrays.toString(res[i]));
		}
	}

}
