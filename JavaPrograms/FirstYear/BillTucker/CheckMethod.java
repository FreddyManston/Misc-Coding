import javax.swing.*;

public class CheckMethod
{

	public static void main (String[] args)	
	{

		JOptionPane.showMessageDialog(null, "- - - - - - - Shop 1 - - - - - - - -");		// Input for shop 1.
		String str_amount1 = JOptionPane.showInputDialog("What is the value of the credit amount, \nwhich you have to pay off?").replace("R","");
		check_valid(str_amount1);
		double num_amount1 = Double.parseDouble(str_amount1);
		check_positive(num_amount1);

		String str_installment1 = JOptionPane.showInputDialog("What is your bank's daily compound rate? \nPlease input your answer as a percentage").replace("%","");
		check_valid(str_installment1);
		double num_installment1 = Double.parseDouble(str_installment1);
		check_positive(num_installment1);

		String str_interest1 = JOptionPane.showInputDialog("What are your monthly installments?").replace("R","");
		check_valid(str_interest1);
		double num_interest_rate1 = Double.parseDouble(str_interest1);
		check_positive(num_interest_rate1);

		String str_years_to_pay1 = JOptionPane.showInputDialog("How many years will it take you to pay off the amount? \nPlease input your answer as a whole number or a as decimal.").replace(",",".");
		check_valid(str_years_to_pay1);
		double num_years_to_pay1 = Double.parseDouble(str_years_to_pay1);
		check_positive(num_years_to_pay1);
		double num_months_to_pay1 = num_years_to_pay1 * 12;	// Change years to months.


		JOptionPane.showMessageDialog(null, "- - - - - - - Shop 2 - - - - - - - -");			// Input for shop 2.
		String str_amount2 = JOptionPane.showInputDialog("What is the value of the credit amount, \nwhich you have to pay off?").replace("R","");
		check_valid(str_amount2);
		double num_amount2 = Double.parseDouble(str_amount2);
		check_positive(num_amount2);

		String str_installment2 = JOptionPane.showInputDialog("What are your monthly installments?").replace("R","");
		check_valid(str_installment2);
		double num_installment2 = Double.parseDouble(str_installment2);
		check_positive(num_installment2);

		String str_interest2 = JOptionPane.showInputDialog("What is your bank's daily compound rate? \nPlease input your answer as a percentage").replace("%","");
		check_valid(str_interest2);
		double num_interest_rate2 = Double.parseDouble(str_interest2);
		check_positive(num_interest_rate2);

		String str_years_to_pay2 = JOptionPane.showInputDialog("How many years will it take you to pay off the amount? \nPlease input your answer as a whole number or a as decimal.").replace(",",".");
		check_valid(str_years_to_pay2);
		double num_years_to_pay2 = Double.parseDouble(str_years_to_pay2);
		check_positive(num_years_to_pay2);
		double num_months_to_pay2 = num_years_to_pay2 * 12;	// Change years to months.
		
		// Invoking the 'the_calculation' method to compute the balances for each shop.
		double balance1 = the_calculation(num_amount1, num_installment1, num_interest_rate1, num_months_to_pay1);
		double balance2 = the_calculation(num_amount2, num_installment2, num_interest_rate2, num_months_to_pay2);

		//System.out.println(balance1);
		//System.out.println(balance2);

		// If statements to checking which shop is cheapest.
		if (balance1 == balance2)
			System.out.println("Same");
		else if (balance1 > balance2)
			System.out.println("Store 1 is cheaper");
		else if (balance1 < balance2)
			System.out.println("Store 2 is cheaper");

		//System.out.println("Balance 1 = R"+balance1);
		//System.out.println("Balance 2 = R"+balance2);
		
	}

	public static void check_valid (String check)
	{

		String check_list = "0123456789.";

		for (int i = 0; i < check.length(); i++)
		{
			if (check_list.contains(String.valueOf(check.charAt(i))))
			{
				// Do nothing.
			}
			else
			{
				System.out.println("Invalid input");
				System.exit(0);
			}
		
		}
		
	}
	
	public static void check_positive (double number)
	{

		if (number >= 0)
		{
			// Do nothing.
		}	
		else
		{
			JOptionPane.showMessageDialog("Invalid input");
			System.exit(0);
		}

	}

	public static double the_calculation (double amount, double installment, double interest_rate, double period)
	{

		double interest = 0;
		double balance = amount;
	
		int track = 1; //Running variable

		while (track <= period)
		{   
			interest = (balance*((interest_rate/100)/365)*31);
			balance = (balance+interest-installment);
			track ++;
		}

		balance = Math.round(balance*100)/100.0d;
		return (balance);	

	}
}



