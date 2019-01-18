import java.util.Arrays;

class UserSolution {	

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
		int uid;
		int gid;
		Node prev, next;
		Node adr;
		public Node() {
			
		}
		public Node(String e, int g, Node p, Node n) {
			ename = e;
			gid = g;
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

		// uid list array ***
		Node head = uid_arr[uid];
		head.next = new Node(en, groupid, head, head.next);
		if (head.next.next != null) head.next.next.prev = head.next;

		ENode enode;
		Node unode;
		// non-maaster
		if (g_table.get(en) != null) {
			enode = g_table.get(en);
			if (enode.master_uid != uid) {
				enode.no_uid++;
				unode = new Node(en, uid, enode.uid_head, enode.uid_head.next);
				enode.uid_head.next = unode;
				if (unode.next != null) unode.next.prev = unode;
				// *** ref
				unode.adr = head.next;
			}
		}
		// master
		else {
			enode = new ENode(en, groupid, uid, 1, new Node()); 
			unode = new Node(en, uid, enode.uid_head, enode.uid_head.next);
			enode.uid_head.next = unode;
			// *** ref
			unode.adr = head.next;
		}

		System.out.println(" g_table " +  en + " " + groupid + " " );
		
		// count array
		count[uid]++;
	}

	void del_node(Node node) {
		node.prev.next = node.next;
		if (node.next != null) node.next.prev = node.prev;
	}

	int deleteEvent(int uid, char ename[])
	{
		String en = getStr(ename);
		System.out.println("##### DeleteEvent " + uid + " " + en );
		// uid list array ***
		Node uhead = uid_arr[uid];
		int del_cnt = 0;
		// table
		while (uhead.next != null) {
			uhead = uhead.next;
			if (uhead.ename.equals(en)) {
				int gid = uhead.gid;
				Hashtable g_table = tb[gid];
				// g_table update
				if (g_table.get(en) != null) {
					// non-maaster
					ENode enode = g_table.get(en);
					if (enode.master_uid != uid) {
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							Node htmp = head_tmp.next;
							if (htmp.uid == uid) {
								del_node(htmp);
								del_node(htmp.adr);
								count[uid]--;
								del_cnt++;
							}
						}
						g_table.put(en, enode);
					}
					else {
						// master
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							Node htmp = head_tmp.next;
							// ** other uids 
							del_node(htmp.adr);
							count[htmp.uid]--;
						}
						del_cnt += enode.no_uid;
						g_table.remove(en);
					}
					// remove from uid list
					del_node(uhead);
				}
				
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
		Node uhead = uid_arr[uid];
		int chn_cnt = 0;
		// table
		while (uhead.next != null) {
			uhead = uhead.next;
			if (uhead.ename.equals(en)) {
				int gid = uhead.gid;
				Hashtable g_table = tb[gid];
				// g_table update
				if (g_table.get(en) != null) {
					// non-maaster
					ENode enode = g_table.get(en);
					if (enode.master_uid != uid) {
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							Node htmp = head_tmp.next;
							if (htmp.uid == uid) {
								del_node(htmp);
								del_node(htmp.adr);
								count[uid]--;
								chn_cnt++;
								addEvent(uid, cname, gid);
								g_table.put(en, enode);
							}
						}
					}
					else {
						// master
						Node head_tmp = enode.uid_head;
						while (head_tmp.next != null) {
							Node htmp = head_tmp.next;
							// ** other uids 
							addEvent(htmp.uid, cname, gid);
							del_node(htmp.adr);
							count[htmp.uid]--;
						}
						chn_cnt += enode.no_uid;
						g_table.remove(en);
					}
					// remove from uid list
					del_node(uhead);
				}
				
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
