/**
 * 
 */const container = document.querySelector(".container"),
      pwShowHide = document.querySelectorAll(".showHidePw"),
      pwFields = document.querySelectorAll(".password"),
      signUp = document.querySelector(".signup-link"),
      login = document.querySelector(".login-link");

    //   js code to show/hide password and change icon
    pwShowHide.forEach(eyeIcon =>{
        eyeIcon.addEventListener("click", ()=>{
            pwFields.forEach(pwField =>{
                if(pwField.type ==="password"){
                    pwField.type = "text";

                    pwShowHide.forEach(icon =>{
                        icon.classList.replace("uil-eye-slash", "uil-eye");
                    })
                }else{
                    pwField.type = "password";

                    pwShowHide.forEach(icon =>{
                        icon.classList.replace("uil-eye", "uil-eye-slash");
                    })
                }
            }) 
        })
    })

    // js code to appear signup and login form
    signUp.addEventListener("click", ( )=>{
        container.classList.add("active");
    });
    login.addEventListener("click", ( )=>{
        container.classList.remove("active");
    });
    
    
    
    document.getElementById("signupButton").addEventListener("click", function(event) {
    var form = document.getElementById("registrationForm");
    var inputs = form.querySelectorAll("input[required]");
    var isValid = true;
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].value.trim() === "") {
            isValid = false;
            break;
        }
    }
    if (!isValid) {
        alert("Please fill in all required fields.");
        event.preventDefault();
    }
});

