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
			alert("in fun1");
			window.location.href="list_videos.jsp";
		}
		function perform_upload()
		{
			alert("uploading file...");
		}
		function upload_video()
		{
			alert("redirecting...");
			window.location.href="upload_file.jsp";
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
			<input type="submit" name="list" value="List Videos" onclick="list_videos()"/>		
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" name="play" value="Play Video"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" name="delete" value="Delete Video"/>
			<hr><br><br><br><br>
		</form>
	</div>
	<div id="activity">
		<h3> List of videos uploaded</h3> 
	    <table id="video_list">
	   		
	   		<%@page import="java.util.ArrayList" %>
	   		<% ArrayList arr = (ArrayList) request.getAttribute("link");    
	   		   for(int i=0;i<arr.size();i++) {
	   		%> 
	   			<tr>
	   				<td> <%= arr.get(i) %> </td>
	   				<td>
	   					<form name="play_form" action="ExtractURL">
	   						<input type="submit"  value="Play" name="<%=arr.get(i) %>"/>
	   					</form>	
	   				</td>
	   				<td>
	   					<form name="delete_form" action="Delete_Video">
	   						<input type="submit" value="Delete" name="<%=arr.get(i) %>"/>
	   					</form>	
	   				</td>
	   				</tr>	
	   			<%} %>			   
	   </table>
	</div>
</body>
</html>