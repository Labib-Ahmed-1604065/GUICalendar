package calendar;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A JPanel that represents the Calendar Months in GUI. 
 *
 */
public class MonthGUI extends JPanel implements ChangeListener{

	//instance variables
	private JPanel labelPanel;				//contains the current month name label
	private static JPanel btnPanel;			//btnPanel contains the clickable days
	private Scheduler scheduler;
	private LocalDate selectedDate;

	/**
	 * Constructs a MonthGui object.
	 * @param s a reference to the Scheduler object (model)
	 */
	public MonthGUI(Scheduler s)
	{
		//initialize panels 
		labelPanel = new JPanel();
		btnPanel = new JPanel();
		scheduler = s;
		selectedDate = scheduler.getSelectedDate();
	}

	/**
	 * Draws the MonthGUI object.
	 */
	public void drawView()
	{
		//set layout manager of label panel 
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

		//String month =  " " + MONTH.values()[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
		//get current month
		Month month = selectedDate.getMonth(); 
		//create label with the current month
		JLabel monthLabel = new JLabel(month.toString() + " " + selectedDate.getYear()); 
		//add labels to the label panel
		labelPanel.add(monthLabel);

		//set layout manager of the month panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(labelPanel);
		//create clickable calendar days 
		createButtonPanel(selectedDate);
		//btnPanel.setPreferredSize(new Dimension(320, 100));
		this.add(btnPanel);
		this.setPreferredSize(new Dimension(250,100));
	}

	/**
	 * Creates and returns a new MouseAdapter instance that changes the cursor's shape
	 * into the hand cursor when the user hovers on a button. 
	 * @param b the button on which the user hovered
	 * @return the MouseAdapter instance
	 */
	private MouseAdapter hoverEffect(JButton b)
	{
		return new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				//changes the cursor shape into the hand cursor
				b.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		};
	}

	/**
	 * Creates the clickable month days.
	 * @param c the currently selected localDate
	 */
	private void createButtonPanel(LocalDate date)
	{
		//create new grid with 7 columns and 0 gaps both horizontally and vertically
		btnPanel.setLayout(new GridLayout(0, 7, 0, 0));

		//create temporary calendar with DAY_OF_MONTH = 1 : the first day of the month
		//GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		LocalDate temp = LocalDate.of(date.getYear(), date.getMonthValue(), 1);

		List<String> weekDays = Arrays.asList("Su", "Mo", "Tu",
				"We", "Th", "Fr", "Sa");

		//create buttons with weekdays 
		for (String s : weekDays)
		{
			JButton button = new JButton(s);
			//remove visible border
			button.setBorder(new EmptyBorder(0, 0, 0, 0));
			//add to the panel
			btnPanel.add(button);
		}

		//determine the name of the first day of month. 
		//getValue returns the int value of the enum
		//int  firstDay = temp.get(Calendar.DAY_OF_WEEK);		
		int firstDay = temp.getDayOfWeek().getValue();

		//add blank buttons till you reach the first day of the month
		for (int j = 1; j < firstDay; j++)
		{
			JButton button = new JButton();
			button.setBorder(new EmptyBorder(0, 0, 0, 0));
			btnPanel.add(button);
		}

		//LocalDate d = date.toLocalDate();
		//loop over the number of days in the month, add a button in each iteration
		for (int i = 0; i < date.lengthOfMonth(); i++) 
		{
			JButton button;

			button = new JButton(Integer.toString(i + 1));
			button.setBorder(new EmptyBorder(0, 0, 0, 0));
			button.addMouseListener(hoverEffect(button));
			button.addActionListener(getListener(i + 1));
			//if it matches today's date, change the font color to red
			if (i + 1 == date.getDayOfMonth())
				{
					//button.setForeground(Color.RED);
					button.setOpaque(true);
					button.setBackground(Color.GRAY);
				}

			//add button to the panel
			btnPanel.add(button);
		}
	}

	/**
	 * Returns a new ActionListener instance that changes the currentDay and
	 * notifies the model.  
	 * @param dayValue new Day Value 
	 * @return ActionListener object
	 */
	private ActionListener getListener(int dayValue)
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				//create a new dummy localDate with the updated dayValue
				LocalDate dummy = scheduler.getSelectedDate().withDayOfMonth(dayValue);
				//update the model
				scheduler.setSelectedDate(dummy);
			}
		};
	}
	
	@Override
	/**
	 * Updates the MonthGUI view upon a change in the model. 
	 * @param e the event that notifies the view of the change
	 */
	public void stateChanged(ChangeEvent e) 
	{
		//get updated data
		selectedDate = scheduler.getSelectedDate();
		//remove any existing components in the MonthGUI panel
		this.removeAll();
		this.updateUI();
		//remove any existing components in the BtnPanel and the labelPanel
		btnPanel.removeAll();
		labelPanel.removeAll();
		//draw the monthGUI view again
		this.drawView();
	}

//	public static void main(String[] args)
//	{
//		//MyCalendar c = new MyCalendar();
//		Scheduler s = new Scheduler();
//		LocalDateTime date = LocalDateTime.now();
//		MonthGUI monthView = new MonthGUI(s);
//		monthView.drawView();
//
//		//		s.setSelectedDate(LocalDate.of(2018, 12, 23));
//		//		monthView.drawView();
//
//		JFrame frame = new JFrame();
//		frame.setSize(800, 300);
//		frame.setLayout(new BorderLayout());
//
//		frame.add(monthView, BorderLayout.WEST);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		//frame.pack();
//	}
}
