// Kedare Harshvardhan 21B11198
// 課題 3.4
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_03_04.java」
// コードの実行方法 ：　「java kadai_21_11198_03_04 linearcode.txt」(入力ファイルあり)　または　「java kadai_21_11198_03_04」(入力ファイルなし)

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; 
import java.lang.Math;
import java.util.ArrayList;  
import java.util.List;  

public class kadai_21_11198_03_04 {
	
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
	
	public static List<String> row_echelon_convertor(List<String> matrix, int dimension, int asize) {
		
		// Function to convert the generator matrix in standard form
		// Reference : A First Course in Coding Theory by Raymond Hill, pg. 51
		
		char one = '1'; 
		char zero = '0'; 
		
		int count = 0; 
		while(count < dimension){
			// Step 1
			if(matrix.get(count).charAt(count) != one) {
				boolean found = false; 
				int i = count; 
				while(!found && i < dimension) {
					if(matrix.get(i).charAt(count) == one) {
						// found the row with 1, now swap the rows
						String swap = matrix.get(count); 
						matrix.set(count, matrix.get(i));
						matrix.set(i, swap); 
						// exit the loop 
						found = true; 
					}
					i++; 
				}
			}
			// Step 2 not required omitted 
			// Step 3
			int i = 0; 
			while(i < dimension){
				if(i != count && matrix.get(i).charAt(count) == one) {
					// perform row operations every row with 1 in the column: Ri = Rcount - Ri
					for(int k=0; k<asize; k++) {
						int a = matrix.get(i).charAt(k) - 48; 
						int b = matrix.get(count).charAt(k) - 48; 
						int c = (a+b)%2; 
						String replace = matrix.get(i).substring(0,k) + (char)(c + 48)  + matrix.get(i).substring(k+1,asize);
						matrix.set(i, replace); 
						// subtracted one row from another 
					}
				}
				i++;
				// skip the procedure 
			}
			count++; 
		}
		
		return matrix; 
	}
	
	public static List<String> parity_check_creator(List<String> G_matrix, List<String> H_matrix, int dimension, int asize) {
		
		// Function to create the parity-check matrix
		// Reference : A First Course in Coding Theory by Raymond Hill, pg. 71
		
		String identity_row = "0"; 
		String place = "s";  
		for(int i=0; i<asize-dimension-1; i++) {
			identity_row += "0"; 
		}
		
		for(int i=0; i<asize-dimension; i++) {
			place = "s";  
			// add the first dimension elements and now we append an identity matrix
			for(int j=0; j<dimension; j++) {
				place += G_matrix.get(j).charAt(dimension+i);
			}
			// add the identity matrix portion
			identity_row = identity_row.substring(0,i) + "1" + identity_row.substring(i+1); 			
			place += identity_row; 
			H_matrix.add(i, place.substring(1));
			// resolve the changed part of identity_row
			identity_row = identity_row.substring(0,i) + "0" + identity_row.substring(i+1); 	
		}
		
		return H_matrix; 
	}
	
	public static List<String> transpose(List<String> H_transpose, List<String> H_matrix, int dimension, int asize) {
		
		String s = "s"; 
		
		for(int i=0; i<asize; i++) {
			 s = "s"; 
			for(int j=0; j<asize-dimension; j++) {
				s += H_matrix.get(j).charAt(i); 
			}
			H_transpose.add(s.substring(1)); 
		}
				
		return H_transpose; 
	}
	
	public static List<String> product(List<String> product_matrix, List<String> G, List<String> H, int dimension, int asize) {
				
		for(int i=0; i<dimension; i++) { // i is the row of G
			
			String s = "s"; 
			
			for(int j=0; j<dimension; j++) { // j is the col of H
				
				int g = 0, h = 0; 
				char a, b; 
				int product = 0; 
				for(int k=0; k<asize; k++){
					g = G.get(i).charAt(k) - 48;
					h = H.get(k).charAt(j) - 48;
					product += g*h; 
				}
				product = product%2; 
				s = s + (char)(product+48);
			}
			product_matrix.add(s.substring(1)); 
		}
		
		return product_matrix; 
	}
	
	public static void main(String[] args) {
		
		int asize= 0, dimension = 0; 
		String c0 = ""; 
		// Use ArrayList to first create a array of size 0 
		List<String> G_matrix = new ArrayList<String>(); 
				
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
			dimension = Integer.parseInt(c0.substring(2)); 
			
			// Skip the G= line 
			c0 = sc.nextLine();
			int count = 0; 
			// Read the all the codes
			while(count < dimension) {
				G_matrix.add(sc.nextLine());
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
			System.out.print("k=");
			dimension = sc.nextInt();
			sc.nextLine();
			// Print the G= line 
			System.out.println("G=");
			// Take codes as inputs 
			int count = 0; 
			while(count < dimension) {
				// generation_matrix[count] = sc.nextLine(); 
				G_matrix.add(sc.nextLine());
				count++; 
			}
			// Close the scanner 
			sc.close(); 
		}
		
		// Process and output all the constants first 
		System.out.println("");
		System.out.println("M=" + (int)Math.pow(2,dimension));
		
		String[] codes = new String[dimension]; 
		for (int i=0; i<dimension; i++) {
			codes[i] = G_matrix.get(i);  
		}
				
		int d_min = d_min_calc(codes, asize, dimension);
		float R = (float) dimension / (float) asize; 
		// R = k/n 
		System.out.println("R=" + R);
		System.out.println("d_min=" + d_min);
	
		// Process and output all the arraylists then
		
		List<String> G_standard_matrix = new ArrayList<String>(); 
		for(int i=0; i<dimension; i++) {
			G_standard_matrix.add(i,G_matrix.get(i));  
		}
				
		System.out.println("G'="); 
		
		G_standard_matrix = row_echelon_convertor(G_standard_matrix, dimension, asize); 
		
		for(int i=0; i<dimension; i++) {
			System.out.println(G_standard_matrix.get(i));
		}
		
		System.out.println("H'="); 
		
		List<String> H_matrix = new ArrayList<String>();
		H_matrix = parity_check_creator(G_standard_matrix, H_matrix, dimension, asize);
		
		for(int i=0; i<dimension; i++) {
			System.out.println(H_matrix.get(i));
		}
		
		//System.out.println("(H')^T"); 
		
		List<String> H_transpose = new ArrayList<String>();
		H_transpose = transpose(H_transpose, H_matrix, dimension, asize); 
		
		//for(int i=0; i<asize; i++) {
		//	System.out.println(H_transpose.get(i));
		//}
		
		System.out.println("G(H’)^T="); 
		
		List<String> G_H_product = new ArrayList<String>();
		
		G_H_product = product(G_H_product, G_matrix, H_transpose, dimension, asize);
		
		for(int i=0; i<dimension; i++) {
			System.out.println(G_H_product.get(i));
		}
		
		System.out.println("G'(H’)^T="); 
		
		List<String> G_standard_H_product = new ArrayList<String>();
		
		G_standard_H_product = product(G_standard_H_product, G_standard_matrix, H_transpose, dimension, asize);
		
		for(int i=0; i<dimension; i++) {
			System.out.println(G_standard_H_product.get(i));
		}
		
	}	
}

// 実行結果が長いため、「21_11198_03_04_result.txt」 で書き込んだ。
