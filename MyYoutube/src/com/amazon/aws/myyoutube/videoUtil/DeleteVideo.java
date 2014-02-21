package com.amazon.aws.myyoutube.videoUtil;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class DeleteVideo {
	public static boolean deleteVideo(String fileName) {
		  String bucketName = "aniketutube"; // change to proper value
		  System.out.println("Removing video file from AmazonS3 from a file: " + fileName);
		  File file;
		  try {
			System.out.println("Fetching Credentials");
			AWSCredentials pc =new PropertiesCredentials(UploadVideo.class.getResourceAsStream("AwsCredentials.properties"));
			System.out.println(pc);
			System.out.println("Received credentials");
		    AmazonS3Client s3 = new AmazonS3Client(pc);
		    System.out.println("Amazon credentials loaded.");
		    s3.deleteObject(bucketName, fileName);
		    
		    AmazonRDSClient rds = new AmazonRDSClient(pc);
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
	        
	        String query="DELETE from User_Rating where video_name='"+fileName+"';";
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
	    catch(Exception e)
	    {
		   System.out.println("Unable to delete "+fileName+" error");
		   e.printStackTrace();
	    }
		return false;
	}

	
}
