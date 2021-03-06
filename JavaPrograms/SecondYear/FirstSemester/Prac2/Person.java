public class Person
{
	private String name, surname;
	private int age;
	
	public Person()
	{
		name = null;
		surname = null;
		age = 0;
	}
	
	public Person(String initialName, String initialSurname, int initialAge)
	{
		name = initialName;
		surname = initialSurname;
		age = initialAge;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public void setSurname(String newSurname)
	{
		surname = newSurname;
	}
	
	public void setAge(int newAge)
	{
		age = newAge;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSurname()
	{
		return surname;
	}
	
	public int getAge()
	{
		return age;
	}

	public void writeOutput()
	{
		System.out.println("Name: " + name + "\nSurname: " + surname + "\nAge: " + age + "\n\n");
	}
}
