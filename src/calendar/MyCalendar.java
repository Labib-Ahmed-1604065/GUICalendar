package calendar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
enum MONTHS
{
	January, February, March, April, May, June, July, August, 
	September, October, November, December;
}
enum DAYS
{
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday ;
}

/**
 * 
 * A calendar that allows the user to schedule, load, delete, and save events as well as 
 * to switch between day and month view. It can show requested dates. 
 * 
 */
public class MyCalendar extends GregorianCalendar {

	//instance variables
	private Scheduler scheduler;
	private MONTHS[] arrayOfMonths;
	private DAYS[] arrayOfDays;

	/**
	 * Construct a MyCalendar object.
	 */
	public MyCalendar()
	{
		scheduler = new Scheduler();
		arrayOfMonths = MONTHS.values();
		arrayOfDays = DAYS.values();
	}

	/**
	 * Schedule a new event.
	 * @param title the event title
	 * @param date the event date in string representation
	 * @param startingTime the event starting time
	 * @param endingTime the event ending time
	 */
	//schedule new event with event details as parameters
	public void createEvent(String title, String date, String startingTime, String endingTime)
	{
		scheduler.createEvent(title, date, startingTime, endingTime);
	}
	
	/**
	 * Quit the program and save events into events.txt.
	 * @throws IOException
	 */
	//quit and save
	public void quit() throws IOException
	{
		scheduler.save();
	}

	/**
	 * Populate the calendar with existing events from events.txt.
	 */
	//load calendar events
	public void load() 
	{
		try {
			scheduler.load();
		} catch (IOException e) {
			System.out.println("Cannot find file events.txt because this is the first run of the program!");
		}
	}

	/**
	 * Get all scheduled events.
	 * @return a string of scheduled events
	 */
	//browse event list
	public String browseEventList()
	{
		return scheduler.browseEvents();
	}

	/**
	 * Get the previous day.
	 * @param c Calendar instance 
	 * @return a call to dayView(c) which generates a string of the date and any events on it
	 * @throws ParseException
	 */
	public String preDay(Calendar c) throws ParseException
	{
		c.add(Calendar.DAY_OF_WEEK, -1);
		return dayView(c);
	}

	/**
	 * Get the following day.
	 * @param c Calendar instance
	 * @return a call to dayView(c) which generates a string of the date and any events on it
	 * @throws ParseException
	 */
	//change to next day
	public String nextDay(Calendar c) throws ParseException
	{
		c.add(Calendar.DAY_OF_WEEK, 1);
		return dayView(c);
	}

	/**
	 * Get the day view of the given date.
	 * @param c Calendar instance
	 * @return a string with the given day and any events on it
	 * @throws ParseException
	 */
	public String dayView(Calendar c) throws ParseException
	{
		Date date = c.getTime();								
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("MM/dd/yyyy");
		String stringDate = dateFormat.format(date);
		date = dateFormat.parse(stringDate);
		c.setTime(date);
		return dayViewString(c);
	}

	/**
	 * Get the day view of today's date.
	 * @return a string with today's date and any events on it
	 * @throws ParseException
	 */
	//return a string with the day view of today's date
	public String getToday() throws ParseException
	{
		Calendar c = getInstance();
		return dayView(c);
	}
	
	/**
	 * Generate day view of date. 
	 * @param c Calendar instance
	 * @return a string with the requested day and any events on it
	 */
	private String dayViewString(Calendar c)
	{
		String s = "";
		s += arrayOfDays[c.get(Calendar.DAY_OF_WEEK) - 1];	//Monday, Tuesday, ...
		s += ", ";
		s += arrayOfMonths[c.get(Calendar.MONTH)];			//June, July, ... 
		s += " ";
		s += c.get(Calendar.DAY_OF_MONTH);					//23, 24, ...
		s += ", ";
		s += c.get(Calendar.YEAR);							//2017, 2018, ...
		s += "\n";
		if (scheduler.hasEvents(c.getTime()))
			s += scheduler.getEventsOnThisDay(c.getTime());
		return s += "\n";
	}
	
