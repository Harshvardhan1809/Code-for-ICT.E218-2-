 // Kedare Harshvardhan 21B11198
// 課題 4.6
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_04_06_poly.java」
// コードの実行方法 ：「java kadai_21_11198_04_06_poly RS256_encode_problem.txt」(入力ファイルあり)　または　「java kadai_21_11198_04_06_poly」(入力ファイルなし)

// 注意点
// 1. このコードは　k x n　の生成行列を生成するので、実行に結構時間がかかります (10分ほど)
// 2. このコードは　4.6 の (a)の回答です。復号化の部分は時間内に解ききれなかったです。
// このコードは生成行列の計算のみになっているので、符号語を求めることはしていません
// 3. 実行結果（シンドロム）は以下の通りである
// 		実行結果
// 		符号するときのシンドロム　
//		00000000
//		11011110
//		11000011
//		01010110
// 4. 

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class kadai_21_11198_04_06_poly {


	// may or may not need
	private static List<Integer[]> combinations = new ArrayList<Integer[]>(); 
	private static List<Integer> unit_polynomial =  new ArrayList<Integer>();
	private static List<Integer> degree_one_polynomial =  new ArrayList<Integer>();
	private static List<Integer> zero_polynomial =  new ArrayList<Integer>();
	// necessary lists are declared statically 
	private static List<Integer> f = new ArrayList<Integer>(); 
	private static List<Integer[]> u = new ArrayList<Integer[]>(); 
	private static List<Integer[]> basis = new ArrayList<Integer[]>(); 
	// defining the matrix as a lis of lists of integer arrays
	private static List< List<Integer[]> > A = new ArrayList< List<Integer[]> >(); 
	// Declaring as instance variable so that we don't have to pass it to modify
	
	// Performing the operation
	// 1. Convert to row echelon form; perform row operations on b simultaneously
	// 2. The values of b are solution

	public static void base_vector_creator(int n, int k, int m) {
		
		// create a base vector
		List<Integer> base = new ArrayList<Integer>(); 
		
		for(int i=0; i<n; i++) {
			if(i==0) basis.add(unit_polynomial.toArray(new Integer[0]));
			else if(i == 1) basis.add(degree_one_polynomial.toArray(new Integer[0]));
			else {
				base = function_multiplication(base, Arrays.asList(basis.get(basis.size()-1)), degree_one_polynomial, m);
				base = polynomial_padding(base, m);
				basis.add(base.toArray(new Integer[0]));
				base.clear(); 
			}
		}
				
	}
	
	public static void generator_matrix(int n, int k, int m) {
		
		List<Integer> element = new ArrayList<Integer>(); 
		
		base_vector_creator(n, k, m);
				
		for(int i=0; i<k; i++) {
			
			List<Integer[]> row = new ArrayList<Integer[]>(); 
			
			// Create the rows
			if(i==0) {
				// First row just add unit polynomial
				for(int p=0; p<n; p++) {
					row.add(unit_polynomial.toArray(new Integer[0]));
				}
			}
			
				// Else do the calculations
			else {
								
				List<Integer> e = new ArrayList<Integer>(); 
				e.addAll(Arrays.asList(basis.get(i)));
				// asList returns a fixed sized list so cant f with it 
				List<Integer> copyer = new ArrayList<Integer>(); 
				for(int j=0; j<n; j++) {
					System.out.print(" " + j + " ");
					// Create the individual element 
					if(j==0)copyer = function_multiplication(copyer,e, unit_polynomial, m);
					else copyer = function_multiplication(copyer,e, Arrays.asList(basis.get(i)), m);
					copyer = polynomial_padding(copyer, m);
					row.add(copyer.toArray(new Integer[0]));
					e.addAll(copyer); 
					copyer.clear();
				}
				e.clear(); 
			}
			
			
			A.add(row);
			// System.out.println("Row added  " + row.size() + " size of 0th element " + Arrays.toString(row.get(i)));
		}
		
	}
	
	public static void syndrome_4_calculator(int n, int m, int k) {
		
		List<Integer[]> syndromes = new ArrayList<Integer[]>(); 
		
		for(int i=0; i<k; i++) {
			
			List<Integer> synd = new ArrayList<Integer>(); 
			List<Integer> syndrome = new ArrayList<Integer>(); 
			
			for(int q=0; q<m; q++) syndrome.add(0);
			
			// multiply u to the row
			// u size is 223 
			for(int j=0; j<4; j++) {
				synd = function_multiplication(synd, Arrays.asList(A.get(i).get(j)), Arrays.asList(u.get(i)), m);
				synd = polynomial_padding(synd, m);
				for(int p=0; p<m; p++) {
					syndrome.set(p, (syndrome.get(p) + synd.get(p))%2 );
				}
			}
			syndromes.add(syndrome.toArray(new Integer[0]));
		}
		
	}
	
	public static void vector_creator(String code, int dimension, int size, char adduct) {
		
		// have to use recursion to create 2^dimension combinations
		
		if(size == dimension) {
			code += adduct;  

			Integer[] c = new Integer[size];
			for(int i=0; i<size; i++) {
				c[i] = code.charAt(i) - 48;
			}
			combinations.add(c); 
			// Desired operation :
			// code_generator(code, dimension, asize);
		}
		
		else {
			if(size !=0) code += adduct; 
			vector_creator(code, dimension, size+1, '0');
			vector_creator(code, dimension, size+1, '1');
		}		
	}
	
	public static int degree(List<Integer> polynomial) {
				
		for(int i=polynomial.size()-1; i>-1; i--) {
			if(polynomial.get(i) == 1) return i; 
		}
		
		return 0; 
	}
	
	public static void polynomial_unit_initializer(int m) {
		
		unit_polynomial.add(1);
		while(--m>0) unit_polynomial.add(0);
		
	}
	
	public static void polynomial_degree_one_initializer(int m) {

		degree_one_polynomial.add(0);
		while(--m>0) degree_one_polynomial.add(0);
		degree_one_polynomial.set(1, 1);
		
	}	
	
	public static void polynomial_zero_initializer(int m) {
		
		while(--m>=0) zero_polynomial.add(0);
		
	}
	
	public static List<Integer> polynomial_reduce(List<Integer> remainder) {
		
		List<Integer> polynomial = new ArrayList<Integer>(); 
		
		int degree = degree(remainder);
		
		Integer[] ar = remainder.toArray(new Integer[0]); 
		
		for(int i=0; i<degree+1; i++) {
			polynomial.add(remainder.get(i));
		}
		 
		return polynomial; 
	}
	
	public static List<Integer> polynomial_padding(List<Integer> polynomial, int p) {
		
		List<Integer> function = new ArrayList<Integer>(); 
		function.addAll(polynomial);
		
		while(function.size() != p) {
			function.add(0);
		}
		 
		return function; 
	}
	
	public static boolean equals_zero_polynomial(List<Integer> polynomial) {
		
		for(int i=0; i< polynomial.size(); i++)
			if(polynomial.get(i) != 0) return false; 
		
		return true; 
	}
	
	public static List<Integer> function_addition(List<Integer> function, List<Integer> f, List<Integer> g) {
		
		// We assumr f has higher degree
		// Copy f in function
		for(int j=0; j<f.size(); j++) {
			function.add(f.get(j));
		}
				
		for(int i=0; i<g.size(); i++) {
			function.set(i, ( f.get(i) + g.get(i) )%2 );
		}
		
		return function; 
	}
	
	public static List<Integer> function_subtraction(List<Integer> function, List<Integer> f, List<Integer> g, int m) {
		
		// Copy f in function
		for(int j=0; j<f.size(); j++) {
			function.add(f.get(j));
		}
		
		while(g.size() != m) g.add(0);
		
		for(int i=0; i<g.size(); i++) {
			int value = f.get(i) - g.get(i); 
			if(value < 0) value += 2; 
			function.set(i, value%2 );
		}
		
		return function; 
	}
	
	public static List<Integer> function_multiplication(List<Integer> function, List<Integer> f, List<Integer> g, int size) {
		
		//System.out.println("In function function_multiplication " + size);
		
		List<Integer> dummy =  new ArrayList<Integer>(); 
		Integer[] powers = new Integer[f.size() + g.size()-1]; 
		int value; 
		Arrays.fill(powers, 0); 
		
		for(int i=0; i<f.size(); i++) {
			for(int j=0; j<g.size(); j++) {
				value = f.get(i)*g.get(j);  // 1 [01] 3 [1101] 
				value = value%2; 
				powers[i+j] += value; 
			}
		}
		
		for(int i=0; i<powers.length; i++) {
			powers[i] = powers[i]%2; 
			function.add(powers[i]);
		}
		
		// Format the strings by removing the excess 0s from right
		if(powers.length > 0) {
			dummy = polynomial_reduce(function);
			function.clear();
			function.addAll(dummy);
			dummy.clear(); 
		}
	
		// Now perform modulo on the function with highest degrees
		// When is size > 0 
		if(degree(function) > degree(f) && size > 0) {
			dummy = function_modulo(function);
			function.clear();
			function.addAll(dummy);
			dummy.clear(); 
		}
		
		return function; 
	}
	
	public static List<Object> function_division(List<Integer> f, List<Integer> g) {
				
		List<Integer> quotient = new ArrayList<Integer>(); 
		List<Integer> remainder = new ArrayList<Integer>(); 
		List<Integer> dummy = new ArrayList<Integer>(); 
		List<Integer> dummy2 = new ArrayList<Integer>(); 
		
		int highest_order_f = 0; 
		int highest_order_g = 0; 
		int count = 0;
		
		while(highest_order_f >= 0) {
			
			dummy.clear(); 
						
			// Get the highest orders of the functions f and g
			highest_order_f = degree(f);
			highest_order_g = degree(g);
			
			// Get the rightmost aka the highest order element of f
			int f_coeff = f.get( degree(f) );
			// Get the rightmost aka the highest order element of g
			int g_coeff = g.get( degree(g) );
			
			// Do the necessary operations on quotient function
			int entry_order = highest_order_f - highest_order_g; 
						
			for(int i=0; i<entry_order+1; i++) {
				if(i == entry_order) dummy.add( (int) f_coeff/g_coeff ); 
				else dummy.add(0);
			}
			
			// If dummy is longer we pass it as f or else as g 
			dummy2 = (degree(dummy) >= degree(quotient)) ? function_addition(dummy2, dummy, quotient) : function_addition(dummy2, quotient, dummy); 
			
			// Replace quotient
			quotient.clear();
			quotient.addAll(dummy2);
			// Remove contents in the dummy lists
			dummy2.clear(); 
			
			
			// Do the necessary operations on the remainder function
			dummy2 = function_multiplication(dummy2, g, dummy, -1);
			// Add f and ()x^()q(x) and store in remainder 
			remainder.clear();
			remainder = function_addition(remainder, f, dummy2);
			// Remove contents in the dummy lists
			dummy.clear(); 
			dummy2.clear(); 

			if(degree(remainder) < degree(g)) {
				// reducing the zero elements of the remainder
				dummy = polynomial_reduce(remainder); 
				return Arrays.asList(quotient, dummy);
			}
			else {
				f.clear();
				f.addAll(remainder);
			}
			
		}
		
		return Arrays.asList(quotient, remainder); 
	}
	
	public static List<Integer> function_modulo(List<Integer> function) {
		
		// we find the modulo of the function with degree higher than size
		
		List<Object> division_result = function_division(function, f);
		List<Integer> remainder = new ArrayList<Integer>(); 
		
		String s = division_result.get(1).toString(); 
		s = s.replace(",", "").replace(" ", "").replace("[", "").replace("]", "");
		
		// Now, take the remainder and return it 
		
		for(int i=0; i<s.length(); i++) {
			remainder.add(s.charAt(i) - 48);
		}
		
		return remainder; 
	}
	
	public static List<Integer> function_inverse(List<Integer> function, int m) {
				
		// We already have the unit polynomial 
		List<Integer> dummy =  new ArrayList<Integer>(); 
		List<Integer> dummy2 =  new ArrayList<Integer>(); 
		
		for(int i=0; i<combinations.size(); i++) {
									
			for(int j=0; j<combinations.get(i).length; j++) {
				dummy.add(combinations.get(i)[j]);
				// dummy2.add(combinations.get(i)[j]);
			}
			
			// Arrays.asList() gives immutable arrays 
			// dummy = Arrays.asList(combinations.get(i));
			
			dummy2 = function_multiplication(dummy2, dummy, function, m);
			
			// Making the lengths same for smaller products
			if(degree(dummy2) < m) {
				while(dummy2.size() < m) dummy2.add(0);
			}
			
			// Make comparison
			if( dummy2.equals(unit_polynomial) ) { 
					return dummy; 
				}; 
			
			// If no match empty the lists for further iterations
			dummy.clear();
			dummy2.clear();
			
		}
		
		return dummy;  
	}
	
	public static void A_printer(int n, int k) {
		System.out.println("Printing A");
		
		for(int i=0; i<k; i++) {
			for(int j=0; j<4; j++) {
				
				// System.out.println("Print size " + A.size()); // should be 223
				// System.out.println("Size " + A.get(i).size()); // should be 255
				// System.out.println("S " + A.get(i).get(j).length); // should be m=8
				
				Integer[] a = A.get(i).get(j); 
				System.out.print("[");
				for(int p=0; p<a.length; p++) {
					System.out.print(A.get(i).get(j)[p] + "");
				}
				System.out.print("]");
				
			}
			
			System.out.println("");
			System.out.println(" " + i);
		}
		
		System.out.println("");
	}
	
	public static void u_printer(int n, int m) {
		for(int i=0; i<n; i++) {
			System.out.print("[");
			for(int j=0; j<u.get(i).length; j++) {
				System.out.print(u.get(i)[j] + "");
			}
			System.out.println("]");
		}
	}
	
	public static List<Integer[]> rows_subtract(List<Integer[]> row, List<Integer[]> count_row, int p, int n) {
		
		int value; 
		// subtract a row containing n polynomials
		List<Integer[]> new_row = new ArrayList<Integer[]>();
		
		for(int i=0; i<n; i++) {
			Integer[] row_element = row.get(i); 
			Integer[] count_row_element = count_row.get(i); 

			for(int j=0; j<p; j++) {
				row_element[j] = (row_element[j] + row_element[j])%2; 
			}
			new_row.add(row_element); 
		}
				
		return new_row; 
	}
	
	public static List<List<Integer[]>> row_echelon_convertor(List<List<Integer[]>> matrix, int p, int n) {
		
		int count = 0; 
		while(count < n){
			
			// Step 1
			// This step swaps a zero row with a non-zero row 
			
			if(matrix.get(count).get(count).equals(zero_polynomial)) {
				boolean found = false; 
				int i = count; 
				while(!found && i < n) {
										
					if(!matrix.get(i).get(count).equals(zero_polynomial)) {
						// For matrix found the row with 1, now swap the rows
						List<Integer[]> swap = new ArrayList<Integer[]>(); 
						swap.addAll(matrix.get(count)); 
						matrix.set(count, matrix.get(i));
						matrix.set(i, swap); 
						// exit the loop 
						found = true; 
						
						// Swap the rows in b
						Integer[] a = u.get(count); 
						u.set(count, u.get(i));
						u.set(i,a);
					}
					i++; 
				}
			}		
			
			// Step 2 
			// In this step, we make the diagonal element [100] and divide the entire row to do so 
			
			
			// CORRECT FOR B 
			if(!matrix.get(count).get(count).equals(unit_polynomial.toArray(new Integer[0]))) { 
				
				List<Integer[]> row = new ArrayList<Integer[]>(); 
				List<Integer> row_element = new ArrayList<Integer>();
				List<Integer> padded_row_element = new ArrayList<Integer>();
				
				List<Integer> inverse_a = function_inverse(Arrays.asList(matrix.get(count).get(count)), p); 
								
				// Now multiple each function in the row with inverse
				for(int k=0; k<n; k++) { // function_multiplication
					row_element = function_multiplication(row_element, Arrays.asList(matrix.get(count).get(k)),inverse_a, p);
					// Pad the row element such that it has a length of p 
					padded_row_element = polynomial_padding(row_element, p); 
					// Add the padded row element to the row
					row.add(padded_row_element.toArray(new Integer[0]));
					row_element.clear(); 
					padded_row_element.clear();
				}
				// the row list has the updated row elements 
				
				// Repace the matrix.get(count) list with the new row
				matrix.set(count, row);
				// Update the elements of the vector b
				row_element = function_multiplication(row_element,Arrays.asList(u.get(count)),inverse_a, p);
				padded_row_element = polynomial_padding(row_element, p); 
				u.set(count, padded_row_element.toArray(new Integer[0]));
				row_element.clear();
				padded_row_element.clear();
				
			}

			// Step 3  
			// We have to convert the next steps into zero 
			// So find x such that x*matrix.get(count).get(count) + matrix.get(i).get(count) = [000]
			// => x*matrix.get(count).get(count) = matrix.get(i).get(count)
			// => x = inverse(matrix.get(count).get(count)) * matrix.get(i).get(count)
			// Finally, do matrix.get(i) +=  x*{matrix.get(count)}
			// Since {matrix.get(count)} = [100] this basically means x =  matrix.get(i).get(count)
						
			int i = 0; 
			List<Integer> inverse = function_inverse(Arrays.asList(matrix.get(count).get(count)), p); 
			
			while(i < n){
												
				if(i != count && !equals_zero_polynomial(Arrays.asList(matrix.get(i).get(count)))) {
										
					// multiply entire count row with the column element of row 	
					List<Integer[]> count_row = matrix.get(count); 
					List<Integer[]> row = matrix.get(i); 
					List<Integer> row_element = Arrays.asList(row.get(count)); 
					for(int w=0; w<n; w++) {
						List<Integer> inverse_replacer = new ArrayList<Integer>();
						inverse_replacer = function_multiplication(inverse_replacer, Arrays.asList(count_row.get(w)), row_element, p);
						count_row.set(w, inverse_replacer.toArray(new Integer[0]));
						inverse_replacer.clear(); 
					}
					// Do same with b
					List<Integer> b_count = new ArrayList<Integer>(); 
					b_count = function_multiplication(b_count, Arrays.asList(u.get(count)), row_element, p);
					b_count = polynomial_padding(b_count, p);
					u.set(count, b_count.toArray(new Integer[0]));
					b_count.clear(); 
					
					// subtract count row from row
					List<Integer[]> new_rows = new ArrayList<Integer[]>(); 
					for(int z=0; z<n; z++) {
						List<Integer> new_ = new ArrayList<Integer>(); 
						List<Integer> new_1 = new ArrayList<Integer>(); 
						List<Integer> new_2 = new ArrayList<Integer>(); 
						
						new_1 = polynomial_padding(Arrays.asList(row.get(z)), p); 
						new_2 = polynomial_padding(Arrays.asList(count_row.get(z)), p); 
						
						// ERROR AREA 
						// System.out.println("Subtract the rows");
						new_ = function_subtraction(new_, new_1, new_2, p);
						// ERROR AREA
						new_rows.add(new_.toArray(new Integer[0]));
						new_.clear();
						new_1.clear();
						new_2.clear();
					}
					// update the ith row 
					matrix.set(i, new_rows); 
					// Do the same with b
					b_count = function_subtraction(b_count, Arrays.asList(u.get(count)), Arrays.asList(u.get(i)), p);
					u.set(i, b_count.toArray(new Integer[0]));
					b_count.clear(); 
					
					// Return the count row to its previous state
					List<Integer> inverse_count_row_element = function_inverse(Arrays.asList(count_row.get(count)), p);
					List<Integer> inverse_count_row_element_b = function_inverse(Arrays.asList(u.get(count)), p);
					List<Integer> padder = new ArrayList<Integer>(); 
					padder = polynomial_padding(inverse_count_row_element, p); 
					padder.clear(); 
					for(int w=0; w<n; w++) {
						List<Integer> padder1 = new ArrayList<Integer>(); 
						List<Integer> inverse_replacer = new ArrayList<Integer>();
						inverse_replacer = function_multiplication(inverse_replacer, Arrays.asList(count_row.get(w)), inverse_count_row_element, p);
						padder = polynomial_padding(inverse_replacer, p); 
						count_row.set(w, padder.toArray(new Integer[0]));
						inverse_replacer.clear(); 
						padder1.clear(); 
					}
					matrix.set(count, count_row); 
					// Do the same for b
					b_count = function_multiplication(b_count, Arrays.asList(u.get(count)), inverse_count_row_element, p);
					b_count = polynomial_padding(b_count, p);
					u.set(count, b_count.toArray(new Integer[0]));
					b_count.clear(); 
					inverse_count_row_element.clear(); 
					inverse_count_row_element_b.clear();
					
				}
				i++;
				// skip the procedure 
				
			}
			
			inverse.clear(); 
			count++; 
		}
		
		return matrix; 
	}
	
	public static void main(String args[]) {
		
		int n=0, k=0, m=0; 
		 
		
		if(args.length == 1) {
			// コマンドにファイル名が渡される場合
			String filename = args[0]; 
			File file = new File(filename);
			String c0 = ""; 
			Scanner sc = new Scanner(System.in); 
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			// Skip the INPUT line 
			c0 = sc.nextLine();
			
			// Read the function f
			c0 = sc.nextLine();
			c0 = c0.substring(6, c0.length()-1);
			for(int i=0; i<c0.length(); i++) {
				f.add( (int) (c0.charAt(i) - 48) );
			}
			
			// Read n line 
			c0 = sc.nextLine();
			n = Integer.parseInt(c0.substring(2)); 
			
			// Read m line
			c0 = sc.nextLine();
			k = Integer.parseInt(c0.substring(2)); 
			c0 = sc.nextLine();
			
			// Set m
			m = (int)(Math.log(n+1) / Math.log(2));
			System.out.println("value of m = " + m);
			
			// Skip the u= line
			// Read b 
			String s = sc.nextLine(); 
			List<Integer> adder = new ArrayList<Integer>(); 
			for(int i=0; i<k ; i++) {
	     		s = s.substring(1);
				for(int j=0; j<m; j++) {
					adder.add((int) (s.charAt(0) - 48));
					s = s.substring(1);
				}
				u.add(adder.toArray(new Integer[0])); 
				if(s.length() >= 1) s = s.substring(1);
				adder.clear();
			}
						
			// Close the scanner 
			sc.close(); 
			
		}
		
		// Initialize unit and zero polynomials 
		polynomial_unit_initializer(m);
		polynomial_zero_initializer(m);
		polynomial_degree_one_initializer(m);		
		
		// Create the generator matrix (k x n)
		generator_matrix(n, k, m);

		// A_printer(n, k); 
		
		syndrome_4_calculator(n, m, k);
				
		// Now the adjusted b is bascially the solution so just print it 
		System.out.println("u=");
		// u_printer(k, m);
		
		
	}
}

