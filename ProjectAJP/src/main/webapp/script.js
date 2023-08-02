var x = document.getElementById("login");
var y = document.getElementById("register");
var z  = document.getElementById("btn");

function register(){
    x.style.left = "-400px";
    y.style.left = "50px";
    z.style.left = "110px";
}

function login(){
    x.style.left = "50px";
    y.style.left = "450px";
    z.style.left = "0px";
}
function validateLoginForm() {
    const email = document.forms["login"]["email"].value;
    const password = document.forms["login"]["password"].value;
    if (email.trim() === "" || password.trim() === "") {
      alert("Both email and password fields must be filled out.");
      return false;
    }
    return true;
}
  

  function validateRegistrationForm() {
    var namevalidate = validateName();
    var emailvalidate = validateEmail();
    var passvalidate = validatePassword();
  
    if(namevalidate == false || emailvalidate == false || passvalidate == false) {
        //alert("Fields cannot be blank");
        return false;
    }
    return true;
}
  
function validateName(){
    var firstName = document.forms["register"]["first_name"].value;
    var lastName = document.forms["register"]["last_name"].value;
    if(firstName == null || firstName == "" || lastName == null || lastName == "") {
        alert("Name cannot be blank");
        return false;
    }
    else if(!/^[a-zA-Z]+$/.test(firstName) || !/^[a-zA-Z]+$/.test(lastName)) {
        alert("Enter valid characters only");
        return false;
    }
    return true;
}

  
function validateEmail() {
    var email = document.forms["register"]["email"].value;
    if(email == null || email == "") {
        alert("Email field cannot be empty");
        return false;
    }
    return true;
}
  
function validatePassword() {
    var pass = document.forms["register"]["password"].value;
    var confirmpass = document.forms["register"]["confirm_password"].value;
  
    // check length
    if (pass.length < 8) {
        alert("Password must be at least 8 characters long");
        return false;
    }
  
    // check for uppercase, lowercase, and number
    var hasUpperCase = /[A-Z]/.test(pass);
    var hasLowerCase = /[a-z]/.test(pass);
    var hasNumber = /\d/.test(pass);
  
    if (!hasUpperCase || !hasLowerCase || !hasNumber) {
        alert("Password must contain at least one uppercase letter, one lowercase letter, and one number");
        return false;
    }
  
    // check if password and confirm password fields match
    if (pass !== confirmpass) {
        alert("Passwords do not match");
        return false;
    }
  
    // if we get here, the password is valid
    return true;
}

