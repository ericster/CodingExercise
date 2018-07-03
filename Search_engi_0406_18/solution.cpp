/**** Solution.cpp ***/

/*
 * solution.cpp
 *
 *  Created on: Apr 6, 2018
 *      Author: me.jung
 */
//#include <bits/stdc++.h>
#include <iostream>
#include <string>
#include <string.h>
#include <stdio.h>
#include <cstddef>
#define MAX_KEY 11
#define MAX_TABLE 10000

using namespace std;
const int ALPHABET_SIZE = 26;
bool DEBUG = false;
bool DEBUG2 = true;



// linked list node
struct node
{
    int data;
    node *next;
};

// linked list class
class linked_list
{
private:
    node *head,*tail;
public:
    linked_list()
    {
        head = NULL;
        tail = NULL;
    }

    void add_node(int n)
    {
        node *tmp = new node;
        tmp->data = n;
        tmp->next = NULL;

        if(head == NULL)
        {
            head = tmp;
            tail = tmp;
        }
        else
        {
            tail->next = tmp;
            tail = tail->next;
        }
    }

    node* gethead()
    {
        return head;
    }

    static void display(node *head)
    {
        if(head == NULL)
        {
            cout << "NULL" << endl;
        }
        else
        {
            cout << "page " << head->data << endl;
            display(head->next);
        }
    }

    static void concatenate(node *a,node *b)
    {
        if( a != NULL && b!= NULL )
        {
            if (a->next == NULL)
                a->next = b;
            else
                concatenate(a->next,b);
        }
        else
        {
            cout << "Either a or b is NULL\n";
        }
    }

    void front(int n)
    {
        node *tmp = new node;
        tmp -> data = n;
        tmp -> next = head;
        head = tmp;
    }

    void after(node *a, int value)
    {
        node* p = new node;
        p->data = value;
        p->next = a->next;
        a->next = p;
    }

    //node* search(int key)
    bool search(int key)
    {
        node* current = head;  // Initialize current
        //while (current != NULL)
        while (current != nullptr)
        {
            if (current->data == key)
                return true;
            current = current->next;
        }
        return false;
    }

    void del(int key)
    {
    	node* temp = head; // If head node itself holds the key to be deleted
    	node* prev;
    	if (temp != NULL && temp->data == key)
    	{
    		head = temp->next;
    		return;
    	}

    	// Search for the key to be deleted, keep track of the
    	// previous node as we need to change 'prev->next'
    	while (temp !=NULL && temp->data != key)
    	{
    		prev = temp;
    		temp = temp->next;
    	}

    	// If key was not present in linked list
    	if (temp == NULL) return;

    	// Unlik the node from linked list
    	prev->next = temp->next;

    	// BUG-0414 when tail is removed!!
    	if (tail->data == temp->data){
    		tail = prev;
    	}
    }
};

// trie node
struct TrieNode
{
	struct TrieNode *children[ALPHABET_SIZE];
	bool isEndOfWord = false;
	bool block = false;
	int blockCnt = 0;
	bool removed = false;
	linked_list *pages = new linked_list;
};

class STrie
{
private:
    TrieNode *root;
public:
    STrie()
    {
    	// BUG : not initialized
    	//root = (TrieNode *) calloc(1, sizeof(TrieNode));
    	//TrieNode* rootNode = new TrieNode;
		//root = rootNode;
		root = new TrieNode;
    }

	TrieNode *getNode(void)
	{
		struct TrieNode *pNode =  new TrieNode;

		pNode->isEndOfWord = false;

		for (int i = 0; i < ALPHABET_SIZE; i++)
			pNode->children[i] = NULL;

		return pNode;
	}

	// function to check if current node is leaf node or not
	bool isLeafNode()
	{
		return root->isEndOfWord!= false;
	}

	// function to display the content of Trie
	void printTrie(char str[], int level)
	{
		if (DEBUG)	cout << "print Trie of page added" << endl;
		printTrieUtil(root, str, level);

	}

