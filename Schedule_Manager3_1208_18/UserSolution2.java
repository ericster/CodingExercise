import java.util.HashMap;

class UserSolution2 {	

	class Hash {
		String ename;
		ENode data;
	}

	class Hash2 {
		String ename;
		int master_uid;
		int no_uid;
		// ***
		int[] uid = new int[5];
	}

	class Node{
		String ename;
		int gid;
		int uid;
		Node prev, next;
		Node adr;
		public Node() {
			
		}
		public Node(String e, int g, int u, Node p, Node n) {
			ename = e;
			gid = g;
			uid = u;
			prev = p;
			next = n;
		}
	}
	
	class ENode {
		String ename;
		int gid;
		int master_uid;
		int no_uid;
		// size 5
		Node uid_head;
		public ENode(String en, int g, int muid, int nuid, Node head) {
			ename = en;
			gid = g;
			master_uid = muid;
			no_uid = nuid;
			uid_head = head;
		}
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
		
		boolean put(String key, ENode data) {
			int h = hash(key);
			while(tb[h].ename != null ) {
				if (tb[h].ename.equals(key)) {
					// overwrite
					tb[h].data = data;
					return false;
				}
				h = (h + 1) % capacity;
			}
			tb[h].ename = key;
			tb[h].data = data;
			return true;
		}
		
		ENode get(String key) {
			int h = hash(key);
			int cnt = capacity;
			while (tb[h].ename != null && (--cnt) != 0 ) {
				if (tb[h].ename.equals(key)) {
					return tb[h].data;
				}
				h = (h + 1) % capacity;
			}
			return null;
		}

		boolean remove(String key) {
			int h = hash(key);
			int cnt = capacity;
			while (tb[h].ename != null && (--cnt) != 0) {
				if (tb[h].ename.equals(key)) {
					tb[h].ename = null;
					tb[h].data = null;
					return true;
				}
				h = (h + 1) % capacity;
			}
			return false;
		}
		
		
	}

	final static boolean LOG =  false;
	final static int MAX_GROUP =  100;
	final static int MAX_USER =  1000;
	HashMap[] tb;
	Node[] uid_arr;
	int[] count;

