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
   	<script type='text/javascript' src='https://s3.amazonaws.com/aniketutube/jwplayer.js'></script>
	</head>
	<% String video = request.getAttribute("play").toString();
	   String rating = request.getAttribute("rating").toString();
	   String count = request.getAttribute("count").toString();
	   String link = "rtmp://d2w3h3qqem60ck.cloudfront.net/cfx/st/"+video;

	%>
<body>
 	<div id="controls">
		<h1> Welcome to My Youtube</h1>
		<hr>
		<form id="options_form" action="Get_List" method="get">
			<input type="button" name="upload" value="Upload Video" onclick="upload_video()" id="upload_button"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit" name="list" value="List Videos"/>		
			<hr><br><br><br><br>
		</form>
	</div>

<div id='user_interactive'>
		<h3>Playing <%=video %></h3>
	    <h3>Current Rating for the video <%= rating %></h3>
	    <form action="UpdateRating" method="get">
	    	<input type="hidden" value=<%=video %> name="video_name"/>
	    	<input type="hidden" value=<%=rating %> name="old_rating" />
	    	<input type="hidden" value=<%=count %> name="count" />
	    	<select name="user_rating">
	    		<option></option>
	    		<option>1</option>
	    		<option>2</option>
	    		<option>3</option>
	    		<option>4</option>
	    		<option>5</option>
	    	</select>
	    	<input type="submit" value="Rate" name="rate"/>
	    </form>
	    <br><br>
	</div>

	<div id='mediaplayer'></div>
<script type="text/javascript">
   jwplayer('mediaplayer').setup({
      file: "<%= video %>",
      width: "720",
      height: "480"
   });
</script>
</body>
</html>