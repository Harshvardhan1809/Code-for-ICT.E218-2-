// Kedare Harshvardhan 21B11198
// 課題 3.5
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_03_05_hakidashi.java」
// コードの実行方法 ：　「java kadai_21_11198_03_05_hakidashi dual.txt」(入力ファイルあり)　または　「java kadai_21_11198_03_05_hakidashi」(入力ファイルなし)

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; 
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;  

public class kadai_21_11198_03_05_hakidashi {
	
	// Use ArrayList to handle the matrices
	private static List<String> G_matrix = new ArrayList<String>(); 
	private static List<String> G_standard_matrix = new ArrayList<String>(); 
	private static List<String> H_matrix = new ArrayList<String>(); 
	private static int coefficients[];
	private static int coefficients_B[];
	private static int coefficient_dummy[];
	private static boolean dual_code = false; 
	
	public static List<Integer> binomial_expander(int pow, int variable) {
		
		// Reference https://www.geeksforgeeks.org/program-print-binomial-expansion-series/ 
		// Reference https://www.geeksforgeeks.org/multiply-two-polynomials-2/ 
		
		// for variable == 1 in our case A = 1 X = 1 N = pow
		// for variable == -1 in our case A = 1 X = -1 N = pow
		
		// variable is X then 1 if Y then -1
		if( variable == 1) {
			
			List<Integer> XplusY_result = new ArrayList<Integer>(); 
			int A = 1; 
			int X = 1; 
			int n = pow; 
			
			// expand for X with pow 
	        int term = (int)Math.pow(A, n);
	        XplusY_result.add(term);
	 
	        for (int i = 1; i <= n; i++) {
	            term = term * X * (n - i + 1)
	                                / (i * A);
	            XplusY_result.add(term);
	            // System.out.print(term + " ");
	        }
	        
	        return XplusY_result; 
		}
			
		List<Integer> XminusY_result = new ArrayList<Integer>(); 
		int A = 1; 
		int X = -1; 
		int n = pow; 
		
		// expand for X with pow 
        int term = (int)Math.pow(A, n);
        XminusY_result.add(term);
 
        for (int i = 1; i <= n; i++) {
            term = term * X * (n - i + 1)
                                / (i * A);
            XminusY_result.add(term);
            // System.out.print(term + " ");
        }

	    return XminusY_result; 
		
	}
	
	public static List<Integer> expansion_product(List<Integer> f, List<Integer> g){
		
		Integer[] product = new Integer[f.size() + g.size() -1 ];
		
		for(int j=0; j<product.length; j++) {
			product[j] = 0;  
		}
		
		for(int i=0; i<f.size(); i++) {
			for(int j=0; j<g.size(); j++) {
				product[i+j] += f.get(i)*g.get(j); 
			}
		}
		
		return Arrays.asList(product); 
	}
	
	public static List<Integer> function_constant_product(List<Integer> XYproduct, int coeff, int dimension){
		
		List<Integer> result = new ArrayList<Integer>(); 
		
		for(int j=0; j<XYproduct.size(); j++) {
			result.add(coeff*XYproduct.get(j));  
		}
		
		return result; 
	}
	
	public static void append_coefficients(List<Integer> XYproduct){
		
		for(int i = XYproduct.size()-1; i>-1; i--) {
			coefficients_B[i] += XYproduct.get(i); 
		}
		 
	}
	
	public static void dual_code_finder(int n, int k) {
		
		// Copy the binomial coefficients first 
		for(int i=0; i<coefficients.length; i++) {
			// coefficients_B[i] = coefficients[i]; 
			coefficients_B[i] = 0; 
		}
		
		// Next for each term, do the binomial expansion, calculate the coefficients and add to coefficients_B
		for(int i=0; i<coefficients.length; i++) {
			int coeff = coefficients[i];   
			
			// Example for coefficients [1,3,3,1]; i = 1 then coeff = 3; 
			// then binomial expander for X with (asize-i)
			// then binomial expander for Y with (i)
			// then multiply with each other 
			// then multiply with coeff
			// then add to coefficients
			
			// expand the term of X as X+Y and Y as X-Y
			List<Integer> XplusY_result = binomial_expander(n-i, 1);
			List<Integer> XminusY_result = binomial_expander(i, -1);
			List<Integer> XYproduct = expansion_product(XplusY_result, XminusY_result); 
			XYproduct = function_constant_product(XYproduct, coeff, k); 
			append_coefficients(XYproduct);
		}
		
		for(int i = 0; i<coefficients_B.length; i++) {
			coefficients_B[i] = coefficients_B[i]/(int) Math.pow(2, k); 
		}
		
		
	}
	
