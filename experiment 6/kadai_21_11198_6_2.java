
// Kedare Harshvardhan 21B11198
// 課題 6.2
// コードのコンパイル方法
// ターミナルでファイルのダイレクトリーに入って、「javac kadai_21_11198_6_2.java」
// コードの実行方法 ：「java kadai_21_11198_6_2」(入力ファイルなし)
// コードを実行すると、暗号化・復号化の計算過程が出力される

import java.lang.Math;

public class kadai_21_11198_6_2 {

    /*
    
    First, we calculate the value of n using p & q {n = p*q = 119}
    Then we calculate the value of phi(n) = (p-1)(q-1) = 96

    Finding e such that gcd(phi(n), e) = 1; we get e = 5;
    Finding d such that e = d^(-1) mod(phi(n)); we get d = 77

    For encryption, we find c = m^(e) mod n by 
    i) Finding m^(e) = (19)^(5) = 2476099
    ii) Dividing m^(e) by n by subtracting a lot of times
    iii) Finding the remainder

    For decryption, we find m = c^(d) mod n by using 高速べき乗剰余演算 
        
    */

    public static void main(String[] args) {

        int M = 19;
        int e = 5;
        int n = 119;

        int power = (int) Math.pow(M, e);
        // System.out.println(power);
        int val = power;

        System.out.println("----------暗号化---------");

        String a = String.format("\nn = %d , e = %d , M = %d , c = ?? ", n, e, M);
        System.out.println(a);
        System.out.println("\n計算過程");
        int counter = 0;
        // System.out.println(power + " - " + n + " = " + (power -= n));
        int quotient = 0;
        while (power >= n) {
            quotient++;
            if (counter++ < 5)
                System.out.println(power + " - " + n + " = " + (power -= n) + "    " + quotient);
            if (counter > 5 && counter++ < 11)
                System.out.println(".");
            power -= n;
        }
        System.out.println((power += n) + " - " + n + " = " + (power -= n) + "    " + quotient);

        System.out.println("\nユークリッドの補題 " + val + " = " + quotient + "*" + n + " + " + power);
        System.out.println("\n計算結果");

        System.out.println("c = " + power);
        // System.out.println(code);

        System.out.println("\n----------復号化---------\n");

        int d = 77;

        a = String.format("n = %d , d = %d , c = %d , m = ?? \n", n, d, power);
        System.out.println(a);

        System.out.println("計算過程");

        // 高速べき乗剰余演算を用いる

        int res = power;
        int value = 1;
        int exp = 0;
        int i = 0;
        String binary = Integer.toBinaryString(d);
        // System.out.println(binary);
        int len = binary.length();
        while (i < len) {

            if (binary.charAt(i) == '1') {
                if (i != 0) {
                    value = (int) Math.pow(value, 2);
                    value = value % n;
                    exp = exp * 2;
                }
                value = (value * res) % n;
                exp++;
                i++;
            } else {
                value = (int) Math.pow(value, 2);
                value = value % n;
                exp = exp * 2;
                i++;
            }
            System.out.println("c^" + exp + " mod N = " + value);
        }

        System.out.println("\n計算結果");
        System.out.println("m = " + value);

    }

}