// Kedare Harshvardhan 21B11198
// 課題 4.2
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_04_02_hakidashi_Fp.java」
// コードの実行方法 ：　「java kadai_21_11198_04_02_hakidashi_Fp linear_equation_Fp.txt」(入力ファイルあり)　または　「java kadai_21_11198_04_02_hakidashi_Fp」(入力ファイルなし)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner; 

public class kadai_21_11198_04_02_hakidashi_Fp {
	
	// Declaring as instance variable so that we don't have to pass it to modify
	private static List<Integer> vector = new ArrayList<Integer>(); 
	
	// Turn the matrix in row echelon form
	// Going diagonal-wise, if the element is not zero, then turn to one, and make other rows as zero
	// If the element is zero, then search for non-zero row below it and swap
	// Repeat until diagonally last element is reached
	
	public static Integer[] row_divide(Integer[] row, int p, int n, int count) {
		
		// Mechanism - we divide the whole row based on a value passed in parameters
		
		// Element of row wrt which we want to normalize 
		int element = row[count]; 
		int inverse = 0;
				
		// Finding the inverse of element
		
		for(int i=0; i<p; i++) {
			if( (i*element)%p == 1 ) {
				inverse = i; 
				break; 
			}
		}
		
		// Multiplying all the elements of the matrix row with the inverse 
		
		for(int i=0; i<n; i++) {
			int val = row[i]; 
			val = val*inverse; 
			val = val%p; 
			row[i] = val; 
		}
		
		// Multiplying element of the vector with the inverse 
		int v = vector.get(count); 
		v = (v*inverse)%p; 
		vector.set(count, v); 
				
		return row; 
	}
	
	public static Integer[] rows_subtract(Integer[] diagonal_row, Integer[] row, int p, int n) {
		
		int value; 
		
		for(int i=0; i<n; i++) {
			value = 0; 
			value = row[i] - diagonal_row[i]; 
			if(value < 0) value += p; 
			row[i] = value; 
		}
				
		return row; 
	}
	
	public static Integer[] row_multiply(Integer[] row, int p, int n, int value) {
		
		for(int i=0; i<n; i++) {
			row[i] = (row[i]*value)%p; 
		}
		return row; 
	}
	
	public static List<Integer[]> row_echelon_convertor(List<Integer[]> matrix, int p, int n) {
		
		int count = 0; 
		while(count < n){
			
			// Step 1
			// This step swaps a zero row with a non-zero row 
			
			if(matrix.get(count)[count] == 0) {
				boolean found = false; 
				int i = count; 
				while(!found && i < n) {
										
					if(matrix.get(i)[count] != 0) {
						// For matrix found the row with 1, now swap the rows
						Integer[] swap = matrix.get(count); 
						matrix.set(count, matrix.get(i));
						matrix.set(i, swap); 
						// exit the loop 
						found = true; 
						
						// Swap the rows in vector
						int a = vector.get(count); 
						vector.set(count, vector.get(i));
						vector.set(i,a);
					}
					i++; 
				}
			}
			
			// Step 2 
			// In this step, we make the diagonal element 1 and divide the entire row to do so 
			
			if(matrix.get(count)[count] != 1) {
				
				Integer[] row = matrix.get(count); 
				
				// Divide the rows in matrix and the vector
				row = row_divide(row, p, n, count); 
				
			}
			
			// Step 3 
			// In this step, we make the subtract the row of diagonal element to other rows 
			// and hence make them zero 
						
			int i = 0; 
			while(i < n){
				
				// looping through all the rows to perform row operation
				// skip if the row is count row or is has count element 0 
				
				
				if(i != count && matrix.get(i)[count] != 0) { 
					// perform row operations every row with 1 in the column: Ri = Rcount - Ri
					
					int a = matrix.get(i)[count]; 
					int b = matrix.get(count)[count]; 
										
					// multiply the row, subtract the rows, then divide the row again
					Integer[] row = matrix.get(count); 
					
					row = row_multiply(row, p, n, a);
					vector.set(count, (vector.get(count)*a)%p );
					
					row = rows_subtract(row, matrix.get(i), p, n);
					matrix.set(i, row); 
					int val =  vector.get(i) - vector.get(count); 
					if(val < 0) val += p; 
					vector.set(i, val);
															
					row = row_divide(matrix.get(count), p, n, count); 
					matrix.set(count, row); 
					
				}
				i++;
				// skip the procedure 
			}
			count++; 
		}
		
		return matrix; 
	}

	public static void main(String args[]) {
		
		int p=0, n=0; 
	    List<Integer[]> matrix = new ArrayList<Integer[]>(); 
		
		if(args.length == 1) {
			// コマンドにファイル名が渡される場合
			String filename = args[0]; 
			File f = new File(filename);
			String c0 = ""; 
			Scanner sc = new Scanner(System.in); 
			try {
				sc = new Scanner(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			// Reading n & M lines and parsing each of them as integers 
			c0 = sc.nextLine();
			p = Integer.parseInt(c0.substring(2)); 
			c0 = sc.nextLine();
			n = Integer.parseInt(c0.substring(2)); 
			
			// Skip the A= line 
			c0 = sc.nextLine();
			
			// Read the matrix 
			for(int i=0; i<n; i++){
				Integer row[] = new Integer[n]; 
				c0 = sc.nextLine();
				for(int j=0; j<n; j++) {
					row[j] = c0.charAt(j) - 48; 
				}
				matrix.add(row);
				// sc.nextLine(); 
			}
			
			// Skip the b= line 
			c0 = sc.nextLine();
			
			// Read the matrix 
			for(int i=0; i<n; i++){
				vector.add(sc.nextInt());
				sc.nextLine(); 
			}
	
			// Close the scanner 
			sc.close(); 
		}
		
		else {
			Scanner sc = new Scanner(System.in);
			// Taking input of the intger values
			System.out.print("p=");	
			p = sc.nextInt(); 
			sc.nextLine();
			System.out.print("n=");
			n = sc.nextInt(); 
			sc.nextLine();
			
			// Take input of the matrix
			System.out.println("A=");
			for(int i=0; i<n; i++) {
				Integer[] dummy = new Integer[n];
				Arrays.fill(dummy, 0);
				for(int j=0; j<n; j++) {
					dummy[j] = sc.nextInt();
				}
				sc.nextLine(); 
				matrix.add(dummy);
			}
			
			// Take input of the vector
			System.out.println("b=");
			for(int i=0; i<n; i++) {
				vector.add(sc.nextInt()); 
				sc.nextLine(); 
			}
		}
		
		matrix = row_echelon_convertor(matrix, p, n);
		
		// Test if converted correctly 
//		for(int i=0; i<n;i++) { 
//			Integer[] dummy = matrix.get(i);
//			for(int j=0; j<n; j++) {
//				System.out.print(dummy[j] + ", ");
//			}
//			System.out.println("");
//		}
		
		System.out.println("x=");
		for(int i=0; i<n; i++) {
			System.out.println(vector.get(i));
		}
		
	}
}

//実行結果
//x=
//5
//0
//4
//2
//5
//0
//5
//6
//1
//6
