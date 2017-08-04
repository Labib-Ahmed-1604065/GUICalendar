package calendar;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.time.*;

/**
 * A DayField is a representation of each hour and the events scheduled at that hour. This class is 
 * used by the DayGUI class which has an array of DayFields. 
 *
 */
public class DayField extends JPanel{

	//instance variables
	private int hour; 
	private JTextArea area;
	boolean flag;
	
	/**
	 * Constructs a DayField object.
	 */
	public DayField()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		area = new JTextArea();
		area.setEditable(false);
		hour = 0;
		flag = false;
	}
	
	/**
	 * Draws the DayField object. 
	 */
	public void drawField()
	{
		String s = "";
		if (hour < 10)
			s += "0" + hour + ":00";
		else
			s += hour + ":00";

		JLabel label = new JLabel(s );
		label.setPreferredSize(new Dimension(40, 200));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		this.add(label);
		this.add(area);
	}
	
	/**
	 * Gets the hour assigned to the DayField object.
	 * @return the hour
	 */
	public int getHour()
	{
		return hour;
	}
	
	/**
	 * Gets a String representation of the hour assigned to the DayField object in the following format: 00.
	 * @return the hour
	 */
	public String getStringHour()
	{
		String s = "";
		if (hour < 10)
			return s+= "0" + hour;
		return s + hour;
	}
	

	/**
	 * Gets the DayField's JTextArea .
	 * @return JTextArea
	 */
	public JTextArea getArea()
	{
		return area;
	}
	
	/**
	 * Changes the hour assigned to the DayField object.
	 * @param h the new hour
	 */
	public void setHour(int h)
	{
		hour = h;
	}
	
	/**
	 * Changes the string of the DayField's JTextArea.
	 * @param s the new String
	 */
	public void setAreaText(String s)
	{
		area.setText(s);
	}
	
	/**
	 * Changes the value of the DayField's flag.
	 * @param value the new value
	 */
	public void setFlag(boolean value)
	{
		flag = value;
	}
	
	/**
	 * Gets the String shown in the DayField's JTextArea.
	 * @return the JTextArea's string
	 */
	public String getAreaText()
	{
		return area.getText();
	}
	
	/**
	 * Checks the value of the DayField's flag.
	 * @return the flag's value
	 */
	public boolean isFlagged()
	{
		return flag;
	}
	
}
