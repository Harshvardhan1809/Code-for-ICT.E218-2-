// Kedare Harshvardhan 21B11198

// プログラミング言語: C 
// コンパイル方法: gcc 21_11198_arithmetic_code.c -o 21_11198_arithmetic_code
// 実行方法: ./21_11198_arithmetic_code

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define STRING_SIZE 124

// use dynamic allocation to optimize the code (no issues of passing arrays as parameters)
// グロバル変数が便利なので、配列や数列をポインターで定義する
int asize;
int length; 
double *prob;
char *symb;
double *interval; 
char *ss; 
double *base_interval; 
char *code; 
char *dec_cdws; 
char decoded[STRING_SIZE]; 

double beg = 0; 
double end = 0; 
double real_prob = 0; 
int misc = 0; // 雑な処理に役たつ任意の変数
double probs = 1; // 各符号の確率を表す任意の変数

int index_finder(char s){
    for(int i=0; i<strlen(symb); i++){
        if(symb[i] == s) return i; 
    }

    return 0; 
}

int interval_generator(int counter){

    char s; 

    if(counter == -1){ // generating the base interval
        for(int i=0; i<asize; i++){
            for(int j=i; j<asize; j++){
                interval[j+1] += prob[i]; 
            }  
        }
    }

    else{ 
            beg = interval[index_finder(ss[counter])]; 
            end = interval[index_finder(ss[counter])+1];  

            double new_interval[asize+1];   

            new_interval[0] = beg; new_interval[asize] = end;  
            for(int i=1; i<asize; i++){
                new_interval[i] = beg + (end-beg)*base_interval[i]; 
            }

            for(int i=0; i<asize+1; i++) {
                interval[i] = new_interval[i]; 
            }
        } 

    return 0; 
}

void encode(double mx, double lx){

    // 方法の説明 https://www.quora.com/Can-you-express-values-less-than-1-in-binary 
    for(int i=0; i<lx; i++){
        mx = mx*2; 
        code[i] = (int) mx + '0';
        if(mx >= 1) mx -= 1; 
    }
}

double negative_power(int base, int exp){
    double val = 1; 
    for(int i=0; i<exp; i++){
        val = val/2; 
    }

    return val; 
}

int decode(double real_prob, int place, int length){

    // to decode first convert the binary to double
        
        for(int i=0; i<strlen(dec_cdws); i++){
        real_prob += (dec_cdws[i] - 48) * negative_power(2, (i+1)); 
        }   
        // printf("prob %lf \n", real_prob); 


    while(length-- >= 0){
        // find its place in the interval
        int i=0; 
        int pos = -1; 
        while(pos == -1){
                if(base_interval[i] > real_prob) pos = i; 
                i++; 
            }

        real_prob = (real_prob - base_interval[pos-1])/(base_interval[pos]-base_interval[pos-1]); 
        if(length == 0){
            // terminate the function call 
            decoded[place] = symb[pos-1]; 
            printf("decoded: %s \n", decoded); 

            return 0; 
        }
        else{
            decoded[place] = symb[pos-1]; 
            place++; 
        }
    }

    return 0; 

}

int main(){

    printf("alphabet size> ");
    scanf("%d", &asize);

    // 動的に配列や数列にメモリーを割り当てる
    symb = (char *)malloc(asize*sizeof(char)); 
    prob = (double *)malloc(asize*sizeof(double)); 
    interval = (double *)malloc((asize+1)*sizeof(double)); 
    base_interval = (double *)malloc((asize+1)*sizeof(double)); 
    memset(interval, 0, (asize+1)*sizeof(double)); // 確率の数列を1で初期化する

    for (int i = 0; i < asize; i++)
    {
        printf("symbol_%d> ", i + 1);
        scanf("%s", &symb[i]);
        printf("p_%d> ", i + 1);
        scanf("%lf", &prob[i]);
    }

    interval_generator(-1); // initialize the 区間
    // for(int i=0; i<asize+1; i++) printf("%lf, ", interval[i]);
    // printf("\n"); 
    for(int i=0; i<asize+1; i++) base_interval[i] = interval[i];

    // 処理の簡易化のため、入力記号列をグロバル化する
    char symbols[STRING_SIZE] = {0};
    printf("symbols> ");
    scanf("%s", symbols);
    int len_symbols = strlen(symbols); 
    ss = (char *)malloc(len_symbols*sizeof(char)); 
    strcpy(ss, symbols);

    misc = 0; 

    while(misc < len_symbols){
        interval_generator(misc++); 
    }

    printf("range: [%lf, %lf) \n", interval[0], interval[asize]);


    // take the 区間 bounds from interval, find the mx and then lx
    double mx = (interval[0] + interval[asize])/2; 
    double lx = ceil(-log10(interval[asize] - interval[0]) / (0.3010)) + 1;
    code = (char *)malloc((int)lx*sizeof(char)); 
    encode(mx, lx); 
    printf("encoded: %s \n", code); 
    // 符号語生成完了

    // 復号化作業
    char cdws[STRING_SIZE]; int length; 
    printf("codewords> ");
    scanf("%s", cdws);
    dec_cdws = (char *)malloc(strlen(cdws)*sizeof(char)); 
    strcpy(dec_cdws, cdws); 
    printf("length> ");
    scanf("%d", &length);
    decode(real_prob, 0, length);


    free(prob); // メモリーの解放
    free(symb); 
    free(interval); 
    free(ss); 
    free(base_interval); 
    free(code); 
    free(dec_cdws); 

    return 0; 
}

// 実行結果
// symbols> aadaadaabcccbdbcbacc
// range: [0.336027, 0.336027) 
// encoded: 0101011000000101110110110110011010111110000000 
// codewords> 00101011001100110011                          
// length> 30
// decoded: aaabdaaaaacaaaaaabababcbbbbadd 