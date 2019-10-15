

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Validation
 */
@WebServlet("/Validation")
public class Validation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Validation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String keyword = request.getParameter("keyword");
		String option = request.getParameter("option");
		String error ="";
		String error2 = "";
		String next = "/SearchResults.jsp";
		
		
		
		if(keyword.equals("")) {
			error += "Search Box is empty";
			request.setAttribute("keyworderror", keyword);
			next = "/HomePage.jsp";
		}
		
		
		if(option == null) {
			
			option = "";
		}
//		if(option!=null) {
//			
//		
//			if(option.equals("Author")) {
//				//search for author
//				
//				
//			}
//			
//			else if(option.equals("ISBN")) {
//				
//				
//			}
//			
//			else if(option.equals("Publisher")) {
//				
//				
//			}
//			
//		}else {
//				error2 += "Choose at least one option.";
//				request.setAttribute("optionerror", option);
//				next = "/HomePage.jsp";
//			
//		}
//		
		request.setAttribute("error", error);
		request.setAttribute("error2", error2);
		request.setAttribute("keyword", keyword);
		request.setAttribute("option", option);
		
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
		try {
			dispatch.forward(request, response);
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ServletException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
