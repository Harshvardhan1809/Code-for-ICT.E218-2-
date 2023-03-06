// Kedare Harshvardhan 21B11198

// プログラミング言語: C 
// コンパイル方法: gcc 21_11198_kraft.c -o 21_11198_kraft
// 実行方法: ./21_11198_kraft

#include <stdio.h>
#include <string.h>

#define CODE_SIZE 128
#define MAX_NODES 256
#define BUFFER_SIZE 1024
#define MAX_VAL 100

typedef struct node_t
{
    struct node_t *up, *left, *right; // 親と左右の子
    int visit;                        // 訪問済みかどうかのフラグ // default value is zero
    int symbol;                       // 記号の番号
    int leaf;                         // 葉かどうかのフラグ
} *node;                              // node_tのポインタをnodeと名付ける

// node_t型のpoolをMAX_NODES個作製して初期化。符号の木の節点数が最大MAX_NODES
struct node_t pool[MAX_NODES] = {{0}};
int n_nodes = 1;                    // 符号の木の節点数
char buf[BUFFER_SIZE][BUFFER_SIZE]; // 符号語の書き込みに利用する記憶領域

char code[CODE_SIZE][CODE_SIZE];

void make_tree(int asize, int *l)
{
    node t; // 符号の木
    for (int i = 0; i < asize; i++)
    {
        t = pool; // poolの先頭アドレスの節点を根とする
        for (int j = 0; j < l[i]; j++)
        {
            // 左の子がない場合は左右の子が無いのでそれらに新規節点を割当
            if (t->left == 0)
            {
                t->left = pool + n_nodes++;  //  左の子のアドレスを保存する
                t->left->up = t;             // 　左の子の親を自分自身にする
                t->right = pool + n_nodes++; //  右の子のアドレスを保存する
                t->right->up = t;
            }

            // i番目の符号語の割当において左の子を訪問していない場合
            if (t->left->visit != asize + i && t->left->leaf != 1) // asizeは任意に選ばれている // フラグが同じなら、その先の符号語を訪問できない
            {                                                      // visitは何番目に訪れられたかを見る
                t->left->visit = asize + i;
                t->left->symbol = 0;
                buf[i][j] = '0';
                if (j == l[i] - 1)
                {
                    t->left->leaf = 1;
                    buf[i][j + 1] = '\0'; 
                    break;
                }
                else
                    t = t->left;
            }

            // i番目の符号語の割当において右の子を訪問していない場合
            else if (t->right->visit != asize + i && t->right->leaf != 1)
            {
                t->right->visit = asize + i;
                t->right->symbol = 1;
                buf[i][j] = '1';
                if (j == l[i] - 1)
                {
                    t->right->leaf = 1;
                    buf[i][j + 1] = '\0';
                    break;
                }
                else
                    t = t->right;
            }
            // 左右の子に訪問していた場合
            else
            {
                t = t->up;
                j = j - 2;
            }
        }
    }
}

int key = 0;
void build_code(node n, char *c, int len)
{
    if (n->leaf)
    {
        for (int i = 0; i < len; i++)
        {
            code[key][i] = buf[key][i];
        }
        key++;
    }
    else
    {
        if (n->left->visit != 0)
            build_code(n->left, c, ++len);
        if (n->right->visit != 0)
            build_code(n->right, c, ++len);
    }
}

int main(void)
{
    int asize;
    printf("alphabet size> ");
    scanf("%d", &asize);

    int l[asize];
    for (int i = 0; i < asize; i++)
    {
        printf("l_%d> ", i + 1);
        scanf("%d", &l[i]);
    }

    // 符号の木の作成
    make_tree(asize, l);

    // 符号語の作成
    char c[MAX_NODES] = {0}; // 1つの符号語の長さは節点数の最大値としておく
    //
    build_code(pool, c, 0); // poolは符号の木の根のアドレスを表す
    for (int i = 0; i < asize; i++)
    {
        printf("cw for l_%d: %s\n", i + 1, code[i]); // code[i]にi番目の符号語が入っている
    }
    return 0;
}


// alphabet␣size>␣6
// l_1>␣1
// l_2>␣2
// l_3>␣4
// l_4>␣8
// l_5>␣16
// l_6>␣32
// <?>

// 実行結果
// cw for l_1: 0
// cw for l_2: 10
// cw for l_3: 1100
// cw for l_4: 11010000
// cw for l_5: 1101000100000000
// cw for l_6: 11010001000000010000000000000000