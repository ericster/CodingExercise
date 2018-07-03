/*
 * Solution.cpp
 *
 *  Created on: Mar 28, 2018
 *      Author: me.jung
 */


#define MAX_N 1000
#include <iostream>
#include <stdio.h>
//#include <bits/stdc++.h>
#include <string>
using namespace std;


extern int my_strlen(const char* a);
extern void my_strcpy(char* a, const char* b);
extern int my_strcmp(const char* a, const char* b);
extern char* my_strcat(char* a, const char* b);

int numcmp(const char* a, const char* b);
void add(char* r, const char* a, const char* b);
void sub(char* r, const char* a, const char* b);
void mul(char* r, const char* a, const char* b);
void div(char* r, const char* a, const char* b);


#define MAX 1000


struct fractionSt
{
    //char num[500];
    //char denom[500];
    //char* num;
    //char* denom;
    string num;
    string denom;
};

int gcd(int a, int b){
    if (a==0) return b;
    return gcd(b%a, a);
}

void lcm(int &den, int num){
    int cfactor = gcd(num, den);
    den = den/cfactor;
    num = num/cfactor;
}

void addFraction(int num1, int den1, int num2, int den2, int &num, int &den)
{
    den = gcd(den1,den2);
    den = (den1*den2) / den;
    num = (num1)*(den/den1) + (num2)*(den/den2);
    lcm(den,num);
}

// The main function that converts given infix expression
// to postfix expression.

class Stack
{
    int top;
public:
    int a[MAX];    //Maximum size of Stack

    Stack()  { top = -1; }
    bool push(int x);
    int pop();
    int peek();
    int size();
    bool isEmpty();
};

bool Stack::push(int x)
{
    if (top >= MAX)
    {
        cout << "Stack Overflow";
        return false;
    }
    else
    {
        a[++top] = x;
        return true;
    }
}

int Stack::pop()
{
    if (top < 0)
    {
        cout << "Stack Underflow";
        return 0;
    }
    else
    {
        int x = a[top--];
        return x;
    }
}

int Stack::size(){
    return top+1;

}
int Stack::peek()
{
    if (top < 0)
    {
        cout << "Stack Underflow";
        return 0;
    }
    else
    {
        int x = a[top];
        return x;
    }
}

bool Stack::isEmpty()
{
    return (top < 0);
}

// A utility function to check if the given character is operand
int isOperand(char ch)
{
    return (ch >= '0' && ch <= '9');
}

int isOperator(char ch)
{
    return (ch == '+' || ch == '-' || ch == '*' || ch == '/');
}

// A utility function to return precedence of a given operator
// Higher returned value means higher precedence
int Prec(char ch)
{
    switch (ch)
    {
    case '+':
    case '-':
        return 1;

    case '*':
    case '/':
        return 2;

    case '^':
        return 3;
    }
    return -1;
}

/*
int infixToPostfix(char* exp)
{
    int i, k;

    // Create a stack of capacity equal to expression size
    struct Stack* stack = createStack(strlen(exp));
    if(!stack) // See if stack was created successfully
        return -1 ;

    for (i = 0, k = -1; exp[i]; ++i)
    {
        // If the scanned character is an operand, add it to output.
        if (isOperand(exp[i]))
            exp[++k] = exp[i];

        // If the scanned character is an ‘(‘, push it to the stack.
        else if (exp[i] == '(')
            push(stack, exp[i]);

        // If the scanned character is an ‘)’, pop and output from the stack
        // until an ‘(‘ is encountered.
        else if (exp[i] == ')')
        {
            while (!isEmpty(stack) && peek(stack) != '(')
                exp[++k] = pop(stack);
            if (!isEmpty(stack) && peek(stack) != '(')
                return -1; // invalid expression
            else
                pop(stack);
        }
        else // an operator is encountered
        {
            while (!isEmpty(stack) && Prec(exp[i]) <= Prec(peek(stack)))
                exp[++k] = pop(stack);
            push(stack, exp[i]);
        }

    }

    // pop all the operators from the stack
    while (!isEmpty(stack))
        exp[++k] = pop(stack );

    exp[++k] = '\0';
    printf( "%sn", exp );
}
*/

/*
 *
5
16/19+11/17 1.48916408668730650154
8/4*8+16*8-81/16 138.9375
7/8-2*40/7+31/28/2 -10
918*22-80-79-588/4-828+26/79*158*49 21610
115505409478830799813945526305909*1984/93876932890258295829490609296928358 2.44109735321125681021

1
16/19+11/17 1.48916408668730650154

1
8/4*8+16*8-81/16 138.9375


 *
 */
