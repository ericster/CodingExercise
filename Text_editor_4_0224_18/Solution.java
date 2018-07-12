import java.util.Scanner;

class Solution{

    static final int SCP = 0;
    static final int SCU = 1;
    static final int SIN = 2;
    static final int SPA = 3;
    static final int PAS = 4;
    static final int INS = 5;
    static final int GET = 6;
    static final int LEN = 7;
    static final int INSERT = 100;
    static final int SELECT = 200;
    static final int COPY = 300;
    static final int CUT = 400;
    static final int PASTED = 500;
    static final int GETSUBSTRING = 999;

    static char init_str[] = new char[100005];
    static char insert_str[] = new char[1005];
    static char res_str[] = new char[15];
    static char dummy1[] = new char[5555];
    static char dummy2[] = new char[5555];
    static char ans_str[] = new char[15];

    static int seed;

    static int status[] = new int[8];
    static int commandList[] = new int[100005];
    static int listsize = 0;
    static int newCommand[] = {0, COPY, CUT, INSERT, PASTED, PASTED, INSERT, GETSUBSTRING};

    private static int totalScore, curScore;

    private static Scanner sc;
    private static UserSolution user = new UserSolution();

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("sample_input.txt"));
        sc = new Scanner(System.in);

        int T = sc.nextInt();
        totalScore = 0;

        for (int test_case = 1; test_case <= T; test_case++){
            curScore = 0;
            if(run())
                curScore = 100;

            System.out.println("#" + test_case + " " + curScore);
            totalScore += curScore;
        }
        System.out.println("Total Score = " + totalScore/T);
        sc.close();
    }

    private static boolean run() {
        int init_stringsize, scale, accepted = 0;
        int input_cnt = 0; 
        int ans_cnt; 
        int userLen = 0;
        seed = sc.nextInt();
        init_stringsize = sc.nextInt();

        for (int i = 0; i < 8; i++) {
            status[i] = sc.nextInt();
            if (i < 4) 
                input_cnt += status[i] * 2;
            else if (i < 7)
                input_cnt += status[i];
        }
        scale = sc.nextInt();
        ans_cnt = status[GET];
        makeInsertStr(init_stringsize, init_str);
        user.init(init_stringsize, init_str);
        userLen = init_stringsize;

        makeComList();

        for (int i = 0; i < input_cnt; i++){
            int comm = commandList[i], insert_strlen, left, right;
            switch (comm) {
                case INSERT:
                    insert_strlen = pseudo_rand() % status[LEN] + 1;    
                    makeInsertStr(insert_strlen, init_str);
                    userLen = user.insert_string(insert_strlen, insert_str);
                    break;
                case SELECT:
                    left = pseudo_rand() % userLen * (scale / 100);
                    right = left + pseudo_rand() % status[LEN];
                    user.select_string(left,right);
                    break;
                case COPY:
                    user.copy_string(); 
                    break;
                case CUT:
                    userLen = user.cut_string();
                    break;
                case PASTED:
                    userLen = user.paste_string();
                    break;
                case GETSUBSTRING:
                    left = pseudo_rand() % (userLen - 2);
                    right = left + pseudo_rand() % 10;

                    user.get_substring(left, right, res_str);

                    String t = sc.next();
                    int tlen = t.length();
                    for (int k=0; k < tlen; k++)
                        ans_str[k] = t.charAt(k);

                    ans_str[tlen] = '\0';

                    if (mystrcmp(res_str, ans_str) == 0)
                        accepted++;
                    break;
            
                default:
                    break;
            }

        }

        return (accepted == ans_cnt);

    }

    private static int pseudo_rand(){
        seed = seed * 431345 + 2531999;
        return seed & 0x7FFFFFFF; 
    }

    private static void makeInsertStr(int M, char[] str){

        int i = 0;
        for (i = 0; i < M; i++){
            int val = pseudo_rand() % 36;

            if (val < 26)
                str[i] = (char)(val + 'a');
            else
                str[i] = (char)(val - 26 + '0');
        }
        str[i] = '\0';
    }


    private static int mystrcmp(char[] s1, char[] s2){
        for (int i = 0; i < 30; i++){
            if (s1[i] != s2[i])
                break;

            if (s1[i] == '\0')
                return 0;
        }

        return 1;
    }

    private static void makeComList(){
        int tot = 0, listsize = 0;
        for (int i = 0; i < 7; i++){
            tot += status[i];
        }
        for (int i = tot; i > 0; i--){
            int val = pseudo_rand() % i;
            int pos = 0;
            val -= status[pos];
            while (val > 0 || status[pos] == 0){
                pos++;
                val -= status[pos];
                if (pos < 0)
                    break;
            }
            
            switch (pos) {
                case SCP:
                    commandList[listsize++] = SELECT;
                    commandList[listsize++] = COPY;
                    break;
                case SCU:
                    commandList[listsize++] = SELECT;
                    commandList[listsize++] = CUT;
                    break;
                case SIN:
                    commandList[listsize++] = SELECT;
                    commandList[listsize++] = INSERT;
                    break;
                case SPA:
                    commandList[listsize++] = SELECT;
                    commandList[listsize++] = PASTED;
                    break;
                case PAS:
                    commandList[listsize++] = PASTED;
                    break;
                case INS:
                    commandList[listsize++] = INSERT;
                    break;
                case GET:
                    commandList[listsize++] = GETSUBSTRING;
                    break;
                default:
                    break;
            }
            status[pos]--;
        }
    }

}