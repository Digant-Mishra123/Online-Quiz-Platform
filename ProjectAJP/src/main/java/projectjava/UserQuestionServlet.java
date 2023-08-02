package projectjava;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/display")
public class UserQuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int userId = (int) request.getAttribute("userId");
            System.out.println(userId);

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "digant", "digant123");

            String selectQuestionsQuery = "SELECT question_id, question_text FROM questions";
            pstmt = con.prepareStatement(selectQuestionsQuery);
            rs = pstmt.executeQuery();

            boolean hasQuestions = false;
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Quiz Questions</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f2f2f2; }");
            out.println(".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
            out.println("h1 { color: #333; text-align: center; }");
            out.println("h2 { color: #555; }");
            out.println("form { margin-top: 20px; }");
            out.println("label { display: block; margin-bottom: 10px; }");
            out.println("input[type='radio'] { margin-right: 5px; }");
            out.println("input[type='submit'] { background-color: #555; color: #fff; padding: 10px 20px; border: none; cursor: pointer; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class='container'>");
            out.println("<h1>Quiz Questions</h1>");
            out.println("<form method='post' action='score'>");
//            out.print("<h1>Quiz Questions</h1>");
//            out.print("<form method='post' action='score'>");

            while (rs.next()) {
                hasQuestions = true;

                int questionId = rs.getInt("question_id");
                String questionText = rs.getString("question_text");

                out.print("<h2>Question:</h2>");
                out.print("<p>" + questionText + "</p>");
                out.print("<input type='hidden' name='questionId' value='" + questionId + "'>");
                out.print("<input type='hidden' name='userId' value='" + userId + "'>"); // Pass the userId as a hidden input field

                // Retrieve the options for the current question from the database
                String selectOptionsQuery = "SELECT option_text FROM options WHERE question_id = ?";
                PreparedStatement optionsStmt = con.prepareStatement(selectOptionsQuery);
                optionsStmt.setInt(1, questionId);
                ResultSet optionsRs = optionsStmt.executeQuery();

                // Display the options
                while (optionsRs.next()) {
                    String optionText = optionsRs.getString("option_text");
                    out.print("<input type='radio' name='answer_" + questionId + "' value='" + optionText + "'> " + optionText + "<br>");
                }

                // Close the options ResultSet and statement
                optionsRs.close();
                optionsStmt.close();

                out.print("<hr>");
            }

            if (hasQuestions) {
//                // Display the submit button if there are questions
//                out.print("<input type='submit' value='Next'>");
//            } else {
//                // No questions found
//                out.print("<h1>No Questions Found</h1>");
//            }
//
//            out.print("</form>");
            	// Display the submit button if there are questions
                out.println("<div style='text-align: center;'>");
                out.println("<input type='submit' value='Next'>");
                out.println("</div>");
            } else {
                // No questions found
                out.println("<h1>No Questions Found</h1>");
            }

            out.println("</form>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception
            }
        }
    }
}




