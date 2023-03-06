
// Kedare Harshvardhan 21B11198
// 課題 3.1
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_03_01.java」
// コードの実行方法 ：　「java kadai_21_11198_03_01 vec.txt」(入力ファイルあり)　または　「java kadai_21_11198_03_01」(入力ファイルなし)

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; 

public class kadai_21_11198_03_01 {

	public static void main(String[] args) {
		
		int asize= 0; 
		String c0="", c1="";   
				
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
			// Reading n line as string and parsing out as integer 
			c0 = sc.nextLine();
			asize = Integer.parseInt(c0.substring(2)); 
			// Reading string c0 and removing the first 3 chars
			c0 = sc.nextLine(); 
			c0 = c0.substring(3); 
			// Reading string c1 and removing the first 3 chars
			c1 = sc.nextLine(); 
			c1 = c1.substring(3); 
			
			sc.close(); 
		}
		else {
			Scanner sc = new Scanner(System.in); 
			System.out.print("n=");
			asize = sc.nextInt();
			// nextInt() does not read "\n" so we have to manually go to the next line 
			sc.nextLine();
			System.out.print("c0=");
			c0 = sc.nextLine(); 
			System.out.print("c1=");
			c1 = sc.nextLine(); 
			sc.close(); 
		}
						
		int diff = 0; // ハミング距離を表す変数
		
		for(int i=0; i<asize; i++) {
			if(c0.charAt(i) != c1.charAt(i)) {
				diff++; 
			}
		}
		
		System.out.println("d="+diff);
	}
	
}

// 実行結果
// d=521
