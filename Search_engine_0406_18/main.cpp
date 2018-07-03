#ifndef _CRT_SECURE_NO_WARNINGS
#define _CRT_SECURE_NO_WARNINGS
#endif

#include<stdio.h>
#include<iostream>
#include<string.h>
#include <time.h>
using namespace std;

#define MAXPAGES 50000
#define MAXWORDS 10000
#define MAXBLOCK 10000

#define REMOVERATE 5
#define BLOCKRATE 10
#define RECOVERRATE 15
#define ADDRATE 60


extern void init(int n);
extern void addPage(int mId, int m, char word[][11]);
extern void removePage(int mId);
extern void blockWord(char word[]);
extern void recoverWord(char word[]);
extern int search(char word[2][11], int mode);

static int seed = 3;
static int N, M, addN;
static char words[MAXWORDS][11];

static int pageN, blockN, blockCnt;
static bool pageChk[MAXPAGES];
static bool blockChk[MAXWORDS];
static int blockWords[MAXBLOCK];

bool DEBUGm = false;

static int pseudo_rand(void)
{
	seed = seed * 431345 + 2531999;
	return seed & 0x7FFFFFFF;
}

static void makeWords() {
	int len;

	for (register int i = 0; i < MAXWORDS; ++i) {
		len = pseudo_rand() % 6 + 4;

		for (register int j = 0; j < len; ++j) {
			words[i][j] = pseudo_rand() % 26 + 'a';
		}
		words[i][len] = 0;
	}
}

static void addFunc() {
	int wordsN = pseudo_rand() % 4 + 1;
	char temp[4][11] = { 0 };

	for (int i = 0; i < wordsN; ++i) {

		bool sameChk = false;
		int randIdx = pseudo_rand() % (N < MAXWORDS ? N : MAXWORDS);

		for (int j = 0; j < i && !sameChk; ++j) {
			if (!strcmp(words[randIdx], temp[j])) {
				sameChk = true;
			}
		}

		if (!sameChk) {
			strcpy(temp[i], words[randIdx]);
		}
		else {
			--i;
		}
	}

	addPage(pageN, wordsN, temp);

	pageChk[pageN] = true;
	++pageN;
}

static void removeFunc() {
	int randIdx = pseudo_rand() % pageN;

	while (!pageChk[randIdx]) {
		++randIdx;

		if (randIdx == pageN) {
			randIdx = 0;
		}
	}

	removePage(randIdx);

	pageChk[randIdx] = false;
}

static void blockFunc() {
	int randIdx = pseudo_rand() % (N < MAXWORDS ? N : MAXWORDS);
	char temp[11] = { 0 };

	while (blockChk[randIdx]) {
		++randIdx;

		if (randIdx == (N < MAXWORDS ? N : MAXWORDS)) {
			randIdx = 0;
		}
	}

	strcpy(temp, words[randIdx]);

	blockWord(temp);

	blockChk[randIdx] = true;
	blockWords[blockN] = randIdx;
	++blockN;
	++blockCnt;
}

static void recoverFunc() {
	int randIdx = pseudo_rand() % blockN;
	char temp[11] = { 0 };

	while (blockWords[randIdx] == -1) {
		++randIdx;

		if (randIdx == blockN) {
			randIdx = 0;
		}
	}

	strcpy(temp, words[blockWords[randIdx]]);

	recoverWord(temp);

	blockChk[blockWords[randIdx]] = false;
	blockWords[randIdx] = -1;
	--blockCnt;
}

static int searchFunc() {
	int mode = pseudo_rand() % 3;
	int randIdx = pseudo_rand() % (N < MAXWORDS ? N : MAXWORDS);
	char temp[2][11] = { 0 };

	while (blockChk[randIdx]) {
		++randIdx;

		if (randIdx == (N < MAXWORDS ? N : MAXWORDS)) {
			randIdx = 0;
		}
	}

	strcpy(temp[0], words[randIdx]);

	if (mode >= 1) {
		do {
			randIdx = pseudo_rand() % (N < MAXWORDS ? N : MAXWORDS);

			while (blockChk[randIdx]) {
				++randIdx;

				if (randIdx == (N < MAXWORDS ? N : MAXWORDS)) {
					randIdx = 0;
				}
			}
		} while (!strcmp(words[randIdx], temp[0]));

		strcpy(temp[1], words[randIdx]);
	}

	return search(temp, mode);
}

static int run(int test_case) {

	int accepted = 0;

	pageN = blockN = blockCnt = 0;

	//cout << "Main init addFunc() " << addN << endl;
	for (int i = 0; i < addN; ++i) {
		addFunc();
	}

	for (int i = 0; i < M; ++i) {

		int command = pseudo_rand() % 100;

		if (command < REMOVERATE) {
			//cout << "Main removeFunc()" << endl;
			removeFunc();
		}
		else if (command < BLOCKRATE) {
			//cout << "Main blockFunc()" << endl;
			blockFunc();
		}
		else if (command < RECOVERRATE && blockCnt > 0) {
			//cout << "Main recoverFunc()" << endl;
			recoverFunc();
		}
		else if (command < ADDRATE && pageN < N) {
			//cout << "Main addFunc()" << endl;
			addFunc();
		}
		else {
			int answer = searchFunc();
			//cout << "Main  searchFunc()" << endl;
			int correct;

			if (test_case <= 5) {
				//scanf("%d", &correct);
				cin >> correct ;
				if (DEBUGm) cout << "Main CORRECT: " << correct << endl;
				if (DEBUGm) cout << "Main Answer: " << answer << endl;

				if (correct == answer) {
					++accepted;
					if (DEBUGm) cout << "Main Accepted: " << accepted << endl;
				}
				else{
					if (DEBUGm) cout << "Main NOT Accepted: " << endl;

				}
			}
			else {
				accepted += answer;
			}
		}
	}

	for (int i = 0; i < blockN; ++i) {
		if (blockWords[i] != -1) {
			blockChk[blockWords[i]] = false;
		}
	}

	return accepted;
}

int main(void) {

    clock_t start, end;
    double cpu_time_used;


	int T;
	int correct, totalScore = 0;

	freopen("sample_input.txt", "r", stdin);
    start = clock();

	scanf("%d", &T);

	setbuf(stdout, NULL);

	makeWords();

	for (int test_case = 1; test_case <= T; ++test_case) {

		scanf("%d%d%d%d%d", &seed, &N, &M, &addN, &correct);

		init(N);

		if (run(test_case) == correct) {
			printf("#%d 100\n", test_case);
			totalScore += 100;
		}
		else {
			printf("#%d 0\n", test_case);
		}
	}

	printf("Total Score = %d\n", totalScore / T);
    end = clock() - start;
    double time_taken = ((double)end)/CLOCKS_PER_SEC; // in seconds

    printf("runtime  %f seconds to execute \n", time_taken);

	return 0;
}

