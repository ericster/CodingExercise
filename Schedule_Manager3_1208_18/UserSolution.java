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
			while(tb[h].ename != null ) {
				if (tb[h].ename.equals(key)) {
					return false;
				}
				h = (h + 1) % capacity;
			}
			tb[h].ename = key;
			int nouid = tb[h].no_uid;
			if (nouid < 1) {
				tb[h].master_uid = data;
				tb[h].uid[nouid] = data;
				tb[h].no_uid++;
			}
			else {
				tb[h].uid[nouid] = data; 
				tb[h].no_uid++;
			}
			
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
								for (int j =i; j < tb[h].no_uid-1; j++) {
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

	void addEvent(int uid, char ename[], int groupid)
	{
		// table
		Hashtable g_table = tb[groupid];
		g_table.put(ename.toString(), uid);
		
		
		// uid list array ***
		Node head = uid_arr[uid];
		head.next = new Node(ename.toString(), groupid, head, head.next);
		if (head.next.next != null) head.next.next.prev = head.next;
		
		// count array
		count[uid]++;
	}

	int deleteEvent(int uid, char ename[])
	{
		
		Node head = uid_arr[uid];
		int del_cnt = 0;
		// table
		while (head.next != null) {
			head = head.next;
			int gid = head.gid;
			Hashtable g_table = tb[gid];
			int[] res =  g_table.remove(ename.toString(), uid); 
			if (res != null) {
				for (int i = 0; i < res.length; i++) {
					if (res[i] != 0) {
						count[res[i]]--;
						del_cnt--;
					}
				}
			}
		}
		return del_cnt;
	}

	int changeEvent(int uid, char ename[], char cname[])
	{
		Node head = uid_arr[uid];
		int chn_cnt = 0;
		// table
		while (head.next != null) {
			head = head.next;
			int gid = head.gid;
			Hashtable g_table = tb[gid];
			int[] res = g_table.remove(ename.toString(), uid);
			if (res != null) {
				for (int i = 0; i < res.length; i++) {
					if (res[i] != 0) {
						int uuid = res[i];
						addEvent(uuid, ename, gid);
						count[res[i]]--;
						chn_cnt--;
					}
				}
			}
		}
		return chn_cnt; 
	}

	int getCount(int uid)
	{
		return count[uid];
	}
}
