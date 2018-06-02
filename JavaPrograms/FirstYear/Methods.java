class HowToClass
{
	public static void twoStrings (String string)
	{
		System.out.println (string);
		System.out.println (string + " Again.");
	}

	public static void skipLine ()
	{
		System.out.println();
	}

	public static void skip3Lines ()
	{
		skipLine(); skipLine(); skipLine();	//Combining 3 statements into 1 line.	
	}

	public static void main (String[] args)
	{
		twoStrings("Shall I repeat myself?");
		skip3Lines();
		System.out.println("We're done here.");
		skipLine();
	}
}
