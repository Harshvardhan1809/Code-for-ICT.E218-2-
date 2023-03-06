// Kedare Harshvardhan 21B11198

// プログラミング言語: C
// コンパイル方法: gcc 21_11198_markov.c -o 21_11198_markov
// 実行方法: ./21_11198_markov

// Basically combining iid and huffman

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>

// グロバル変数
int asize;
int len;
char *symbol;
double *prob;
int *char_counter; // 符号語の各位置に相当する記号の決め方
int misc = 0;      // 雑な処理に役たつ任意の変数
int new_misc = 0;
double probs = 1; // 各符号の確率を表す任意の変数
double val;

int abstract_recursive_function(int count, double array[asize][asize])
{ // 再帰関数を用いる

    if (count == 0)
    {
        abstract_recursive_function(count + 1, array);
    }

    if (count < len & count != 0)
    {
        for (int i = 0; i < asize; i++)
        {
            char_counter[count - 1] = i;
            abstract_recursive_function(count + 1, array);
        }
        char_counter[count - 1] = 0;
    }

    if (count == len)
    { // here, we permute the last digit asize times and get all combinations for last digit

        for (int k = 0; k < asize; k++)
        {
            printf("P(");
            for (int j = 0; j < len - 1; j++)
            {
                printf("%c", symbol[char_counter[j % asize]]);
                probs *= prob[char_counter[j % asize]];
                new_misc = char_counter[j % asize]; 
            }
                misc = k; 
                probs *= array[new_misc][k];
                printf("%c): %f \n", symbol[k], probs); 
                probs = 1;
        }
        char_counter[misc] = 0;
    }

    return 0;
}

int input_combination_function(int count, double array[asize][asize]) // if we know the dimensions we can pass array without pointers
{
    // iidのコードにおけるlenを2で置き換える。

    if (count == 0)
    {
        input_combination_function(count + 1, array);
    }

    if (count < 2 & count != 0)
    {
        for (int i = 0; i < asize; i++)
        {
            char_counter[count - 1] = i;
            input_combination_function(count + 1, array);
        }
        char_counter[count - 1] = 0;
    }

    if (count == 2)
    { // here, we permute the last digit asize times and get all combinations for last digit
        for (int k = 0; k < asize; k++)
        {
            printf("cp_");
            for (int j = 0; j < 2 - 1; j++)
            {
                printf("%d", char_counter[j % asize]+1);
                new_misc = j;
                printf("%d> ", k+1);
                scanf("%lf", &val);
                misc = k;
                array[char_counter[j % asize]][k] = val; 
            } 
        }
        char_counter[misc] = 0;
    }

    return 0;
}

int main()
{ 

    printf("alphabet size> ");
    scanf("%d", &asize);
    printf("length> ");
    scanf("%d", &len);

    double prob_combination[asize][asize];

    char_counter = (int *)malloc(asize * sizeof(int));
    symbol = (char *)malloc(asize * sizeof(char));
    prob = (double *)malloc(asize * sizeof(double));

    for (int i = 0; i < asize; i++)
    {
        char_counter[i] = 0;
        printf("symbol_%d> ", i + 1);
        scanf("%s", &symbol[i]);
        printf("p_%d> ", i + 1);
        scanf("%lf", &prob[i]);
    }

    input_combination_function(0, prob_combination); 
    // この関数は再帰的に長さ2の記号列を全て表示、マルコフ情報源の生起確率（条件付き確率）を受け取る。

    abstract_recursive_function(0, prob_combination); 
    // この関数は再帰的に長さlenの記号列の全てに対して、生起確率を計算して表示している。

    free(char_counter);
    free(symbol);
    free(prob);

    return 0;
}

// 実行結果
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(ababa): 0.012800 
// P(ababb): 0.012800 
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(aaaaa): 0.000320 
// P(aaaab): 0.001280 
// P(babaa): 0.005120 
// P(babab): 0.020480 
// P(babaa): 0.005120 
// P(babab): 0.020480 
// P(babaa): 0.005120 
// P(babab): 0.020480 
// P(babaa): 0.005120 
// P(babab): 0.020480 
// P(bbbba): 0.204800 
// P(bbbbb): 0.204800 
// P(babaa): 0.005120 
// P(babab): 0.020480 
// P(babaa): 0.005120 
// P(babab): 0.020480 
// P(babaa): 0.005120 
// P(babab): 0.020480 