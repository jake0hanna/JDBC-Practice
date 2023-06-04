

import java.sql.*;
import java.text.SimpleDateFormat;

public class DBInterface 
{
	//add entry on each employees time table
	//remove entry on any table
	//populate from employee table
	//populate per entry from employees individual time table
	//populate 
	
	String allTimeQuery, individualTimeQuery;
	Connection conn;
	Statement stmt;
	PreparedStatement pstmt;
	
	public static final String URL = "jdbc:mysql://localhost:3306/punchclockdb";
	private static final String USER = "java";
	private static final String PASSWORD = "xxxx";

	public DBInterface() throws SQLException 
	{
		 conn = DriverManager.getConnection(URL, USER, PASSWORD); 
	             
	}
	

	public ResultSet timeQuery(int id, Timestamp startTime, Timestamp endTime) throws SQLException
	{
		PreparedStatement pstmt;
		//0 = ALL
		if(id == 0) 
		{
			String employeeAmountQuery = "SELECT * FROM employees ORDER BY id DESC";
			
			pstmt = conn.prepareStatement(employeeAmountQuery);
			ResultSet empAmount = pstmt.executeQuery();
			empAmount.next();

			for(int i = 1; i > (empAmount.getInt(1)+1) ; i++) 
			{
				String allTimeQuery = "SELECT times.punchtime "
					+ "FROM  times "
					+ "WHERE (? = times.id AND ? < times.punchtime AND ? > times.punchtime) " 
					+ "ORDER BY times.punchtime";

				pstmt = conn.prepareStatement(allTimeQuery);
				pstmt.setInt(1, id);
				pstmt.setTimestamp(2, startTime);
				pstmt.setTimestamp(3, endTime);

				ResultSet rset = pstmt.executeQuery();
				
				System.out.println("Employee " + id + "'s punched times are:");
				long hours = 0;
				long previousTS = 0;
				int tsTracker = 0;
			
				while(rset.next()) 
				{   
					Timestamp ts   = rset.getTimestamp("punchtime");   
					System.out.println(ts);
				
					if(tsTracker == 0)
					{
						previousTS = ts.getTime();
						tsTracker++;
					}
					else 
					{
						hours += (ts.getTime() - previousTS);
						tsTracker--;
					}
				}
				System.out.println("Total number of hours = " + hours /1000 /60 /60);
				rset.close();
			}
        pstmt.close();
    }
    else 
    {
        String individualTimeQuery = "SELECT times.punchtime "
            + "FROM  times "
            + "WHERE ? = times.id "
            + "ORDER BY times.punchtime";

        pstmt = conn.prepareStatement(individualTimeQuery);
        pstmt.setInt(1, id);

        ResultSet rset = pstmt.executeQuery();

        System.out.println("Employee " + id + "'s punched times are:");
        long hours = 0;
        long previousTS = 0;
        int tsTracker = 0;
    
        while(rset.next()) 
        {   
            Timestamp ts   = rset.getTimestamp("punchtime");   
            System.out.println(ts);
        
            if(tsTracker == 0)
            {
                previousTS = ts.getTime();
                tsTracker++;
            }
            else 
            {
                hours += (ts.getTime() - previousTS);
                tsTracker--;
            }
        }
        System.out.println("Total number of hours = " + hours /1000 /60 /60);
        rset.close();
        pstmt.close();
	}
		return null;
	}
	
	public ResultSet populateQuery() throws SQLException 
	{
     
		String populateQuery = "SELECT * FROM employees;";
		       
		ResultSet rset = stmt.executeQuery(populateQuery);
		 
		return rset;

		
		
	}
	
    public void punchTime(int id) throws SQLException
    {
        long msTime = System.currentTimeMillis()/1000;
        Timestamp ct = new Timestamp(msTime);
        String currentTime = ct.toString().split("\\.")[0];
        
        String punchTime = "INSERT into times values (?, ?);";
        
        pstmt = conn.prepareStatement(punchTime);
        pstmt.setInt(1, id);
        pstmt.setString(2, currentTime);

        pstmt.execute();
    }

    public void addEmployee(String name) throws SQLException 
    {
        String employeeAmountQuery = "SELECT * FROM employees ORDER BY id DESC";

        pstmt = conn.prepareStatement(employeeAmountQuery);
        ResultSet empAmount = pstmt.executeQuery();
        empAmount.next();

        String populateQuery = "INSERT into employees values (?, ?);";
      
        pstmt = conn.prepareStatement(populateQuery);
        pstmt.setInt(1, empAmount.getInt(1)+1);
        pstmt.setString(2, name);

        pstmt.execute();
    }

    public void deleteEmployee(int id) throws SQLException
    {
        String deleteQuery = "DELETE FROM employees WHERE id = ?";

        pstmt = conn.prepareStatement(deleteQuery);
        pstmt.setInt(1, id);
        
        pstmt.execute();
    }

	
	
}
