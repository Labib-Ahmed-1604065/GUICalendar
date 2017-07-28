package calendar;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
 * Problems to tackle:
 * 1) events can have no ending time  					TACKLED
 * 2) ending time MUST be > starting time
 * 3) time should be entered in this format HH:MM
 * 4) compareTo should compare time then date			TACKLED		
 */

/**
 * 
 * An event created by the user.
 *
 */

public class CalendarEvent implements Comparable<CalendarEvent>{

	//instance variables
	private String title;				//event title
	private String stringDate;			//date represented as String object
	private String startHour;			//starting hour represented as String object
	private String startMinute;			//starting minute represented as String object
	private String endHour;				//ending hour represented as String object
	private String endMinute;			//ending minute represented as String object

	private LocalDate date;				//date with starting hour and minute

	/**
	 * Construct an Event object that has an ending time.
	 * @param title the event title
	 * @param date the event date
	 * @param start	the event starting time
	 * @param end	the event ending time
	 */
	public CalendarEvent (String title, String date, String start, String end)
	{
		this.title = title;
		//initialize the details of starting time and ending time
		getTimeDetails(start, end);
		this.stringDate = date;

		initializeDate(stringDate);

	}

	/**
	 * Construct an Event object with no ending time.
	 * @param title	the event title
	 * @param date the event date
	 * @param start the event starting time
	 */
	public CalendarEvent (String title, String date, String start)
	{
		this.title = title;
		getTimeDetails(start);
		this.stringDate = date;

		initializeDate(stringDate);
	}

	/**
	 * Initializes the event's date variable.
	 * @param date string representation of date 
	 */
	private void initializeDate(String date)
	{
		//convert the stringDate into a LocalDate object
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		this.date = LocalDate.parse(date, formatter);
	}

	/**
	 * Change event's title.
	 * @param title the new title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Change event's string date.
	 * @param date the new date 
	 */
	public void setDate(String date)
	{
		this.stringDate = date;
		initializeDate(stringDate);
	}

	/**
	 * Change event's starting time.
	 * @param start the new starting time
	 */
	public void setStart(String start)
	{
		String[] startDetails = start.split(":");		//split the starting hours and minutes
		startHour = startDetails[0];
		startMinute = startDetails[1];					//update the ending time
		//set the start hour and minute to the new starting time
		getTimeDetails(start);
	}

	/**
	 * Change event's ending time.
	 * @param end the new ending time
	 */
	public void setEnd(String end)
	{
		//set the end hour and minute to the new ending time
		String[] endDetails = end.split(":");			//split the starting hours and minutes
		endHour = endDetails[0];
		endMinute = endDetails[1];						//update the ending time

	}

	//getters
	/**
	 * Get the event's title.
	 * @return the event title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Get the event's string date.
	 * @return the event string date
	 */
	public String getStringDate()
	{
		return stringDate;
	}

	/**
	 * Get the event's date with the starting time.
	 * @return the event date
	 */
	public LocalDate getDate()
	{
		return date;
	}


	/**
	 * Get the event's full starting time in the string format.
	 * @return the event starting time
	 */
	public String getStartHrsMins()
	{
		String s = "";
		//concatenate starting hour and minute
		s += startHour + ":" + startMinute;
		return s;
	}

	/**
	 * Get the event's full ending time in the string format.
	 * @return the event ending time
	 */
	public String getEndHrsMins()
	{
		String s = "";
		//concatenate ending hour and minute
		s += endHour + ":" + endMinute;
		return s;
	}

	/**
	 * Get the event's starting hour in the string format.
	 * @return the event starting hour
	 */
	public String getStartHrs()
	{
		String s = "";
		//append starting hour
		s += startHour;
		return s;
	}

	public String getStartMins()
	{
		String s = "";
		//append starting minute
		s += startMinute;
		return s;
	}

	public String getEndHrs()
	{
		String s = "";
		//append starting hour
		s += endHour;
		return s;
	}

	public String getEndMins()
	{
		String s = "";
		//append starting minute
		s += endMinute;
		return s;
	}

