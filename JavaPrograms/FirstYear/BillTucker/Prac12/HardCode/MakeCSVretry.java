import java.io.*;
import java.util.*;

/**
* MakeCSV program transfers the objects (of type Employee) from a binary file (.dat) to a text file (.csv).
* @author Joshua Abraham - 3475896
* @param Raddo Scanner object to ask the user for input
* @param inputStream ObjectInputStream object to open the binary file (.dat)
* @param outputStream ObjectOutputStream object to open the text file (.csv)
* @param employees[] a list of Employee objects, of size 10
* @param datFile a String for the name of the binary file
* @param CSVFile a String for the name of the text file
* @param index an integer to keep track of how many employees are read from the binary file
*/
public class MakeCSV
{
	private static Scanner Reddo = new Scanner(System.in);
	private static ObjectInputStream inputStream = null;
	private static ObjectOutputStream outputStream = null;
	private static Employee employees[] = new Employee [10];
	private static String datFile, CSVFile;
	private static int index = 0;
	
	/**
	* The main method of the MakeCSV class. Asks the user for the names of the binary and text files respectively.
	* Uses the names of the files to call on the ReadData method and then the WriteData method after sorting the list of employees.
	*/
	public static void main (String[] args)
	{
		System.out.print("Input file: ");
		datFile = Reddo.next();
		
		System.out.println("Output file: ");
		CSVFile = Reddo.next();
		
		
		try
		{
			File new_file = new File (datFile);
			if (!new_file.exists())
				throw new FileNotFoundException();
				
			inputStream = new ObjectInputStream(new FileInputStream(datFile));
			
			boolean keep_calm_and_carry_on = true;
			// While loop to keep reading objects from the binary file until the list is full or there are no more objects to read.
			while (keep_calm_and_carry_on)
			{
				if (index >= 10)
					throw new ArrayFullException();
					
				employees [index] = (Employee) inputStream.readObject();
				index++;
			}
			inputStream.close();
		}
		
		// FIleNotFoundException catch block asks for a new binary file name and calls the ReadData method again with the new name
		catch (FileNotFoundException e)
		{
			System.out.println("FileNotFoundException");
		}
		// EOFException catch block sorts current employee list and proceeds to call the WriteData method
		catch (EOFException e)
		{
			System.out.println("EOFException");
			
			Arrays.sort(employees,0,count);
			WriteData(CSVFile);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("ClassNotFoundException");
			System.exit(0);
		}
		catch (InvalidClassException e)
		{
			System.out.println("InvalidClassException");
			System.exit(0);
		}
		catch (OptionalDataException e)
		{
			System.out.println("OptionalDataException");
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("IOException");
			System.exit(0);
		}
		// ArrayFullException catch block sorts current employee list and proceeds to call the WriteData method
		catch (ArrayFullException e)
		{
			System.out.println("ArrayFullException");
			
			Arrays.sort(employees);
			WriteData(CSVFile);
		}
		
		try
		{
			outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
			
			while (index >= 0)
			{
				outputStream.writeObject(employees [index]);
				index--;
			}
			outputStream.close();
		}
		
		catch (FileNotFoundException e)
		{
			System.out.println("FileNotFoundException");
			
			System.out.println("Name of CSV file: ");
			CSVFile = Reddo.next();
			WriteData(CSVFile);
		}
		catch (IOException e)
		{
			System.out.println("IOException");
			System.exit(0);
		}
		
	}
}
