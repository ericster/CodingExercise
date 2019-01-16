import java.util.Arrays;

class UserSolution {	

	class Hash {
		String ename;
		int master_uid;
		int no_uid;
		// ***
		int[] uid = new int[5];
	}
	
	class Hashtable{
		int capacity;
		Hash tb[];
		
		public Hashtable(int cap) {
			capacity = cap;
			tb = new Hash[capacity];
			for (int i = 0; i < capacity; i++) {
				tb[i] = new Hash();
			}
		}
		
		int hash(String str) {
			int hash = 5381;
			for (int i = 0; i < str.length(); i++) {
				int c = (int)str.charAt(i);
				hash = ((hash << 5) + hash) + c;
			}
			if (hash < 0) hash *= -1;
			return hash % capacity;
		}
		
		boolean put(String key, int data) {
			int h = hash(key);
			int nouid = tb[h].no_uid;
			while(tb[h].ename != null ) {
				if (tb[h].ename.equals(key)) {
					nouid = tb[h].no_uid;
					tb[h].uid[nouid] = data; 
					tb[h].no_uid++;
					System.out.println("non-master uid " );
					return false;
				}
				h = (h + 1) % capacity;
			}
			tb[h].ename = key;
			nouid = tb[h].no_uid;
			System.out.println("no_uid " + nouid);
			tb[h].master_uid = data;
			tb[h].uid[nouid] = data;
			tb[h].no_uid++;
			System.out.println("master uid " );
			System.out.println("no_uid " + tb[h].no_uid);
			
			return true;
		}
		
		int[] get(String key) {
			int h = hash(key);
			int cnt = capacity;
			while (tb[h].ename != null && (--cnt) != 0) {
				if (tb[h].ename.equals(key)) {
					return tb[h].uid;
				}
				h = (h + 1) % capacity;
			}
			return null;
		}

		int[] remove(String key, int data) {
			int h = hash(key);
			int cnt = capacity;
			while (tb[h].ename != null && (--cnt) != 0) {
				if (tb[h].ename.equals(key)) {
					if (tb[h].master_uid == data) {
						tb[h].ename = null;
						int[] res = tb[h].uid;
						tb[h].uid = new int[5];
						tb[h].no_uid = 0;
						return res;
					}
					else {
						int[] uid_arr = tb[h].uid;
						for (int i = 1; i < tb[h].no_uid; i++) {
							if (uid_arr[i] == data) {
								for (int j =i; j < tb[h].no_uid; j++) {
									uid_arr[j] = uid_arr[j+1];
								}
								return new int[] {data};
							}
						}
					}
				}
				h = (h + 1) % capacity;
			}
			return null;
		}
		
		
	}
	class Node{
		String ename;
		int gid;
		Node prev, next;
		public Node() {
			
		}
		public Node(String e, int g, Node p, Node n) {
			ename = e;
			gid = g;
			prev = p;
			next = n;
		}
	}

	final static int MAX_GROUP =  100;
	final static int MAX_USER =  1000;
	Hashtable[] tb;
	Node[] uid_arr;
	int[] count;
	

	void init()
	{
		// 1. Hashtable for group
		tb = new  Hashtable[MAX_GROUP];
		int capacity = 200;
		for (int i = 0; i < MAX_GROUP; i++ ) {
			tb[i] = new Hashtable(capacity);
		}
		
		// 2. uid array of list
		uid_arr = new Node[1000];
		for (int i = 0; i < 1000; i++) {
			uid_arr[i] = new Node();
		}
		
		// 3. count array
		count = new int[MAX_USER];
	}

	String getStr(char ename[]) {
		int len = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 15; i++) {
			if (ename[i] != 0) {
				sb.append(ename[i]);
			}
			else {
				break;
			}
		}
		return sb.toString();
	}
	void addEvent(int uid, char ename[], int groupid)
	{
		// table
		Hashtable g_table = tb[groupid];
		//System.out.println(" ename converted " + getStr(ename));
		System.out.println("***uid " + uid + " ename " + getStr(ename) + " gid " + groupid);
		
		String en = getStr(ename);

		g_table.put(en, uid);
		System.out.println(" g_table " +  en + " " + groupid + " " + Arrays.toString(g_table.get(en)) );
		
		
		// uid list array ***
		Node head = uid_arr[uid];
		head.next = new Node(en, groupid, head, head.next);
		if (head.next.next != null) head.next.next.prev = head.next;
		
		// count array
		count[uid]++;
	}

	int deleteEvent(int uid, char ename[])
	{
		
		String en = getStr(ename);
		System.out.println("##### DeleteEvent " + uid + " " + en );
		// uid list array ***
		Node head = uid_arr[uid];
		int del_cnt = 0;
		// table
		while (head.next != null) {
			head = head.next;
			if (head.ename.equals(en)) {
				int gid = head.gid;
				Hashtable g_table = tb[gid];
				int[] res =  g_table.remove(en, uid); 
				System.out.println("##### deleteEvent res " + gid + " " + Arrays.toString(res));
				if (res != null) {
					for (int i = 0; i < res.length; i++) {
						if (res[i] != 0) {
							
							// count array
							count[res[i]]--;
							del_cnt++;
						}
					}
				}
				// uid list array update *** 
				Node tmp = head;
				tmp.prev.next = tmp.next;
				if (tmp.next != null) tmp.next.prev = tmp.prev;
			}

		}
		System.out.println("##%% deleteEvent " + uid + " " + en + " cnt: " + del_cnt);
		return del_cnt;
	}

	int changeEvent(int uid, char ename[], char cname[])
	{
		String en = getStr(ename);
		String cn = getStr(cname);
		System.out.println("##### ChangeEvent " + uid + " " + en + " to " + cn);
		Node head = uid_arr[uid];
		int chn_cnt = 0;
		// table
		while (head.next != null) {
			head = head.next;
			int gid = head.gid;
			Hashtable g_table = tb[gid];
			if (head.ename.equals(en)) {
				int[] res = g_table.remove(en, uid);
				System.out.println("##### changeEvent remove res " + Arrays.toString(res));
				if (res != null) {
					for (int i = 0; i < res.length; i++) {
						if (res[i] != 0) {
							int uuid = res[i];
							addEvent(uuid, cname, gid);
							count[res[i]]--;
							chn_cnt++;
						}
					}
				}
				// uid list array update *** 
				Node tmp = head;
				tmp.prev.next = tmp.next;
				if (tmp.next != null) tmp.next.prev = tmp.prev;
			}
		}
		System.out.println("##%% changeEvent " + uid + " " + en + " " + cn + " cnt: " + chn_cnt);
		return chn_cnt; 
	}

	int getCount(int uid)
	{
		System.out.println("##%% getCount " + uid + " # " + count[uid]);
		return count[uid];
	}
}
