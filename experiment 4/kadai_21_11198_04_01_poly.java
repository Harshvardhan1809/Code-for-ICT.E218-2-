// Kedare Harshvardhan 21B11198
// 課題 4.1
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_04_01_poly.java」
// コードの実行方法 ：「java kadai_21_11198_04_01_poly」(入力ファイルなし)

import  java.util.Scanner; 

public class kadai_21_11198_04_01_poly {
	
	public static void operation(int prime_number, char operation) {
	
		int value; 
		int inverse = 1; 
		char symbol = '-'; 
		System.out.print("  | ");
		for(int i=0; i<prime_number; i++) {
			System.out.print(i  + "  ");
		}
		System.out.println("");		
		System.out.print("--+");
		for(int i=0; i<prime_number; i++) {
			System.out.print("---");
		}
		System.out.println("");		
		
		for(int i=0; i<prime_number; i++) {
			
			System.out.print(i + " |");
			
			for(int j=0; j<prime_number; j++) {
				
				value = 0; 
				if((int)operation == 43) value = i+j; 
				if((int)operation == 42) value = i*j; 
				if((int)operation == 45) {
					value = i-j;
					if(value < 0) value += prime_number; 
				} 
				if((int)operation == 47) {
					// If the denominator is 0 then this 
					if (j == 0) symbol = '-'; 
					// For other cases, find the multiplicative inverse of j and then multiply it to i
					else if(j !=0){
						for(int k=0; k<prime_number; k++) {
							if ( (k*j)%prime_number == 1 ) inverse = k; 
						}
						value = i*inverse; 
						inverse = 0; 
					}
				}
				
				if( !(operation == 47 && j==0) ) System.out.print(" " + value%prime_number + " "); 
				else System.out.print(" - ");
				
			}
			
			System.out.println("");
		}
	}
	
	public static void main(String args[]) {
		
		// Taking the input for prime number
		int prime_number; 
		System.out.print("p="); 
		Scanner sc = new Scanner(System.in); 
		prime_number = sc.nextInt(); 
		sc.close(); 
		
		System.out.println("ADD");
		operation(prime_number, '+'); 
		
		System.out.println("MUL");
		operation(prime_number, '*'); 
		
		System.out.println("SUB");
		operation(prime_number, '-'); 
		
		System.out.println("DIV");
		operation(prime_number, '/'); 
		
	}

}
