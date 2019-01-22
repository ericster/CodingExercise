import java.util.Arrays;

class UserSolution {
	
	class Node {
		int mId;
		int mType;
		int mY, mX;
		Node prev, next;
		Node adr;
		public Node () {
			
		}
		public Node (int id, int type, int y, int x, Node p, Node n) {
			mId = id;
			mType = type;
			mY = y;
			mX = x;
			prev = p;
			next = n;
		}
	}
	
	boolean withinRadius (int mY, int mX, int mR, int y, int x) {
		boolean res = false;
		int sq = (mY - y) * (mY - y) + (mX - x) * (mX - x);
		if (sq <= mR * mR ) return true;
		return res;
	}
	int distance (int mY, int mX, int y, int x) {
		int res = 0;
		res = (mY - y) * (mY - y) + (mX - x) * (mX - x);
		return res;
	}

	final static int MAX_ID =  50000 ;
	final static int MAX_TYPE =  10;

	Node[] types;
	Node[] ids;

	void init_(int n) {

	}
	void init(int n) {
		types = new Node[MAX_TYPE+1];
		for (int i = 1; i <= MAX_TYPE; i++) {
			types[i] =  new Node();
		}
		ids = new Node[MAX_ID+1];
		for (int i = 1; i < MAX_ID; i++) {
			ids[i] =  new Node();
		}


	}

	void addFacility_(int mId, int mType, int mY, int mX) {

	}
	int cnt = 0;
	void addFacility(int mId, int mType, int mY, int mX) {
		cnt++;
		//System.out.println("cnt: " + cnt + " added " + mId + " type: " + mType );
		Node typeh = types[mType];
		push(typeh, mId, mType, mY, mX);
		Node idh = ids[mId];
		push(idh, mId, mType, mY, mX);
		idh.next.adr = typeh.next;

	}

	void removeFacility_(int mId) {

	}
	void removeFacility(int mId) {
		//System.out.println(" remove " + mId );
		Node idh = ids[mId]; 
		while (idh.next != null) {
			idh = idh.next;
			del(idh.adr);
		}

		ids[mId] = new Node();

	}

	int search1_(int mType, int mY, int mX, int mRadius) {
		return 0;

	}
	int search1(int mType, int mY, int mX, int mRadius) {
		//System.out.println(" search1  type: " + mType );
		int cnt = 0;
		Node typeh;
		if (mType == 0) {
			for (int i = 1; i <= MAX_TYPE; i++ ) {
				typeh = types[i];

				while (typeh.next != null) {
					typeh = typeh.next;
					if (withinRadius(mY, mX, mRadius, typeh.mY, typeh.mX)) {
						cnt++;
					}
				}
				
			}
			
		}
		else {
			typeh = types[mType];

			while (typeh.next != null) {
				typeh = typeh.next;
				if (withinRadius(mY, mX, mRadius, typeh.mY, typeh.mX)) {
					cnt++;
				}
			}
		}

		return cnt;
	}

	void search2_(int mType, int mY, int mX, int mIdList[]) {

	}
	void search2(int mType, int mY, int mX, int mIdList[]) {
		//System.out.println(" search2  type: " + mType );
		Node typeh = types[mType];
		int len = 0;

		while (typeh.next != null) {
			typeh = typeh.next;
			len++;
		}

		int[] id_arr = new int[len];
		int[] dist_arr = new int[len];
		typeh = types[mType];
		int idx = 0;
		//System.out.println(" search2  type: distance" );
		while (typeh.next != null) {
			typeh = typeh.next;
			id_arr[idx] = typeh.mId;
			dist_arr[idx++] = distance(mY, mX, typeh.mY, typeh.mX);
		}
		

		//System.out.println(" search2  type: before sort" );
		//System.out.println(" search2  type: before sort len " + len );

		quicksort(0, len-1, dist_arr, id_arr);
		System.out.println(" search2  type: ids " + Arrays.toString(id_arr) );
		System.out.println(" search2  type: dist " + Arrays.toString(dist_arr) );
		//System.out.println(" search2  type: after sort" );
		int num = ( len < 5) ? len : 5;
		for (int i = 0; i < num; i++) {
			mIdList[i] = id_arr[i];
		}

	}
	
	void quicksort(int first, int last, int[] arr, int[] id_arr) {
		int pivot, i, j, tmp, tmp2;
		
		if (first < last) {
			pivot = first;
			i = first;
			j = last;
			while (i < j) {
				while( arr[i] <= arr[pivot] && i < last) {
					if (arr[i] == arr[pivot]) {
						while( id_arr[i] <= id_arr[pivot] && i < last) {
							i++;
						}
						i--;
					}
					i++;
				}
				while( arr[j] > arr[pivot] ) {
					j--;
				}
				if (i < j) {
					tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;

					tmp2 = id_arr[i];
					id_arr[i] = id_arr[j];
					id_arr[j] = tmp2;

				}
			}
			tmp = arr[pivot];
			arr[pivot] = arr[j];
			arr[j] = tmp;

			tmp2 = id_arr[pivot];
			id_arr[pivot] = id_arr[j];
			id_arr[j] = tmp2;
			
			quicksort(first, j-1, arr, id_arr);
			quicksort(j+1, last, arr, id_arr);
		}
	}
	
	
	void push(Node head, int id, int type, int y, int x) {
		head.next = new Node(id, type, y, x , head, head.next);
		if(head.next.next != null) head.next.next.prev = head.next;
	}

	void del(Node node) {
		node.prev.next = node.next;
		if (node.next != null) node.next.prev = node.prev;
	}


}