	void printTrieUtil(TrieNode *root, char str[], int level)
	{
		// If node is leaf node, it indiicates end
		// of string, so a null charcter is added
		// and string is displayed
		//if (isLeafNode(root))
		if (root->isEndOfWord)
		{
			str[level] = '\0';
			if (DEBUG)
			{
				cout << "word " << str << endl;
				node *tmp = root->pages->gethead();
				root->pages->display(tmp);
			}
		}

		//for (int i = 0; i < ALPHABET_SIZE; i++)
		for (int i = 0; i < 26; i++)
		{
			// if NON NULL child is found
			// add parent key to str and
			// call the display function recursively
			// for child node
			if (root->children[i])
			{
				str[level] = i + 'a';
				//cout << " print Trie of page added str : " << str << endl; printTrie(root->children[i], str, level + 1);
				printTrieUtil(root->children[i], str, level + 1);
			}
		}
	}

	void removePageTrie(int mId, int level)
	{
		removePageTrieUtil(root, mId, level);

	}

	void removePageTrieUtil(TrieNode* root, int mId, int level)
	{
		// If node is leaf node, it indiicates end
		// of string, so a null charcter is added
		// and string is displayed
		//if (isLeafNode(root))
		if (root->isEndOfWord)
		{
			root->pages->del(mId);
		}

		//for (int i = 0; i < ALPHABET_SIZE; i++)
		for (int i = 0; i < 26; i++)
		{
			// if NON NULL child is found
			// add parent key to str and
			// call the display function recursively
			// for child node
			if (root->children[i])
			{
				//cout << " print Trie of page added str : " << str << endl;
				removePageTrieUtil(root->children[i], mId, level + 1);
			}
		}
	}


	void insert(char* key, int mId )
	{
		struct TrieNode *pNode = root;
		// null terminated array
		int length = strlen(key);
		if (DEBUG) cout << "str length is xx " << length << endl;

		/*** BUG
		 * character size should be determined
		 * otherwise, caused runtime termination (-1)
		 */
		for (int i = 0; i < length; i++)
		{
			int index = key[i] - 'a';
			if (DEBUG) cout << "inserted " << key[i] << " mId " << mId << endl;
			if (!pNode->children[index])
				//pNode->children[index] = getNode();
				//pNode->children[index] = new TrieNode;
				pNode->children[index] = new TrieNode();

			pNode = pNode->children[index];
		}

		if (DEBUG) cout << "inserted " << key << " mId " << mId << endl;

		pNode->isEndOfWord = true;
		pNode->pages->add_node(mId);
	}

	bool searchTrie(char* key)
	{
		struct TrieNode *pNode = root;
		int length = strlen(key);
		if (DEBUG) cout << "str length is " << length << endl;

		for (int i = 0; i < length; i++)
		{
			//cout << " key " << key << " i " << key[i] << endl;
			int index = key[i] - 'a';
			if (!pNode->children[index]){
				if (DEBUG) cout << " failed " << endl;
				return false;
			}

			pNode = pNode->children[index];
		}

		//return (pNode != NULL && pNode->isEndOfWord);
		return (pNode->isEndOfWord);
	}

	linked_list* searchTriePages(char* key)
	{
		struct TrieNode *pNode = root;
		int length = strlen(key);
		if (DEBUG) cout << "str length is " << length << endl;

		//if (length == 0 ) return NULL;

		for (int i = 0; i < length; i++)
		{
			//cout << " key " << key << " i " << key[i] << endl;
			int index = key[i] - 'a';
			if (!pNode->children[index]){
				if (DEBUG) cout << " failed " << endl;
				return NULL;
			}

			pNode = pNode->children[index];
		}

		//return (pNode != NULL && pNode->isEndOfWord);
		//return (pNode->isEndOfWord) ? &(pNode->pages) : NULL;
		return pNode->pages;
	}