	public static void dual_polynomial_printer(int[] coefficients, int dimension, int asize) {
		
		System.out.println("B(X,Y) = ");
		
		for(int i=0; i<asize+1; i++) {
			if(coefficients_B[i] != 0) {
				System.out.println(coefficients_B[i] + "X^" + (asize-i) + "Y^" + i);
			}
		}
		
	}
	
	public static void polynomial_printer(int[] coefficients, int dimension, int asize) {
		
		System.out.println("A(X,Y) = ");
		
		for(int i=0; i<asize+1; i++) {
			if(coefficients[i] != 0) {
				System.out.println(coefficients[i] + "X^" + (asize-i) + "Y^" + i);
			}
		}
		
	}
	
	public static int polynomial_coefficients(String code, int dimension, int asize) {
		
		int distance = 0; 
		
		for(int i=0; i<asize; i++) {
			if((int)code.charAt(i) == 49) {
				distance++;
			} 
		}
		coefficients[distance] += 1; 
		
		return 0; 
	}
	
	public static int code_generator(String vector,int dimension, int asize) {
		
		// here we actually multiply with the generator matrix to get the code
		
		String code = ""; 
		int a=0, b=0, product=0; 
		
		for(int i=0; i<asize; i++){
			product = 0; 
			for(int j=0; j<dimension; j++){
				a = vector.charAt(j) - 48;
				if(!dual_code) b = G_matrix.get(j).charAt(i) - 48; 
				else  b = H_matrix.get(j).charAt(i) - 48; 
				product += a*b; 
			}
			product = product%2; 
			code +=  (char)(product + 48); 
		}
		
		// created the code, now the code will be passed to another function to find the coefficients for the polynomial 
		polynomial_coefficients(code, dimension, asize);
		
		return 0; 
	}
	
	public static void vector_creator(String code, int dimension, int asize, int size, char adduct) {
				
		// have to use recursion to create 2^dimension combinations
		
		if(size == dimension) {
			code += adduct; 
			// System.out.println(code + " " + ++count);
			code_generator(code, dimension, asize);
		}
		
		else {
			if(size !=0) code += adduct; 
			vector_creator(code, dimension, asize, size+1, '0');
			vector_creator(code, dimension, asize, size+1, '1');
		}		
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
	
	public static List<String> parity_check_creator(List<String> G_s_matrix, List<String> H_matrix, int dimension, int asize) {
		
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
				place += G_s_matrix.get(j).charAt(dimension+i);
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
	
	public static void main(String[] args) {
			
			int asize= 0, dimension = 0; 
			String c0 = ""; 
					
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
						
			System.out.println(""); 
			
			String blank_code = ""; 
			
			// Allocating memory to the array
			coefficients = new int[asize+1];
			coefficients_B = new int[asize+1];
			// Initialize the coefficients array 
			for(int i=0; i<asize+1; i++) {
				coefficients[i] = 0; 
				coefficients_B[i] = 0; 
			}
			
			vector_creator(blank_code, dimension, asize, 0, '1');
			polynomial_printer(coefficients, dimension, asize);
			// Adjust the i/o and reset variables
			System.out.println(""); 
			System.out.println(""); 
			blank_code = ""; 

			// Creating the parity-check matrix
			for(int i=0; i<dimension; i++) {
				G_standard_matrix.add(G_matrix.get(i));  
			}
			G_standard_matrix = row_echelon_convertor(G_standard_matrix, dimension, asize); 
			H_matrix = parity_check_creator(G_standard_matrix, H_matrix, dimension, asize);
			
			dual_code = true;
			dual_code_finder(asize, dimension); 
			dual_polynomial_printer(coefficients_B, dimension, asize);

	}
}

// 実行結果が長いため、別のファイルで書き込んで提出します