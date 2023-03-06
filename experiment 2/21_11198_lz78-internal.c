// Kedare Harshvardhan 21B11198

// プログラミング言語: C
// コンパイル方法: gcc 21_11198_lz78-internal.c -o 21_11198_lz78-internal
// 実行方法: ./21_11198_lz78-internal

// Basically combining iid and huffman

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>

#define ASCII 128
#define MAX_NODES 256
#define BUFFER_SIZE 1024
#define STRING_SIZE 1024
#define FILENAME 100
#define CHILD_SIZE 124
#define NULL __DARWIN_NULL

// 自己参照構造体（木構造を作成する）

typedef struct node_t
{
    struct node_t *parent;          // Only has one parent
    struct node_t *children[ASCII]; // since symbols are in ASCII format, at most 128 children can exist
    int node_number;                // 節点番号
    char symbol;                    // 記号の番号 has a symbol to it the ascii can be turned into binary
                                    // int leaf;                   // whether its has something ahead of it can be omitted by checking children
} *node;

// node_t型のpoolをMAX_NODES個作製して初期化。符号の木の節点数が最大MAX_NODES
// since node is a pointer to a node_t struct, created an array just to reference some memories

struct node_t pool[MAX_NODES] = {{0}};
node qqq[MAX_NODES - 1], *q = qqq - 1; // ヒープを実現する配列
int n_nodes = 0;                       // ハフマン木の節点数
int qend = 1;                          // ヒープの節点数

// global variables

char symb[STRING_SIZE];
int asize;
int node_counter = 0;
struct node_t null_children[ASCII] = {{NULL}};
node null_children_node = null_children;
int ptr;

node root;

int elements_in_children(node a)
{
    int i = 0;
    while (a->children[i] != NULL)
        i++;

    return i;
}

node create_node(node parent, node *child, int number, char symbol)
{

    node new_node = pool + n_nodes++;
    new_node->parent = parent;
    for (int i = 0; i < ASCII; i++)
        new_node->children[i] = child[i];
    new_node->node_number = node_counter++;
    new_node->symbol = symbol;

    return new_node;
}

bool match_found_bool;

int match_found(node match)
{

    // goal of this function: traverse down the match found and add node appropriately
    // move to the next symbol in the 記号列
    // write in a recursion friendly manner

    int k = 0;
    ptr++;

    match_found_bool = false;

    while (match->children[k] != NULL)
    {

        if (match->children[k]->symbol == symb[ptr])
        {

            // recursion can be made in this way

            if (match_found(match->children[k]) == 1)
                return 0;
            match_found_bool = true;
        }
        k++;
    }

    if (!match_found_bool)
    {
        int num_elements = elements_in_children(match);
        match->children[num_elements] = create_node(match, &null_children_node, node_counter, symb[ptr]);
        printf("(%d, ", match->node_number);
        printf("%c) ", match->children[num_elements]->symbol);

        return 1;
    }

    return 0;
}

void dict_construct()
{

    // 根を作る
    root = create_node(NULL, &null_children_node, 0, -1);
    // add as  a first level node

    // take input from the string and add nodes
    bool match_found_bool = false;
    while (ptr < strlen(symb))
    {

        int j = 0;

        // assume input to be ababbababaa
        // search dictionary if "a" exists
        // traverse the children of root

        while (root->children[j] != NULL && ptr < strlen(symb))
        {

            // search if an element has matching symbol

            if (root->children[j]->symbol == symb[ptr])
            {

                // if we find a match, complicated processing
                // traverse the children of root to find the element of the 記号列

                match_found(root->children[j]);
                ptr++;
                j = -1;
                match_found_bool = true;
            }
            j++;
        }

        if (!match_found_bool && ptr < strlen(symb))
        {

            // no match so add a new node in children of root

            int num_elements = elements_in_children(root);
            root->children[num_elements] = create_node(root, &null_children_node, node_counter, symb[ptr]);
            printf("(%d, ", root->node_number);
            printf("%c) ", root->children[num_elements]->symbol);
        }

        ptr++;
    }
}

int decoder(node n)
{

    char str[100];
    int i = 0;
    while (n->parent != NULL)
    {
        str[i++] = n->symbol;
        n = n->parent;
    }

    for (int j = strlen(str) - 1; j >= 0; j--)
        printf("%c", str[j]);

    return 0;
}

void decode_function(int *node_val, char *symb_val){

    int len = 0;
    int traverse = 0;
    int val;

    while (len < asize)
    {
        traverse = 0; val = 0; 

        while (traverse < n_nodes)
        {

            if (pool[traverse].node_number == node_val[len])
            {

                // found the struct which has the node_number
                // now we go through its children for matching the symbol

                val = 0;
                while (val < elements_in_children(&pool[traverse]))
                {

                    if (pool[traverse].children[val]->symbol == symb_val[len])
                    {

                        // found the child and now we decode and print

                        node a = pool[traverse].children[val];
                        decoder(a);

                        // ending the val loop, arbitray values to leave the loop 
                        val = 128; traverse = 128; 
                    }
                    val++;
                }
                //
            }
            traverse++;
        }
        len++;
    }

}

int main()
{

    printf("symbols> ");
    scanf("%s", symb);

    // make the dictionary
    printf("encoded: ");
    dict_construct();

    // take length for decoding
    printf("\ncodewords length> ");
    scanf("%d", &asize);
    int node_val[asize];
    char symb_val[asize];

    for (int i = 0; i < asize; i++)
    {
        printf("node: ");
        scanf("%d", &node_val[i]);
        printf("symbol: ");
        scanf("%s", &symb_val[i]);
    }
    printf("decoded: ");   
    decode_function(node_val, symb_val);  
    printf("\n"); 

    return 0;
}

// 実行結果
// symbols> LZ77_and_LZ78_are_the_two_lossless_data_compression_algorithms_published_in_papers_by_Abraham_Lempel_and_Jacob_Ziv_in_1977_and_1978._They_are_also_known_as_LZ1_and_LZ2_respectively._These_two_algorithms_form_the_basis_for_many_variations_including_LZW,_LZSS,_LZMA_and_others._Besides_their_academic_influence,_these_algorithms_formed_the_basis_of_several_ubiquitous_compression_schemes,_including_GIF_and_the_DEFLATE_algorithm_used_in_PNG_and_ZIP.

// encoded: (0, L) (0, Z) (0, 7) (3, _) (1, Z) (3, 8) (1, e) (2, i) (3, 7) (6, .) (5, 1) (5, 2) (5, W) (5, S) (5, M) (1, A) (2, I) 
// codewords length> 10
// node: 0
// symbol: L
// node: 0
// symbol: Z
// node: 0
// symbol: 7
// node: 3
// symbol: _
// node: 0
// symbol: a
// node: 0
// symbol: n
// node: 0
// symbol: d
// node: 0
// symbol: _
// node: 1
// symbol: Z
// node: 3
// symbol: 8
// decoded: LZ77_LZ78