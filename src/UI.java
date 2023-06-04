


import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;




public class UI implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	//Main UI components
	JFrame coreFrame;
	Container  mainContainer, inoutContainer, addremoveContainer, reportsContainer;
	JButton inoutButton, addremoveButton, reportsButton, backButton, punchButton, addButton, removeButton, generateReportButton;
	JTextField addField, firstDate, lastDate;
	JLabel reports;
	JComboBox<String> empList;
 
	//Database Connection
	DBInterface dbConn;
	ResultSet empResultSet;
	
	
	UI() throws SQLException
	{
		
		//Not very MVC of you
		dbConn = new DBInterface();
	
		
		//Core Frame + Buttons
		coreFrame = new JFrame();
		coreFrame.setSize(300, 500);
		coreFrame.setVisible(true);
		coreFrame.setResizable(false);
		coreFrame.setBackground(Color.CYAN);
		
		mainContainer = new Container();
		mainContainer.setSize(300, 500);
		mainContainer.setBackground(Color.CYAN);
		coreFrame.add(mainContainer);
			
		
		inoutButton = new JButton();
		mainContainer.add(inoutButton);
		inoutButton.setBounds(50, 50, 200, 100);
		inoutButton.setLabel("Punch In/Out");
		inoutButton.addActionListener(this);
		
		 
		addremoveButton = new JButton();
		mainContainer.add(addremoveButton);
		addremoveButton.setBounds(50, 175, 200, 100);
		addremoveButton.setLabel("Add/Remove an Employee");
		addremoveButton.addActionListener(this);
		
		reportsButton = new JButton();
		mainContainer.add(reportsButton);
		reportsButton.setBounds(50, 300, 200, 100);
		reportsButton.setLabel("Generate Reports");
		reportsButton.addActionListener(this);

		
		
		
	//Submenus
		
		//Clock in/out
		inoutContainer = new Container();
		coreFrame.add(inoutContainer);
		inoutContainer.setVisible(false);
		inoutContainer.setSize(300, 500);
		
		punchButton = new JButton();
		punchButton.addActionListener(this);
		punchButton.setBounds(50, 300, 200, 100);
		
		
		//Add/Remove Menu
		addremoveContainer = new Container();
		coreFrame.add(addremoveContainer);
		addremoveContainer.setVisible(false);
		addremoveContainer.setSize(300, 500);
		
		removeButton = new JButton();
		removeButton.setBounds(150, 300, 99, 99);
		removeButton.setLabel("Delete");
		removeButton.addActionListener(this);
		addremoveContainer.add(removeButton);
		
		addButton = new JButton();
		addButton.setBounds(50, 300,99, 99);
		addButton.setLabel("Add");
		addButton.addActionListener(this);
		addremoveContainer.add(addButton);
		
		addField = new JTextField();
		addField.setBounds(50, 250, 100, 20);
		addremoveContainer.add(addField);	
		
		
		
		//Generate Reports
		reportsContainer = new Container();
		coreFrame.add(reportsContainer);
		reportsContainer.setVisible(false);
		reportsContainer.setSize(300, 500);
		
		generateReportButton = new JButton();
		generateReportButton.setBounds(50, 300, 200, 100);
		generateReportButton.addActionListener(this);
		generateReportButton.setLabel("Generate Report");
		reportsContainer.add(generateReportButton);
		
		
		firstDate = new JTextField();
		lastDate = new JTextField();
		
		firstDate.setBounds(50, 200,99, 30);
		lastDate.setBounds(150, 200,99, 30);
		
		firstDate.setText("yyyy-mm-dd");
		lastDate.setText("yyyy-mm-dd");
		
		reports = new JLabel("Start                          End");
		reports.setBounds(75, 165, 200, 30);
		
		reportsContainer.add(reports);
		reportsContainer.add(firstDate);
		reportsContainer.add(lastDate);		
		
		
		//Universal
		backButton = new JButton();
		backButton.setLabel("Back");
		backButton.addActionListener(this);
		backButton.setBounds(10, 10, 100, 30);
		

		
		//Employee List
		empList = new JComboBox<String>();
		empList.setBounds(50, 50, 200, 50);
		
		updateList();
		
	}
	
	private void hideLayers() 
	{
		mainContainer.setVisible(false);
		inoutContainer.setVisible(false);
		addremoveContainer.setVisible(false);
		reportsContainer.setVisible(false);
		
	}

	
	public void actionPerformed(ActionEvent e)
	{
		
		 if(e.getSource().equals(backButton)) 
			{
			 	hideLayers();
				mainContainer.setVisible(true);
				
				inoutContainer.remove(backButton);
				addremoveContainer.remove(backButton);
				reportsContainer.remove(backButton);
				
				inoutContainer.remove(empList);
				addremoveContainer.remove(empList);
				reportsContainer.remove(empList);
				
			}
		else
		{
			try {
				updateList();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			if(e.getSource().equals(addremoveButton)) 
			{
				hideLayers();
				addremoveContainer.setVisible(true);
				addremoveContainer.add(backButton);
				addremoveContainer.add(empList);

				
				
			}
			else if(e.getSource().equals(reportsButton)) 
			{
				hideLayers();
				reportsContainer.setVisible(true);
				reportsContainer.add(backButton);
				reportsContainer.add(empList);
				empList.addItem("All Employees");
				
				
			}
			else
			if(e.getSource().equals(inoutButton)) 
			{
				hideLayers();
				inoutContainer.setVisible(true);
				inoutContainer.add(backButton);
				inoutContainer.add(punchButton);
				inoutContainer.add(empList);
				
			}
			else if(e.getSource().equals(punchButton)) 
			{
				String cleanUp = String.valueOf(empList.getSelectedItem());
				try 
				{
					dbConn.punchTime(Integer.valueOf(cleanUp.substring(0, 4).replaceAll("\\s", "")));
				} 
				catch (NumberFormatException e1) 
				{
					
					e1.printStackTrace();
				} 
				catch (SQLException e1) 
				{
					
					e1.printStackTrace();
				}
				
				
			}
			else if(e.getSource().equals(addButton))
			{
				
				if(! addField.getText().equals(null))
				{
					try {
						dbConn.addEmployee(addField.getText());
						updateList();
						
					
					} catch (SQLException e1) {
					
						e1.printStackTrace();
					}
				}
				
			}
			else if(e.getSource().equals(removeButton))
			{
				
				try {
					int id = Integer.valueOf(((String) empList.getSelectedItem()).substring(0, 4).replaceAll("\\s", ""));
							dbConn.deleteEmployee(id);
							updateList();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
			else if(e.getSource().equals(generateReportButton)) 
			{
				empList.addItem("All Employees");
				
				if(empList.getSelectedItem().equals("All Employees")) 
				{
					
					try {
						dbConn.timeQuery(
								0,
								Timestamp.valueOf(firstDate.getText()),
								Timestamp.valueOf(lastDate.getText())
								);
					} catch (SQLException e1) {
						
						e1.printStackTrace();
					}
					
				}
				else
				{
					
						try {
							System.out.print(Timestamp.valueOf(firstDate.getText() + " 00:00:00"));
							dbConn.timeQuery(
									Integer.valueOf(((String) empList.getSelectedItem()).substring(0, 4).replaceAll("\\s", "")),
									Timestamp.valueOf(firstDate.getText() + " 00:00:00"),
									Timestamp.valueOf(lastDate.getText() + " 00:00:00")
									);
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					
				}
				
				
			}
		}
		
	}
	
	public void updateList() throws SQLException
	{
		empList.removeAllItems();
		empResultSet = dbConn.populateQuery();
		while(empResultSet.next())
		{   
			empList.addItem(empResultSet.getString(1).concat("   "+ empResultSet.getString(2)));
			
		
		}
		
	
		
	}
	
	

}
