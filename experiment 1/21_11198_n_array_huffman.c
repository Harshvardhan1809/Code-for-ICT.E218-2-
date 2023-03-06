// Kedare Harshvardhan 21B11198

// プログラミング言語: C
// コンパイル方法: gcc 21_11198_n_array_huffman.c -o 21_11198_n_array_huffman
// 実行方法: ./21_11198_n_array_huffman

// ファイルを指定して実行したい場合に
// 実行方法: ./21_11198_huffman n_array_huffman.txt

// 普段のハフマン符号との主な違い、左右の子ノードの代わりに任意の数の数の子ノードを持つように構造体を変更した
// また、新たなノードを作るために、三つのノードをまとめる必要がある。要するに、qremove()を3回も動かしてノードを求める必要がある。
// children[]という配列のインデックスによって、ノードの値が0、1または2になる。

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define ASCII 128
#define CODE_SIZE 128
#define MAX_NODES 256
#define BUFFER_SIZE 1024
#define STRING_SIZE 1024
#define FILENAME 100

// 自己参照構造体（木構造を作成する）
typedef struct node_t
{
    // struct node_t *left, *right; // 左右の子
    struct node_t *children[ASCII];
    double prob;                 // 節点の確率
    int symbol;                  // 記号の番号
    int leaf;                    // 葉かどうかのフラグ
} *node;                         // node_tのポインタをnodeと名付ける

// node_t型のpoolをMAX_NODES個作製して初期化。符号の木の節点数が最大MAX_NODES

struct node_t pool[MAX_NODES] = {{0}};
node qqq[MAX_NODES - 1], *q = qqq - 1; // ヒープを実現する配列
int n_nodes = 0;                       // ハフマン木の節点数
int qend = 1;                          // ヒープの節点数


char code[CODE_SIZE][CODE_SIZE] = {{0}};
char buf[BUFFER_SIZE]; // 符号語の書き込みに利用する記憶領域

// To intialize the children of a new node 
struct node_t null_children[ASCII] = {{NULL}};
node null_children_node = null_children;

double *prob;
char *symb;

node new_node(double prob, int symbol, int leaf, node *a)
{
    node n = pool + n_nodes++; // poolから一つ節点を新規割当

    // 葉であればnを葉にするための処理を行う
    if (leaf == 1)
    {
        // ???
        n->leaf = 1;
        n->prob = prob;
        n->symbol = symbol;
        for(int i=0; i<ASCII; i++){
            n->children[i] = a[i]; 
        }
    }
    else
    {
        // ???
        n->prob = prob;
        n->symbol = symbol;
        n->leaf = 0;
        for(int i=0; i<ASCII; i++){
            n->children[i] = a[i]; 
        }
    }

    return n;
}

void qinsert(node n) // qinsertするときは最後の位置に追加する、常に小さい順に並んでいるので
{
    int j, i = qend++;

    while ((j = i / 2))
    { //
        if (q[i - 1]->prob >= n->prob)
        {
            q[i] = q[i - 1];
            i--;
        }
        else
        {
            break;
        }
    }
    q[i] = n; // 検索した位置にnを追加  // new members get added here

}

node qremove() // 常に小さい順に並んでいるので、取り出すのでは最初の位置からだ。
{
    int i, l;
    node n = q[i = 1]; // 根の取り出し
    node p;

    if (qend < 2)
        return NULL;
    qend--;
    while (1)
    {
        if (i - 1 > qend)
            break;
        q[i] = q[i + 1];
        i++;
    }
    q[i] = NULL;
    return n;
}

int key = 0;
int codes = 0;
int iter = 0;

void build_code(node n, int len)
{
    // build code goes in infnite loop 

    if (n->prob != 1.0)
        len++;
    if (n->leaf == 1)
    {
        for (int i = 0; i < len; i++)
        {
            code[n->symbol][i] = buf[i];
        }
        code[n->symbol][len] = '\0';
    }
    else
    {
        buf[len] = '2';
        if(n->children[2]->symbol != -1) build_code(n->children[2], len);
        buf[len] = '1';
        if(n->children[1]->symbol != -1) build_code(n->children[1], len);
        //build_code(n->left, len);
        buf[len] = '0';
        if(n->children[0]->symbol != -1) build_code(n->children[0], len);
        //build_code(n->right, len);
    }

}


// ハフマン木の作成

int init(int asize, double *prob)
{
    // Making a k-ary tree

    int i;
    char c[MAX_NODES];

    // we qinsert a new node 

    for (i = 0; i < asize; i++)
        qinsert(new_node(prob[i], i + 1, 1, &null_children_node)); 

    int sym_counter = 0;

    while (qend != 1)
    {

        // we need three nodes to make a new node
        // add dummy nodes just in case the number of nodes remaining are less than two 

        node abc[3] = {qremove(), qremove(), qremove()}; 

        for(int i=0; i<3; i++){
            if(abc[i] == NULL){
                printf("making dummy node \n"); 
                abc[i] = new_node(0, -1, 1, &null_children_node);
            }
        }

        qinsert(new_node((abc[0]->prob + abc[1]->prob + abc[2]->prob), asize + (++sym_counter), 0, abc));
        if (qend == 2)
            break;
    }

    qend = 1;

    build_code(q[1], 0);

    return 0; 
}