	void blockTrie(char* key)
	{
		struct TrieNode *pNode = root;
		int length = strlen(key);
		for (int i = 0; i < length; i++)
		{
			int index = key[i] - 'a';
			if (!pNode->children[index]){
				return ;
			}

			pNode = pNode->children[index];
		}

		//return (pNode != NULL && pNode->isEndOfWord);
		pNode->block = true;
		root->blockCnt++;
		if (root->blockCnt > 0){
			root->block = true;
		}
	}

	/*
	 * DEBUG: consider the case of having two or more block words
	 */
	linked_list* getPages(char* key)
	{
		struct TrieNode *pNode = root;
		int length = strlen(key);
		for (int i = 0; i < length; i++)
		{
			int index = key[i] - 'a';
			if (!pNode->children[index]){
				//return nullptr;
				return NULL;
			}

			pNode = pNode->children[index];
		}

		return (pNode != NULL && pNode->isEndOfWord) ? pNode->pages : NULL ;
		pNode->block = false;
		//return &(pNode->pages);
	}
};



typedef struct Hash
{
	char key[MAX_KEY + 1];
	linked_list *pages;
} Hash;

class Hashtable {
private:
	Hash tb[MAX_TABLE];
public:
	Hashtable()
	{

	}
	unsigned long hash(const char *str)
	{
		unsigned long hash = 5381;
		int c;

		while (c = *str++)
		{
			hash = (((hash << 5) + hash) + c) % MAX_TABLE;
		}

		return hash % MAX_TABLE;
	}

	int find(const char *key, linked_list *data)
	{
		unsigned long h = hash(key);
		int cnt = MAX_TABLE;
		int step = 1;

		while (tb[h].key[0] != 0 && cnt--)
		{
			if (strcmp(tb[h].key, key) == 0)
			{
				data = tb[h].pages;

				return 1;
			}
			h = (h + 1) % MAX_TABLE;
		}
		return 0;
	}

	int add(const char *key, linked_list *data)
	{
		unsigned long h = hash(key);
		int step = 1;
		int i = 0;

		while (tb[h].key[0] != 0)
		{
			if (strcmp(tb[h].key, key) == 0)
			{
				return 0;
			}

			h = (h + 1) % MAX_TABLE;
		}
		strcpy(tb[h].key, key);
		tb[h].pages = data;
		return 1;
	}

};

int page_cnt;
//struct TrieNode* pages;
STrie *st ;
//int blocked_page[50000] = {0};
bool active_pages[50000] = {false} ;
int *blocked_page;
//bool active_pages[50000] ;
Hashtable *ht;


void init(int n)
{
	int N = 6000;
	st = new STrie;
	//bool pages [50000] = {0};
	blocked_page = new int[50000];
	ht = new Hashtable();
	if (DEBUG) cout << "init called" << endl;

	//page_cnt = 0;

}

void addPage_0(int mId, int m, char word[][11])
{

}
void addPage(int mId, int m, char word[][11])
{

	active_pages[mId] = true;
	if (DEBUG) cout << "added page " << mId << endl;
	if (DEBUG) cout << "page count " << page_cnt << endl;
	for (int i=0;i<m;i++){
		if (DEBUG) cout << "current word " << word[i] << endl;
		st->insert(word[i], mId);
	}

    int level = 0;
    char str[11];
	//st->printTrie(str, 0);

}

void removePage_0(int mId)
{

}
void removePage(int mId)
{
	if (DEBUG) cout << "page removed " << mId << endl;
	active_pages[mId] = false;
	int level = 0;
	// TASK: optimization ?
	st->removePageTrie(mId, level);

}

void blockWord_0(char word[]) {

}
void blockWord(char word[]) {
	if (DEBUG) cout << "page blocking " << word << endl;
	linked_list *bpages = st->getPages(word);
	if (bpages == NULL) {
		if (DEBUG) cout << "page returned " << word << endl;
		return;
	}
	node *tmp = bpages->gethead();

	while (tmp != NULL){
		if (DEBUG) cout << "blocked page " << tmp->data << endl;
		blocked_page[tmp->data]++ ;
		tmp = tmp->next;
	}

	/*
	 *
	 * 1, 2, 3, 4, 5
	 * abc -> 1, 3
	 * bcd -> 1,   5
	 */
}

