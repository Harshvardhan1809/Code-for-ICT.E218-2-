/*

KEDARE HARSHVARDHAN 21B11198

Compile instructions : javac kadai_5_5_xor_dump.java 
Execution instruction : java kadai_5_5_xor_dump dump.txt dump_5_5.txt cipher.txt cipher_modified.txt

注意：実行の際には、実行手順で記載した全てのファイルを同じダイレクトリーに含むこと
注意：Java言語ではファイル名とファイル内に定義されたクラスの名が同じであるべきなので、変更したらエラーになる
注意：考察は別のテキストファイルに書いてある

*/

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class kadai_5_5_xor_dump {

    // HEX operations needed - read input as hex/store in hex array (or store as
    // string and convert to hex while XOR)
    public static void main(String[] args) {

        // File for 課題 5_4 dump.txt
        String file_5_4 = args[0];
        File f_5_4 = new File(file_5_4);
        List<Integer> cipher_5_4 = new ArrayList<Integer>();
        try {
            Scanner sc = new Scanner(f_5_4);
            while (sc.hasNextLine()) {

                // Skip the line_number: part
                sc.next();

                // Now read all the 16 numbers in the line
                for (int i = 0; i < 16; i++) {
                    String hex = "0x" + sc.next();
                    cipher_5_4.add(Integer.decode(hex));
                }

                // Move to the next line
                sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // File for 課題 5_5 dump_5_5.txt
        String file_5_5 = args[1];
        File f_5_5 = new File(file_5_5);
        List<Integer> cipher_5_5 = new ArrayList<Integer>();
        try {
            Scanner sc = new Scanner(f_5_5);
            while (sc.hasNextLine()) {

                // Skip the line_number: part
                sc.next();

                // Now read all the 16 numbers in the line
                for (int i = 0; i < 16; i++) {
                    String hex = "0x" + sc.next();
                    cipher_5_5.add(Integer.decode(hex));
                }

                // Move to the next line
                sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // File for substitution cipher cipher.txt
        String file_substitution = args[2];
        File f_substitution = new File(file_substitution);
        List<Integer> cipher_substitution = new ArrayList<Integer>();
        try {
            Scanner sc = new Scanner(f_substitution);
            while (sc.hasNext()) {

                // Read each element
                String c = sc.next();
                int strlen = c.length();
                for (int i = 0; i < strlen; i++) {
                    cipher_substitution.add((int) c.charAt(i));
                }

                // Move to the next line
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // File for modified substitution cipher cipher_modified.txt
        String file_substitution_modified = args[3];
        File f_substitution_modified = new File(file_substitution_modified);
        List<Integer> cipher_substitution_modified = new ArrayList<Integer>();
        try {
            Scanner sc = new Scanner(f_substitution_modified);
            while (sc.hasNext()) {

                // Read each element
                String c = sc.next();
                int strlen = c.length();
                for (int i = 0; i < strlen; i++) {
                    cipher_substitution_modified.add((int) c.charAt(i));
                }

                // Move to the next line
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Now perform the XOR operation

        List<Integer> xor_result = new ArrayList<Integer>();
        // XOR each element of block ciphers and place in the list
        int size = cipher_5_4.size();
        for (int i = 0; i < size; i++) {
            xor_result.add(cipher_5_4.get(i) ^ cipher_5_5.get(i));
        }

        List<Integer> xor_result_subsitution = new ArrayList<Integer>();
        // XOR each element of substitution ciphers and place in the list
        int size_1 = cipher_substitution_modified.size();
        for (int i = 0; i < size_1; i++) {
            xor_result_subsitution.add(cipher_substitution.get(i) ^
                    cipher_substitution_modified.get(i));
        }

        // Now print the results side-by-side
        System.out.println("index   block_cipher    substitution_cipher");
        for (int i = 0; i < size_1; i++) {
            System.out.println(i + "    " + xor_result.get(i) + "    " + xor_result_subsitution.get(i));
        }
        for (int i = size_1; i < size; i++) {
            System.out.println(i + "    " + xor_result.get(i) + "    ");
        }

        // Dump the result in a file
        File xor_dump = new File("xor_dump.txt");
        try {
            xor_dump.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter dumper = new FileWriter("xor_dump.txt")) {

            // Write the first line
            dumper.write("index       block_cipher       substitution_cipher\n");

            // Write the next lines
            for (int i = 0; i < size_1; i++) {
                String a = String.format("%5d %15d %15d \n", (i + 1), xor_result.get(i),
                        xor_result_subsitution.get(i));
                // dumper.write(i + " " + xor_result.get(i) + " " +
                // xor_result_subsitution.get(i) + "\n");
                dumper.write(a);
            }
            for (int i = size_1; i < size; i++) {
                String a = String.format("%5d %15d \n", (i + 1), xor_result.get(i));
                // dumper.write((i + 1) + " " + xor_result.get(i) + " " + "\n");
                dumper.write(a);
            }
            dumper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}