package com.stempin.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource){
		dataSource=theDataSource;		
	}
	
	
	public List<Student> getStudents() throws Exception
	{
		List<Student> students=new ArrayList<>();
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
			//get a connection
			myConn=dataSource.getConnection();
			
			//create a SQL statement
			String sql="select * from student ORDER BY last_name";
			myStmt=myConn.createStatement();
			
			//execute query
			myRs=myStmt.executeQuery(sql);
			
			// process result set
			while (myRs.next())
			{
				//retrieve data from result set row
				int id=myRs.getInt("id");
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				// create new student object
				Student tempStudent=new Student(firstName, lastName, email,id);
				
				// add it to the list of students
				students.add(tempStudent);
			}
			
			return students;
		}
		
		finally 
		{
			//close JDBC objects
			close(myConn, myStmt, myRs);
		}
		
	}


	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try
		{
			if (myRs != null){
				myRs.close();
			}
			
			if (myStmt != null)
			{
				myStmt.close();
			}
			
			if (myConn != null)
			{
				myConn.close();
			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}


	public void addStudent(Student theStudent) throws SQLException {
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try{
			//get db connection
			myConn=dataSource.getConnection();
			//create a sql for insert
			String sql="insert into student "+"(first_name, last_name, email) "+ "values (?, ?, ?)";
			
			myStmt=myConn.prepareStatement(sql);
			
			//set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());			
			
			
			//execute the sql insert
			myStmt.execute();
		}
			
		finally
		{
		//clean up JDB object
			close(myConn, myStmt, null);
		}
	}
	
	
}

