package web;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

		

	private static String generate(String input) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] msg = input.getBytes();

			// Update the message digest with some more bytes
			// This can be performed multiple times before creating the hash
			md.update(msg);

			// Create the digest from the message
			byte[] aMessageDigest = md.digest();
			return hexToString(aMessageDigest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       

		return null;

	}



	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	
	
	
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 ServletContext context = getServletContext();
		 Database db = context.getAttribute("database");
        // accountManager account = (accountManager) context.getAttribute("account");
         
		 
		 
		 String userName = request.getParameter("userName");
         String password = request.getParameter("password");
         
         /*
         String passcode = generate(password);
         
         if(db.passwordMatchesName(userName,passcode)) {
        	 
         }
         */
		 
		 User user = new User(userName, password);
         
         
	}

}
