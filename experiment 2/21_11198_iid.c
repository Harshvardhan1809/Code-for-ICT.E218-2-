// Kedare Harshvardhan 21B11198

// プログラミング言語: C 
// コンパイル方法: gcc 21_11198_iid.c -o 21_11198_iid
// 実行方法: ./21_11198_iid

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

// use dynamic allocation to optimize the code (no issues of passing arrays as parameters)
// グロバル変数が便利なので、配列や数列をポインターで定義する
int asize;
int length; 
int *char_counter; // 符号語の各位置に相当する記号の決め方
double *prob;
char *symb;

int misc = 0; // 雑な処理に役たつ任意の変数
double probs = 1; // 各符号の確率を表す任意の変数

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
            printf("P(");
            for(int j = 0; j < length-1; j++){
                printf("%c", symb[char_counter[j % asize]]);
                probs *= prob[char_counter[j % asize]]; 
            }
            misc = k;  
            probs *= prob[k]; 
            printf("%c): %f \n", symb[k], probs);
            probs = 1; 
        }
        char_counter[misc] = 0; 
    }

    return 0; 
}


int main(){

    printf("alphabet size> ");
    scanf("%d", &asize);
    printf("length> ");
    scanf("%d", &length);

    // 動的に配列や数列にメモリーを割り当てる
    symb = (char *)malloc(asize*sizeof(char)); 
    prob = (double *)malloc(asize*sizeof(double)); 
    char_counter = (int *)malloc(asize*sizeof(int)); 

    for (int i = 0; i < asize; i++)
    {
        char_counter[i] = 0; 
        printf("symbol_%d> ", i + 1);
        scanf("%s", &symb[i]);
        printf("p_%d> ", i + 1);
        scanf("%lf", &prob[i]);
    }

    // 符号語の生成
    abstract_recursive_function(0); 

    free(prob); // メモリーの解放
    free(symb); 
    free(char_counter); 

    return 0; 
}

// 実行結果
// P(aaaa): 0.000100 
// P(aaab): 0.000200 
// P(aaac): 0.000700 
// P(aaba): 0.000200 
// P(aabb): 0.000400 
// P(aabc): 0.001400 
// P(aaca): 0.000700 
// P(aacb): 0.001400 
// P(aacc): 0.004900 
// P(abaa): 0.000200 
// P(abab): 0.000400 
// P(abac): 0.001400 
// P(abba): 0.000400 
// P(abbb): 0.000800 
// P(abbc): 0.002800 
// P(abca): 0.001400 
// P(abcb): 0.002800 
// P(abcc): 0.009800 
// P(acaa): 0.000700 
// P(acab): 0.001400 
// P(acac): 0.004900 
// P(acba): 0.001400 
// P(acbb): 0.002800 
// P(acbc): 0.009800 
// P(acca): 0.004900 
// P(accb): 0.009800 
// P(accc): 0.034300 
// P(baaa): 0.000200 
// P(baab): 0.000400 
// P(baac): 0.001400 
// P(baba): 0.000400 
// P(babb): 0.000800 
// P(babc): 0.002800 
// P(baca): 0.001400 
// P(bacb): 0.002800 
// P(bacc): 0.009800 
// P(bbaa): 0.000400 
// P(bbab): 0.000800 
// P(bbac): 0.002800 
// P(bbba): 0.000800 
// P(bbbb): 0.001600 
// P(bbbc): 0.005600 
// P(bbca): 0.002800 
// P(bbcb): 0.005600 
// P(bbcc): 0.019600 
// P(bcaa): 0.001400 
// P(bcab): 0.002800 
// P(bcac): 0.009800 
// P(bcba): 0.002800 
// P(bcbb): 0.005600 
// P(bcbc): 0.019600 
// P(bcca): 0.009800 
// P(bccb): 0.019600 
// P(bccc): 0.068600 
// P(caaa): 0.000700 
// P(caab): 0.001400 
// P(caac): 0.004900 
// P(caba): 0.001400 
// P(cabb): 0.002800 
// P(cabc): 0.009800 
// P(caca): 0.004900 
// P(cacb): 0.009800 
// P(cacc): 0.034300 
// P(cbaa): 0.001400 
// P(cbab): 0.002800 
// P(cbac): 0.009800 
// P(cbba): 0.002800 
// P(cbbb): 0.005600 
// P(cbbc): 0.019600 
// P(cbca): 0.009800 
// P(cbcb): 0.019600 
// P(cbcc): 0.068600 
// P(ccaa): 0.004900 
// P(ccab): 0.009800 
// P(ccac): 0.034300 
// P(ccba): 0.009800 
// P(ccbb): 0.019600 
// P(ccbc): 0.068600 
// P(ccca): 0.034300 
// P(cccb): 0.068600 
// P(cccc): 0.240100 