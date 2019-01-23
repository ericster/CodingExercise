import java.util.Arrays;

class UserSolution {	

	class HashNode {
		String ename;
		ENode data;
		HashNode next;
		public HashNode() {
			
		}
		public HashNode (String en, ENode dt, HashNode n) {
			ename = en;
			data = dt;
			next = n;
		}
	}

	class Hash2 {
		String ename;
		ENode data;
	}

	class Hash1 {
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
		HashNode tb[];
		
		public Hashtable(int cap) {
			capacity = cap;
			tb = new HashNode[capacity];
			/*
			for (int i = 0; i < capacity; i++) {
				tb[i] = new HashNode();
			}
			*/
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
		
		boolean put2(String key, ENode data) {
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
		
		ENode get2(String key) {
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

		boolean remove2(String key) {
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
		boolean put(String key, ENode data) {
			int h = hash(key);
			if (tb[h] == null) {
				tb[h]  = new HashNode(key, data, null);
			}
			else {
				HashNode cur = tb[h];
				while (cur.next != null && !cur.ename.equals(key))
					cur = cur.next;
				if (cur.ename.equals(key)) {
					cur.data = data;
					return false;
				}
				else
					cur.next = new HashNode(key, data, null);
			}
			return true;
		}
		
		ENode get(String key) {
			int h = hash(key);
			if (tb[h] == null)
				return null;
			else {
				HashNode cur = tb[h];
				while (cur != null  && !cur.ename.equals(key))
					cur = cur.next;
				if (cur == null)
					return null;
				else
					return cur.data;
			}
		}

		boolean remove(String key) {
			int h = hash(key);
			if (tb[h] != null) {
				HashNode prev = null;
				HashNode cur = tb[h];
				while (cur != null && !cur.ename.equals(key)) {
					prev = cur;
					cur = cur.next;
				}
				if (cur.ename.equals(key)) {
					if (prev == null)
						tb[h] = cur.next;
					else
						prev.next = cur.next;
					return true;
				}
			}
			return false;
		}
		
		
	}

	final static boolean LOG =  false;
	final static int MAX_GROUP =  100;
	final static int MAX_USER =  1000;
	Hashtable[] htable;
	Node[] uid_arr;
	int[] count;

	void init()
	{
		if(LOG) System.out.println("The beginning");
		// 1. Hashtable for group
		htable = new  Hashtable[MAX_GROUP];
		int capacity = 200;
		for (int i = 0; i < MAX_GROUP; i++ ) {
			htable[i] = new Hashtable(capacity*2);
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
		Hashtable g_table = htable[groupid];
		//if(LOG) System.out.println(" ename converted " + getStr(ename));
		if(LOG) System.out.println("***uid " + uid + " ename " + getStr(ename) + " gid " + groupid);
		
		String en = getStr(ename);

		// uid list array ***
		Node head = uid_arr[uid];
		push(head, en, groupid, uid);

		ENode enode;
		Node unode;
		// non-master
		if (g_table.get(en) != null) {
			if(LOG) System.out.println("non-master ");
			enode = g_table.get(en);
			if (enode.master_uid != uid) {
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
			enode = new ENode(en, groupid, uid, 1, new Node()); 
			// push
			unode = new Node(en, groupid, uid, enode.uid_head, enode.uid_head.next);
			enode.uid_head.next = unode;
			//enode.no_uid++;
			// *** ref
			unode.adr = head.next;
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
		if(LOG) System.out.println("##### DeleteEvent " + uid + " " + en );
		// uid list array ***
		Node uhead = uid_arr[uid];
		int del_cnt = 0;
		// table
		while (uhead.next != null) {
			uhead = uhead.next;
			if(LOG) System.out.println("uhead name: " + uhead.ename + " gid: " + uhead.gid);
			if (uhead.ename.equals(en)) {
				int gid = uhead.gid;
				Hashtable g_table = htable[gid];
				// g_table update
				if (g_table.get(en) != null) {
					// non-master
					ENode enode = g_table.get(en);
					if(LOG) System.out.println(" master uid: " + enode.master_uid);
					if (enode.master_uid != uid) {
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
						if(LOG) System.out.println(" master uid: " );
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
		if(LOG) System.out.println("##### ChangeEvent " + uid + " " + en + " to " + cn);
		Node uhead = uid_arr[uid];
		int chn_cnt = 0;
		// table
		while (uhead.next != null) {
			uhead = uhead.next;
			if (uhead.ename.equals(en)) {
				int gid = uhead.gid;
				Hashtable g_table = htable[gid];
				// g_table update
				if (g_table.get(en) != null) {
					// non-maaster
					ENode enode = g_table.get(en);
					if (enode.master_uid != uid) {
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							head_tmp = head_tmp.next;
							if (head_tmp.uid == uid) {
								del_node(head_tmp);
								del_node(head_tmp.adr);
								count[uid]--;
								chn_cnt++;
								// TODO: why????
								del_node(uhead);
								addEvent(uid, cname, gid);
								g_table.put(en, enode);
							}
						}
					}
					else {
						// master
						Node head_tmp = enode.uid_head;
						// add master uid first
						addEvent(uid, cname, gid);
						// TODO: why????
						del_node(uhead);
						while (head_tmp.next != null) {
							head_tmp = head_tmp.next;
							// ** other uids 
							if (head_tmp.uid != uid) {
								addEvent(head_tmp.uid, cname, gid);
							}
							del_node(head_tmp.adr);
							count[head_tmp.uid]--;
							chn_cnt++;
						}
						g_table.remove(en);
					}
					// remove from uid list
				}
				
			}
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