	public int getIntegerStartTime()
	{
		String s = "";
		s += getStartHrs() + getStartMins();
		return Integer.parseInt(s);
	}

	public int getIntegerEndTime()
	{
		String s = "";
		s += getEndHrs() + getEndMins();
		return Integer.parseInt(s);
	}

	/**
	 * Convert event to the following format: DayName Month MonthDay StartingTime - EndingTime Title.
	 * @return string of event details
	 */
	public String toString()
	{
		String s = "";
		//if there is NO ending time in the event
		if (getEndHrsMins() == null | getEndHrsMins() == "")
			s += date.getDayOfWeek() + " " + date.getMonth().toString() + 
			" " + date.getDayOfMonth() + " " + date.getYear() + " " 
			+ getStartHrsMins() + " " + title;
		else
			s += date.getDayOfWeek() + " " + date.getMonth().toString() + " " + 
					date.getDayOfMonth() + " "+  date.getYear() + " " + 
					getStartHrsMins() + " - " + getEndHrsMins() 
					+ " " + title;
		return s;
	}

	public String printEvent()
	{
		String s = "";

		if (getEndHrsMins() == null | getEndHrsMins() == "")
			s += title + " " + getStartHrsMins();
		else
			s += title + " " + getStartHrsMins() + " - " + getEndHrsMins();
		return s;
	}

	/**
	 * Removes the ':' in the given String time and stores
	 * it into the hour and minute instance variables. 
	 * Example: 14:30 -> 1430  startHour = 14, startMinute = 30
	 * @param start string representation of the starting time 
	 * in 24-hr clock format
	 * @param end string representation of the ending time 
	 * in 24-hr clock format
	 */
	private void getTimeDetails(String start, String end)
	{
		//14:00 -> 1400, 14:30 -> 1430, 24:00 -> 2400
		String[] startDetails = start.split(":");		//split the starting hours and minutes
		String[] endDetails = end.split(":");			//split the ending hours and minutes

		startHour = startDetails[0];
		startMinute = startDetails[1];
		endHour = endDetails[0];
		endMinute = endDetails[1];

	}

	/**
	 * Removes the ':' in the given String time and stores
	 * it into the hour and minute instance variables. 
	 * Example: 14:30 -> 1430  startHour = 14, startMinute = 30
	 * @param start string representation of the starting time 
	 * in 24-hr clock format
	 */
	private void getTimeDetails(String start)
	{
		String[] startDetails = start.split(":");		//split the starting hours and minutes
		startHour = startDetails[0];
		startMinute = startDetails[1];
	}


	@Override
	//Override compareTo method to specify TreeSet order
	/**
	 * Override compareTo method to specify TreeSet order by starting time then by date.
	 * @param that event to be compared to
	 * @return integer 0 (equals), 1 (more than), or -1 (less than)
	 */
	public int compareTo(CalendarEvent that) {
		//compare by starting time then by date
		int thisStartTime = Integer.parseInt(this.startHour + this.startMinute); 		//get starting time of this instance
		int thatStartTime = Integer.parseInt(that.startHour + that.startMinute);		//get starting time of passed parameter

		//compare by starting time
		if (thisStartTime != thatStartTime)
			return thisStartTime - thatStartTime; 

		//compare by date
		return this.getDate().compareTo(that.getDate());
	}

	@Override
	/**
	 * Override equals to be compatible with compareTo method
	 * @param x object to test for equality
	 * @return integer value returned by compareTo method
	 */
	public boolean equals(Object x)
	{
		CalendarEvent that = (CalendarEvent) x;
		return this.compareTo(that) == 0;
	}

	public static void main(String[] args)
	{
		CalendarEvent event = new CalendarEvent("Test Event", "12/10/1997", "12:30", "2:00");
		System.out.println(event.toString());
		event.setDate("01/10/2017");
		System.out.println(event.toString());
		event.setEnd("6:30");
		System.out.println(event.toString());
		event.setStart("4:00");
		System.out.println(event.toString());
	}
}
