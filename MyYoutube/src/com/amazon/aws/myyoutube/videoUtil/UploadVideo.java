package com.amazon.aws.myyoutube.videoUtil;

import java.io.File;
import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

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

public class UploadVideo {
	private static final Logger log = Logger.getLogger(UploadVideo.class.getName());

	public static String uploadVideoToAmazonS3(InputStream filecontent,String filename) {
		String amazonURLToRecording = "URL not generated"; //default value
		String bucketName = "aniketutube"; // change to proper value
		System.out.println("Uploading video file to AmazonS3 from a file: " + filename);
		System.out.println(filename);
		//		File file;

		try {
			AWSCredentials pc =new PropertiesCredentials(UploadVideo.class.getResourceAsStream("AwsCredentials.properties"));
			AmazonS3Client s3 = new AmazonS3Client(pc);
			AmazonRDSClient rds = new AmazonRDSClient(pc);
			//			file = new File(filecontent);
			//			InputStream item = filecontent.getInputStream();
			// pass input stream and metadata and take input stream twice
System.out.println("credentials");
			ObjectMetadata metaData = new ObjectMetadata();
			metaData.setContentType("application/octet-stream"); //binary data
			PutObjectRequest putObject = new PutObjectRequest(bucketName, filename, filecontent, metaData).withCannedAcl(CannedAccessControlList.PublicRead);;
			putObject.setMetadata(metaData);
			s3.putObject(putObject);
			System.out.println("Video was successfully uploaded.");
			log.info("video uploaded");


			Calendar origDay = Calendar.getInstance();
			Calendar expirationDay = (Calendar) origDay.clone();
			expirationDay.add(Calendar.DAY_OF_YEAR, 30);
			Date expirationDate = expirationDay.getTime();

			amazonURLToRecording = s3.generatePresignedUrl(bucketName,filename, expirationDate, HttpMethod.GET).toString();
			System.out.println("Amazon URL Get: " + amazonURLToRecording);

			System.out.println("URL to the new recording:" + amazonURLToRecording);	    

			DescribeDBInstancesRequest instRequest = new DescribeDBInstancesRequest().withDBInstanceIdentifier("mydbinstance");

			DescribeDBInstancesResult instres = rds.describeDBInstances(instRequest);

			Endpoint e = instres.getDBInstances().get(0).getEndpoint();
			System.out.println("End point "+e.getAddress()+" "+e.getPort() );
			System.out.println("Database Created");
			log.info("database created");

			//connection
			java.sql.Connection con = null;
			String url = "jdbc:mysql://"+e.getAddress()+":"+e.getPort()+"/dbname?user=master&password=password";   //  "jdbc:mysql://master:password@"+e+"/dbname";
			System.out.println("Url is "+url); 
			String user = "master";
			String password = "password";
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);

			System.out.println("Connection created");
			log.info("connection created");
			java.sql.Statement stat = con.createStatement();

			String query="INSERT INTO User_Rating VALUES ('"+filename+"',0.0,0);";
			stat.execute(query);
			return amazonURLToRecording;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "\nERROR IN UPLOADING THE VIDEO...PLEASE TRY AGAIN";
		}

	}

	/*	public static void main()
	{
		new UploadVideo().uploadVideoToAmazonSs3("C:\\Users\\aniket_a\\Documents\\Youcam\\video1");
	}*/
}