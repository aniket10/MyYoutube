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
import com.amazonaws.services.s3.AmazonS3Client;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.Driver;

public class GetURL {
	public static String getURL(String key)
	{
		try {
			System.out.println("Fetching Credentials");
			AWSCredentials pc =new PropertiesCredentials(UploadVideo.class.getResourceAsStream("AwsCredentials.properties"));
			System.out.println(pc);
			System.out.println("Received credentials");
		    AmazonS3Client s3 = new AmazonS3Client(pc);
		    
		    String url=s3.getUrl("aniketutube", key).toString();
		    System.out.println(url);
		    
		    return url;
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String getRating(String key)
	{
		String rating="";
		String count="";
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
	        
	        String query="SELECT rating,count from User_Rating where video_name='"+key+"';";
	        System.out.println(query);
	        ResultSet rs = stat.executeQuery(query);
	       
	        if(rs.next()) {
	        	System.out.println("Rating is "+rs.getFloat("rating"));
	        rating = rs.getString("rating");
	        count = rs.getString("count");
	        }
	        System.out.println("Rating for "+key+"is "+rating+" by "+count+" people");
	        
	        
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
		return rating=="" || count == ""?"0:0":rating+":"+count;
		
	}
}
