package com.amazon.aws.myyoutube.videoUtil;

/*
 * Copyright 2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * Modified by Aniket
 * aniket@nyu.edu
 * 
 */

import java.sql.DriverManager;
import java.util.ArrayList;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.AuthorizeDBSecurityGroupIngressRequest;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.CreateDBSecurityGroupRequest;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DBSecurityGroup;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.rds.model.Endpoint;
import com.mysql.jdbc.Statement;

public class GetRDSInstance {

	/*
	 * Important: Be sure to fill in your AWS access credentials in the
	 *            AwsCredentials.properties file before you try to run this
	 *            sample.
	 * http://aws.amazon.com/security-credentials
	 */

	static AmazonRDSClient rds;	
	static AWSCredentials credentials; 

	public static void main(String[] args) throws Exception {

		
		credentials = new PropertiesCredentials(
				GetRDSInstance.class.getResourceAsStream("AwsCredentials.properties"));
	
		try {
			rds = new AmazonRDSClient(credentials);
		
			/*********************************************
			 *  RDS DB
			 *********************************************/
			
			System.out.println("Creating a database instance");
	
			CreateDBSecurityGroupRequest d = new CreateDBSecurityGroupRequest()
			.withDBSecurityGroupName("javaSecurityGroup1")
			.withDBSecurityGroupDescription("DB security group1");
			rds.createDBSecurityGroup(d);
	
			AuthorizeDBSecurityGroupIngressRequest auth = new AuthorizeDBSecurityGroupIngressRequest()
			.withDBSecurityGroupName("javaSecurityGroup1")
	//			.withEC2SecurityGroupName("javaSecurityGroup")
			.withCIDRIP("0.0.0.0/0");
	//			.withCIDRIP("216.165.95.69/32");
			
			DBSecurityGroup dbsecuritygroup= rds.authorizeDBSecurityGroupIngress(auth);
			String[] dBSecurityGroups= {dbsecuritygroup.getDBSecurityGroupName()} ;
	
			CreateDBInstanceRequest createDBInstanceRequest = new CreateDBInstanceRequest()
			.withEngine("MySQL")
			.withLicenseModel("general-public-license")
			.withEngineVersion("5.6.13")
			.withDBInstanceClass("db.t1.micro")
			.withMultiAZ(false)
			.withAutoMinorVersionUpgrade(true)
			.withAllocatedStorage(5)
			.withDBInstanceIdentifier("mydbinstance1")
			.withMasterUsername("awsuser")
			.withMasterUserPassword("mypassword")
			.withDBName("dbname1")
			.withPort(3306)
			.withAvailabilityZone(null)
			.withDBSecurityGroups(dBSecurityGroups);
	
			ArrayList<String> arrDbSecur = new ArrayList<String>();
			arrDbSecur.add("javaSecurityGroup1");
			createDBInstanceRequest.setDBSecurityGroups(arrDbSecur);
	
			DBInstance dbInstance = rds.createDBInstance(createDBInstanceRequest);
			
	 		Thread.sleep(600000);
	
			DescribeDBInstancesRequest instRequest = new DescribeDBInstancesRequest().withDBInstanceIdentifier("mydbinstance1");
			
			DescribeDBInstancesResult instres = rds.describeDBInstances(instRequest);
			     		
	 		Endpoint e = instres.getDBInstances().get(0).getEndpoint();
			System.out.println("ENd point "+e.getAddress()+" "+e.getPort() );
			System.out.println("Database Created");
			System.out.println("Creating a table");
			//connection
			java.sql.Connection con = null;
	        Statement st = null;
	     // Format "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
	        String url = "jdbc:mysql://"+e.getAddress()+":"+e.getPort()+"/dbname1?user=awsuser&password=mypassword";   //  "jdbc:mysql://master:password@"+e+"/dbname";
	        System.out.println("Url is "+url); 
	        String user = "awsuser";
	        String password = "mypassword";
	        
	        con = DriverManager.getConnection(url, user, password);
	        
	        System.out.println("Connection created");
	        
	        java.sql.Statement stat = con.createStatement();
	        
	        String query="CREATE TABLE Items ( item_id VARCHAR(200), type INTEGER, quantity INTEGER, user VARCHAR(100), price FLOAT(5,2) );";
	        stat.execute(query);
	        
	        String query1="CREATE TABLE WishList ( user VARCHAR(200), wishlistId VARCHAR(100) );";
	        stat.execute(query1);

	} catch (AmazonServiceException ase) {
		System.out.println("Caught Exception: " + ase.getMessage());
		System.out.println("Response Status Code: " + ase.getStatusCode());
		System.out.println("Error Code: " + ase.getErrorCode());
		System.out.println("Request ID: " + ase.getRequestId());
	}
	}
}

