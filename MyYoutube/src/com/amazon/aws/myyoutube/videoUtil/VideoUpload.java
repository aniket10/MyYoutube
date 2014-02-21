package com.amazon.aws.myyoutube.videoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
/**
 * Servlet implementation class VideoUpload
 */
public class VideoUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VideoUpload() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/*String name=request.getParameter("file");
		String path=request.getParameter("path");
		String filename=path+name;
		System.out.println("filename is "+filename);

		String ret = UploadVideo.uploadVideoToAmazonS3(filename);

		System.out.println("**********************************\nFunction returned "+ret+"\n***********************************");
		//		response.sendRedirect("video_link.jsp?link="+ret);
		String url="/video_link.jsp";
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(url);

		request.setAttribute("link",ret);
		rd.forward(request, response);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		try{

			String filename=null;
			
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {
				System.out.println(item);
				if (item.isFormField()) {
					/*// Process regular form field (input type="text|radio|checkbox|etc", select, etc).
					String fieldname = item.getFieldName();
					System.out.println("fieldname "+fieldname);
					String fieldvalue = item.getString();
					System.out.println("fieldvalue "+fieldvalue+"filedname "+item.getName());
					// ... (do your job here)
					filename=item.getName();*/
				} 
				else {
					filename = FilenameUtils.getName(item.getName());
					InputStream filecontent = item.getInputStream();
					
					String ret = UploadVideo.uploadVideoToAmazonS3(filecontent,filename);

					System.out.println("\n*****************\nFunction returned "+ret+"\n***********************************");
					String url="/video_link.jsp";
					ServletContext sc = getServletContext();
					RequestDispatcher rd = sc.getRequestDispatcher(url);

					request.setAttribute("link",ret);
					rd.forward(request, response);
				}			
			}
			
//			String ret = UploadVideo.uploadVideoToAmazonS3(filecontent);
//
//			System.out.println("\n*****************\nFunction returned "+ret+"\n***********************************");
//			String url="/video_link.jsp";
//			ServletContext sc = getServletContext();
//			RequestDispatcher rd = sc.getRequestDispatcher(url);
//
//			request.setAttribute("link",ret);
//			rd.forward(request, response);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
