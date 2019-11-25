

import java.beans.Statement;
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

/**
 * Servlet implementation class DetailsValidate
 */
@WebServlet("/DetailsValidate")
public class DetailsValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DetailsValidate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String p = (String) request.getParameter("name");
		String username = (String) request.getParameter("username");
		String bookid = (String) request.getParameter("bookid");
		PrintWriter out = response.getWriter();
		if(p.equals("fav")) {
			try {
				conn = DriverManager.getConnection("jdbc:mysql://google/boneseuk?cloudSqlInstance=airy-highlander-255321:us-central1:lab8&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=boneseuk&password=bona0324");
				ps = conn.prepareStatement("INSERT INTO books (username, bookid) VALUES (?,?)");
				ps.setString(1, username);
				ps.setString(2, bookid);
				ps.executeUpdate();
						
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
			
		else if(p.equals("remove")){
			try {
				conn = DriverManager.getConnection("jdbc:mysql://google/boneseuk?cloudSqlInstance=airy-highlander-255321:us-central1:lab8&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=boneseuk&password=bona0324");
				ps = conn.prepareStatement("DELETE FROM books WHERE username = ? AND bookid = ?");
				ps.setString(1, username);
				ps.setString(2, bookid);
				ps.executeUpdate();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		else {
			try {
				conn = DriverManager.getConnection("jdbc:mysql://google/boneseuk?cloudSqlInstance=airy-highlander-255321:us-central1:lab8&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=boneseuk&password=bona0324");
				ps = conn.prepareStatement("SELECT bookid FROM books WHERE username = ? AND bookid = ?");
				ps.setString(1, username);
				ps.setString(2, bookid);
				rs = ps.executeQuery();
				if(!rs.next())
					out.print("false");
				else
					out.print("true");
				
						
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			
			
			
		}
		out.flush();
		out.close();
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Details.jsp");
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