void run(char* rst, const char* str)
{
    cout << "input string " << str << endl;
    int N = my_strlen(str);
    bool plus = true;
    bool denumF = false;
    int k=0,m=0;
    int l=0;
    int no_cnt;
    int opcode=0;
    N = 100;
    // http://ysonggit.github.io/coding/2015/01/01/variable-length-array-for-non-pod-element-type.html
    fractionSt* pfraction = new fractionSt[N];
    fractionSt* mfraction = new fractionSt[N];
    fractionSt mfractiont;
    //char operand[500];
    char result[1000];
    pfraction[0].num ="1";
    pfraction[0].denom ="1";
    mfraction[0].num ="1";
    mfraction[0].denom ="1";
    char op_stack = '+';
    int plus_cnt = 0;
    int minus_cnt = 0;
    string one("1");

    // ##1 parsing and prep +/- fractions
    for (int i=0;i<N;i++){
        char ch = str[i];
        char chn = str[i+1];
        string operand;
        //cout << "character " << ch << endl;
        //pfraction[0].num = "1";
        //pfraction[0].denom = "1";
        if(isOperand(ch)) {
            while(isOperand(str[i])){
                operand += str[i++];
            }
            cout << "operand " << operand << endl;
            i--;
            //char char_arr[operand.length()+1]; /** bug: value incorretly updated **/
            //char* char_arr = new char[operand.length() + 1];
            //strcpy(char_arr, operand.c_str());
            cout << "p num " << pfraction[plus_cnt].num << endl;
            cout << "p denom " << pfraction[plus_cnt].denom << endl;
            cout << "op_stack " << op_stack << endl;
            char* res = new char[500];
            switch (op_stack){
                case '+':
                    plus_cnt++;
                    //cout << "+plus char_arr" << char_arr << endl;
                    pfraction[plus_cnt].num = operand;
                    pfraction[plus_cnt].denom = &one[0];
                    //pfraction[plus_cnt].num = operand;
                    //pfraction[plus_cnt].denom = "2";
                    cout << "+plus plus_cnt " << plus_cnt << endl;
                    cout << "+plus num " << pfraction[plus_cnt].num << endl;
                    cout << "+plus denom " << pfraction[plus_cnt].denom << endl;
                    break;

                case '-':
                    minus_cnt++;
                    mfraction[minus_cnt].num = operand;
                    mfraction[minus_cnt].denom = &one[0];
                    break;
                case '*':
                    if (plus){
                        mul(res, pfraction[plus_cnt].num.c_str(), operand.c_str());
                        pfraction[plus_cnt].num = res;
                        cout << "p/ num " << pfraction[plus_cnt].num << endl;
                        cout << "p/ denom " << pfraction[plus_cnt].denom << endl;
                    }
                    else {
                        mul(res, mfraction[minus_cnt].num.c_str(), operand.c_str());
                        cout << "/minus result " << res << endl;
                        mfraction[minus_cnt].num = res;
                    }
                    break;
                case '/':
                    if (plus){
                        //cout << "+plus char_arr" << char_arr << endl;
                        cout << "/plus plus_cnt " << plus_cnt << endl;
                        cout << "/plus denom before " << pfraction[plus_cnt].denom << endl;
                        //mul(res, pfraction[plus_cnt].denom.c_str(), operand.c_str());
                        mul(res, pfraction[plus_cnt].denom.c_str(), operand.c_str());
                        cout << "/plus operand " << operand << endl;
                        cout << "/plus denom " << pfraction[plus_cnt].denom << endl;
                        cout << "/plus result " << res << endl;
                        pfraction[plus_cnt].denom = res;
                        //pfraction[plus_cnt].denom = operand;
                        cout << "p/ num " << pfraction[plus_cnt].num << endl;
                        cout << "p/ denom " << pfraction[plus_cnt].denom << endl;
                    }
                    else {
                        mul(res, mfraction[minus_cnt].denom.c_str(), operand.c_str());
                        cout << "/minus result " << res << endl;
                        mfraction[minus_cnt].denom = res;
                    }
                    break;
            }
        }
        else if (isOperator(ch)){
            cout << "operator " << ch << endl;
            switch (ch)
            {
                //#1
                case '+':
                    plus = true;
                    op_stack = ch;
                    break;
                //#2
                case '-':
                    plus = false;
                    op_stack = ch;
                    break;
                //#3
                case '*':
                    op_stack = ch;
                    break;
                //#4
                case '/':
                    op_stack = ch;
                    break;
            }
        }
    }


    // ##2 formulate fractions
    // pfraction (plus_cnt), mfraction (minus_cnt)
    /*
     * 1
8/4*8+16*8-81/16 138.9375

     *
     */
    int common_denom;
    cout << "*** final plus_cnt " << plus_cnt << endl;
    for (int i=0;i<=plus_cnt;i++){
        cout << "plus_cnt " << i << endl;
        cout << "p num " << pfraction[i].num << endl;
        cout << "p denom " << pfraction[i].denom << endl;
    }
    cout << "*** final minus_cnt " << minus_cnt << endl;
    for (int i=0;i<=minus_cnt;i++){
        cout << "minus_cnt " << i << endl;
        cout << "m num " << mfraction[i].num << endl;
        cout << "m denom " << mfraction[i].denom << endl;
    }

    char* temp_res;
	string num_res, denom_res;
	string mnum_res, mdenom_res;
    char* inter = "1";
	char* res1 = new char[500];
	char* res2 = new char[500];
	char* res3 = new char[500];
	char* res4 = new char[500];
    char* res5 = new char[500];
    char* res6 = new char[500];
    char* rem = new char[500];
    char* res8 = new char[500];
    char* res9 = new char[500];
    char* rem_res = new char[500];
    cout << "**** + - fraction cal" << endl;
    num_res = pfraction[1].num;
    denom_res = pfraction[1].denom;
    if (plus_cnt >1){
        for (int i=2;i<=plus_cnt;i++){
            mul(res1, pfraction[i].denom.c_str(), denom_res.c_str());
            mul(res2, pfraction[i].denom.c_str(), num_res.c_str());
            mul(res3, pfraction[i].num.c_str(), denom_res.c_str());
            add(res4, res2, res3);
            denom_res = res1;
            num_res = res4;

            cout << "denom_res " << denom_res << endl;
            cout << "num_res " << num_res << endl;
			//delete res1, res2, res3, res4;
        }
    }
    mnum_res = mfraction[1].num;
    mdenom_res = mfraction[1].denom;
    if (minus_cnt >1){
        for (int i=2;i<=plus_cnt;i++){
            mul(res1, mfraction[i].denom.c_str(), mdenom_res.c_str());
            mul(res2, mfraction[i].denom.c_str(), mnum_res.c_str());
            mul(res3, mfraction[i].num.c_str(), mdenom_res.c_str());
            add(res4, res2, res3);
            mdenom_res = res1;
            mnum_res = res4;

            cout << "mdenom_res " << mdenom_res << endl;
            cout << "mnum_res " << mnum_res << endl;
			delete res1, res2, res3, res4;
        }
    }
	cout << "mdenom_res " << mdenom_res << endl;
	cout << "mnum_res " << mnum_res << endl;

    // ##2-1
    mul(res1, denom_res.c_str(), mdenom_res.c_str());
    mul(res2, num_res.c_str(), mdenom_res.c_str());
    mul(res3, mnum_res.c_str(), denom_res.c_str());
    sub(res4, res2, res3);
    denom_res = res1;
    num_res = res4;
            cout << " +/- denom_res " << denom_res << endl;
            cout << " +/- num_res " << num_res << endl;

    // ##3 floating points
    string rem_zero;
    string decimal;
    string final_result;
	string fraction = ".";
    div(res5, num_res.c_str(), denom_res.c_str());
    cout << "decimal " << res5 << endl;
    decimal = string(res5);
    // 481/323 = 1
    cout << " numcmp " << numcmp(res5, "0") << endl;
	mul(res6, res5, denom_res.c_str());
	sub(rem, num_res.c_str(), res6);
	cout << "remainder " << rem << endl;
	int f_cnt = 0;
	while (f_cnt <20){

		rem_zero = string(rem) + '0';
		div(res8, rem_zero.c_str(), denom_res.c_str());
		cout << " rem_zero " << rem_zero << endl;
		cout << " float no " << res8 << endl;
		fraction += res8;
		mul(res9, res8, denom_res.c_str());
		sub(rem, rem_zero.c_str(), res9);
		f_cnt++;
	}
	cout << " fraction " << fraction << endl;
	// 1.48916408668730650154
	final_result = decimal + fraction;
	cout << " final result " << final_result << endl;
	strcpy(rst, final_result.c_str());

    delete res5, res6, rem, res8, res9;


}