/*
 * BUG change page type to reference with addr operator
 *
 */
void recoverWord_0(char word[])
{

}
void recoverWord(char word[])
{
	if (DEBUG) cout << "page recovering " << word << endl;
	linked_list* bpages = st->getPages(word);
	if (bpages == NULL) return;
	node *tmp = bpages->gethead();

	while (tmp != NULL){
		// BUG
		if (blocked_page[tmp->data] > 0) blocked_page[tmp->data]-- ;
		tmp = tmp->next;
	}

}


/*
1
50 5 10 0 5
2 3 3 1 1


1
60 5 10 0 6
0 1 1 0 2 1


1 3 4 5
1 2 6 7
 */
int search_0(char word[][11], int mode)
{
	if (DEBUG) cout << "null func" << endl;
	return 0;

}
int search(char word[][11], int mode)
{
	/*
	 * mode
	 * 0: should be searched as 0-th word
	 * 1: AND
	 * 2: OR
	 */
	int len=2;
	for (int i=0;i<len;i++){
		if (DEBUG) cout << "word in searching " << word[i] << endl;
	}
	int m = mode;
	int match_pages=0;
	int level = 0;
	char str[11];
	/*
	 * BUG: var moved to correctly update
	 */
	if (DEBUG) cout << "*** searching page  with words in mode " << m << endl;
	if (DEBUG) cout << "*** print done" << endl;
	linked_list *search_0th;
	linked_list *search_1st;
	int first_cnt=0;
	int second_cnt=0;
	int mode2_cnt=0;
	int common_cnt=0;
	// mode 0 default

	search_0th = st->searchTriePages(word[0]);
	search_1st = st->searchTriePages(word[1]);
	// BUG: handle NULL ptr
	// ????
	//if (search_0th == NULL || search_1st == NULL) return 0;
	//if (search_0th == NULL ) return 0;
	// BUG-0414 to handle null linked list
	node *temp = (search_0th == nullptr) ? nullptr : search_0th->gethead();
	//node *temp = search_0th->gethead();
	while (temp != nullptr){
		if (blocked_page[temp->data] == 0){
			first_cnt++;
		}
		temp = temp->next;
	}
	if (DEBUG) cout << "0th pages before " <<  first_cnt << endl;
	// mode 1
	node *temp1 = (search_1st == nullptr ) ? nullptr : search_1st->gethead();
	//node *temp1 = search_1st->gethead();
	if (DEBUG) cout << "0th pages " <<  first_cnt << endl;
	if (DEBUG && search_0th != NULL) search_0th->display(search_0th->gethead());
	if (DEBUG && search_1st != NULL) search_1st->display(temp1);
	while (temp1 != nullptr){
		if (blocked_page[temp1->data] == 0){
			second_cnt++;
		}
		if ((search_0th != NULL) && search_0th->search(temp1->data)){
			if (blocked_page[temp1->data] == 0){
				common_cnt++;
			}
		}
		temp1 = temp1->next;
	}
	if (DEBUG) cout << "first cnt " <<  first_cnt << endl;
	if (DEBUG) cout << "second cnt " << second_cnt << endl;
	if (DEBUG) cout << "common cnt " << common_cnt << endl;
	mode2_cnt = first_cnt + second_cnt - common_cnt;
	if (DEBUG) cout << "mode 2 cnt " <<  mode2_cnt << endl;



	if (mode==0){
		match_pages = first_cnt;
	}
	else if (mode==1) {
		match_pages = common_cnt;
	}
	else if (mode==2){
		match_pages = mode2_cnt;
		if (DEBUG) cout << "mode 2" << endl;
	} else {
		if (DEBUG) cout << "nothing" << endl;

	}

	if (DEBUG) cout << "**** matching pages " << match_pages << " in mode "  << mode <<  endl;
	return match_pages;
	//return 0;
}
