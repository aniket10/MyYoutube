package com.amazon.aws.myyoutube.videoUtil;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.sql.ResultSetMetaData;


public class ExtractVideo {
	public static ArrayList<String> getAllVideos()
	{
		System.out.println("Reached function");
		ArrayList<String> res = new ArrayList<String>();
		ResultSet r=null;
		
		String bucketName = "aniketutube"; // change to proper value
		try {
			AWSCredentials pc =new PropertiesCredentials(UploadVideo.class.getResourceAsStream("AwsCredentials.properties"));
			AmazonRDSClient rds = new AmazonRDSClient(pc);
			AmazonS3Client s3 = new AmazonS3Client(pc);

			ListObjectsRequest lor = new ListObjectsRequest().withBucketName(bucketName);
			ObjectListing ol = s3.listObjects(lor);
			for (S3ObjectSummary os : ol.getObjectSummaries())
			{	
				System.out.println(os.getKey());
//				res.add(os.getKey());
			}

			DescribeDBInstancesRequest instRequest = new DescribeDBInstancesRequest().withDBInstanceIdentifier("mydbinstance");

			DescribeDBInstancesResult instres = rds.describeDBInstances(instRequest);

			Endpoint e = instres.getDBInstances().get(0).getEndpoint();
			//			System.out.println("End point "+e.getAddress()+" "+e.getPort() );

			//connection
			java.sql.Connection con = null;
			String url = "jdbc:mysql://"+e.getAddress()+":"+e.getPort()+"/dbname?user=master&password=password";   //  "jdbc:mysql://master:password@"+e+"/dbname";
			//			System.out.println("Url is "+url); 
			String user = "master";
			String password = "password";
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);

			System.out.println("Connection created");

			java.sql.Statement stat = con.createStatement();

			String query="SELECT * FROM User_Rating ORDER BY rating DESC;";
			stat.execute(query);
			r=stat.getResultSet();
			System.out.println(r);	

			ResultSetMetaData Meta = null;
			Meta = r.getMetaData();
			System.out.println("meta "+Meta);
			while (r.next()) {
				String name = r.getString("video_name");
				System.out.println(name);
				res.add(name);
			}
		}
		catch(Exception e){}
		return res;
		//		return r;
	}
}
