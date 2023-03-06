
// Kedare Harshvardhan 21B11198
// 課題 3.2
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_03_02.java」
// コードの実行方法 ：　「java kadai_21_11198_03_02 code.txt」(入力ファイルあり)　または　「java kadai_21_11198_03_02」(入力ファイルなし)

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; 

public class kadai_21_11198_03_02 {
	
	public static int d_min_calc(String [] codes, int asize, int code_num) {
		
		int d_min = 1024; 
		int diff = 0; // ハミング距離を表す変数
		// Finding the 最小距離 
		// We go in three level of loops, calculate the d, and compare with the min value 
		
		for(int i=0; i<code_num; i++) {
			for(int j=i; j<code_num; j++) {
				for(int k=0; k<asize; k++) {
					if(codes[i].charAt(k) != codes[j].charAt(k)) {
						diff++; 
					}
				}
				// For testing : System.out.println("i = " + i + " j= " + j + " diff= " + diff);
				if(diff < d_min && diff > 0) d_min = diff; 
				diff = 0; 
			}
		}
		return d_min; 
	}
	
	public static void main(String[] args) {
			
			int asize= 0, code_num = 1, d_min = 1024;
			float R=0;  
			String c0="";  
			String[] codes = new String[code_num]; 
					
			if(args.length == 1) {
				// コマンドにファイル名が渡される場合
				String filename = args[0]; 
				File f = new File(filename);
				Scanner sc = new Scanner(System.in); 
				try {
					sc = new Scanner(f);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				// Reading n & M lines and parsing each of them as integers 
				c0 = sc.nextLine();
				asize = Integer.parseInt(c0.substring(2)); 
				c0 = sc.nextLine();
				code_num = Integer.parseInt(c0.substring(2)); 
				
				// Skip the C= line 
				c0 = sc.nextLine();
				int count = 0; 
				// Create a new array and replace with the original one 
				String[] codes_new = new String[code_num]; 
				codes = codes_new; 
				// Read the all the codes
				while(count < code_num) {
					codes[count] = sc.nextLine();
					count++; 
				}
				// Close the scanner 
				sc.close(); 
			}
			else {
				Scanner sc = new Scanner(System.in); 
				System.out.print("n=");
				asize = sc.nextInt();
				// nextInt() does not read "\n" so we have to manually go to the next line 
				sc.nextLine();
				System.out.print("M=");
				code_num = sc.nextInt();
				sc.nextLine();
				// Print the C= line 
				System.out.println("C=");
				// Create a new array and replace 
				String[] codes_new = new String[code_num]; 
				codes = codes_new; 
				// Take codes as inputs 
				int count = 0; 
				while(count < code_num) {
					codes[count] = sc.nextLine(); 
					count++; 
				}
				// Close the scanner 
				sc.close(); 
			}
							
			// Calculating R
			// R = 2/asize
			d_min = d_min_calc(codes, asize, code_num); 
			R = (float) 2/ (float) asize; 
			// 
			
			// Printing the results 
			System.out.println("d_min="+d_min);
			System.out.println("R="+R);
			
		}
}

// 実行結果
//d_min=453
//R=0.001953125

