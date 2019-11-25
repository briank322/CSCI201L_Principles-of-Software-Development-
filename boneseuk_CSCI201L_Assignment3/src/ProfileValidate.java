

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ProfileValidate
 */
@WebServlet("/ProfileValidate")
public class ProfileValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfileValidate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Entering Profile Servlet!!!!");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		//String p = (String) request.getParameter("name");
		String username = (String) request.getParameter("username");
		//System.out.println(username);
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://google/boneseuk?cloudSqlInstance=airy-highlander-255321:us-central1:lab8&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=boneseuk&password=bona0324");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT bookid FROM books WHERE books.username='" + username +"' ORDER BY boba");
			String a = "";
			while(rs.next()) {
				System.out.println(rs.getString("bookid"));
				a = a + rs.getString("bookid") + ",";
			}
			out.println(a);
			System.out.println(a);
			
					
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		out.flush();
		out.close();
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Profile.jsp");
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
