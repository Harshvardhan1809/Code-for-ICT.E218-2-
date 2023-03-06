// Kedare Harshvardhan 21B11198

// プログラミング言語: C 
// コンパイル方法: gcc 21_11198_block_huffman.c -o 21_11198_block_huffman
// 実行方法: ./21_11198_block_huffman

// Basically combining iid and huffman 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define CODE_SIZE 128
#define MAX_NODES 256
#define BUFFER_SIZE 1024
#define STRING_SIZE 1024
#define FILENAME 100

// 自己参照構造体（木構造を作成する）
typedef struct node_t
{
  struct node_t *left, *right; // 左右の子
  double prob;                 // 節点の確率
  int symbol;                  // 記号の番号
  int leaf;                    // 葉かどうかのフラグ
} *node;                       // node_tのポインタをnodeと名付ける

// node_t型のpoolをMAX_NODES個作製して初期化。符号の木の節点数が最大MAX_NODES

struct node_t pool[MAX_NODES] = {{0}};
node qqq[MAX_NODES - 1], *q = qqq - 1; // ヒープを実現する配列
int n_nodes = 0;             // ハフマン木の節点数
int qend = 1;                // ヒープの節点数
// char *code[CODE_SIZE] = {0}; // 符号語の集合
char code[CODE_SIZE][CODE_SIZE] = {{0}};
char buf[BUFFER_SIZE];       // 符号語の書き込みに利用する記憶領域
int dummy;
int dummy_symbol;
node dummy_node;

int asize; 
int length = 3; 
int *char_counter; // 符号語の各位置に相当する記号の決め方
double *probability;
char *symb;

int misc = 0; // 雑な処理に役たつ任意の変数
double probs = 1; // 各符号の確率を表す任意の変数

char *new_symb[3]; //ブロック符号語の記号
double *new_prob; // ブロック符号語の確率
int new_misc = 0; // 雑な処理に役立つ変数

int abstract_recursive_function(int count){ // 再帰関数を用いる

    if(count == 0){
        abstract_recursive_function(count+1); 
    }

    if(count < length & count !=0){
        for(int i = 0; i<asize; i++){
            char_counter[count-1] = i; 
            abstract_recursive_function(count+1); 
        }
       char_counter[count-1] = 0; 
    }

    if(count == length){ // here, we permute the last digit asize times and get all combinations for last digit
        for(int k=0; k<asize; k++){
            // printf("P(");
            for(int j = 0; j < length-1; j++){
                new_symb[j][new_misc] = symb[char_counter[j % asize]]; 
                //printf("%c", symb[char_counter[j % asize]]);
                probs *= probability[char_counter[j % asize]];
            }
            misc = k;  
            new_symb[2][new_misc] = symb[k]; // 符号語を配列に入れ終わった
            probs *= probability[k]; 
           // printf("%c): %f \n", symb[k], probs);
            new_prob[new_misc] = probs; 
            new_misc++; // 次の符号語に移る
            probs = 1; 
        }
        char_counter[misc] = 0; 
    }

    return 0; 
}


node new_node(double prob, int symbol, int leaf, node a, node b)
{
  node n = pool + n_nodes++; // poolから一つ節点を新規割当

  // 葉であればnを葉にするための処理を行う
  if (leaf == 1)
  {
    // ???
    n->leaf = 1;
    n->prob = prob;
    n->symbol = symbol;
    n->left = NULL;
    n->right = NULL;
  }
  else
  {
    // ???
    n->prob = prob;
    n->symbol = symbol;
    n->leaf = 0;
    n->left = a;
    n->right = b;
  }
  return n;
}

void qinsert(node n) // qinsertするときは最後の位置に追加する、常に小さい順に並んでいるので
{
  int j, i = qend++;

    while ((j = i / 2)){  // 
        if (q[i - 1]->prob >= n->prob) {
            q[i] = q[i - 1];
            i--;
        }
        else { 
            break;
        }
    }
  q[i] = n; // 検索した位置にnを追加  // new members get added here
}

