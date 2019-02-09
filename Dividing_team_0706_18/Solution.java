import java.util.Scanner;

public class Solution {
	
	public static final int MAXID = 100000;
	public static final int MAXTENDENCY = 100001;
	
	private static final int REMOVERATE = 6;
	private static final int AVERAGERATE = 4;
	
	static UserSolution user = new UserSolution();
	static Scanner sc;
	
	public static class Participant {
		int id;
		int tendency;
	}
	
	static int idChk[] = new int[MAXID];
	
	static int seed = 3;
	static int N, M;
	
	static int pseudo_rand() {
		seed = seed * 431345 + 2531999;
		return seed & 0x7FFFFFFF;
	}
	
	static int getTendency() {
		int tendency;

		do {
			tendency = pseudo_rand() % MAXTENDENCY;
		} while (tendency == MAXTENDENCY - 1);

		return tendency;
	}
	
	static void initFunc(int test_case, int n) {

		Participant participants[] = new Participant[50001];

		for (int i = 0; i < n; ++i) {
			int id = pseudo_rand() % MAXID;
			int tendency = getTendency();

			while (idChk[id] == test_case) {
				id = (id + 1) % MAXID;
			}

			idChk[id] = test_case;
			
			participants[i] = new Participant();

			participants[i].id = id;
			participants[i].tendency = tendency;
		}

		user.init(n, participants);
	}
	
	static void addFunc(int test_case) {

		Participant participant = new Participant();

		int id = pseudo_rand() % MAXID;
		int tendency = getTendency();

		while (idChk[id] == test_case) {
			id = (id + 1) % MAXID;
		}

		idChk[id] = test_case;

		participant.id = id;
		participant.tendency = tendency;

		user.addParticipant(participant);
	}
	
	static int run(int test_case) {
		int accepted = 0;

		boolean conChk = true;

		initFunc(test_case, N);

		while (M-- > 0) {
			int command = pseudo_rand() % (REMOVERATE + AVERAGERATE);

			if (conChk && command < AVERAGERATE) {
				int tot[] = new int[2];
				int correctTot[] = new int[2];

				user.getTotal(tot);
				
				correctTot[0] = sc.nextInt();
				correctTot[1] = sc.nextInt();

				if (tot[0] == correctTot[0] || tot[1] == correctTot[1]) {
					++accepted;
				}
				else {
				System.out.println("tot0 : " + tot[0] + " tot1 : " + tot[1] + " "
						+ "corTot0 : " + correctTot[0] + " corTot1 : " + correctTot[1]);
				}

				conChk = false;
			}
			else {
				int K = pseudo_rand() % 3;
				int retId, correctId;

				retId = user.removeParticipants(K);
				correctId = sc.nextInt();

				if (retId == correctId) {
					++accepted;
				}
				else {
					System.out.println("retID : " + retId + " correctId : " + correctId);
				}

				addFunc(test_case);
				if (K == 1 && N % 2 == 0) {
					addFunc(test_case);
				}

				conChk = true;
			}
		}

		return accepted;
	}
	
	public static void main(String args[]) throws Exception {
		
		int T;
		int correct, totalScore = 0;
		
		System.setIn(new java.io.FileInputStream("sample_input.txt"));
		sc = new Scanner(System.in);
		T = sc.nextInt();
		
		for (int test_case = 1 ; test_case <= T; ++test_case) {
			
			seed = sc.nextInt();
			N = sc.nextInt();
			M = sc.nextInt();
			correct = sc.nextInt();
			
			if (run(test_case) == correct) {
				System.out.println("#" + test_case + " 100");
				totalScore += 100;
			} else {
				System.out.println("#" + test_case + " 0");
			}
		}
		
		System.out.println("Total Score = " + (totalScore / T));
	}
}