void encode(char *s, int *isymb, char *out)
{
    int i = 0;
    while (i != strlen(s))
    {
        strcat(out, code[isymb[s[i++]] + 1]);
    }
}

void decode(char *symb, char *c, node t)
{
    node n = t;
    int i = 0;

    char zero_char[] = {'0', '\0'};
    char one_char[] = {'1', '\0'};
    char two_char[] = {'2', '\0'};

    while (i < strlen(c))
    {
        while (n->leaf != 1)
        {

            if (c[i] == '2'){ n = n->children[2]; i++; }
            else if (c[i] == '1'){ n = n->children[1]; i++; }
            else if (c[i] == '0'){ n = n->children[0]; i++; }
        }

        if (n->leaf == 1 && n->symbol > 0)
        {
            printf("%c", symb[n->symbol - 1]);
            // i = 0;
        }

        n = t;
    }
}

int main(int argc, char *argv[]) // ファイルから入力を求めることができる
{
    int asize;
    // double prob[asize];
    // char symb[asize];

    FILE *file;
    char filename[FILENAME];

    if (argc < 2)
    {
        printf("alphabet size> ");
        scanf("%d", &asize);
        prob = (double *)malloc(asize * sizeof(double));
        symb = (char *)malloc(asize * sizeof(char));
        for (int i = 0; i < asize; i++)
        {
            printf("symbol_%d> ", i + 1);
            scanf("%s", &symb[i]);
            printf("p_%d> ", i + 1);
            scanf("%lf", &prob[i]);
        }
    }
    else if (argc >= 2)
    {

        // open file
        printf("%s \n", argv[1]);
        strcpy(filename, argv[1]);
        file = fopen(filename, "r");

        // read asize
        char probs[12];
        fgets(probs, 12, file);
        asize = atoi(probs);
        printf("%d \n", asize);

        prob = (double *)malloc(asize * sizeof(double));
        symb = (char *)malloc(asize * sizeof(char));

        // helpers
        int p = 0;
        char *ptr;

        // start reading
        while (fgets(probs, 12, file) != NULL)
        {
            // read the symbols
            if (p % 2 == 0)
            {
                symb[p++ / 2] = probs[0];
            }
            // read the probabilities
            else if (p % 2 != 0)
            {
                prob[p++ / 2] = strtod(probs, &ptr);
            }
        }
        fclose(file);
    }

    // ハフマン木の作成

    init(asize, prob);

    float entropy = 0;
    float average_length = 0;

    // codewords
    for (int i = 0; i < asize; i++)
        if (code[i])
            printf("cw for %c: %s\n", symb[i], code[i + 1]);

    for (int i = 0; i < asize; i++)
    {
        average_length += prob[i] * (int)strlen(code[i + 1]);
        entropy += -prob[i] * (log10(prob[i]) / (0.3010));
    }

    // entropy
    printf("entropy: %lf\n", entropy);

    // average length
    printf("average length: %lf\n", average_length);

    // // 記号の逆引き配列を作成すると便利
    int isymb[128] = {0}; // ASCII文字を仮定する
    for (int i = 0; i < asize; i++)
        isymb[symb[i]] = i;

    // encoded
    char ss[STRING_SIZE] = {0};
    printf("symbols> ");
    scanf("%s", ss);
    char out[STRING_SIZE] = {0};
    encode(ss, isymb, out);
    printf("encoded: %s\n", out);

    // decoded
    char cdws[STRING_SIZE] = {0};
    printf("codewords> ");
    scanf("%s", cdws);
    printf("decoded: ");
    decode(symb, cdws, q[1]);

    free(prob);
    free(symb);

    return 0;
}

// 実行結果
// n_array_huffman.txt 
// 27 
// cw for a: 22
// cw for b: 21
// cw for c: 20
// cw for d: 12
// cw for e: 10
// cw for f: 02
// cw for g: 01
// cw for h: 00
// cw for i: 111
// cw for j: 110
// cw for k: 1122
// cw for l: 1120
// cw for m: 11212
// cw for n: 11211
// cw for o: 112102
// cw for p: 112101
// cw for q: 1121002
// cw for r: 1121000
// cw for s: 11210011
// cw for t: 11210010
// cw for u: 112100122
// cw for v: 112100121
// cw for w: 1121001202
// cw for x: 1121001200
// cw for y: 11210012012
// cw for z: 11210012011
// cw for _: 11210012010

// entropy: 3.367406

// average length: 2.170445

// symbols> symbols_are_symbols

// encoded: 1121001111210012012112122111210211201121001111210012010221121000101121001201011210011112100120121121221112102112011210011

// codewords> 1121001111210012012112122111210211201121001111210012010221121000101121001201011210011112100120121121221112102112011210011

// decoded: symbols_are_symbols　