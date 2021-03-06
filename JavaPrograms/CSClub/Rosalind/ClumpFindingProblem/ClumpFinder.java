import java.util.*;

public class ClumpFinder
{
	public static void main (String[] args)
	{
		String genome = "CGGACTCGACAGATGTGAAGAAATGTGAAGACTGAGTGAAGAGAAGAGGAAACACGACACGACATTGCGACATAATGTACGAATGTAATGTGCCTATGGC";
		System.out.println(findClumps(genome, 5, 4, 75));
	}
	
	public static String findClumps (String genome, int k, int t, int L)
	{
		Hashtable clumps = new Hashtable();
		String kmers = ": ";
		
		//int first_of_last = 0;
		
		String window = genome.substring(0, 75);
		String clump = null;
		
		// Runs through first window in genome
		for (int i = 0; i < window.length() - k; i ++)
		{
			clump = window.substring(i, i + k);
			if (clumps.containsKey(clump))
			{
				clumps.put(clump, (int) clumps.get(clump) + 1);
			}
			else
			{
				clumps.put(clump, 1);
			}
			//first_of_last = i;
		}
		
		Enumeration list_of_clumps = clumps.keys();
		// Adds all the clumps, which appear t or more times, to String kmers
		while (list_of_clumps.hasMoreElements())
		{
			String current_clump = (String) list_of_clumps.nextElement();
			if (!kmers.contains(current_clump) && (int) clumps.get(current_clump) >= t)
				kmers = kmers + current_clump + " ";
		}
		
		for (int i = 1; i < genome.length() - L; i ++)
		{
			String first_clump = genome.substring(i - 1, i - 1 + k);
			//System.out.println("first: " + first_clump);
			clumps.put(first_clump, (int) clumps.get(first_clump) - 1);
			String last_clump = genome.substring(i + L - k, i + L);
			//System.out.println("last: " + last_clump);
			//if (last_clump.equals("AATGT"))
			//		System.out.println(last_clump);
			/*if (last_clump.equals("AATGT"))
			{System.out.println("HERE");
				clumps.put(last_clump, (int) clumps.get(last_clump) + 1);
			}*/
			if (clumps.contains(last_clump))
			{
				if (last_clump.equals("AATGT"))
					System.out.println("ERROROROROROROROROR");
				clumps.put(last_clump, (int) clumps.get(last_clump) + 1);
			}
			else
			{
				if (last_clump.equals("AATGT"))
					System.out.println("ERROROROROROROROROR");
				clumps.put(last_clump, 1);
			}
			
			list_of_clumps = clumps.keys();
		
			while (list_of_clumps.hasMoreElements())
			{
				String current_clump = (String) list_of_clumps.nextElement();
				if (current_clump.equals("AATGT"))
					System.out.println(current_clump + ": " + clumps.get(current_clump));
				if (!kmers.contains(current_clump) && (int) clumps.get(current_clump) >= t)
					kmers = kmers + current_clump + " ";
			}
		}
		
		return (kmers);
		
	}
}