int numcmp(const char* a, const char* b)
{
    if (a[0] != '-' && b[0] != '-')
    {
        int c = my_strlen(a);
        int d = my_strlen(b);
        if (c > d)
            return 1;
        if (c < d)
            return -1;
        do
        {
            if (*b < *a)
                return 1;
            if (*a < *b++)
                return -1;
        }   while (*++a != 0);
    }
    else if (a[0] == '-' && b[0] == '-')
        return numcmp(&b[1], &a[1]);
    else if (a[0] == '-')
        return -1;
    else
        return 1;
    return 0;
}

void add(char* r, const char* a, const char* b)
{
    if (a[0] != '-' && b[0] != '-')
    {
        if (0 <= numcmp(a, b))
        {
            int tmp[MAX_N];
            int pos_a = my_strlen(a);
            int pos_b = my_strlen(b);
            int carry = 0;
            int len = 0;
            while (0 < pos_a)
            {
                int num1 = a[--pos_a] - '0';
                int num2 = (0 < pos_b) ? b[--pos_b] - '0' : 0;
                int num3 = carry + num1 + num2;
                tmp[len++] = num3 % 10;
                carry = num3 / 10;
            }
            if (0 < carry)
                tmp[len++] = 1;
            for (int i = 0; i < len; ++i)
                r[i] = tmp[len - 1 - i] + '0';
            r[len] = 0;
        }
        else
            add(r, b, a);
    }
    else if (a[0] == '-' && b[0] == '-')
    {
        add(&r[1], &a[1], &b[1]);
        r[0] = '-';
    }
    else if (a[0] == '-')
        sub(r, b, &a[1]);
    else
        sub(r, a, &b[1]);
}

