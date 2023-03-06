// Kedare Harshvardhan 21B11198
// 課題 4.4
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_04_04_poly.java」
// コードの実行方法 ：「java kadai_21_11198_04_04_poly」(入力ファイルなし)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class kadai_21_11198_04_04_poly {
	
	// 43 -> +
	// 45 -> -
	// 42 -> *
	// 47 -> /
	
	private static List<Integer[]> combinations = new ArrayList<Integer[]>(); 
	private static List<Integer> f = new ArrayList<Integer>(); 
	private static List<Integer> unit_polynomial =  new ArrayList<Integer>();
	
	public static void vector_creator(String code, int dimension, int size, char adduct) {
		
		// have to use recursion to create 2^dimension combinations
		
		if(size == dimension) {
			code += adduct;  
			// String c = Arrays.toString(code).replace(" ", "").replace(",", "");
			// System.out.println("Printing code" + code);
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
	
	public static void polynomial_unit_initializer(int m) {
				
		unit_polynomial.add(1);
		while(--m>0) unit_polynomial.add(0);
		
	}
	
	public static int degree(List<Integer> polynomial) {
		
		// int degree= polynomial.size()-1
		
		for(int i=polynomial.size()-1; i>-1; i--) {
			if(polynomial.get(i) == 1) return i; 
		}
		
		return 0; 
	}
	
	public static List<Integer> polynomial_reduce(List<Integer> polynomial, List<Integer> remainder) {
		
		int degree = degree(remainder);
		
		for(int i=0; i<degree+1; i++) {
			polynomial.add(remainder.get(i));
		}
		 
		return polynomial; 
	}
	
	public static List<Integer> function_addition(List<Integer> function, List<Integer> f, List<Integer> g) {
		
		// We assumr f has higher degree
		// Copy f in function
		for(int j=0; j<f.size(); j++) {
			function.add(f.get(j));
		}
		
		// System.out.println("size " + g.size());
		
		for(int i=0; i<g.size(); i++) {
			function.set(i, ( f.get(i) + g.get(i) )%2 );
		}
		
		return function; 
	}
	
	public static List<Integer> function_subtraction(List<Integer> function, List<Integer> f, List<Integer> g) {
		
		// Copy f in function
		for(int j=0; j<f.size(); j++) {
			function.add(f.get(j));
		}
		
		for(int i=0; i<g.size(); i++) {
			int value = f.get(i) - g.get(i); 
			if(value < 0) value += 2; 
			function.set(i, value%2 );
		}
		
		return function; 
	}
	
	public static List<Integer> function_modulo(List<Integer> function) {
		
		// we find the modulo of the function with degree higher than size
		
		List<Object> division_result = function_division(function, f);
		List<Integer> remainder = new ArrayList<Integer>(); 
		
//		//System.out.print(" mod ");
//		
//		return remainder; 
		
		String s = division_result.get(1).toString(); 
		s = s.replace(",", "").replace(" ", "").replace("[", "").replace("]", "");
		
		// Now, take the remainder and return it 
		
		// System.out.println("Printing the remainder " + s);
		
		for(int i=0; i<s.length(); i++) {
			remainder.add(s.charAt(i) - 48);
			// remainder.add(0);
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
			// System.out.println( "Print dummy2" + dummy2.toString() + " dummy " + dummy.toString() );
			
			// Making the lengths same for smaller products
			if(degree(dummy2) < m) {
				while(dummy2.size() < m) dummy2.add(0);
			}
			
			// Make comparison
			if( dummy2.equals(unit_polynomial) ) { 
					// System.out.println( "Returning this polynomial" + dummy.toString()  );
					return dummy; 
				}; 
			
			// If no match empty the lists for further iterations
			dummy.clear();
			dummy2.clear();
			
		}
		
		return dummy;  
	}
	
	public static List<Integer> function_multiplication(List<Integer> function, List<Integer> f, List<Integer> g, int size) {
		
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
		dummy = polynomial_reduce(dummy, function);
		function.clear();
		function.addAll(dummy);
		dummy.clear(); 
				
		// Now perform modulo on the function with highest degrees
		
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
			
			// System.out.println(" function ");
			
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
			
//			dummy2 = polynomial_reduce(dummy2, dummy);
//			dummy.clear();
//			dummy.addAll(dummy2);
//			dummy2.clear();
			//System.out.println("dummy " + dummy.toString().replace(" ", "").replace(",", "") + " size " + dummy.size() ); 
			//System.out.println("quotient " + quotient.toString().replace(" ", "").replace(",", "") + " size " + quotient.size()); 
			//System.out.println("dummy degree " + degree(dummy) + " quotient degree " + degree(quotient));
			
			// If dummy is longer we pass it as f or else as g 
			dummy2 = (degree(dummy) >= degree(quotient)) ? function_addition(dummy2, dummy, quotient) : function_addition(dummy2, quotient, dummy); 
			
			// Replace quotient
			quotient.clear();
			quotient.addAll(dummy2);
			// Remove contents in the dummy lists
			dummy2.clear(); 
			
			
			// Do the necessary operations on the remainder function
			dummy2 = function_multiplication(dummy2, g, dummy, -1);
			// dummy2 = scalar_multiplication(dummy2, -1); Not necessary since the modulus of -1 is 1 
			// Add f and ()x^()q(x) and store in remainder 
			remainder.clear();
			remainder = function_addition(remainder, f, dummy2);
			// Remove contents in the dummy lists
			dummy.clear(); 
			dummy2.clear(); 

			if(degree(remainder) < degree(g)) {
				// reducing the zero elements of the remainder
				dummy = polynomial_reduce(dummy, remainder); 
				return Arrays.asList(quotient, dummy);
			}
			else {
				f.clear();
				f.addAll(remainder);
			}
			
		}
		
		return Arrays.asList(quotient, remainder); 
	}
	
	public static void finite_field_operation(char operator, int m) {
	
		switch(operator) {
			case 43: System.out.print("ADD   |"); break;
			case 45: System.out.print("SUB   |"); break;
			case 42: System.out.print("MUL   |"); break;
			case 47: System.out.print("DIV   |"); break;
		}		
		
		// Printing the first line 
		for(int i=0; i<combinations.size(); i++) {
			System.out.print(" " +  Arrays.toString(combinations.get(i)).replace(" ", "").replace(",", "") + " ");
		}
		System.out.println("");
		for(int i=0; i<combinations.size()*(combinations.size()); i++) {
			System.out.print("-"); // 7
		}
		
		// Printing the next lines 
		System.out.println("");
		
		// Printing a line 
		for(int i=0; i<combinations.size(); i++) {
			
			System.out.print(Arrays.toString(combinations.get(i)).replace(" ", "").replace(",", "") + " |");
			
			List<Integer> f1 = new ArrayList<Integer>(); 
			int size = m;
			for(int p=0; p<size; p++) {
				f1.add(combinations.get(i)[p]);
			}
			
			// Printing the results of operations one by one 
			for(int j=0; j<combinations.size(); j++) {
				
				List<Integer> f2 = new ArrayList<Integer>(); 
				for(int p=0; p<size; p++) {
					f2.add(combinations.get(j)[p]);
				}
				
				List<Integer> dummy = new ArrayList<Integer>(); 
				
				switch(operator) {
					case 43: dummy = function_addition(dummy, f1, f2); break;
					case 45: dummy = function_subtraction(dummy, f1, f2); break;
					case 42: dummy = function_multiplication(dummy, f1, f2, size); break;
					case 47: dummy = function_multiplication(dummy, f1, function_inverse(f2, m), size); break;
				}	
				
				while(dummy.size() != m && size > 0) {
					dummy.add(0);
				}
				
				if(operator == 47 && Arrays.asList(combinations.get(j)).equals(Arrays.asList(combinations.get(0))) ) {
					String s = ""; 
					for(int k = 0; k<m; k++) s += '-'; 
					System.out.print("  " + s + "  ");
				}else {
					System.out.print(" " + dummy.toString().replace(" ", "").replace(",", "") + " ");
				}
				
				// System.out.print(" " + dummy.toString().replace(" ", "").replace(",", "") + " ");
				
				dummy.clear(); 
				
				//System.out.print(" " +  Arrays.toString(combinations.get(j)).replace(" ", "").replace(",", "") + " ");
			}
			
			
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static void main(String args[]) {
		
		int m=0; 
		
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
			// Reading m line and parsing as integer 
			c0 = sc.nextLine();
			m = Integer.parseInt(c0.substring(2)); 
			
			// Read the function f
			c0 = sc.nextLine();
			c0 = c0.substring(1, c0.length()-1);
			for(int i=0; i<c0.length(); i++) {
				f.add( (int) (c0.charAt(i) - 48) );
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
			
			// Take input of the function f
			System.out.print("f(X)=");
			String s = sc.nextLine(); 
			
     		s = s.substring(1, s.length()-1);
			for(int i=0; i<s.length(); i++) {
				f.add( (int) (s.charAt(i) - 48) );
			}
			
			sc.close();
		}
		
		System.out.println(""); 
		String blank_code = ""; 
		int code_size = (int) Math.pow(2, m);
		
		// Won't use List since they are pass by reference 
		// Integer[] blank_code = new Integer[m]; 
		
		// Create all the combinations for the given size
		vector_creator(blank_code, m, 0, '1');
		
		polynomial_unit_initializer(m);

		List<Integer> random = new ArrayList<Integer>(); 
		random = Arrays.asList(combinations.get(2));
		
		// Function operations 
		finite_field_operation('+', m);
		finite_field_operation('*', m);
		finite_field_operation('-', m);
		finite_field_operation('/', m);
	
	}
}

// 実行結果
//	[00100001]*[11111100]= [0, 0, 1, 1, 0, 1]
//	[00010101]*[10101111]= [1, 0, 1, 1, 0, 1, 1]
//	[00111100]*[00111011]= [0, 1, 0, 0, 0, 0, 1, 1]
//	[00000011]/[01110110]= [1, 1, 0, 1, 1, 0, 0, 1]
//	[10100000]/[00110001]= [1, 0, 1, 0, 0, 1, 0, 1]
//	[10111011]/[00010100]= [1, 1, 0, 0, 0, 0, 1, 1]
