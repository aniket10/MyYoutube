package com.amazon.aws.myyoutube.videoUtil;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;

public class Update_Rating {
	public static boolean updateRating(String video_name, double old_rating, double new_rating,int count)
	{
		double rating=0;
		AWSCredentials credentials;
		try {
			credentials = new PropertiesCredentials(
					Assignment2.class.getResourceAsStream("AwsCredentials.properties"));
			AmazonRDSClient rds = new AmazonRDSClient(credentials);
			DescribeDBInstancesRequest instRequest = new DescribeDBInstancesRequest().withDBInstanceIdentifier("mydbinstance");
			
			DescribeDBInstancesResult instres = rds.describeDBInstances(instRequest);
			     		
	 		Endpoint e = instres.getDBInstances().get(0).getEndpoint();
			System.out.println("ENd point "+e.getAddress()+" "+e.getPort() );
			System.out.println("Database Created");
			System.out.println("Creating a table");
			//connection
			java.sql.Connection con = null;
	        // Format "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
	        String url = "jdbc:mysql://"+e.getAddress()+":"+e.getPort()+"/dbname?user=master&password=password";   //  "jdbc:mysql://master:password@"+e+"/dbname";
	        System.out.println("Url is "+url); 
	        String user = "master";
	        String password = "password";
	        Class.forName("com.mysql.jdbc.Driver");
	        con = DriverManager.getConnection(url, user, password);
	        	        
	        System.out.println("Connection created");
	        
	        java.sql.Statement stat = con.createStatement();
	        
	        double final_rating = (old_rating * count + new_rating)/(count+1);
	        count=count+1;
	        
	        String query="UPDATE User_Rating SET COUNT="+count+", RATING="+final_rating+" WHERE video_name='"+video_name+"';";
	        System.out.println(query);
	        stat.execute(query);
	        
	        return true;      
	        
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	
		
		
	}
}
