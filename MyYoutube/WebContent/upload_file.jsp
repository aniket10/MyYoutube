<%@page import="com.amazon.aws.myyoutube.videoUtil.UploadVideo"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.amazonaws.*" %>
<%@ page import="com.amazonaws.auth.*" %>
<%@ page import="com.amazonaws.services.ec2.*" %>
<%@ page import="com.amazonaws.services.ec2.model.*" %>
<%@ page import="com.amazonaws.services.s3.*" %>
<%@ page import="com.amazonaws.services.s3.model.*" %>
<%@ page import="com.amazonaws.services.dynamodbv2.*" %>
<%@ page import="com.amazonaws.services.dynamodbv2.model.*" %>

<%! // Share the client objects across threads to
    // avoid creating new clients for each web request
    private AmazonEC2         ec2;
    private AmazonS3           s3;
    private AmazonDynamoDB dynamo;
 %>

<%
    /*
     * AWS Elastic Beanstalk checks your application's health by periodically
     * sending an HTTP HEAD request to a resource in your application. By
     * default, this is the root or default resource in your application,
     * but can be configured for each environment.
     *
     * Here, we report success as long as the app server is up, but skip
     * generating the whole page since this is a HEAD request only. You
     * can employ more sophisticated health checks in your application.
     */
    if (request.getMethod().equals("HEAD")) return;
%>

<%
    if (ec2 == null) {
        AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
        ec2    = new AmazonEC2Client(credentialsProvider);
        s3     = new AmazonS3Client(credentialsProvider);
        dynamo = new AmazonDynamoDBClient(credentialsProvider);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>Hello AWS Web World!</title>
    <link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
    	<script type="text/javascript">
		function list_videos()
		{
			val = document.getElementById("file").value;
			tE = document.getElementById("filepath");
			alert(val);
			tE.setAttribute("value", val);
			return true;
		}
	</script>
</head>
<body>
 	<div id="controls">
		<h1> Welcome to My Youtube</h1>
		<hr>
		<form action="" id="options_form" action="get_list">
			<input type="button" name="upload" value="Upload Video" onclick="upload_video()" id="upload_button"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit" name="list" value="List Videos" />		
			<hr><br><br><br><br>
		</form>
	</div>
	<div id="activity">
			<h3>Select the Video you wish to upload</h3>
			<form action="VideoUpload" method="post" id="upload_form" enctype= "multipart/form-data">
				<input type="file" id="file" name="file" accept="video/*" onchange="return list_videos();"/> 
				<input type="text" id="filepath" name="filepath" />
			    <input type="submit" value="Upload" name="submit" />
			</form>
	</div>
</body>
</html>