package calendar;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * A JPanel that represents the Navigation buttons in GUI. 
 *
 */
public class NavigateGUI extends JPanel {

	//instance variables
	private JButton create;
	private JButton next;
	private JButton prev;
	private JButton quit;
	private Scheduler scheduler;
	private boolean quitIsClicked;

	/**
	 * Constructs a NavigateGUI object
	 */
	public NavigateGUI(Scheduler s)
	{
		//initialize instance variables
		create = new JButton("Create");
		quit = new JButton("Quit");
		next = new JButton(">");
		prev = new JButton("<");
		scheduler = s;
		quitIsClicked = false;

		//set the layout manager of this panel
		this.setLayout(new BorderLayout());

		//add action listeners
		next.addActionListener(getListener(next));
		prev.addActionListener(getListener(prev));
		create.addActionListener(getListener(create));
		quit.addActionListener(getListener(quit));

		//add mouse listeners
		create.addMouseListener(hoverEffect(create));
		next.addMouseListener(hoverEffect(next));
		prev.addMouseListener(hoverEffect(prev));
		quit.addMouseListener(hoverEffect(quit));
	}

	/**
	 * Draws the NavigateGUI object.
	 */
	public void drawView()
	{
		//create new local panel that contains the prev and next buttons
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(prev);
		panel.add(next);

		//add all elements to this panel
		this.add(create, BorderLayout.WEST);
		this.add(quit, BorderLayout.EAST);
		this.add(panel, BorderLayout.CENTER);
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
				//change the mouse cursor into the hand cursor
				b.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

		};

	}

	/**
	 * Creates and returns a new ActionListener instance that behaves differently depending on the
	 * button that was clicked.
	 * @param btn the clicked button
	 * @return the ActionListener instance
	 */
	private ActionListener getListener(JButton btn)
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//if the "Create" button was clicked
				if (btn.equals(create))
				{
					//prompt "create new event" dialogue
					promptInputBox();
				}
				//if the "Quit" button was clicked
				else if (btn.equals(quit))
				{
					//terminate program and save the events in "events.txt"
					try {
						scheduler.save();
						quitIsClicked = true;
					} catch (IOException m) {
						System.out.println(m.getMessage());
					}
				}
				//if the ">" button was clicked
				else if (btn == next)
				{
					//navigate to the next day
					navigateToNext();
				}
				//if the "<" button was clicked
				else if (btn == prev)
				{
					//navigate to the previous day
					navigateToPrev();
				}
			}
		};
	}

	/**
	 * Creates a group of panels that get the new event information from the user and calls the method
	 * that prompts the dialogue box containing those panels.
	 */
	private void promptInputBox()
	{
		//create all input text fields
		JTextField title = new JTextField(31);
		JTextField date = new JTextField(8);
		JTextField start = new JTextField(8);
		JTextField finish = new JTextField(8);

		//create 3 panels 
		JPanel inputPanel = new JPanel();
		JPanel titlePanel = new JPanel();
		JPanel detailsPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

		//add title-related components
		titlePanel.add(new JLabel("Title"));
		titlePanel.add(title);

		//create a new formatter of pattern MM/DD/YYYY
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		//get the user-selected date and apply the pattern to it
		//then, set the text of the field to the formatted date
		date.setText(scheduler.getSelectedDate().format(formatter));

		//add date label and text field
		detailsPanel.add(new JLabel("Date"));
		detailsPanel.add(date);
		//add start hour label and text field
		detailsPanel.add(new JLabel("from"));
		detailsPanel.add(start);
		//add finish hour label and text field
		detailsPanel.add(new JLabel("to"));
		detailsPanel.add(finish);

		//add the two subpanels to the main panel
		inputPanel.add(titlePanel);
		inputPanel.add(detailsPanel);
		inputPanel.add(new JLabel("MM/DD/YYYY                 00:00                    24:00"));

		//show dialogue 
		showDialogue(inputPanel, title, date, start, finish);

	}

	/**
	 * Prompts a dialogue box that asks the user for the information of the event to be created.
	 * @param inputPanel the panel containing all JComponents 
	 * @param title the textField in which the user enters the event's title
	 * @param date the textField in which the user enters the event's date
	 * @param start the textField in which the user enters the event's starting time
	 * @param finish the textField in which the user enters the event's ending time
	 */
	private void showDialogue(JPanel inputPanel, JTextField title, JTextField date, JTextField start,
			JTextField finish)
	{
		int result = JOptionPane.showConfirmDialog(null, inputPanel,
				"Event Information", JOptionPane.DEFAULT_OPTION);
		//when the "Ok" button is clicked
		if (result == JOptionPane.OK_OPTION) 
		{
			//create new event object with the user-entered information
			CalendarEvent event = new CalendarEvent(title.getText(), date.getText(), 
					start.getText(), finish.getText());
			//check if there's a time conflict 
			boolean isConflicting = scheduler.checkForTimeConflict(event);

			//if there is indeed a time conflict
			if (isConflicting)
			{
				//the calendar generates an error message, asking the user
				//to enter an event without any time conflict. 
				JOptionPane.showMessageDialog(null, 
						"This event conflicts with an existing event! \n"
								+ "Please enter the event again with a correct time.",
								"Time Conflict", JOptionPane.ERROR_MESSAGE);
				//recursive call to show the event details dialogue
				showDialogue(inputPanel, title, date, start, finish);
			}
			else
				//schedule event 
				scheduler.createEvent(event);
		}
		//if the close ("x") button is clicked, do nothing
		else if (result == JOptionPane.CLOSED_OPTION)
		{}
	}

	/**
	 * Updates the model's selected date to the next day in the calendar.
	 */
	private void navigateToNext()
	{
		//get current selected date in the model
		LocalDate currentDate = scheduler.getSelectedDate();
		//create new dummy LocalDate variable with the next day in the month
		LocalDate nextDate;
		//if the maximum days in the month has not been reached
		if (currentDate.getDayOfMonth() < currentDate.lengthOfMonth())
			//increment month day
			nextDate = currentDate.withDayOfMonth(currentDate.getDayOfMonth() + 1);
		//else, if the month is not December
		else if (currentDate.getMonthValue() < 12)
			//increment month 
			nextDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue() + 1, 1);
		//else if December has been reached
		else 
			//increment year
			nextDate = LocalDate.of(currentDate.getYear() + 1, 1, 1);

		//update the model's selected date to the dummy variable
		scheduler.setSelectedDate(nextDate);
	}

	/**
	 * Updates the model's selected date to the previous day in the calendar.
	 */
	private void navigateToPrev()
	{

		//get current selected date in the model
		LocalDate currentDate = scheduler.getSelectedDate();
		//create new dummy LocalDate variable with the next day in the month
		LocalDate prevDate;
		//if the first day in the month has not been reached
		if (currentDate.getDayOfMonth() > 1)
			//decrement month day
			prevDate = currentDate.withDayOfMonth(currentDate.getDayOfMonth() - 1);
		//else, if the month is not January
		else if (currentDate.getMonthValue() > 1)
			//decrement month 
		{
			LocalDate dummyDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue() - 1,
					currentDate.getDayOfMonth());
			prevDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue() - 1,
					dummyDate.lengthOfMonth());

		}

		//else if January has been reached
		else 
			//decrement year
			prevDate = LocalDate.of(currentDate.getYear() - 1, 12, 31);

		//update the model's selected date to the dummy variable
		scheduler.setSelectedDate(prevDate);
	}

	/**
	 * Checks if the quit button is clicked.
	 * @return true if the quit button is clicked
	 */
	public boolean isQuitClicked()
	{
		return quitIsClicked;
	}

//	public static void main(String[] args)
//	{
//		Scheduler s = new Scheduler();
//		NavigateGUI navigateView = new NavigateGUI(s);
//		navigateView.drawView();
//
//		JFrame frame = new JFrame();
//		frame.setSize(800, 300);
//		frame.setLayout(new BorderLayout());
//
//		frame.add(navigateView, BorderLayout.NORTH);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		//frame.pack();	
//	}
}
