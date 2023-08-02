package projectjava;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "digant", "digant123");

            String insertUserQuery = "INSERT INTO users (user_id, first_name, last_name, email, password) VALUES (user_id_seq.NEXTVAL, ?, ?, ?, ?)";
            PreparedStatement userStmt = con.prepareStatement(insertUserQuery);
            userStmt.setString(1, firstName);
            userStmt.setString(2, lastName);
            userStmt.setString(3, email);
            userStmt.setString(4, password);
            int i = userStmt.executeUpdate();

            if (i > 0) {
                // Get the generated user ID
                String selectUserIdQuery = "SELECT user_id_seq.CURRVAL FROM dual";
                PreparedStatement userIdStmt = con.prepareStatement(selectUserIdQuery);
                ResultSet userIdRs = userIdStmt.executeQuery();

                int userId = 0;
                if (userIdRs.next()) {
                    userId = userIdRs.getInt(1);
                }

                // Store the user ID in a session attribute to be used later
                request.getSession().setAttribute("userId", userId);

                out.print("<h1>" + i + " record(s) inserted</h1>");
                RequestDispatcher rd = request.getRequestDispatcher("Success.jsp");
                rd.forward(request, response);

                userIdRs.close();
                userIdStmt.close();
            }
            con.close();
        } catch (Exception e) {
            out.print(e);
            //response.sendRedirect("Error.jsp");
        }
    }
}