void sub(char* r, const char* a, const char* b)
{
    if (a[0] != '-' && b[0] != '-')
    {
        if (0 <= numcmp(a, b))
        {
            int tmp[MAX_N];
            int pos_a = my_strlen(a);
            int pos_b = my_strlen(b);
            int carry = 0;
            int len = 0;
            while (0 < pos_a)
            {
                int num1 = a[--pos_a] - '0';
                int num2 = (0 < pos_b) ? b[--pos_b] - '0' : 0;
                int num3 = 10 - carry + num1 - num2;
                tmp[len++] = num3 % 10;
                carry = 1 - (num3 / 10);
            }
            while (1 < len && tmp[len - 1] == 0)
                --len;
            for (int i = 0; i < len; ++i)
                r[i] = tmp[len - 1 - i] + '0';
            r[len] = 0;
        }
        else
        {
            sub(&r[1], b, a);
            r[0] = '-';
        }
    }
    else if (a[0] == '-' && b[0] == '-')
        sub(r, &b[1], &a[1]);
    else if (a[0] == '-')
    {
        add(&r[1], &a[1], b);
        r[0] = '-';
    }
    else
        add(r, a, &b[1]);
}

void mul(char* r, const char* a, const char* b)
{
    if (numcmp(a, "0") == 0 || numcmp(b, "0") == 0)
        my_strcpy(r, "0");
    else if (a[0] != '-' && b[0] != '-')
    {
        char cpy_a[MAX_N + 1];
        my_strcpy(cpy_a, a);
        my_strcpy(r, "0");
        for (int i = my_strlen(b) - 1; i >= 0; --i)
        {
            for (int j = 0; j < b[i] - '0'; ++j)
                add(r, r, cpy_a);
            my_strcat(cpy_a, "0");
        }
    }
    else if (a[0] == '-' && b[0] == '-')
        mul(r, &a[1], &b[1]);
    else
    {
        if (a[0] == '-')
            mul(&r[1], &a[1], b);
        else
            mul(&r[1], a, &b[1]);
        r[0] = '-';
    }
}

void div(char* r, const char* a, const char* b)
{
    if (numcmp(a, "0") == 0 || numcmp(b, "0") == 0)
        my_strcpy(r, "0");
    else if (numcmp(((a[0] != '-') ? a : &a[1]), ((b[0] != '-') ? b : &b[1])) == -1)
        my_strcpy(r, "0");
    else if (a[0] != '-' && b[0] != '-')
    {
        char cpy_a[MAX_N + 1];
        char cpy_b[MAX_N + 1];
        char itr[MAX_N + 1];
        my_strcpy(cpy_a, a);
        my_strcpy(r, "");
        int c = (my_strlen(a) > my_strlen(b)) ? my_strlen(a) - my_strlen(b) : 1;
        for (int i = 0; i < c; ++i)
        {
            my_strcpy(cpy_b, b);
            for (int j = 0; j < c - i - 1; ++j)
                my_strcat(cpy_b, "0");
            my_strcpy(itr, "0");
            while (0 <= numcmp(cpy_a, "0"))
            {
                add(itr, itr, "1");
                sub(cpy_a, cpy_a, cpy_b);
            }
            sub(itr, itr, "1");
            add(cpy_a, cpy_a, cpy_b);
            my_strcat(r, itr);
        }
    }
    else if (a[0] == '-' && b[0] == '-')
        div(r, &a[1], &b[1]);
    else
    {
        if (a[0] == '-')
            div(&r[1], &a[1], b);
        else
            div(&r[1], a, &b[1]);
        r[0] = '-';
    }
}
