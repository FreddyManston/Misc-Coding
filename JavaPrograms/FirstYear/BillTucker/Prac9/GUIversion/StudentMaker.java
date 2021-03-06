import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class StudentMaker
{
	static public int count = 0;
	static String initial_name;
	static int initial_number;
	static public Student [] student_list;
	static public JTextField names;
	static public JTextField numbers;
	
	public static void main (String[] args)
	{
		String str_num_students = JOptionPane.showInputDialog("How many students in the list?");
		int int_num_students = Integer.parseInt(str_num_students);
		student_list = new Student [int_num_students];
		
		StudentGui();
		
		System.out.print(student_list);
		
	}
	
	public static void StudentGui()
	{
		
		JFrame theFrame = new JFrame();
		Container framing = theFrame.getContentPane();
		
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setTitle("Student Maker");
		theFrame.setSize(600, 510);
		
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		theFrame.setLocationRelativeTo(null);
		
		//Creating labels:
		JLabel theNames = new JLabel("Name:");
		JLabel theNumbers = new JLabel("Numbers:");
		
		//Creating text fields and setting background colours:
		names = new JTextField("Type the student's name", 20);
		//names.setBackground(Color.white);
		numbers = new JTextField("Type the student's number", 20);
		//numbers.setBackground(Color.white);
		
		//Creating text areas and scroll panels:JTextField names = new JTextField("Type the student's name", 20);
		JTextArea textArea = new JTextArea(10, 40);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		
		
		//Creating buttons:
		JButton addStudent = new JButton("Add Student");
		addStudent.addActionListener(new ActionListener()
		{
			@Override
			
			public void actionPerformed(ActionEvent event)
			{
				initial_name = names.getText();
				initial_number = Integer.parseInt(numbers.getText());

				student_list [count] = new Student(initial_name, initial_number);
				count++;
				System.out.println(initial_name);
				names.setText("Type another student's name");
				numbers.setText("Type another student's number");
			}
		});

		JButton finishOff = new JButton("Done");
		
		//Creating the panels and setting their layouts:
		final JPanel namesPanel = new JPanel();
		//namesPanel.setLayout(new BorderLayout());
		final JPanel numbersPanel = new JPanel();
		//numbersPanel.setLayout(new BorderLayout());
		final JPanel buttonsPanel = new JPanel();
		//buttonsPanel.setLayout(new GridLayout(2,1));
		final JPanel textPanel = new JPanel();
		
		//Adding to the panels:
		namesPanel.add(theNames);
		namesPanel.add(names);
		numbersPanel.add(theNumbers);
		numbersPanel.add(numbers);
		buttonsPanel.add(addStudent);
		buttonsPanel.add(finishOff);
		
		textPanel.add(textArea);
		
		//Adding the panels to the frame:
		framing.add(namesPanel, BorderLayout.NORTH);
		framing.add(numbersPanel, BorderLayout.EAST);
		framing.add(buttonsPanel, BorderLayout.CENTER);
		framing.add(textPanel, BorderLayout.SOUTH);
		
		//Make the frame visible:
		theFrame.setVisible(true);
		
	}
}
