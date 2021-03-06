import java.util.Scanner;

public class Utility
{
	public static boolean isGreater(int num1, int num2)
	{
		if (num1 > num2)
			return (true);
		else
			return (false);
	}
	
	/**
	* What if the numbers are the same value??
	*/
	public static int minimumOf(int num1, int num2)
	{
		if (isGreater(num1, num2))
			return (num1);
		else 
			return (num2);
	}
	
	public static void main (String[] args)
	{
		Car start_program = new Car();
		start_program.readInput();
	}
}

class Car
{
	private String manufacturer, model;
	private int year, max_speed, engine_size;
	private static int counter;
	
	public Car()
	{
		counter++;
	}
	
	public Car(String new_manufacturer, String new_model, int new_year, int new_max, int new_engine)
	{
		manufacturer = new_manufacturer;
		model = new_model;
		year = new_year;
		max_speed = new_max;
		engine_size = new_engine;
		
		counter++;
	}
	
	public void setManufacturer(String new_manufacturer)
	{
		manufacturer = new_manufacturer;
	}
	
	public void setModel(String new_model)
	{
		model = new_model;
	}
	
	public void setYear(int new_year)
	{
		year = new_year;
	}
	
	public void setMaxSpeed(int new_max)
	{
		max_speed = new_max;
	}
	
	public void setEngineSize(int new_engine)
	{
		engine_size = new_engine;
	}
	
	public void setCounter(int count)
	{
		counter = count;
	}
	
	public String getManufacturer()
	{
		return (manufacturer);
	}
	
	public String getModel()
	{
		return (model);
	}
	
	public int getYear()
	{
		return (year);
	}
	
	public int getMaxSpeed()
	{
		return (max_speed);
	}
	
	public int getEngineSize()
	{
		return (engine_size);
	}
	
	public int getCounter()
	{
		return (counter);
	}
	
	public String toString()
	{
		return ("\nManufacturer: " + manufacturer + "\nModel: " + model + "\nYear: " + year + "\nMaximum speed: " + max_speed + "km/h" + "\nSize of engine: " + engine_size + "L\n");
	}
	
	public void readInput()
	{
		Scanner Reddo = new Scanner(System.in);
		
		System.out.print("How many cars are going to be maufactured? ");
		int cars_to_make = Reddo.nextInt();
		
		Car new_car[] = new Car[cars_to_make];
		
		for (int i = 0; i < cars_to_make; i++)
		{
			System.out.println("Car " + (i + 1) + ": ");
			System.out.print("Please enter the vehicle's manufacturer: ");
			String manufacturer = Reddo.next();
			System.out.print("Please enter the model of the vehicle: ");
			String model = Reddo.next();
			System.out.print("Please enter the year the vehicle was produced: ");
			int year = Reddo.nextInt();
			System.out.print("Please enter the maximum speed, in km/h, of the vehicle: ");
			int max_speed = Reddo.nextInt();
			System.out.print("Please enter the vehicle's engine capacity, in litres: ");
			int engine_size = Reddo.nextInt();
			
			new_car[i] = new Car(manufacturer, model, year, max_speed, engine_size);
		}
		
		Analysis new_object = new Analysis(new_car);	
	}
	
	public void writeOutput()
	{
		System.out.println(toString());
	}
	
	public boolean isEqual(Car automobile)
	{
		// If and else conditionals to check the manufacturer variable of the two.
		if (manufacturer.equalsIgnoreCase(automobile.manufacturer))
		{
			// Do nothing.	
		}
		else
		{
			return (false);
		}
		
		// If and else conditionals to check the model variable of the two.
		if (model.equalsIgnoreCase(automobile.model))
		{
			// Do nothing.	
		}
		else
		{
			return (false);
		}
		
		// If and else conditionals to check the year variable of the two.
		if (year == automobile.year)
		{
			// Do nothing.	
		}
		else
		{
			return (false);
		}
		
		// If and else conditionals to check the max_speed variable of the two.
		if (max_speed == automobile.max_speed)
		{
			// Do nothing.	
		}
		else
		{
			return (false);
		}
		
		// If and else conditionals to check the engine_size variable of the two.
		if (engine_size == automobile.engine_size)
		{
			// Do nothing.	
		}
		else
		{
			return (false);
		}
		
		return (true);
		
	}
	
