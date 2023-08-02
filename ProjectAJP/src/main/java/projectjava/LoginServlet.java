package projectjava;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection con = null;
        PreparedStatement userStmt = null;
        ResultSet userRs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "digant", "digant123");

            String selectUserQuery = "SELECT * FROM users WHERE email = ? AND password = ?";
            userStmt = con.prepareStatement(selectUserQuery);
            userStmt.setString(1, email);
            userStmt.setString(2, password);
            userRs = userStmt.executeQuery();

            if (userRs.next()) {
                // User exists and credentials are valid
                int userId = userRs.getInt("user_id");
                request.setAttribute("userId", userId);

                // Redirect to the user questions page
//                RequestDispatcher rd = request.getRequestDispatcher("display");
                RequestDispatcher rd = request.getRequestDispatcher("display");
                rd.forward(request, response);
            } else {
                // Invalid credentials
                response.sendRedirect(request.getContextPath() + "/Loginunsuccessful.jsp");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception and display an error message
            request.setAttribute("errorMessage", "An error occurred during login.");
            RequestDispatcher rd = request.getRequestDispatcher("Error.jsp");
            rd.forward(request, response);
        } finally {
            // Close database resources in a finally block
            try {
                if (userRs != null)
                    userRs.close();
                if (userStmt != null)
                    userStmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
