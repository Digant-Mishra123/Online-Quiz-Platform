<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Regsitration</title>
<script>
      setTimeout(function() {
         // Display loading animation or message
         document.getElementById("loading").style.display = "block";

         // Redirect to loginpage.html after a certain delay
         setTimeout(function() {
            window.location.href = "Loginpage.html";
         }, 2000); // Delay of 2 seconds (adjust as needed)
      }, 2000); // Initial delay of 3 seconds before showing loading and redirecting
   </script>
</head>
<body>
 <h1>Registration Successful</h1>
    <p>Thank you for registering!</p>
    <h3>Redirecting to login page.......</h3>
    <div id="loading" style="display: none;">
      
      Loading...
   </div>
</body>
</html>