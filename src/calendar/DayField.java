package calendar;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.time.*;

public class DayField extends JPanel{

	private int hour; 
	private JTextArea area;
	boolean flag;
	
	public DayField()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		area = new JTextArea();
		area.setEditable(false);
		hour = 0;
		flag = false;
	}
	
	public void drawField()
	{
		JLabel label = new JLabel(Integer.toString(hour));
		label.setPreferredSize(new Dimension(40, 200));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		this.add(label);
		this.add(area);
	}
	
	//getters
	public int getHour()
	{
		return hour;
	}
	
	
	public String getStringHour()
	{
		String s = "";
		if (hour < 10)
			return s+= "0" + hour;
		return s + hour;
	}
	

	public JTextArea getArea()
	{
		return area;
	}
	
	//setters
	public void setHour(int h)
	{
		hour = h;
	}
	
	public void setAreaText(String s)
	{
		area.setText(s);
	}
	
	public void setFlag(boolean value)
	{
		flag = value;
	}
	
	public String getAreaText()
	{
		return area.getText();
	}
	
	public boolean isFlagged()
	{
		return flag;
	}
	
}
