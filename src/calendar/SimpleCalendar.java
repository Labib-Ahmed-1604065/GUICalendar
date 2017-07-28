package calendar;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class SimpleCalendar{

	public static void main(String[] args)
	{
		Scheduler s = new Scheduler();
		MonthGUI monthView = new MonthGUI(s);
		DayGUI dayView = new DayGUI(s);
		NavigateGUI navigateGUI = new NavigateGUI(s);

		s.attach(monthView);
		s.attach(dayView);

		JFrame frame = new JFrame();
		monthView.drawView();
		dayView.drawView();
		navigateGUI.drawView();
		frame.setSize(800, 300);
		frame.setLayout(new BorderLayout());

		frame.add(monthView, BorderLayout.WEST);
		frame.add(navigateGUI, BorderLayout.NORTH);
		frame.add(dayView, BorderLayout.CENTER);

		//if events.txt exists 
		//load events 
		File f = new File("events.txt");
		
		if(f.exists() && !f.isDirectory()) { 
		    try {
				s.load();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		frame.setVisible(true);

		
		while (navigateGUI.isQuitClicked() == false)
		{

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}


		//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		if (navigateGUI.isQuitClicked() == true)
		{
			frame.setVisible(false);
			frame.dispose();
			//terminate program
			System.exit(0);
		}

	}

	//view 
	//controller
	//model
	/*
	 * model:
	 * mutator 
	 * accessor
	 * attach listeners 
	 * data structure storing data 
	 * update:
	 * - update data
	 * - notify views 
	 * 
	 * view:
	 * GUI part 
	 * - graphical representation of data
	 * Listener part:
	 * - when data is changed, access the model data and repaint 
	 * 
	 * Controller:
	 * GUI part
	 * - processes user input
	 * Listener part:
	 * - notifies model of input, using the model mutator
	 */
}