node qremove() // 常に小さい順に並んでいるので、取り出すのでは最初の位置からだ。
{
  int i, l;
  node n = q[i = 1]; //根の取り出し
  node p; 
 
  if (qend < 2) return NULL;
  qend--;
    while (1) { 
        if (i-1 > qend)break;
        q[i] = q[i+1];
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
    if(n->prob != 1.0)len++;
    if (n->leaf==1) {
        for (int i = 0; i < len; i++) {
            code[n->symbol][i] = buf[i];
        }
        code[n->symbol][len] = '\0';
    }
    else {
            buf[len] = '1';
            build_code(n->left, len);
            buf[len] = '0';
            build_code(n->right, len);
    }
}
 
// ハフマン木の作成 
void init(int new_asize, double *new_prob)
{
  int i;
  char c[MAX_NODES];
  for (i = 0; i < new_asize; i++)
    qinsert(new_node(new_prob[i], i+1, 1, 0, 0));

  int sym_counter = 0;

  while (qend != 1)
  {

    node a = qremove(); // 最小の要素を2回取り出して新しいノードを作る。
    node b = qremove();
    qinsert(new_node((a->prob + b->prob), new_asize+(++sym_counter), 0, a, b)); 
    if(qend == 2) break; 
  }

  qend = 1; 

  build_code(q[1], 0);
}

void encode(int enc_rows) 
{
  char ss[STRING_SIZE] = {0};
  printf("symbols> ");
  scanf("%s", ss);

  int len_ss = strlen(ss); 
  char sample_ss[3] = "000"; 
  printf("endcoded: "); 
  char code_for_ss[3] = "000"; 
  int i = -1; 

  while(i<len_ss){ // loop till we reach end of symbols
    new_misc = 0; 
    sample_ss[0] = ss[++i]; sample_ss[1] = ss[++i]; sample_ss[2] = ss[++i]; // copy three elements in an array
    while(new_misc < enc_rows){ // loop through all the new_symb to compare with 
      code_for_ss[0] = new_symb[0][new_misc]; code_for_ss[1] = new_symb[1][new_misc]; code_for_ss[2] = new_symb[2][new_misc]; 
      if(strcmp(sample_ss, code_for_ss) == 0) {
        printf("%s", code[new_misc+1]); 
        break; 
      }
      else new_misc++; 
    }
  }
  printf("\n"); 
}

int main()
{
  printf("alphabet size> ");
  scanf("%d", &asize);
 
  // 動的に配列や数列にメモリーを割り当てる
  int rows = asize*asize*asize; 
  symb = (char *)malloc(asize*sizeof(char)); 
  probability = (double *)malloc(asize*sizeof(double)); 
  char_counter = (int *)malloc(asize*sizeof(int)); 
  for(int i=0; i<rows; i++){   // create the array for new symbol 
    new_symb[i] = (char *)malloc(rows*sizeof(char)); 
  }
  new_prob = (double *)malloc(rows*sizeof(double));  

  for (int i = 0; i < asize; i++)
  {
      char_counter[i] = 0; 
      printf("symbol_%d> ", i + 1);
      scanf("%s", &symb[i]);
      printf("p_%d> ", i + 1);
      scanf("%lf", &probability[i]);  
  }

  abstract_recursive_function(0); 

  // ハフマン木の作成
  init(asize*asize*asize, new_prob);
  // codewords
  for (int i = 0; i < rows; i++){

    if (code[i]){
      printf("cw for ");
      for(int j=0;j<3; j++){
        printf("%c", new_symb[j][i]);
      } 
      printf(": %s\n", code[i+1]);
    }
  }
      
  float entropy = 0;
  float average_length = 0;
  for (int i = 0; i < rows; i++){
        average_length += new_prob[i] * (int)strlen(code[i]);
        entropy += -new_prob[i] * (log10(new_prob[i]) / (0.3010));
  }

  // code upto this point works well 
  int printer = (int) strlen(code[0]);
  for(int i=0; i<printer; i++){
    printf("%s \n", code[i]); 
  } 

  // entropy
  printf("entropy: %lf\n", entropy);

  // average length
  printf("average length: %lf\n", average_length);

  encode(rows);

  //decoded
  char cdws[STRING_SIZE] = {0};
  printf("codewords> ");
  scanf("%s", cdws);
  printf("decoded: ");
    
  int len_cdws = strlen(cdws); 
  char code_comparer[STRING_SIZE]; 
  int j=0;

  while(j < len_cdws){
    for(int i=1; i<rows+1; i++){ 
      if (strncmp(&cdws[j], code[i], (int)strlen(code[i])) == 0){
        printf("%c%c%c", new_symb[0][i-1], new_symb[1][i-1], new_symb[2][i-1]); 
        j += (int) strlen(code[i]); 
        i = rows;
      }
    }
  }
  printf("\n"); 

  free(probability); // メモリーの解放
  free(symb);  
  for(int i=0; i<3; i++){   
    free(new_symb[i]); 
  }
  free(new_prob); 
  free(char_counter); 


  return 0;
}

// 実行結果
// cw for aaa: 101000101
// cw for aab: 10100011
// cw for aac: 011110010
// cw for aad: 00011011
// cw for aba: 000110100
// cw for abb: 0100000
// cw for abc: 00011000
// cw for abd: 111111
// cw for aca: 011110011
// cw for acb: 1111100
// cw for acc: 10100000
// cw for acd: 0100011
// cw for ada: 10100001
// cw for adb: 101001
// cw for adc: 0111010
// cw for add: 100100
// cw for baa: 000110101
// cw for bab: 0100001
// cw for bac: 00011001
// cw for bad: 111100
// cw for bba: 0100010
// cw for bbb: 10011
// cw for bbc: 000111
// cw for bbd: 00110
// cw for bca: 1100010
// cw for bcb: 001010
// cw for bcc: 111101
// cw for bcd: 000100
// cw for bda: 110010
// cw for bdb: 00111
// cw for bdc: 000101
// cw for bdd: 00001
// cw for caa: 101000100
// cw for cab: 1111101
// cw for cac: 01111010
// cw for cad: 0111011
// cw for cba: 1100011
// cw for cbb: 001011
// cw for cbc: 110011
// cw for cbd: 11010
// cw for cca: 01111011
// cw for ccb: 0000010
// cw for ccc: 0111000
// cw for ccd: 100101
// cw for cda: 0111001
// cw for cdb: 10101
// cw for cdc: 011010
// cw for cdd: 01001
// cw for daa: 01111000
// cw for dab: 0000011
// cw for dac: 0111110
// cw for dad: 011011
// cw for dba: 110000
// cw for dbb: 00100
// cw for dbc: 11011
// cw for dbd: 1110
// cw for dca: 0111111
// cw for dcb: 000000
// cw for dcc: 011000
// cw for dcd: 10000
// cw for dda: 011001
// cw for ddb: 1011
// cw for ddc: 10001
// cw for ddd: 0101
// entropy: 5.539870
// average length: 6.166000
// symbols> abcdaabbccdd
// endcoded: 000110000111100000011101001
// codewords> 000110000111100000011101001
// decoded: abcdaabbccdd