// Kedare Harshvardhan 21B11198

// プログラミング言語: C 
// コンパイル方法: gcc 21_11198_huffman.c -o 21_11198_huffman
// 実行方法: ./21_11198_huffman

// ファイルを指定して実行したい場合に
// 実行方法: ./21_11198_huffman n_array_huffman.txt

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

double *prob;
char *symb; 

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
void init(int asize, double *prob)
{

  int i;
  char c[MAX_NODES];
  for (i = 0; i < asize; i++)
    qinsert(new_node(prob[i], i+1, 1, 0, 0));

  int sym_counter = 0;

  while (qend != 1)
  {

    node a = qremove(); // 最小の要素を2回取り出して新しいノードを作る。
    node b = qremove();
    qinsert(new_node((a->prob + b->prob), asize+(++sym_counter), 0, a, b)); 
    if(qend == 2) break; 
  }

  qend = 1; 

  build_code(q[1], 0);
}

void encode(char *s, int *isymb, char *out) 
{
  int i = 0; 
  while (i != strlen(s))
  {
    strcat(out,code[isymb[s[i++]]+1]); 
  }
}

void decode(char *symb, char *c, node t)
{
  node n = t;
  int i = 0; 

  char zero_char[] = {'0','\0'}; 
  char one_char[] = {'1','\0'}; 

  while (i < strlen(c))
  { 
    while(n->leaf != 1) {
      if(c[i] == '1'){
        n = n->left; 
        i++; 
      }
      else if(c[i] == '0'){
        n = n->right; 
        i++; 
      }
    }

    if(n->leaf == 1){
      printf("%c", symb[n->symbol-1]); 
      // i = 0; 
    }
    
    n = t; 
  }

}

int main(int argc, char *argv[]) //ファイルから入力を求めることができる
{
  int asize;
  // double prob[asize]; 
  // char symb[asize];  

  FILE *file;  
  char filename[FILENAME]; 

  if(argc < 2){
    printf("alphabet size> ");
    scanf("%d", &asize);
    prob = (double *)malloc(asize*sizeof(double)); 
    symb = (char *)malloc(asize*sizeof(char)); 
    for (int i = 0; i < asize; i++)
    {
      printf("symbol_%d> ", i + 1);
      scanf("%s", &symb[i]);
      printf("p_%d> ", i + 1);
      scanf("%lf", &prob[i]);
    }
  } else if(argc >= 2){
  
   
    // open file
    printf("%s \n", argv[1]);
    strcpy(filename, argv[1]); 
    file = fopen(filename, "r"); 

    // read asize
    // fscanf(file, "%d", &asize);
    char probs[12];
    fgets(probs, 12, file); 
    asize = atoi(probs); 
    printf("%d \n", asize);  

    prob = (double *)malloc(asize*sizeof(double)); 
    symb = (char *)malloc(asize*sizeof(char));  

    // helpers 
    int p = 0; 
    char *ptr; 

    // start reading 
    while(fgets(probs, 12, file) != NULL){
      // read the symbols
      if(p%2 == 0) {
        symb[p++/2] = probs[0];
      }  
      // read the probabilities
      else if(p%2 != 0) { 
        prob[p++/2] = strtod(probs, &ptr);
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
      printf("cw for %c: %s\n", symb[i], code[i+1]);

    for (int i = 0; i < asize; i++)
    {
        average_length += prob[i] * (int)strlen(code[i+1]);
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

  //decoded
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
// cw for a: 000
// cw for b: 001
// cw for c: 010
// cw for d: 100
// cw for e: 101
// cw for f: 111
// cw for g: 0110
// cw for h: 1100
// cw for i: 01110
// cw for j: 01111
// cw for k: 11011
// cw for l: 110101
// cw for m: 1101001
// cw for n: 11010000
// cw for o: 110100011
// cw for p: 1101000100
// cw for q: 11010001010
// cw for r: 110100010111
// cw for s: 11010001011000
// cw for t: 11010001011001
// cw for u: 11010001011010
// cw for v: 110100010110110
// cw for w: 1101000101101110
// cw for x: 11010001011011111
// cw for y: 110100010110111100
// cw for z: 1101000101101111010
// cw for _: 1101000101101111011
// entropy: 3.367406
// average length: 3.397289
// symbols> symbols_are_symbols
// encoded: 110100010110001101000101101111001101001001110100011110101110100010110001101000101101111011000110100010111101110100010110111101111010001011000110100010110111100110100100111010001111010111010001011000
// codewords> 110100010110001101000101101111001101001001110100011110101110100010110001101000101101111011000110100010111101110100010110111101111010001011000110100010110111100110100100111010001111010111010001011000
// decoded: symbols_are_symbols 