	public Car makeClone()
	{
		Car the_clone = new Car();
		the_clone.manufacturer = manufacturer;		// Copy directly.
		the_clone.setModel(getModel());			//Copy using set and get methods.
		the_clone.year = year;
		the_clone.setMaxSpeed(getMaxSpeed());
		the_clone.engine_size = engine_size;
		return (the_clone);
	}
	
}

class Analysis
{
	private Car[] car_list;
	private int min_speed, max_speed, latest_model;
	private double avg_speed;
	
	public Analysis()
	{
	}
	
	public Analysis(Car[] list_of_cars)
	{
		for (int i = 0; i < list_of_cars.length; i++)
		{
			car_list = list_of_cars;	// If any changes occur in any of the lists, both car_list & list_of_cars changes accordingly
		}
		
		analyse();
	}
	
	public void setMinSpeed(int new_minimum)
	{
		min_speed = new_minimum;
	}
	
	public void setMaxSpeed(int new_maximum)
	{
		max_speed = new_maximum;
	}
	
	public void setAverageSpeed(int new_average)
	{
		avg_speed = new_average;
	}
	
	public void setLatestModel(int new_latest)
	{
		latest_model = new_latest;
	}
	
	public int getMinSpeed()
	{
		return (min_speed);
	}
	
	public int getMaxSpeed()
	{
		return (max_speed);
	}
	
	public double getAverageSpeed()
	{
		return (avg_speed);
	}
	
	public int getLatestModel()
	{
		return (latest_model);
	}
	
	public void analyse()
	{
		//int minimum = 0, maximum = 0, newest = 0;
		int total_speed = 0;
		
		for (int i = 0; i < car_list.length; i++)
		{
			// Makes variable 'minimum' equal to the first Car in the lists max speed. 
			if (i == 0)
				min_speed = car_list [i].getMaxSpeed();
			// For the rest of the Cars, checks if their max speed is less than 'minimum'. If yes, then makes minimum equal to that max speed.
			else
			{
				if (car_list [i].getMaxSpeed() < min_speed)
					min_speed = car_list [i].getMaxSpeed();
			}
			// Check if max speed of Car at position i is greater than 'maximum'. If yes, then makes maximum equal to that max speed.
			if (car_list [i].getMaxSpeed() > max_speed)
				max_speed = car_list [i].getMaxSpeed();
			
			// Check if year of Car at position i is greater than 'newest'. If yes, then makes newest equal to that Car's year.
			if (car_list [i].getYear() > latest_model)
				latest_model = car_list [i].getYear();	
				
			total_speed = total_speed +  car_list [i].getMaxSpeed();
		}
			
		avg_speed = (double) total_speed / (double) car_list.length;
		this.displayResults();
	}
	
	public void displayResults()
	{
		System.out.println("The average speed of all cars in this list is: " + avg_speed);
		
		System.out.println("The slowest cars in this list are: ");
		for (int i = 0; i < car_list.length; i++)
		{
			if (car_list [i].getMaxSpeed() == min_speed)
				car_list [i].writeOutput();
		}
		
		System.out.println("The fastest cars in this list are: ");
		for (int i = 0; i < car_list.length; i++)
		{
			if (car_list [i].getMaxSpeed() == max_speed)
				car_list [i].writeOutput();
		}
		
		System.out.println("The rest are: ");
		for (int i = 0; i < car_list.length; i++)
		{
			if (car_list [i].getMaxSpeed() != min_speed && car_list [i].getMaxSpeed() != max_speed)
				car_list [i].writeOutput();
		}
		
		System.out.println("The newest cars in this list are: ");
		for (int i = 0; i < car_list.length; i++)
		{
			if (car_list [i].getYear() == latest_model)
				car_list [i].writeOutput();
		}
	}
}
 
