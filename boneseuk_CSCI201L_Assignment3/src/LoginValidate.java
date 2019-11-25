

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class LoginValidate
 */
@WebServlet("/LoginValidate")
public class LoginValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginValidate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("Entering Login Servlet!");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		PrintWriter out = response.getWriter();

		if(username.equals("")) {
			//request.setAttribute("unError", "Please enter username!");
			System.out.println("Please enter username!");
			out.println("Please enter username!");
			//next = "/Register.jsp";
		}
		
		if(password.equals("")){
			//request.setAttribute("pwError", "Please enter password!");
			System.out.println("Please enter password!");
			out.println("Please enter password!");
			//next = "/Register.jsp";
		}
		
		else {
			//PrintWriter out = response.getWriter();
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			//SQL queries
			//Gets all rows from PageVisited
			//String searchString = "SELECT * FROM login WHERE username = ? AND password = ?";
			
			
			
			try {
				conn = DriverManager.getConnection("jdbc:mysql://google/boneseuk?cloudSqlInstance=airy-highlander-255321:us-central1:lab8&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=boneseuk&password=bona0324");
				ps = conn.prepareStatement("SELECT * FROM login WHERE username = ?");
				ps.setString(1, username);
				rs = ps.executeQuery();
				boolean validuser = false;
				boolean validpw = false;
				if(rs.next()) {
					//out.println("Username is in the database");
					System.out.println("Username is in the database");
					validuser = true;
					//next = "/Register.jsp";
					//out.println("This username is already taken.");
				}
				else {
					out.println("This user does not exist.");
				}
				
				if(validuser) {
					ps = conn.prepareStatement("SELECT * FROM login WHERE username = ? AND password = ?");
					ps.setString(1, username);
					ps.setString(2, password);
					rs = ps.executeQuery();
					if(rs.next()) {
						//out.println("Password is in the database");
						validpw = true;
						//out.println("Successful login!!");
					}
					else {
						out.println("Incorrect Password");
					}
				}
				
				
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			
		} 
		
		//request.setAttribute("confirmpassword", confirmpassword);
		request.setAttribute("username", username);
		request.setAttribute("password", password);
//		request.setAttribute("nouser",nouser);
//		request.setAttribute("nopw",nopw);
//		request.setAttribute("pwerror",pwerror);
		out.flush();
		out.close();
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Register.jsp");
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
