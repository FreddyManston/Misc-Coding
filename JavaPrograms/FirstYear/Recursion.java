class Recursions
{	

	public static void main (String[] args)
	{
		countdown (6);
	}
	
	public static void countdown (int time)
	{
		if (time == 0)
		{	
			System.out.println ("Blastoff!!!");
		}
		
		else if (time < 0)		//If time entered was a negative:
		{
			int newtime = -1 * time;
			System.out.println ("We had to Blastoff!!! " + newtime + " seconds ago.");
		}
		
		else
		{
			System.out.println (time);
			countdown (time - 1);		//Causes countdown method to recure until time equals zero.
		}
		
	}

}
