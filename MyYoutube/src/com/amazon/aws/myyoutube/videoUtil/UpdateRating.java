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
 * Servlet implementation class UpdateRating
 */
public class UpdateRating extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateRating() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "unchecked", "null" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	   System.out.println("******Updating the rating*******");
	   int i=0;
	   double new_rating=0;
	   double old_rating=0;
	   int count=0;
	   		   	
	   Map<String,String[]> m = request.getParameterMap();
	   int n=m.size();
	   String[] key = new String[5];
	   String[] value = new String [5];
	   
	   System.out.println("Reqest string had "+m.size()+" argunments");
	   for (Map.Entry<String,String[]> params : m.entrySet())
	   {
		   key[i]=params.getKey();
		   System.out.println("Value size "+params.getValue().toString());
		   value[i] = params.getValue()[0]; 
		   
		   System.out.println("i= "+i+" key is "+key[i]+" value "+value[i]);
		   i++;
	   }
	   
	   String video_name = value[1];
	   count = new Integer(value[2]).intValue();
	   old_rating = new Double(value[3]).doubleValue();	   
	   new_rating = new Double(value[4]).doubleValue();
	   
	   boolean res = Update_Rating.updateRating(video_name,old_rating,new_rating,count);
	   
	   String url="/index.jsp";
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(url);
		System.out.println("created rd");
		rd.forward(request, response); 	
		   
		   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
