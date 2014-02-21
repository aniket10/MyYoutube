package com.amazon.aws.myyoutube.videoUtil;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ExtractURL
 */
public class ExtractURL extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExtractURL() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String key="";
		Map<String,String[]> m = request.getParameterMap();
		   for (Map.Entry<String,String[]> params : m.entrySet())
		   {
			   key=params.getKey();
			   System.out.println("Key is "+key);
		   }
		   String res=GetURL.getURL(key);
		   String com=GetURL.getRating(key);
		   System.out.println("Res is "+res);
		   System.out.println("key is "+key);
		   String arr[] = com.split(":");
		   double rating = new Double(arr[0]).doubleValue();
		   int count = new Integer(arr[1]).intValue();
		   String url="/play_video.jsp";
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			System.out.println("created rd");
			request.setAttribute("play",key);
			request.setAttribute("rating", rating);
			request.setAttribute("count", count);
			rd.forward(request, response);
		   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
