package projectjava;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/score")
public class UserScoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "digant", "digant123");

            String selectQuestionsQuery = "SELECT question_id, question_text FROM questions";
            pstmt = con.prepareStatement(selectQuestionsQuery);
            rs = pstmt.executeQuery();

//            out.print("<h1>Quiz Scores</h1>");
//            int totalQuestions = 0;
//            int score=0;
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Quiz Scores</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f2f2f2; }");
            out.println(".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
            out.println("h1 { color: #333; text-align: center; }");
            out.println("h2 { color: #555; }");
            out.println("p { margin-bottom: 10px; }");
            out.println("hr { border: none; border-top: 1px solid #ddd; margin: 20px 0; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class='container'>");
            out.println("<h1>Quiz Scores</h1>");

            int totalQuestions = 0;
            int score = 0;
            String result = "False";
            while (rs.next()) {
            	//score=0;
            	result="False";
            	totalQuestions++;
                int questionId = rs.getInt("question_id");
                String questionText = rs.getString("question_text");
                String selectedAnswer = request.getParameter("answer_" + questionId);

                String selectCorrectAnswerQuery = "SELECT correct_answer FROM questions WHERE question_id = ?";
                PreparedStatement correctAnswerStmt = con.prepareStatement(selectCorrectAnswerQuery);
                correctAnswerStmt.setInt(1, questionId);
                ResultSet correctAnswerRs = correctAnswerStmt.executeQuery();

                if (correctAnswerRs.next()) {
                    String correctAnswer = correctAnswerRs.getString("correct_answer");
                    //int score = selectedAnswer != null && selectedAnswer.equals(correctAnswer) ? 1 : 0;
                    if (selectedAnswer.equals(correctAnswer)) {
                      score++;
                      result = "True";
                  }
                    out.print("<h2>Question:</h2>");
                    out.print("<h4>" + questionText + "</h4>");
                    out.print("<p>Selected Answer: " + (selectedAnswer != null ? selectedAnswer : "") + "</p>");
                    out.print("<p>Correct Answer: " + correctAnswer + "</p>");
                    out.print("<p><b>Result: " + result + "</b></p>");
                    out.print("<hr>");
                }
//                out.print("<p>Score: " + score + "/" + totalQuestions + "</p>");
                correctAnswerRs.close();
                correctAnswerStmt.close();
            }
            //out.print("<h3>Score: " + score + "/" + totalQuestions + "</h3>");
            out.println("<div style='text-align: center;'>");
            out.println("<h3>Total Score: " + score + "/" + totalQuestions + "</h3>");
            String insertScoreQuery = "INSERT INTO user_scores (user_id, score) VALUES (?, ?)";
            PreparedStatement insertScoreStmt = con.prepareStatement(insertScoreQuery);
            insertScoreStmt.setInt(1, userId);
            insertScoreStmt.setInt(2, score);
            insertScoreStmt.executeUpdate();
            insertScoreStmt.close();
            out.println("</div>");

            out.println("<div style='text-align: center;'>");
            out.println("<a href='Homepage.html'><button style='padding: 10px 20px; background-color: #86316a; color: #fff;'>LOGOUT</button></a>");
            out.println("</div>");
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
