import java.io.*;
import java.util.*;

public class KMPPatternMatching {
	public static void main (String[] args) {
		try {	
			File text_file = new File("lesmiserables.text");
			File pat_file = new File("patterns.text");
			FileInputStream text_input = new FileInputStream(text_file);
			FileInputStream pat_input = new FileInputStream(pat_file);

			byte [] text_data = new byte[(int) text_file.length()];
			byte [] pat_data = new byte[(int) pat_file.length()];
			text_input.read(text_data);
			text_input.close();
			pat_input.read(pat_data);
			pat_input.close();

			String book = new String(text_data, "UTF-8");
			String patterns = new String(pat_data, "UTF-8");
			
			String [] text = book.split(" ");
			String [] pat = patterns.split(" ");
			
			int counter = 0;
			for (int i = 0; i < pat.length; i ++) {
				System.out.println(i);
				System.out.println(matchKMP(text, pat [i]));
				//if (matchKMP(text, pat [i]) >= 0) { counter ++; }
			}
			System.out.println(counter);
			
			/*String patt = "arp";
			String [] textt = new String [1];
			textt [0] = "The Rabin-Karp method is much faster that the brute-force method.";
			System.out.println(matchKMP(textt, patt));*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int matchKMP(String [] text, String pat) {
		if (pat.equals("A.")) {System.exit(0);}
		System.out.println("This is pat: " + pat);
		int pat_len = pat.length();
		int j = 0, k = 0, word_len, next[] = new int [pat_len + 1];
		String word;

		for (int i = 0; i < text.length; i ++) {
			word = text [i];
			System.out.println(word);
			word_len = word.length();
			
			if (pat_len == 0) { return 0; }
			else if (word_len == 0) { return -1; }
			else if (pat_len > word_len) { return -1; }
		
			buildNext(pat, next);
		
			while (j < word_len) {
				if (pat.charAt(k) == word.charAt(j)) { j ++; k ++; }
				else {
					k = next [k];
					if (k < 0) { k = 0; j ++; }
				}
				if (k == pat_len) { return j - pat_len; }; 
			}
		}
		return -1;
	}
	
	public static void buildNext(String pat, int[] next) {
		int pat_len = pat.length();
		int i, j;

		next [0] = -2;
		next [1] = -1;

		for (i = 2; i < pat_len; i ++) {
			j = next [i - 1] + 1;
			
			while (j >= 0 && pat.charAt(i - 1) != pat.charAt(j)) {
				j = next [j] + 1;
			}
			next [i] = j;
		}
		for (j = 0; j < pat_len; j ++) { next [j] ++; }
	}	
}