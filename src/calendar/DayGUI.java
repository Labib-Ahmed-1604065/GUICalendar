package calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;
import java.time.*;
import java.util.TreeMap;
import java.util.TreeSet;
/*
 * 1) add a scroll bar
 * 2) Days with events are aligned to the right 
 */

/**
 * A JPanel that represents the Calendar Day View in GUI. 
 *
 */
public class DayGUI extends JPanel implements ChangeListener{

	//instance variables
	private Scheduler scheduler;
	private DayField[] dayFields;
	private LocalDate selectedDate;

	/**
	 * Constructs a DayGUI object.
	 * @param s a reference to the Scheduler object (model)
	 */
	public DayGUI(Scheduler s)
	{
		scheduler = s;
		dayFields = new DayField[19];	//from 5 am to 23 pm
		selectedDate = s.getSelectedDate();
		//set layout manager of the dayGUI panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		int counter = 5;

		for (int i = 0; i < dayFields.length; i++)
		{
			dayFields[i] = new DayField();
			dayFields[i].setHour(counter);
			dayFields[i].setAreaText("");
			counter++;
			//dayFields[i].setHour(i);
		}
	}

	/**
	 * Draws the DayGUI object.
	 */
	public void drawView()
	{

		//get current day
		DayOfWeek weekDay = selectedDate.getDayOfWeek();
		//labelText is a String in the following format: weekDay month/monthDay
		//example: Monday 07/17
		String labelText = weekDay.toString() + " " + selectedDate.getMonthValue() + "/" + 
				selectedDate.getDayOfMonth();
		//create label with the current day
		JLabel dayLabel = new JLabel(labelText);
		dayLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		//dayLabel.setHorizontalAlignment(SwingConstants.WEST);
		//add label to panel
		this.add(dayLabel);
		//draw fields

		for (DayField field : dayFields)
		{
			//if there is no scheduled events on this day
			//clear the text areas and apply default layout settings
			if (!scheduler.hasEvents(selectedDate))
			{
				field.setAreaText("");
				field.getArea().setBackground(Color.WHITE);
				field.setFlag(false);
			}

			//remove any existing components in the field panel
			//so when the method drawField() is called, the 
			//new components won't overlap with pre-existing ones
			field.removeAll();
			field.updateUI();
			field.drawField();
			//field.getArea().setText("" + field.getHour());
			if (!field.isFlagged())
				this.add(new JSeparator(SwingConstants.HORIZONTAL));
			this.add(field);
			//this.add(area);
		}
		this.setPreferredSize(new Dimension(400,100));
	}

	/**
	 * Display events scheduled on the currently selected day in the day view.
	 * @param date the currently selected date 
	 */
	private void displayEvents(LocalDate date)
	{
		//get updated data
		TreeMap<LocalDate, TreeSet<CalendarEvent>> data = scheduler.getData();
		//get a set of events on this date
		TreeSet<CalendarEvent> events = data.get(date);

		//clear all dayFields from any pre-existing string
		for (DayField d : dayFields)
		{
			d.setAreaText("");
			d.getArea().setBackground(Color.white);
			d.setFlag(false);
		}

		//iterate over all events scheduled on that day
		for (CalendarEvent event : events)
		{
			//get the starting hour of the event 
			String startHr = event.getStartHrs();

			//JTextArea area = null;

			if (event.getIntegerEndTime() - event.getIntegerStartTime() > 100) 
				highlightLongEvent(event);

			else
			{
				//iterate over all dayFields
				for (DayField dayField : dayFields)
				{
					//if the dayField corresponds to the same hour as that of the event
					if (dayField.getStringHour().equals(startHr))
					{
						//append the event info to the text in the dayField 
						String text = dayField.getAreaText() + " " + event.printEvent() + "\n";
						//clear the dayField
						dayField.setAreaText("");
						//display the new updated dayField
						dayField.setAreaText(text);
						break;
					}	
				}
			}

		}
	}

	/**
	 * Changes the visual representation of events that are longer than 1 hour.
	 * @param event the event
	 */
	private void highlightLongEvent(CalendarEvent event)
	{
		String startHr = event.getStartHrs();
		String finishHr = event.getEndHrs();
		int intStartHr = Integer.parseInt(startHr);
		int intFinishHr = Integer.parseInt(finishHr);

		//iterate over dayFields till you find the corresponding starting hour
		for (DayField dayField : dayFields)
		{
			//if you've reached the corresponding starting hour
			if (dayField.getStringHour().equals(startHr))
			{
				//change the representation of the JTextArea of the field
				//change background color to gray and change the border
				dayField.getArea().setBackground(Color.LIGHT_GRAY);
				dayField.setBorder(new EmptyBorder(0,0,0,0));
				//print event info
				String text = dayField.getAreaText() + " " + event.printEvent() + "\n";
				dayField.setAreaText("");
				dayField.setAreaText(text);
				//flag the field
				dayField.setFlag(true);
			}

			//if you've reached the corresponding ending hour
			else if (dayField.getStringHour().equals(finishHr))
			{
				//change the representation of the JTextArea of the field
				dayField.getArea().setBackground(Color.LIGHT_GRAY);
				dayField.setBorder(new EmptyBorder(0,0,0,0));
				dayField.setFlag(true);
			}
			
			//if a dayField is between the starting and ending hours
			else if ((Integer.parseInt(dayField.getStringHour()) > intStartHr) && 
					(Integer.parseInt(dayField.getStringHour()) < intFinishHr))
			{
				//change the representation of the JTextArea of the field
				dayField.getArea().setBackground(Color.LIGHT_GRAY);
				dayField.setBorder(new EmptyBorder(0,0,0,0));
				dayField.setFlag(true);
			}
		}
	}

	@Override
	/**
	 * Updates the DayGUI view upon a change in the model. 
	 * @param e the event that notifies the view of the change
	 */
	public void stateChanged(ChangeEvent e) {

		//remove any existing components in the DayGUI panel
		this.removeAll();
		this.updateUI();
		//get updated information from the scheduler (the model)
		selectedDate = scheduler.getSelectedDate();
		//if the selected date has any scheduled events, display them
		if (scheduler.hasEvents(selectedDate))
			displayEvents(selectedDate);

		//draw the day view
		drawView();
	}

	/**
	 * Gets the array of dayFields in the dayGUI.
	 * @return the array of dayFields
	 */
	public DayField[] getDayFields()
	{
		return dayFields;
	}

//	public static void main(String[] args)
//	{
//		Scheduler s = new Scheduler();
//		//LocalDateTime date = LocalDateTime.now();
//		DayGUI dayView = new DayGUI(s);
//		s.attach(dayView);
//
//		CalendarEvent event = new CalendarEvent("Test", "07/24/2017", "14:00");
//		CalendarEvent event2 = new CalendarEvent("TTTEST", "07/24/2017", "14:30");
//		s.createEvent(event);
//		s.createEvent(event2);
//
//
//		dayView.drawView();
//
//		for (DayField dayField : dayView.getDayFields())
//		{
//			System.out.println(dayField.getArea().getText());
//		}
//
//		//s.setSelectedDate(LocalDate.of(1997, 1, 19));
//		//dayView.drawView();
//		JFrame frame = new JFrame();
//		frame.setSize(800, 300);
//		frame.setLayout(new BorderLayout());
//
//		frame.add(dayView, BorderLayout.EAST);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();	
//	}

}
