 // Kedare Harshvardhan 21B11198
// 課題 4.5
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_04_05_hakidashi_Fp.java」
// コードの実行方法 ：「java kadai_21_11198_04_05_hakidashi_Fp linear_equation_F256.txt」(入力ファイルあり)　または　「java kadai_21_11198_04_05_hakidashi_Fp」(入力ファイルなし)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class kadai_21_11198_04_05_hakidashi_Fp {


	// may or may not need
	private static List<Integer[]> combinations = new ArrayList<Integer[]>(); 
	private static List<Integer> unit_polynomial =  new ArrayList<Integer>();
	private static List<Integer> zero_polynomial =  new ArrayList<Integer>();
	// necessary lists are declared statically 
	private static List<Integer> f = new ArrayList<Integer>(); 
	private static List<Integer[]> b = new ArrayList<Integer[]>(); 
	private static List<Integer[]> x = new ArrayList<Integer[]>(); 
	// defining the matrix as a lis of lists of integer arrays
	private static List< List<Integer[]> > A = new ArrayList< List<Integer[]> >(); 
	// Declaring as instance variable so that we don't have to pass it to modify
	
	// Performing the operation
	// 1. Convert to row echelon form; perform row operations on b simultaneously
	// 2. The values of b are solution
	
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
	
	public static void A_printer(int n, int m) {
		System.out.println("Printing A");
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				
				Integer[] a = A.get(i).get(j); 
				System.out.print("[");
				for(int k=0; k<a.length; k++) {
					System.out.print(A.get(i).get(j)[k] + "");
				}
				System.out.print("]");
				
			}
			
			System.out.println("");
		}
		
		System.out.println("");
	}
	
	public static void b_printer(int n, int m) {
		for(int i=0; i<n; i++) {
			System.out.print("[");
			for(int j=0; j<b.get(i).length; j++) {
				System.out.print(b.get(i)[j] + "");
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
						Integer[] a = b.get(count); 
						b.set(count, b.get(i));
						b.set(i,a);
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
				row_element = function_multiplication(row_element,Arrays.asList(b.get(count)),inverse_a, p);
				padded_row_element = polynomial_padding(row_element, p); 
				b.set(count, padded_row_element.toArray(new Integer[0]));
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
					b_count = function_multiplication(b_count, Arrays.asList(b.get(count)), row_element, p);
					b_count = polynomial_padding(b_count, p);
					b.set(count, b_count.toArray(new Integer[0]));
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
					b_count = function_subtraction(b_count, Arrays.asList(b.get(count)), Arrays.asList(b.get(i)), p);
					b.set(i, b_count.toArray(new Integer[0]));
					b_count.clear(); 
					
					// Return the count row to its previous state
					List<Integer> inverse_count_row_element = function_inverse(Arrays.asList(count_row.get(count)), p);
					List<Integer> inverse_count_row_element_b = function_inverse(Arrays.asList(b.get(count)), p);
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
					b_count = function_multiplication(b_count, Arrays.asList(b.get(count)), inverse_count_row_element, p);
					b_count = polynomial_padding(b_count, p);
					b.set(count, b_count.toArray(new Integer[0]));
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
		
		int n=0, m=0; 
		 
		
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
			
			// Skip the input line 
			c0 = sc.nextLine();

			// Reading m line and parsing as integer 
			c0 = sc.nextLine();
			n = Integer.parseInt(c0.substring(2)); 
			
			// Reading m line and parsing as integer 
			c0 = sc.nextLine();
			m = Integer.parseInt(c0.substring(2)); 
			
			// Read the function f
			c0 = sc.nextLine();
			c0 = c0.substring(6, c0.length()-1);
			for(int i=0; i<c0.length(); i++) {
				f.add( (int) (c0.charAt(i) - 48) );
			}
			
			// Skip the A = line 
			c0 = sc.nextLine(); 
			// Line is 2D -> array of arrays
			for(int j=0; j<n; j++) {
				
				// Read the entire line and process indivdual elements separately within the next loop
				Integer[][] line = new Integer[n][n]; 
				String s = sc.nextLine(); 
				
				// Reading the specific functions within the line
				for(int k=0; k<m; k++) {
					
					Integer[] poly = new Integer[m]; 
					
					// System.out.println("Printing line " + s + "  Printing k=" + k);
					
					// Remove the first bracket
					s = s.substring(1); 
					// Read the polynomial
					for(int l = 0; l<m; l++) {
						//System.out.println(" s.charAt(" + l + ") = " + s.charAt());
						poly[l] = s.charAt(0) - 48; 
						s = s.substring(1); 
					}
					// Add polynomial to the list  
					// A.get(i).add(poly);
					// Remove the last bracket
					s = s.substring(1);
					
					line[k] = poly; 
				}
				A.add(Arrays.asList(line)); 						 
			}
			
			// Skip the b= line
			c0 = sc.nextLine(); 
			// Read b 
			for(int i=0; i<n ; i++) {
				Integer[] poly = new Integer[m]; 
				String s = sc.nextLine(); 
	     		s = s.substring(1);
				for(int j=0; j<m; j++) {
					poly[j] = (int) (s.charAt(j) - 48);
				}
				b.add(poly); 
			}
							
			// Close the scanner 
			sc.close(); 
			
		}
		else {
			
			Scanner sc = new Scanner(System.in);
			
			// Taking input of the intger values
			System.out.print("m=");	
			m = sc.nextInt(); 
			sc.nextLine();
			System.out.print("n=");
			n = sc.nextInt(); 
			sc.nextLine();
			
			// Take input of the function f
			System.out.print("f(X)=");
			String s = sc.nextLine(); 
			
     		s = s.substring(1, s.length()-1);
			for(int i=0; i<s.length(); i++) {
				f.add( (int) (s.charAt(i) - 48) );
			}
			
			// Take input of the matrix A
			System.out.println("A=");
			
			// Line is 2D -> array of arrays
				for(int j=0; j<n; j++) {
					
					// Read the entire line and process indivdual elements separately within the next loop
					Integer[][] line = new Integer[n][n]; 
					s = sc.nextLine(); 
					
					// Reading the specific functions within the line
					for(int k=0; k<m; k++) {
						
						Integer[] poly = new Integer[m]; 
						
						// Remove the first bracket
						s = s.substring(1); 
						// Read the polynomial
						for(int l = 0; l<m; l++) {
							poly[l] = s.charAt(0) - 48; 
							s = s.substring(1); 
						}
						// Add polynomial to the list  
						
						// Remove the last bracket
						s = s.substring(1);
						
						line[k] = poly; 
					}
					A.add(Arrays.asList(line)); 						 
				}

			
			// Take input of the b
			System.out.println("b=");
			for(int i=0; i<n ; i++) {
				Integer[] poly = new Integer[m]; 
				s = sc.nextLine(); 
	     		s = s.substring(1);
				for(int j=0; j<m; j++) {
					poly[j] = (int) (s.charAt(j) - 48);
				}
				b.add(poly); 
			}
			
			sc.close();
		}
		
		// Initialize unit and zero polynomials 
		polynomial_unit_initializer(m);
		polynomial_zero_initializer(m);
				
		// Create the combinations of polynomials to help in inverse calculation
		String blank_code = ""; 
		vector_creator(blank_code, m, 0, '1');
		
		// Convert the matrix into row echelon form
		A = row_echelon_convertor(A, m, n);
				
		// Now the adjusted b is bascially the solution so just print it 
		System.out.println("x=");
		b_printer(n, m);
		
		
	}	
}

//	実行結果
//	x=
//	[01001101]
//	[00010001]
//	[01110011]
//	[00010110]
//	[00001101]
//	[01011101]
//	[11001000]
//	[00001100]
