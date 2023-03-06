// Kedare Harshvardhan 21B11198
// 課題 3.3
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_03_03.java」
// コードの実行方法 ：　「java kadai_21_11198_03_03 r.txt」(入力ファイルあり)　または　「java kadai_21_11198_03_03」(入力ファイルなし)

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; 

public class kadai_21_11198_03_03 {
	
	public static int hat_c_index(String [] codes, String r, int asize, int code_num) {
		
		int d_min_index = asize; 
		int d_min = 1024; 
		int diff = 0; 
		
		for(int i=0; i<code_num; i++) {
			for(int k=0; k<asize; k++) {
				if(codes[i].charAt(k) != r.charAt(k)) {
					diff++; 
				}
			}
			if(diff < d_min && diff > 0) { 
				d_min = diff; 
				d_min_index = i; 
			}
			diff = 0; 
		}
		
		return d_min_index; 
	}

	public static void main(String[] args) {
		
		int asize= 0, code_num = 1; 
		String c0="";  
		String r; 
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
			// Skip the r= line
			sc.nextLine(); 
			// Read the 受信語 
			r = sc.nextLine();
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
			// Enter the 受信語
			System.out.println("r=");
			r = sc.nextLine(); 
			// Close the scanner 
			sc.close(); 
		}
		
		int index = hat_c_index(codes, r, asize, code_num);	
		
		System.out.println("hat_c=");
		System.out.println(codes[index]);
		
		System.out.println("i_MD="+index);
		
	}
}

//実行結果
//hat_c=
//0110011111111101110000100011110110000111011111100011100111110111111001010010100101100110100000100100100001111110111100111101101001010101001010110000100011001010100111100011111101101011010001011111111001010000011001110000111111100101010111100110001011000010010001111011101101011011001111001101010111110110010110111000110101010100101001110111100100100110011101000001000101111101000000011101010101010111011101110100011010101100000111100111001010111011100000100000000011110010101010100111100010111011111001010101010111010100101000100010011000100010100001010110001111111000000011011011101100011110010111000101010001000010111011001011000101110100101111111101110100111100111100011000010100000000111101100110110111101110110010100101001111111110110001101101110110011110000010001000001100011101010011100110000101100110101111010011111001100000011000011010000011110000100111100101000101000110001100001100001000000000001101010100101000011001000011111001110101001001000011011001111111101101000001101010011100001000100011101100100100111100
//i_MD=56

