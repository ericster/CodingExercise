public class UserSolution {
	int S = 1 << 17;
	int MIN_ID = S-2, MAX_ID = S-1;
	//int N, M;
	int[] arr;  
	int[] sumfr;
	int[] cnt;
	Tree[][] tree ;
	
	class Tree {
		int minid, maxid;
		Tree() { 
			minid = MIN_ID;
			maxid = MAX_ID;
		}
		Tree(int id){
			minid = id;
			maxid = id;
		}
	}

	void update(int d, int node) {
		if (DEBUG) System.out.println(" d: " + d + " updated node : " + node);
		if (node < 1) return;


		if (arr[tree[d][node*2].minid] != arr[tree[d][node*2 + 1].minid])  {
			if (DEBUG) System.out.println(" node*2 minid " + tree[d][node*2].minid);
			if (DEBUG) System.out.println(" node*2+1 minid " + tree[d][node*2+1].minid);
			tree[d][node].minid = (arr[tree[d][node*2].minid] < arr[tree[d][node*2 + 1].minid]) ?
					tree[d][node*2].minid : tree[d][node*2 + 1].minid  ;
		}
		else {
			tree[d][node].minid = tree[d][node*2].minid < tree[d][node*2 + 1].minid ?
					tree[d][node*2].minid : tree[d][node*2 + 1].minid ;
			
		}
		if (DEBUG) System.out.println(" tree minid " + tree[d][node].minid);
		if (arr[tree[d][node*2].maxid] != arr[tree[d][node*2 + 1].maxid])  {
			tree[d][node].maxid = (arr[tree[d][node*2].maxid] > arr[tree[d][node*2 + 1].maxid]) ?
					tree[d][node*2].maxid : tree[d][node*2 + 1].maxid  ;
		}
		else {
			tree[d][node].maxid = tree[d][node*2].maxid > tree[d][node*2 + 1].maxid?
					tree[d][node*2].maxid: tree[d][node*2 + 1].maxid;
		}
		if (DEBUG) System.out.println(" tree maxid " + tree[d][node].maxid);

		update(d, node/2);
	}

	void init(int N, Solution.Participant mParticipants[]) {
		arr = new int[S];
		sumfr = new int[2];
		cnt = new int[2];
		tree = new Tree[2][S*2];
		for (int i = 0; i < S; i++) {
			arr[i] = 0;
		}
		arr[MIN_ID] = S; 
		arr[MAX_ID] = -1;
		for (int i = 0; i < S*2; i++) {
			tree[0][i] = new Tree();
			tree[1][i] = new Tree();
		}
		for (int i = 0; i < N; i++) {
			addParticipant(mParticipants[i]);
		}
		

	}

	boolean DEBUG = false;
	void addParticipant(Solution.Participant mParticipant) {
		int id = mParticipant.id;
		int td = mParticipant.tendency;
		arr[id] = td;
		Tree tmp = new Tree(id);
		int d;
		if (DEBUG) {
			System.out.println("tree[0][1].minid " + tree[0][1].minid );
			System.out.println("tree[0][1].maxid " + tree[0][1].maxid );
			System.out.println("tree[1][1].minid " + tree[1][1].minid );
			System.out.println("tree[1][1].maxid " + tree[1][1].maxid );
		}
		if (arr[tmp.maxid] != arr[tree[0][1].maxid])  {
			d = (arr[tmp.maxid] > arr[tree[0][1].maxid]) ? 1 : 0;
		}
		else {
			d = (tmp.maxid > tree[0][1].maxid) ? 1 : 0;
			
		}
		if (DEBUG) {
			System.out.println("id: " + id + " td: " + td + " d: " + d);
			System.out.println("tmp maxid: " + tmp.maxid + " tmp minid: " + tmp.minid );
		}
		sumfr[d] += td;
		cnt[d]++;
		tree[d][S + id] = tmp;
		update(d, (S + id)/2);
		balance();

	}
	
	int removeParticipants(int K) {
		if (DEBUG) System.out.println("=== remove mode: " + K);
		if (DEBUG) {
			System.out.println("tree[0][1].minid " + tree[0][1].minid );
			System.out.println("tree[0][1].maxid " + tree[0][1].maxid );
			System.out.println("tree[1][1].minid " + tree[1][1].minid );
			System.out.println("tree[1][1].maxid " + tree[1][1].maxid );
		}
		int id, res = 0, d ;
		if (K == 0) {
			id = tree[0][1].minid;
			d = 0;
		}
		else if (K == 2) {
			id = tree[1][1].maxid;
			d = 1;
		}
		else if (cnt[0] >= cnt[1]) {
			id = tree[0][1].maxid;
			d = 0;
		}
		else {
			id = tree[1][1].minid;
			d = 1;
		}
		
		sumfr[d] -= arr[id];
		cnt[d]--;
		arr[id] = 0;
		tree[d][S + id] = new Tree();
		update(d, (S + id)/2);
		res = id;
		
		if (K == 1 && cnt[0] < cnt[1]) res += removeParticipants(1);
		balance();
		

		return res;
	}

	void balance() {
		int id, from, to;
		if (DEBUG) System.out.println(" cn0 : " + cnt[0] + " cnt1 : " + cnt[1]);
		if (cnt[0] > cnt[1] + 1) {
			from = 0;
			id = tree[0][1].maxid;
			if (DEBUG) {
				System.out.println("from 0 to 1, id : " + id);
			}
		}
		else if (cnt[0] + 1 < cnt[1]) {
			from = 1;
			id = tree[1][1].minid;
			if (DEBUG) {
				System.out.println("from 1 to 0, id : " + id);
			}

		}
		else {
			if (DEBUG) System.out.println("balanced already");
			return;
		}
		
		to = 1 - from;
		
		tree[from][S + id] = new Tree();
		tree[to][S + id] = new Tree(id);
		update(from, (S + id)/2);
		update(to, (S + id)/2);
		sumfr[from] -= arr[id];
		sumfr[to] += arr[id];
		cnt[from]--;
		cnt[to]++;
		
	}
	
	
	void getTotal(int tot[]) {
		tot[0] = sumfr[0];
		tot[1] = sumfr[1];
		
		if (cnt[0] > cnt[1]) {
			tot[0] -= arr[tree[0][1].maxid];
		}
		else if (cnt[0] < cnt[1]) {
			tot[1] -= arr[tree[1][1].minid];
		}
	}
}