	/**
	 * Get the current month.
	 * @return a string with the current month.
	 */
	//return a string with the current month
	public String getMonth()
	{
		Calendar c = getInstance();
		return monthView(c);
	}
	
	/**
	 * Go to specified date
	 * @param stringDate string representation of the date
	 * @return a string with the day view of the specified day
	 * @throws ParseException
	 */
	//go to given date
	public String goToDate(String stringDate) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		GregorianCalendar c = new GregorianCalendar();
		Date date = dateFormat.parse(stringDate);
		c.setTime(date);
		return dayViewString(c);
	}

	/**
	 * Get the previous month
	 * @param c Calendar instance
	 * @return a call to monthView(c) which generates a string of the month
	 */
	//change to previous month
	public String preMonth(Calendar c)
	{
		c.add(Calendar.MONTH, -1);
		return monthView(c);
	}

	/**
	 * Get the following month.
	 * @param c Calendar instance
	 * @return a call to monthView(c) which generates a string of the month
	 */
	//change to next month
	public String nextMonth(Calendar c)
	{
		c.add(Calendar.MONTH, 1);
		return monthView(c);
	}

	/**
	 * Generate month view.
	 * @param c Calendar instance
	 * @return a string with the month and the scheduled events surrounded with square brackets
	 */
	public String monthView(Calendar c)
	{
		String s = "";
		//print header
		s += arrayOfMonths[c.get(Calendar.MONTH)]+ " " + c.get(Calendar.YEAR) + "\n";

		//print week names sun ---> sat
		s += "Su   Mo   Tu   We   Th   Fr   Sa\n";

		//create temporary calendar with DAY_OF_MONTH = 1 : the first day of the month
		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);

		//determine the name of the first day of month
		int  firstDay = temp.get(Calendar.DAY_OF_WEEK);				//number of spaces needed before displaying the days of the month

		//determine how many days are in this month, 30, 31, or 28
		int maxDaysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		//print spaces in first week of month
		for (int i = 1; i < firstDay; i++)
		{
			s += "     ";
		}

		int count = firstDay;					//used to end line at the end each week
		String spaces = "    ";					//spaces between days

		//loop over the number of days in this month
		for (int i = 1; i <= maxDaysOfMonth; i++)
		{
			//change the date of the temp calendar to the i-th day
			temp.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), i);

			if (i == 10)						//if day is 10th
				spaces = "   ";					//decrease the spacing so it will accommodate 2-digit numbers with the same layout

			//check for events on this day
			if (scheduler.hasEvents(temp.getTime()))
			{
				if (i >= 10)					//if day is a 2-digit number
					s += "[" + i + "]" + " ";	//surround with brackets and modify spacing
				else
					s += "[" + i + "]" + "  ";	//surround with brackets
			}

			//if there are no events
			else
				s += i + spaces;				//print the day followed by the spaces

			if (i == maxDaysOfMonth)			//if maximum days is reached, end line
				s += "\n";
			if (count == 7)						//if max number of days in week is reached,
			{									//end line and and re-initialize count to 1
				s += "\n";					
				count = 1;					
			}
			else
				count ++;					//if max number of days in week is not reached, increment count
		}

		return s += "\n";
	}

	/**
	 * Delete scheduled events on selected date. 
	 * @param stringDate a string representation of the date
	 */
	//delete event
	public void deleteEvent(String stringDate)
	{
		scheduler.deleteEvent(stringDate);
	}

	/**
	 * Delete all scheduled events in calendar.
	 */
	//delete all events
	public void deleteAllEvents()
	{
		scheduler.deleteAllEvents();
	}

	/**
	 * Get the calendar's scheduler. 
	 * @return the calendar scheduler
	 */
	
	//return scheduler 
	public Scheduler getSchdeule()
	{
		return scheduler;
	}

}