	void init()
	{
		if(LOG) System.out.println("Solution3 The beginning");
		// 1. Hashtable for group
		tb = new  HashMap[MAX_GROUP];
		int capacity = 200;
		for (int i = 0; i < MAX_GROUP; i++ ) {
			tb[i] = new HashMap<String, ENode>();
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
		HashMap<String, ENode> g_table = tb[groupid];
		//if(LOG) System.out.println(" ename converted " + getStr(ename));
		if(!LOG && uid == 499 ) System.out.println("***uid " + uid + " ename " + getStr(ename) + " gid " + groupid);
		
		String en = getStr(ename);

		// uid list array ***
		Node head = uid_arr[uid];
		push(head, en, groupid, uid);
		if(!LOG && uid == 499 ) {
			System.out.println("***uid " + uid + "  " + head.next.ename);
		}

		ENode enode;
		Node unode;

		// non-master
		if (g_table.get(en) != null) {
			if(LOG) System.out.println("non-master ");
			enode = g_table.get(en);
			if (enode.master_uid != uid) {
				if(!LOG && uid == 499) System.out.println("##### addEvent non-master added " ) ;
				enode.no_uid++;
				// push
				unode = new Node(en, groupid, uid, enode.uid_head, enode.uid_head.next);
				enode.uid_head.next = unode;
				if (unode.next != null) unode.next.prev = unode;
				// *** ref
				unode.adr = head.next;
				if(LOG) System.out.println("non-master added " + head.next.uid);
				g_table.put(en, enode);
			}
		}
		// master
		else {
			if(LOG) System.out.println("master ");
			if(!LOG && uid == 499) System.out.println("##### addEvent master added " ) ;
			enode = new ENode(en, groupid, uid, 1, new Node()); 
			// push
			unode = new Node(en, groupid, uid, enode.uid_head, enode.uid_head.next);
			enode.uid_head.next = unode;
			//enode.no_uid++;
			// *** ref
			unode.adr = head.next;
			if(!LOG && uid == 499) {
				System.out.println("##### addEvent master added " + head.next.uid + " " + head.next.ename 
						+ " " + enode.master_uid) ;
				System.out.println("##### addEvent master added " + uid + " " + head.ename 
						+ " " + enode.master_uid) ;
			}
			if(LOG) System.out.println(" master added " + head.next.uid);
			g_table.put(en, enode);
		}

		if(LOG) System.out.println(" g_table " +  en + " " + groupid + " " );
		
		// count array
		count[uid]++;
	}

	int deleteEvent(int uid, char ename[])
	{
		String en = getStr(ename);
		if(!LOG && uid == 499) System.out.println("##### DeleteEvent " + uid + " " + en );
		// uid list array ***
		Node uhead = uid_arr[uid];
		int del_cnt = 0;
		// table
		while (uhead.next != null) {
			uhead = uhead.next;
			if(!LOG && uid == 499) System.out.println("uhead name: " + uhead.ename + " gid: " + uhead.gid);
			if (uhead.ename.equals(en)) {
				int gid = uhead.gid;
				HashMap<String, ENode> g_table = tb[gid];
				// g_table update
				if (g_table.get(en) != null) {
					// non-master
					ENode enode = g_table.get(en);
					if (enode.master_uid != uid) {
						if(!LOG && uid == 499) System.out.println(" master uid: " + enode.master_uid + " " + enode.ename);
						if(LOG) System.out.println(" non-master uid: " );
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							head_tmp = head_tmp.next;
							if(LOG) System.out.println(" head_tmp: " + head_tmp.uid );
							if (head_tmp.uid == uid) {
								del_node(head_tmp);
								del_node(head_tmp.adr);
								count[uid]--;
								del_cnt++;
								enode.no_uid--;
							}
						}
						g_table.put(en, enode);
					}
					else {
						// master
						if(!LOG && enode.master_uid == 499) {
							System.out.println(" master uid: " + enode.ename );
						}
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							head_tmp = head_tmp.next;
							// ** other uids 
							del_node(head_tmp.adr);
							count[head_tmp.uid]--;
							del_cnt++;
						}
						g_table.remove(en);
					}
					// remove from uid list
					del_node(uhead);
				}
				
			}
		}
		if(LOG) System.out.println("Ans ##%% deleteEvent " + uid + " " + en + " cnt: " + del_cnt);
		return del_cnt;
	}

	int changeEvent(int uid, char ename[], char cname[])
	{
		String en = getStr(ename);
		String cn = getStr(cname);
		if(!LOG && uid == 499) System.out.println("##### ChangeEvent " + uid + " " + en + " to " + cn);
		Node uhead = uid_arr[uid];
		int chn_cnt = 0;
		// table
		while (uhead.next != null) {
			uhead = uhead.next;
			if (uhead.ename.equals(en)) {
				int gid = uhead.gid;
				HashMap<String, ENode> g_table = tb[gid];
				// g_table update
				if (g_table.get(en) != null) {
					// non-master
					ENode enode = g_table.get(en);
					if (enode.master_uid != uid) {
						if(!LOG && uid == 499) System.out.println("##### ChangeEvent non-master ") ;
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							head_tmp = head_tmp.next;
							if (head_tmp.uid == uid) {
								del_node(head_tmp);
								del_node(head_tmp.adr);
								count[uid]--;
								chn_cnt++;
								if(!LOG && uid == 499) System.out.println("##### ChangeEvent non-master added " + getStr(cname) + " gid " + gid) ;
								// ???????
								del_node(uhead);
								addEvent(uid, cname, gid);
								g_table.put(en, enode);
							}
						}
					}
					else {
						if(!LOG && uid == 499) System.out.println("##### ChangeEvent master ") ;
						// master
						Node head_tmp = enode.uid_head;
						// add master uid first
						addEvent(uid, cname, gid);
						del_node(uhead);
						while (head_tmp.next != null) {
							head_tmp = head_tmp.next;
							// ** other uids 
							if (head_tmp.uid != uid) {
								addEvent(head_tmp.uid, cname, gid);
							}
							if(!LOG && uid == 499) System.out.println("##### ChangeEvent master del_node " + head_tmp.ename) ;
							del_node(head_tmp.adr);
							count[head_tmp.uid]--;
							chn_cnt++;
						}
						g_table.remove(en);
					}
					// remove from uid list
					//uhead = uid_arr[uid];
					//if(!LOG && uid == 499) System.out.println("##### ChangeEvent remove head " + uhead.next.ename) ;
				}
				
			}
		}
		if(!LOG && uid == 499 ) {
			System.out.println("***uid ChangeEvent" + uid + "  " + uid_arr[uid].next.ename);
		}

		if(LOG) System.out.println("Ans ##%% changeEvent " + uid + " " + en + " " + cn + " cnt: " + chn_cnt);
		return chn_cnt; 
	}

	int getCount(int uid)
	{
		if(LOG) System.out.println("Ans ##%% getCount " + uid + " # " + count[uid]);
		return count[uid];
	}

	void push(Node head, String ename, int gid, int uid) {
		head.next = new Node(ename, gid, uid, head, head.next);
		if(head.next.next != null) head.next.next.prev = head.next;
	}
	void del_node(Node node) {
		node.prev.next = node.next;
		if (node.next != null) node.next.prev = node.prev;
	}
}
