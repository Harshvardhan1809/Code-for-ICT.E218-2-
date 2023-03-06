// Kedare Harshvardhan 21B11198
// 課題 4.3
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_04_03_poly.java」
// コードの実行方法 ：　「java kadai_21_11198_04_03_poly poly.txt」(入力ファイルあり)　または　「java kadai_21_11198_04_03_poly」(入力ファイルなし)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class kadai_21_11198_04_03_poly {

	// Important: [1011] stands for 1 + x^2 + x^3 
	// n=13
	// m=9
	// [11001111100111]
	// [0011011111]
	
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
	
	public static List<Integer> scalar_multiplication(List<Integer> function, int constant) {
		
		Integer[] powers = new Integer[function.size()]; 

		int value; 
		
		// Multiply each element of function with constant 
		for(int i=0; i<function.size(); i++) {
				value = function.get(i); 
				value = (constant*value)%2; 
				powers[i] = value; 
		}
		
		// Copy the result in List 
		for(int i=0; i<powers.length; i++) {
			powers[i] = powers[i]%2; 
			function.add(powers[i]);
		}
		
		return function; 
	}
	
	public static List<Integer> function_multiplication(List<Integer> function, List<Integer> f, List<Integer> g) {
		
		Integer[] powers = new Integer[f.size() + g.size()-1]; 
		int value; 
		Arrays.fill(powers, 0); 
		
		for(int i=0; i<f.size(); i++) {
			for(int j=0; j<g.size(); j++) {
				value = f.get(i)*g.get(j); 
				value = value%2; 
				powers[i+j] += value; 
			}
		}
		
		for(int i=0; i<powers.length; i++) {
			powers[i] = powers[i]%2; 
			function.add(powers[i]);
		}
		
		return function; 
	}
	
	public static int degree(List<Integer> polynomial) {
				
		int degree= polynomial.size()-1;
		
		for(int i=polynomial.size()-1; i>-1; i--) {
			if(polynomial.get(i) == 1) return i; 
		}
		
		return degree; 
	}
	
	public static List<Integer> polynomial_reduce(List<Integer> polynomial, List<Integer> remainder) {
		
		int degree = degree(remainder);
		
		for(int i=0; i<degree+1; i++) {
			polynomial.add(remainder.get(i));
		}
		 
		return polynomial; 
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
			dummy2 = (degree(dummy) > degree(quotient)) ? function_addition(dummy2, dummy, quotient) : function_addition(dummy2, quotient, dummy); 
			
			// Replace quotient
			quotient.clear();
			quotient.addAll(dummy2);
			// Remove contents in the dummy lists
			dummy2.clear();
			
			// Do the necessary operations on the remainder function
			dummy2 = function_multiplication(dummy2, g, dummy);
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

	public static void main(String args[]) {
		
		int n=0, m=0; 
		List<Integer> f = new ArrayList<Integer>(); 
		List<Integer> g = new ArrayList<Integer>(); 
		
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
			// Reading p & n lines and parsing each of them as integers 
			c0 = sc.nextLine();
			n = Integer.parseInt(c0.substring(2)); 
			c0 = sc.nextLine();
			m = Integer.parseInt(c0.substring(2)); 
			
			// Read the function f
			c0 = sc.nextLine();
			c0 = c0.substring(1, c0.length()-1);
			for(int i=0; i<c0.length(); i++) {
				f.add( (int) (c0.charAt(i) - 48) );
			}
			
			// Read the function g
			c0 = sc.nextLine();
			c0 = c0.substring(1, c0.length()-1);
			for(int i=0; i<c0.length(); i++) {
				g.add( (int) (c0.charAt(i) - 48) );
			}
				
			// Close the scanner 
			sc.close(); 
			
		}
		else {
			
			Scanner sc = new Scanner(System.in);
			// Taking input of the intger values
			System.out.print("n=");	
			n = sc.nextInt(); 
			sc.nextLine();
			System.out.print("m=");
			m = sc.nextInt(); 
			sc.nextLine();
			
			// Take input of the function f
			System.out.print("f=");
			String s = sc.nextLine(); 
			
     		s = s.substring(1, s.length()-1);
			for(int i=0; i<s.length(); i++) {
				f.add( (int) (s.charAt(i) - 48) );
			}
			
			// Take input of the function g
			System.out.print("g=");
			s = sc.nextLine();
			s = s.substring(1, s.length()-1);

			for(int i=0; i<s.length(); i++) {
				g.add( (int) (s.charAt(i) - 48) );
			}
			
			sc.close();
		}

		List<Integer> function = new ArrayList<Integer>(); 
		
		System.out.print("f+g=");
		function = function_addition(function, f, g);
		System.out.println(function.toString().replace(", " ,""));
		function.removeAll(function);
		
		System.out.print("f-g=");
		function = function_subtraction(function, f, g);
		System.out.println(function.toString().replace(", " ,""));
		function.removeAll(function);
		
		System.out.print("f*g=");
		function = function_multiplication(function, f, g);
		System.out.println(function.toString().replace(", " ,""));
		function.removeAll(function);
		
		System.out.println("f/g= ");
		List<Object> division_result = function_division(f, g);
		System.out.print("商 " + division_result.get(0).toString().replace(", " ,"") + " ");
		System.out.print("剰余 " + division_result.get(1).toString().replace(", " ,"") + " ");

	}
}

//	実行結果
//	f+g=[0011010011100110101110100011]
//	f-g=[0011010011100110101110100011]
//	f*g=[100010100110001101000110001110000010011001000111]
//	f/g= 
//	商 [11110111] 剰余 [01100110101000000111]



